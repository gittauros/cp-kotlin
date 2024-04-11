package com.tauros.cp.archive.mex

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/10
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1875/D
    // 设dp[i]为mex为i时清空数组的最小花费
    val cases = rd.ni()
    val cap = 5000
    val cnt = iar(cap)
    val dp = iar(cap)
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        for (i in 0 until n) cnt[i] = 0
        for (num in nums) if (num in 0 until n)
            cnt[num] += 1
        val mex = (0 until n).firstOrNull { cnt[it] == 0 } ?: n

        var ans = if (mex == 0) 0 else mex * (cnt[0] - 1)
        for (i in 1 until mex) {
            dp[i] = 0x3f3f3f3f
            for (j in 0 until i) {
                dp[i] = minOf(dp[i], dp[j] + (cnt[j] - 1) * i + j)
            }
            ans = minOf(ans, dp[i] + mex * (cnt[i] - 1) + i)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}