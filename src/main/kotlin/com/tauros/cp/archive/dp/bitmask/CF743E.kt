package com.tauros.cp.archive.dp.bitmask

import com.tauros.cp.ar
import com.tauros.cp.common.copyInto
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/29
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/743/E
    // https://github.com/gittauros/Daily_CF_Problems/blob/main/daily_problems/2024/03/0329/solution/cf743e.md
    // 看错题+想不出合适的状态
    // 学羊解，身体倍棒
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toInt() - 1 }.toIntArray()

    val min = n / 8 + 1
    val next = ar(n) { ar(8) { iar(min) { n + 1 } } }
    for (i in n - 1 downTo 0) {
        for (num in 0 until 8) {
            if (i < n - 1) next[i + 1][num].copyInto(next[i][num])
            if (num == nums[i]) {
                for (j in min - 1 downTo 1) next[i][num][j] = next[i][num][j - 1]
                next[i][num][0] = i + 1
            }
        }
    }

    var ans = nums.distinct().size
    val maxState = 1 shl 8
    for (less in 1 .. min) {
        val dp = ar(9) { iar(maxState) { n + 1 } }
        dp[0][0] = 0

        for (cnt in 0 until 8) for (state in 0 until maxState) if (dp[cnt][state] + 1 <= n) {
            val i = dp[cnt][state] + 1
            for (num in 0 until 8) if (1 shl num and state == 0) {
                val nextState = 1 shl num or state
                val j = next[i - 1][num][less - 1]
                dp[cnt][nextState] = minOf(dp[cnt][nextState], j)
                if (less < min) {
                    val k = next[i - 1][num][less]
                    dp[cnt + 1][nextState] = minOf(dp[cnt + 1][nextState], k)
                }
            }
        }
        for (cnt in 0 .. 8) if (dp[cnt][maxState - 1] <= n) {
            ans = maxOf(ans, less * 8 + cnt)
        }
    }
    println(ans)
}