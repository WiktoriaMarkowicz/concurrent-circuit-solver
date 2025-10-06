package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.ThresholdNode;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Runnable class for a thread performing GT operations.
public class GT implements Runnable {
    private ThresholdNode node; // Currently processed node.
    private BlockingQueue<Boolean> resultQueue; // Queue to which the result of the GT operation is sent.
    private BlockingQueue<Boolean> childQueue; // Queue storing the results of the node's child nodes.
    private ArrayList<Thread> children; //  List of the node's child nodes.

    public GT(ThresholdNode node, BlockingQueue<Boolean> resultQueue) {
        this.node = node;
        this.resultQueue = resultQueue;
        this.children = new ArrayList<>();
        this.childQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        int counter = 0;
        try {
            // Creating threads for each of the node's child nodes.
            for (CircuitNode n : node.getArgs()) {
                Thread t = new Thread(TaskManager.manage(n, childQueue));
                t.start();
                children.add(t);
            }

            counter = node.getArgs().length;
            Boolean result = null;
            int trues = 0;
            int index = 0;
            while(index < counter) {
                result = childQueue.take();
                if (result) {
                    trues++;
                }
                if (trues > node.getThreshold()) {
                    break;
                }
                index++;
            }

            // Determining the result of the operation.
            if (trues > node.getThreshold())
                resultQueue.put(true);
            else resultQueue.put(false);

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
