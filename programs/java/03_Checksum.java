import java.util.Scanner;

public class Checksum {
    static int fold(long total, int width) {
        int mask = (1 << width) - 1;
        while ((total >> width) != 0) total = (total & mask) + (total >> width);
        return (int) total;
    }

    static String toBits(int value, int width) {
        String bits = Integer.toBinaryString(value);
        StringBuilder result = new StringBuilder();
        for (int i = bits.length(); i < width; i++) result.append('0');
        return result.append(bits).toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter binary data: ");
            String data = sc.nextLine().trim();
            if (!data.matches("[01]+")) throw new IllegalArgumentException("Enter a non-empty binary string.");
            System.out.print("Enter block size (8 or 16): ");
            int width = Integer.parseInt(sc.nextLine().trim());
            if (width != 8 && width != 16) throw new IllegalArgumentException("Block size must be 8 or 16.");

            int padding = (width - data.length() % width) % width;
            StringBuilder paddedBuilder = new StringBuilder(data);
            for (int i = 0; i < padding; i++) paddedBuilder.append('0');
            String padded = paddedBuilder.toString();
            long sum = 0;
            for (int i = 0; i < padded.length(); i += width)
                sum += Integer.parseInt(padded.substring(i, i + width), 2);

            int mask = (1 << width) - 1;
            int checksum = (~fold(sum, width)) & mask;
            System.out.println("\n--- Sender Side ---");
            System.out.println("Original data: " + data);
            System.out.println("Padded data: " + padded);
            System.out.println("Checksum: " + toBits(checksum, width));
            int receiverSum = fold(sum + checksum, width);
            System.out.println("\n--- Receiver Side ---");
            System.out.println("One's-complement sum: " + toBits(receiverSum, width));
            System.out.println(receiverSum == mask ? "No error detected." : "Error detected.");
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}