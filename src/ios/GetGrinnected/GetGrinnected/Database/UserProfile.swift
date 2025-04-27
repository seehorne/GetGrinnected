//
//  userProfile.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//

import Foundation

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
    
    
    func setUsername(_ newUsername: String){
       usernameText = newUsername
    }
    
    func setPassword(_ newPassword: String) -> Bool {
        if validatePassword(newPassword){
            _password = newPassword
            isPasswordValid = true
            return true
        } else {
            return false
        }
    }//setpassword
    
    
    /*Validation functino for the email, username, and password*/
    
    private func validateEmail(_  email: String) -> Bool {
        if !email.contains("@grinnell.edu") {
            print("Invalid email format! Must include @grinnell.edu!")
            return false
        }
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
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/login")!
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
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/verify")!
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

                // Success
                completion(.success(decodedResponse.message ?? "Success"))
            } catch {
                print("Decoding error: \(error)")
                completion(.failure(APIError.decoderError))
            }
        }
        task.resume()
    }
    
    
    func signUpUser(email: String, user: String, completion: @escaping (Result<String, Error>) -> Void) {
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/signup")!
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
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/resend-otp")!
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
    
    // all the things that could be in the API response
    // all marked as optional
    struct APIResponse: Codable {
        let error: String?
        let message: String?
        let refresh_token: String?
        let access_token: String?
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
        var localizedStringResource: LocalizedStringResource {
            switch self {
            case .badEmail: return "Email wrong:";
            case .invalidResponse: return "Invalid response from server";
            case .decoderError: return "Could not decode JSON";
            case .signInError(let message):  return "Login error \(message)"
            }
        }
    }

                // Success

            
}

