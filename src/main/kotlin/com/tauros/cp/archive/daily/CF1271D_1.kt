package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2023/12/8
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, k) = rd.na(3)
    val props = ar(n) { rd.na(3) }

    val to = iar(n) { it }
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        to[v] = maxOf(to[v], u)
    }
    val graph = ar(n) { mlo<int>() }
    for (v in 0 until n) if (to[v] != -1) graph[to[v]].add(v)

    val cap = 5000 + k
    val dp = ar(2) { iar(cap + 1) { -INF } }
    var cur = 0
    dp[cur][k] = 0
    for (i in 0 until n) {
        val pre = cur
        cur = 1 - cur
        dp[cur].fill(-INF)
        for (j in props[i][0] .. cap - props[i][1]) dp[cur][j + props[i][1]] = dp[pre][j]

        for (score in graph[i].map { props[it][2] }) {
            for (j in 0 until cap) {
                dp[cur][j] = maxOf(dp[cur][j], dp[cur][j + 1] + score)
            }
        }
    }

    val ans = dp[cur].maxOrNull() ?: -1
    wt.println(if (ans < 0) -1 else ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}