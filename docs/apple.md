# Android docs
This is your guide to using GetGrinnected on an iOS device

_Looking for Android guide? Click [here](android.md) for Android docs_

# Table of contents
- [General navigation](#general-navigation)
- Pages/features
    - [Login/Signup](#login-signup)
        - [Login](#login)
        - [Sign up](#sign-up)
        - [Verification](#verification)
    - [Home](#home)
        - [View event details](#view-event-details)
        - [Favorite events](#favorite-events)
        - [Subscribe to event notifications](#subscribe-to-event-notifications)
        - [Filter events by day](#filter-events-by-day)
        - [Filter events by tags](#filter-events-by-tags-home)
    - [Calendar](#calendar)
        - [Day view](#day-view)
        - [Week view](#week-view)
        - [Month view](#month-view)
        - [Filter by tags](#filter-by-tags-calendar)
    - [Favorites](#favorites)
    - [Settings](#settings)
        - [Light and dark mode](#light-and-dark-mode)
        - [Change username](#change-username)
        - [Sign out](#sign-out)
        - [Explore organizations](#explore-organizations)
        - [Follow and unfollow organizations](#follow-and-unfollow-organizations)

# General navigation
- Before navigating to most app features is possible, you must be logged in
    - If you already have an account, see [login page](#login) for instructions
    - Otherwise, see [sign up](#sign-up) for instructions
- Upon logging in, you will default land on the [home page](#home)
- To navigate to other pages, click on the icons in the bottom navigation bar
    - Icons to navigate to the following pages are listed left to right in this order:
        - [Home](#home)
        - [Calendar](#calendar)
        - [Favorites](#favorites)
        - [Settings](#settings)
    - Clicking any nav-bar icon from any page post-login navigates to the page associated with the selected icon
- Back to [table of contents](#table-of-contents)

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

## Verification
- Coming soon! Stay tuned :tada:
    - See restructuring discussed in [login](#login) and [sign up](#sign-up)
- Back to [table of contents](#table-of-contents)

# Home
_Events are sorted in order of start time by day_

## View event details
- Click on an event to expand it and view additional details
- To collapse an event, click it again
- Events start in a collapsed view
- A collapsed event displays:
    - Event name
    - Host organization(s) name(s)
    - Event date
    - Event time
    - Event location
- An expanded event displays:
    - Everything displayed on a collapsed event
    - Event description
    - Event tags
- Back to [table of contents](#table-of-contents)

## Favorite events
- Click the heart icon on an event card to add it to your favorites (UNDER CONSTRUCTION :building_construction:)
    - Under construction because while favorite status is toggleable, it does not yet write to favorites page
- Back to [table of contents](#table-of-contents)

## Subscribe to event notifications
- Click the bell icon on an event card to get reminders and updates about it (UNDER CONSTRUCTION :building_construction:)
    - Under construction because while notification status is toggleable, it does not yet save such that the user is actually notified
- Back to [table of contents](#table-of-contents)

## Filter events by day
- Click the day drop down to see events happening on a specific day (UNDER CONSTRUCTION :building_construction:)
    - Under construction because currently, selecting the day does not change which events are shown
    - Waiting on an effective filtering approach 
- Default view is current day
- Can select any day within the next two weeks
- Back to [table of contents](#table-of-contents)

## Filter events by tags (home)
- Use checkboxes to select tags of interest (UNDER CONSTRUCTION :building_construction:)
    - Under construction because selected tags do not currently update what events are shown
    - Waiting on an effective filtering approach 
- Eventually...
    - No tags selected = everything will show up
    - All tags selected = everything will show up still
    - 1 to (total-1 tags selected) = only selected tags will show up
- Back to [table of contents](#table-of-contents)

# Calendar

_Views can be selected from a view dropdown_

## Day view
- Select individual day to view (UNDER CONSTRUCTION :building_construction:)
    - Under construction due to waiting on the same filtering approach described in [home](#home)
- Back to [table of contents](#table-of-contents)

## Week view
- Default behavior is current week
- Can swipe to see future weeks
- (UNDER CONSTRUCTION :building_construction:)
    - Waiting on the same filtering call as [day view](#day-view)
- Back to [table of contents](#table-of-contents)

## Month view
- Can pull down a calendar to select a different month 
    - Can also select a day within a month to transition into day view
- (UNDER CONSTRUCTION :building_construction:)
    - Waiting on the same filtering call as [day view](#day-view)
- Back to [table of contents](#table-of-contents)

## Filter by tags (calendar)
- Coming soon! Stay tuned :tada:
- Back to [table of contents](#table-of-contents)

# Favorites
- Coming soon! Stay tuned :tada:
- Back to [table of contents](#table-of-contents)

# Settings

## Light and dark mode
- Use slider to toggle between light mode and dark mode
- Back to [table of contents](#table-of-contents)

## Change username
- Type desired username in text box on settings page (UNDER CONSTRUCTION :building_construction:)
    - Under construction because changed username does not currently save, and updated username is not checked for validity by original username rules
- Back to [table of contents](#table-of-contents)

## Sign out
- Coming soon! Stay tuned :tada:
- Back to [table of contents](#table-of-contents)

## Explore organizations
- Coming soon! Stay tuned :tada:
- Back to [table of contents](#table-of-contents)

## Follow and unfollow organizations
- Coming soon! Stay tuned :tada:
- Back to [table of contents](#table-of-contents)

## Switch account
- Coming soon! Stay tuned :tada:
    - This feature will allow switching between personal accounts and org accounts
        - Org leaders will have access to the account for their organization
- Back to [table of contents](#table-of-contents)