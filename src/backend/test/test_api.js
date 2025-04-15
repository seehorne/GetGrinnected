// testing funcs
const { it, describe, mock, before, after } = require('node:test');
const assert = require('node:assert/strict');
const request = require('supertest');
const sinon = require('sinon');

// lodash specific functions
const isEqual = require('lodash/isEqual');
const isEmpty = require('lodash/isEmpty');
const arrayXOR = require('lodash/xor');

// local files
const api = require('../api');
const db = require('../db_connect'); // so we can mock it

// return true if the arrays have all the same elements
function arraysEqual(array1, array2) {
    return isEmpty(arrayXOR(array1, array2));
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
            const req = request('http://localhost:8080');
            const res = await req.get('/');
            assert.strictEqual(res.statusCode, 200);
            assert.strictEqual(res.text, 'API online!');
        });
    });

    describe('GET /events', () => {
        // Create fake DB methods
        before(() => {
            sinon.stub(db, 'getEvents').callsFake(async () => {
                return 'getEvents';
            });

            sinon.stub(db, 'getEventsWithTags').callsFake(async (_tags) => {
                return 'getEventsWithTags';
            });
        });

        it('should call getEvents when no tags provided', async () => {
            const req = request('http://localhost:8080');
            const res = await req.get('/events');
            assert.strictEqual(res.statusCode, 200);
            assert.strictEqual(
                res.text,
                '"getEvents"'
            );
        });

        it('should call getEventsWithTags when tags are provided', async () => {
            const req = request('http://localhost:8080');
            const res = await req.get('/events?tag=exists');
            assert.strictEqual(res.statusCode, 200);
            assert.strictEqual(
                res.text,
                '"getEventsWithTags"'
            );
        });
    });

    describe('GET /events/between', () => {
        // Create fake DB methods
        before(() => {
            sinon.stub(db, 'getEventsBetween').callsFake(
                async (_start, _end) => {
                    return 'getEventsBetween';
                }
            );

            sinon.stub(db, 'getEventsBetweenWithTags').callsFake(
                async (_start, _end, _tags) => {
                    return 'getEventsBetweenWithTags';
                }
            );
        });

        it('should call getEventsBetween when no tags are provided', async () => {
            const req = request('http://localhost:8080');
            const res = await req.get('/events/between/2021-03-04/2021-03-06');
            assert.strictEqual(res.statusCode, 200);
            assert.strictEqual(
                res.text,
                '"getEventsBetween"'
            );
        });

        it('should call getEventsBetweenWithTags when tags are provided',
            async () => {
                const req = request('http://localhost:8080');
                const res = await req.get('/events/between/2021-03-04/2021-03-06?tag=example');
                assert.strictEqual(res.statusCode, 200);
                assert.strictEqual(
                    res.text,
                    '"getEventsBetweenWithTags"'
                );
            });

        it('should fail with entirely invalid dates', async () => {
            const req = request('http://localhost:8080');
            const res = await req.get('/events/between/20210304/20210306');
            assert.strictEqual(res.statusCode, 400);
            assert.strictEqual(
                res.text,
                JSON.stringify({
                    'error': 'Invalid date',
                    'message': 'Start and end date could not be read properly.'
                })
            );
        });

        it('should fail with invalid start date', async () => {
            const req = request('http://localhost:8080');
            const res = await req.get('/events/between/20210304/2021-03-06');
            assert.strictEqual(res.statusCode, 400);
            assert.strictEqual(
                res.text,
                JSON.stringify({
                    'error': 'Invalid date',
                    'message': 'Start date could not be read properly.'
                })
            );
        });

        it('should fail with invalid end date', async () => {
            const req = request('http://localhost:8080');
            const res = await req.get('/events/between/2021-03-02/20210304');
            assert.strictEqual(res.statusCode, 400);
            assert.strictEqual(
                res.text,
                JSON.stringify({
                    'error': 'Invalid date',
                    'message': 'End date could not be read properly.'
                })
            );
        });

        it('should fail with start date after end date', async () => {
            const req = request('http://localhost:8080');
            const res = await req.get('/events/between/2021-03-06/2021-03-02');
            assert.strictEqual(res.statusCode, 400);
            assert.strictEqual(
                res.text,
                JSON.stringify({
                    'error': 'Bad date order',
                    'message': 'Start date must occur before end date.'
                })
            );
        });

    });

    /*
    describe('GET /events/between', () => {

    });

    todo: between valid dates, assuming that parseParamDate already works

    make sure to try invalid start + end dates to show you get the right errr

    do also dates in the wrong order, also to get the right err
    */
});


describe('parseParamDate', () => {
    // Before any tests run, set our timezone to UTC so 
    // defaulting to Grinnell time will actually be tested
    // (otherwise it will vary based on computer region)
    before(() => {
        process.env.TZ = 'UTC';
    });

    it('should accept ISO-8601 time unchanged', () => {
        const input = '2025-04-05T22:19-0500';
        const expected = new Date(input);
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected.getDate(), actual.getDate());
    });

    it('should respect non-Grinnell ISO-8601 timezones', () => {
        const input = '2022-03-12T10:32+1230';
        const expected = new Date(input);
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('should assume Grinnell time if timezone unspecified', () => {
        const input = '1999-01-01T08:19';
        const expected = new Date(input + '-0500');
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('should assume Grinnell midnight if no time specified', () => {
        const input = '2025-08-04';
        const expected = new Date(input + 'T00:00-0500');
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('should reject clearly malformed time', () => {
        const input = 'this is not a date'
        const expected = NaN;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('should reject date with seconds', () => {
        const input = '2021-05-10T10:30:53-0500'
        const expected = NaN;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('should reject dates with no separator', () => {
        const input = '20250405'
        const expected = NaN;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('should reject Unix time', () => {
        const input = '1743909495';
        const expected = NaN;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('should reject natural language formatted dates', () => {
        const input = 'March 3, 2002';
        const expected = NaN;
        const actual = api.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });
});

describe('parseQueryTags', () => {
    it('should ignore empty tags', () => {
        const input = null;
        const expected = [];
        const actual = api.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('should accept one tag', () => {
        const input = 'a';
        const expected = ['"a"'];
        const actual = api.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('should split one tag, comma separated', () => {
        const input = 'a,b,c';
        const expected = ['"a"', '"b"', '"c"'];
        const actual = api.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('should accept multiple tags, in a list', () => {
        const input = ['a', 'b', 'c'];
        const expected = ['"a"', '"b"', '"c"'];
        const actual = api.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('should split a mix of list and comma-separated tags', () => {
        const input = ['a', 'b,c', 'd'];
        const expected = ['"a"', '"b"', '"c"', '"d"'];
        const actual = api.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });
});
