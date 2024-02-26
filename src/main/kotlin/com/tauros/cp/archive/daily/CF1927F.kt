package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.DSU
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.graph.IGraph
import com.tauros.cp.iao
import com.tauros.cp.iar
import kotlin.math.abs

/**
 * @author tauros
 * 2024/2/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1927/problem/F
    val cases = rd.ni()
    val cap = 2e5.toInt() + 10
    val log = iar(cap)
    for (i in 2 until cap) log[i] = log[i / 2] + 1
    data class Edge(val u: int, val v: int, val w: int)
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val edges = ar(m) { Edge(rd.ni() - 1, rd.ni() - 1, rd.ni()) }

        edges.sortBy { it.w }
        val (vis, dsu) = bar(n) to DSU(n)
        val (used, graph) = bar(m) to IGraph(n)
        for ((i, edge) in edges.withIndex()) {
            val (u, v, w) = edge
            if (dsu.find(u) == dsu.find(v)) continue
            dsu.merge(u, v, false)
            used[i] = true
            vis[u] = true; vis[v] = true
            graph.addEdge(u, v, w); graph.addEdge(v, u, w)
        }

        val parent = ar(n) { iar(log[n] + 1) { -1 } }
        val minEdge = ar(n) { iar(log[n] + 1) { INF } }
        val dep = iar(n)
        fun IGraph.dfs(u: int, fa: int = -1, len: int = INF, d: int = 0) {
            parent[u][0] = fa; minEdge[u][0] = len
            for (b in 1 .. log[n]) if (parent[u][b - 1] != -1) {
                parent[u][b] = parent[parent[u][b - 1]][b - 1]
                minEdge[u][b] = minOf(minEdge[u][b - 1], minEdge[parent[u][b - 1]][b - 1])
            }
            dep[u] = d
            each(u) {
                val v = to[it]
                if (v == fa) return@each
                dfs(v, u, weight[it], d + 1)
            }
        }
        for (i in 0 until n) if (vis[i] && dsu.find(i) == i) {
            graph.dfs(i)
        }

        fun lca(i: int, j: int): IIP {
            var (u, v, min) = iao(i, j, INF)
            while (dep[u] != dep[v]) {
                val b = log[abs(dep[u] - dep[v])]
                if (dep[u] > dep[v]) {
                    min = minOf(min, minEdge[u][b])
                    u = parent[u][b]
                } else {
                    min = minOf(min, minEdge[v][b])
                    v = parent[v][b]
                }
            }
            if (u == v) return u to min
            for (b in log[n] downTo  0) {
                if (parent[u][b] != parent[v][b]) {
                    min = minOf(min, minEdge[u][b], minEdge[v][b])
                    u = parent[u][b]; v = parent[v][b]
                }
            }
            min = minOf(min, minEdge[u][0], minEdge[v][0])
            return parent[u][0] to min
        }
        var (p, q, l, ans) = iao(-1, -1, -1, INF)
        for (i in 0 until m) if (!used[i]) {
            val (u, v) = edges[i]
            val (lca, res) = lca(u, v)
            if (res < ans) {
                ans = res; p = u; q = v; l = lca
            }
        }

        tailrec fun goUp(cur: int, end: int, calc: (Int) -> Unit) {
            calc(cur)
            if (cur == end) return else goUp(parent[cur][0], end, calc)
        }
        val path1 = buildList { goUp(p, l) { add(it + 1) } }
        val path2 = buildList { goUp(q, l) { add(it + 1) } }
        val path = path1.reversed() + path2.subList(0, path2.size - 1)
        wt.println("$ans ${path.size}")
        for (res in path) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}