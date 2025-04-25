//
//  ArrayTransformer.swift
//  GetGrinnected
//
//  Created by Budhil Thijm on 4/25/25.
//
import Foundation
import SwiftUI
import SwiftData

@objc(ArrayTransformer)
class ArrayTransformer: ValueTransformer {
    static let name = NSValueTransformerName("ArrayTransformer")
    
    override class func transformedValueClass() -> AnyClass {
        return NSData.self
    }
    
    override class func allowsReverseTransformation() -> Bool {
        return true
    }
    
    override func transformedValue(_ value: Any?) -> Any? {
        guard let array = value as? [String] else { return nil }
        
        do {
            let data = try NSKeyedArchiver.archivedData(withRootObject: array, requiringSecureCoding: false)
            return data
        } catch {
            print("Error transforming array to data: \(error)")
            return nil
        }
    }
    
    override func reverseTransformedValue(_ value: Any?) -> Any? {
        guard let data = value as? Data else { return [] }
        
        do {
            let array = try NSKeyedUnarchiver.unarchiveTopLevelObjectWithData(data) as? [String]
            return array ?? []
        } catch {
            print("Error reverse transforming data to array: \(error)")
            return []
        }
    }
    
    static func register() {
        ValueTransformer.setValueTransformer(
            ArrayTransformer(),
            forName: name
        )
    }
}
