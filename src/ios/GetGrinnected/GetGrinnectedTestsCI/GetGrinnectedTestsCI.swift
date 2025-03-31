//
//  GetGrinnectedTestsCI.swift
//  GetGrinnectedTestsCI
//
//  Created by Michael Paulin on 3/30/25.
//

import Testing
@testable import GetGrinnected

struct GetGrinnectedTestsCI {

    @Suite("Main navigation view tests")
    struct MainNavViewTests {
        let mainNavView: MainNavView
        
        init () throws {
            mainNavView = .init()
        }
        
        // Tests that the main view has started
        @Test("Main navigation view starts") func MainNavViewStarts() throws {
            #expect(mainNavView != nil)
        }
    }

}
