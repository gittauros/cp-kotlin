package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/1/24
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/264/C
    // 考虑只用记录最大颜色和次大颜色
    val (n, q) = rd.ni() to rd.ni()
    val (value, color) = rd.na(n) to rd.na(n)

    val dp = lar(n + 1)
    repeat(q) {
        val (a, b) = rd.nl() to rd.nl()

        dp.fill(-INF_LONG)
        dp[0] = 0
        var (first, second) = 0 to 1
        for (i in 0 until n) {
            val (v, c) = value[i] to color[i]
            if (dp[c] != -INF_LONG) dp[c] = maxOf(dp[c], dp[c] + v * a)

            dp[c] = maxOf(dp[c], (if (c == first) dp[second] else dp[first]) + v * b)
            // 维护最大和次大的标号不能相同
            if (c != first) {
                if (dp[c] > dp[first]) {
                    second = first
                    first = c
                } else if (c != second && dp[c] > dp[second]) {
                    second = c
                }
            }
        }
        wt.println(dp[first])
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}