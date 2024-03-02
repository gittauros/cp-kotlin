package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.dfz
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/3/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/687/A
    // 相当于二分图染色
    val (n, m) = rd.ni() to rd.ni()
    val graph = Graph(n, m * 2)
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    val vis = bar(graph.totalEdgeCnt)
    val color = iar(n) { -1 }
    fun Graph.dfs(u: int, c: int): boolean {
        if (color[u] == c xor 1) return false
        if (color[u] == c) return true
        color[u] = c
        each(u) {
            if (vis[it]) return@each
            val v = to[it]
            vis[it] = true; vis[it xor 1] = true
            if (!dfs(v, c xor 1)) return false
        }
        return true
    }
    var success = true
    for (i in 0 until n) if (color[i] == -1) {
        if (!graph.dfs(i, 0)) success = false
        if (!success) break
    }

    if (!success) {
        wt.println(-1)
        return
    }
    val vtx = ar(2) { mlo<int>() }
    for (i in 0 until n) vtx[color[i]].add(i + 1)
    for (i in 0 until 2) {
        wt.println(vtx[i].size)
        for (res in vtx[i]) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}