package com.tauros.cp.archive.daily

import com.tauros.cp.common.string
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/22
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/566/F
    // 只能往倍数转移
    val n = readln().toInt()
    val nums = readln().split(" ").map(string::toInt).toIntArray()

    val cap = 1e6.toInt()
    val cnt = iar(cap + 1)
    for (num in nums) cnt[num] += 1

    val dp = iar(cap + 1) { cnt[it] }
    var ans = 0
    for (i in 1 .. cap) if (dp[i] != 0) {
        ans = maxOf(ans, dp[i])
        for (j in i + i .. cap step i) {
            dp[j] = maxOf(dp[j], dp[i] + cnt[j])
        }
    }
    println(ans)
}