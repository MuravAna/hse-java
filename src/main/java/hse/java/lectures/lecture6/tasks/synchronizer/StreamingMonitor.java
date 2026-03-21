package hse.java.lectures.lecture6.tasks.synchronizer;

public class StreamingMonitor {

    private final int[] sortedIds;
    private final int targetTicks;
    private int currentIdx = 0;
    private int completedTicks = 0;

    public StreamingMonitor(int[] sortedIds, int targetTicks) {
        this.sortedIds = sortedIds;
        this.targetTicks = targetTicks;
    }

    public synchronized boolean waitTurn(int id) {
        try {
            while (completedTicks < targetTicks && sortedIds[currentIdx] != id) {
                wait();
            }
            return completedTicks < targetTicks;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public synchronized void tickDone() {
        currentIdx++;
        if (currentIdx == sortedIds.length) {
            currentIdx = 0;
            completedTicks++;
        }
        notifyAll();
    }

    public synchronized void awaitDone() {
        try {
            while (completedTicks < targetTicks) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}