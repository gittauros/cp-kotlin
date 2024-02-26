package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/2/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/711/C
    val (n, m, k) = rd.na(3)
    val color = rd.na(n)
    val cost = ar(n) { rd.na(m) }

    val dp = ar(2) { ar(k + 1) { lar(m + 1) { INF_LONG } } }
    val min = ar(k + 1) { iar(2) { -1 } }
    dp[0][0][0] = 0
    var cur = 0
    for (i in 1 .. n) {
        val pre = cur; cur = 1 - cur
        dp[cur].onEach { it.fill(INF_LONG) }
        min.onEach { it.fill(-1) }

        fun IntArray.updateMin(seg: int) {
            val pdp = dp[pre][seg]
            for (c in 0 .. m) {
                if (c != this[0] && (this[0] == -1 || pdp[c] < pdp[this[0]])) {
                    this[1] = this[0]
                    this[0] = c
                } else if (c != this[0] && (this[1] == -1 || pdp[c] < pdp[this[1]])) {
                    this[1] = c
                }
            }
        }
        for (seg in 0 .. k) min[seg].updateMin(seg)

        for (seg in 1 .. minOf(i, k)) for (c in 1 .. m) {
            if (color[i - 1] != 0 && color[i - 1] != c) continue
            val pay = if (color[i - 1] == c) 0 else cost[i - 1][c - 1]
            val diffMin = min[seg - 1]
            dp[cur][seg][c] = minOf(dp[cur][seg][c], dp[pre][seg][c] + pay,
                if (diffMin[0] != -1 && diffMin[0] != c) dp[pre][seg - 1][diffMin[0]] + pay
                else if (diffMin[1] != -1 && diffMin[0] != -1 && diffMin[0] == c) dp[pre][seg - 1][diffMin[1]] + pay
                else INF_LONG
            )
        }
    }

    val ans = dp[cur][k].min()
    wt.println(if (ans == INF_LONG) -1 else ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}