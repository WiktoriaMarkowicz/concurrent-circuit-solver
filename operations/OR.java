package cp2024.solution;

import cp2024.circuit.CircuitNode;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Runnable class for a thread performing OR operations.
public class OR implements Runnable {
    private CircuitNode node;
    private BlockingQueue<Boolean> resultQueue;
    private BlockingQueue<Boolean> childQueue;
    private ArrayList<Thread> children;

    public OR(CircuitNode node, BlockingQueue<Boolean> resultQueue) {
        this.node = node;
        this.resultQueue = resultQueue;
        this.childQueue = new LinkedBlockingQueue<Boolean>();
        this.children = new ArrayList<>();
    }
    @Override
    public void run() {
        try {
            // Creating threads for each of the node's child nodes.
            for (CircuitNode n : node.getArgs()) {
                Thread t = new Thread(TaskManager.manage(n, childQueue));
                t.start();
                children.add(t);
            }

            // Determining the result of the OR operation.
            int counter = node.getArgs().length;
            int index = 0;
            Boolean result = null;
            while (index < counter) {
                    result = childQueue.take();
                if (result) {
                    break;
                }
                index++;
            }
            resultQueue.put(result);

            // If the expression was computed before all child nodes finished, we interrupt the remaining ones.
            if (index < counter - 1) {
                for(Thread t : children) {
                    t.interrupt();
                }
            }

            // Waiting for all child nodes.
            for (Thread t : children) {
                t.join();
            }

        } catch (InterruptedException e) {
            try {
                for (Thread t : children) {
                    t.interrupt();
                }

                for (Thread t : children) {
                    t.join();
                }
            } catch (InterruptedException ignored) {

            }
        }




    }
}
