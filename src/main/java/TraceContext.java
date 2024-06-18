import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class TraceContext {

    private static final ScopedValue<TraceContext> context = ScopedValue.newInstance();

    <U> Callable<? extends U> trace(Callable<? extends U> task) {
        return () -> ScopedValue.callWhere(context,new TraceContext(this), task);
    }
    
    private final Queue<Event> events;
    private final static AtomicLong nextSpanId = new AtomicLong();
    private final Deque<Span> stack = new ArrayDeque<>();
    private final TraceContext parent;
    private final long parentSpanId;

    public TraceContext() {
        this.events = new ConcurrentLinkedQueue<>();
        this.parent=null;
        this.parentSpanId = -1;
    }
    public TraceContext(TraceContext parent) {
        this.parent = parent;
        this.parentSpanId = parent.current().spanId;
        this.events = parent.events;
    }
    /** open a new span with a parent of the current span */
    public Span open(String method) {
        Span span = new Span(method);
        stack.push(span);
        return span;
    }
    private void finish() {
        Span span = stack.pop();
        span.closeEvent();
    }
    /** obtain a reference to the current span */
    public Span current() {
        return Optional.ofNullable(stack.peek()).orElse(parent!=null ? parent.current():null);
    }
    void event(String description) {
        final long now = System.currentTimeMillis();
        events.add(new Event(now,now,current(),description));
    }
    public class Span implements AutoCloseable {
        private final long start = System.currentTimeMillis();
        private final String method;
        private final Span parentSpan = current();
        private final long spanId = nextSpanId.incrementAndGet();
        public final Set<String> tags = new LinkedHashSet<>();
        private Span(String method) {
            this.method = method;
            events.add(new Event(start,System.currentTimeMillis(),this,"open "+method));
        }
        private void closeEvent() {
            events.add(new Event(start,System.currentTimeMillis(),this,"close "+method));
        }
        @Override
        public void close() {
            TraceContext.this.finish();
        }
        public long spanId() {
            return spanId;
        }
        private long parentSpanId() {
            return parentSpan==null ? parentSpanId : parentSpan.spanId();
        }
        @Override
        public String toString() {
            return "span "+spanId+", tags "+tags+", parent "+parentSpanId();
        }
    }
    public record Event(long start,long end,Span span,String description){
        @Override
        public String toString() {
            return "Event[@"+start+" for "+(end-start)+"ms, "+span+", \""+description+"\"";
        }
    }
    public void dump() {
        events.stream().forEach(e -> System.out.println(e.toString()));
    }
    public static TraceContext get() {
        return context.orElse(null);
    }
}

