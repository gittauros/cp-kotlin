package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/405/E
    val (n, m) = rd.ni() to rd.ni()
    val graph = Graph(n, m * 2)
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    if (m % 2 == 1) {
        wt.println("No solution")
        return
    }

    val ans = buildString {
        val dep = iar(n) { n }
        fun Graph.dfs(u: int, d: int = 0): int {
            dep[u] = d
            var son = -1
            each(u) {
                val v = to[it]
                if (dep[v] < d) return@each
                val rest = if (dep[v] == n) dfs(v, d + 1) else -1
                if (rest != -1) append("${u + 1} ${v + 1} ${rest + 1}\n")
                else if (son != -1) {
                    append("${son + 1} ${u + 1} ${v + 1}\n")
                    son = -1
                } else son = v
            }
            return son
        }
        graph.dfs(0, -1)
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}