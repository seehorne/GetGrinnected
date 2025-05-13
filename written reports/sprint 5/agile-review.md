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

## API



## Git Diff Link

To see the code change between last sprint and this one, look at this diff link.

If you click the link before the `sprint5` tag is pushed, the diff will appear to be empty.

<https://github.com/seehorne/GetGrinnected/compare/sprint4..sprint5>
