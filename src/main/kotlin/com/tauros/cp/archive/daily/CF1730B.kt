package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.double
import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/20
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
        val x = rd.na(n)
        val t = rd.na(n)

        val idxes = (0 until n).sortedBy { x[it] }.toIntArray()
        val suf = iar(n)
        suf[n - 1] = t[idxes[n - 1]] + x[idxes[n - 1]]
        for (i in n - 2 downTo 0) {
            suf[i] = maxOf(suf[i + 1], t[idxes[i]] + x[idxes[i]])
        }

        var (ans, res) = suf[0] - x[idxes[0]] + 0.0 to x[idxes[0]].toDouble()
        var (l, pre) = x[idxes[0]].toDouble() to t[idxes[0]] - x[idxes[0]]
        for (i in 1 until n) {
            val r = x[idxes[i]].toDouble()
            val (cl, cr) = pre to suf[i]

            fun trans(x0: double) {
                val cur = maxOf(cl + x0, cr - x0)
                if (x0 in l .. r && cur < ans) {
                    ans = cur
                    res = x0
                }
            }
            trans(l)
            trans(r)
            trans((cr - cl) / 2.0)

            pre = maxOf(pre, t[idxes[i]] - x[idxes[i]])
            l = r
        }
        if (pre + x[idxes[n - 1]] < ans) {
            res = x[idxes[n - 1]].toDouble()
        }

        wt.printf("%.6f\n", res)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}