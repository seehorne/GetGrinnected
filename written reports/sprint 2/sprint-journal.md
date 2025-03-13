# Part 2: Software Architecture

## Decision 1

At least for the time being, we have decided it is most realistic for us to have a single instance of our database. This is reflected in architecture diagram by the existence of only one server component in the bottom layer.

**Alternative:** The alternative is to utilize more than one instance of the database--physically separate copies if possible. This has several pros that are attractive to us:
- Resilience against data loss (e.g. corruption, hardware failure, or disasters that take down servers)
- Better scaling to a larger number of requests
- Each instance can afford to have lower resources without impacting performance significantly

However, there are cons that led to us not choosing this for the time being. The main one is that at this stage, it does not seem possible for us to get access to more than one server that can run a database--especially for free. We may reconsider this if circumstances change. It is a smaller aspect, but such a configuration also has a higher up-front labor cost.

We ended up deciding to make the database an entire layer in order to make switching to a distributed database such as MongoDB easier if we need to reverse this decision later in the process.

## Decision 2

We decided to classify the event finder component on the same layer as the user clients, rather than at some layer closer to the bottom of the system.

**Alternative:** We initially considered categorizing the event finder as part of the same layer as "Presentation (API)." This was motivated by the idea that the event finder has higher priveleges than any users do, and is not tied to an account the same way that users are. However, we decided these pros were outweighed by the cons:

- If the event finder uses its own method of communication with the server, this will likely involve duplicating code in order to do very similar things to what the API must be capable of (adding, editing, and removing events). This duplication isn't desirable.
- It's likely less hard than we thought to give the event finder special privileges working through the API, depending on implementation.
- The event finder doesn't need to interact directly with the business rules layer in order to do its job, so this would be putting it too low for no reason.

# Part 5: Process Description

## 5.1: Risk Assessment

## 5.2: Epics

## 5.3: Product Roadmap

# Part 6: Continuous Integration (CI) Plan

## Test Libraries

There are a few different languages in our tech stack, and each of them is going to need a different testing library.

- **Swift:**: Swift Testing framework
  - This framework is cross-platform and built for CI tasks, unlike some other Swift testing tools built into XCode.
  - <https://developer.apple.com/xcode/swift-testing/>
- **Kotlin:**: JUnit
  - Widely supported, both for different IDEs and CI services.
  - <https://kotlinlang.org/docs/jvm-test-using-junit.html#add-dependencies> (assumes you are using IntelliJ IDEA)
  - <https://developer.android.com/training/testing/local-tests> (assumes you are using Android Studio)
- **Node.js**: Builtin test runner (`node:test`)
  - Builtin is nice, because no extra dependencies!
  - Other runners claim higher speed or more advanced features, but from what we know now our project won't need these.
  - <https://nodejs.org/en/learn/test-runner/using-test-runner>

There are other parts of the project that need to be tested, but this is not part of CI.

## CI Service

We are choosing to use GitHub Actions as our CI service. It's directly linked to the project repo, and doesn't require any additional setup to use besides creating the CI config files.

One of the big reasons we're using it is (once again) the fact that it's built in and well-supported by GitHub. We want to avoid reinventing the wheel when we can, because there's lots to get done and CI is only a small part of it.

Additionally, GitHub Actions has a marketplace of CI actions that do things we need to do. For instance, there's one that runs JUnit tests and makes a report of them whenever you're merging new changes.

# Part 7: Test Automation and Continuous Integration Setup
