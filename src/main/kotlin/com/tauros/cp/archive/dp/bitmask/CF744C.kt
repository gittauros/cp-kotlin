package com.tauros.cp.archive.dp.bitmask

import com.tauros.cp.ar
import com.tauros.cp.common.char
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/30
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/744/C
    // 好难，这个dp状态要反着表示，用省了多少代替表示花了多少
    val n = readln().toInt()
    data class Card(val c: char, val r: int, val b: int)
    val cards = ar(n) {
        val (c, r, b) = readln().split(" ")
        Card(c[0], r.toInt(), b.toInt())
    }

    val inf = 0x3f3f3f3f
    val maxState = 1 shl n
    // dp[买到了mask][省了多少红]=最少花多少蓝
    val dp = ar(maxState) { iar(n * n + 1) { inf } }
    dp[0][0] = 0
    for (state in 0 until maxState) for (save in 0 .. n * n) if (dp[state][save] != inf) {
        var (r, b) = 0 to 0
        for (i in 0 until n) if (1 shl i and state != 0) {
            val (c) = cards[i]
            if (c == 'R') r += 1 else b += 1
        }
        for (i in 0 until n) if (1 shl i and state == 0) {
            val nextState = 1 shl i or state
            val (_, cr, cb) = cards[i]
            val (rCost, bCost) = maxOf(0, cr - r) to maxOf(0, cb - b)
            val nextSave = save + cr - rCost
            dp[nextState][nextSave] = minOf(dp[nextState][nextSave], dp[state][save] + bCost)
        }
    }
    val totalRed = cards.map { it.r }.reduceOrNull(int::plus) ?: 0
    var ans = inf
    for (save in 0 .. n * n) if (dp[maxState - 1][save] != inf) {
        val res = maxOf(dp[maxState - 1][save], totalRed - save) + n
        ans = minOf(ans, res)
    }
    println(ans)
}