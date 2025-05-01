# API - Manual Test Procedure

The login functionality of the API can only fully be tested when it is in production, 
since it requires email connectivity.

That is what this manual testing plan covers.

# Setup

To set up the test, do the following steps.

1. Push all API changes to a branch.

2. SSH into Reclaim Cloud, and log into the Node.js Application servers.

3. Change to the project directory and pull from git.
   ```
   cd ~/live_test/
   git pull
   ```

4. Check out your branch.
   ```
   git checkout <YOUR_BRANCH>
   ```

5. Run the API from your checked-out directory.
   ```
   npm ci
   node src/backend/api/api.cjs
   ```

# What to test

For these steps, open a second terminal also connected to the Node.js application servers.

## Delete your user account

The easiest way to delete your user account is to go to <https://node16113-csc324--spring2025.us.reclaim.cloud/>.

This will delete your account information including favorited events,
so make sure you're okay with doing this or take a backup first!

1. Log in as `root` user.

2. Click "Databases" in top bar.

3. Click "GetGrinnected" from the list, then "accounts" from the list that appears.

4. Locate your user account, and click the "⊖ Delete" button. Confirm your choice.

## Sign up for a new account

1. Make a request to the `/session/signup` API endpoint, specifying the new account you want to create.

   For instance, this is almond's request since they do testing a lot and wanna copy+paste.

   ```bash
   curl \
      -H 'Content-Type: application/json' \
      --request POST \
      --data '{"email":"heilalmond@grinnell.edu", "username":"almond"}' \
      http://localhost:8080/session/signup | jq
   ```

2. Confirm the response is positive. It should look like this.

   ```json
   {
      "message":"Account created, and OTP successfully sent."
   }
   ```

## Verify your signup OTP code

1. Check your Grinnell outlook for an email from getgrinnected@gmail.com,
   and copy the code inside.

2. Use the code to send another POST request, this time to the `/session/verify`
   endpoint.

   ```bash
   curl \
      -H 'Content-Type: application/json' \
      --request POST \
      --data '{"email":"heilalmond@grinnell.edu", "code":"123456"}' \
      http://localhost:8080/session/verify | jq
   ```

3. Make sure your response looks good. Here's mine, only I changed the tokens to be the example one from <https://jwt.io> so I'm not leaking anything.

   Your `refresh_token` and `access_token` *should* be different strings from each other, but
   it's tedious to check by hand and an automated test takes care of it.

   ```json
   {
      "message": "Successfully authenticated",
      "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
      "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
   }
   ```

## Log into your existing account

1. Now that your account is created, send a login request to it.

   ```bash
   curl \
      -H 'Content-Type: application/json' \
      --request POST \
      --data '{"email":"heilalmond@grinnell.edu"}' \
      http://localhost:8080/session/login | jq
   ```

2. It should respond with this.

   ```json
   {
      "message": "OTP successfully sent."
   }
   ```

## Verify your login OTP code

Follow the exact same steps under "Verify your signup OTP code".
The behavior should be the same.

# Teardown

When you are done, undo everything you did in the setup.

1. Press Ctrl+C to stop your running API.

2. Change back to the most up-to-date version of the main branch.
   ```
   git checkout main
   git pull
   ```

3. Delete your testing branch.
   ```
   git branch -D <YOUR_BRANCH>
   ```

4. Close your ssh session.
