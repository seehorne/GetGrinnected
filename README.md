# About GetGrinnected

*GetGrinnected* is an iOS and Android app designed to help Grinnell students find out and attend events on and around Grinnell campus. Offering seamless UI that helps sort events based on certain days and event tags allowing you to easily find events you are looking for. Students will sign into accounts with their college emails to confirm their identity as a Grinnell student. This ensures limiting the user base to those who should have access to events limiting potential safety hazards. *GetGrinnected* will also allow students to get in contact with organization leaders to join mailing lists for more direct connections for events. Helping drive engagement with opportunities will benefit campus culture. Helping event organizers advertise and gain attendance and helping students find cool and fun opportunities. 

# Layout of Repository

This repository is a *working*, and *live document*, meaning that any challenges we encounter can be solved through changing these meta-rules and technical decisions (i.e. changing the database that we are using to better fit our needs). 
Our tools: We use google docs as a staging area for many of these textual descriptions of our product and ReadMe.md, Trello as our issue tracking tool, github for our git repository, and [Discord](https://discord.gg/e4PrM4RyEr) as our means of communication. 

The structure of our Repository is as follows: 

## Sprint Reports

- `written reports/` - Markdown write-ups for class milestones
  - `requirements.md` - Living requirements document
  - `sprint #/` - directory containing writeups for a specific sprint
    - `agile-planning.md` - Agile planning (goal: name planned outcomes)
    - `agile-review.md` - Agile review (goal: show what has been completed)
    - `sprint-journal.md` - Sprint journal (goal: store written work not belonging to an agile component)

## Repo Details

- `LICENSE` - MIT license for all content in the repo
- `README.md` - The file you're reading, explaining the repo

## Source Code

- `src/` - Directory for all code
    - `android` - Kotlin (front-end)
    - `ios` - Swift (front-end)
    - `backend` - Node.js (back-end)
- `package.json` and `package-lock.json` - npm packages and lockfile
- `image-utils` - images/visual elements app will use

## Docs

- `mkdocs.yml` - MKDocs configuration file
- `docs/` - Documentation pages

## Test Procedures

- `test procedures/` - Manual test procedures we are keeping.
  - `[COMPONENT].md` - Test procedure to run for a particular COMPONENT.

# Issue Tracking

Our issue manager is Trello. See our Trello board at <https://trello.com/b/pAnl7SQ3/getgrinnected>.

# Developer Guidelines

These guidelines are here to make development easier and make sure we meet class requirements and standards.

## Branching and Merging

This is the pattern you should follow when you are developing a new code feature.

1. Change to the main branch, and pull any new changes.
2. Create a new branch for your feature. It should be named according to the pattern `<NAME>/<FEATURE>`, such as `almond/deploy-api`.
3. Work on that branch, committing and pushing as needed.
4. When you are done with your feature, create a Pull Request to merge it into main. You will need approval to merge.

## Testing

You are expected to add unit tests where relevant to your code. Review to the sections below for instructions on adding tests for each language used.

### Kotlin

Add tests to a file named matching the class you are testing. For instance, to test the class `com.example.getgrinnected` which has its class file located at the path `app/src/main/java/com/example/getgrinnected/MainActivity.kt` you would create a new test under the path `app/src/test/java/com/example/getgrinnected/MainActivityTest.kt`.

In Android Studio, if you right-click a function name you can choose "Generate > Test" in the menu that it brings up. Default settings should work, but do make sure the test gets generated in the right location.

### Node.js

Add tests under the `test` directory in a `.js` or `.mjs` file as preferred. Note that some official resources such as <https://nodejs.org/en/learn/test-runner/using-test-runner> use `.mjs` files.

There should be exactly one test file per code file, and the naming should be `test_COMPONENT`. For instance, `api.js` is tested by `test/test_api.js`.

When it makes sense to do so, create test suites to group tests by using `describe` imported from `node:test`.

### Swift

Add a new test bundle by going to file > new > target and then searching "test". You will then select Unit Testing Bundle and choose Swift as the language and Swift Testing as the testing system. Set your other settings how you want and click finish. Now go to your test file and import your project.

Now you can add tests to your testing file using this site <https://developer.apple.com/documentation/testing/definingtests> as documentation.

## Continuous Integration

When you create a pull request modifying code files, it will trigger one or more CI pipelines in order to run unit tests.

Pipelines will be run based on which code files you modify. For instance, if you edit files under `src/ios/` only Swift tests will be run, but if you edit both `src/ios/` and `src/backend/` both Swift and Node.js tests will be run.

Pipelines run when pushing to any feature branch, as well as before merging to main.

## Commit Messages

This is an example of a good commit message.

```
Implement a basic homescreen in Swift

Currently, the only buttons on the homescreen are "calendar" and
"favorites", and they only go to blank pages.
```

Here are features you should follow in your commit messages.

- First line is a short, easily readable description. Try to keep it below 50 characters.
- First line uses imperative mode ("Implement", as compared to "Implementing" or  "Implements")
  - It may be helpful to think of this as answering the question "What does this commit do?"
- Any other necessary description is written below, separated by one line. (optional)

## Style Guidelines

Make sure to follow style guidelines relevant to the code you are writing. Style will be reviewed as part of the merge process.

Kotlin:
- <https://developer.android.com/kotlin/style-guide>
- We chose this guideline because it was understandable and well formatted. We will hold each other accountable to these guidelines by reviewing each other's code and commenting when others make a mistake. We will hold ourselves responsible by individually reading the guidelines and trying to follow them to the best of our ability.  

Swift:
- <https://google.github.io/swift/>
- We chose this guideline because it was understandable and well formatted. We will hold each other accountable to these guidelines by reviewing each other's code and commenting when others make a mistake. We will hold ourselves responsible by individually reading the guidelines and trying to follow them to the best of our ability. 

Node:
- <https://github.com/felixge/node-style-guide>
- We chose this guideline because it was understandable and well formatted. We will hold each other accountable to these guidelines by reviewing each other's code and commenting when others make a mistake. We will hold ourselves responsible by individually reading the guidelines and trying to follow them to the best of our ability. 

# How to Build, Test, and Run this System

## Building

### Kotlin frontend

We use Android Studio for this.

Android Studio doesn't distinguish between building and running code. See the running section below.

### Swift frontend

We use Xcode for this.

Xcode will build and run in one step. See the running section below.

### Node backend

1. Clone this repo.

2. Install Node.js and NPM, the latest LTS versions of each. <https://docs.npmjs.com/downloading-and-installing-node-js-and-npm>

3. In the terminal, `cd` into the repo and run `npm ci` to install all packages we depend on.

This builds all dependencies of the backend.

## Testing

### Kotlin frontend

1. In Android Studio, select the drop down for build type.

2. Switch the build type to "All Tests."

3. Click the green run button.

Or, run from (mostly) the command line using Gradle.

1. In Android Studio, run something (e.g. tests, app) on a virtual device so that it sets up the virtual device.

2. Do not close Android Studio, but open a terminal in the repo.

3. Change directories to `src/android/GetGrinnected/MyApplication`.

4. Run `./gradlew connectedCheck`.

### Swift frontend

1. In Xcode, click the Product drop down menu.

2. In the drop down menu click Test.

### Node backend

1. From the top level of the repo, run `npm test`.

## Running

### Kotlin frontend

All Kotlin code is currently built and run from Android Studio.

Follow these steps to build and run Android code.

1. Install Android Studio on your device: <https://developer.android.com/studio/install>

2. Open the `src/android/GetGrinnected/MyApplication` directory as a project in Android Studio.

3. Set up a device to build for.

    - Physical device: <https://developer.android.com/studio/run/device>
    - Virtual device: <https://developer.android.com/studio/run/managing-avds>

4. On the top bar of Android studio, select the correct device to build for.

5. To the right of the device selector, make sure the dropdown menu for build job is set to 'My_Application.app'.

6. Click the green Run or Play button. When the code is built, it will automatically open the app on the selected device.

### Swift Frontend

All Swift code is currently built and run from Xcode.

Follow these steps to build and run Android code.

1. Search for Xcode in the Apple app store on an apple computer and install it.

2. Open Xcode and click "Open Existing Project...". Open the `src/ios/GetGrinnected` directory as a project.

3. If any files are missing from your Xcode UI, drag them in from the finder.

4. Install the simulator for iOS 17.5 by following these steps:

    1. Window > Devices and Simulators

    2. Press the + in the bottom left corner.

    3. OS Version > Download more simulator runtimes

    4. Press the + in the bottom left corner.

    5. Look for iOS 17.5 and install it.

    6. Create a new simulator using iOS 17.5.

5. Click on the device that is shown in the middle of the top of the Xcode window and switch it to your new simulator.

6. Product > Run

### Node backend

All of these run on a dedicated server, but you could technically run them locally from a computer you have administrative access to.

These instructions are written for a linux machine, but they may work on Mac. It is not reasonable for us to write instructions to run on any non-Linux operating system.

#### Database

##### (For the instructor) Use our database

You should already have access to Reclaim Cloud, so you can use our database.

You can find the login details from the environment file used to run the database connector. This is how to access that file.

1. Log into Reclaim Cloud.

2. Open the project titled "GetGrinnected" with the subtitle "csc324--spring2025.us.reclaim.cloud"

3. Hover over the "Application Servers" area. A "Web SSH" icon should appear that is a small black terminal with green text reading `>_`. Click that icon.

4. In the terminal it opens, run `cd ROOT`.

5. Next, run `less .env` to open the environment file.

6. Note the lines that start with `MYSQL_`.

7. Use these details for your database connection, or copy the entire `.env` file to the same path in the repo you clone to run our code.

##### Create your own database

1. Set up a MySQL server with version 9.2.0.

2. Log into the server as a user that can create new databases.

3. In the mysql prompt, source the file to create the tables. Here, the path is given starting at the top level of the repository.

   ```
   mysql> source src/backend/Database/GetGrinnectedDB.sql
   ```

4. Create a `.env` file at the top level of the repo that has the following contents, according to the dotenv description at <https://www.npmjs.com/package/dotenv>.

    - `MYSQL_HOST` - hostname of the system the MySQL server is running on.
    - `MYSQL_USER` - user to authenticate as
    - `MYSQL_PASSWORD` - that user's password.
    - `MYSQL_DATABASE` - database name, GetGrinnected.

5. Optionally, the `.env` file can have the following options to allow the API to run on HTTPS with proper SSL certificates.

    - `HTTPS_PORT` - the port to attempt to host HTTPS over. for port 443, node must be run with `sudo`.
    - `HTTP_PORT` - the port to attempt to host HTTP over. for port 80, node must be run with `sudo`.
    - `SSL_KEY` - private key for a valid SSL cert.
    - `SSL_CA` - CA certificate for a valid SSL cert.
    - `SSL_CERT` - Certificate for a valid SSL cert.

#### Event scraper

1. Set up a cron job that will run the correct script daily. This is what to put in the crontab, replacing the daily events path with an absolute path.

   ```
   5 5 * * * /usr/bin/bash /PATH/TO/update_daily_events.sh 
   ```

2. Run the script manually in order to kickstart the database with events. This path is given with respect to the top of the repository.

   ```
   /usr/bin/bash /PATH/TO/update_daily_events.sh
   ```

#### API

1. Run the API in a terminal. From the top of the repository, run:

   ```
   node src/backend/api.js
   ```

2. It announces what ports the API is running on. By default it will run HTTP on port 8080 and HTTPS on port 4443, unless
   otherwise specified in the .env file or environment variables.

# Currently Operational Use Cases

This list will be updated as we progress.

- Partial functionality for Use Case 1, Signing Up.

  - It is possible to go through the sign up screen, but it does not check any fields or send any data over the network to create an account.

- Partial functionality for Use Case 3, Finding an Event

  - It is possible to see events on the home screen, but not search.
