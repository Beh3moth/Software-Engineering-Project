package it.polimi.ngsw.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * This class is used to read the input stream and making the input kind of interruptible.
 */
public class InputReadTask implements Callable<String> {
    private final BufferedReader br;
    public InputReadTask() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }
    @Override
    public String call() throws IOException, InterruptedException {
        String input;
        while (!br.ready()) {Thread.sleep(200);}
        input = br.readLine();
        return input;
    }
}
