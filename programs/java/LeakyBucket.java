import java.util.Scanner;

public class LeakyBucket {
    static void simulate(int[] incoming, int capacity, int leakRate) {
        if (capacity <= 0 || leakRate <= 0)
            throw new IllegalArgumentException("Capacity and leak rate must be positive.");
        int bucket = 0;
        System.out.println("\nTime\tIncoming\tRemaining\tSent\tDropped");
        for (int i = 0; i < incoming.length; i++) {
            if (incoming[i] < 0) throw new IllegalArgumentException("Packet counts cannot be negative.");
            bucket += incoming[i];
            int dropped = Math.max(0, bucket - capacity);
            bucket = Math.min(bucket, capacity);
            int sent = Math.min(bucket, leakRate);
            bucket -= sent;
            System.out.println((i + 1) + "\t" + incoming[i] + "\t\t" + bucket
                    + "\t\t" + sent + "\t" + dropped);
        }
        System.out.println("Packets remaining in bucket: " + bucket);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter incoming packet counts separated by spaces: ");
            String[] parts = sc.nextLine().trim().split("\\s+");
            int[] incoming = new int[parts.length];
            for (int i = 0; i < parts.length; i++) incoming[i] = Integer.parseInt(parts[i]);
            System.out.print("Enter bucket capacity: ");
            int capacity = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Enter leak rate: ");
            int rate = Integer.parseInt(sc.nextLine().trim());
            simulate(incoming, capacity, rate);
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}