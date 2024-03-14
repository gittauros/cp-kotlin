package com.tauros.cp.archive.graph.shortestpath

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.dq
import com.tauros.cp.graph.IGraph
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/contest/1941/problem/G
    // 边转点，原点拆点拆边，01BFS
    // 下面这个做法正确的前提是输入保证了同一个颜色一定只有一个连通块
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val oriGraph = IGraph(n)
        repeat(m) {
            val (u, v, c) = rd.na(3).map { it - 1 }
            oriGraph.addEdge(u, v, c); oriGraph.addEdge(v, u, c)
        }
        val (st, ed) = rd.ni() - 1 to rd.ni() - 1

        val sorted = buildList { oriGraph.each { add(oriGraph.weight[it]) } }.sorted().distinct().toIntArray()
        val discrete = sorted.withIndex().associate { (i, v) -> v to i }
        val (colors, inBase, outBase) = iao(sorted.size, sorted.size, sorted.size + n)
        val newGraph = IGraph(colors + n + n)
        for (u in 0 until n) {
            val (inU, outU) = u + inBase to u + outBase
            newGraph.addEdge(inU, outU, 1)
            oriGraph.each(u) {
                val c = oriGraph.weight[it]
                newGraph.addEdge(discrete[c]!!, inU, 0)
                newGraph.addEdge(outU, discrete[c]!!, 0)
            }
        }

        val (start, end) = inBase + st to inBase + ed
        val dist = iar(colors + n + n) { 0x3f3f3f3f }
        val vis = bar(colors + n + n)
        val dq = dq<IIP>()
        dist[start] = 0; dq.addFirst(0 to start)
        while (dq.isNotEmpty()) {
            val (uDist, u) = dq.removeFirst()
            if (vis[u]) continue
            vis[u] = true
            newGraph.each(u) {
                val (v, w) = newGraph.to[it] to newGraph.weight[it]
                if (uDist + w < dist[v]) {
                    dist[v] = uDist + w
                    if (w == 0) dq.addFirst(dist[v] to v)
                    else dq.addLast(dist[v] to v)
                }
            }
        }
        wt.println(dist[end])
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}