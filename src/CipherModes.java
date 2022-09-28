import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CipherModes {
    public static List<Integer> getBinaryOfString(String s){
        byte[] bytes = s.getBytes();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++)
        {
            int val = bytes[i];
            for (int j = 0; j < 8; j++)
            {
                result.add((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            result.remove(i * 7); // todo: fix this or something
        }
        return result;
    }
    //         System.out.println("1. ECB\n2. CTR\n3. CBC\n4. CFB\n5. OFB");

    public static List<Integer> EK(List<Integer> plainText, List<Integer> key){
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

    public static List<Integer> ECB(List<Integer> plainText, List<Integer> key){
        return null;
    }
    public static List<Integer> CTR(List<Integer> plainText, List<Integer> key){
        return null;
    }
    public static List<Integer> CBC(List<Integer> plainText, List<Integer> key){
        return null;
    }
    public static List<Integer> CFB(List<Integer> plainText, List<Integer> key){
        return null;
    }
    public static List<Integer> OFB(List<Integer> plainText, List<Integer> key){
        return null;
    }
}
