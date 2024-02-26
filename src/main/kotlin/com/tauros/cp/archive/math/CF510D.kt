package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.gcd
import com.tauros.cp.common.int
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2023/11/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val lens = rd.na(n)
    val costs = rd.na(n)
    var dp = mmo<int, int>().default { INF }
    dp[0] = 0
    for (i in 0 until n) {
        val new = mmo<int, int>().default { INF }.also { it.putAll(dp) }
        for ((g, c) in dp) {
            val gcd = gcd(g, lens[i])
            new[gcd] = minOf(new[gcd], c + costs[i])
        }
        dp = new
    }
    val ans = if (dp[1] == INF) -1 else dp[1]
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}