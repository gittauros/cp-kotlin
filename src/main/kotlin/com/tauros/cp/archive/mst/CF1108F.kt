package com.tauros.cp.archive.mst

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.DSU
import com.tauros.cp.common.int

/**
 * @author tauros
 * 2024/1/16
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1108/F
    val (n, m) = rd.ni() to rd.ni()

    data class Edge(val u: int, val v: int, val w: int)
    val edges = ar(m) {
        Edge(rd.ni() - 1, rd.ni() - 1, rd.ni())
    }
    edges.sortBy { it.w }

    val dsu = DSU(n)
    var ans = 0
    var (p, q) = 0 to 0
    var (exp, act) = 0 to 0
    while (p < m) {
        if (p >= q) {
            ans += exp - act
            exp = 0
            while (q < m && edges[q].w == edges[p].w) {
                val (u, v) = edges[q]
                if (dsu.find(u) != dsu.find(v)) exp++
                q++
            }
            act = 0
        }
        val (u, v) = edges[p]
        if (dsu.find(u) != dsu.find(v)) {
            dsu.merge(u, v, false)
            act++
        }
        p++
    }
    ans += exp - act
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}