package com.tauros.cp.archive.pain

import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/3
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/457/A
    // q^n = q^(n-1) + q^(n-2)
    // 重要的点在于 2*q^n > q^(n-1) + q^(n-2) + ... + q^2 + q + 1
    val a = readln()
    val b = readln()

    val len = maxOf(a.length, b.length)
    val cmpA = "0".repeat(len - a.length) + a
    val cmpB = "0".repeat(len - b.length) + b
    val cmp = iar(len) { cmpA[it] - cmpB[it] }

    for (i in 0 until len) {
        if (cmp[i] < -1) {
            println("<"); return
        } else if (cmp[i] > 1) {
            println(">"); return
        } else if (i + 2 < len) {
            cmp[i + 1] += cmp[i]; cmp[i + 2] += cmp[i]
            cmp[i] = 0
        }
    }

    val (c2, c1) = (if (len < 2) 0 else cmp[len - 2]) to cmp[len - 1]
    println(if (c2 == 0 && c1 == 0) "=" else if (c2 < 0 || c2 == 0 && c1 < 0) "<" else ">")
}