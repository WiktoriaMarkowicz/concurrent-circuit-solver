package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.ThresholdNode;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Runnable class for a thread performing LT operations.
public class LT implements Runnable {
    private ThresholdNode node;
    private BlockingQueue<Boolean> resultQueue;
    private BlockingQueue<Boolean> childQueue;
    private ArrayList<Thread> children;

    public LT(ThresholdNode node, BlockingQueue<Boolean> resultQueue) {
        this.node = node;
        this.resultQueue = resultQueue;
        this.children = new ArrayList<>();
        this.childQueue = new LinkedBlockingQueue<>();
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

            int counter = node.getArgs().length;
            Boolean result = null;
            int trues = 0;
            int index = 0;
            while (index < counter) {
                result = childQueue.take();
                if (result) {
                    trues++;
                    if (trues >= node.getThreshold()) {
                        break;
                    }
                }
                index++;
            }

            if (trues >= node.getThreshold()) {
                resultQueue.put(false);
            } else {
                resultQueue.put(true);
            }

            // If the expression was computed before all child nodes finished, we interrupt the remaining ones.
            if (index < counter - 1) {
                for (Thread t : children) {
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
            } catch (InterruptedException f) {

            }
        }


    }
}
