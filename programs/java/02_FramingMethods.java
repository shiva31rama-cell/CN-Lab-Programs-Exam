import java.util.Scanner;

public class FramingMethods {
    static String characterStuff(String data, char flag, char escape) {
        if (flag == escape) throw new IllegalArgumentException("Flag and escape must be different.");
        StringBuilder result = new StringBuilder().append(flag);
        for (char ch : data.toCharArray()) {
            if (ch == flag || ch == escape) result.append(escape);
            result.append(ch);
        }
        return result.append(flag).toString();
    }

    static String characterDestuff(String frame, char flag, char escape) {
        if (frame.length() < 2 || frame.charAt(0) != flag || frame.charAt(frame.length() - 1) != flag)
            throw new IllegalArgumentException("Invalid frame boundaries.");
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < frame.length() - 1; i++) {
            char ch = frame.charAt(i);
            if (ch == escape) {
                i++;
                if (i >= frame.length() - 1) throw new IllegalArgumentException("Invalid escape sequence.");
                ch = frame.charAt(i);
            }
            result.append(ch);
        }
        return result.toString();
    }

    static String bitStuff(String bits) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (char bit : bits.toCharArray()) {
            if (bit != '0' && bit != '1') throw new IllegalArgumentException("Enter only binary digits.");
            result.append(bit);
            if (bit == '1') {
                count++;
                if (count == 5) { result.append('0'); count = 0; }
            } else count = 0;
        }
        return result.toString();
    }

    static String bitDestuff(String bits) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (int i = 0; i < bits.length(); i++) {
            char bit = bits.charAt(i);
            if (bit != '0' && bit != '1') throw new IllegalArgumentException("Enter only binary digits.");
            result.append(bit);
            if (bit == '1') {
                count++;
                if (count == 5) {
                    if (i + 1 >= bits.length() || bits.charAt(i + 1) != '0')
                        throw new IllegalArgumentException("Invalid stuffed sequence.");
                    i++;
                    count = 0;
                }
            } else count = 0;
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("CHARACTER STUFFING");
            System.out.print("Enter message: ");
            String message = sc.nextLine();
            System.out.print("Enter flag character: ");
            char flag = sc.nextLine().charAt(0);
            System.out.print("Enter escape character: ");
            char escape = sc.nextLine().charAt(0);
            String framed = characterStuff(message, flag, escape);
            System.out.println("Original message: " + message);
            System.out.println("After character stuffing: " + framed);
            System.out.println("After character destuffing: " + characterDestuff(framed, flag, escape));
            System.out.println("\nBIT STUFFING");
            System.out.print("Enter binary message: ");
            String bits = sc.nextLine().trim();
            String stuffed = bitStuff(bits);
            System.out.println("After bit stuffing: " + stuffed);
            System.out.println("After bit destuffing: " + bitDestuff(stuffed));
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}