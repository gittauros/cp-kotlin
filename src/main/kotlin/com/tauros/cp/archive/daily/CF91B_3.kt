package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/91/B
    val n = rd.ni()
    val nums = rd.na(n)

    val suf = iar(n)
    for (i in n - 1 downTo 0) suf[i] = if (i == n - 1) nums[i] else minOf(suf[i + 1], nums[i])
    val ans = iar(n) { -1 }
    for (i in 0 until n - 1) if (suf[i + 1] < nums[i]) {
        ans[i] = findFirst(i + 1, n) { suf[it] >= nums[i] } - 1
    }

    for ((i, j) in ans.withIndex()) wt.print("${if (j == -1) -1 else j - i - 1} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}