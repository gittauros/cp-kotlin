package com.tauros.cp.archive.young

import com.tauros.cp.ar
import com.tauros.cp.common.inv
import com.tauros.cp.iar

/**
 * @author tauros
 * 2025/3/3
 */
private val mod = 1e9.toInt() + 7
private val inv2 = inv(2.toLong(), mod.toLong())

fun main(args: Array<String>) {
    val (k, m) = readLine()!!.split(" ").map { it.toInt() }
    val n = readLine()!!.map { it - '0' }.reversed().toIntArray()

    val dp = ar(2) { ar(m + 2) { iar(2) } }
    var cur = 0
    dp[cur][0][0] = 1
    for (i in 1 .. k) {
        val pre = cur
        cur = cur xor 1
        dp[cur].forEach { it.fill(0) }
        val b = if (i > n.size) 0 else n[i - 1]

        for (j in 0 .. m) {
            if (b == 1) {
                // 1 0 | 0 1
                dp[cur][j + 1][0] += dp[pre][j][0]
                if (dp[cur][j + 1][0] >= mod) dp[cur][j + 1][0] -= mod
                dp[cur][j + 1][0] += dp[pre][j][0]
                if (dp[cur][j + 1][0] >= mod) dp[cur][j + 1][0] -= mod

                dp[cur][j][1] += dp[pre][j][1]
                if (dp[cur][j][1] >= mod) dp[cur][j][1] -= mod
                dp[cur][j][1] += dp[pre][j][1]
                if (dp[cur][j][1] >= mod) dp[cur][j][1] -= mod
            } else {
                // 0 0
                dp[cur][j][0] += dp[pre][j][0]
                if (dp[cur][j][0] >= mod) dp[cur][j][0] -= mod
                dp[cur][j + 1][0] += dp[pre][j][1]
                if (dp[cur][j + 1][0] >= mod) dp[cur][j + 1][0] -= mod
                // 1 1
                dp[cur][j][1] += dp[pre][j][0]
                if (dp[cur][j][1] >= mod) dp[cur][j][1] -= mod
                dp[cur][j + 1][1] += dp[pre][j][1]
                if (dp[cur][j + 1][1] >= mod) dp[cur][j + 1][1] -= mod
            }
        }
    }
    val ans = (dp[cur][m][0] + (if (m == 0) 0 else dp[cur][m - 1][1])) % mod
    println(if (n.all { it == 0 }) ans else (ans * inv2 % mod).toInt())
}