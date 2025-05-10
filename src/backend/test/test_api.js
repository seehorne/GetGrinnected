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

    /*
     * CHECK ALL NON-AUTHENTICATED API CALLS
     */

    describe('Unauthenticated', () => {
        describe('GET /', () => {
            it('returns online text', async () => {
                const res = await req.get('/');
                assert.strictEqual(res.statusCode, 200, res.text);
                assert.strictEqual(res.text, 'API online!');
            });
        });

        describe('GET /events', () => {
            it('returns all events with no tags', async () => {
                const res = await req.get('/events');

                // Check return status and length (but not content) of response array
                assert.strictEqual(res.statusCode, 200, res.text);
                const eventsArray = JSON.parse(res.text);
                assert.strictEqual(eventsArray.length, 3, res.text);
            });

            it('returns fewer events with tags', async () => {
                const res = await req.get('/events?tag=odd');

                // Check return status and length (but not content) of response array
                assert.strictEqual(res.statusCode, 200, res.text);
                const eventsArray = JSON.parse(res.text);
                assert.strictEqual(eventsArray.length, 2, res.text);
            });
        });

        describe('GET /events/between', () => {
            it('returns only events on a certain date', async () => {
                const res = await req.get('/events/between/2025-05-31/2025-06-01');

                // Check return status and length (but not content) of response array
                assert.strictEqual(res.statusCode, 200, res.text);
                const eventsArray = JSON.parse(res.text);
                assert.strictEqual(eventsArray.length, 2, res.text);
            });

            it('filters those events with a tag provided', async () => {
                const res = await req.get('/events/between/2025-05-31/2025-06-01?tag=odd');

                // Check return status and length (but not content) of response array
                assert.strictEqual(res.statusCode, 200, res.text);
                const eventsArray = JSON.parse(res.text);
                assert.strictEqual(eventsArray.length, 1, res.text);
            });

            it('fails with entirely invalid dates', async () => {
                const res = await req.get('/events/between/20210304/20210306');
                assert.strictEqual(res.statusCode, 400, res.text);
                assert.strictEqual(
                    res.text,
                    JSON.stringify({
                        'error': 'Invalid date',
                        'message': 'Start and end date could not be read properly.'
                    }),
                );
            });

            it('fails with invalid start date', async () => {
                const res = await req.get('/events/between/20210304/2021-03-06');
                assert.strictEqual(res.statusCode, 400, res.text);
                assert.strictEqual(
                    res.text,
                    JSON.stringify({
                        'error': 'Invalid date',
                        'message': 'Start date could not be read properly.'
                    })
                );
            });

            it('fails with invalid end date', async () => {
                const res = await req.get('/events/between/2021-03-02/20210304');
                assert.strictEqual(res.statusCode, 400, res.text);
                assert.strictEqual(
                    res.text,
                    JSON.stringify({
                        'error': 'Invalid date',
                        'message': 'End date could not be read properly.'
                    })
                );
            });

            it('fails with start date after end date', async () => {
                const res = await req.get('/events/between/2021-03-06/2021-03-02');
                assert.strictEqual(res.statusCode, 400, res.text);
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

    /*
     * CHECK ALL AUTHENTICATED API CALLS
     */

    var refresh_token;
    var access_token;
    describe('Authenticated', () => {
        // Fake the user tokens, as if they had logged in.
        before(() => {
            const tokens = util.generateUserTokens('email@example.com');
            refresh_token = tokens.refresh;
            access_token = tokens.access;
        });

        // Small test: make sure refresh token is not the same as access token.
        it('has different refresh and access tokens', () => {
            assert.notStrictEqual(refresh_token, access_token, 'refresh and access token are the same');
        });

        /*
         * PARAMETERIZED TESTS FOR 400 ERRORS THAT 
         * ALL AUTHENTICATED ROUTES SHARE.
         * 
         * By doing this, we're checking that these routes actually
         * do require authentication properly.
         */

        describe('Error handling', () => {
            // These tests check if our routes handle auth errors properly.
            //
            // A route is (in general) a way the API lets us interact with it.
            // The most important thing is the name, becuase it lets us construct
            // the URL we use to access it.

            // Test all routes that PUT data
            describe('PUT routes', () => {
                // For routes that PUT (write) information, we need to know what
                // information we are writing to test them fully. That's why we add
                // this "body" key within the object.
                const putRouteData = [
                    { 'name': '/user/events/favorited', 'body': 'favorited_events' },
                    { 'name': '/user/events/notified', 'body': 'notified_events' },
                    { 'name': '/user/email', 'body': 'new_email' },
                    { 'name': '/user/username', 'body': 'username' }
                ];

                it('give 400 when no body is included', { concurrency: true }, async t => {
                    const cases = putRouteData.map(async ({ name }) => {
                        await t.test(name, async () => {
                            const res = await req
                                .put(name)
                                .set('Authorization', `Bearer ${access_token}`)
                                .set('Content-Type', 'application/json');

                            assert.strictEqual(res.statusCode, 400, res.text);
                        });
                    });

                    await Promise.allSettled(cases);
                });

                it('give 400 when body is included with wrong key', { concurrency: true }, async t => {
                    const cases = putRouteData.map(async ({ name }) => {
                        await t.test(name, async () => {
                            const res = await req
                                .put(name)
                                .set('Authorization', `Bearer ${access_token}`)
                                .set('Content-Type', 'application/json')
                                .send({}); // send an empty body

                            assert.strictEqual(res.statusCode, 400, res.text);
                        });
                    });

                    await Promise.allSettled(cases);
                });

                it('give 401 when token is not provided', { concurrency: true }, async t => {
                    const cases = putRouteData.map(async ({ name, body }) => {
                        var jsonBody = {}
                        jsonBody[body] = 'fake_but_present';

                        await t.test(name, async () => {
                            const res = await req
                                .put(name)
                                .set('Content-Type', 'application/json')
                                .send(jsonBody);

                            assert.strictEqual(res.statusCode, 401, res.text);
                        });
                    });

                    await Promise.allSettled(cases);
                });

                it('give 403 when the token is not good', { concurrency: true }, async t => {
                    const cases = putRouteData.map(async ({ name, body }) => {
                        var jsonBody = {}
                        jsonBody[body] = 'fake_but_present';

                        await t.test(name, async () => {
                            const res = await req
                                .put(name)
                                .set('Content-Type', 'application/json')
                                .set('Authorization', `Bearer invalid_token`)
                                .send(jsonBody);

                            assert.strictEqual(res.statusCode, 403, res.text);
                        });
                    });

                    await Promise.allSettled(cases);
                });

                it('give 404 when the user does not exist', { concurrency: true }, async t => {
                    const fakeTokens = util.generateUserTokens('fake@example.com');

                    const cases = putRouteData.map(async ({ name, body }) => {
                        var jsonBody = {}
                        jsonBody[body] = 'fake_but_present';

                        await t.test(name, async () => {
                            const res = await req
                                .put(name)
                                .set('Content-Type', 'application/json')
                                .set('Authorization', `Bearer ${fakeTokens.access}`)
                                .send(jsonBody);

                            assert.strictEqual(res.statusCode, 404, res.text);
                        });
                    });

                    await Promise.allSettled(cases);
                });
            });

            // Test all routes that GET data.
            // These have to be tested separately because GET behaves differently.
            // Specifically, GET requests don't care about the body of the request so we don't
            // check for that error.
            // (also I don't think I can parameterize doing a get vs a put request)
            describe('GET routes', () => {
                const getRouteNames = [
                    '/user/events/favorited',
                    '/user/username',
                    '/user/events/notified',
                    '/user/data'
                ];

                it('give 401 when token is not provided', { concurrency: true }, async t => {
                    const cases = getRouteNames.map(async (name) => {
                        await t.test(name, async () => {
                            const res = await req
                                .get(name)
                                .set('Content-Type', 'application/json');

                            assert.strictEqual(res.statusCode, 401);
                        });
                    });

                    await Promise.allSettled(cases);
                });

                it('give 403 when token is not provided', { concurrency: true }, async t => {
                    const cases = getRouteNames.map(async (name) => {
                        await t.test(name, async () => {
                            const res = await req
                                .get(name)
                                .set('Content-Type', 'application/json')
                                .set('Authorization', `Bearer invalid_token`);

                            assert.strictEqual(res.statusCode, 403);
                        });
                    });

                    await Promise.allSettled(cases);
                });

                it('give 404 when user does not exist', { concurrency: true }, async t => {
                    const fakeTokens = util.generateUserTokens('fake@example.com');

                    const cases = getRouteNames.map(async (name) => {
                        await t.test(name, async () => {
                            const res = await req
                                .get(name)
                                .set('Content-Type', 'application/json')
                                .set('Authorization', `Bearer ${fakeTokens.access}`);

                            assert.strictEqual(res.statusCode, 404, res.text);
                        });
                    });

                    await Promise.allSettled(cases);
                });
            });
        });

        /*
         * TEST /session/refresh ROUTE 
         */

        describe('POST /session/refresh', () => {
            it('returns new tokens', async () => {
                const res = await req
                    .post('/session/refresh')
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
            it('gives 401 when the token is not included', async () => {
                const res = await req
                    .post('/session/refresh')
                    .set('Authorization', `Bearer not_a_real_token_lol`)
                    .set('Content-Type', 'application/json');
                assert.strictEqual(res.statusCode, 403, res.text);
            });

            it('gives 403 when the token is not good', async () => {
                const res = await req
                    .post('/session/refresh')
                    .set('Authorization', `Bearer not_a_real_token_lol`)
                    .set('Content-Type', 'application/json');
                assert.strictEqual(res.statusCode, 403, res.text);
            });

            it('gives 404 when the user does not exist', async () => {
                const fakeTokens = util.generateUserTokens('fake@example.com');
                const res = await req
                    .post('/session/refresh')
                    .set('Authorization', `Bearer ${fakeTokens.refresh}`)
                    .set('Content-Type', 'application/json');
                assert.strictEqual(res.statusCode, 404, res.text);
            });
        });

        describe('GET /user/data', () => {
            it('returns data for the current user', async () => {
                const res = await req
                    .get('/user/data')
                    .set('Authorization', `Bearer ${access_token}`)
                    .set('Content-Type', 'application/json');
                assert.strictEqual(res.statusCode, 200, res.text);
            });
        });

        /*
         * TEST /user/events/notified AND /user/events/favorited,
         * SINCE BOTH NEED TO HANDLE THE SAME ERRORS AS EACH OTHER.
         * 
         * This is a nested parameterized test.
         * - first by type of event (favorited vs notified)
         * - then by valid and invalid inputs for PUT-ing data
         */

        const arrayEventTypes = [
            'favorited',
            'notified'
        ]
        describe(`GET + PUT /user/events/ arrays`, () => {
            it('modifies and gets valid events', async t => {
                const types = arrayEventTypes.map(async (name) => {
                    await t.test(`${name}`, async () => {
                        // Create the body, key depends on current eventType
                        var body = {};
                        body[`${name}_events`] = [1, 2];

                        // Make sure it adds the items
                        const putRes = await req
                            .put(`/user/events/${name}`)
                            .set('Authorization', `Bearer ${access_token}`)
                            .set('Content-Type', 'application/json')
                            .send(body);
                        assert.strictEqual(putRes.statusCode, 200, putRes.text);

                        const getRes = await req
                            .get(`/user/events/${name}`)
                            .set('Authorization', `Bearer ${access_token}`)
                            .set('Content-Type', 'application/json');
                        assert.strictEqual(getRes.statusCode, 200, getRes.text);
                        assert.strictEqual(getRes.text, `{"${name}_events":[1,2]}`);
                    });
                });

                await Promise.allSettled(types);
            });

            it('accepts valid inputs and rejects invalid ones', async t => {
                const testInputs = [
                    // these should match the input because they are valid
                    { 'input': [], 'expected': [] },
                    { 'input': [1], 'expected': [1] },
                    { 'input': [1, 2], 'expected': [1, 2] },
                    // this should filter out the invalid but keep the valid
                    { 'input': [1, 5], 'expected': [1] },
                    // these should reject all the invalid
                    { 'input': [5], 'expected': [] },
                    { 'input': 'a literal string', 'expected': [] },
                    { 'input': 1, 'expected': [] },
                ];

                // this mapping tests over both types of array
                const types = arrayEventTypes.map(async (name) => {
                    await t.test(`${name}`, async tInner => {

                        // this mapping tests over all the test inputs
                        const cases = testInputs.map(async ({ input, expected }) => {
                            await tInner.test(`${JSON.stringify(input)} -> ${JSON.stringify(expected)}`, async () => {
                                // Construct body with differing event type
                                var body = {};
                                body[`${name}_events`] = input;

                                const res = await req
                                    .put(`/user/events/${name}`)
                                    .set('Authorization', `Bearer ${access_token}`)
                                    .set('Content-Type', 'application/json')
                                    .send(body);

                                // all requests should give code 200
                                assert.strictEqual(res.statusCode, 200, res.text);

                                // the response for new_value should match the expected
                                const resObject = JSON.parse(res.text);
                                assert.ok(arraysEqual(resObject.new_value, expected), res.text);
                            });
                        });

                        // wait for all cases to evaluate
                        await Promise.allSettled(cases);
                    });
                });

                // and wait for both types to be evaluated
                await Promise.allSettled(types);
            });
        });

        /*
         * TEST /user/username ROUTE
         *
         * This is totally separate because it doesn't have the same form as the
         * other user data requests. They take arrays, this takes strings.
         */

        describe('GET + PUT /user/username', () => {
            it('modifies and gets account name successfully', async () => {
                const newName = 'new_account_name';

                // Set the username to a valid one.
                const putRes = await req
                    .put('/user/username')
                    .set('Authorization', `Bearer ${access_token}`)
                    .set('Content-Type', 'application/json')
                    .send({ "username": newName });
                assert.strictEqual(putRes.statusCode, 200, putRes.text);

                // Get the username, and make sure it matches what we put there.
                const getRes = await req
                    .get('/user/username')
                    .set('Authorization', `Bearer ${access_token}`)
                    .set('Content-Type', 'application/json');
                assert.strictEqual(getRes.statusCode, 200, getRes.text);
                assert.strictEqual(getRes.text, `{"username":"${newName}"}`);
            });

            it('accepts valid usernames and rejects invalid ones', { concurrency: true }, async t => {
                const usernames = [
                    { 'username': 'a', 'status': 200 },
                    { 'username': 'abc123', 'status': 200 },
                    { 'username': 'ALL.CAPS.NAME', 'status': 200 },
                    { 'username': '1997.word', 'status': 200 },
                    { 'username': 'twen.ty_chars_ex.act', 'status': 200 },
                    { 'username': 'double__underscore', 'status': 400 },
                    { 'username': 'double..period', 'status': 400 },
                    { 'username': '.start_period', 'status': 400 },
                    { 'username': '_start_underscore', 'status': 400 },
                    { 'username': 'end_period.', 'status': 400 },
                    { 'username': 'end_underscore_', 'status': 400 },
                    { 'username': 'twenty_characters_max', 'status': 400 },
                    { 'username': 'inv@l!d char$', 'status': 400 },
                    { 'username': '1997', 'status': 400 },
                    { 'username': '', 'status': 400 }
                ];

                // Map tests using the test context `t` onto the username array
                const cases = usernames.map(async ({ username, status }) => {
                    await t.test(`"${username}"`, async () => {
                        const res = await req
                            .put('/user/username')
                            .set('Authorization', `Bearer ${access_token}`)
                            .set('Content-Type', 'application/json')
                            .send({ 'username': username });

                        assert.strictEqual(res.statusCode, status, res.text);
                    });
                });

                // Await all of the tests to be evaluated
                await Promise.allSettled(cases);
            });
        });

        /*
         * Test user deletion by deleting the user we have in the test database.
         *
         * Because of this change, running the tests locally just got more
         * complicated. You'll need to repopulate the database each time.
         */
        describe('DELETE /user', () => {
            // here I am copy+pasting the auth tests, since this is the only
            // DELETE route that won't save or cost us any extra lines of code.
            describe('does not delete with incorrect authorization', () => {
                const route = '/user'
                it(`gives 401 when token is not provided`, async () => {
                    // Make the request
                    const res = await req
                        // URL ending
                        .delete(route)
                        // Headers
                        .set('Content-Type', 'application/json');

                    assert.strictEqual(res.statusCode, 401, res.text);
                });

                it(`gives 403 when the token is not good`, async () => {
                    // Make the request
                    const res = await req
                        // URL ending
                        .delete(route)
                        // Headers
                        .set('Authorization', `Bearer invalid_token`)
                        .set('Content-Type', 'application/json');

                    assert.strictEqual(res.statusCode, 403, res.text);
                });

                it(`gives 404 when the user does not exist`, async () => {
                    // Generate tokens for a user that's not really in the db.
                    const fakeTokens = util.generateUserTokens('fake@example.com');

                    // Make the request
                    const res = await req
                        // URL ending
                        .delete(route)
                        // Headers
                        .set('Authorization', `Bearer ${fakeTokens.access}`)
                        .set('Content-Type', 'application/json');

                    assert.strictEqual(res.statusCode, 404, res.text);
                });
            });

            // make sure it reports having deleted the user
            it(`deletes the testing user`, async () => {
                const res = await req
                    .delete('/user')
                    .set('Authorization', `Bearer ${access_token}`)
                    .set('Content-Type', 'application/json');

                assert.strictEqual(res.statusCode, 200, res.text);
            });

            // when the user is deleted, their auth should start triggering error 404
            // because they no longer exist in the database.
            it(`stops authenticated methods from working once user deleted`, async () => {
                // Make the request
                const res = await req
                    // I'm just gonna test an arbitrary authenticated method, rather than all.
                    .get('/user/username')
                    // Use our old access token in the header
                    .set('Authorization', `Bearer ${access_token}`)
                    .set('Content-Type', 'application/json');

                // If we don't get a 404 ("no such user"), we're in trouble
                assert.strictEqual(res.statusCode, 404, res.text);
            });
        });
    });
});

/*
 * TEST HELPER FUNCTIONS FROM UTILS
 *
 * For these, the name of the description will match the name of the function.
 * 
 * These tests are really long because they are not parameterized, but at this
 * point it doesn't feel worth changing.
 */

describe('parseParamDate', () => {
    // Before any tests run, set our timezone to UTC so 
    // defaulting to Grinnell time will actually be tested
    // (otherwise it will vary based on computer region)
    before(() => {
        process.env.TZ = 'UTC';
    });

    it('accepts ISO-8601 time unchanged', () => {
        const input = '2025-04-05T22:19-0500';
        const expected = new Date(input);
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('respects non-Grinnell ISO-8601 timezones', () => {
        const input = '2022-03-12T10:32+1230';
        const expected = new Date(input);
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('assumes Grinnell time if timezone unspecified', () => {
        const input = '1999-01-01T08:19';
        const expected = new Date(input + '-0500');
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('assumes Grinnell midnight if no time specified', () => {
        const input = '2025-08-04';
        const expected = new Date(input + 'T00:00-0500');
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected.toISOString(), actual.toISOString());
    });

    it('rejects clearly malformed time', () => {
        const input = 'this is not a date'
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('rejects date with seconds', () => {
        const input = '2021-05-10T10:30:53-0500'
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('rejects dates with no separator', () => {
        const input = '20250405'
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('rejects Unix time', () => {
        const input = '1743909495';
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });

    it('rejects natural language formatted dates', () => {
        const input = 'March 3, 2002';
        const expected = NaN;
        const actual = util.parseParamDate(input);
        assert.strictEqual(expected, actual.valueOf());
    });
});

describe('parseQueryTags', () => {
    it('ignores empty tags', () => {
        const input = null;
        const expected = [];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('accepts one tag', () => {
        const input = 'a';
        const expected = ['"a"'];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('splits one tag, comma separated', () => {
        const input = 'a,b,c';
        const expected = ['"a"', '"b"', '"c"'];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('accepts multiple tags, in a list', () => {
        const input = ['a', 'b', 'c'];
        const expected = ['"a"', '"b"', '"c"'];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });

    it('splits a mix of list and comma-separated tags', () => {
        const input = ['a', 'b,c', 'd'];
        const expected = ['"a"', '"b"', '"c"', '"d"'];
        const actual = util.parseQueryTags(input);
        assert.ok(
            arraysEqual(expected, actual),
            `expected ${JSON.stringify(expected)}, got ${JSON.stringify(actual)}`
        );
    });
});