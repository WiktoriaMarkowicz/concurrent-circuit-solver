package cp2024.solution;

import cp2024.circuit.Circuit;
import cp2024.circuit.CircuitNode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class StartingThread implements Runnable{
    private Circuit circuit;
    private BlockingQueue<Boolean> childQueue;
    private ParallelCircuitValue p;

    public StartingThread(Circuit c, ParallelCircuitValue p) {
        this.circuit = c;
        this.childQueue = new LinkedBlockingQueue<>();
        this.p = p;
    }

    @Override
    public void run() {
        Thread t = null;
        try {
            // Creating a thread to handle the root node.
            CircuitNode n = circuit.getRoot();
            t = new Thread(TaskManager.manage(n, childQueue));
            t.start();
            // Waiting for the result.
            boolean result = childQueue.take();
            // Setting the result value in the ParallelCircuitValue object.
            p.setValue(result);

            // Waiting for the thread.
            t.join();
        } catch (InterruptedException e) {
            try {
                t.interrupt();
                t.join();
            } catch (InterruptedException f) {

            }
        }


    }
}
