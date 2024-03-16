package com.tauros.cp.archive.daily

/**
 * @author tauros
 * 2024/3/15
 */
fun main(args: Array<String>) {
    val cases = readln().toInt()
    repeat(cases) {
        val n = readln().toInt()
        if (n % 2 != 0) {
            println("NO")
            return@repeat
        }
        println("YES")
        val ans = buildString {
            var (rest, iter) = n to 0
            while (rest > 0) {
                append('A' + iter); append('A' + iter)
                iter = 1 - iter; rest -= 2
            }
        }
        println(ans)
    }
}