package com.tauros.cp.archive.virtualtree

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.MInt
import com.tauros.cp.common.int
import com.tauros.cp.common.withMod
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.vtBuilder

/**
 * @author tauros
 * 2024/2/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://atcoder.jp/contests/abc340/tasks/abc340_g
    // https://atcoder.jp/contests/abc340/editorial/9256 虚树教程
    // https://www.luogu.com.cn/blog/298051/abc340g-ti-xie dp状态方程
    // 好好练练怎么统计子树个数吧
    val n = rd.ni()
    val vtx = rd.na(n).map { it - 1 }.toIntArray()
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    fun Graph.out() = buildString {
        each {
            val (u, v) = from[it] to to[it]
            append("${u + 1} ${v + 1}\n")
        }
    }

    val colorMap = (0 until n).groupBy { vtx[it] }
    withMod(998244353) {
        val vt = graph.vtBuilder(0, needFrom = true)
        var ans = MInt.ZERO
        for ((c, nodes) in colorMap) {
            val tree = vt.buildByKeyNodes(nodes)
            fun Graph.dfs(u: int): MInt {
                var res = MInt.ONE
                var sum = MInt.ZERO
                each(u) {
                    val v = to[it]
                    val sub = dfs(v)
                    res *= (sub + 1)
                    sum += sub
                }
                res -= if (vtx[u] != c) 1 else 0
                val dp = res - if (vtx[u] != c) sum else MInt.ZERO
                ans += dp
                return res
            }
            tree.dfs(0)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}