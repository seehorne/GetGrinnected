fun test1(first: String): Unit {
    println("Hello" + " " + first)
}

fun test2(add: Int): Int {
    return add + 3
}

fun main(){
    println("Hello World")
    test1("Ethan")
    println(test2(7))
}
main()