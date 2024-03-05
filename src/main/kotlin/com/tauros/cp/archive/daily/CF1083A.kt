package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.graph.IGraph
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/3/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1083/A
    // dp每个子树单链最长，然后比较单链（单点也是单链）与双链即可
    val n = rd.ni()
    val vtx = rd.na(n)
    val graph = IGraph(n, (n - 1) * 2)

    repeat(n - 1) {
        val (u, v, c) = iao(rd.ni() - 1, rd.ni() - 1, rd.ni())
        graph.addEdge(u, v, c); graph.addEdge(v, u, c)
    }

    var ans = 0L
    fun IGraph.dfs(u: int, fa: int = -1): long {
        var (max1, max2) = lar(2) { -0x3f3f3f3f3f3f3f3fL }
        var dp = vtx[u].toLong()
        each(u) {
            val (v, c) = to[it] to weight[it]
            if (v == fa) return@each
            val chd = dfs(v, u)
            val sub = chd - c
            if (sub > max1) {
                max2 = max1
                max1 = sub
            } else if (sub > max2) {
                max2 = sub
            }
            dp = maxOf(dp, sub + vtx[u])
        }
        ans = maxOf(ans, dp, max1 + max2 + vtx[u])
        return dp
    }
    graph.dfs(0)
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}