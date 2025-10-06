package cp2024.solution;

import cp2024.circuit.CircuitNode;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Runnable class for a thread performing IF operations.
public class IF implements Runnable {
    private CircuitNode node; // Currently processed node.
    private BlockingQueue<Boolean> resultQueue;
    private BlockingQueue<Boolean> childQueue;
    private ArrayList<Thread> children;

    public IF(CircuitNode node, BlockingQueue<Boolean> resultQueue) {
        this.node = node;
        this.resultQueue = resultQueue;
        this.children = new ArrayList<>();
        this.childQueue = new LinkedBlockingQueue<Boolean>();
    }

    @Override
    public void run() {
        Boolean result = null;
        try {
            // Creating a thread for `a`.
            Thread a = new Thread(TaskManager.manage(node.getArgs()[0], childQueue));
            a.start();
            children.add(a);
            result = childQueue.take();
            // Depending on the result, we choose either `b` or `c`.
            if (result) {
                Thread b = new Thread(TaskManager.manage(node.getArgs()[1], childQueue));
                b.start();
                children.add(b);
                result = childQueue.take();
            } else {
                Thread c = new Thread(TaskManager.manage(node.getArgs()[2], childQueue));
                c.start();
                children.add(c);
                result = childQueue.take();
            }

            // Placing the result into the result queue.
            resultQueue.put(result);

            // Waiting for the child nodes.
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
