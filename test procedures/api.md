# API - Manual Test Procedure

The API can only fully be tested when it is in production and can communicate with the database.
This testing currently can't be done automatically, so must be done by a person.

# Setup

To set up the test, do the following steps.

1. Push all API changes to a branch.

2. SSH into Reclaim Cloud.

3. Change to the project directory and pull from git.
   ```
   cd ~/ROOT/
   git pull
   ```

4. Check out your branch.
   ```
   git checkout <YOUR_BRANCH>
   ```

5. Stop the currently-running API process.
   ```
   sudo systemctl stop nodejs
   ```

6. Run the API from your checked-out directory
   ```
   npm ci
   sudo node src/backend/api.js
   ```

# What to test

For these steps, open a second terminal not connected to the server. 

## Ensure API online

1. Perform a curl request on the `/` route of the server.
   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/'
   ```
   
   > This will request the server load its default page, and then send the data back to you.
   > It prints the server's response to your screen by default.

2. Ensure the output says "API online!"

## Get all events

1. Perform this curl request.
   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events' | jq
   ```

   > By piping the response of the server into `jq`, it will organize the JSON the server
   > sends back to make it print prettily. Otherwise, it'll just be a block of text.

2. Confirm that the output shows multiple events.

3. Find a tag that appears multiple times, such as "Multicultural" or "Student". Copy its text.

## Get tagged events

1. Perform a new curl request, substituting `TAGNAME` for the tag you copied.

   Make sure to HTML escape it, such as by replacing spaces with `%20`.
   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events?tag="TAGNAME"' | jq
   ```

2. Confirm that all events shown in the output have the tag you copied.

3. Find a tag that appears in at least one item outputted, but not all. Copy its text as well.

## Get multi-tagged events

1. Perform a new curl request, subsituting `TAG1` and `TAG2` for the two tag names you copied.

   Make sure to HTML escape them, such as by replacing spaces with `%20`.
   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events?tag="TAG1"&tag="TAG2"' | jq
   ```

2. Confirm that all events shown have both of the tags you specified.

## Compare get output lengths

1. In sequence, repeat the three `curl` commands you did before. Instead of piping them into `jq`, pipe them into `wc -c`.

   > By piping into `wc -c` instead of `jq`, your computer will count how many characters are in each
   > output rather than printing the whole text to the screen. This gives an easy-to-read summary.

2. Say the sizes are s1, s2, and s3 in order of the curl commands. Make sure that s1 > s2 > s3.

## Get nonexistent tagged events

1. Perform this curl request.
   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events?tag="doesnotexist"' | jq
   ```

2. Confirm the output is an empty array.

## Get events between dates

TODO: This section cannot yet be written.

## Get tagged events between dates

TODO: This section cannot yet be written.

# Teardown

When you are done, undo everything you did in the setup.

1. Change back to the main branch.
   ```
   git checkout main
   ```

2. Delete your testing branch.
   ```
   git branch -D <YOUR_BRANCH>
   ```

3. Restart the system process for the API.
   ```
   sudo systemctl start nodejs
   ```

4. Confirm that the process is marked as running.
   ```
   sudo systemctl status nodejs
   ```

5. Close your ssh session.
