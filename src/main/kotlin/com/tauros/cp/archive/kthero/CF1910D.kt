package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)
        val dp = ar(n + 1) { iar(2) { INF } }
        dp[0][0] = 0
        dp[0][1] = 0
        for (i in 1 .. n) {
            if (nums[i - 1] > dp[i - 1][0]) {
                dp[i][0] = nums[i - 1]
            } else if (nums[i - 1] + 1 > dp[i - 1][0]) {
                dp[i][0] = nums[i - 1] + 1
            }
            dp[i][1] = dp[i - 1][0]
            if (nums[i - 1] > dp[i - 1][1]) {
                dp[i][1] = minOf(dp[i][1], nums[i - 1])
            } else if (nums[i - 1] + 1 > dp[i - 1][1]) {
                dp[i][1] = minOf(dp[i][1], nums[i - 1] + 1)
            }
        }
        wt.println(if (dp[n][1] != INF) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}