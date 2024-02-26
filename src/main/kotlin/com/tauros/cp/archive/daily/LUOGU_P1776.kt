@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily


import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ao
import com.tauros.cp.ar
import com.tauros.cp.iao
import com.tauros.cp.iar
import java.io.FileInputStream

/**
 * @author tauros
 * 2023/10/18
 */
private val bufCap = 128

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, cap) = iao(rd.ni(), rd.ni())
    val (v, w, cnt) = ao(iar(n), iar(n), iar(n))
    var res = 0
    for (i in 0 until n) {
        v[i] = rd.ni()
        w[i] = rd.ni()
        cnt[i] = rd.ni()
        if (w[i] == 0) res += v[i] * cnt[i]
    }
    val dp = iar(cap + 1)
    for (i in 0 until n) if (w[i] != 0) {
        val q = ar(2) { iar(cap / w[i] + 1) }
        for (rest in 0 until w[i]) {
            var (head, tail) = iao(0, 0)
            for ((c, j) in (rest .. cap step w[i]).withIndex()) {
                val tag = dp[j] - c * v[i]
                while (head < tail && q[1][head] < c - cnt[i]) head++
                while (head < tail && q[0][tail - 1] < tag) tail--
                q[0][tail] = tag
                q[1][tail] = c
                tail++
                dp[j] = maxOf(dp[j], q[0][head] + c * v[i])
            }
        }
    }
    val ans = res + dp[cap]
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}