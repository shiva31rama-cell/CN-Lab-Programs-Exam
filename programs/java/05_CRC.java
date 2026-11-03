import java.util.Scanner;

public class CRC {
    static String divide(String dividend, String generator) {
        char[] work = dividend.toCharArray();
        for (int i = 0; i <= dividend.length() - generator.length(); i++) {
            if (work[i] == '1') {
                for (int j = 0; j < generator.length(); j++)
                    work[i + j] = work[i + j] == generator.charAt(j) ? '0' : '1';
            }
        }
        return new String(work, work.length - generator.length() + 1, generator.length() - 1);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter data to send (binary): ");
            String data = sc.nextLine().trim();
            System.out.print("Enter generator polynomial (binary): ");
            String generator = sc.nextLine().trim();
            if (!data.matches("[01]+")) throw new IllegalArgumentException("Data must be binary.");
            if (generator.length() < 2 || !generator.matches("[01]+")
                    || generator.charAt(0) != '1' || generator.charAt(generator.length() - 1) != '1')
                throw new IllegalArgumentException("Generator must be binary, start/end with 1 and have length at least 2.");
            if (data.length() < generator.length() - 1)
                throw new IllegalArgumentException("Data is too short for this generator.");
            String zeros = "";
            for (int i = 0; i < generator.length() - 1; i++) zeros += "0";
            String remainder = divide(data + zeros, generator);
            String codeword = data + remainder;
            System.out.println("\n--- Sender Side ---");
            System.out.println("Remainder: " + remainder);
            System.out.println("Data sent: " + codeword);
            String receiverRemainder = divide(codeword, generator);
            System.out.println("\n--- Receiver Side ---");
            System.out.println("Receiver remainder: " + receiverRemainder);
            System.out.println(receiverRemainder.matches("0+")
                    ? "Data received without detected error." : "Data contains an error.");
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}