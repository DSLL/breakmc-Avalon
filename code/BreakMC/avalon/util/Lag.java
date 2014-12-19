package code.BreakMC.avalon.util;

public class Lag implements Runnable
{
    public static int TICK_COUNT;
    public static long[] TICKS;
    public static long LAST_TICK;
    
    static {
        Lag.TICK_COUNT = 0;
        Lag.TICKS = new long[600];
        Lag.LAST_TICK = 0L;
    }
    
    public static double getTPS() {
        return getTPS(100);
    }
    
    public static double getTPS(final int ticks) {
        if (Lag.TICK_COUNT < ticks) {
            return 20.0;
        }
        final int target = (Lag.TICK_COUNT - 1 - ticks) % Lag.TICKS.length;
        final long elapsed = System.currentTimeMillis() - Lag.TICKS[target];
        return ticks / (elapsed / 1000.0);
    }
    
    public static long getElapsed(final int tickID) {
        final int length = Lag.TICKS.length;
        final long time = Lag.TICKS[tickID % Lag.TICKS.length];
        return System.currentTimeMillis() - time;
    }
    
    @Override
    public void run() {
        Lag.TICKS[Lag.TICK_COUNT % Lag.TICKS.length] = System.currentTimeMillis();
        ++Lag.TICK_COUNT;
    }
}
