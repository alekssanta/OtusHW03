package utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    public static int getRandomNum(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
