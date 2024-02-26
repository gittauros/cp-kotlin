package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.dfz
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1923/problem/E
    // 点分治做法，用点分治时，所有要每层动态申请的结构尽量要避免，常数巨大，尤其是hash set/map
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val vtx = rd.na(n).map { it - 1 }.toIntArray()
        val graph = Graph(n, (n - 1) * 2)
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v); graph.addEdge(v, u)
        }

        val vis = iar(n)
        val tot = iar(n)
        val sub = iar(n)
        val totQ = iar(n)
        val subQ = iar(n)
        var ans = 0L
        dfz(graph, 0) { root ->
            var (subHead, subTail) = 0 to 0
            fun Graph.collect(u: int, fa: int) {
                val c = vtx[u]
                if (vis[c] == 0) {
                    sub[c] += 1
                    subQ[subTail++] = c
                }
                vis[c] += 1
                each(u) {
                    val v = to[it]
                    if (v == fa || deleted[v]) return@each
                    collect(v, u)
                }
                vis[c] -= 1
            }

            var (head, tail) = 0 to 0
            g.each(root) {
                val v = g.to[it]
                if (deleted[v]) return@each
                g.collect(v, root)
                while (subHead < subTail) {
                    val c = subQ[subHead++]
                    if (c == vtx[root]) ans += sub[c]
                    else {
                        ans += sub[c].toLong() * tot[c]
                        tot[c] += sub[c]
                    }
                    sub[c] = 0
                    totQ[tail++] = c
                }
            }
            while (head < tail) tot[totQ[head++]] = 0
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}