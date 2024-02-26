package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.common.MInt
import com.tauros.cp.common.int
import com.tauros.cp.common.sum
import com.tauros.cp.common.withMod
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar
import com.tauros.cp.miar

/**
 * @author tauros
 * 2024/2/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val (s, t) = rd.ni() - 1 to rd.ni() - 1
        val graph = Graph(n, m * 2)
        repeat(m) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v); graph.addEdge(v, u)
        }

        val dist = iar(n) { INF }
        val q = dq<int>()
        q.addLast(s)
        dist[s] = 0
        while (q.isNotEmpty()) {
            val u = q.removeFirst()
            graph.each(u) {
                val v = graph.to[it]
                if (dist[v] != INF) return@each
                dist[v] = dist[u] + 1
                q.addLast(v)
            }
        }
        withMod(1e9.toInt() + 7) {
            val (dp, inq) = ar(2) { miar(n) } to bar(n)
            dp[0][s] = MInt.ONE
            q.addLast(s)
            inq[s] = true
            var d = 0
            while (q.isNotEmpty()) {
                q.forEach { inq[it] = false }
                repeat(q.size) {
                    val u = q.removeFirst()
                    graph.each(u) {
                        val v = graph.to[it]
                        if (d + 1 > dist[v] + 1) return@each
                        val to = dp[if (d + 1 == dist[v]) 0 else 1]
                        val from = dp[if (d == dist[u]) 0 else 1]
                        to[v] += from[u]
                        if (!inq[v]) {
                            q.addLast(v)
                            inq[v] = true
                        }
                    }
                }
                d += 1
            }

            wt.println(dp[0][t] + dp[1][t])
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}