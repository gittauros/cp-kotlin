package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar
import java.io.FileInputStream

/**
 * @author tauros
 * 2023/11/16
 */
private val bufCap = 65536

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val k = rd.nl()
    val vtx = rd.na(n)
    val graph = Graph(n, m)
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v)
    }
    val cap = vtx.maxOrNull() ?: 1
    val deg = iar(n)
    val dist = iar(n)
    val q = iar(n)
    val ans = findFirst(1, cap + 1) { lim ->
        deg.fill(0)
        dist.fill(0)
        var tot = 0
        for (u in 0 until n) if (vtx[u] <= lim) {
            tot++
            graph.each(u) {
                val v = graph.to[it]
                if (vtx[v] <= lim) deg[v]++
            }
        }
        var (head, tail) = 0 to 0
        for (u in 0 until n) if (vtx[u] <= lim && deg[u] == 0) {
            dist[u] = 1
            q[tail++] = u
        }
        while (head < tail) {
            val u = q[head++]
            tot--
            if (dist[u] >= k) return@findFirst true
            graph.each(u) {
                val v = graph.to[it]
                if (vtx[v] <= lim) {
                    dist[v] = maxOf(dist[v], dist[u] + 1)
                    if (--deg[v] == 0) {
                        q[tail++] = v
                    }
                }
            }
        }
        tot > 0
    }
    wt.println(if (ans > cap) -1 else ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}