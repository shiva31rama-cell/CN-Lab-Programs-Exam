def character_stuff(data, flag, escape):
    if len(flag) != 1 or len(escape) != 1 or flag == escape:
        raise ValueError("Flag and escape must be different single characters.")
    result = flag
    for char in data:
        if char == flag or char == escape:
            result += escape
        result += char
    return result + flag

def character_destuff(frame, flag, escape):
    if len(frame) < 2 or frame[0] != flag or frame[-1] != flag:
        raise ValueError("Invalid frame boundaries.")
    payload, result, i = frame[1:-1], [], 0
    while i < len(payload):
        if payload[i] == escape:
            i += 1
            if i >= len(payload):
                raise ValueError("Invalid escape sequence.")
        result.append(payload[i])
        i += 1
    return "".join(result)

def bit_stuff(bits):
    if any(bit not in "01" for bit in bits):
        raise ValueError("Bit data must contain only 0 and 1.")
    result, count = [], 0
    for bit in bits:
        result.append(bit)
        if bit == "1":
            count += 1
            if count == 5:
                result.append("0")
                count = 0
        else:
            count = 0
    return "".join(result)

def bit_destuff(bits):
    result, count, i = [], 0, 0
    while i < len(bits):
        bit = bits[i]
        if bit not in "01":
            raise ValueError("Bit data must contain only 0 and 1.")
        result.append(bit)
        if bit == "1":
            count += 1
            if count == 5:
                i += 1
                if i >= len(bits) or bits[i] != "0":
                    raise ValueError("Invalid stuffed bit sequence.")
                count = 0
        else:
            count = 0
        i += 1
    return "".join(result)

def main():
    print("CHARACTER STUFFING")
    message = input("Enter message: ")
    flag = input("Enter flag character: ")
    escape = input("Enter escape character: ")
    framed = character_stuff(message, flag, escape)
    print("Original message:", message)
    print("After character stuffing:", framed)
    print("After character destuffing:", character_destuff(framed, flag, escape))
    print("\nBIT STUFFING")
    bits = input("Enter binary message: ").strip()
    stuffed = bit_stuff(bits)
    print("After bit stuffing:", stuffed)
    print("After bit destuffing:", bit_destuff(stuffed))

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)