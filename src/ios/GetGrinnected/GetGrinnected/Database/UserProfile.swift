//
//  userProfile.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//

import Foundation
import SwiftData
import SwiftUICore

/**
 Class for creating a new user profile. For now, we will keep isPowerUser and isAdmin both false.
 New userProfile created only in SIGNUP
 */

class UserProfile: ObservableObject {
    
    //set email and set password after validating them
    private var emailText = ""
    
    private var isPasswordValid: Bool = false
    
    private var usernameText = ""
    
    //private storage for password
    private var _password: String = ""
    @Environment(\.modelContext) private var modelContext
    
    
    
    //For now, we do not need this but I have this here just in case.
    //these could also inherit from this class, but for now there's no need.
    private var isPowerUser = false
    private var isAdmin = false
    
    /**Setters*/
    
    //set email
    func setEmail(_ newEmail: String) -> Bool {
        if validateEmail(newEmail){
            emailText = newEmail
            return true
        } else{
            return false
        }//if
    }//function set email
    
    
    func setLocalUsername(newUsername: String){
       usernameText = newUsername
       UserDefaults.standard.set(newUsername, forKey: "username")
    }
    
    
    func setLocalEmail(newEmail: String){
        if validateEmail(newEmail){
            UserDefaults.standard.set(newEmail, forKey: "email")
        }
    }
    
    func setLocalPassword(_ newPassword: String) -> Bool {
        if validatePassword(newPassword){
            _password = newPassword
            isPasswordValid = true
            return true
        } else {
            return false
        }
    }//setpassword
    
    //method to update the login state which is an appstorage value
    func updateLoginState(isLoggedIn: Bool) {
        UserDefaults.standard.set(isLoggedIn, forKey: "isLoggedIn")
    }
    
    /*Validation functino for the email, username, and password*/
    
    private func validateEmail(_  email: String) -> Bool {
        if !email.contains("@grinnell.edu") {
            print("Invalid email format! Must include @grinnell.edu!")
            return false
        }
        print("successfully validated email")
        return true
    }//validateEmail()
    
    private func validatePassword(_ password: String) -> Bool {
        if password.count < 8 {
            print("Password msut be at least 8 characters!")
            return false
        }
        return true
    }

    func loginUser(email: String, completion: @escaping (Result<String, Error>) -> Void) {
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/session/login")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        let body: [String: Any] = [
            "email": email
        ]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)

        //run a post request to specified route
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error { //if it had an undisclosed error, return a failure
                print("Error: \(error)")
                completion(.failure(error))
                return
            }

            guard let data = data else { // no data is also an error
                print("No data received")
                completion(.failure(APIError.invalidResponse))
                return
            }

            do {
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                if let error = decodedResponse.error, !error.isEmpty {
                    //at this point you should have message
                    //so if the error field has stuff in, put message as the error message
                    completion(.failure(APIError.signInError(decodedResponse.message ?? "Error")))
                    return
                }

                // Success
                completion(.success(decodedResponse.message ?? "Success"))
            } catch {
                print("Decoding error: \(error)")
                completion(.failure(APIError.decoderError))
            }
        }
        task.resume()
    }
    
    func verifyUser(email: String, code: String, completion: @escaping (Result<String, Error>) -> Void) {
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/session/verify")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        let body: [String: Any] = [
            "email": email,
            "code": code
        ]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)

        //make post request to the right route
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error { //if it had an undisclosed error, return a failure
                print("Error: \(error)")
                completion(.failure(error))
                return
            }

            guard let data = data else { // no data also is an error
                print("No data received")
                completion(.failure(APIError.invalidResponse))
                return
            }

            do {
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                if let error = decodedResponse.error, !error.isEmpty {
                    //at this point you should have message
                    //so if the error field has stuff in, put message as the error message
                    completion(.failure(APIError.signInError(decodedResponse.message ?? "Error")))
                    return
                }

                // Success??
                // well first check if we got the relevant tokens and if we did, save them for later use
                if let aToken = decodedResponse.access_token, let rToken = decodedResponse.refresh_token,
                             !aToken.isEmpty && !rToken.isEmpty {
                    UserDefaults.standard.set(decodedResponse.access_token, forKey: "accessToken")
                    UserDefaults.standard.set(decodedResponse.refresh_token, forKey: "refreshToken")
                    self.updateLoginState(isLoggedIn: true)//change status to actually logged in
                    //in order to be in this condition we must have an access token so we're fine to assume its here
                    completion(.success(decodedResponse.message ?? "Success"))
                }
                else { //otherwise we are verified but not authorized
                    completion(.failure(APIError.unauthorized))
                }
            } catch {
                print("Decoding error: \(error)")
                completion(.failure(APIError.decoderError))
            }
        }
        task.resume()
    }
    
    
    func signUpUser(email: String, user: String, completion: @escaping (Result<String, Error>) -> Void) {
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/session/signup")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        let body: [String: Any] = [
            "email": email,
            "username": user
        ]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)

        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error { //if it had an undisclosed error, return a failure
                print("Error: \(error)")
                completion(.failure(error))
                return
            }

            guard let data = data else { // no data, also error
                print("No data received")
                completion(.failure(APIError.invalidResponse))
                return
            }

            do {
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                if let error = decodedResponse.error, !error.isEmpty {
                    //at this point you should have message
                    //so if the error field has stuff in, put message as the error message
                    completion(.failure(APIError.signInError(decodedResponse.message ?? "Error")))
                    return
                }

                // Success
                completion(.success(decodedResponse.message ?? "Success"))
            
            } catch {
                print("Decoding error: \(error)")
                completion(.failure(APIError.decoderError))
            }
        }
        task.resume()
    }
    
    //API call to resend OTP
    func resendOTP(email: String, completion: @escaping (Result<String, Error>) -> Void) {
        //make post request to correct route
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/session/resend-code")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        let body: [String: Any] = [
            "email": email,
        ]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        
        //process the response
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error { //if it had an undisclosed error, return a failure
                print("Error: \(error)")
                completion(.failure(error))
                return
            }

            guard let data = data else { //if it didn't give you data, also an error, failure
                print("No data received")
                completion(.failure(APIError.invalidResponse))
                return
            }

            do {//try to recode the response
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                if let error = decodedResponse.error, !error.isEmpty {
                    //at this point you should have message
                    //so if the error field has stuff in, put message as the error message
                    completion(.failure(APIError.signInError(decodedResponse.message ?? "Error")))
                    return
                }

                // Success if you got this far! Yay!
                completion(.success(decodedResponse.message ?? "Success"))
            } catch {
                print("Decoding error: \(error)")
                completion(.failure(APIError.decoderError))
            }
        }
        task.resume()
    }
    
    //try to refactor API error message thing as function
    //this takes in the error and generates the right message for it
    //error is the error an API call returns
    //this function returns the most specific description we can get for what that is
    func getErrorMessage(error: Error) -> String{
        if let apiError = error as? APIError {
            switch apiError {
                case .signInError(let message):
                    return message //get the message out if possible
                default: //if nothing, just used localized description
                    return apiError.localizedDescription
                }
        } else {//not one we wrote the error for
            //use the error message swift came up with
            return error.localizedDescription
        }
    }
    
    //this function refreshes the access token and keeps it local for the user
    //it pulls out the the existing refresh token from UserDefaults to see if its still valid
    //if it isn't, forces logout
    //otherwise, makes an API call to get new tokens, and saves them back to be used in future
    //authorization calls
    func refreshAccessToken(completion: @escaping (Result<String, Error>) -> Void) {
        guard let refreshToken = UserDefaults.standard.string(forKey: "refreshToken") else {
            completion(.failure(APIError.unauthorized))
            return
        }
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/session/refresh")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        //request.setValue("Bearer \(refreshToken)", forHTTPHeaderField: "Authorization")
        request.addValue("Bearer \(refreshToken)", forHTTPHeaderField: "Authorization")
        //request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(.failure(error))
                return
            }
            guard let data = data else {
                completion(.failure(APIError.invalidResponse))
                return
            }
            do {
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                // Store new tokens
                if let aToken = decodedResponse.access_token, let rToken = decodedResponse.refresh_token,
                             !aToken.isEmpty && !rToken.isEmpty {
                    UserDefaults.standard.set(decodedResponse.access_token, forKey: "accessToken")
                    UserDefaults.standard.set(decodedResponse.refresh_token, forKey: "refreshToken")
                    //in order to be in this condition we must have an access token so we're fine to assume its here
                    completion(.success(aToken))
                }
                else {
                    completion(.failure(APIError.unauthorized))
                }
            } catch {
                completion(.failure(APIError.decoderError))
            }
        }
        task.resume()
    }
    
    
    //request builder, the actual call we care about
    //any authorized call (ie that requires being logged in, should be made wrapped in this)
    //it will check if the access token is fine, and if it is, make the call
    //if the access token isn't fine, it will attempt to refresh it, and if it can do so, then make the call
    //if after an attempted refresh, the code still isnt fine, the user shouldn't be logged in, so they get logged out
    func safeApiCall( requestBuilder: @escaping (String) -> URLRequest,
                      completion: @escaping (Result<Data, Error>) -> Void) {
        //Get current access token
        guard let accessToken = UserDefaults.standard.string(forKey: "accessToken") else {
            completion(.failure(APIError.unauthorized))
            return
        }
        //Make the API call
        //this is an internal function, not my favorite, but does the job
        func makeRequest(token: String) {
            var request = requestBuilder(token)
            let task = URLSession.shared.dataTask(with: request) { data , response, error in
                if let error = error {
                    completion(.failure(error))
                    self.updateLoginState(isLoggedIn: false)
                    return
                }
                guard let httpResponse = response as? HTTPURLResponse else {
                    completion(.failure(APIError.invalidResponse))
                    return
                }
                if httpResponse.statusCode == 403 {
                    // Try refreshing token and retrying
                        self.refreshAccessToken { result in
                            switch result {
                            case .success(let newToken):
                                let retryRequest = requestBuilder(newToken)
                                let internalTask = URLSession.shared.dataTask(with: retryRequest) { data, response, error in
                                    if let error = error {
                                        completion(.failure(error))
                                        return
                                    }
                                    guard let data = data else {
                                        completion(.failure(APIError.invalidResponse))
                                        return
                                    }
                                    completion(.success(data))
                                }
                                internalTask.resume()
                            case .failure(let refreshError):
                                completion(.failure(refreshError))
                            }
                        }
                    return
                    }
                //handle it if it ISNT a 403
                guard let data = data else {
                    completion(.failure(APIError.invalidResponse))
                    return
                }
                completion(.success(data))
                }
            task.resume()
            }
        makeRequest(token: accessToken)
        }
    
    
    //this calls the API to get the username as stored in the database
    func getUsername() {
         safeApiCall(requestBuilder: { token in
            var request = URLRequest(url: URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/username")!)
            request.httpMethod = "GET"
            request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse.message ?? "Success but no response to print")
                    if decodedResponse.username != nil {
                        print(decodedResponse.username!)
                        self.setLocalUsername(newUsername: decodedResponse.username!)
                    }
                }
            case .failure(let error):
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    //this takes user input for a username change and sends it to the API to be stored in the database
    func setUsername(newUsername: String, completion: @escaping (Result<String, Error>) -> Void) {
        print("the set username function is being called at least")
         safeApiCall(requestBuilder: { token in
            var request = URLRequest(url: URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/username")!)
            request.httpMethod = "PUT"
            let body: [String: Any] = [
                "username": newUsername,
            ]
            request.httpBody = try? JSONSerialization.data(withJSONObject: body)
            request.addValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                print("There was success")
                //print("Success! Set username: \(data)")
                if let raw = String(data: data, encoding: .utf8) {
                    print("Raw API Response: \(raw)")
                } else {
                    print("Couldn't decode raw data to UTF-8 string.")
                }
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse.message ?? "Success but no response to print")
                }
            case .failure(let error):
                print("There was not success")
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    
    //this calls the API to get the username as stored in the database
    func getEmail() {
         safeApiCall(requestBuilder: { token in
            var request = URLRequest(url: URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/email")!)
            request.httpMethod = "GET"
            request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse.message ?? "Success but no response to print")
                    if decodedResponse.email != nil {
                        print(decodedResponse.username!)
                        self.setLocalEmail(newEmail: decodedResponse.email!)
                    }
                }
            case .failure(let error):
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    //this takes user input for a email change and sends it to the API to be stored in the database
    func setEmail(newEmail: String, completion: @escaping (Result<String, Error>) -> Void) {
        print("the set email function is being called at least")
         safeApiCall(requestBuilder: { token in
            var request = URLRequest(url: URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/username")!)
            request.httpMethod = "PUT"
            let body: [String: Any] = [
                "email": newEmail,
            ]
            request.httpBody = try? JSONSerialization.data(withJSONObject: body)
            request.addValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                print("There was success")
                //print("Success! Set username: \(data)")
                if let raw = String(data: data, encoding: .utf8) {
                    print("Raw API Response: \(raw)")
                } else {
                    print("Couldn't decode raw data to UTF-8 string.")
                }
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse.message ?? "Success but no response to print")
                }
            case .failure(let error):
                print("There was not success")
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    //this function takes a model context as a parameter, so that it can save and update the events saved by a user as notified
    //it gets these events by pulling the API for the IDs that are marked by the user as notified
    //then it goes through all events s.t. they can be marked that way in local storage as well
    //it must be passed this model context from a View, as that is the only place model contexts can actually be updated
    func getUserNotifiedEvents(context: ModelContext) {
         safeApiCall(requestBuilder: { token in
            var request = URLRequest(url: URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/events/notified")!)
            request.httpMethod = "GET"
             request.addValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
             request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                if let jsonString = String(data: data, encoding: .utf8) {
                    print("Raw JSON response: \(jsonString)")
                }
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse)
                    print(decodedResponse.message ?? "Success but no response to print")
                    //this has to be done on the main thread, since its using a model context
                    //so even though this isn't an explicitly async function, we have to put it here
                    DispatchQueue.main.async {
                        do {
                            let notifiedIDs = decodedResponse.notified_events ?? []
                            print(notifiedIDs)
                            let fetchDescriptor = FetchDescriptor<EventModel>()
                            let allEvents = try context.fetch(fetchDescriptor)
                            for event in allEvents {
                                if notifiedIDs.contains(event.id) {
                                    event.notified = true
                                    print(event.id)
                                }
                            }
                        } catch {
                            print("Error decoding or fetching: \(error)")
                        }
                    }
                }
            case .failure(let error):
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    //this function takes an int array as a parameter, specifically one of all the IDs a user has marked as notified
    //this array is based on the events they have clicked the bell icon for, in the moment of call or previous
    //it then sends this array to the API so that the local notified events can be present there too
    func setUserNotifiedEvents(events: [Int]) {
         safeApiCall(requestBuilder: { token in
             let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/events/notified")!
            var request = URLRequest(url: url)
            request.httpMethod = "PUT"
             let body: [String: Any] = [
                 "notified_events": events,
             ]
            request.httpBody = try? JSONSerialization.data(withJSONObject: body)
             request.addValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
             request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse.message ?? "Success but no response to print")
                }
            case .failure(let error):
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    //this function takes a model context as a parameter, so that it can save and update the events saved by a user as favorited
    //it gets these events by pulling the API for the IDs that are marked by the user as favorited
    //then it goes through all events s.t. they can be marked that way in local storage as well
    //it must be passed this model context from a View, as that is the only place model contexts can actually be updated
    func getUserFavoritedEvents(context: ModelContext) {
        print("calling getUserFavoritedEvents")
         safeApiCall(requestBuilder: { token in
             let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/events/favorited")!
             var request = URLRequest(url: url)
             request.httpMethod = "GET"
             request.addValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
             request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                if let jsonString = String(data: data, encoding: .utf8) {
                    print("Raw JSON response: \(jsonString)")
                }
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse)
                    print(decodedResponse.message ?? "Success but no response to print")
                    //this has to be done on the main thread, since its using a model context
                    //so even though this isn't an explicitly async function, we have to put it here
                    DispatchQueue.main.async {
                        do {
                            let favoritedIDs = decodedResponse.favorited_events ?? []
                            print(favoritedIDs)
                            let fetchDescriptor = FetchDescriptor<EventModel>()
                            let allEvents = try context.fetch(fetchDescriptor)
                            for event in allEvents {
                                if favoritedIDs.contains(event.id) {
                                    event.favorited = true
                                    print(event.id)
                                }
                            }
                        } catch {
                            print("Error decoding or fetching: \(error)")
                        }
                    }
                }
            case .failure(let error):
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    //this function takes an int array as a parameter, specifically one of all the IDs a user has marked as favorited
    //this array is based on the events they have clicked the heart icon for, in the moment of call or previous
    //it then sends this array to the API so that the local favorited events can be present there too
    func setUserFavoritedEvents(events: [Int]){
         safeApiCall(requestBuilder: { token in
             let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/events/favorited")!
             var request = URLRequest(url: url)
            request.httpMethod = "PUT"
             let body: [String: Any] = [
                 "favorited_events": events,
             ]
            request.httpBody = try? JSONSerialization.data(withJSONObject: body)
             //print(request.httpBody)
             print("Bearer \(token)") //checking if this is malformed or smth
             //request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
             request.addValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
             request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                //print("Success! Set favorited events: \(data)")
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse.message ?? "Success but no response to print")
                }
            case .failure(let error):
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    //this function is used to get all of a user's data, basically pulls their db entry and returns it to the app
    func getUserData()  {
         safeApiCall(requestBuilder: { token in
            var request = URLRequest(url: URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/data")!)
            request.httpMethod = "GET"
            //request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
             request.addValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
             request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            return request
        }, completion: { result in
            switch result {
            case .success(let data):
                if let decodedResponse = try? JSONDecoder().decode(APIResponse.self, from: data) {
                    print(decodedResponse.message ?? "Success but no response to print")
                }
            case .failure(let error):
                print(self.getErrorMessage(error: error))
            }
        })
    }
    
    //this function makes an int array of all the IDs a user has marked as favorited
    //this array is sourced from the cache's account of what has been favorited
    //it goes through all the events, and picks out the ones that match the predicate of favorited
    //then puts all their ids in an array
    //if no events have been favorited, this returns an empty array
    func fetchFavoritedEventIDs(from context: ModelContext) -> [Int] {
        let predicate = #Predicate<EventModel> { $0.favorited }
        let descriptor = FetchDescriptor<EventModel>(predicate: predicate)

        if let results = try? context.fetch(descriptor) {
            print(results.map { $0.id })
            print("in context fetch favorites")
            return results.map { $0.id }
        } else {
            print("in \"else\" favorites")
            return []
        }
    }
    
    //this function makes an int array of all the IDs a user has marked as notified
    //this array is sourced from the cache's account of what has been notified
    //it goes through all the events, and picks out the ones that match the predicate of notified
    //then puts all their ids in an array
    //if no events have been notified, this returns an empty array
    func fetchNotifiedEventIDs(from context: ModelContext) -> [Int] {
        let predicate = #Predicate<EventModel> { $0.notified }
        let descriptor = FetchDescriptor<EventModel>(predicate: predicate)

        if let results = try? context.fetch(descriptor) {
            print("in context fetch notifications")
            print(results.map { $0.id })
            return results.map { $0.id }
        } else {
            print("in \"else\" notifications")
            return []
        }
    }
    
    
    // all the things that could be in the API response
    // all marked as optional
    struct APIResponse: Codable {
        let error: String?
        let message: String?
        let refresh_token: String?
        let access_token: String?
        let favorited_events: [Int]?
        let notified_events: [Int]?
        let username: String?
        let email: String?
    }
    
    /*
     ASSOCIATED ERRORS WITH API CALLING
     (named so as to not conflict with existing parent classes)
     */
    /// Custom errors (
    enum APIError: Swift.Error, CustomLocalizedStringResourceConvertible {
        case badEmail
        case invalidResponse
        case decoderError
        case signInError(String)
        case unauthorized
        case usernameError(String)
        case emailError(String)
        var localizedStringResource: LocalizedStringResource {
            switch self {
            case .badEmail: return "Email wrong:";
            case .invalidResponse: return "Invalid response from server";
            case .decoderError: return "Could not decode JSON";
            case .signInError(let message):  return "Login error \(message)";
            case .usernameError(let message): return "Username error \(message)";
            case .emailError(let message): return "Email error \(message)";
            case .unauthorized: return "Could not authenticate user session"
            }
        }
    }

                // Success

            
}

