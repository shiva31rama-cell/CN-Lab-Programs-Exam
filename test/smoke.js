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
  if (experiment.kind === 'guide') continue;
  for (const language of ['python', 'java']) {
    const source = path.join(root, experiment.sources[language]);
    assert.ok(fs.existsSync(source), 'Missing ' + language + ' source for experiment ' + experiment.number);
  }
}

const version = spawnSync(process.execPath, [cli, '--version'], { encoding: 'utf8' });
assert.equal(version.status, 0, 'The CLI should report its version.');
assert.equal(version.stdout.trim(), pkg.version);

const session = spawnSync(
  process.execPath,
  [cli],
  { input: '1\n2\n3\n5\n0\n0\n', encoding: 'utf8', timeout: 10000 }
);
assert.equal(session.status, 0, 'The interactive menu should exit normally.');
assert.match(session.stdout, /COMPUTER NETWORKS LAB/);
assert.match(session.stdout, /SELECT AN EXPERIMENT/);
assert.match(session.stdout, /SOURCE CODE — PYTHON/);
assert.match(session.stdout, /character_stuff/);

console.log('Smoke tests passed: package, catalogue, source paths and interactive CLI.');
