package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val a = rd.na(n)
    val m = rd.ni()
    val b = rd.na(m)

    val cntA = iar(101)
    for (num in a) cntA[num] += 1
    val cntB = iar(101)
    for (num in b) cntB[num] += 1

    var ans = 0
    for (i in 1 .. 100) {
        for (j in -1 .. 1) if (i + j in cntB.indices) {
            val res = minOf(cntA[i], cntB[i + j])
            ans += res
            cntA[i] -= res
            cntB[i + j] -= res
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}