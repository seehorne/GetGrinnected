# Sprint 3 Review Report

**Period:** 4/14 - 4/27

It's not listed as something that is required, but we're predicting you might want a link that shows the diff between this and last sprint. Like with previous sprints, if you click the link before the `sprint4` tag is pushed it will show an empty diff.

<https://github.com/seehorne/GetGrinnected/compare/sprint3..sprint4>

# Kotlin Frontend Functionality

## Installing the app

1. Clone the project from Github.

2. Install Android Studio, and open this path within the GitGrinnected repo as a project: `src/android/GetGrinnected/MyApplication`.

3. Follow these steps to run the app on your device. I recommend connecting to your device over USB, rather than WiFi. <https://developer.android.com/studio/run/device>

    - When you run the app, make sure the dropdown menu at the top (next to the green play and debug icons) is set to "MyApplication.app".

## Login

1. After starting the app, click the "Login" button.

2. Enter an email associated with an existing account (If you don't you will receive an error indicating the account is not found.)

3. Optionally, click the "Join Now" text in the bottom message to jump to the "Sign Up" page.

4. Click the "Login" button to go to the "Verification" page.

## Sign Up

1. After starting the app, click the "Sign up" button.

2. Enter an @grinnell.edu email not currently associated with an account (if it is not a grinnell email it will give an error asking for a grinnell email, if it is empty it will ask for a grinnell email, if it is assigned to an account that already exists it will give an error)

3. Enter a username made of any case letters, any numbers .s or _s but no ..s, or __s and no starting with _s or .s and your username must contain at least on letter.

3. Optionally, click the "Join Now" text in the bottom message to jump to the "Sign Up" page.

4. Click the "Sign up" button to go to the "Verification" page.

## Verification

1. Go to the email you entered on the previous page and wait for a verification code to be sent.

2. Enter the code given (if you enter an invalid code it will give you an error and if you enter a code less than 6 digits it will ask you for a 6 digit code. It will also not allow you to enter more than 6 digits)

3. Click the verify button to go to the "Home" page on a correct code.

4. Optionally, click the "Resend Code" button to be sent a new verification code to your email.

5. Optionally, click the "Cancel" button to navigate back to either the "signup" or "login" page depending on the process you were doing.

## Event cards

1. Click on card shown to expand the description (Click again to close card).

2. Press Heart Icon

    - Notice the Icon becomes filled (You have now favorited the event)
    - The event is now in your favorites page
    - You can press the Icon again and it will be unfavorited

3. Press Bell Icon

    - Notice the Icon becomes filled (You have now have notifications on for the event)
    - Currently this is just an immediate push notification. (We hope to have these push notification some duration before the event occurs).

## Home

1. Look at all of the event cards for all events we have in the database currently.

2. Click on any of the event cards (see event card functionalities).

3. Optionally, click the date on the top bar to open a dropdown and change which date is being shown.

    - Notice that the events displayed switch to displaying events only on that day.

4. Optionally, click the tags on the top bar to open a dropdown for checkboxes and select boxes.

    - Notice that the events displayed switch to displaying events only with the given tags selected

5. Optionally, click the 'Unselect all' in the checkboxes.

    - Notice all tags are unselected and now all events are displayed

5. Optionally, scroll through the list of cards.

6. Click any of the other icons on the bottom bar to go to their pages.

## Calendar (This entire section may be removed due to inviability and for the sake of progress on more pertinent features)

1. Click the "Week" button in the top left to open a dropdown and change between Day, Week, and Month views.

3. Notice that the background text changes for each view.

4. On the "Month" view tap on a day on the calendar.

5. Notice that the bottom of the screen displays that day and a scrollable field with event cards on that day.

6. Click on any of the event cards (see event card functionalities).

8. Optionally, scroll through the list of cards.

9. Optionally, click the tags on the top bar to open a dropdown for checkboxes and select boxes.

    - Notice that the events displayed switch to displaying events only with the given tags selected (currently you must switch to another day to see the filter effect)

10. Click any of the other icons on the bottom bar to go to their pages.

## Favorites

1. Click on any of the event cards (see event card functionalities).

2. Optionally, click on the heart next to an event to unselect it.

    - Notice the event disappears from the favorites page.

3. Optionally, scroll through the list of cards.

4. Click any of the other icons on the bottom bar to go to their pages.

## Settings

1. Click on Switch Account button (Switch cycle) to switch accounts.

    - Currently, this will have no effect on visuals and does nothing.
    - Note: We have moved away from this feature as maybe being viable

2. Click on pencil icon by the username to change your username

     - Notice a prompt pops up to change your username
     - Optionally, press cancel button to not make changes to username
     - Optionally, click save, notice that the prompt disappears and the username is now the updated username you entered.

4. Click on switch next to dark/light mode to change between modes on the app.

     - Notice that the app pages switch to a new theme (light or dark respectively)

Note: 5 through 7 pertain to features we may scrape due to viability and lack of necessity for MVP
5. Optionally, scroll through the list of org cards.

6. Click on any of the org cards shown to expand the and see an about section.

7. Optionally, click on the heart next to an event to unselect it.

    - Currently, unhearting an event does not do anything.

8. Click any of the other icons on the bottom bar to go to their pages.

# Swift Frontend

## Installing the app

1. Clone the project from Github.

2. Install Xcode, and open this path within the GitGrinnected repo as a project: `src/ios/GetGrinnected`.

3. Follow these steps to run the app on your device. <https://developer.apple.com/documentation/xcode/running-your-app-in-simulator-or-on-a-device>

## Login

1. This is the page the app starts on.

1. Enter an @grinnell.edu email that is current associated with an account

2. Optionally, tap the "Don't have an account?" text in the bottom message to jump to the "Sign Up" page.

3. Tap the "Login" button to go to the "Verification" page.

## Sign Up

1. After starting the app, tap the "Don't have an account" button to get here.

2. Enter a username made of any case letters, any numbers .s or _s but no ..s, or __s and no starting with _s or .s and your username must contain at least on letter.

3. Enter an @grinnell.edu email not currently associated with an account (if it is not a grinnell email it will give an error asking for a grinnell email, if it is empty it will ask for a grinnell email, if it is assigned to an account that already exists it will give an error)

4. Optionally, tap the "Back" button in the top left to jump to the "Login" page.

5. Tap the "Sign In" button to go to the "Verification" page.

## Verification

1. Go to the email you entered on the previous page and wait for a verification code to be sent.

2. Enter the code given (if you enter an invalid code it will give you an error and if you enter a code less than 6 digits it will ask you for a 6 digit code. It will also not allow you to enter more than 6 digits)

3. Tap the verify button to go to the "Home" page on a correct code.

4. Optionally, tap the "Resend Code" button to be sent a new verification code to your email.

5. Optionally, tap the "Cancel" button to navigate back to either the "signup" or "login" page depending on the process you were doing.

## Event cards

1. Tap on card shown to expand the description (Tap again to close card).

2. Tap Heart Icon

    - Icon becomes filled (You have now favorited the event)
    - The event is now in your favorites page
    - You can press the Icon again and it will be unfavorited

3. Tap Bell Icon

    - Icon currently does not become filled
    - Currently no push notification

## Home

1. See events on the current day viewed.

2. Change currently viewed day. (Did change currently viewed day, got broken during refactoring for caching)

3. Change what tag you are filtering by. (Currenting in progress)

4. See the events that are happening today and scroll through them.

5. Move to this page by pressing the home button on the bottom.

6. Move to other pages by pressing their buttons on the bottom.

## Calendar (This page may end up being removed or combined with the home page)

1. See the events on the current day viewed.

2. Change currently viewed day. Can be done by tapping the dropdown showing the day or by swiping the week part and tapping a day.

3. See the events that are happening today and scroll through them.

4. Move to this page by pressing the calendar button on the bottom.

5. Move to other pages by pressing their buttons on the bottom.

## Favorites

1. See events that you have favorited and scroll through them.

2. Move to this page by pressing the favorites button on the bottom.

3. Move to other pages by pressing their buttons on the bottom.

## Settings

1. Change username by tapping username. (Currently not implemented)

2. Switch between light and dark mode.

3. Move to this page by pressing the profile button on the bottom.

4. Move to other pages by pressing their buttons on the bottom.

# Invisible Progress

## Caching

Both Frontends have worked this sprint to implement a caching system for information coming from the API. Ideally this allows our app to be utilized off line with the most recent sync information from the API. Additionally we can hopefully utilize querying or storing systems that make persistent states simpler. For Kotlin we implemented a local db that syncs with the repository to store event and account related information. Additionally we have Storage Preferences associated with our access and refresh tokens, whether we are currently logged in, the current account we are logged in with, and the theme we currently have set. These states allow us to have persistent authorized login so users do not need to continually log in each time they open the app. These hold for things like our Theme state that ensures we start the app in the correct theme. Finally we have a pull to refresh functionality that allows us to resync with the API when we pull down on the homepage as a manual refresh (Otherwise we resync with the API every 3 hours).

## API

APIs are naturally not very visible from a user's perspective, and the API for our app has come a long way during this sprint. At the last demo most of the mechanics happening on the frontend were just faked (logging in, choosing favorites, etc) but now we are completely connected to the API for logging in on both frontends and the correct routes exist to allow for setting user data like favorite events and usernames. Because the API functionality has taken a lot longer to develop than we anticipated, the frontends do not use those routes. We talk about this as part of our MVP that must be completed in sprint 5, over in the sprint journal document.

# Git Diff Link

This diff shows the changes we made to our codebase over the course of this sprint: <4>

Note: If you click the link before the `sprint4` tag is pushed to Github, the diff will appear to be empty.
