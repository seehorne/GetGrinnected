//
//  userProfile.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/1/25.
//

import Foundation
import SwiftUI
import SwiftData


/**
 Class for creating a new user profile. For now, we will keep isPowerUser and isAdmin both false.
 New userProfile created only in SIGNUP
 
 A SwiftData file that saves data of the user, SECURELY..
 */

//add model attribute to make it persistent.
//for now no model
//@Model
final class UserProfile: ObservableObject {
    //set email and set password after validating them
    @Published private(set) var emailText = ""
    
    @Published private(set) var isPasswordValid: Bool = false
    
    @Published private(set) var usernameText = ""
    
    
    //basic initializer, temporary placeholder for now, no model
//    init(emailText: String = "", isPasswordValid: Bool, usernameText: String = "", isPowerUser: Bool = false, isAdmin: Bool = false) {
//        self.emailText = emailText
//        self.isPasswordValid = isPasswordValid
//        self.usernameText = usernameText
//        self.isPowerUser = isPowerUser
//        self.isAdmin = isAdmin
//    }
    
    
    //For now, we do not need this but I have this here just in case.
    //these could also inherit from this class, but for now there's no need.
    private var isPowerUser = false
    private var isAdmin = false
    
    //initialize password
    private var _password = ""
    
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


}

