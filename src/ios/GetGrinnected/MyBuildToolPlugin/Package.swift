// swift-tools-version: 6.0
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "MyBuildToolPlugin",
    products: [
        // Products can be used to vend plugins, making them visible to other packages.
        .plugin(
            name: "MyBuildToolPlugin",
            targets: ["MyBuildToolPlugin"]),
    ],
    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .plugin(
            name: "MyBuildToolPlugin",
            capability: .buildTool()
        ),
        .executableTarget(name: "GetGrinnected",
                          path: "Sources/GetGrinnected")
    ]
)
