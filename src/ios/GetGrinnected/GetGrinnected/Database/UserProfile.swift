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

        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error: \(error)")
                completion(.failure(error))
                return
            }

            guard let data = data else {
                print("No data received")
                completion(.failure(APIError.invalidResponse))
                return
            }

            do {
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                if let error = decodedResponse.error, !error.isEmpty {
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

        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error: \(error)")
                completion(.failure(error))
                return
            }

            guard let data = data else {
                print("No data received")
                completion(.failure(APIError.invalidResponse))
                return
            }

            do {
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                if let error = decodedResponse.error, !error.isEmpty {
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
            if let error = error {
                print("Error: \(error)")
                completion(.failure(error))
                return
            }

            guard let data = data else {
                print("No data received")
                completion(.failure(APIError.invalidResponse))
                return
            }

            do {
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                if let error = decodedResponse.error, !error.isEmpty {
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
    
    func resendOTP(email: String, completion: @escaping (Result<String, Error>) -> Void) {
        let url = URL(string: "https://node16049-csc324--spring2025.us.reclaim.cloud/user/resend-otp")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        let body: [String: Any] = [
            "email": email,
        ]
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)

        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Error: \(error)")
                completion(.failure(error))
                return
            }

            guard let data = data else {
                print("No data received")
                completion(.failure(APIError.invalidResponse))
                return
            }

            do {
                let decodedResponse = try JSONDecoder().decode(APIResponse.self, from: data)
                if let error = decodedResponse.error, !error.isEmpty {
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

