package com.tauros.cp.archive.greedy

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.runningFold

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
    // https://codeforces.com/contest/1923/problem/D
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val slimes = rd.na(n)

        val ans = iar(n) { INF }
        for (i in 0 until n)
            if (i > 0 && slimes[i - 1] > slimes[i] || i < n - 1 && slimes[i + 1] > slimes[i]) ans[i] = 1

        val sum = slimes.runningFold(0L, long::plus)
        var (s1, s2) = -1 to -1
        for (i in 1 .. n) {
            val slime = slimes[i - 1]
            if (i > 1 && s2 != -1) {
                val ed = findFirst(0, s2) {
                    val range = sum[i - 1] - sum[it]
                    range <= slime
                } - 1
                if (ed >= 0) ans[i - 1] = minOf(ans[i - 1], i - 1 - ed)
            }
            if (s1 != -1 && slime != slimes[s1 - 1]) s2 = s1
            s1 = i
        }
        s1 = -1; s2 = -1
        for (i in n downTo 1) {
            val slime = slimes[i - 1]
            if (i < n && s2 != -1) {
                val ed = findFirst(s2, n + 1) {
                    val range = sum[it] - sum[i]
                    range > slime
                }
                if (ed <= n) ans[i - 1] = minOf(ans[i - 1], ed - i)
            }
            if (s1 != -1 && slime != slimes[s1 - 1]) s2 = s1
            s1 = i
        }

        for (res in ans) wt.print("${if (res == INF) -1 else res} ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}