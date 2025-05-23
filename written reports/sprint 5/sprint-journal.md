# (1) Adoption Plan

## 1. 
5/5/2025
## 2.
Current State
- Working Login and account creation using email verification
- Sortable home-page in both IOS and Android
- Can favorite upcoming events
- Working settings page with adjustable text size and dark mode
- Most accessibility metrics met 
- Kotlin does not form to every phone sizing sometimes cutting text off
- Search feature in progress on Kotlin
- Notifications only work in app in Kotlin
- Search Feature in Progress in Swift
- Authorization in progress on swift
- Screen Reader accessibility partially implemented. 
### Benefits 
- Helps advertise grinnell events to students so everyone can know what is happening on campus
- Especially helpful for new students who don't know grinnell to find things to do to build community.
### Risks
- App might crash with bugs we have not experienced
- Text might not be visible on all phone versions as it wraps to certain sizes.
- The app might not be fully accessible to everyone as certain accessibility features are still in progress.
  
I think our current state is reasonable to start deploying to users. It has defects and missing features that make it incomplete but what is present makes for a useful app that can have real benefits to the Grinnell community. We plan to continue development slightly to try and get the feature we want included in the product but it is worthwhile to get our product in peoples hands before the end of the semester. 
## 3. A
We would like our app to be on 80% of the Grinnell students populations phones (1420 users). We don't want them using it very often for most users once a week and some once a day. We would like usage of our app to decline over the semester as people find organizations and activities to participate in weekly but expect some usage to check for gardners, talks and other one time events. 

To get students aware of the product we want to use a combination of posters, tabling, club emails and word of mouth. For early adoption we will use posters spread around campus with links to download the app, at least the beta version. We hope to table in front of Dhal to promote the app as well, engaging people to get the links and try the app. Additionally we plan to engage student organizations we are involved in or are friends with to send emails encouraging adoption. Finally we hope that next semester when we are gone we will get it to spread through word of mouth to the first year class. 

To make continual adoption as easy as possible we plan to publish the app to the google play and apple app store respectfully so that any student on campus can download them. This should make continued use easy for students who only need to know it exists and find it on their phone. 

Despite moving towards adoption we do plan to complete the work on our product. Finishing any features that are necessary and incomplete by the end of the semester. Examples might end up including timed notifications and search features for kotlin and notifications for swift. We think our current product is worthy of publishing but understand where it could be better. 

## 1.B 
5/12/2025
We created and distribuited posters with QR code links to the IOS and Android Versions of our app. We have allready recieved feedback from users and tried to impliment improvments. One user was confused by the username field being labled name preventing them from logging in. We were unfortunalty not able to find enough time to do everything we wanted for our adoption plan but we are already seeing people interested in our product. 

# (3) Bug Logging

We have other issues tagged with "Bug" from this sprint because we had more than 3 bugs, but here are the bugs required for this part of the assignment.

1. <https://trello.com/c/wV1lMEbg> "Routes crash when no body is provided"

2. <https://trello.com/c/dSIH5cun> "On Kotlin, changing apps (to check your email) sets back login."

3. <https://trello.com/c/LQCBxtmL> "Swift auth won't actually send calls"

# (5) Wrap-Up Work

For our sprint wrap-up, we wanted to make sure not only to make sure our code was cleaned up but also keep our external-facing resources up to date.

We refactored the "session" API routes into their own file to clean them up, and made sure code and comments were cleaned up for the Kotlin homescreen, search, and notifications code as well as the Swift API calls.

We also updated our README.md to be more focused towards audiences outside of this class, while still keeping all the required sections we have accrued throughout the semester. This came in the form of some description changes, but mainly reordering some sections. We also updated our LICENSE file to change from saying the code belonged to `seehorne` (Ellie's Github username), we listed all of our names.

Finally, we made sure our external docs were up-to-date with the functionality of the app in our final build.

## External Docs Changes

One piece of wrap-up work, a loose end that we wanted to tie up, was reevaluating our external documentation. In the original Documentation Plan we made lofty plans, intending to create a wiki-like project with pages not only on app features but also common workflows.

Based on feedback sessions with stakeholders, it seems like such a large scale source of documentation may not be helpful to users. Instead, we decided to modify our docs so that their sections would be easily visible in the navigation panel on the side and leave the rest unchanged (of course, keeping them up to date for new features).

# (7) Blog Post

The blog post can be found inside the same directory as this file, with the name `blog-post.md`. (path `written reports/sprint 5/blog-post.md` from the top of the repository)
