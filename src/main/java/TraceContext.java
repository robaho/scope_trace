import java.util.LinkedList;
import java.util.List;

public class TraceContext {
    private static final ScopedValue<TraceContext> context = ScopedValue.newInstance();
    
    private final List<Event> events = new LinkedList();
    private final long requestId;
    private final long start = System.currentTimeMillis();

    private TraceContext(long requestId) {
        this.requestId = requestId;
    }

    public Scope open(String method) {
        return new Scope(method);
    }
    public class Scope implements AutoCloseable {
        private final long start = System.currentTimeMillis();
        private final String method;
        private Scope(String method) {
            this.method = method;
            events.add(new Event(start,System.currentTimeMillis(),"open method "+method));
        }
        @Override
        public void close() {
            events.add(new Event(start,System.currentTimeMillis(),"close method "+method));
        }
    }
    public record Event(long start,long end,String description){}
    public void dump() {
        long end = System.currentTimeMillis();
        events.stream().forEach(e -> System.out.println("request "+requestId+", event "+e+", duration "+(e.end-e.start)+"ms"));
        System.out.println("request "+requestId+", total duration "+(end-start)+"ms");
    }
    public static TraceContext get() {
        return context.get();
    }
    public static Runnable traceRequest(long requestId,final Runnable r) {
        final TraceContext _context = new TraceContext(requestId);
        return () -> {
            ScopedValue.runWhere(context,_context,() -> {
                r.run();
                TraceContext.get().dump();
            });
        };
    }
}