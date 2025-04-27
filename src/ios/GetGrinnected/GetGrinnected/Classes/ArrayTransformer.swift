//
//  ArrayTransformer.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/25/25.
//
import Foundation
import SwiftUI
import SwiftData

/**
 Required by swiftData in order to transform EventDTO array [] attributes into EventModel array attributes
 */
@objc(ArrayTransformer) //signifier
class ArrayTransformer: ValueTransformer {
    //find array transformer
    static let name = NSValueTransformerName("ArrayTransformer")
    
    //overrid the function to set the class as itself
    override class func transformedValueClass() -> AnyClass {
        return NSData.self
    }
    
    //return true for reverse transformation
    override class func allowsReverseTransformation() -> Bool {
        return true
    }
    
    //transformed value according to our arays
    override func transformedValue(_ value: Any?) -> Any? {
        //let array as the value, return nil if nothing (from array returnd ata)
        guard let array = value as? [String] else { return nil }
        
        //try to archive data, if not, return nothing
        do {
            let data = try NSKeyedArchiver.archivedData(withRootObject: array, requiringSecureCoding: false)
            return data
        } catch {
            print("Error transforming array to data: \(error)")
            return nil
        }
    }
    
    //reversed transformed value
    override func reverseTransformedValue(_ value: Any?) -> Any? {
        //from data, return array
        guard let data = value as? Data else { return [] }
        
        //unarchive values from our cache, using this function.
        do {
            //tell archiver what type our data will be in
            let array = try NSKeyedUnarchiver.unarchivedObject(ofClasses: [NSArray.self, NSString.self], from: data) as? [String]
            return array ?? []
        } catch {
            print("Error reverse transforming data to array: \(error)")
            return []
        }
    }
    
    //register the array transformer in GetGrinnectedApp (@main)
    static func register() {
        ValueTransformer.setValueTransformer(
            ArrayTransformer(),
            forName: name
        )
    }
}
