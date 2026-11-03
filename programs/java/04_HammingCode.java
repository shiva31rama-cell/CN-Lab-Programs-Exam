import java.util.Scanner;

public class HammingCode {
    static int[] encode(String data) {
        if (!data.matches("[01]{4}")) throw new IllegalArgumentException("Enter exactly four binary bits.");
        int[] h = new int[7];
        h[2] = data.charAt(0) - '0'; h[4] = data.charAt(1) - '0';
        h[5] = data.charAt(2) - '0'; h[6] = data.charAt(3) - '0';
        h[0] = h[2] ^ h[4] ^ h[6];
        h[1] = h[2] ^ h[5] ^ h[6];
        h[3] = h[4] ^ h[5] ^ h[6];
        return h;
    }

    static int[] correct(String received) {
        if (!received.matches("[01]{7}")) throw new IllegalArgumentException("Enter exactly seven binary bits.");
        int[] h = new int[7];
        for (int i = 0; i < 7; i++) h[i] = received.charAt(i) - '0';
        int s1 = h[0] ^ h[2] ^ h[4] ^ h[6];
        int s2 = h[1] ^ h[2] ^ h[5] ^ h[6];
        int s4 = h[3] ^ h[4] ^ h[5] ^ h[6];
        int position = s1 + 2 * s2 + 4 * s4;
        if (position != 0) h[position - 1] ^= 1;
        return new int[] {position, h[2], h[4], h[5], h[6], h[0], h[1], h[3]};
    }

    static String bits(int[] values) {
        StringBuilder result = new StringBuilder();
        for (int value : values) result.append(value);
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter 4 data bits: ");
            String data = sc.nextLine().trim();
            System.out.println("Generated 7-bit Hamming code: " + bits(encode(data)));
            System.out.print("Enter received 7-bit codeword: ");
            String received = sc.nextLine().trim();
            int[] result = correct(received);
            if (result[0] == 0) System.out.println("No single-bit error detected.");
            else {
                System.out.println("Error detected at bit position: " + result[0]);
                char[] fixed = received.toCharArray();
                int p = result[0] - 1;
                fixed[p] = fixed[p] == '0' ? '1' : '0';
                System.out.println("Corrected codeword: " + new String(fixed));
            }
            System.out.println("Recovered data bits: " + result[1] + result[2] + result[3] + result[4]);
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}