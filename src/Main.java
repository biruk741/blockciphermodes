import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Scanner scanner2 = new Scanner(System.in);

    static List<Integer> IV = null;
    static List<Integer> key = null;
    // todo: We will probably need to be able to specify an IV and key because we need to decrypt another groups text with a given IV and key.
    public static void main(String[] args) {
        System.out.println("Welcome!");
        mainPrompt();
    }

    /**
     * We should think of a way to get a user input key and IV and use them later?
     */
    private static void encryptPrompt() {
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
        mainPrompt();
    }

    /**
     * For decryption, we need to be able to
     */
    private static void decryptPrompt() {
        System.out.println("1. ECB\n2. CTR\n3. CBC\n4. CFB\n5. OFB\n");
        int choice = scanner.nextInt();
        System.out.println("Please enter the ciphertext:");
        switch (choice) {
            case 1 -> ECBDecrypt(scanner2.nextLine());
            case 2 -> CTRDecrypt(scanner2.nextLine());
            case 3 -> CBCDecrypt(scanner2.nextLine());
            case 4 -> CFBDecrypt(scanner2.nextLine());
            case 5 -> OFBDecrypt(scanner2.nextLine());
        }
        mainPrompt();
    }


    private static void OFBDecrypt(String nextLine) {
        List<Integer> binaryCipherText = CipherModesDecryption.stringToList(nextLine.replaceAll(" ", ""));
        List<List<Integer>> partitionedCipherText = partition(binaryCipherText);
        List<Character> res = CipherModesDecryption.OFBDecrypt(partitionedCipherText, getKey(), getIV());
        System.out.println("The output of decryption using OFB is: " + CipherModesDecryption.cleanString(res));
    }

    private static void CFBDecrypt(String nextLine) {
        List<Integer> binaryCipherText = CipherModesDecryption.stringToList(nextLine.replaceAll(" ", ""));
        List<List<Integer>> partitionedCipherText = partition(binaryCipherText);
        List<Character> res = CipherModesDecryption.CFBDecrypt(partitionedCipherText, getKey(), getIV());
        System.out.println("The output of decryption using CFB is: " + CipherModesDecryption.cleanString(res));
    }

    private static void CBCDecrypt(String nextLine) {
        List<Integer> binaryCipherText = CipherModesDecryption.stringToList(nextLine.replaceAll(" ", ""));
        List<List<Integer>> partitionedCipherText = partition(binaryCipherText);
        List<Character> res = CipherModesDecryption.CBCDecrypt(partitionedCipherText, getKey(), getIV());
        System.out.println("The output of decryption using CBC is: " + CipherModesDecryption.cleanString(res));
    }

    private static void CTRDecrypt(String nextLine) {
        List<Integer> binaryCipherText = CipherModesDecryption.stringToList(nextLine.replaceAll(" ", ""));
        List<List<Integer>> partitionedCipherText = partition(binaryCipherText);
        List<Character> res = CipherModesDecryption.CTRDecrypt(partitionedCipherText, getKey(), getIVShorter());
        System.out.println("The output of decryption using CTR is: " + CipherModesDecryption.cleanString(res));
    }

    private static void ECBDecrypt(String nextLine) {
        List<Integer> binaryCipherText = CipherModesDecryption.stringToList(nextLine.replaceAll(" ", ""));
        List<List<Integer>> partitionedCipherText = partition(binaryCipherText);
        List<Character> res = CipherModesDecryption.ECBDecrypt(partitionedCipherText, getKey());
        System.out.println("The output of decryption using ECB is: " + CipherModesDecryption.cleanString(res));
    }

    private static void mainPrompt() {
        System.out.println("1. Encrypt\n2. Decrypt");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> encryptPrompt();
            case 2 -> decryptPrompt();
        }
        mainPrompt();
    }

    private static List<List<Integer>> partition(List<Integer> binaryText) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < binaryText.size() / 35; i++) {
            res.add(binaryText.subList(i * 35, i * 35 + 35));
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

        List<Integer> res = CipherModes.ECB(partitioned, getKey());
        System.out.println("Output of ECB is: " + CipherModes.cleanBinary(res));
    }

 // TODO: PLS TEST
    public static void CTR(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> res = CipherModes.CTR(partitioned, getKey(), getIVShorter());
        System.out.println("Output of CTR is: " + CipherModes.cleanBinary(res));
    }

    public static void CBC(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        binaryPlainText = padAsNeeded(binaryPlainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> IV = CipherModes.generateRandomIV();
        List<Integer> res = CipherModes.CBC(partitioned, getKey(), IV);
        System.out.println("Output of CBC is: " + CipherModes.cleanBinary(res) + "\nUsed IV: " + CipherModes.cleanBinary(IV) + "\n");
    }

    public static void CFB(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        binaryPlainText = padAsNeeded(binaryPlainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> IV = CipherModes.generateRandomIV();
        List<Integer> res = CipherModes.CFB(partitioned, getKey(), IV);
        System.out.println("Output of CFB is: " + CipherModes.cleanBinary(res) + "\nUsed IV: " + CipherModes.cleanBinary(IV) + "\n");
    }

    public static void OFB(String plainText) {
        List<Integer> binaryPlainText = CipherModes.getBinaryOfString(plainText);
        List<List<Integer>> partitioned = partition(binaryPlainText);

        List<Integer> IV = CipherModes.generateRandomIV();
        List<Integer> res = CipherModes.OFB(partitioned, getKey(), IV);
        System.out.println("Output of OFB is: " + CipherModes.cleanBinary(res) + "\nUsed IV: " + CipherModes.cleanBinary(IV) + "\n");
    }

    private static List<Integer> getIV() {
        if (IV != null) return IV;

        System.out.println("Please enter an IV string:");
        IV = stringToList(scanner2.nextLine().replaceAll(" ", ""));
        System.out.println("You have entered: " + IV);
        return IV;
    }

    private static List<Integer> getIVShorter() {
        if (IV != null) return IV;

        System.out.println("Please enter an IV string:");
        String input = scanner2.nextLine().replaceAll(" ", "");
        if (input.length() != 19) throw new IllegalArgumentException();
        IV = stringToList(input);
        System.out.println("You have entered: " + CipherModes.cleanBinary(IV));
        return IV;
    }

    private static List<Integer> getKey() {
        if (key != null) return key;

        System.out.println("Please enter a key string:");
        key = stringToList(scanner2.nextLine().replaceAll(" ", ""));
        System.out.println("You have entered: " + CipherModes.cleanBinary(key));
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