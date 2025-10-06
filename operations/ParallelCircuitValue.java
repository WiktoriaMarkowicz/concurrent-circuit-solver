package cp2024.solution;

import cp2024.circuit.Circuit;
import cp2024.circuit.CircuitValue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelCircuitValue implements CircuitValue {
    private List<ParallelCircuitValue> circuitList;
    private Boolean readyValue;
    private static Circuit circuit;
    private Boolean Value;
    private Boolean stopped;
    private Thread t;
    private AtomicInteger acceptComputations;

    public ParallelCircuitValue(List<ParallelCircuitValue> circuitList, Circuit circuit, AtomicInteger acceptComputations) throws InterruptedException {
        this.circuitList = circuitList;
        this.readyValue = false;
        this.circuit = circuit;
        this.Value = null;
        this.stopped = false;
        compute();
        this.acceptComputations = acceptComputations;
    }

    public void compute() throws InterruptedException {
        // Creating a thread to start the computation.
        t  = new Thread(new StartingThread(circuit, this));
        t.start();
        // Adding this object to the list of ParallelCircuitValue objects managed by the solver.
        circuitList.add(this);
    }

    @Override
    public boolean getValue() throws InterruptedException {
        if (readyValue) {
            return Value;
        } else {
            if (stopped) {
                return new BrokenCircuitValue().getValue();
            } else {
                t.join();
                return Value;
            }
        }
    }

    public void setValue(boolean val) {
        Value = val;
        readyValue = true;
    }


    public void stop()  {
        try {
            stopped = true;
            if (readyValue == false) {
                t.interrupt();
                t.join();
            }
        } catch (InterruptedException e) {

        }
    }
}
