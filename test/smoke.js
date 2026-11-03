'use strict';

const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { spawnSync } = require('node:child_process');

const root = path.resolve(__dirname, '..');
const pkg = require(path.join(root, 'package.json'));
const catalog = require(path.join(root, 'data', 'experiments.json')).experiments;
const cli = path.join(root, 'bin', 'cn-lab-programs-exam.js');

assert.equal(catalog.length, 15, 'The catalogue should contain all 15 official practicals.');
assert.ok(pkg.bin['cn-lab-programs-exam'], 'The npm executable must be configured.');

for (const experiment of catalog) {
  assert.ok(experiment.aim, 'Experiment ' + experiment.number + ' needs an aim.');
  assert.ok(experiment.description, 'Experiment ' + experiment.number + ' needs a description.');
  assert.ok(Array.isArray(experiment.algorithm), 'Experiment ' + experiment.number + ' needs a procedure.');
  if (experiment.kind === 'guide') continue;
  for (const language of ['python', 'java']) {
    const source = path.join(root, experiment.sources[language]);
    assert.ok(fs.existsSync(source), 'Missing ' + language + ' source for experiment ' + experiment.number);
  }
}

function runCli(args) {
  return spawnSync(process.execPath, [cli, ...args], { encoding: 'utf8', timeout: 10000 });
}

const version = runCli(['--version']);
assert.equal(version.status, 0, 'The CLI should report its version.');
assert.equal(version.stdout.trim(), pkg.version);

const help = runCli(['--help']);
assert.equal(help.status, 0, 'The CLI help command should work.');
assert.match(help.stdout, /npx cn-lab-programs-exam/);

const list = runCli(['--list']);
assert.equal(list.status, 0, 'The experiment list command should work.');
assert.match(list.stdout, /Study of Network Devices and LAN Setup/);
assert.match(list.stdout, /Hamming Code/);
assert.match(list.stdout, /Operating System Detection Using Nmap/);

console.log('Smoke tests passed: package metadata, all 15 experiment descriptions, source paths and CLI commands.');
