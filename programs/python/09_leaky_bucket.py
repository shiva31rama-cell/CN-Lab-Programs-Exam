def leaky_bucket(incoming, capacity, leak_rate):
    if capacity <= 0 or leak_rate <= 0:
        raise ValueError("Bucket capacity and leak rate must be positive.")
    bucket = 0
    print("\nTime\tIncoming\tRemaining\tSent\tDropped")
    for time, packets in enumerate(incoming, start=1):
        if packets < 0:
            raise ValueError("Packet counts cannot be negative.")
        bucket += packets
        dropped = max(0, bucket - capacity)
        bucket = min(bucket, capacity)
        sent = min(bucket, leak_rate)
        bucket -= sent
        print("{}\t{}\t\t{}\t\t{}\t{}".format(time, packets, bucket, sent, dropped))
    print("Packets remaining in bucket:", bucket)

def main():
    values = input("Enter incoming packet counts separated by spaces: ").split()
    incoming = [int(value) for value in values]
    capacity = int(input("Enter bucket capacity: "))
    leak_rate = int(input("Enter leak rate: "))
    leaky_bucket(incoming, capacity, leak_rate)

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)