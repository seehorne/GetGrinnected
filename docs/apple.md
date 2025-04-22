# Table of contents
- [Login/Signup](#login-signup)
    - [Login](#login)
    - [Sign up](#sign-up)
- [Home](#home)
    - [View event details](#view-event-details)
    - [Favorite events](#favorite-events)
    - [Subscribe to event notifications](#subscribe-to-event-notifications)
    - [Filter events by day](#filter-events-by-day)
    - [Filter events by tags](#filter-events-by-tags-home)
- [Calendar](#calendar)
- [Favorites](#favorites)
- [Settings](#settings)


# Login-Signup

## Login
- Use this option if you already have an account
- Type Grinnell email into the text box requesting email
- Type password into text box requesting password (UNDER CONSTRUCTION :building_construction:)
    - Under construction, because password will not always be needed
    - Currently moving towards using passcode sent to email instead
- Press button to "Sign in" (UNDER CONSTRUCTION :building_construction:)
    - Does not currently verify the information is associated with account
    - Waiting on local database
- Successful sign in redirects to [home page](#home)
- Back to [table of contents](#table-of-contents)

## Sign up
- Use this option if you do not yet have an account
- Flow starts on [login page](#login)
- Click the "Don't have an account" button, redirected to [sign up page](#sign-up)
- Fill in the text boxes requesting a username, Grinnell email, and password (UNDER CONSTRUCTION :building_construction:)
    - Username rules are as follows:
        - allowed chars `a-z` `_` and `.`
        - no `__` `..` `_.` or `._`
        - no ending or starting with `.` or `_`
        - at least 8 characters
    - Password rules as follows:
        - Must be at least 8 characters
    - Under construction because, like in [login](#login), we will be moving towards emailed passcodes over passwords
- Submit
    - Checks for valid Grinnell email and password (UNDER CONSTRUCTION :building_construction:)
        - Under construction because checks for valid construct, but does not yet check for if the account exists or not, waiting on local database
- Back to [table of contents](#table-of-contents)

# Home
_Events are sorted in order of start time by day_

## View event details
- Click on an event to expand it and view additional details, such as description
- To collapse an event, click it again
- Back to [table of contents](#table-of-contents)

## Favorite events
- Click the heart icon on an event card to add it to your favorites (UNDER CONSTRUCTION :building_construction:)
    - Under construction because while favorite status is toggleable, it does not yet write to favorites page

## Subscribe to event notifications
- Click the bell icon on an event card to get reminders and updates about it (UNDER CONSTRUCTION :building_construction:)
    - Under construction because while notification status is toggleable, it does not yet save such that the user is actually notified

## Filter events by day
- Click the day drop down to see events happening on a specific day (UNDER CONSTRUCTION :building_construction:)
    - Under construction because currently, selecting the day does not change which events are shown
- Default view is current day

## Filter events by tags (home)
- Use checkboxes to select tags of interest (UNDER CONSTRUCTION :building_construction:)
    - Under construction because selected tags do not currently update what events are shown
- Eventually...
    - No tags selected = everything will show up
    - All tags selected = everything will show up still
    - 1 to (total-1 tags selected) = only selected tags will show up
- Back to [table of contents](#table-of-contents)


# Calendar

# Favorites
(UNDER CONSTRUCTION :building_construction:)

# Settings