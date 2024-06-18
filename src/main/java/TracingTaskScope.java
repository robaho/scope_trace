
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;

public class TracingTaskScope<T> extends StructuredTaskScope<T> {
    public final TraceContext context;
    private final TraceContext.Span span;
    public TracingTaskScope(String requestId){
        context = Optional.ofNullable(TraceContext.get()).map(c -> new TraceContext(c)).orElse(new TraceContext());
        span = context.open("request "+requestId);
    }
    @Override
    public <U extends T> Subtask<U> fork(Callable<? extends U> task) {
        Callable<? extends U> callable = context.trace(task);
        return super.fork(callable);
    }
    @Override
    public void close() {
        span.close();
        context.dump();
        super.close();
    }
}