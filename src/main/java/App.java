import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class App {
    private static final Random RANDOM = new Random();

    private void beforeStatement() {
       var span = TraceContext.get().next("making jdbc call");
       span.tags.add("jdbc");
    }

    private void afterStatement() {
        TraceContext.get().remove().close();
    }

    public void callJdbc() {
        beforeStatement();
        // do jdbc call
        TraceContext.get().event("call jdbc");
        afterStatement();
    }

    public void callServer() {
        // the try-with-resources would be emitted by byte code injection
        try(final var scope = TraceContext.get().open("callServer")) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(RANDOM.nextInt(100)));
        }
    }
    public void callServer2() {
        // the try-with-resources would be emitted by byte code injection
        try(final var scope = TraceContext.get().open("callServer2")) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(RANDOM.nextInt(100)));
        }
    }

    public void handleRequest(Request request) {
        // the try-with-resources would be emitted by byte code injection
        try(final var scope = TraceContext.get().open("handleRequest")) {
            callServer();
            callServer2();
            callJdbc();
        }
    }

    record Request(long id){}

    public static void main(String[] args) throws InterruptedException {
        App app = new App();
        ExecutorService server = Executors.newVirtualThreadPerTaskExecutor();
        for(int i=0;i<1000;i++) {
            final long id = i;
            // this is the only change required to setup the context at the start of a request
            server.execute(TraceContext.traceRequest(id,() ->  app.handleRequest(new Request(id))));
        }
        for(int i=0;i<1000;i++) {
            final long id = i;
            // this is the only change required to setup the context at the start of a request
            server.execute(() ->  app.handleRequest(new Request(id)));
        }
        server.shutdown();
        server.awaitTermination(30, TimeUnit.SECONDS);
    }
}
