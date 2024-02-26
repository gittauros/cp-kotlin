package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2023/12/12
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    val f = iar(n - 1)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        f[it] = graph.addEdge(u, v)
        graph.addEdge(v, u)
    }
    val edges = iar((n - 1) * 2) { 1 }

    val m = rd.ni()
    val roads = ar(m) { iar(n) }
    val checks = ar(m) { iar(2) }
    repeat(m) { i ->
        val (st, ed, w) = iao(rd.ni() - 1, rd.ni() - 1, rd.ni())
        var len = 0
        fun Graph.find(u: int, fa: int, d: int): boolean {
            if (u == ed) {
                len = d
                return true
            }
            var res = false
            eachBreakable(u) {
                val v = to[it]
                if (v == fa) return@eachBreakable false
                if (find(v, u, d + 1)) {
                    roads[i][d] = it
                    res = true
                    true
                } else false
            }
            return res
        }
        graph.find(st, -1, 0)
        for (j in 0 until len) {
            val id = roads[i][j]
            edges[id] = maxOf(edges[id], w)
            edges[id xor 1] = maxOf(edges[id xor 1], w)
        }
        checks[i][0] = w
        checks[i][1] = len
    }
    var success = true
    for ((i, road) in roads.withIndex()) {
        var cur = INF
        for (j in 0 until checks[i][1]) {
            cur = minOf(cur, edges[road[j]])
        }
        if (checks[i][0] != cur) {
            success = false
            break
        }
    }
    if (!success) {
        wt.println(-1)
        return
    }
    for (i in 0 until n - 1) wt.print("${edges[f[i]]} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}