import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Scanner scanner2 = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println(CipherModes.getBinaryOfString("abc"));
        System.out.println(35 - (103 % 35));
        System.out.println(partition(CipherModes.getBinaryOfString("abc")));
    }

    private static void prompt(){
        System.out.println("1. ECB\n2. CTR\n3. CBC\n4. CFB\n5. OFB");
        switch (scanner.nextInt()){
            case 1:
                String[] userInput = getUserInput();
                List<Integer> binaryPlainText = CipherModes.getBinaryOfString(userInput[0]);
                List<Integer> binaryKey = CipherModes.getBinaryOfString(userInput[1]);
                List<Integer> partitioned = partition(binaryPlainText);
//                CipherModes.CBC(userInput[0], userInput[1])
        }
    }

    private static List<Integer> partition(List<Integer> binaryPlainText) {
        return null;
    }

    private static String[] getUserInput(){
        System.out.println("Enter plain text:");
        String plainText = scanner2.nextLine();
        System.out.println("Enter key:");
        String key = scanner2.nextLine();
        return new String[]{plainText, key};
    }

    private static List<Integer> padAsNeeded(List<Integer> binary){
        int needed = 35 - (binary.size() % 35);
        for (int i = 0; i < needed; i++) {
            binary.add(0);
        }
        return binary;
    }
}