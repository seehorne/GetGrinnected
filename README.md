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

# Developer guidelines

- Make a new branch for each feature
- `main` branch is protected, and you need 2 approvals to merge
