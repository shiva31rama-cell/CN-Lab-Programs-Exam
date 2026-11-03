def main():
    frames = input("Enter frames separated by spaces (example: F0 F1 F2 F3): ").split()
    if not frames:
        raise ValueError("Enter at least one frame.")
    window = int(input("Enter window size: "))
    if window < 1:
        raise ValueError("Window size must be at least 1.")

    base = 0
    print("\n--- Go-Back-N Sender Simulation ---")
    while base < len(frames):
        end = min(base + window, len(frames))
        print("\nCurrent window:")
        for i in range(base, end):
            print("Sent frame {}: {}".format(i, frames[i]))
        ack = int(input("Enter highest consecutively ACKed frame number (-1 for timeout/loss): "))
        if ack == -1:
            print("Timeout/loss. Retransmit from frame {}.".format(base))
        elif base <= ack < end:
            base = ack + 1
            print("Cumulative ACK received. New window base:", base)
        else:
            print("Invalid ACK. Enter a frame number in the current window or -1.")
    print("\nAll frames sent and acknowledged.")

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)