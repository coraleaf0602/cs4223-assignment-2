package timer;

public class Timer {
    private int cycles;

    public Timer() {
        this.cycles = 0;
    }

    // Method to simulate passing of one cycle
    public void tick() {
        this.cycles++;
    }

    // Increment the timer by a specific number of cycles
    public void addCycles(int cycles) {
        this.cycles += cycles;
    }

    // Method to get the current cycle count
    public int getCurrentCycle() {
        return this.cycles;
    }
}
