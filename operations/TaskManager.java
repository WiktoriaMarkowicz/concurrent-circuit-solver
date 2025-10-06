package cp2024.solution;

import cp2024.circuit.*;

import java.util.concurrent.BlockingQueue;

// Helper class for creating appropriate threads for a node's child nodes.
public class TaskManager {

    // Method that creates the appropriate thread depending on the node type.
    public static Thread manage(CircuitNode node, BlockingQueue<Boolean> queue) {
        return switch (node.getType()) {
            case IF -> new Thread(new IF(node, queue));
            case AND -> new Thread(new AND(node, queue));
            case OR -> new Thread(new OR(node, queue));
            case GT -> new Thread(new GT((ThresholdNode) node, queue));
            case LT -> new Thread(new LT((ThresholdNode) node, queue));
            case NOT -> new Thread(new NOT(node, queue));
            case LEAF -> new Thread(new LEAF((LeafNode) node, queue));
            default -> throw new RuntimeException("Illegal type " + node.getType());
        };
    }
}
