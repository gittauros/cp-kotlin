package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar

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
    // https://codeforces.com/problemset/problem/622/E
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    val deg = iar(n)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v); graph.addEdge(v, u)
        deg[v] += 1; deg[u] += 1
    }

    var cnt = 0
    for (i in 0 until n) if (deg[i] == 1) cnt += 1
    if (deg[0] == 1) cnt -= 1

    var ans = 0
    val diff = iar(cnt)
    var idx = 0
    fun Graph.dfs(u: int, fa: int): IIP {
        var (st, ed) = INF to -1
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            val (cl, cr) = dfs(v, u)
            if (u != 0) {
                diff[cl] += 1
                if (cr + 1 < cnt) diff[cr + 1] -= 1
            } else {
                for (i in cl + 1 .. cr + 1) if (i < cnt) diff[i] += diff[i - 1]
                diff.sort(cl, cr + 1)
                var res = -1
                for (i in cl .. cr) {
                    if (res >= diff[i]) res += 1
                    else res = diff[i]
                }
                ans = maxOf(res + 1, ans)
            }
            st = minOf(cl, st)
            ed = maxOf(cr, ed)
        }
        if (ed == -1) {
            st = idx
            ed = idx
            idx += 1
        }
        return st to ed
    }
    graph.dfs(0, -1)

    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}