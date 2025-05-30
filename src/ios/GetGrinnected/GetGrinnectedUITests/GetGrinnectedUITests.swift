//
//  GetGrinnectedUITests.swift
//  GetGrinnectedUITests
//
//  Created by Ellie Seehorn on 3/4/25.
//

import XCTest
import ViewInspector
@testable import GetGrinnected
import SwiftUICore

final class GetGrinnectedUITests: XCTestCase {

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.

        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }

    func testExample() throws {
        // UI tests must launch the application that they test.
        let app = XCUIApplication()
        app.launch()

        // Use XCTAssert and related functions to verify your tests produce the correct results.
    }

    func testLaunchPerformance() throws {
        if #available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 7.0, *) {
            // This measures how long it takes to launch your application.
            measure(metrics: [XCTApplicationLaunchMetric()]) {
                XCUIApplication().launch()
            }
        }
    }
    
    final class LoginViewTests: XCTestCase {
        func testLoginViewLoadsCorrectly() throws {
            // Create an instance of the view
            let loginView = LoginView(isLoggedIn: .constant(false))
            
            let vStack = try loginView.inspect().vStack()
            
            let emailInput = try vStack.text(0)
            XCTAssertEqual(try emailInput.string(), "Grinnell Email")
            
            let passwordInput = try vStack.text(1)
            XCTAssertEqual(try passwordInput.string(), "Password")
            

            let signUpLink = try vStack.text(2)
            XCTAssertEqual(try signUpLink.string(), "Don't have an account?")
        }
    }
}
