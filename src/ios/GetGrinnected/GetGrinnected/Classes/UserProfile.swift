//
//  userProfile.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//

import Foundation
import SwiftUI
/**
 Class for creating a new user profile. For now, we will keep isPowerUser and isAdmin both false.
 New userProfile created only in SIGNUP
 */
class UserProfile: ObservableObject {
    //set email and set password after validating them
    @Published private(set) var emailText = ""
    
    @Published private(set) var isPasswordValid: Bool = false
    
    @Published private(set) var usernameText = ""
    
    
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
        if !emailText.contains("@grinnell.edu") {
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
    
    static var exampleUser = UserProfile()
    let emailSuccess = exampleUser.setEmail("thijmbud@grinnell.edu")
    let passwordSuccess = exampleUser.setPassword("Password123")
    
    
    
    
}
