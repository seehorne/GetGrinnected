//
//  GetGrinnectedTestsCI.swift
//  GetGrinnectedTestsCI
//
//  Created by Michael Paulin on 3/30/25.
//

import Testing
@testable import GetGrinnected
import SwiftUI

struct GetGrinnectedTestsCI {

    @Suite("Main navigation view tests")
    struct LoginViewTests {
        let LoginView: LoginView
        
        init () throws {
            LoginView = .init(isLoggedIn: .constant(true))
        }
        
        // Tests that the main view has started
        @Test("Login view starts") func LoginViewStarts() throws {
            #expect(LoginView != nil)
        }
    }

}
