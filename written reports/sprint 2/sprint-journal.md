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
### Risk 1 - Unable to link our mySQL database with our GitHub repository.
- Likelihood of occurring: <span style="color:orange">medium</span>

- Impact if it occurs: <span style="color:red">high</span>

- Evidence for estimates
  - We have already had some issues with connecting to our database. If we are not able to connect to our database, we will not be able to automatically update our deployed code.

- Steps to reduce the likelihood or impact, and steps to get better estimates
  - We will reduce the likelihood by figuring out how to connect our database to our GitHub.

- Plan for detecting the problem
  - We will detect this problem by attempting to automatically connect to our database.

- Mitigation plan should it occur
  - Manually copy our code to our mySQL database when we update the software.

### Risk 2 - Not enough people using our app.
- Likelihood of occurring: <span style="color:green">low</span>

- Impact if it occurs: <span style="color:orange">medium</span>

- Evidence for estimates
  - We have had interest in using the app expressed by all students we have told about it. If we do not have many users it will not be a massive issue because we are not longer using user generated content.

- Steps you are taking to reduce the likelihood or impact, and steps to permit better estimates
  - We are planning to spread knowledge of our app through word of mouth and our connection in the college.

- Plan for detecting the problem
  - We will monitor the amount of traffic our server gets.

- Mitigation plan should it occur
  - We will contact people we know at the college to spread word of the app.

### Risk 3 - Unable to finish our app before the end of the semester.
- Likelihood of occurring: <span style="color:green">low</span>

- Impact if it occurs: <span style="color:red">high</span>

- Evidence for estimates
  - We feel that we will be able to finish the app in time since we reduced our features. If we are unable to finish the app, that would be a large issue.

- Steps you are taking to reduce the likelihood or impact, and steps to permit better estimates
  - Planning, communication, and teamwork.

- Plan for detecting the problem
  - We will discuss if we feel like we are behind schedule at our weekly meetings.

- Mitigation plan should it occur
  - We will reduce our features/scope or change our development schedule.

## 5.2: Epics
### Epic 1 - Home Page for Easy daily event aggregation 
#### Introduction: 
There should be an easy way to see what is happening on campus so that students can easily find ways to get involved. 
#### User Stories:
- Jessica is a 1st year and wants to learn what events are happening on campus
    - As Jessica, I need to be able to see upcoming events so that I can attend them and get involved.
    - As Jessica, I need to be able to save events I'm interested in so that I can remember they exist
    - As Jessica, I need to be able to learn what organizations exist on campus so that I can learn more about campus life and determine how I fit
- Megan is a 2nd year and wants to figure out what major they want to declare.
    - As Megan, I need to be able to sort events so that I only see those related to majors.
    - As Megan, I need to be able to see major related events so that I can attend major presentations and other related events to determine my future major.
    - As Megan, I need to find out about talks on campus to gauge my interest with the topics and the departments
- Chris is a 4th year and wants to apply to Grad School
    - As Chris, I need to know about CLS events to help me apply 
    - As Chris, I need to know about visiting Grad schools so I can attend and figure out where I want to apply
- Sarah is a 3rd year involved in multiple student organizations and interested in trying new ones.
    - As Sarah, I need something to do on Thursday afternoons, so that I don't get bored in my dorm.
    - As Sarah, I need to do get updated on any time changes or cancellations to events so that I don't miss my regular activities
    - As Sarah, I need to be able to distinguish between different types of events so that I can easily determine which ones match the interests I have my current orgs don't meet
- Brock is a 3rd year club organizer and wants people to attend their new organization
    - As Brock, I need to advertise my events and engage interested students
#### Product Requirements:
- Easily display upcoming events this week
- Updates daily to ensure accurate events
- Can be sorted to help students find events of interest
- Events have location time and description
#### Technical Requirements:
- Scrape data from 25 live
- Format data into easily understood format
- Run on IOS and android systems
- Loads quickly 
- Updates daily 
#### Feedback:
- Destiny - Mentor
    - Mentioned favorite button for events
- Ella - Classmate
    - Needs to know if event is recurring 
- Lily - normal student
    - Would like pictures on the events
    - Excited to have access to information on phone
- Regan - SEPC leader
    - Need a comprehensive list of tags
#### Metrics:
- Easy to use
- Accessible to everyone


### Epic 2 - Profile Page 
#### Introduction: 
The app needs a place to manage your profile 
#### User Stories:
- Ji-Ho is a 3rd year SEPC member and runs two student orgs
    - As Ji-Ho I need to quickly switch between accounts to manage my orgs 
    - As Ji-Ho I need to set different profile pics to help me differentiate my profiles 
    - As Ji-Ho I need to know which profile I am on 
- AJ is a 2nd year who doesnt like leaving accounts signed in and has Blue-Yellow color blindness
    - As AJ I need a way to log out of my account when I am done using it
    - As AJ I need a way to switch the color scheme to something I can read easily
    - As AJ I need my settings to  save so I don't need to navigate and change the settings every time I log in
- Jess is a first year who plans her days the night before and has trouble reading small text
    - As Jess I need a darkmode on the app to reduce eye strain when I use it in dark conditions and lowers blue light that wakes you up
    - As Jess I need a way to increase the text size so I can read it
#### Product Requirements:
- Allows user to sign out of their profile
- Allows user to change between profile
- Allows users to change their profile image to any image in their photo library
- Allows you to change settings
- Settings for text size for accessibility
- Settings for color schemes compatible with different color blindnesses
- Setting for light vs dark mode to reduce eye strain 
#### Technical Requirements:
- Keeps track of individual users, their accounts, and saved settings 
- Applies changes in settings across the app on every screen
- Have multiple color palettes available that address different types of color blindness
- App has access to users photo library
#### Feedback:
- Regan 
    - Should have a sign out option
- Yuina 
    - If you have multiple accounts they should be linked 
- CIDER Lab
    - Should have a light and dark mode 
    - Should have a way to zoom in or increase text size
#### Metrics:
- Easy to use
- Accessible to everyone


## 5.3: Product Roadmap

# Part 6: Continuous Integration Plan

# Part 7: Test Automation and Continuous Integration Setup
