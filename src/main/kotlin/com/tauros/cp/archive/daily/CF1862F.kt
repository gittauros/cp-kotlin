package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
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
    val cases = rd.ni()
    repeat(cases) {
        val (w, f) = rd.ni() to rd.ni()
        val n = rd.ni()
        val s = rd.na(n)

        val sum = s.sum()
        val (wMax, fMax) = iar(sum + 1) to iar(sum + 1)
        for (h in s) {
            for (v in sum downTo h) wMax[v] = maxOf(wMax[v], wMax[v - h] + h)
            for (v in sum downTo h) fMax[v] = maxOf(fMax[v], fMax[v - h] + h)
        }
        val cap = (sum + w - 1L) / w
        val ans = findFirst(cap) { time ->
            val (wCap, fCap) = minOf(time * w, sum.toLong()).toInt() to minOf(time * f, sum.toLong()).toInt()
            sum - wMax[wCap] <= fCap || sum - fMax[fCap] <= wCap
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}