package cp2024.solution;

import cp2024.circuit.CircuitSolver;
import cp2024.circuit.CircuitValue;
import cp2024.circuit.Circuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelCircuitSolver implements CircuitSolver {
    // Variable storing information about whether computations can continue.
    private AtomicInteger acceptComputations = new AtomicInteger(0);
    // List storing all ParallelCircuitValue objects that this solver is tasked to solve.
    private List<ParallelCircuitValue> circuits = Collections.synchronizedList(new ArrayList<>());

    @Override
    public synchronized CircuitValue solve(Circuit c)  {
        try {
            // If new computations cannot be performed, return a BrokenCircuitValue object.
            if (acceptComputations.get() != 0)
                return new BrokenCircuitValue();
            // Otherwise, immediately return a ParallelCircuitValue object.
            return new ParallelCircuitValue(circuits, c, acceptComputations);
        } catch (Exception e) {
            return new BrokenCircuitValue();
        }

    }

    @Override
    public synchronized void stop() {
        // Change the variable value to stop accepting new tasks.
        acceptComputations.set(1);
        // Call the stop method for each object that this solver was tasked to compute.
        for (ParallelCircuitValue c : circuits) {
            c.stop();
        }
    }
}
