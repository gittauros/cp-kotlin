package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.graph.Graph
import com.tauros.cp.lar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/1/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1515/F
    val (n, m, x) = rd.na(3)
    val componentSum = rd.nal(n)
    val graph = Graph(n, m * 2)
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    val sum = componentSum.reduce(long::plus)
    if (sum < (n - 1L) * x) {
        wt.println("NO")
        return
    }

    wt.println("YES")
    val vis = bar(n)
    val tail = mlo<int>()
    fun Graph.dfs(u: int, fa: int) {
        vis[u] = true
        each(u) {
            val (v, e) = to[it] to it / 2 + 1
            if (v == fa || vis[v]) return@each
            dfs(v, u)
            if (componentSum[v] >= x) {
                wt.println(e)
                componentSum[u] += componentSum[v] - x
            } else {
                tail.add(e)
            }
        }
    }
    graph.dfs(0, -1)
    for (i in tail.indices.reversed()) wt.println(tail[i])
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}