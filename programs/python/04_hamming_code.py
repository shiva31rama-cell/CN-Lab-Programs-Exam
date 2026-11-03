def encode(data):
    if len(data) != 4 or any(bit not in "01" for bit in data):
        raise ValueError("Enter exactly four binary data bits.")
    # Positions (1-based): P1 P2 D1 P3 D2 D3 D4
    h = [0] * 7
    h[2], h[4], h[5], h[6] = map(int, data)
    h[0] = h[2] ^ h[4] ^ h[6]
    h[1] = h[2] ^ h[5] ^ h[6]
    h[3] = h[4] ^ h[5] ^ h[6]
    return h

def correct(received):
    if len(received) != 7 or any(bit not in "01" for bit in received):
        raise ValueError("Enter exactly seven binary bits.")
    h = [int(bit) for bit in received]
    s1 = h[0] ^ h[2] ^ h[4] ^ h[6]
    s2 = h[1] ^ h[2] ^ h[5] ^ h[6]
    s4 = h[3] ^ h[4] ^ h[5] ^ h[6]
    position = s1 + 2 * s2 + 4 * s4
    if position: h[position - 1] ^= 1
    data = "".join(str(h[i]) for i in (2, 4, 5, 6))
    return h, position, data

def main():
    data = input("Enter 4 data bits: ").strip()
    codeword = "".join(map(str, encode(data)))
    print("Generated 7-bit Hamming code:", codeword)
    received = input("Enter received 7-bit codeword: ").strip()
    fixed, position, recovered = correct(received)
    if position:
        print("Error detected at bit position:", position)
        print("Corrected codeword:", "".join(map(str, fixed)))
    else:
        print("No single-bit error detected.")
    print("Recovered data bits:", recovered)

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)