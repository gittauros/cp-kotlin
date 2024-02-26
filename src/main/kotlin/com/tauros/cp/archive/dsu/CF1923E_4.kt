package com.tauros.cp.archive.dsu

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.swap
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

        val colors = ar(n) { mmo<int, int>().default { 0 } }
        var ans = 0L
        fun Graph.dfs(u: int, fa: int) {
            each(u) {
                val v = to[it]
                if (v == fa) return@each
                dfs(v, u)
                ans += colors[v][vtx[u]]
                if (colors[v].size > colors[u].size) colors.swap(u, v)
                for ((c, cnt) in colors[v]) if (c != vtx[u]) {
                    ans += colors[u][c] * cnt.toLong()
                    colors[u][c] += cnt
                }
            }
            colors[u][vtx[u]] = 1
        }
        graph.dfs(0, -1)
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}