package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.MInt
import com.tauros.cp.common.MIntArray
import com.tauros.cp.common.int
import com.tauros.cp.common.ma
import com.tauros.cp.common.mm
import com.tauros.cp.common.sum
import com.tauros.cp.common.withMod
import com.tauros.cp.graph.Graph
import com.tauros.cp.miao
import com.tauros.cp.miar

/**
 * @author tauros
 * 2024/2/20
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1929/D
    // 极其挣扎
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val graph = Graph(n, (n - 1) * 2)
        repeat(n - 1) {
            val (u, v) = rd.ni() - 1 to rd.ni() - 1
            graph.addEdge(u, v); graph.addEdge(v, u)
        }

        withMod(998244353) {
            var ans = 1
            fun Graph.dfs(u: int, fa: int): int {
                var one = 1
                each(u) {
                    val v = to[it]
                    if (v == fa) return@each
                    val o = dfs(v, u)
                    one = o ma 1 mm one
                }
                ans = ans ma one
                return one
            }
            graph.dfs(0, -1)
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}