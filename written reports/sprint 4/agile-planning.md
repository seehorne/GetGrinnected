# Sprint 4 planning

## Capacity

### Michael: 3~4/5 beans
- Econ paper draft and midterm next week
- Final art project starting, not super pressing yet

### almond: 4 beans
- Linguistics is a hard class
- Assignments haven’t piled up yet, but they will before the end of the sprint

### Ethan: 3 beans
- Campus wide nerf
- Sala paper

### Ellie: 1.5 beans until Wednesday, 2 beans until Friday, 3-3.5 beans thereafter
- Tomorrow (Wednesday) I have a conference paper due and I'm first author on that so it's actually my responsibility
- Friday I have a prob stats exam gross
- Afterwards I'm pretty normal

### Budhil: 2 Beans until Thursday, 3-4 from Monday after
- Tomorrow I have a Japanese Essay Due
- Thursday I have a Religious Studies Literature Review Due
- Friday I have a Japanese presentation
- Monday I have a presentation for another group project (AI class)
 
### Anthony: 2~3 beans
- Interviews been very busy
- Also still prepping for additional rounds
- Starting of final projects for other courses

### Backlog:

Both FEs:
- Sort by tags
- Search for events
- Update event cards
- Calendar page
- Homepage search bar
- Populate cal events
- Calendar views
  - Month
  - Week
  - Day
- Notifications
- Populate favorites w/ events
- Turn favorites on and off
- Username in settings
- Org descs in settings
- Log out in settings
- Connect login to API

Kotlin FE:
- Fix signup nav test
- Fix date sorting

Swift FE:
- Light/Dark mode in settings

Backend:
- Research what authentication method we might use
- Creating accounts through the API
- Logging into accounts through the API (authentication)
- Requiring API authentication to see sensitive details

### Milestone: 
- Prep external docs for stakeholders
- Stakeholder meetings
  - Lily
  - Yuina
  - Regan

## Task Breakdown
To see task breakdown, view "Sprint 4 Backlog", "In Progress", and "Done in Sprint 4" on our Trello.

<https://trello.com/b/pAnl7SQ3/getgrinnected>

## Assignments

### Collectively:
- Something to do with AI (each person must work on at least one)

### Anthony:
- Dark mode/light mode switching
- Persistent states in kotlin
- Login Process full validation of inputs
- Logout from settings
- Local DB for caching
- Username Settings
- Profile Pic Settings
- Schedule a Stakeholder Meeting

### Budhil:
- Local DB for caching
- Calendar page (month, day, week)
- Schedule a Stakeholder Meeting
- Expandable Event Cards
- Login process (validation of inputs & more)
  
### Ellie:
- Schedule Lily stakeholder meeting [DONE]
- Handle event updates in JSON/scraping
- Cowork with almond on authorization/authentication

### Ethan:
- Kotlin 
- Sort by tags
- Search for events
- Update event cards
- Calendar page
- Homepage search bar
- Populate cal events
- Calendar views
  - Month
  - Week
  - Day
- Notifications
- Populate favorites w/ events
- Turn favorites on and off
- Org descs in settings
- Something with AI ):

### Michael:
- Sort by tags
- Search for events
- Homepage search bar
- Populate favorites w/ events
- Turn favorites on and off
- Username in settings
- Org descs in settings
- Log out in settings
- Light/dark mode in settings

### almond:
- Well form sprint tasks on Trello
- Figure out + implement API authentication
  - This needs to be broken into more individual tasks for Trello
- API CALLS TO IMPLEMENT (split w/ Ellie):
  - Does an account with this email already exist?
  - I want to sign up a new user with this email and this username, please send me a PIN.
  - I want to log in a user with this email, please send me a PIN.
  - I have a PIN for this login / signup transaction, please verify it and send me tokens.
  - Updating user favorites.
  - Logging out a user.
  - Changing their username
  - Changing their profile picture
  - Unless we nix the profile picture altogether.
If user settings get bigger than dark/light mode: update user settings.
- Notes on API authorization
  - For security of students and stuff, we want all API calls to need you to be logged in. This will require changes to manual testing, but that’s ok.

