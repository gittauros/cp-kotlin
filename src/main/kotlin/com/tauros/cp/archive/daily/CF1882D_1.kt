package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/1/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val vtx = rd.na(n)

        val graph = Graph(n, (n - 1) * 2)
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v)
            graph.addEdge(v, u)
        }

        val ans = lar(n)
        repeat(20) { b ->
            val dp = ar(2) { lar(n) }
            val son = ar(2) { lar(n) }
            val size = iar(n)
            val cost = 1L shl b

            fun Graph.dfs(u: int, fa: int) {
                size[u] = 1
                val hit = vtx[u] shr b and 1 == 1
                each(u) {
                    val v = to[it]
                    if (v == fa) return@each
                    dfs(v, u)
                    son[0][u] += dp[0][v]
                    son[1][u] += dp[1][v]
                    size[u] += size[v]
                }
                dp[0][u] = if (hit) son[1][u] + size[u] * cost else son[0][u]
                dp[1][u] = if (hit) son[1][u] else (son[0][u] + size[u] * cost)
            }
            graph.dfs(0, -1)

            val whole = n * cost
            fun Graph.root(u: int, fa: int, fa0: long, fa1: long) {
                val hit = vtx[u] shr b and 1 == 1
                son[1][u] += fa1
                son[0][u] += fa0
                val res0 = if (hit) (son[1][u] + whole) else son[0][u]
                val res1 = if (hit) son[1][u] else son[0][u] + whole
                ans[u] += minOf(res0, res1)

                each(u) {
                    val v = to[it]
                    if (v == fa) return@each
                    son[0][u] -= dp[0][v]
                    son[1][u] -= dp[1][v]
                    val oriSizeV = size[v]
                    size[v] = n
                    size[u] = n - oriSizeV
                    val nex0 = if (hit) (son[1][u] + size[u] * cost) else son[0][u]
                    val nex1 = if (hit) son[1][u] else (son[0][u] + size[u] * cost)
                    root(v, u, nex0, nex1)
                    size[u] = n
                    size[v] = oriSizeV
                    son[0][u] += dp[0][v]
                    son[1][u] += dp[1][v]
                }
            }
            graph.root(0, -1, 0, 0)
        }
        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}