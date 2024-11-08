package src.helper;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.concurrent.*;

public class CancelableReader extends BufferedReader {

    private final ExecutorService executor;
    private Future future;

    public CancelableReader(Reader in) {
        super(in);
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public String readLine() {

        future = executor.submit(super::readLine);

        try {
            return (String) future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Stop read due to: " + e );
        } catch (CancellationException e) {
            return null;
        }

        return null;

    }

    public boolean cancelRead() {
        return future.cancel(true);
    }

}