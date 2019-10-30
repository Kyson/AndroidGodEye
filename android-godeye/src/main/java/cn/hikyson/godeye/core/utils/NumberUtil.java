package cn.hikyson.godeye.core.utils;

public class NumberUtil {

    public static double range(double num, double max, double min) {
        if (max < min) {
            return num;
        }
        if (num > max) {
            return max;
        }
        if (num < min) {
            return min;
        }
        return num;
    }
}
