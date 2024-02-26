package com.tauros.cp.archive.dsu

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.dfz
import com.tauros.cp.graph.dsu
import com.tauros.cp.iar
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

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
    // 启发式合并做法
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val vtx = rd.na(n).map { it - 1 }.toIntArray()
        val graph = Graph(n, (n - 1) * 2)
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v); graph.addEdge(v, u)
        }

        val colors = iar(n)
        val subColors = iar(n)
        val q = iar(n)
        val vis = iar(n)
        var ans = 0L
        graph.dsu(0) { rt, fa, keep ->
            var (head, tail) = 0 to 0
            fun Graph.collect(u: int, fa: int) {
                val c = vtx[u]
                if (vis[c] == 0) {
                    subColors[c] += 1
                    q[tail++] = c
                }
                vis[c] += 1
                each(u) {
                    val v = to[it]
                    if (v == fa) return@each
                    collect(v, u)
                }
                vis[c] -= 1
            }

            ans += colors[vtx[rt]]
            colors[vtx[rt]] = 1
            g.each(rt) {
                val u = g.to[it]
                if (u == fa || u == son[rt]) return@each
                g.collect(u, rt)
                while (head < tail) {
                    val c = q[head++]
                    if (c == vtx[rt]) ans += subColors[c]
                    else {
                        ans += colors[c].toLong() * subColors[c]
                        colors[c] += subColors[c]
                    }
                    subColors[c] = 0
                }
            }

            if (!keep) {
                for (i in dfnRange(rt)) {
                    val v = ori[i]
                    val c = vtx[v]
                    colors[c] = 0
                }
            }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}