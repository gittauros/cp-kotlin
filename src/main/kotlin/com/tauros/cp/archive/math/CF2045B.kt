package com.tauros.cp.archive.math

/**
 * @author tauros
 * 2025/1/3
 */
fun main(args: Array<String>) {
    // 学的羊解，感觉很奇妙
    var (n, d, s) = readln().split(" ").map { it.toLong() }
    n /= s
    d /= s
    if (n - 1 <= d) {
        println(n * s)
    } else {
        n = minOf(n, 2 * d)
        for (i in 2 .. 1e6.toInt()) {
            if (n % i == 0L && n - n / i <= d) {
                println(n * s)
                return
            }
        }
        println((n - 1) * s)
    }
}