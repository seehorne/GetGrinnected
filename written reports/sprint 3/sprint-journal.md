# Requirements Changes

To see changes in our requirements document, look at the difference from the start of the sprint, and then search for `written reports/requirements.md`.

<http://github.com/seehorne/GetGrinnected/compare/sprint2..sprint3>

# Non User-Facing Progress

I feel it's worth saying that these lists do not reflect the full scope of what we've worked on during this sprint, only what we have kept after exploring many options.

It's like looking at the diff of an entire merge request, where you don't get the detail from the individual commits.

## Database

- Database has been created with correct columns according to plan
- Database can insert events from scraped data
- Database can drop events that the scraper marks as expired
- Database can return all events, or filter them by tags
- Database can get and create accounts
- Database can very a user's password is correct

## Event Scraping

- Scraper can fully get information from events.grinnell.edu
- Scraper writes information to files for the database to interact with
- Scraper handles expired events and new events

## API

- API runs HTTP and HTTPS servers, configurable or default ports.
- API loads SSL from file
- API fully supports getting events and getting events by tag
- API can parse tags and dates from URL queries and parameters
- API has routes for getting events by date, but they don't work yet
- API can be tested automatically, except for database connection
- API has manual testing plan for deployment to make sure it is functioning

# Demo Code Git Tag

The tag for the demo code is `sprint3`.

You can see all changes in the repo since sprint 2 at the following link:

<http://github.com/seehorne/GetGrinnected/compare/sprint2..sprint3>

# Manual Testing Rationale

## API deployment

We use manual testing to confirm that the API has been deployed successfully, in addition to the automated tests that already run on the API.

The manual testing procedure is at `testing procedures/api.md` from the top level of the repo.

This testing cannot be done automatically because it tests the integration between the API and the Database, which can only be tested when it is deployed on the server with our current configuration.
