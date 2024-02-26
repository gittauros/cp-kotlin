package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.cao
import com.tauros.cp.car
import com.tauros.cp.common.MInt
import com.tauros.cp.common.fill
import com.tauros.cp.common.ma
import com.tauros.cp.common.ms
import com.tauros.cp.common.sum
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.miar
import kotlin.math.abs
import kotlin.random.Random

/**
 * @author tauros
 * 2023/11/30
 */
private val bufCap = 8
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    withMod(1000000007) {
        val (n, m, k) = rd.na(3)
        val s = rd.ns(n)
        val t = rd.ns(m)
        val lcp = ar(m + 1) { iar(n + 1) }
        for (i in m - 1 downTo 0) {
            for (j in n - 1 downTo 0) {
                lcp[i][j] = if (t[i] == s[j]) lcp[i + 1][j + 1] ma 1 else 0
            }
        }

        val dp = ar(2) { ar(m + 2) { iar(n + 2) } }
        var cur = 0
        dp[cur][0][0] = 1
        repeat(k) {
            val pre = cur
            cur = 1 - cur
            dp[cur].onEach { it.fill(0) }
            for (i in it until m) {
                var sum = 0
                for (j in it until n) {
                    sum = sum ma dp[pre][i][j]
                    if (lcp[i][j] > 0) {
                        val len = lcp[i][j]
                        dp[cur][i + 1][j + 1] = dp[cur][i + 1][j + 1] ma sum
                        dp[cur][i + 1 + len][j + 1 + len] = dp[cur][i + 1 + len][j + 1 + len] ms sum
                    }
                }
            }
            for (i in 1 .. m + 1) for (j in 1 .. n + 1) dp[cur][i][j] = dp[cur][i][j] ma dp[cur][i - 1][j - 1]
        }
        val ans = dp[cur][m].reduce(Int::ma)
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}