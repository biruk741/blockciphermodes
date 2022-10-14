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

    /**
     * Converts a list of Integers into a String of regular characters
     * @param binary A list of Integers
     * @return an ASCII character
     */
    public static Character binaryToChar(List<Integer> binary) {
        String binaryString = CipherModes.cleanBinary(binary);
        int binaryInt = Integer.valueOf(binaryString, 2);
        char character = (char) binaryInt;
        return character;
    }

    private static List<List<Integer>> partitionBlocks(List<Integer> cipherText) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < cipherText.size() / 7; i++) {
            res.add(cipherText.subList(i * 7, i * 7 + 7));
        }
        return res;
    }

    /**
     * Takes a string of binary (1s and 0s) and converts it into a list of 1s and 0s so that
     * we can use partition on it and apply EK to each block.
     * @param cipherText
     * @return
     */
    public static List<Integer> stringToList(String cipherText) {
        List<Integer> binaryCipherText = Arrays.stream(cipherText.split("\\B"))
                .map(Integer::parseInt).toList();
        return binaryCipherText;
    }

    public static List<Integer> EKInverse(List<Integer> cipherText, List<Integer> key) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            int x = cipherText.get(i);
            int y = key.get(i);
            int res = x ^ y;

            result.add(res);
        }
        Collections.rotate(result, -3);

        return result;
    }

    public static List<Character> ECBDecrypt(List<List<Integer>> cipherTexts, List<Integer> key) {
        List<Integer> beforePartition = new ArrayList<>();
        List<Character> result = new ArrayList<>();
        for (List<Integer> list: cipherTexts){
            beforePartition.addAll(EKInverse(list, key));
        }
        List<List<Integer>> partitioned = partitionBlocks(beforePartition);
        for (List<Integer> list: partitioned) {
            result.add(binaryToChar(list));
        }
        return result;
    }

    public static List<Character> CTRDecrypt(List<List<Integer>> cipherTexts, List<Integer> key, List<Integer> IV) {
        List<Integer> finalResult = new ArrayList<>();
        List<Character> finalResultChars = new ArrayList<>();


        for (int i = 0; i < cipherTexts.size(); i++) {
            List<Integer> current = cipherTexts.get(i);

            String result = Integer.toBinaryString(i);
            String resultWithPadding = String.format("%16s", result).replaceAll(" ", "0");

            List<Integer> list = Arrays.stream(resultWithPadding.split("\\B"))
                    .map(Integer::parseInt).toList();
            List<Integer> clonedIV = new ArrayList<>(IV);
            clonedIV.addAll(list);

            finalResult.addAll(XOR(CipherModes.EK(clonedIV, key), current));
        }

        List<List<Integer>> partitioned = partitionBlocks(finalResult);
        for (List<Integer> list: partitioned) {
            finalResultChars.add(binaryToChar(list));
        }
        return finalResultChars;
    }

    public static List<Character> CBCDecrypt(List<List<Integer>> cipherTexts, List<Integer> key, List<Integer> IV) {
        List<Integer> finalResult = new ArrayList<>();
        List<Character> finalResultChars = new ArrayList<>();

        List<Integer> prevIV = null;

        for (List<Integer> list: cipherTexts){
            if (prevIV == null){
                prevIV = IV;
            }
            finalResult.addAll(XOR(EKInverse(list, key),prevIV));
            prevIV = list;
        }
        List<List<Integer>> partitioned = partitionBlocks(finalResult);
        for (List<Integer> list: partitioned) {
            finalResultChars.add(binaryToChar(list));
        }
        return finalResultChars;
    }

    public static List<Character> CFBDecrypt(List<List<Integer>> cipherTexts, List<Integer> key, List<Integer> IV) {
        List<Integer> finalResult = new ArrayList<>();
        List<Character> finalResultChars = new ArrayList<>();


        List<Integer> prevResult = null;

        for (List<Integer> list: cipherTexts){
            if (prevResult == null){
                prevResult = IV;
            }

            finalResult.addAll(XOR(CipherModes.EK(prevResult, key), list));
            prevResult = list;
        }
        List<List<Integer>> partitioned = partitionBlocks(finalResult);
        for (List<Integer> list: partitioned) {
            finalResultChars.add(binaryToChar(list));
        }
        return finalResultChars;
    }

    public static List<Character> OFBDecrypt(List<List<Integer>> cipherTexts, List<Integer> key, List<Integer> IV) {
        List<Integer> finalResult = new ArrayList<>();
        List<Character> finalResultChars = new ArrayList<>();

        List<Integer> prevIV = null;

        for (List<Integer> list: cipherTexts){
            if (prevIV == null){
                prevIV = IV;
            }

            prevIV = CipherModes.EK(prevIV, key);

            finalResult.addAll(XOR(prevIV, list));
        }
        List<List<Integer>> partitioned = partitionBlocks(finalResult);
        for (List<Integer> list: partitioned) {
            finalResultChars.add(binaryToChar(list));
        }
        return finalResultChars;
    }

    public static List<Integer> XOR(List<Integer> list1,List<Integer> list2){
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            result.add(list1.get(i) ^ list2.get(i));
        }
        return result;
    }

    public static String cleanString(List<Character> res) {
        String result = res.toString().replaceAll("[\\[\\]\s,]", "");
        return result;
    }
}
