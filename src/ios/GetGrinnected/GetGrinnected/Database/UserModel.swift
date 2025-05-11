//
//  UserModel.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/24/25.
//

import SwiftData

//the storage of users locally (the person's own data, but this is to be made more secure!
@Model
final class UserModel {
    var username: String
    var email: String
    var password: String
    var isPaswordValid: Bool
    var isLoggedIn: Bool = false
    init(username: String = "", email: String = "", password: String = ""){
        self.username = username
        self.email = email
        self.password = password
        self.isPaswordValid = false
        self.isLoggedIn = false
    }
    
    
}
