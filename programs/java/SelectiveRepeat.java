import java.util.Scanner;

public class SelectiveRepeat {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter frames separated by spaces (example: A B C D E): ");
            String[] frames = sc.nextLine().trim().split("\\s+");
            if (frames.length == 0 || frames[0].isEmpty())
                throw new IllegalArgumentException("Enter at least one frame.");
            System.out.print("Enter window size: ");
            int window = Integer.parseInt(sc.nextLine().trim());
            if (window < 1) throw new IllegalArgumentException("Window size must be at least 1.");

            boolean[] acknowledged = new boolean[frames.length];
            int base = 0;
            System.out.println("\n--- Selective Repeat Sender Simulation ---");
            while (base < frames.length) {
                int end = Math.min(base + window, frames.length);
                System.out.println("\nCurrent window:");
                for (int i = base; i < end; i++)
                    if (!acknowledged[i]) System.out.println("Sent frame " + i + ": " + frames[i]);

                System.out.print("Enter ACKed frame numbers in this window, comma-separated (blank = none): ");
                String answer = sc.nextLine().trim();
                if (!answer.isEmpty()) {
                    for (String value : answer.split(",")) {
                        try {
                            int number = Integer.parseInt(value.trim());
                            if (number >= base && number < end) acknowledged[number] = true;
                            else System.out.println("Ignoring ACK outside current window: " + number);
                        } catch (NumberFormatException error) {
                            System.out.println("Ignoring invalid ACK value: " + value);
                        }
                    }
                }
                while (base < frames.length && acknowledged[base]) base++;
                if (base < frames.length)
                    System.out.println("Only unacknowledged frames will be sent again.");
            }
            System.out.println("\nAll frames sent and acknowledged.");
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}