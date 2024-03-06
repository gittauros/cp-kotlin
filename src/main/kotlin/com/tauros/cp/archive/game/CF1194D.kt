package com.tauros.cp.archive.game

import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/3/6
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1194/D
    // 手画下状态就出来了
    // k是3的倍数就是：
    // 012 345 678k
    // FTT FTT FTTT
    // k不是3的倍数就是：
    // FTT FTT
    val cases = readln().toInt()
    repeat(cases) {
        val (n, k) = readln().split(" ").map(string::toInt)
        if (k % 3 == 0) {
            val m = n % (k + 1)
            val bob = m % 3 == 0 && m != k
            println(if (bob) "Bob" else "Alice")
        } else {
            val bob = n % 3 == 0
            println(if (bob) "Bob" else "Alice")
        }
    }
}