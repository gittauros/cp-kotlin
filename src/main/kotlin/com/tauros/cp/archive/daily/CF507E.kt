package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.graph.EdgeGraph
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/507/E
    // out[0] + out[1] + in[0] + in[1] = m
    // in[0] + in[1] = dist[n - 1]
    //
    // ans = min{ out[1] + in[0] } -> min{ out[1] - in[1] } + dist[n - 1]
    // out[1] + in[1] = all[1]
    // ans = min { all[1] - in[1] - in[1] } + dist[n - 1]
    val (n, m) = rd.ni() to rd.ni()
    data class Edge(val to: int, val p: int, val id: int)
    val graph = EdgeGraph<Edge>(n, m * 2, true)
    var ans = 0
    repeat(m) {
        val (x, y, z) = iao(rd.ni() - 1, rd.ni() - 1, rd.ni())
        graph.addEdge(x, Edge(y, z, it))
        graph.addEdge(y, Edge(x, z, it))
        ans += z
    }

    val (pre, dp) = iar(n) { -1 } to iar(n) { -1 }
    dp[0] = 0
    val (vis, dq) = bar(n) to dq<int>()
    dq.addLast(0)
    vis[0] = true
    val dist = iar(n)
    while (dq.isNotEmpty()) {
        val u = dq.removeFirst()
        graph.each(u) {
            val (v, p) = get(it)
            if ((!vis[v] || dist[v] == dist[u] + 1) && dp[u] + p > dp[v]) {
                dp[v] = dp[u] + p
                pre[v] = it xor 1
            }
            if (!vis[v]) {
                dq.addLast(v)
                dist[v] = dist[u] + 1
                vis[v] = true
            }
        }
    }
    ans += dist[n - 1]

    val inPath = bar(m)
    var iter = n - 1
    while (true) {
        val (to, _, id) = if (pre[iter] != -1) graph[pre[iter]] else break
        inPath[id] = true
        iter = to
    }

    ans -= dp[n - 1] * 2
    wt.println(ans)
    graph.each {
        if (it % 2 == 1) return@each
        val (v, p, id) = graph[it]
        val u = from[it]
        val change = inPath[id] && p == 0 || !inPath[id] && p == 1
        if (change) {
            wt.println("${u + 1} ${v + 1} ${p xor 1}")
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}