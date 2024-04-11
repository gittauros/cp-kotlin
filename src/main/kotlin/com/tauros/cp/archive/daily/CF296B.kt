package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.ma
import com.tauros.cp.common.mm
import com.tauros.cp.common.withMod
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/296/B
    val n = rd.ni()
    val s = rd.ns(n)
    val t = rd.ns(n)

    val cap = 10
    withMod(1e9.toInt() + 7) {
        val dp = ar(2) { iar(4) }
        dp[0][0] = 1
        var cur = 0
        for (i in 0 until n) {
            val pre = cur; cur = 1 - cur
            dp[cur].fill(0)

            val (sc, tc) = s[i] to t[i]
            for (from in 0 until 4) {
                if (sc == '?' && tc == '?') {
                    val lt = cap * (cap - 1) / 2
                    val gt = cap * (cap - 1) / 2
                    val eq = cap
                    dp[cur][from or 1] = dp[pre][from] mm lt ma dp[cur][from or 1]
                    dp[cur][from or 2] = dp[pre][from] mm gt ma dp[cur][from or 2]
                    dp[cur][from] = eq mm dp[pre][from] ma dp[cur][from]
                } else if (sc == '?') {
                    val lt = tc - '0'
                    val gt = '9' - tc
                    val eq = 1
                    dp[cur][from or 1] = dp[pre][from] mm lt ma dp[cur][from or 1]
                    dp[cur][from or 2] = dp[pre][from] mm gt ma dp[cur][from or 2]
                    dp[cur][from] = eq mm dp[pre][from] ma dp[cur][from]
                } else if (tc == '?') {
                    val lt = '9' - sc
                    val gt = sc - '0'
                    val eq = 1
                    dp[cur][from or 1] = dp[pre][from] mm lt ma dp[cur][from or 1]
                    dp[cur][from or 2] = dp[pre][from] mm gt ma dp[cur][from or 2]
                    dp[cur][from] = eq mm dp[pre][from] ma dp[cur][from]
                } else {
                    if (sc == tc) {
                        dp[cur][from] = dp[pre][from] ma dp[cur][from]
                    } else if (sc < tc) {
                        dp[cur][from or 1] = dp[pre][from] ma dp[cur][from or 1]
                    } else {
                        dp[cur][from or 2] = dp[pre][from] ma dp[cur][from or 2]
                    }
                }
            }
        }
        val ans = dp[cur][3]
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}