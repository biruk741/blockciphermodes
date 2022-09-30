import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Scanner scanner2 = new Scanner(System.in);

    static List<Integer> IV = null;
    static List<Integer> key = null;

    public static void main(String[] args) {
        System.out.println("Welcome!");
        prompt();
    }

    private static void prompt() {
        System.out.println("1. ECB\n2. CTR\n3. CBC\n4. CFB\n5. OFB\n6. Run all modes");
        int choice = scanner.nextInt();
        System.out.println("Please enter the plaintext:");
        switch (choice) {
            case 1 -> ECB(scanner2.nextLine());
            case 2 -> CTR(scanner2.nextLine());
            case 3 -> CBC(scanner2.nextLine());
            case 4 -> CFB(scanner2.nextLine());
            case 5 -> OFB(scanner2.nextLine());
            case 6 -> runAll(scanner2.nextLine());
        }
        prompt();
    }

    private static List<List<Integer>> partition(List<Integer> binaryPlainText) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < binaryPlainText.size() / 35; i++) {
            res.add(binaryPlainText.subList(i * 35, i * 35 + 35));
        }
        return res;
    }

    private static List<Integer> padAsNeeded(List<Integer> binary) {
        int mod = binary.size() % 35;
        int needed = 35 - (mod == 0 ? 35 : mod);
        for (int i = 0; i < needed; i++) {
            binary.add(0);
        }
        return binary;
    }

    public static void ECB(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        binaryPlainText = padAsNeeded(binaryPlainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> res = CipherModes.ECB(partitioned, getKey(), getIV());
        System.out.println(res);
    }

    public static void CTR(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> res = CipherModes.CTR(partitioned, getKey(), getIV());
        System.out.println(res);
    }

    public static void CBC(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        binaryPlainText = padAsNeeded(binaryPlainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> res = CipherModes.CBC(partitioned, getKey(), getIV());
        System.out.println(res);
    }

    public static void CFB(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        binaryPlainText = padAsNeeded(binaryPlainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> res = CipherModes.CFB(partitioned, getKey(), getIV());
        System.out.println(res);
    }

    public static void OFB(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> res = CipherModes.OFB(partitioned, getKey(), getIV());
        System.out.println(res);
    }

    private static List<Integer> getIV() {
        if (IV != null) return IV;

        System.out.println("Please enter an IV string:");
        IV = stringToList(scanner2.nextLine().replaceAll(" ", ""));
        System.out.println("You have entered: " + IV);
        return IV;
    }

    private static List<Integer> getKey() {
        if (key != null) return key;

        System.out.println("Please enter a key string:");
        key = stringToList(scanner2.nextLine().replaceAll(" ", ""));
        System.out.println("You have entered: " + key);
        return key;
    }

    private static List<Integer> stringToList(String str) {
        return str.chars()
                .mapToObj(e -> Character.getNumericValue((char) e))
                .collect(Collectors.toList());
    }

    private static void runAll(String s) {
        ECB(s);
        CTR(s);

        CBC(s);
        CFB(s);
        OFB(s);
    }
}