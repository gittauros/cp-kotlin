package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val hps = rd.na(n)
    val min = iar(n)
    var ans = INF
    for ((i, hp) in hps.withIndex()) {
        var res = INF
        if (i > 0) {
            res = 0
            var (a, b) = minOf(hp, hps[i - 1]) to maxOf(hp, hps[i - 1])
            if (a < b) {
                val diff = b - a
                val cut = minOf(diff, (b + 1) / 2)
                b -= cut * 2
                a -= cut
                res += cut
            }
            if (b > 0) {
                val cut = (a + b + 2) / 3
                b -= cut * 3
                a -= cut * 3
                res += cut
            }
        }

        val cur = (hp + 1) / 2
        min[i] = minOf(if (i >= 1) min[i - 1] else INF, cur)
        if (i >= 2) {
            res = minOf(res, cur + min[i - 2])
        }

        val (pre, suf) = (if (i == 0) INF else hps[i - 1]) to (if (i < n - 1) hps[i + 1] else INF)
        val doubleSide = maxOf(pre, suf)
        res = minOf(res, doubleSide)
        val oneSide = maxOf(0, minOf(pre, suf) - cur)
        res = minOf(res, oneSide + cur)

        if (pre % 2 == 1 && suf % 2 == 1) {
            res = minOf(res, pre / 2 + suf / 2 + 1)
        }

        ans = minOf(ans, res)
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}