package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
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

    val sorted = (0 until n).sortedBy { nums[it] }.toIntArray()
    val ans = iar(n) { -1 }
    var (max, pre) = -1 to -1
    for (i in sorted) {
        if (pre != -1 && nums[i] != nums[pre]) max = pre
        if (max > i) ans[i] = max
        pre = maxOf(pre, i)
    }

    for ((i, j) in ans.withIndex()) wt.print("${if (j == -1) -1 else j - i - 1} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}