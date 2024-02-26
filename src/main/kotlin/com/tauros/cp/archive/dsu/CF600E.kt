package com.tauros.cp.archive.dsu

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.dsu
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2023/12/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val vtx = rd.na(n)
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v)
        graph.addEdge(v, u)
    }

    val cnt = iar(n + 1)
    var (max, sum) = 0 to 0L
    fun visitColor(c: int) {
        cnt[c] += 1
        if (cnt[c] > max) {
            max = cnt[c]
            sum = 0
        }
        if (cnt[c] == max) {
            sum += c
        }
    }

    val ans = lar(n)
    graph.dsu(0) { u, fa, keep ->
        visitColor(vtx[u])
        graph.each(u) {
            val v = graph.to[it]
            if (v == fa || v == son[u]) return@each
            for (idx in dfnRange(v)) {
                val node = ori[idx]
                visitColor(vtx[node])
            }
        }
        ans[u] = sum

        if (!keep) {
            for (idx in dfnRange(u)) {
                val node = ori[idx]
                cnt[vtx[node]] -= 1
            }
            max = 0
            sum = 0
        }
    }

    for (i in 0 until n) wt.print("${ans[i]} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}