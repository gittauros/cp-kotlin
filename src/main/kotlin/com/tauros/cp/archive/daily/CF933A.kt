package com.tauros.cp.archive.daily

import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/15
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/933/A
    // 乱dp乱搞，中间的部分枚举下翻转的状态就好，空间得控制好
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toInt() - 1 }.toIntArray()

    val cnt = nums.runningFold(0, int::plus).toIntArray()
    var ans = 0
    val dp = ar(2) { iar(4) }
    for (l in 0 until n) {
        var cur = 0
        dp[cur].fill(0)
        for (r in l until n) {
            val pre = cur; cur = 1 - cur
            dp[cur].fill(0)
            val num = nums[r]
            // 0
            val head = l - cnt[l]
            // 1
            val tail = cnt[n] - cnt[r + 1]

            // 00
            dp[cur][0] = maxOf(dp[pre][0], if (num == 0) dp[pre][0] + 1 else 0)
            // 11
            dp[cur][3] = maxOf(dp[pre][3], if (num == 1) dp[pre][3] + 1 else 0)
            // 01
            dp[cur][1] = maxOf(dp[pre][1], if (num == 1) maxOf(dp[pre][0], dp[pre][1]) + 1 else 0)
            // 10
            dp[cur][2] = maxOf(dp[pre][2], if (num == 0) maxOf(dp[pre][3], dp[pre][2]) + 1 else 0)
            val res = head + dp[cur].max() + tail
            ans = maxOf(res, ans)
        }
    }
    println(ans)
}