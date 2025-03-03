fun test1(first: String): Unit {
    println("Hello" + " " + first)
}

fun test2(add: Int): Int {
    return add + 3
}

fun recursive(iterate: Int): Int{
    if (iterate == 100) {
        return iterate
    }
    return recursive(iterate + 1)
}

fun loop(){
val colors = listOf("Red", "Green", "Blue")
for (color in colors) {
    print("$color \n")
    }
}

fun add(a: Int, b: Int): Int {
    val sum = a + b
    return sum
}
fun main(){
    println("Hello World")
    test1("Ethan")
    println(test2(7))
    println(recursive(1))
    val result = add(5, 3)
    println(result)
    loop()
}
main()