# Sprint 5 Review Report

**Period:** 4/28 - 5/14

This was a particularly productive sprint for connecting components and really getting work across the finish line. Below will be a division of 3 primary sections: Kotlin, Swift, and API, which will describe the progress made in each sector.

## Kotlin

### Search Page

For context, as a team we decided that continuing with the calendar view did not align as well with the functionality of our app as we had originally envisioned. However something that came up in nearly every stakeholder meeting was a note about the importance of a search bar like feature to easily type in an event name an find it. From this the search page was born to take the place of the calendar.

#### Functionalities

- Users can search by name of event
- Users can search through the description for words of interest say for example "food" would find all events with food in the description.
- Users can search through the orgs that are putting on the event say for example "Board Game Club" would find all events that board game club puts on.
- Users can also search by a given date thus similar to the homepage you can find events on a specific day.

### API Connections

#### Updated / Already Existed but now completed

- Users can now change their username and it will update the remote database account as well.
- The login process and signup process now gets the account from the remote database so that we actually login to the real account stored in the remote database.
- Favorite events are now updated on the remote database so that the user could login on a different device and still have their favorite events.
- Notified events are now updated on the remote database so that the user could login on a different device and still have their notified events.
- We now clean out expired (day of event has passed) events from the local database, this also ensures that a users old favorite events are not still appearing on their favorites screen the day after the event has occurred.

### New Features

- Now on the settings page a user can open the profile section, and edit their email, this brings a popup to edit the email and when they confirm this change it will navigate them to the verification page and on a successful submission of the code sent to their new email they will be brought back to the home page with a changed email.
- A user can now press the delete account text button it will provide them with a prompt confirming they want to delete their account, on press they will be moved to the welcome page and their account will be deleted on the remote database.
- We added a composable referred to as a snackbar that now when a user presses the favorite button on an event it will pop-up and say "this event has been added to your favorites".
- Now when a user who utilizes android talk back opens the app we have descriptive alt text for all images and icons so that our app is explanatory to this use case.
- A user can now go to the Accessibility section of the settings page and press on the size dropdown and it will offer them a variety of font sizes, when they select a different one all text on all screen will increase of decrease accordingly.
- We added unit testing to our functions for username and email validation to ensure their outputs and functionalities acted as we expected.

### Refactorred Features / Screens

- Previous we had a bug when a user would leave the app during verification to check their email to see the code when they came back it would navigate them to the welcome screen. We have fixed this and thus now they will be kept on the verification as you would intuitively think you should be.

The Settings Screen after last sprint required a noticable Revamp as we had decided to move away from the org cards and thus a sectioning of the settings screen was in order. We divided it into the following sections:

- Profile
    - Edit Username
    - Edit Email
- Appearance
    - Dark / Light Mode Switch
- Accessibility
    - Font Size Selector
- Notification
    - A user can change the amount of time before an event they want to be notified (ie if a user choose 5 minutes they will be notified 5 minutes before the start time of the event they have notifications on for)
- About
    - This section includes a blurb about us the developers, acknowledgement and thanks to stakeholders and professor.
    - Icons that when pressed take the user to our github and discord respectively.
- Delete Account Button
- Log out Button

## Swift

### API Connections

#### Connection Features

- Users can now change their username by changing their username in the settings page and pressing the submit button which will update the account username at the remote db.
- Favorite events are now updated on the remote database so that the user could login on a different device and still have their favorite events.
- Notified events are now updated on the remote database so that the user could login on a different device and still have their notified events.
- Now on the settings page a user can open the profile section, and edit their email, when they change it and press the submit button (so long as the email input is valid and meets our standards) a box will pop up on the screen that will ask for the code that was sent to their email. When they enter the code (so long as it is the correct code) thier email will be changed at the remote database and thier account will be linked to this new email.

### New Features

- A user can now press the delete account text button it will provide them with a prompt confirming they want to delete their account, on press they will be moved to the welcome page and their account will be deleted on the remote database.
- A user can now go to the Accessibility section of the settings page and press on the size dropdown and it will offer them a variety of font sizes, when they select a different one all text on all screen will increase of decrease accordingly.
- Event cards now have a share icon which when pressed allows the user to copy and send the information in the event card to a friend (Eventually when the app is on the store this will allow them to be sent to an embedded link with in the app that would pop up with the specific event card).
- Notifications now work as intended meaning a user can press the notification icon and they will be alert x number of minutes before the event starts (x is the number of minutes they have set for this setting in the settings page).

### Search Page

For context, as a team we decided that continuing with the calendar view did not align as well with the functionality of our app as we had originally envisioned. However something that came up in nearly every stakeholder meeting was a note about the importance of a search bar like feature to easily type in an event name an find it. From this the search page was born to take the place of the calendar.

#### Functionalities

- A user can search by the name of an event.
- A user can also search by the events they have favorited.
- Generally a user can enter a search prompt and search through events for a match.
- A user can look between a specific date range for a certain event/s
- A user can search for events by the standard tags and time sorting found on the home page and favorites page

### Home Page

- A user can filter by day and select days through the week view (horizontal scrolling of dates) date selector for date.
- A user can apply tag filters and also a sorting picker to sort by name or by time of that day.

### Favorites Page

All of these are relative to events they have already favorited
- Users can favorite and unfavorite events and they will appear and disappear from the favorites page respectively.
- Users can now search for events via selecting tags from the dropdown.
- Users can now search for events by a certain day / time and only see events at that time.

### Settings Page

The Settings Screen after last sprint required a noticable Revamp as we had decided to move away from the org cards and thus a sectioning of the settings screen was in order. We divided it into the following sections:

- Profile
    - Edit Username
    - Edit Email
- Appearance
    - Dark / Light Mode Switch
- Accessibility
    - Font Size Selector
- Notification
    - A user can change the amount of time before an event they want to be notified (ie if a user choose 5 minutes they will be notified 5 minutes before the start time of the event they have notifications on for)
- About
    - This section includes a blurb about us the developers, acknowledgement and thanks to stakeholders and professor.
    - Icons that when pressed take the user to our github and discord respectively.
- Delete Account Button
- Log out Button

### Auth

Swift now utilizes authorized api calls via the token system almond setup to ensure that a user is properly logged in and that they make the legal calls with valid access tokens. If the access token is expired a refreshToken call will be made to and so long as the refresh token is valid a new access token will be recieved. This ensures we have secure api calls.

## API

A general note is that the API docs have also been maintained with the additions of new calls and updates to the structure of the files related to the API calls.

See Docs: [https://getgrinnected.sites.grinnell.edu/docs/dev/api-v1/](https://getgrinnected.sites.grinnell.edu/docs/dev/api-v1/)

### New Calls / Updates

- **Delete User:** This call is used by the front end to delete a user from our remote database
- **Change Email:** This call is actually a two part composition:
    - Call with the new email that checks the that the new email is valid and if so it sends the OTP to the new email destination
    - Verify call when it recieves the code if this code is valid then it updates the email in our remote database.
- **Change Username:** This call is used by the front end to update a user's username that already exists in the database to new valid username.
- Created Demo Account for Apple reviewers to use to get testflight approved. This also meant ensuring that this account code pass as a grinnell email even though it doesn't exist thus when is detected it is allowed to bypass verification.
- Function to close our database connection, this avoids runs where we would have hanging runs that would just never conclude because our connection was still established.

### Fixes, Tweaks, and Testing

- Added testing to all implemented api calls and functions to ensure they function as intended.
- Implemented common way of checking for body parameters, which also prevented routes from crashing when no body was provided.
- Tweaked the manner in which we put array information such that if we get an eventID we don't have in our database it is not put into the database but all valid ones are still put.

## Git Diff Link

To see the code change between last sprint and this one, look at this diff link.

If you click the link before the `sprint5` tag is pushed, the diff will appear to be empty.

<https://github.com/seehorne/GetGrinnected/compare/sprint4..sprint5>
