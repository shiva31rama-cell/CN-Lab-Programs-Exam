import java.util.Scanner;

public class StopAndWait {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter frames separated by spaces (example: A B C D): ");
            String[] frames = sc.nextLine().trim().split("\\s+");
            if (frames.length == 0 || frames[0].isEmpty())
                throw new IllegalArgumentException("Enter at least one frame.");
            System.out.println("\n--- Stop-and-Wait Simulation ---");
            for (int i = 0; i < frames.length; i++) {
                while (true) {
                    System.out.println("\nSent frame " + i + ": " + frames[i]);
                    System.out.print("Was ACK " + i + " received? (y/n): ");
                    String ack = sc.nextLine().trim().toLowerCase();
                    if (ack.equals("y")) {
                        System.out.println("ACK received. Moving to next frame.");
                        break;
                    } else if (ack.equals("n")) {
                        System.out.println("ACK not received or frame lost. Retransmitting the same frame.");
                    } else {
                        System.out.println("Enter y or n.");
                    }
                }
            }
            System.out.println("\nAll frames sent and acknowledged.");
        } finally { sc.close(); }
    }
}