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

For these steps, open a second terminal also connected to the server.

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
