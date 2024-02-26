package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.graph.IGraph

/**
 * @author tauros
 * 2023/12/6
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val graph = IGraph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        val w = rd.ni()
        graph.addEdge(u, v, w)
        graph.addEdge(v, u, w)
    }

    val ans = buildList {
        fun IGraph.dfs(u: int, fa: int): boolean {
            var res = false
            each(u) {
                val (v, w) = to[it] to weight[it]
                if (v == fa) return@each
                val chd = dfs(v, u)
                if (w == 2 && !chd) {
                    res = true
                    add(v + 1)
                }
                res = res or chd
            }
            return res
        }
        graph.dfs(0, -1)
    }

    wt.println(ans.size)
    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}