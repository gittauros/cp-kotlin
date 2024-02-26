package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/59/E
    val (n, m, k) = rd.na(3)
    val graph = Graph(n, m * 2)
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }
    fun state(a: int, b: int, c: int) = (a.toLong() * n + b) * n + c
    val illegal = buildSet {
        repeat(k) {
            val (a, b, c) = rd.na(3).map { it - 1 }
            add(state(a, b, c))
        }
    }

    val dist = ar(n) { iar(n) { INF } }
    val (vis, pre) = ar(n) { bar(n) } to ar(n) { iar(n) { -1 } }
    val q = dq<IIP>()
    q.addLast(0 to n)
    var step = 0
    while (q.isNotEmpty()) {
        val size = q.size
        repeat(size) {
            val (u, p) = q.removeFirst()
            if (p < n) {
                if (vis[p][u]) return@repeat
                vis[p][u] = true
            }
            graph.each(u) {
                val v = graph.to[it]
                if (p < n && state(p, u, v) in illegal) return@each
                if (step + 1 < dist[u][v]) {
                    dist[u][v] = step + 1
                    pre[u][v] = p * n + u
                    q.addLast(v to u)
                }
            }
        }
        step += 1
    }

    val ans = (0 until n).minBy { dist[it][n - 1] }
    if (dist[ans][n - 1] == INF) {
        wt.println(-1)
    } else {
        wt.println(dist[ans][n - 1])
        val path = buildList {
            var pos = ans * n + n - 1
            while (true) {
                val (u, v) = pos / n to pos % n
                add(v + 1)
                if (u >= n) break
                pos = pre[u][v]
            }
        }.reversed()
        for (res in path) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}