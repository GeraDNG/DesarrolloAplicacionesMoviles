package com.example.t01_p14

// Gerardo Daniel Naranjo Gallegos, A01209499

println("P14: Duplicate the elements of a list.")

// SOLUCION 1: LISTA FIJA
/*
var lista:ArrayList<Char> = ArrayList()
var lista2:ArrayList<Char> = ArrayList()
lista.add('a')
lista.add('b')
lista.add('c')
lista.add('d')

for (temp in lista) {
    lista2.add(temp)
    lista2.add(temp)
}

print("La lista inicial es: ")
for (temp in lista) {
    print(temp)
    print(", ")
}
println()
print("La lista duplicada es: ")
for (temp in lista2) {
    print(temp)
    print(", ")
}
*/

//SOLUCION 2: LISTA DADA POR TERMINAL
var lista:ArrayList<Char> = ArrayList()
var lista2:ArrayList<Char> = ArrayList()

println("Please insert a list of elements, for example: abcd")
var entrada = readLine()!!

for (temp in 0..entrada.length-1) {
    lista.add(entrada.get(temp))
    lista2.add(entrada.get(temp))
    lista2.add(entrada.get(temp))
}

print("The initial list is: ")
for (temp in lista) {
    print(temp)
    print(", ")
}
println()
print("The duplicated list is: ")
for (temp in lista2) {
    print(temp)
    print(", ")
}