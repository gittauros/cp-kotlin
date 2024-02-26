package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.common.randomTree
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.TreeInfo
import com.tauros.cp.graph.hld
import com.tauros.cp.graph.vtBuilder
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.mmo
import com.tauros.cp.randomInt
import kotlin.math.abs

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
    // 虚树做法
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val vtx = rd.na(n).map { it - 1 }.toIntArray()
        val graph = Graph(n, (n - 1) * 2)
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v); graph.addEdge(v, u)
        }

        val colorMap = (0 until n).groupBy { vtx[it] }

        val vt = graph.vtBuilder(0)
        var ans = 0L
        for ((c, nodes) in colorMap) {
            val tree = vt.buildByKeyNodes(nodes)
            fun Graph.dfs(u: int = 0): int {
                var sum = 0
                each(u) {
                    val v = to[it]
                    val sub = dfs(v)
                    if (vtx[u] == c) ans += sub
                    else {
                        ans += sub.toLong() * sum
                        sum += sub
                    }
                }
                return if (vtx[u] == c) 1 else sum
            }
            tree.dfs()
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}