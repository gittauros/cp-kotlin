package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.structure.IIHeap

/**
 * @author tauros
 * 2024/1/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    val grid = ar(k) { rd.na(n).map { it - 1 }.toIntArray() }

    val positions = ar(n) { mlo<int>() }
    for (nums in grid) for ((i, num) in nums.withIndex()) {
        positions[num].add(i)
    }
    positions.sortBy { it[0] }

    val dp = iar(n)
    var ans = 0
    for (i in 0 until n) {
        var res = 0
        for (j in 0 until i) if ((0 until k).all { positions[j][it] < positions[i][it] }) {
            res = maxOf(dp[j], res)
        }
        dp[i] = res + 1
        ans = maxOf(ans, dp[i])
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}