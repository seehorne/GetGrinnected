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
