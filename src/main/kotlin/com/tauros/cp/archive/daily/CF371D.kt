package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.DSU
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/371/D
    val n = rd.ni()
    val caps = rd.na(n)
    val vols = iar(n)
    val dsu = DSU(n + 1)
    val m = rd.ni()
    repeat(m) {
        val tp = rd.ni()
        if (tp == 1) {
            val (p, x) = rd.ni() - 1 to rd.ni()
            var rest = x
            while (rest > 0) {
                val iter = dsu.find(p)
                if (iter == n) break
                val cut = minOf(caps[iter] - vols[iter], rest)
                vols[iter] += cut; rest -= cut
                if (vols[iter] == caps[iter]) dsu.merge(iter, iter + 1)
            }
        } else {
            val k = rd.ni() - 1
            wt.println(vols[k])
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}