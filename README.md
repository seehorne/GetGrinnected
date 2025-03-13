# About GetGrinnected

*GetGrinnected* is an iOS and Android app designed to help Grinnell students find out and attend events on and around Grinnell campus. Offering seamless UI that helps sort events based on certain days and event tags allowing you to easily find events you are looking for. Students will sign into accounts with their college emails to confirm their identity as a Grinnell student. This ensures limiting the user base to those who should have access to events limiting potential safety hazards. *GetGrinnected* will also allow students to get in contact with organization leaders to join mailing lists for more direct connections for events. Helping drive engagement with opportunities will benefit campus culture. Helping event organizers advertise and gain attendance and helping students find cool and fun opportunities. 

# Layout of Repository

This repository is a *working*, and *live document*, meaning that any challenges we encounter can be solved through changing these meta-rules and technical decisions (i.e. changing the database that we are using to better fit our needs). 
Our tools: We use google docs as a staging area for many of these textual descriptions of our product and ReadMe.md, Trello as our issue tracking tool, github for our git repository, and [Discord](https://discord.gg/e4PrM4RyEr) as our means of communication. 

The structure of our Repository is as follows: 

- `written reports/` - Markdown write-ups for class milestones
  - `requirements.md` - Living requirements document
  - `sprint #/` - directory containing writeups for a specific sprint
    - `agile-planning.md` - Agile planning (goal: name planned outcomes)
    - `agile-review.md` - Agile review (goal: show what has been completed)
    - `sprint-journal.md` - Sprint journal (goal: store written work not belonging to an agile component)

- `LICENSE` - MIT license for all content in the repo

- `README.md` - The file you're reading, explaining the repo

- `src/` - Directory for all code
    - `android` - Kotlin (front-end)
    - `ios` - Swift (front-end)
    - `backend` - Node.js (back-end)
- `image-utils` - images/visual elements app will use

# Issue Tracking

[Trello](https://trello.com/invite/b/67aa2af610b85d0ead6a8419/ATTI86565b68d11ca1636671d8b646735837A143ECBB/getgrinnected)

# Developer Guidelines

## Branching and Merging

- Make a new branch for each feature
- `main` branch is protected, and you need 2 approvals to merge

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

**TODO: WE NEED SOMEONE FROM SWIFT TEAM TO TALK ABOUT ADDING TESTS.**

## Continuous Integration

When you create a pull request modifying code files, it will trigger one or more CI pipelines in order to run unit tests.

Pipelines will be run based on which code files you modify. For instance, if you edit files under `src/ios/` only Swift tests will be run, but if you edit both `src/ios/` and `src/backend/` both Swift and Node.js tests will be run.
