@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.graph.Graph

/**
 * @author tauros
 */
private val bufCap = 128
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val n = rd.ni()
    val graph = Graph(n, (n - 1) shl 1)
    val value = IntArray(n)
    for (i in 0 until n) {
        val u = rd.ni() - 1
        value[u] = rd.ni()
        val m = rd.ni()
        for (j in 0 until m) {
            val v = rd.ni() - 1
            graph.addEdge(u, v)
            graph.addEdge(v, u)
        }
    }
    val INF = 0x3f3f3f3f
    fun dfs(u: Int, fa: Int): IntArray {
        val ans = IntArray(3)
        // cur parent child
        var minAdd = INF
        var chdCovered = false
        graph.each(u) {
            val v = graph.to[it]
            if (v == fa) return@each
            val res = dfs(v, u)
            ans[0] += res.reduce(::minOf)
            ans[1] += minOf(res[0], res[2])
            if (res[0] <= res[2]) {
                chdCovered = true
                ans[2] += res[0]
            } else {
                ans[2] += res[2]
            }
            minAdd = minOf(minAdd, res[0] - res[2])
        }
        ans[0] += value[u]
        if (!chdCovered) {
            ans[2] += minAdd
        }
        return ans
    }
    val ans = dfs(0, -1)
    wt.println(minOf(ans[0], ans[2]))
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}