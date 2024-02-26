package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ao
import com.tauros.cp.structure.IIHeap

/**
 * @author tauros
 * 2023/11/20
 */
private val bufCap = 65536

private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val ranges = ao(rd.na(n), rd.na(n))
        var rMin = INF
        var lMax = 0
        var ans = 0L
        var max = 0L
        for (i in 0 until n) {
            val (a, b) = ranges[0][i] to ranges[1][i]
            val (l, r) = minOf(a, b) to maxOf(a, b)
            if (i != 0) {
                if (rMin < l) max = maxOf(max, (l - rMin) * 2L)
                if (lMax > r) max = maxOf(max, (lMax - r) * 2L)
            }
            rMin = minOf(rMin, r)
            lMax = maxOf(lMax, l)
            ans += r - l
        }
        wt.println(ans + max)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}