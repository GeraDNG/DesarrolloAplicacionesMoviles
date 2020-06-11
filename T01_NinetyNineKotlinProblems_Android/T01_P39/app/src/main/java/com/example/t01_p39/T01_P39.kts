package com.example.t01_p39

// Gerardo Daniel Naranjo Gallegos, A01209499

println("P39: Given a range of integers by its lower and upper limit, construct a list of all prime numbers in that range.")

var repeat = true
var LL = 0
var UL = 0

do {
    println("Please, give me the lower limit of the range: ")
    var lowerLimit = readLine()!!
    try {
        LL = lowerLimit.toInt()
    } catch (e: NumberFormatException) {
        println("Please, give a valid number, like: 0")
        break
    }
    println("Please, give me the upper limit of the range: ")
    var upperLimit = readLine()!!
    try {
        UL = upperLimit.toInt()
    } catch (e: NumberFormatException) {
        println("Please, give a valid number, like: 1")
        break
    }
    if (LL <= UL) {
        repeat = false
        getPrimeNumbers()
    }
    else {
        println("Please, give a valid range.")
    }
} while (repeat)

// Solved using Sieve of Eratosthenes: https://www.geeksforgeeks.org/sieve-of-eratosthenes/
fun getPrimeNumbers () {
    var prime:ArrayList<Boolean> = ArrayList()
    if (UL < 2) {
        println("There are not prime numbers in this range (from $LL to $UL)")
        return
    }
    else if (UL == 2) {
        println("The only prime number in this range (from $LL to $UL) is: 2")
        return
    }
    else {
        for (temp in 0..UL+1 step 1) {
            prime.add(true)
        }
        for (i in 2..UL step 1) {
            if (prime.get(i)) {
                for (j in (i*i)..UL step i ) {
                    prime.set(j, false)
                }
            }
        }
        prime.set(0, false)
        prime.set(1, false)
        print("The prime numbers in the range (from $LL to $UL) are: ")
        for (k in LL..UL step 1) {
            if (prime.get(k)) {
                print(k)
                print(", ")
            }
        }
    }
}