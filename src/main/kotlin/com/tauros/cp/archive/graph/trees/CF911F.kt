package com.tauros.cp.archive.graph.trees

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.graph.Graph
import com.tauros.cp.graph.hld
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.lao

/**
 * @author tauros
 * 2024/4/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/911/F
    // 先摘掉直径外的点，再摘直径
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    val deg = iar(n)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
        deg[u] += 1; deg[v] += 1
    }

    val (e0, e1) = run {
        var max = 0
        var (l, r) = -1 to -1
        fun Graph.dfs(u: int, fa: int = -1): IIP {
            var (len0, len1) = 0 to 0
            var (m0, m1) = u to u
            each(u) {
                val v = to[it]
                if (v == fa) return@each
                val (subLen, sub) = dfs(v, u)
                if (subLen + 1 > len0) {
                    len1 = len0; len0 = subLen + 1
                    m1 = m0; m0 = sub
                } else if (subLen + 1 > len1) {
                    len1 = subLen + 1; m1 = sub
                }
            }
            if (m0 != m1 && len0 + len1 > max) {
                max = len0 + len1; l = m0; r = m1
            }
            return len0 to m0
        }
        graph.dfs(0)
        l to r
    }

    val ends = (0 until n).filter { it != e0 && it != e1 && deg[it] == 1 }.toIntArray()
    var ans = 0L
    val ops = buildList {
        val hld = graph.hld(e0)
        fun len(a: int, b: int): long {
            val lca = hld.lca(a, b)
            val len = lao(hld.dep[a].toLong() - hld.dep[lca], hld.dep[b].toLong() - hld.dep[lca])
            return len.sum()
        }
        for (i in ends) {
            var iter = i
            while (iter != e0 && deg[iter] == 1) {
                val (len0, len1) = len(iter, e0) to len(iter, e1)
                if (len0 >= len1) {
                    add(iao(iter, e0, iter)); ans += len0
                } else {
                    add(iao(iter, e1, iter)); ans += len1
                }
                val next = hld.parent[iter]
                deg[next] -= 1; iter = next
            }
        }
        var iter = e1
        while (iter != e0) {
            val len = hld.dep[iter].toLong() - hld.dep[e0]
            add(iao(iter, e0, iter)); ans += len
            iter = hld.parent[iter]
        }
    }
    wt.println(ans)
    for ((u, v, r) in ops) {
        wt.println("${u + 1} ${v + 1} ${r + 1}")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}