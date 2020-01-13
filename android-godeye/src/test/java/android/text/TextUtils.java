package android.text;

public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }
}
