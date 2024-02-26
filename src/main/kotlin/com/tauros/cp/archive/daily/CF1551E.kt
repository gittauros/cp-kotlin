package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()
        val nums = rd.na(n)
        val dp = ar(2) { iar(n + 1) }
        var cur = 0

        var res = n
        for (i in 1 .. n) {
            val pre = cur
            cur = 1 - cur
            dp[cur].fill(0)

            for (j in 0 .. i) {
                if (j > 0) {
                    dp[cur][j] = dp[pre][j - 1]
                }
                if (j < i) {
                    dp[cur][j] = maxOf(dp[cur][j], dp[pre][j] + if (nums[i - 1] == i - j) 1 else 0)
                }
                if (dp[cur][j] >= k) {
                    res = minOf(res, j)
                }
            }
        }
        wt.println(if (res >= n) -1 else res)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}