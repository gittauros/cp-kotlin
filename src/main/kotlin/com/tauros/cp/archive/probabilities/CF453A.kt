package com.tauros.cp.archive.probabilities

import com.tauros.cp.common.pow
import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/3/7
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/453/A
    // 摇出[1, max]的概率减去摇出[1, max-1]的概率，然后快速幂一下即可
    val (m, n) = readln().split(" ").map(string::toInt)

    var ans = 0.0
    for (max in m downTo 1) {
        val probIn = pow(max.toDouble() / m, n)
        val probOut = pow((max - 1.0) / m, n)
        val prob = probIn - probOut
        ans += prob * max
    }
    println(string.format("%.8f", ans))
}