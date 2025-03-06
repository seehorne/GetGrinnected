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

# Part 6: Continuous Integration Plan

# Part 7: Test Automation and Continuous Integration Setup
