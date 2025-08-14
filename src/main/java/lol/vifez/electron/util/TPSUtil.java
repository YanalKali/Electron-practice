package lol.vifez.electron.util;

public class TPSUtil {

    private static long lastTime = System.nanoTime();
    private static long tickCount = 0;
    private static double tps = 20.0;

    private static final int SAMPLE_INTERVAL = 100;

    public static void tick() {
        tickCount++;
        long currentTime = System.nanoTime();

        if (currentTime - lastTime >= 1_000_000_000L) {
            tps = tickCount / ((currentTime - lastTime) / 1_000_000_000.0);
            tickCount = 0;
            lastTime = currentTime;
        }
    }

    public static double getTPS() {
        return tps;
    }
}