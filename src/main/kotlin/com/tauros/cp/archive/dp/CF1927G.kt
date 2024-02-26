package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/8
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1927/problem/G
    // https://zhuanlan.zhihu.com/p/681725397
    // dp[i][j][k] j为最左边待涂的位置，k为最右边已涂的位置
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        val dp = ar(2) { ar(n + 2) { iar(n + 1) { INF } } }
        dp[0][1][0] = 0
        var cur = 0
        for (i in 1 .. n) {
            val (p, pre) = nums[i - 1] to cur
            val (l, r) = maxOf(1, i - p + 1) to minOf(n, i + p - 1)

            cur = 1 - cur
            dp[cur].onEach { it.fill(INF) }

            for (j in 1 .. n + 1) for (k in 0 .. n) {
                // no
                dp[cur][j][k] = minOf(dp[cur][j][k], dp[pre][j][k])
                // left
                val nlk = maxOf(i, k)
                val nlj = if (j < l) j else nlk + 1
                dp[cur][nlj][nlk] = minOf(dp[cur][nlj][nlk], dp[pre][j][k] + 1)
                // right
                val nrk = maxOf(r, k)
                val nrj = if (j < i) j else nrk + 1
                dp[cur][nrj][nrk] = minOf(dp[cur][nrj][nrk], dp[pre][j][k] + 1)
            }
        }

        val ans = dp[cur][n + 1][n]
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}