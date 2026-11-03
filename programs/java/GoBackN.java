import java.util.Scanner;

public class GoBackN {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter frames separated by spaces (example: F0 F1 F2 F3): ");
            String[] frames = sc.nextLine().trim().split("\\s+");
            if (frames.length == 0 || frames[0].isEmpty())
                throw new IllegalArgumentException("Enter at least one frame.");
            System.out.print("Enter window size: ");
            int window = Integer.parseInt(sc.nextLine().trim());
            if (window < 1) throw new IllegalArgumentException("Window size must be at least 1.");

            int base = 0;
            System.out.println("\n--- Go-Back-N Sender Simulation ---");
            while (base < frames.length) {
                int end = Math.min(base + window, frames.length);
                System.out.println("\nCurrent window:");
                for (int i = base; i < end; i++)
                    System.out.println("Sent frame " + i + ": " + frames[i]);
                System.out.print("Enter highest consecutively ACKed frame number (-1 for timeout/loss): ");
                int ack = Integer.parseInt(sc.nextLine().trim());
                if (ack == -1) {
                    System.out.println("Timeout/loss. Retransmit from frame " + base + ".");
                } else if (ack >= base && ack < end) {
                    base = ack + 1;
                    System.out.println("Cumulative ACK received. New window base: " + base);
                } else {
                    System.out.println("Invalid ACK. Enter a frame in the current window or -1.");
                }
            }
            System.out.println("\nAll frames sent and acknowledged.");
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}