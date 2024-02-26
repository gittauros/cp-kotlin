package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
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
    val cases = rd.ni()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()

        val graph = Graph(n, (n - 1) * 2)
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v)
            graph.addEdge(v, u)
        }

        val size = iar(n)
        fun Graph.dfs(u: int, fa: int) {
            size[u] = 1
            each(u) {
                val v = to[it]
                if (v == fa) return@each
                dfs(v, u)
                size[u] += size[v]
            }
        }
        graph.dfs(0, -1)

        var ans = 0L
        val edges = buildList {
            for (i in 1 until n) {
                val res = size[i].toLong() * (n - size[i])
                ans += res * 2
                add(res)
            }
        }.sortedDescending()

        for (i in 0 until k - 1) {
            ans -= edges[i]
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}