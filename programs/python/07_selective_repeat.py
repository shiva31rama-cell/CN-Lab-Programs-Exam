def main():
    frames = input("Enter frames separated by spaces (example: A B C D E): ").split()
    if not frames:
        raise ValueError("Enter at least one frame.")
    window = int(input("Enter window size: "))
    if window < 1:
        raise ValueError("Window size must be at least 1.")

    acknowledged = [False] * len(frames)
    base = 0
    print("\n--- Selective Repeat Sender Simulation ---")
    while base < len(frames):
        end = min(base + window, len(frames))
        print("\nCurrent window:")
        for i in range(base, end):
            if not acknowledged[i]:
                print("Sent frame {}: {}".format(i, frames[i]))
        answer = input("Enter ACKed frame numbers in this window, comma-separated (blank = none): ").strip()
        if answer:
            try:
                numbers = [int(value.strip()) for value in answer.split(",")]
            except ValueError:
                print("Enter comma-separated frame numbers.")
                continue
            for number in numbers:
                if base <= number < end:
                    acknowledged[number] = True
                else:
                    print("Ignoring ACK outside current window:", number)
        while base < len(frames) and acknowledged[base]:
            base += 1
        if base < len(frames):
            print("Only unacknowledged frames will be sent again.")
    print("\nAll frames sent and acknowledged.")

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)