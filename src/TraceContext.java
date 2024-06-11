import java.util.LinkedList;
import java.util.List;

public class TraceContext {
    private static final ScopedValue<TraceContext> context = ScopedValue.newInstance();
    
    private final List<Event> events = new LinkedList();
    private final long requestId;

    private TraceContext(long requestId) {
        this.requestId = requestId;
    }

    public Timer time(String method) {
        return new Timer(method);
    }
    public class Timer implements AutoCloseable {
        private final long start = System.currentTimeMillis();
        private final String method;
        private Timer(String method) {
            this.method = method;
        }
        @Override
        public void close() {
            events.add(new Event(start,System.currentTimeMillis(),"method "+method));
        }
    }
    public record Event(long start,long end,String description){}
    public void dump() {
        events.stream().forEach(e -> System.out.println("request "+requestId+", event "+e+", duration "+(e.end-e.start)+"ms"));
    }
    public static TraceContext get() {
        return context.get();
    }
    public static Runnable traceRequest(long requestId,final Runnable r) {
        return () -> {
            ScopedValue.runWhere(context,new TraceContext(requestId),() -> {
                r.run();
                TraceContext.get().dump();
            });
        };
    }
}