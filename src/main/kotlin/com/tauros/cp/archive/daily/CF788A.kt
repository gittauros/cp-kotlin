package com.tauros.cp.archive.daily

import com.tauros.cp.ar
import com.tauros.cp.common.string
import com.tauros.cp.lar
import kotlin.math.abs

/**
 * @author tauros
 * 2024/3/14
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/788/A
    // 随便水个最大子段和
    val n = readln().toInt()
    val nums = readln().split(" ").map(string::toInt).toIntArray()

    val diff = (1 until n).map { abs(nums[it] - nums[it - 1]) }.toIntArray()

    val INF = 0x3f3f3f3f3f3f3f3fL
    var ans = -INF
    val dp = ar(n) { lar(2) { -INF } }
    dp[0][0] = 0
    for (i in 1 until n) {
        val cur = diff[i - 1].toLong()
        if (i - 2 >= 0) dp[i][0] = maxOf(dp[i][0], dp[i - 2][0] + diff[i - 2] - cur)
        dp[i][0] = maxOf(dp[i][0], dp[i - 1][1] - cur)
        if (i - 2 >= 0) dp[i][1] = maxOf(dp[i][1], dp[i - 2][1] + cur - diff[i - 2])
        dp[i][1] = maxOf(dp[i][1], dp[i - 1][0] + cur)
        dp[i][1] = maxOf(dp[i][1], cur)
        ans = maxOf(ans, dp[i][1], dp[i][0])
    }
    println(ans)
}