import java.util.*;

public class CipherModesDecryption {
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

    public static List<Integer> EKInverse(List<Integer> plainText, List<Integer> key) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            int x = plainText.get(i);
            int y = key.get(i);
            int res = x ^ y;

            result.add(res);
        }
        Collections.rotate(plainText, -3);

        return result;
    }

    public static List<Integer> ECBDecrypt(List<List<Integer>> cipherTexts, List<Integer> key) {
        List<Integer> result = new ArrayList<>();
        for (List<Integer> list: cipherTexts){
            result.addAll(EK(list, key));
        }
        return result;
    }

    public static List<Integer> CTRDecrypt(List<List<Integer>> cipherTexts, List<Integer> key, List<Integer> IV) {
        List<Integer> finalResult = new ArrayList<>();


        for (int i = 0; i < cipherTexts.size(); i++) {
            List<Integer> current = cipherTexts.get(i);

            String result = Integer.toBinaryString(i);
            String resultWithPadding = String.format("%32s", result).replaceAll(" ", "0");

            List<Integer> list = Arrays.stream(resultWithPadding.split("\\B"))
                    .map(Integer::parseInt).toList();
            List<Integer> clonedIV = new ArrayList<>(IV);
            clonedIV.addAll(list);

            finalResult.addAll(XOR(EK(clonedIV, key), current));
        }

        return finalResult;
    }

    public static List<Integer> CBCDecrypt(List<List<Integer>> cipherTexts, List<Integer> key, List<Integer> IV) {
        List<Integer> finalResult = new ArrayList<>();

        List<Integer> prevResult = null;

        for (List<Integer> list: cipherTexts){
            if (prevResult == null){
                prevResult = IV;
            }
            prevResult = EK(XOR(list, prevResult), key);
            finalResult.addAll(prevResult);
        }
        return finalResult;
    }

    public static List<Integer> CFBDecrypt(List<List<Integer>> cipherTexts, List<Integer> key, List<Integer> IV) {
        List<Integer> finalResult = new ArrayList<>();

        List<Integer> prevResult = null;

        for (List<Integer> list: cipherTexts){
            if (prevResult == null){
                prevResult = IV;
            }
            prevResult = XOR(EK(prevResult, key), list);
            finalResult.addAll(prevResult);
        }
        return finalResult;
    }

    public static List<Integer> OFBDecrypt(List<List<Integer>> cipherTexts, List<Integer> key, List<Integer> IV) {
        List<Integer> finalResult = new ArrayList<>();

        List<Integer> prevIV = null;

        for (List<Integer> list: cipherTexts){
            if (prevIV == null){
                prevIV = IV;
            }

            prevIV = EK(prevIV, key);

            finalResult.addAll(XOR(prevIV, list));
        }
        return finalResult;
    }

    public static List<Integer> XOR(List<Integer> list1,List<Integer> list2){
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            result.add(list1.get(i) ^ list2.get(i));
        }
        return result;
    }
}
