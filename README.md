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

- `mkdocs.yml` - MKDocs configuration file
- `docs/` - Documentation pages

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

Add a new test bundle by going to file > new > target and then searching "test". You will then select Unit Testing Bundle and choose Swift as the language and Swift Testing as the testing system. Set your other settings how you want and click finish. Now go to your test file and import your project.

Now you can add tests to your testing file using this site <https://developer.apple.com/documentation/testing/definingtests> as documentation.

## Continuous Integration

When you create a pull request modifying code files, it will trigger one or more CI pipelines in order to run unit tests.

Pipelines will be run based on which code files you modify. For instance, if you edit files under `src/ios/` only Swift tests will be run, but if you edit both `src/ios/` and `src/backend/` both Swift and Node.js tests will be run.

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

## Branching and Merging

There are three branches that will always exist:

- `main` - production branch. 2 approvals required to merge.
- `dev` - development branch
- `writing` - assignment writing and documentation branch

Feel free to create feature branches for your work, especially if it's more than a single commit or two. Make sure to merge these branches into `dev` or `writing` as relevant before attempting to merge them into `main`.

## Kotlin Guidelines <https://developer.android.com/kotlin/style-guide>
We chose this guideline because it was understandable and well formatted. We will hold each other accountable to these guidelines by reviewing each other's code and commenting when others make a mistake. We will hold ourselves responsible by individually reading the guidelines and trying to follow them to the best of our ability.  

## Swift Guidelines <https://google.github.io/swift/>
We chose this guideline because it was understandable and well formatted. We will hold each other accountable to these guidelines by reviewing each other's code and commenting when others make a mistake. We will hold ourselves responsible by individually reading the guidelines and trying to follow them to the best of our ability. 

## Node Guidelines <https://github.com/felixge/node-style-guide>
We chose this guideline because it was understandable and well formatted. We will hold each other accountable to these guidelines by reviewing each other's code and commenting when others make a mistake. We will hold ourselves responsible by individually reading the guidelines and trying to follow them to the best of our ability. 
