# Data Modeling 
## Written Explanation

For the data we will be handling in this project it will come from 2 sources:
- 25Live
    - Will only include event information
- User Input
    - User information
    - Org information
    - Event information

We will manage the creation, storage, manipulation and retrieval of this information via a database in our backend utilizing sqlite3 in conjunction with Node.js. The database will be composed of the following tables (with their corresponding information):

Events Table
- <ins>EventID</ins>: A unique identifier for every event
- Name: The name corresponding with the event
- Description: A descritption for the event
- Location: Where the event is taking place
- Creator: Org associated with putting on the event
- Collaborators: Orgs/People collaborating on the event
- RSVP?: A binary value associated with whether the event requires RSVPing
- Date: The dd/mm/yy of the event
- Time: When the event is occuring
- Tags: list of tags associated with the event
- Private?: A binary value associated with whether the event is private.
- Repeats?: A binary value associated with whether the event repeats.
- Image: Potential image file associated with the event.

Users Table
- <ins>Username</ins>: The username associated with the account selected at creation
- <ins>Email</ins>: Email address
- Password: Password selected at creation
- Display Name: Current displayed name user might change
- Profile Picture: image associated with the account.
- Favorited Events: List of events the user has favorited.
- Followed Orgs: List of orgs the user has followed.

Orgs Table
- <ins>Username</ins>: The username associated with the account selected at creation
- <ins>Email</ins>: Email address associated with the org.
- Display Name: Name of the org
- Password: Password selected at creation
- Profile Picture: image associated with the account.
- Draft Events: List of events currently in drafts by Org.
- Posted Events: List of events currently posted by Org.

## Database Schema

IMAGE OF FINALIZED SCHEMA GOES HERE