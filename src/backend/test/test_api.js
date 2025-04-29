// testing funcs
const { it, describe, before, beforeEach, after } = require('node:test');
const assert = require('node:assert/strict');
const request = require('supertest');
const sinon = require('sinon');

// lodash specific functions
const isEmpty = require('lodash/isEmpty');
const arrayXOR = require('lodash/xor');

// local files
const api = require('../api/api.cjs');
const events = require('../api/routes/events.cjs');
const user = require('../api/routes/user.cjs');
const util = require('../api/utils.cjs');

// return true if the arrays have all the same elements
function arraysEqual(array1, array2) {
    return isEmpty(arrayXOR(array1, array2));
}

describe('Test API', () => {
    var req;
    // Run API to start tests, and stop it on finish.
    before(() => {
        req = request('http://localhost:8080')
        api.run();
    });
    after(() => {
        api.close();
    });

    describe('Unauthenticated', () => {
        describe('GET /', () => {
            it('should return online text', async () => {
                const res = await req.get('/');
                assert.strictEqual(res.statusCode, 200);
                assert.strictEqual(res.text, 'API online!');
            });
        });

        describe('GET /events', () => {
            it('should return all events with no tags', async () => {
                const res = await req.get('/events');

                // Check return status and length (but not content) of response array
                assert.strictEqual(res.statusCode, 200);
                const eventsArray = JSON.parse(res.text);
                assert.strictEqual(eventsArray.length, 3);
            });

            it('should return fewer events with tags', async () => {
                const res = await req.get('/events?tag=odd');

                // Check return status and length (but not content) of response array
                assert.strictEqual(res.statusCode, 200);
                const eventsArray = JSON.parse(res.text);
                assert.strictEqual(eventsArray.length, 2);
            });
        });

        describe('GET /events/between', () => {
            it('should return only events on a certain date', async () => {
                const res = await req.get('/events/between/2025-05-31/2025-06-01');

                // Check return status and length (but not content) of response array
                assert.strictEqual(res.statusCode, 200);
                const eventsArray = JSON.parse(res.text);
                assert.strictEqual(eventsArray.length, 2);
            });

            it('should filter those events with a tag provided', async () => {
                const res = await req.get('/events/between/2025-05-31/2025-06-01?tag=odd');

                // Check return status and length (but not content) of response array
                assert.strictEqual(res.statusCode, 200);
                const eventsArray = JSON.parse(res.text);
                assert.strictEqual(eventsArray.length, 1);
            });

            it('should fail with entirely invalid dates', async () => {
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
    });

    // NOTE: It is not possible to test these routes automatically to my knowledge,
    // because they are too closely tied to the login process and emails.
    // - POST /user/login
    // - POST /user/signup
    // - POST /user/resend-otp
    // - POST /user/verify
    // Instead, they must be incorporated into manual testing.
    // TODO: ADD MANUAL TESTING SECTION ON THIS.

    // the authenticated tests need these tokens, let their scope cover all tests
    var refresh_token;
    var access_token;
    describe('Authenticated', () => {
        before(() => {
            // Fake the user tokens, as if they had logged in.
            const tokens = util.generateUserTokens('email@example.com');
            refresh_token = tokens.refresh;
            access_token = tokens.access;
        });

        describe('Error handling', () => {
            // For GET we only need to know which routes to try.
            const getRoutes = [
                '/user/data',
                '/user/events/favorited',
                '/user/events/notified',
                '/user/username',
            ];
            // For PUT we must provide appropriate body parameters so we don't
            // get caught by check for body parameters unless we want to. 
            const putRoutes = [
                { 'route': '/user/events/favorited', 'body': 'favorited_events' },
                { 'route': '/user/events/notified', 'body': 'notified_events' },
                { 'route': '/user/username', 'body': 'username' }
            ];

            describe('PUT routes', () => {
                for (const item of putRoutes) {
                    it(`PUT ${item.route} should 400 when no body is included`, async () => {
                        // Make the request
                        const res = await req
                            // URL ending
                            .put(item.route)
                            // Headers
                            .set('Authorization', `Bearer ${access_token}`)
                            .set('Content-Type', 'application/json')

                        assert.strictEqual(res.statusCode, 400);
                    });

                    it(`PUT ${item.route} should 400 when body is included without correct key`, async () => {
                        // Create the request body, but omit the correct key.
                        var jsonBody = {};

                        // Make the request
                        const res = await req
                            // URL ending
                            .put(item.route)
                            // Headers
                            .set('Authorization', `Bearer ${access_token}`)
                            .set('Content-Type', 'application/json')
                            // Body
                            .send(jsonBody);

                        assert.strictEqual(res.statusCode, 400);
                    });

                    it(`PUT ${item.route} should 401 when token is not provided`, async () => {
                        // Create the request body to have the required key
                        var jsonBody = {};
                        jsonBody[item.body] = 'fake_but_present';

                        // Make the request
                        const res = await req
                            // URL ending
                            .put(item.route)
                            // Headers
                            .set('Content-Type', 'application/json')
                            // Body
                            .send(jsonBody);

                        assert.strictEqual(res.statusCode, 401);
                    });

                    it(`PUT ${item.route} should 403 when the token is not good`, async () => {
                        // Create the request body to have the required key
                        var jsonBody = {};
                        jsonBody[item.body] = 'fake_but_present';

                        // Make the request
                        const res = await req
                            // URL ending
                            .put(item.route)
                            // Headers
                            .set('Authorization', `Bearer invalid_token`)
                            .set('Content-Type', 'application/json')
                            // Body
                            .send(jsonBody);

                        assert.strictEqual(res.statusCode, 403);
                    });

                    it(`PUT ${item.route} should 404 when the user does not exist`, async () => {
                        // Generate tokens for a user that's not really in the db.
                        const fakeTokens = util.generateUserTokens('fake@example.com');

                        // Create the request body to have the required key
                        var jsonBody = {};
                        jsonBody[item.body] = 'fake_but_present';

                        // Make the request
                        const res = await req
                            // URL ending
                            .put(item.route)
                            // Headers
                            .set('Authorization', `Bearer ${fakeTokens.access}`)
                            .set('Content-Type', 'application/json')
                            // Body
                            .send(jsonBody);

                        assert.strictEqual(res.statusCode, 404);
                    });
                }
            });

            describe('GET routes', () => {
                for (const route of getRoutes) {
                    it(`GET ${route} should 401 when token is not provided`, async () => {
                        // Make the request
                        const res = await req
                            // URL ending
                            .get(route)
                            // Headers
                            .set('Content-Type', 'application/json');

                        assert.strictEqual(res.statusCode, 401);
                    });

                    it(`GET ${route} should 403 when the token is not good`, async () => {
                        // Make the request
                        const res = await req
                            // URL ending
                            .get(route)
                            // Headers
                            .set('Authorization', `Bearer invalid_token`)
                            .set('Content-Type', 'application/json');

                        assert.strictEqual(res.statusCode, 403);
                    });

                    it(`GET ${route} should 404 when the user does not exist`, async () => {
                        // Generate tokens for a user that's not really in the db.
                        const fakeTokens = util.generateUserTokens('fake@example.com');

                        // Make the request
                        const res = await req
                            // URL ending
                            .get(route)
                            // Headers
                            .set('Authorization', `Bearer ${fakeTokens.access}`)
                            .set('Content-Type', 'application/json');

                        assert.strictEqual(res.statusCode, 404);
                    });
                }
            });
        });

        describe('POST /user/token-refresh', () => {
            it('should return new tokens', async () => {
                const res = await req
                    .post('/user/token-refresh')
                    .set('Authorization', `Bearer ${refresh_token}`)
                    .set('Content-Type', 'application/json');

                // Convert response into a JSON object, make sure it has the right keys
                const object = JSON.parse(res.text);
                assert.strictEqual(object.message, "Successfully refreshed");
                assert.notStrictEqual(object.refresh_token, undefined);
                assert.notStrictEqual(object.access_token, undefined);

                // Since we just changed the tokens, update the higher-scoped vars.
                refresh = object.refresh_token;
                access = object.access_token;
            });

            // NOTE: This route's errors are checked separately from the
            // paramaterized tests because they use the refresh token,
            // not the auth token.
            it('should 401 when the token is not included', async () => {
                const res = await req
                    .post('/user/token-refresh')
                    .set('Authorization', `Bearer not_a_real_token_lol`)
                    .set('Content-Type', 'application/json');
                assert.strictEqual(res.statusCode, 403);
            });

            it('should 403 when the token is not good', async () => {
                const res = await req
                    .post('/user/token-refresh')
                    .set('Authorization', `Bearer not_a_real_token_lol`)
                    .set('Content-Type', 'application/json');
                assert.strictEqual(res.statusCode, 403);
            });

            it('should 404 when the user does not exist', async () => {
                const fakeTokens = util.generateUserTokens('fake@example.com');
                const res = await req
                    .post('/user/token-refresh')
                    .set('Authorization', `Bearer ${fakeTokens.refresh}`)
                    .set('Content-Type', 'application/json');
                assert.strictEqual(res.statusCode, 404);
            });
        });

        describe('GET /user/data', () => {
            // TODO: WRITE THE TESTS
            it('should return data for the current user', () => {
                const res = null;
            });
        });

        describe('GET /user/events/favorited', () => {
            // TODO: WRITE THE TESTS
        });

        describe('GET /user/events/notified', () => {
            // TODO: WRITE THE TESTS
        });

        describe('GET /user/username', () => {
            // TODO: WRITE THE TESTS
        });

        describe('PUT /user/events/favorited', () => {
            // TODO: WRITE THE TESTS
        });

        describe('PUT /user/events/notified', () => {
            // TODO: WRITE THE TESTS
        });

        describe('PUT /user/username', () => {
            // TODO: WRITE THE TESTS
        });
    });
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
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('should respect non-Grinnell ISO-8601 timezones', () => {
        const input = '2022-03-12T10:32+1230';
        const expected = new Date(input);
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('should assume Grinnell time if timezone unspecified', () => {
        const input = '1999-01-01T08:19';
        const expected = new Date(input + '-0500');
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('should assume Grinnell midnight if no time specified', () => {
        const input = '2025-08-04';
        const expected = new Date(input + 'T00:00-0500');
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('should reject clearly malformed time', () => {
        const input = 'this is not a date'
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('should reject date with seconds', () => {
        const input = '2021-05-10T10:30:53-0500'
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('should reject dates with no separator', () => {
        const input = '20250405'
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('should reject Unix time', () => {
        const input = '1743909495';
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('should reject natural language formatted dates', () => {
        const input = 'March 3, 2002';
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });
});

describe('parseQueryTags', () => {
    it('should ignore empty tags', () => {
        const input = null;
        const expected = [];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('should accept one tag', () => {
        const input = 'a';
        const expected = ['"a"'];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('should split one tag, comma separated', () => {
        const input = 'a,b,c';
        const expected = ['"a"', '"b"', '"c"'];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('should accept multiple tags, in a list', () => {
        const input = ['a', 'b', 'c'];
        const expected = ['"a"', '"b"', '"c"'];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('should split a mix of list and comma-separated tags', () => {
        const input = ['a', 'b,c', 'd'];
        const expected = ['"a"', '"b"', '"c"', '"d"'];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });
});