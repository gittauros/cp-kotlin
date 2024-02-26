package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/2/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, b, x) = rd.na(3)
        val creatures = rd.na(n)

        val cap = creatures.sum()
        val (squads, diff) = lar(cap + 1) to lar(cap + 1)
        for (cnt in creatures) {
            for (sq in 1 until cnt) {
                val (r, low) = cnt % sq to cnt / sq
                var sum = 0L
                sum += low.toLong() * (cnt - low) * (sq - r)
                sum += (low + 1L) * (cnt - low - 1) * r
                squads[sq] += sum / 2 * b
            }
            val sum = (cnt - 1L) * cnt / 2
            diff[cnt] += sum * b
        }
        for (i in 1 .. cap) diff[i] += diff[i - 1]

        var ans = 0L
        for (sq in 1 .. cap) {
            val res = squads[sq] - (sq - 1L) * x + diff[sq]
            ans = maxOf(res, ans)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}