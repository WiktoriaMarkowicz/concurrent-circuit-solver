package cp2024.solution;

import cp2024.circuit.CircuitNode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Runnable class for a thread performing NOT operations.
public class NOT implements Runnable {
    private CircuitNode node;
    private BlockingQueue<Boolean> resultQueue;
    private BlockingQueue<Boolean> childQueue;

    public NOT(CircuitNode node, BlockingQueue<Boolean> resultQueue) {
        this.node = node;
        this.resultQueue = resultQueue;
        this.childQueue = new LinkedBlockingQueue<Boolean>();
    }

    @Override
    public void run() {
        Thread t = null;
        try {
            // Creating threads for each of the node's child nodes.
            t = new Thread(TaskManager.manage(node.getArgs()[0], childQueue));
            t.start();

            // Waiting for the child node's result.
            Boolean result = childQueue.take();

            // Passing the result to the parent's queue.
            resultQueue.put(!result);

            // Waiting for the child node.
            t.join();
        } catch (InterruptedException e) {
            try {
                if (t != null) {
                    t.interrupt();
                    t.join();
                }
            } catch (InterruptedException f) {

            }
        }



    }
}
