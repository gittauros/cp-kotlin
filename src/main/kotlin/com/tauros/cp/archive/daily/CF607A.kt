package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()

    data class Beacon(val pos: int, val pow: int)
    val beacons = ar(n) { Beacon(rd.ni(), rd.ni()) }
    beacons.sortBy { it.pos }

    val dp = iar(n + 1)
    var ans = 0
    for (i in 1 .. n) {
        val lt = beacons[i - 1].pos - beacons[i - 1].pow
        val idx = findFirst(i) { beacons[it].pos >= lt }
        dp[i] = dp[idx] + 1
        ans = maxOf(ans, dp[i])
    }
    wt.println(n - ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}