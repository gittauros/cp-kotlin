package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ao
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.graph.Graph
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/1/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1646/D
    // 关键性质：除了2个点的树以外，好点和坏点一定是间隔着来的
    val n = rd.ni()
    val graph = Graph(n, (n - 1) * 2)
    repeat(n - 1) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph.addEdge(u, v)
        graph.addEdge(v, u)
    }
    if (n == 2) {
        wt.println("2 2")
        wt.println("1 1")
        return
    }

    data class Info(var cnt: int, var sum: long, var vtx: int)
    val dp = ar(n) { ar(2) { Info(-1, 0, 0) } }
    fun Graph.dfs(u: int, fa: int): Array<Info> {
        var (fCnt, fSum) = 0 to 0L
        var (tCnt, tSub) = 0 to 0
        var tSum = 0L
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            val (f, t) = dfs(v, u)
            if (t.cnt > f.cnt || t.cnt == f.cnt && t.sum + 1 < f.sum) {
                fCnt += t.cnt
                fSum += t.sum + 1
            } else {
                fCnt += f.cnt
                fSum += f.sum
            }
            tSum += f.sum
            tCnt += f.cnt
            tSub += f.vtx
        }
        dp[u][0].cnt = fCnt
        dp[u][0].sum = fSum + 1
        dp[u][0].vtx = 1
        dp[u][1].cnt = tCnt + 1
        dp[u][1].sum = tSum + tSub
        dp[u][1].vtx = tSub
        return dp[u]
    }

    val ans = iar(n)
    fun Graph.fill(u: int, fa: int, add: int, state: int) {
        ans[u] = dp[u][state].vtx + add
        each(u) {
            val v = to[it]
            if (v == fa) return@each
            val (f, t) = dp[v]
            if (state == 0 && (t.cnt > f.cnt || t.cnt == f.cnt && t.sum + 1 < f.sum)) {
                fill(v, u, ans[u], 1)
            } else {
                fill(v, u, 0, 0)
            }
        }
    }

    val (f, t) = graph.dfs(0, -1)
    if (t.cnt > f.cnt || t.cnt == f.cnt && t.sum < f.sum) {
        wt.println("${t.cnt} ${t.sum}")
        graph.fill(0, -1, 0, 1)
    } else {
        wt.println("${f.cnt} ${f.sum}")
        graph.fill(0, -1, 0, 0)
    }
    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}