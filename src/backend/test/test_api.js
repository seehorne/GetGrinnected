const { test, describe } = require('node:test');
const assert = require('node:assert/strict');
const api = require('../api.js');

describe('API unit tests', () => {

  test('element that exists', () => {
    const expected = {"Title":"“Cannon, People, Pedagogies”","Date":"April 2","Time":"4:30 p.m. - 8 p.m.","StartTimeISO":"2025-04-02T16:30:00-05:00","EndTimeISO":"2025-04-02T20:00:00-05:00","AllDay?":false,"Location":"HSSC A1231 - Multi-purpose Room","Description":"\n  Interactive workshop with Music Department guest Danielle Brown.\n","Audience":["Faculty &amp; Staff","Students"],"Org":"Music Department","Tags":["Music"],"ID":30268}
    const actual = api.get_event_by_id(30268);
    assert.strictEqual(expected, actual);
  });

});
