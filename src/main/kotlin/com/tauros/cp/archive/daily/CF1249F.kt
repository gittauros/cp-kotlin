package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1249/F
    val (n, k) = rd.ni() to rd.ni()
    val vtx = rd.na(n)
    val graph = Graph(n, (n - 1) shl 1)
    repeat(n - 1) {
        val (u, v) = rd.na(2).map { it - 1 }
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    fun Graph.dp(u: int, fa: int): Pair<IntArray, IntArray> {
        var dp = iar(n + 1)
        val suf = iar(n + 1)
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            val (chd, chdSuf) = dp(v, u)
            val new = iar(n + 1)
            for (d in 1 .. n) {
                val other = maxOf(d, k + 1 - d)
                new[d] = maxOf(chd[d - 1] + suf[other], dp[d] + chdSuf[other - 1])
            }
            dp = new
            for (d in n - 1 downTo 0) if (d == n - 1) suf[d] = dp[d] else suf[d] = maxOf(suf[d + 1], dp[d])
        }
        dp[0] = vtx[u] + if (k + 1 <= n) suf[k + 1] else 0
        suf[0] = maxOf(suf[0], dp[0])
        return dp to suf
    }
    val (_, suf) = graph.dp(0, -1)
    wt.println(suf[0])
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}