#!/usr/bin/env node
'use strict';

const fs = require('node:fs');
const path = require('node:path');
const os = require('node:os');
const { spawnSync } = require('node:child_process');
const readline = require('node:readline');

const ROOT = path.resolve(__dirname, '..');
const catalog = require(path.join(ROOT, 'data', 'experiments.json')).experiments;
const colors = process.stdout.isTTY && !process.env.NO_COLOR;
const paint = (text, code) => colors ? '\u001b[' + code + 'm' + text + '\u001b[0m' : text;
const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
const ask = (prompt) => new Promise((resolve) => rl.question(prompt, (answer) => resolve(answer.trim())));

function line() { console.log(paint('-'.repeat(68), '2')); }

function banner() {
  console.log('');
  console.log(paint('  COMPUTER NETWORKS LAB', '36;1'));
  console.log(paint('  PRACTICAL PROGRAMS — PYTHON & JAVA', '1'));
  console.log('  Learn | View | Run');
  line();
}

function showList() {
  console.log('\nSELECT AN EXPERIMENT');
  for (const item of catalog) {
    console.log('  ' + String(item.number).padStart(2, ' ') + '. ' + item.title);
  }
  console.log('   0. Back');
}

function details(item, language) {
  line();
  console.log(paint(item.number + '. ' + item.title, '1'));
  console.log('Language: ' + language.toUpperCase());
  console.log('Type: ' + (item.kind === 'guide' ? 'Study / external-tool practical' : 'Programming practical'));
  console.log('\nAIM\n' + item.aim);
  console.log('\nDESCRIPTION\n' + item.description);
  if (item.requirements && item.requirements.length) {
    console.log('\nREQUIREMENTS');
    item.requirements.forEach((v) => console.log('  - ' + v));
  }
  if (item.notes && item.notes.length) {
    console.log('\nNOTES');
    item.notes.forEach((v) => console.log('  - ' + v));
  }
  line();
}

function showAlgorithm(item) {
  console.log('\nALGORITHM / PROCEDURE');
  item.algorithm.forEach((step, index) => console.log('  ' + (index + 1) + '. ' + step));
}

function getSource(item, language) {
  if (item.kind === 'guide') return null;
  return path.join(ROOT, item.sources[language]);
}

function showCode(item, language) {
  const file = getSource(item, language);
  if (!file || !fs.existsSync(file)) {
    console.log(paint('\nNo standalone ' + language.toUpperCase() + ' source is used for this practical.', '33'));
    console.log('This is a study or external-tool experiment. Follow the procedure above.');
    return;
  }
  console.log('\nSOURCE CODE — ' + language.toUpperCase());
  line();
  fs.readFileSync(file, 'utf8').split(/\r?\n/).forEach((text, index) => {
    console.log(paint(String(index + 1).padStart(3, ' ') + ' | ', '2') + text);
  });
  line();
}

function commandAvailable(command, args) {
  return !spawnSync(command, args, { stdio: 'ignore', windowsHide: true }).error;
}

function runPython(file) {
  const options = process.platform === 'win32'
    ? [{command:'py', args:['-3', file]}, {command:'python', args:[file]}]
    : [{command:'python3', args:[file]}, {command:'python', args:[file]}];
  const runner = options.find((option) => commandAvailable(option.command, ['--version']));
  if (!runner) {
    console.log(paint('Python 3 was not found. Install Python and add it to PATH.', '33'));
    return;
  }
  console.log('\nRunning with ' + runner.command + ' ...\n');
  const result = spawnSync(runner.command, runner.args, { stdio: 'inherit', windowsHide: true });
  if (result.error) console.log(paint(result.error.message, '31'));
  else if (result.status !== 0) console.log('Program exited with code ' + result.status);
}

function runJava(file) {
  if (!commandAvailable('javac', ['-version']) || !commandAvailable('java', ['-version'])) {
    console.log(paint('Java JDK was not found. Install a JDK and add java and javac to PATH.', '33'));
    return;
  }
  const className = path.basename(file, '.java');
  const temp = fs.mkdtempSync(path.join(os.tmpdir(), 'cn-lab-'));
  const source = path.join(temp, className + '.java');
  try {
    fs.copyFileSync(file, source);
    console.log('\nCompiling ' + className + '.java ...');
    const compile = spawnSync('javac', ['-encoding', 'UTF-8', '-d', temp, source], { stdio: 'inherit', windowsHide: true });
    if (compile.error || compile.status !== 0) {
      console.log(paint('Compilation failed.', '31'));
      return;
    }
    console.log('\nRunning ' + className + ' ...\n');
    const result = spawnSync('java', ['-cp', temp, className], { stdio: 'inherit', windowsHide: true });
    if (result.error) console.log(paint(result.error.message, '31'));
    else if (result.status !== 0) console.log('Program exited with code ' + result.status);
  } finally {
    try { fs.rmSync(temp, {recursive:true, force:true}); } catch (_) {}
  }
}

function runProgram(item, language) {
  if (item.kind === 'guide') {
    console.log(paint('\nThis experiment uses networking equipment or an external tool, not a standalone ' + language.toUpperCase() + ' program.', '33'));
    console.log('Use the displayed procedure. Install Wireshark or Nmap separately when required.');
    return;
  }
  const file = getSource(item, language);
  if (!file || !fs.existsSync(file)) {
    console.log(paint('The selected source file is not available yet.', '31'));
  } else if (language === 'python') {
    runPython(file);
  } else {
    runJava(file);
  }
}

async function chooseLanguage() {
  while (true) {
    console.log('\nCHOOSE LANGUAGE');
    console.log('  1. Python');
    console.log('  2. Java');
    console.log('  0. Exit');
    const answer = await ask('\nEnter choice: ');
    if (answer === '1') return 'python';
    if (answer === '2') return 'java';
    if (answer === '0' || answer.toLowerCase() === 'q') return null;
    console.log('Please enter 1, 2 or 0.');
  }
}

async function chooseExperiment() {
  while (true) {
    showList();
    const answer = await ask('\nEnter experiment number: ');
    if (answer === '0' || answer.toLowerCase() === 'q') return null;
    const item = catalog.find((entry) => entry.number === Number(answer));
    if (item) return item;
    console.log(paint('Invalid experiment number.', '33'));
  }
}

async function itemMenu(item, language) {
  details(item, language);
  while (true) {
    console.log('\nWHAT DO YOU WANT TO DO?');
    console.log('  1. View description');
    console.log('  2. View algorithm / procedure');
    console.log('  3. Display source code');
    console.log('  4. Run program');
    console.log('  5. Back to experiment list');
    const answer = await ask('\nEnter choice: ');
    if (answer === '1') details(item, language);
    else if (answer === '2') showAlgorithm(item);
    else if (answer === '3') showCode(item, language);
    else if (answer === '4') runProgram(item, language);
    else if (answer === '5' || answer.toLowerCase() === 'b') return;
    else console.log('Please enter a number from 1 to 5.');
  }
}

async function main() {
  if (process.argv.includes('--version') || process.argv.includes('-v')) {
    console.log(require(path.join(ROOT, 'package.json')).version);
    return;
  }
  if (process.argv.includes('--help') || process.argv.includes('-h')) {
    console.log('Usage: npx cn-lab-programs-exam');
    console.log('Starts the interactive Computer Networks practicals menu.');
    return;
  }

  if (process.argv.includes('--list')) {
    console.log('COMPUTER NETWORKS LAB — EXPERIMENTS');
    for (const item of catalog) {
      console.log(String(item.number).padStart(2, ' ') + '. ' + item.title);
    }
    return;
  }

  banner();
  console.log('Select a language and experiment. Then view its description, algorithm, code or run it.');
  console.log('Tool-based practicals display their actual procedure instead of fake Python/Java code.');

  try {
    while (true) {
      const language = await chooseLanguage();
      if (!language) break;
      while (true) {
        const item = await chooseExperiment();
        if (!item) break;
        await itemMenu(item, language);
      }
    }
  } finally {
    rl.close();
  }
  console.log('\nGood luck with your practical examination!');
}

main().catch((error) => {
  console.error(paint('Unexpected error: ' + error.message, '31'));
  rl.close();
  process.exitCode = 1;
});