package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/6
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1144/G
    val n = rd.ni()
    val nums = rd.na(n)

    val decPre = ar(2) { iar(n + 1) }
    val dp = ar(2) { inc -> iar(n + 1) { if (inc == 0) -INF else INF } }
    dp[0][0] = INF
    dp[1][0] = -INF

    for (i in 1 .. n) {
        if (i == 1 || nums[i - 1] > nums[i - 2]) {
            dp[0][i] = dp[0][i - 1]
            decPre[0][i] = decPre[0][i - 1]
        }
        if (i == 1 || nums[i - 1] < nums[i - 2]) {
            dp[1][i] = dp[1][i - 1]
            decPre[1][i] = i - 1
        }

        if (i > 1 && nums[i - 1] > dp[1][i - 1]) {
            if (nums[i - 2] > dp[0][i]) {
                dp[0][i] = nums[i - 2]
                decPre[0][i] = i - 1
            }
        }
        if (i > 1 && nums[i - 1] < dp[0][i - 1]) {
            if (nums[i - 2] < dp[1][i]) {
                dp[1][i] = nums[i - 2]
                decPre[1][i] = decPre[0][i - 1]
            }
        }
    }

    if (dp[0][n] == -INF && dp[1][n] == INF) {
        wt.println("NO")
    } else {
        wt.println("YES")
        val ans = iar(n)
        val end = if (dp[0][n] != -INF) decPre[0][n] else n
        var iter = end
        while (iter > 0) {
            ans[iter - 1] = 1
            iter = decPre[1][iter]
        }
        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}