import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class TraceContext {
    private static final ScopedValue<TraceContext> context = ScopedValue.newInstance();
    private static final ThreadLocal<TraceContext> local = new ThreadLocal<>() {
        @Override
        protected TraceContext initialValue() {
            return new TraceContext(Thread.currentThread().threadId());
        }
    };
    
    private final List<Event> events = new LinkedList();
    private final long requestId;
    private final long start = System.currentTimeMillis();
    private final static AtomicLong nextSpanId = new AtomicLong();
    private Span currentSpan;
    private final Deque<Span> stack = new ArrayDeque<>();

    private TraceContext(long requestId) {
        this.requestId = requestId;
    }
    public Span open(String method) {
        return new Span(method);
    }
    public Span next(String method) {
        Span span = new Span(method);
        stack.push(span);
        return span;
    }
    public Span remove() {
        return stack.pop();
    }
    public Span current() {
        return currentSpan;
    }
    public long parentSpanId() {
        return currentSpan==null ? -1 : currentSpan.parentSpan!=null ? currentSpan.parentSpan.spanId() : -1;
    }
    void event(String description) {
        final long now = System.currentTimeMillis();
        events.add(new Event(now,now,description,current().spanId,currentSpan.parentSpan.spanId()));
    }
    public class Span implements AutoCloseable {
        private final long start = System.currentTimeMillis();
        private final String method;
        private final Span parentSpan = currentSpan;
        private final long spanId = nextSpanId.incrementAndGet();
        public final Set<String> tags = new LinkedHashSet<>();
        private Span(String method) {
            this.method = method;
            events.add(new Event(start,System.currentTimeMillis(),"open span "+method,spanId,parentSpanId()));
            currentSpan = this;
        }
        @Override
        public void close() {
            events.add(new Event(start,System.currentTimeMillis(),"close span "+method,spanId,parentSpanId()));
            currentSpan = parentSpan;
        }
        public long spanId() {
            return spanId;
        }
    }
    public record Event(long start,long end,String description,long spanId,long parentSpanId){}
    public void dump() {
        long end = System.currentTimeMillis();
        events.stream().forEach(e -> System.out.println("request "+requestId+", event "+e+", duration "+(e.end-e.start)+"ms"));
        System.out.println("request "+requestId+", total duration "+(end-start)+"ms");
    }
    public static TraceContext get() {
        return context.orElse(local.get());
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