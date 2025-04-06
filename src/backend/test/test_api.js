const { it, describe, mock, before, beforeEach, after } = require('node:test');
const assert = require('node:assert/strict');
const request = require("supertest");
const _ = require('lodash');

// require the file to be tested
const api = require('../api');

// return true if the arrays have all the same elements
function arraysEqual(array1, array2) {
    return _.isEmpty(_.xor(array1, array2));
}

describe('Test API', () => {
    // Run API to start tests, and stop it on finish.
    before(() => {
        api.run();
    });
    after(() => {
        api.close();
    });

    describe('GET /', () => {
        it('should return online text', async () => {
            const req = request('localhost:5844');
            const res = await req.get('/');
            assert.strictEqual(res.statusCode, 200);
            assert.strictEqual(res.text, 'API online!');
        });
    });

    /*
    describe('GET /events', () => {
        it('should work with no tags', async () => {
            const req = request('localhost:5844');
            const res = await req.get('/events');
            assert.strictEqual(res.statusCode, 200);
            console.log(JSON.stringify(res));
            //assert.strictEqual(res.text, 'API online!');
        });

        it('should work with existing tags', async () => {
        });

        it('should return error with nonexistent tags', async () => {
        });
    });

    describe('GET /events/between', () => {

    });
    */
});


describe('parseParamDate', () => {
    // Before any tests run, set our timezone to UTC so the Grinnell time will actually be tested.
    before(() => {
        process.env.TZ = 'UTC';
    });

    it('should accept ISO-8601 time', () => {
        const input = '2025-04-05T22:19-0500';
        const expected = 1743909540000;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual);
    });

    it('should respect ISO-8601 timezone', () => {
        const input = '2022-03-12T10:32+1000';
        const expected = 1647045120000;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual);
    });

    it('should assume Grinnell time if timezone unspecified', () => {
        const input = '2025-04-05T22:19';
        const expected = 1743909540000;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual);
    });

    it('should assume Grinnell midnight if no time specified', () => {
        const input = '2025-04-05';
        const expected = 1743829200000;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual);
    });

    it('should not accept clearly malformed time', () => {
        const input = 'this is not a date'
        const expected = NaN;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual);
    });

    it('should not accept dates with no separator', () => {
        const input = '20250405'
        const expected = NaN;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual);
    });

    it('should not accept Unix time', () => {
        const input = '1743909495';
        const expected = NaN;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual);
    });
});

describe('parseQueryTags', () => {
    it('should ignore empty tags', () => {
        const input = null;
        const expected = [];
        const actual = api.parseQueryTags(input);
        assert.ok(arraysEqual(expected, actual), `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`);
    });

    it('should accept one tag', () => {
        const input = 'a';
        const expected = ['a'];
        const actual = api.parseQueryTags(input);
        assert.ok(arraysEqual(expected, actual), `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`);
    });

    it('should split one tag, comma separated', () => {
        const input = 'a,b,c';
        const expected = ['a', 'b', 'c'];
        const actual = api.parseQueryTags(input);
        assert.ok(arraysEqual(expected, actual), `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`);
    });

    it('should accept multiple tags, in a list', () => {
        const input = ['a', 'b', 'c'];
        const expected = ['a', 'b', 'c'];
        const actual = api.parseQueryTags(input);
        assert.ok(arraysEqual(expected, actual), `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`);
    });

    it('should split a mix of list and comma-separated tags', () => {
        const input = ['a', 'b,c', 'd'];
        const expected = ['a', 'b', 'c', 'd'];
        const actual = api.parseQueryTags(input);
        assert.ok(arraysEqual(expected, actual), `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`);
    });
});
