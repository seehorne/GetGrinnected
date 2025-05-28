# General Backend

This page is meant as internal documentation. It should let you do these things:

- Understand general info about the API
- Locate specific parts of the codebase
- Add new features, following existing styles

## General API Info

The API is the layer between the user (an app) and the database.
It's responsible for handling the rules, so it's only possible to do things that are allowed.

!!! note

    In many cases, we check the same rules on the frontend (e.g. checking passwords are valid)

    This is duplicative, but the reason is to try and avoid making an API call before it is necessary.

## Routes with Express

We use the `express` library for our API, which gives us a framework to build up the rules.

### Basics

Express lets us organize our functionality into **routes**. A route has three main parts:

- **Name**: The URL that activates a route.
- **Type**: The type of HTTP request that activates a route.
- **Function**: What gets called when the route is activated.

The name and type describe the way a frontend app has to interact with the
route, and the function describes what happens when they do so.

The function is responsible for handling the request and responding to it.

The parameters of the function are:

- **Request**: Holds any information the frontend app sent to the API.
- **Response**: Lets us respond to the frontend app when we are done processing it.

Sometimes we don't use the information stored in the request, but we always use
the response object to finish up the request--otherwise it hangs there, because
the frontend app doesn't realize it is done.

!!! example

    ```
    app.get('/hello', (req, res) => {
        res.json(
            { 'message': 'Hello, world!' }
        );
    });
    ```

    Here, these are the values.

    - **Name**: `'/hello'`
    - **Type**: `get`
    - **Function**: `(req, res) => { ... }`

    If our API were hosted on <https://example.org>, a frontend app would activate this route my making an HTTP GET request to `https://example.org/hello`.

    Then, the `res.json` method turns that JSON object into text and sends it back to the frontend app. So they would recieve the text `'{"message":"Hello, world!"}'` and would then need to parse that back to an object.

### Errors

To send back an error, we not only send back a different object but also send it with an error code.

There are many many status and error codes in existence [[Mozilla](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status)],
but one we use a lot is 400 which means "Bad request." It's a good fit for when the user doesn't form their request correctly.

By default, Express sends code 200 "OK" when we give a response, but we can change this.

!!! example

    ```
    app.get('/error', (req, res) => {
        res.status(400).json(
            { 'error': 'Something went wrong :(' }
        );
    });
    ```

### Middleware

Above, when we send data back to the user it always finishes the transaction.

Middleware lets us use a pipe and filter model, where we can create routes that don't finish the transaction and instead act as intermediate filters.

To do this, we have them take an additional parameter--the next thing in the chain.

!!! example

    Here, `example_middleware` is a named function that takes the same request and response parameters, plus an additional next parameter.

    ```
    function example_middleware(req, res, next) {
        if (something_is_invalid()) {
            return res.status(400).json(
                { 'error': 'It brocken' }
            );
        }

        next();
    }
    ```

    Here, we call a function I didn't write to check if something is invalid. If so, we return an error.

    The different behavior happens if that something IS valid. In that case, we call `next()`, which moves onto the next item in the chain.

We can call middleware by putting it as part of the route definition.

!!! example

    To call one middleware, you can just put it there.

    ```
    app.get('/goodbye', example_middleware, (req, res) => {
        res.json(
            { 'message': 'Goodbye, world!' }
        );
    });
    ```

    To call multiple, put them all in an array. Often this is where you want to change up the formatting a bit.

    ```
    app.get(
        '/goodbye', 
        [
            middleware_A,
            middleware_B,
            middleware_C
        ],
        final_function
    );
    ```

## Locating Something Specific

### From a URL on a frontend app

If you know what URL is used to access something from the frontend apps, it's easy to go from there to find the code that is running.

Start at the file `src/backend/api/api.cjs`, and go down to the `SETTING UP ROUTES` comment. Look for the string on the end of your URL.

Once you find it, go to the defintion of 

!!! example

    If I was trying to figure out how `/session/verify` worked, I would look for that text inside the main API file.

    I'd see that it uses two middlewares to check `email` and `code` exist in the body of the message, and then if those succeed it calls `session.routeVerifyOTP`.

    Then, I could just go to that function definition.

### By topic

The API is divided into a few different files, under the
`src/backend/api/routes` directory. These are split based on what resources
they correspond to, so you can get an idea of which file to look in just based
on that.

## Adding New Features

In this section, I'll put myself in the hypothetical of adding something new and go through what a good process for that might look like.

### Creating a new file?

If this new feature is in a totally different category than anything else that exists, it makes sense to create a new file under `src/backend/api/routes` to organize them.

You want the name of this file to match with the type of resource it controls. This encourages good design where route names match up to  

!!! warning

    A bad example here is the `session` routes: A "session" isn't really a
    resource you can access and modify the same way you can something like a
    "user", so this is an ugly exception to the pattern. 

If you don't create a new file, add your route to one of the existing ones. The
organization varies, but it's probably best for them to be alphabetized when
possible.

### Using existing middlewares

#### Body exists

This middleware can be used to make sure something exists in the body of the request the user sent. It doesn't verify whether that item it valid,
only that it exists.

To use it, call `middlewareBodyExists` with the name of the parameter that must exist. It will generate a function that makes sure that specific string is part of the body.

!!! example

    ```
    app.get(
        '/account/username',
        middlewareBodyExists('email'),
        routeGetUsername
    )
    ```

#### JWT verification

This middleware can be used to make sure the user sent you a valid JSON Web Token to prove they are logged in.

To use it, call `middlewareVerifyJWT` with your JWT secret string. This generates a function that verifies any JWT generated with that secret string, since each one is unique.

!!! example

    ```
    app.get(
        '/logged-in',
        middlewareVerifyJWT(TOKEN_SECRET_STRING),
        routeGetLoggedIn 
    );
    ```

### Writing the function

When you write the function, do whatever logic you need to. This often includes:

- Verifying parameters have valid values
- Performing database queries

Once you're done with your work, you need to respond to the request to make sure the route actually terminates. Otherwise it will hang forever.

On success, you can `return res.json(...)`.

On failure, you can `return res.status(###).json(...)`, where `###` is your error code of choice.

!!! warning

    It's important to always `return` when you're trying to finish the request. Many of our routes run async because they interact with
    the database, and if we don't do this they can behave unpredictably and not do what you think they are doing.

    In old code (what I wrote all semester) the `return` and `res.json` lines are separate, but they don't need to be--it's cleaner to do this way.

### Attaching it to a route

Once you've written your function and exported it from the file, you need to write a route in the main `src/backend/api/api.cjs` file to make it a route when the API is running.

!!! example

    A typical route, which I just copied directly from the code.

    It has all the components discussed above.

    ```
    app.post(                            // type
      '/session/signup',                 // URL
      [                                  // middlewares 
        middlewareBodyExists('email'),
        middlewareBodyExists('username'),
      ],
      session.routeSignUpNewUser         // function
    );
    ```
