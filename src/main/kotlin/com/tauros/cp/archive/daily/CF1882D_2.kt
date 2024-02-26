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

        val size = iar(n)
        fun Graph.dp(u: int, fa: int): long {
            size[u] = 1
            var res = 0L
            each(u) {
                val v = to[it]
                if (v == fa) return@each
                res += dp(v, u)
                size[u] += size[v]
                res += (vtx[u] xor vtx[v]).toLong() * size[v]
            }
            return res
        }
        val default = graph.dp(0, -1)

        val ans = lar(n)
        fun Graph.root(u: int, fa: int, res: long) {
            ans[u] = res
            each(u) {
                val v = to[it]
                if (v == fa) return@each
                val diff = (vtx[u] xor vtx[v]).toLong()
                root(v, u, res + diff * (n - size[v] - size[v]))
            }
        }
        graph.root(0, -1, default)

        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}