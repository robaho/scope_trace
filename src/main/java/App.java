
import java.util.Random;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class App {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException, Exception {

        var app = new App();

        for(int i=0;i<1;i++) {
            app.handleRequest(i);
        }
    }

    private void handleRequest(int requestId) throws Exception {
        try (var scope = new TracingTaskScope<Object>(Integer.toString(requestId))) {
            scope.context.event("request starting");

            Subtask<Integer> subtask1 = scope.fork(() -> { callJdbc("table1"); return 0; });
            Subtask<Integer> subtask3 = scope.fork(() -> { 
                try(var inner = new TracingTaskScope<Object>(Integer.toString(requestId)+".subtask")) {
                    Subtask<Integer> subtask4 = inner.fork(() -> { callJdbc("table2"); return 0; });
                    Subtask<Integer> subtask5 = inner.fork(() -> { callService(); return 0; });
                    inner.join();
                }
                return 0;
            });
            scope.join();

            scope.context.event("request cleanup");
        } // close
    }

    private void beforeStatement() {
       var span = TraceContext.get().open("jdbc call");
       span.tags.add("jdbc");
    }

    private void afterStatement() {
        TraceContext.get().current().close();
    }

    public void callJdbc(String table) {
        beforeStatement();
        // do jdbc call
        TraceContext.get().current().tags.add(table);
        TraceContext.get().event("call jdbc");
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(RANDOM.nextInt(10)));
        afterStatement();
    }
    private void callService() {
        try(var span = TraceContext.get().open("call service")) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(RANDOM.nextInt(100)));
        }
    }
}

