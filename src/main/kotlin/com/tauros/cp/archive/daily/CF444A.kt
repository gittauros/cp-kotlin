package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.graph.IGraph
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/444/A
    val (n, m) = rd.ni() to rd.ni()
    val vtx = rd.na(n)
    val edges = ar(m) { iao(rd.ni() - 1, rd.ni() - 1, rd.ni()) }

    var ans = 0.0
    for ((u, v, w) in edges) {
        val res = (vtx[u] + vtx[v]) / w.toDouble()
        ans = maxOf(res, ans)
    }
    wt.printf("%.12f\n", ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}