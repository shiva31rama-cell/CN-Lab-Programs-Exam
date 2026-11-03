def divide(dividend, generator):
    work = list(dividend)
    for i in range(len(dividend) - len(generator) + 1):
        if work[i] == "1":
            for j in range(len(generator)):
                work[i + j] = str(int(work[i + j]) ^ int(generator[j]))
    return "".join(work[-(len(generator) - 1):])

def main():
    data = input("Enter data to send (binary): ").strip()
    generator = input("Enter generator polynomial (binary): ").strip()
    if not data or any(bit not in "01" for bit in data):
        raise ValueError("Data must be a non-empty binary string.")
    if len(generator) < 2 or any(bit not in "01" for bit in generator):
        raise ValueError("Generator must contain at least two binary bits.")
    if generator[0] != "1" or generator[-1] != "1":
        raise ValueError("Generator must start and end with 1.")
    if len(data) < len(generator) - 1:
        raise ValueError("Data is too short for this generator.")
    remainder = divide(data + "0" * (len(generator) - 1), generator)
    codeword = data + remainder
    print("\n--- Sender Side ---")
    print("Remainder:", remainder)
    print("Data sent:", codeword)
    received_remainder = divide(codeword, generator)
    print("\n--- Receiver Side ---")
    print("Receiver remainder:", received_remainder)
    print("Data received without detected error."
          if set(received_remainder) <= {"0"} else "Data contains an error.")

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)