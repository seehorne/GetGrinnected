const { it, describe } = require('node:test');
const assert = require('node:assert/strict');

describe('name of a testing suite', () => {

  it('should add numbers', () => {
    assert.strictEqual(4, 2+2);
  });

  it('should subtract numbers', () => {
    assert.strictEqual(0, 1-1);
  });

});
