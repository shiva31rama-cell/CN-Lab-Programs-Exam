def fold_ones_complement(total, width):
    mask = (1 << width) - 1
    while total >> width:
        total = (total & mask) + (total >> width)
    return total

def main():
    data = input("Enter binary data: ").strip()
    if not data or any(bit not in "01" for bit in data):
        raise ValueError("Enter a non-empty binary string.")
    width = int(input("Enter block size (8 or 16): "))
    if width not in (8, 16):
        raise ValueError("Block size must be 8 or 16.")
    padded = data + ("0" * ((-len(data)) % width))
    blocks = [int(padded[i:i + width], 2) for i in range(0, len(padded), width)]
    mask = (1 << width) - 1
    checksum = (~fold_ones_complement(sum(blocks), width)) & mask
    print("\n--- Sender Side ---")
    print("Original data:", data)
    print("Padded data:", padded)
    print("Checksum:", format(checksum, "0{}b".format(width)))
    receiver_sum = fold_ones_complement(sum(blocks) + checksum, width)
    print("\n--- Receiver Side ---")
    print("One's-complement sum:", format(receiver_sum, "0{}b".format(width)))
    print("No error detected." if receiver_sum == mask else "Error detected.")

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)