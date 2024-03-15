package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.graph.Graph
import com.tauros.cp.iao
import com.tauros.cp.lar
import com.tauros.cp.mlo
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2024/3/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1076/E
    // 差分再差分，首先是子树dfn差分
    // 然后在子树内对dep差分，对每个点查询它dep的值
    // 可以dfs时在线对dep差分，时间比全部离线更好
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }
    val ops = ar(n + 1) { mlo<IntArray>() }
    val m = rd.ni()
    repeat(m) {
        val (v, d, x) = iao(rd.ni() - 1, rd.ni(), rd.ni())
        ops[v].add(iao(d, x))
    }

    fun LongArray.update(pos: int, add: int) = this.bitUpdateWithIndex(pos) { this[it] += add.toLong() }
    fun LongArray.query(pos: int) = this.bitQuery(pos, 0L, long::plus)

    val (ans, bit) = lar(n) to lar(n + 2)
    fun Graph.dfs(u: int = 0, fa: int = -1, dep: int = 1) {
        for ((d, x) in ops[u]) {
            bit.update(dep, x)
            bit.update(dep + d + 1, -x)
        }
        ans[u] = bit.query(dep)
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            dfs(v, u, dep + 1)
        }
        for ((d, x) in ops[u]) {
            bit.update(dep, -x)
            bit.update(dep + d + 1, x)
        }
    }
    graph.dfs()

    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}