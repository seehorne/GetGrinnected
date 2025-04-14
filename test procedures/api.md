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
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events?tag=TAGNAME' | jq
   ```

2. Confirm that all events shown in the output have the tag you copied.

3. Find a tag that appears in at least one item outputted, but not all. Copy its text as well.

## Get multi-tagged events

1. Perform a new curl request, subsituting `TAG1` and `TAG2` for the two tag names you copied.

   Make sure to HTML escape them, such as by replacing spaces with `%20`.
   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events?tag=TAG1&tag=TAG2' | jq
   ```

2. Confirm that all events shown have both of the tags you specified.

## Get nonexistent tagged events

1. Perform this curl request.
   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events?tag=doesnotexist' | jq
   ```

2. Confirm the output is an empty array.

## Get events between dates

1. Based on today's date, find only events that happen in the next day. To do this, you will pass a start date of today and an end date of tomorrow. Format these dates like `YYYY-MM-DD`.

   Here is an example command.
   ```  
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events/between/YYYY-MM-DD/YYYY-MM-DD' | jq
   ```

2. Confirm that more than one event appears in that date range. If there are too few, choose a different date range and try again. If no events are shown, something is likely broken.

3. Note a tag that at least one event has, but at least one other event does not have.

## Confirm date cutoff applies to end dates

1. Look at the last event in your output from the previous command, and note its end time.

2. Construct a `curl` command that will exclude that event. 

   For instance, if it ends at `2025-04-15T02:00:00.000Z` you need to make the cutoff `2025-04-15T01:59T+0000`. Make sure your end time still matches the API spec.

   Here is an example of such a command. This example uses specific dates to show the altered end date.
   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events/between/2025-04-14/2025-04-15T01:59+0000/' | jq
   ```

3. Confirm that the event you are trying to exclude is no longer at the bottom of the output.

## Get tagged events between dates

1. Using the tag you noted before, query for events with those tags.

   ```
   curl 'https://node16049-csc324--spring2025.us.reclaim.cloud/events/between/YYYY-MM-DD/YYYY-MM-DD?tag=YOURTAGHERE' | jq
   ```

2. Confirm that the event(s) you noted with this tag is shown, but the event(s) you saw without this tag are not shown.

# Teardown

When you are done, undo everything you did in the setup.

1. Press Ctrl+C to stop your running API.

   When you do this the API goes down, so try to do the next steps quickly.

2. Change back to the most up-to-date version of the main branch.
   ```
   git checkout main
   git pull
   ```

3. Delete your testing branch.
   ```
   git branch -D <YOUR_BRANCH>
   ```

4. Restart the system process for the API.
   ```
   sudo systemctl start nodejs
   ```

5. Confirm that the process is marked as running.
   ```
   sudo systemctl status nodejs
   ```

6. Close your ssh session.
