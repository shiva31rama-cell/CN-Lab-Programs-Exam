def main():
    frames = input("Enter frames separated by spaces (example: A B C D): ").split()
    if not frames:
        raise ValueError("Enter at least one frame.")
    print("\n--- Stop-and-Wait Simulation ---")
    for index, frame in enumerate(frames):
        while True:
            print("\nSent frame {}: {}".format(index, frame))
            ack = input("Was ACK {} received? (y/n): ".format(index)).strip().lower()
            if ack == "y":
                print("ACK received. Moving to next frame.")
                break
            if ack == "n":
                print("ACK not received or frame lost. Retransmitting the same frame.")
            else:
                print("Enter y or n.")
    print("\nAll frames sent and acknowledged.")

if __name__ == "__main__":
    main()