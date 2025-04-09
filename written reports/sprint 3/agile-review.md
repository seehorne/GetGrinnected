# Sprint 3 Review Report

**Period:** 3/31 - 4/8

This sprint's review is what you described in the Sprint 3 Requirements document.

> For each user-facing feature that is operational, describe steps to activate that feature in the user interface. The steps should be clear enough that your instructor can follow them while grading your work, without help from your team.

Since there are two frontends, there are two sets of steps.

# Kotlin Frontend

## Installing the app

1. Clone the project from Github.

2. Install Android Studio, and open this path within the GitGrinnected repo as a project: `src/android/GetGrinnected/MyApplication`.

3. Follow these steps to run the app on your device. I recommend connecting to your device over USB, rather than WiFi. <https://developer.android.com/studio/run/device>

    - When you run the app, make sure the dropdown menu at the top (next to the green play and debug icons) is set to "MyApplication.app".

## Login

1. After starting the app, click the "Login" button.

2. Optionally, type in the "Username" and "Password" field. You can enter anything, currently logins are not checked.

3. Optionally, click the "Join Now" text in the bottom message to jump to the "Sign Up" page.

4. Click the "Login" button to go to the "Home" page.

## Sign Up

1. After starting the app, click the "Sign In" button

2. Optionally, enter text in the fields. They are not checked yet.

3. Optionally, click the "Sign in" text in the bottom message to jump to the "Login" page.

4. Click the "Sign In" button to go to the "Home" page.

## Home

1. Look at all of the event cards for all events we have in the database currently.

2. Click on any of the event cards shown to expand the description.

3. Optionally, click the date on the top bar to open a dropdown and change which date is being shown.

    - Notice that the events displayed switch to displaying events only on that day (Currently events 5 or less hours from the dateline it won't be displayed on the correct page)..

4. Optionally, click the tags on the top bar to open a dropdown for checkboxes and select boxes.

    - Currently, this will have no effect on visuals.

5. Optionally, scroll through the list of cards.

6. Click any of the other icons on the bottom bar to go to their pages.

## Calendar

1. Click the "Week" button in the top left to open a dropdown and change between Day, Week, and Month views.

3. Notice that the background text changes for each view.

4. On the "Month" view tap on a day on the calendar.

5. Notice that the bottom of the screen displays that day and a scrollable field with event cards on that day.

6. Click on any of the event cards shown to expand the description.

7. Optionally, click on the heart next to an event to unselect it.

    - Currently, unhearting an event does not do anything.

8. Optionally, scroll through the list of cards.

9. Click any of the other icons on the bottom bar to go to their pages.

## Favorites

1. Click on any of the event cards shown to expand the description.

2. Optionally, click on the heart next to an event to unselect it.

    - Currently, unhearting an event does not do anything.

3. Optionally, scroll through the list of cards.

4. Click any of the other icons on the bottom bar to go to their pages.

## Settings

1. Click on Switch Account button (Looks like refresh) to switch accounts.

    - Currently, this will have no effect on visuals and does nothing.

2. Click on pencil icon by profile picture to change profile picture.

     - Currently, this will have no effect on visuals and does nothing.

3. Click on pencil icon by the username to change your username

     - Currently, this will have no effect on visuals and does nothing.

4. Click on switch next to dark/light mode to change between modes on the app.

     - Currently, this will have no effect on visuals and will not slide.

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

1. Optionally, type in the "Grinnell Email" and "Password" field. You can enter anything, currently logins are not checked.

2. Optionally, click the "Don't have an account?" text in the bottom message to jump to the "Sign Up" page.

3. Click the "Login" button to go to the "Home" page.

## Sign Up

1. After starting the app, click the "Don't have an account" button to get here.

2. Optionally, enter text in the fields. They are not checked yet.

3. Optionally, click the "Back" button in the top left to jump to the "Login" page.

4. Click the "Sign In" button to go to the "Home" page.

## Home

1. See current day viewed.

2. Change currently viewed day. You can only look up to two weeks in the future.

3. Change what tag you are filtering by.

4. Move to this page by pressing the home button on the bottom.

5. Move to other pages by pressing their buttons on the bottom.

## Calendar

1. Move to this page by pressing the calendar button on the bottom.

2. Move to other pages by pressing their buttons on the bottom.

## Favorites

1. Move to this page by pressing the favorites button on the bottom.

2. Move to other pages by pressing their buttons on the bottom.

## Settings

1. Look at the place holder profile picture.

2. Optionally enter a different email for account. You can enter anything, currently it is not checked.

3. Move to this page by pressing the profile button on the bottom.

4. Move to other pages by pressing their buttons on the bottom.

## What progress have you made that is not visible to a common user?

As instructed, this has been summarized in the Sprint Journal instead. Look under the "Non User-Facing Progress" header.

# Git Diff Link

This diff shows the changes we made to our codebase over the course of this sprint: <https://github.com/seehorne/GetGrinnected/compare/sprint2..sprint3>

Note: If you click the link before the `sprint3` tag is pushed to Github, the diff will appear to be empty.
