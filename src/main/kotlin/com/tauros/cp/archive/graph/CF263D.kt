package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/16
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/263/D
    // 每个点度数至少为k，推一下，至少有长为k的链，第k+1个点至少除了第2到第k的k-1个点之外还连了1个点
    // 这个点可能不是1号点，但是没关系，继续搜，记录搜过程中的深度，直到找到深度差距至少为k的点
    // dfs过程中记录下前驱，就能还原整个链了
    val (n, m, k) = rd.na(3)
    val graph = Graph(n, m * 2)
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
    }

    val (dep, pre) = iar(n) { -1 } to iar(n) { -1 }
    var (st, ed) = -1 to -1
    fun Graph.dfs(u: int, fa: int = -1, d: int = 0) {
        pre[u] = fa; dep[u] = d
        each(u) {
            val v = to[it]
            if (v == fa || dep[v] != -1 && dep[v] in d - (k - 1) .. d) return@each
            if (dep[v] in 0 .. d - k) {
                st = v; ed = u
            }
            if (dep[v] == -1) dfs(v, u, d + 1)
        }
    }
    for (i in 0 until n) if (dep[i] == -1) {
        graph.dfs(i)
    }
    val ans = buildList {
        var iter = ed
        while (iter != st) {
            add(iter)
            iter = pre[iter]
        }
        add(st)
    }
    wt.println(ans.size)
    for (res in ans) wt.print("${res + 1} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}