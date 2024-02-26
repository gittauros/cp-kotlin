@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.graph.Graph
import kotlin.math.min

/**
 * @author tauros
 */
private val bufCap = 512
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

fun main(args: Array<String>) {
    solve()
    wt.flush()
}

private fun solve() {
    val n = rd.ni()
    val graph = Graph(n, (n - 1) shl 1)
    for (i in 0 until n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v)
        graph.addEdge(v, u)
    }
    val p = IntArray(n)
    val s = IntArray(n)
    val c = IntArray(n)
    var temp: Int
    fun dfs(u: Int, fa: Int = 0) {
        // parent self chd
        var chdCovered = false
        var minAdd = INF
        graph.each(u) {
            val v = graph.to[it]
            if (v == fa) return@each
            dfs(v, u)
            temp = min(s[v], c[v])
            s[u] += min(p[v], temp)
            p[u] += temp
            if (c[v] < s[v]) {
                c[u] += c[v]
            } else {
                chdCovered = true
                c[u] += s[v]
            }
            if (!chdCovered) {
                minAdd = min(minAdd, s[v] - c[v])
            }
        }
        if (!chdCovered) {
            c[u] += minAdd
        }
        if (fa != 0) {
            s[u]++
        }
    }

    var ans = 0
    graph.each(0) {
        val v = graph.to[it]
        dfs(v)
        ans += s[v]
    }
    wt.println(ans)
}
