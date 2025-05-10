# API - Manual Test Procedure

The login functionality of the API can only fully be tested when it is in production, 
since it requires email connectivity.

That is what this manual testing plan covers.

# Setup

To set up the test, do the following steps.

1. Push all API changes to a branch.

2. SSH into Reclaim Cloud, and log into the Node.js Application servers.

3. Change to the project directory and pull from git.
   ```bash
   cd ~/live_test/
   git pull
   ```

4. Check out your branch.
   ```bash
   git checkout <YOUR_BRANCH>
   ```

5. Run the API from your checked-out directory.
   ```bash
   npm ci
   node src/backend/api/api.cjs
   ```

# What to test

For these steps, open up a second terminal and `ssh` it into the application servers as well.

Then, go to the dir this test file's scripts are in.

```bash
cd ~/live-test/test\ procedures/api_scripts/
```

This directory has some scripts that automate the requests. They're a little jank, but they work if you use them in the intended way. They're only dev tools, so hopefully you can understand.

## Delete the user account

The easiest way to delete a user account without logging into it is to go to <https://node16113-csc324--spring2025.us.reclaim.cloud/>.

This will delete your account information including favorited events,
so make sure you're okay with doing this or take a backup first!

1. Log in as `root` user.

2. Click "Databases" in top bar.

3. Click "GetGrinnected" from the list, then "accounts" from the list that appears.

4. Locate the `manual_test` account, and click the "⊖ Delete" button. Confirm your choice.

## Sign up for a new account.

1. Run the signup script, providing a grinnell email you can check.

   ```bash
   EMAIL=<YOUR EMAIL HERE> ./sign-up.sh
   ```

2. Get the one-time code sent to your email.

3. Verify that code.

   ```bash
   ./verify-login.sh <YOUR CODE HERE>
   ```

4. Confirm that the tokens `verify-login.sh` set are valid by attempting to do a refresh.
   It will show you the formatted response, and you should confirm it looks correct.

   ```bash
   ./refresh.sh
   ```

## Log into that account

1. Run the login script, giving the same email.

   ```bash
   EMAIL=<YOUR EMAIL HERE> ./login.sh
   ```

2. Check your email for the code.

3. Verify that code.

   ```bash
   ./verify-login.sh <YOUR CODE HERE>
   ```

4. Confirm that the tokens `verify-login.sh` set are valid by attempting to do a refresh.
   It will show you the formatted response, and you should confirm it looks correct.

   ```bash
   ./refresh.sh
   ```

## Change your email address

**WARNING:** This is hard to test, because you need access not just to another email
but to another Grinnell email. To test properly, you will need to make a code modification.

1. Stop the running API with ctrl+c.

2. Edit `src/backend/api/routes/user.cjs`, and change the function `routeChangeEmail`
   to comment out the code block starting with `util.validateEmail(newEmail)`. You can restart
   the API code now.

3. Run the change email script. For this script, you must give the new email as a parameter.

   ```bash
   ./change-email.sh <YOUR NON-GRINNELL EMAIL HERE>
   ```

4. In your non-grinnell email, check for the code. Then, you can verify the change.

   ```bash
   ./verify-change-email.sh <CODE HERE> <NON-GRINNELL EMAIL HERE>
   ```

5. Confirm that the tokens `verify-login.sh` set are valid by attempting to do a refresh.
   It will show you the formatted response, and you should confirm it looks correct.

   ```bash
   ./refresh.sh
   ```

## Change your email back

Follow the exact same steps under "Change your email address", but now change
your email from the non-Grinnell address back to your Grinnell one. 

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
