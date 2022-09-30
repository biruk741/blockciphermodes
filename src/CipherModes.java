import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CipherModes {
    public static List<Integer> getBinaryOfString(String s) {
        byte[] bytes = s.getBytes();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            int val = bytes[i];
            for (int j = 0; j < 8; j++) {
                result.add((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            result.remove(i * 7); // todo: fix this or something
        }
        return result;
    }

    public static List<Integer> EK(List<Integer> plainText, List<Integer> key) {
        List<Integer> result = new ArrayList<>();

        Collections.rotate(plainText, 3);
        for (int i = 0; i < 35; i++) {
            int x = plainText.get(i);
            int y = key.get(i);
            int res = x ^ y;

            result.add(res);
        }

        return result;
    }

    public static List<Integer> ECB(List<List<Integer>> plainTexts, List<Integer> key, List<Integer> IV) {
        return null;
    }

    public static List<Integer> CTR(List<List<Integer>> plainTexts, List<Integer> key, List<Integer> IV) {
        return null;
    }

    public static List<Integer> CBC(List<List<Integer>> plainTexts, List<Integer> key, List<Integer> IV) {
        return null;
    }

    public static List<Integer> CFB(List<List<Integer>> plainTexts, List<Integer> key, List<Integer> IV) {
        return null;
    }

    public static List<Integer> OFB(List<List<Integer>> plainTexts, List<Integer> key, List<Integer> IV) {
        return null;
    }
}
