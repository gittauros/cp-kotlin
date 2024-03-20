package com.tauros.cp.archive.daily

import com.tauros.cp.common.findFirst
import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/3/19
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/773/A
    // 推了好久O1，没推出来，二分好了
    val cases = readln().toInt()
    val cap = 1e18.toLong()
    repeat(cases) {
        val (x, y, p, q) = readln().split(" ").map(string::toInt)
        val ans = findFirst(cap) { add ->
            val a = (y + add) / q * q
            if (a < y) return@findFirst false
            val b = a / q * p
            if (b < x) return@findFirst false
            b - x <= a - y
        }
        println(if (ans < cap) ans else -1)
    }
}