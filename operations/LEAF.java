package cp2024.solution;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.LeafNode;

import java.util.concurrent.BlockingQueue;

// Runnable class for a thread handling a leaf node.
public class LEAF implements Runnable{
    private LeafNode leaf;
    private BlockingQueue<Boolean> resultQueue;

    public LEAF(LeafNode leaf, BlockingQueue<Boolean> resultQueue) {
        this.leaf = leaf;
        this.resultQueue = resultQueue;
    }


    @Override
    public void run() {
        try {
            resultQueue.put(leaf.getValue());
        } catch (InterruptedException ignored) {
        }
    }
}
