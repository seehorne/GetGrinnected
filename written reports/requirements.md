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
- As Sarah, I need to do get updated on any time changes to event so that I don't miss my regular activities

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

# Citations

1. Do, Nam, Brian Goodell, Samantha Chu, Lívia Freitas, Kevin Peng, and Bradley Ramsey. 2024. “GrinSync.” Grin-ArchiTech (blog). December 2024. <https://softarchitech.cs.grinnell.edu/grinsync/>.

2. McIlhinney, Molly. “UT Students Create New App to Find Local Events.” The Daily Texan, <https://thedailytexan.com/2022/11/02/ut-students-create-new-app-to-find-local-events/>. Accessed 5 Feb. 2025.

3. Zach Spindler-Krage. 2024. “47% of Students Have Considered Withdrawal from Grinnell College.” The Scarlet & Black. 2024. <https://thesandb.com/43065/article/47-of-students-have-considered-withdrawal-from-grinnell-college>.
