package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1726/problem/D
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val graph = Graph(n, m * 2, true)
        val (edgeId, ori) = iar(m) to iar(m * 2)
        repeat(m) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            edgeId[it] = graph.addEdge(u, v)
            ori[edgeId[it]] = it
            graph.addEdge(v, u)
        }

        val red = 1
        val ans = iar(m) { red }
        val vis = bar(n)
        val parentEdgeId = iar(n) { -1 }
        val dep = iar(n)
        fun Graph.dfs(u: int, fa: int, d: int = 0) {
            vis[u] = true
            dep[u] = d
            each(u) {
                val v = to[it]
                if (v == fa || vis[v]) return@each
                val resolve = it and 1.inv()
                ans[ori[resolve]] = 1 - red
                parentEdgeId[v] = resolve
                dfs(v, u, d + 1)
            }
        }
        graph.dfs(0, -1)

        val restEdgeIds = (0 until m).filter { ans[it] == red }.map { edgeId[it] }
        val restVtx = buildSet {
            for (id in restEdgeIds) {
                add(graph.from[id])
                add(graph.to[id])
            }
        }
        if (restEdgeIds.isNotEmpty() && restVtx.size == restEdgeIds.size) {
            val (u, v) = graph.from[restEdgeIds[0]] to graph.to[restEdgeIds[0]]
            val vtx = if (dep[u] <= dep[v]) v else u
            iao(parentEdgeId[vtx], restEdgeIds[0]).map { ori[it] }.forEach {
                ans[it] = ans[it] xor 1
            }
        }

        for (c in ans) wt.print(c)
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}