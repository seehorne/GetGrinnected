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

We decided to classify the scraper component on the same layer as the business logic layer, rather than some other layer.

**Alternative:** In particular, one alternative we considered was placing the scraper on the same layer as the clients. That is, it would run unrelated to the server and make API requests.

The main reason we were considering this was that it made organizational sense at one point in the process, when allowing users to add events was a central part of the functionality of the app. However, we later decided to scale back that aspect of the scope which would mean the API would require routes only used by the event scraper. This seemed like a bad idea.

Additionally, we decided to classify the scraper as we did because it makes the most sense to run it on Reclaim Cloud with other parts of the app, as opposed to an alternative like running it through Github actions which would use up our CI minutes quickly.

Reversing this decision wouldn't be the simplest thing, but we might decide to do so if we end up letting users post events. This would let it take advantage of existing systems in order to function, and keep it in line with the 'rules' of other parts of the system.

# Part 5: Process Description

## 5.1: Risk Assessment
### Risk 1 - 25Live event API is not accessible
- Likelihood of occurring: <span style="color:orange">medium</span>

- Impact if it occurs: <span style="color:red">high</span>

- Evidence for estimates
  - We are not sure how to access the API. If we can't access the API we will not be able to scrape any events.

- Steps you are taking to reduce the likelihood or impact, and steps to permit better estimates
  - We are looking for possible alternative event data sources that could be scraped.

- Plan for detecting the problem
  - We will detect the problem by trying to access the API.

- Mitigation plan should it occur
  - If we can't access the API we will switch to another data source like events.grinnell.edu.

### Risk 2 - Unable to connect to our database
- Likelihood of occurring: <span style="color:orange">medium</span>

- Impact if it occurs: <span style="color:red">high</span>

- Evidence for estimates
  - If we can't access our database, we will not be able to run the app at all. This might happen because we are still learning how to access the database and use it.

- Steps you are taking to reduce the likelihood or impact, and steps to permit better estimates
  - We are testing how to connect and send requests to our database through our site.

- Plan for detecting the problem
  - We will detect the problem by attempting to send requests to the database.

- Mitigation plan should it occur
  - If we can't connect to our database we will either find a different way to connect to it or use a different database.

### Risk 3 - 
- Likelihood of occurring:

- Impact if it occurs:

- Evidence for estimates


- Steps you are taking to reduce the likelihood or impact, and steps to permit better estimates


- Plan for detecting the problem


- Mitigation plan should it occur


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
