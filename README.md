# Computer Networks Lab — Practical Exam CLI

An interactive terminal menu for Computer Networks lab practicals. Select Python or Java, choose an experiment, read its aim and description, view the algorithm, display the source code, or run the program.

## Run after npm publication

    npx cn-lab-programs-exam

Or install globally:

    npm install --global cn-lab-programs-exam
    cn-lab-programs-exam

The CLI is written in Node.js and uses no third-party runtime dependencies. Node.js 18 or newer is required to launch the menu.

## Run directly from GitHub

    git clone https://github.com/shiva31rama-cell/CN-Lab-Programs-Exam.git
    cd CN-Lab-Programs-Exam
    node bin/cn-lab-programs-exam.js

To test the installed command locally:

    npm link
    cn-lab-programs-exam

## What the menu does

1. Select Python or Java.
2. Select one of the 15 practicals.
3. Read the aim, description, requirements and notes.
4. View the algorithm or procedure.
5. Display the complete source code.
6. Run a program when the selected language runtime is installed.

The CLI detects Python 3 or Java tools from PATH. Java programs are compiled in a temporary directory, so class files are not left in the repository.

## Practical catalogue

| No. | Experiment | Python | Java |
|---:|---|:---:|:---:|
| 1 | Network devices and LAN setup | Guide | Guide |
| 2 | Character and bit stuffing | Yes | Yes |
| 3 | Checksum | Yes | Yes |
| 4 | Hamming code | Yes | Yes |
| 5 | CRC | Yes | Yes |
| 6 | Go-Back-N | Yes | Yes |
| 7 | Selective Repeat | Yes | Yes |
| 8 | Stop-and-Wait | Yes | Yes |
| 9 | Leaky Bucket | Yes | Yes |
| 10 | Dijkstra shortest path | Yes | Yes |
| 11 | Distance Vector routing | Yes | Yes |
| 12 | Broadcast tree (Prim's algorithm) | Yes | Yes |
| 13 | Wireshark packet capture and analysis | Guide | Guide |
| 14 | Nmap network discovery and port scan | Guide | Guide |
| 15 | Nmap operating-system detection | Guide | Guide |

Guide means the lecturer's practical is based on networking devices or an external tool, rather than a standalone Python/Java program. The CLI displays the actual procedure instead of fake source code.

## Requirements

- Node.js 18+ to launch the CLI.
- Python 3 to run Python practicals.
- A JDK, including javac and java, to compile and run Java practicals.
- Wireshark or Nmap for the relevant external-tool practicals.

## Important lab notes

- CRC polynomial variants differ. For Experiment 5, enter the exact generator polynomial in binary given by your lecturer.
- Dijkstra's algorithm requires non-negative edge weights.
- The routing and ARQ programs are classroom simulations, not complete network protocol implementations.
- Wireshark capture should be limited to traffic you are permitted to inspect.
- Only scan your own computer/network or systems for which you have explicit permission.

## Publishing to npm

The repository is prepared as an npm CLI package. Publishing requires the repository owner's npm account:

    npm login
    npm publish --access public

After the first successful publish, the one-command launch is:

    npx cn-lab-programs-exam

Do not share an npm password or access token in chat.

## License

MIT.