# Sprint 3 Review Report

**Period:** 4/14 - 4/27

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


# Git Diff Link

This diff shows the changes we made to our codebase over the course of this sprint: <4>

Note: If you click the link before the `sprint4` tag is pushed to Github, the diff will appear to be empty.
