package be.zlz.kara.bin.util;

public class SizeUtil {

    public static String autoScale(long bytes) {
        String[] prefix = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        double val = bytes;
        int cnt = 0;
        while (val > 1024) {
            val = val / 1024.0;
            cnt++;
        }
        return cnt <= prefix.length - 1 ? Math.round(val * 100.0) / 100.0 + prefix[cnt] : String.valueOf(bytes);
    }

}
