# Requirements

This is a living document, it will be updated as we complete different milestones and sprints.

# Product Descriptions with User Roles

For those unfamiliar with the midwest, arriving in the middle of Iowa as a first year is no easy task. Due to the COVID-19 pandemic, a year of connection between class years and passage of institutional memory and campus culture has suffered. Many students feel underwhelmed by the activities to do, or are disappointed by the classes, and are unable to find the resources or groups on campus to lift them up. In fact, about 47% Grinnellians at one point considered transferring or withdrawing from Grinnell College [3]. Although there are more than one hundred Registered Student Organizations (RSOs) in Grinnell, the infrastructure to reach those RSOs is lacking if not non-existent. Only once per semester do RSOs participate in the Organization Fair. As such, GetGrinnected will be the go-to app for Grinnellians to find events of their interest and connect to the larger student body. With GetGrinnected, all available public Grinnell events are aggregated and then can be added to a Grinnellian’s individual event list. Student leadership can also add events that are for their organization which are unlisted on the public directory. This comprehensive list of events can be sorted by date, date posted, organization, and by kind of activity. GetGrinnected is for Multicultural Leadership Councils, Harris events, public speakers, convocations, SEPC study breaks, sports clubs, and any other RSO events.

This issue has not been left unnoticed. Many other groups and students have made versions of event apps. That being said, no student leadership uses said apps to connect to their student body, and no students use it to connect to their favorite organizations. Another version of the app was made by the previous group of CSC324 students, GrinSync which looked promising, but alas has not been officially put into use [1]. This issue isn’t exclusive to Grinnell: a similar app we drew inspiration from is called Cove, and was created by UT Austin students. This app has some features we appreciate, like having events tagged and in list form, sortable by a variety of criteria (time to occurrence, most recently posted, etc). However, unlike Cove, we strive to be cross-platform, to reach more students [2].

By enabling students to intuitively sort events, students will more easily be able to find their people and explore their interests. RSOs and other leadership bodies will also benefit by connecting and communicating to their student body more efficiently and effectively. GetGrinnected is not only for students, but for faculty, and student leadership, therefore not only serving to bolster the student culture on-campus, but also Grinnell culture in its entirety.

To briefly discuss our plans for a technical implementation, we hope to build frontends in Swift and Kotlin respectively to take advantage of native features on both systems, while minimizing the excess development energy by using a shared backend, likely made with Node.js, which is compatible with both systems. For data management, we would likely use Firebase.

## Types of User/Target Audience

- People looking for clubs and events
- Large MLC organization leadership
- Smaller MLC organization leader
- SEPC Member
- Prospective major (first/second year)
- Different class years
- Typical student organization leader
- Sports organization leader (Volleyball club)
- High street party person
- Do we/how do we accommodate them without getting in administrative trouble
- Weekend organizer
- Grinnell college faculty/staff
- Students are also student org leaders
- Types of Event
- Faculty/staff
- Grinnell-sponsored events: convocation, (not student coordinated),
- Off-campus opportunities (verifying with grinnell email)
- Student organization leader (large)
- Student organization leader (small)
- First - fourth year
- Major organization/SEPC vs MLC vs friend group/informal event (non-associated) vs sports vs “typical student org”

## User Roles/Permissions

- Normal user: Can view events
- Event creator: Can create and edit events for the group they manage
- System admin: Can edit or delete events as needed, content moderation?

# User Personas and User Stories

We've decided to combine persona descriptions with the stories, so the stories are bullet points below each user's persona.

Rohit is a fourth year party man who wants big large off campus parties but them to be safe. (out of scope)

- As Rohit, I need to be able to collect enough money so that I may cover the expenses for the event.
- As Rohit, I need to set rules for the party, and themes for the party (guidelines), so that attendees are aware of the vibe and the kind of party I’m going for.
- As Rohit, I need to allow music requests if I choose so, so that people can add music (during or before the event) so that people are enjoying the event.
- As Rohit, I need to be able to prevent certain parties from seeing my event while encouraging others to see my event so that my parties are safe and accessible.
- As Rohit, I need to update party attendees if anything occurs during the event so that people are safe during the party.
- As Rohit, I need to contact people who attended the party if someone lost any items so that people may find their lost items easily.
- As Rohit, I may need extra funds to support replacing a TV that was broken during a party so that I do not go broke.
- As Rohit, I need to remove people from the attendee list so that people who caused trouble can’t come to future episodes.

Abdul is a 3rd year MSA leader.

- As Abdul, I need to notify non-members after my event is finished, in case there is extra food or drinks, so that the food we bought does not go to waste.

Ji-ho is a 4th year and the leader of the Anthro SEPC.

- As Ji-ho, I need to connect with other SEPCs to collaborate on a larger scale event, so that we can pool our funds and break down barriers between majors.
- As Ji-ho, I need to communicate with professors about our events, so that they know what events we have and what we have planned for the semester.
- As Ji-ho, I need students to be able to easily contact us (the SEPC) in case they have any complaints about professors, classes, or the major organizations, so that we as the SEPC can fulfill our duty to be there for students.
- As Ji-ho, I need to be able to advertise events that are hosted outside standard academic buildings, so that I can facilitate SEPC activities in all the locations that are relevant to the department—such as CERA

Ochen is the ISO president and a 4th year.

- As Ochen, I need to receive feedback on larger scale events so that we can improve on our event for future years.
- As Ochen, I need to distribute tickets for the food bazaar so that not too many people sign up to our events.
- As Ochen, I need to collaborate with other SEPCS so that we can all advertise to our mailing lists.
- As Ochen, I need to make a very strict schedule because I am sooo busy with my event planning, so that I can make enough time to live life.
- As Ochen, I need to gather volunteers for larger events so that I don’t overextend myself with the event so that I can prioritize my academics.
- As Ochen, I need to send notifications to the ISO mailing list about off-campus opportunities or engagement opportunities on-campus so that our membership is informed.
- As Ochen, I need to notify ISO leadership about our upcoming meeting, so that they know we have a meeting soon.
- As Ochen, I need to communicate easily with my ISO leadership so that we can work well during big events.
- As Ochen, I need to provide a list of resources to our international student body so that they know what resources are available to them, and so they know the faces of the people in charge of those departments.
- As Ochen, I need to allow people to be aware of a vote that is ongoing for the next year’s cabinet members, so that we have leadership that is representative of our organization’s student body.

Sampson has colorblindness and only access to a computer.

- As Sampson, I need the ability to read the app so that I can use the app
- As Sampson, I need access to the app on non cellular devices so that I can use it even without my phone.

Jessica is a 1st year and wants to learn what events are happening on campus

- As Jessica, I need to be able to see upcoming events so that I can attend them and get involved.
- As Jessica, I need to be able to save events I'm interested in so that I can remember they exist

Megan is a 2nd year trying to figure out what major they want to declare.

- As Megan, I need to be able to sort events so that I only see those related to majors.
- As Megan, I need to be able to see major related events so that I can attend major presentations and other related events to determine my future major.

Rūta is an academic assistant who coordinates logistics for several academic departments and their events.

- As Rūta, I need to coordinate simultaneous events so that I can do my job effectively for multiple departments who may have overlapping events.

Jack is a 4th year student org leader.

- As Jack, I need to be able to include students who aren’t formally leadership in my org in event management so they can learn what to do to keep the org running once I graduate.
- As Jack, I need to advertise some events to the whole campus community and others to just my orgs existing membership so that we can distinguish our big events from the regular meetings necessary to plan them.

Abby is a 1st year student athlete.

- As Abby, I need to only see events that are relevant to me so I’m not overwhelmed with all the options.
- As Abby, I need a visual reference for where events are held so that I can find them even though I don’t know all the building names yet.

Rosy’s Grinnell email uses their deadname.

- As Rosy, I need to adjust my name so that it represents me as I am.

Sarah is a 4th year involved in multiple student organizations and interested in trying new ones.

- As Sarah, I need something to do on Thursday afternoons, so that I don't get bored in my dorm.
- As Sarah, I need to do get updated on any time changes or cancellations to events so that I don't miss my regular activities
- As Sarah

Zach is a shy 2nd year who wants to make new friends.

- As Zach, I need motivation to attend events, so that I can go out and make new friends
- As Zach, I need to know if I know anyone attending an event, so I can select ones where I won’t have to either hang out all alone or approach people I don’t know
- As Zach, I need to know if an organization is open to new members or people just stopping in so that I know I am welcome to attend

almond is an administrator for the tool.

- As almond, I need to be able to delete events which have passed or are not appropriate so that events are up to date and I can remove things that are against the content policy.
- As almond, I need to verify if people are parts of the organizations they claim to be so I can give them permissions on their events.
- As almond, I need to control who can edit an event so only people in charge of it can update the details.

# Non-functional Requirements

- Ensuring privacy of user passwords (and other data) via hashing and salting or encryption where stored.
- Cross platform available on iOS and Android.
- Server backend has to scale to the number of users we have. Should be able to support the entire campus.

# External Requirements

- Non-passable user inputs return an error invalid input. Try to limit text inputs as much as possible.
- Will try and get it on the app store for both iOS and Android. We probably won't get it on the apple app store so we will try and get a beta version available through testflight
- Making our software buildable by users will mean having good documentation of both the frontend and backend (such as the requirements for the backend server).
- Our scope is scalable as we go on. Our starting minimum viable product should be accomplishable and we have solid stretch goals to expand our scope if we move faster than expected.

# Scope and Feature List

## Major Features

Shows all events currently happening at Grinnell (scraping information off of 25 live)
Create a user account (Grinnell emails only?)
Allows users to sign up for student organization email lists
Student Org leaders being able to create events directly on the app
Allow user accounts to friend each other
Events sorted by category

## Stretch Goals

Map and directions to where events are happening
Puzzle Piece reward system for attending events or add memes
Click through to get tickets (like for ISO food bazaar and theater stuff)
Subscribe to events to get notifications
More private events like MLC’s are only visible to specific groups
Updates for if an event changes (posted by the organizer)

## Out of Scope

Use of app outside of Grinnell or other colleges
Parties or personal get togethers
Normal users creating their own events
Microtransactions or pay functions

# Use Cases

## Use Case 1: Signing Up

TODO (almond): FIX THIS AREA WITH LEAH'S NEW INSTRUCTIONS

**Actor:** A new user to the app. They have no user role yet.

**Goal:** Sign up to use GetGrinnected. 

- Since the user doesn't exist yet, you could say either all user roles or no user roles can trigger this case.
- This wasn't explicitly covered by any of our user stories.

**Trigger:** The user clicks "sign up" on the initial screen of the app (which only appears if they are not already signed in)

**Preconditions:** No existing login data stored for app

**Postconditions:** New user created in database, app stores login data locally.

**Flow:**

1. The user opens the app.
2. They have choices "Sign In" and "Sign Up".
3. They click "Sign Up."
4. On a new screen, the user enters their desired username.
  - If it is available, continue with the main flow.
  - If not, start the alternative flow "username not available."
  - If the database cannot be reached for any reason, start the alternative flow "connection error."
5. After entering their username, the user enters their Grinnell email and clicks a button labeled "Verify Email."
6. If the email address ends in @grinnell.edu, send an email containing a verification code to the email address entered. 
  - If the email already belongs to an account, start the alternative flow "account already exists."
    - If the database cannot be reached, start "connection error."
  - If it does not, start the alternative flow "non grinnell email."
  - If the email server cannot be reached for any reason, start the alternative flow "connection error."
7. The app changes to a new screen asking for a verification code, and telling the user to check their Grinnell email for the code. A button labelled "Cancel Verification" is also displayed.
  - If the user presses "Cancel Verification", return them to step 5 and allow them to enter a different email address.
8. The user enters the verification code that has been emailed to them.
  - If it matches the correct code, continue the main flow.
  - If it does not match, start the alternative flow "incorrect verification code."
9. The app changes to a new screen asking the user to enter a password for their account. It lists any password requirements.
10. The user enters a password. It is checked against requirements.
  - If it fulfils all requirements, continue the main flow.
  - If it does not, start the alternative flow "invalid password".
11. A user account is created in the database
  - If the database cannot be reached for any reason, start the alternative flow "connection error."
12. The user is let into their account, onto the default landing page.

**Alternative flows:**

username not available:

1. The app displays an error message: "That username is already in use."
2. Focus is returned to the username box, so a username can be retyped
3. Return to the main flow of whatever step caused this alternate flow.

connection error:

1. The app displays an error message: "Error connecting to resources. Try again later, or contact the developers."
  - This can be customized based on what resource could not be reached
2. Return to the main flow of whatever step caused this alternate flow.

account already exists:

1. The app displays an error message: "An account is already registered with that email"
2. The app displays an informational message: "Do you want to log in?"
3. Two buttons are displayed: "Log In" and "Use a different email".
  - If "Log In" is pressed, stop the flow and start the log in flow.
  - If "Use a different email" is pressed, go back to step 4 of the sign up flow.

incorrect verification code:

1. The app displays an error message: "Verification code does not match."
2. The box(es) where the code was typed get cleared out.
3. Return to the main flow of whatever step caused this alternate flow.

invalid password:

1. The app displays an error message: "Password does not meet requirements."
  - Can specify what needs to be fixed.
2. The password box is cleared, and a new password can be typed.
3. Return to the main flow of whatever step caused this alternate flow.

## Use Case 2: Creating an Event

**Actor:** A SEPC, Student Org Leader, MLC, MSO, ISO (student group leader of some form) who wants to create a new event for their orginatization. This would be an Event Creator user.

**Goal:** The general overarching goal is to create an event to get their organization or group out for others to see the events they have planned.

- This aligns with the following User Stories
  - Jack
  - Ochen
  - Abduhl
  - Ji-ho

**Trigger:** The user clicks the "plus sign" on the calendar screen of the app (which only appears if they are an event creator user)

**Preconditions:** Logged in as an Event Creator user and navigated to the calendar screen.

**Postconditions:** New event is created and stored in the database and posted on the calendar screen, searchable, etc.

**Flow:**

1. The user presses the "plus sign" button
2. They are then navigated to the event creation page.
3. They are asked to fill in the following information:
  - Title
  - Event Description
  - Date of Event
    - Calendar to select date
  - Whether it Repeats
    - Day selection for repetition
  - Location
    - Drop down with Grinnell Buildings
  - Upload an image associated
  - Hide the event from Public View
  - Add Tags
    - Drop down with options
  - Add Collaborators
    - Search feature for users
4. Upon filling in all the necessary information the user is present with two options
  - "Save to Drafts" button
  - "Post Event" button
  In our case for our user we will press the "Post Event" button.
5. They will then recieve a successfully posted event notification.


**Alternative flows:**

Save to Drafts:

1. When the user selects this button instead of posting the event the app stores the information
2. Navigates them to their drafts page, showing the draft they just created as well as the other events they may have in drafts.
3. From here the user can continue to use the app in any way but can reference back if they decide they are ready to post the event or make edits to the draft.

connection error:

1. The app displays an error message: "Error connecting to resources. Try again later, or contact the developers."
  - This can be customized based on what resource could not be reached
2. Return to the main flow of whatever step caused this alternate flow.

## Use Case 3: Finding an Event

**Actor:** Any user to this app. They can really have any user role.

**Goal:** Search for an event of interest.

- This aligns with the following user stories:
  - Abby

**Trigger:** A user opens the app and presses the search bar. (While we have many features for organizing/filtering events for this case we will use the search bar).

**Preconditions:** User is Logged in and on the homepage.

**Postconditions:** User has new event in their favorites tab.

**Flow:**
1. The users presses the search bar.
2. The user enters the name for an event they are interested in.
3. The screen filters out events that don't match the the information the user specified.
4. The user presses on one of the events left on their page
5. The event gets larger providing the following information:
  - Name
  - Description
  - Time
  - Location
  - Tags
6. The user presses the "heart" button.
7. The event has been added to their favorites tab. 

**Alternative flows:**

no event found:

1. At step 2 of the use case the user provides a name for an event that doesn't exist.
2. The screen filters out all events (since none match the event name).
3. The home screen is left blank saying no events found.
4. To return to flow the user enters a different name into search. (Returning to flow at step 3)

# Citations

1. Do, Nam, Brian Goodell, Samantha Chu, Lívia Freitas, Kevin Peng, and Bradley Ramsey. 2024. “GrinSync.” Grin-ArchiTech (blog). December 2024. <https://softarchitech.cs.grinnell.edu/grinsync/>.

2. McIlhinney, Molly. “UT Students Create New App to Find Local Events.” The Daily Texan, <https://thedailytexan.com/2022/11/02/ut-students-create-new-app-to-find-local-events/>. Accessed 5 Feb. 2025.

3. Zach Spindler-Krage. 2024. “47% of Students Have Considered Withdrawal from Grinnell College.” The Scarlet & Black. 2024. <https://thesandb.com/43065/article/47-of-students-have-considered-withdrawal-from-grinnell-college>.
