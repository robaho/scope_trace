import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class App {
    private static final Random RANDOM = new Random();

    public void openCloseScope() {
        var scope = TraceContext.get().open("openCloseScope");

        callServer();
        
        scope.close();
    }

    public void callServer() {
        // the try-with-resources would be emitted by byte code injection
        try(final var scope = TraceContext.get().open("callServer")) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(RANDOM.nextInt(100)));
        }
    }

    public void handleRequest() {
        // the try-with-resources would be emitted by byte code injection
        try(final var scope = TraceContext.get().open("handleRequest")) {
            openCloseScope();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        App app = new App();
        ExecutorService server = Executors.newVirtualThreadPerTaskExecutor();
        for(int i=0;i<1000;i++) {
            // this is the only change required to setup the context at the start of a request
            server.execute(TraceContext.traceRequest(i,() ->  app.handleRequest()));
        }
        server.shutdown();
        server.awaitTermination(30, TimeUnit.SECONDS);
    }
}
