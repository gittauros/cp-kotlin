package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.mm
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod
import com.tauros.cp.iar
import com.tauros.cp.math.Prime
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2024/3/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/895/C
    val cap = 70
    val prime = Prime(cap)
    val pIdx = prime.zip(0 until prime.size).toMap().default { -1 }

    val n = rd.ni()
    val a = rd.na(n)

    val cnt = iar(cap + 1)
    for (num in a) cnt[num] += 1
    val numMask = iar(cap + 1) { num ->
        var (iter, mask) = num to 0
        while (iter > 1) {
            var (p, div) = 0 to prime.min(iter)
            while (iter % div == 0) {
                iter /= div; p = p xor 1
            }
            mask = p shl pIdx[div] or mask
        }
        mask
    }
    withMod(1e9.toInt() + 7) {
        val maxState = 1 shl prime.size
        val dp = ar(2) { iar(maxState) }
        dp[0][0] = 1
        var cur = 0
        for (num in 1 .. cap) if (cnt[num] > 0) {
            val pre = cur; cur = 1 - cur
            dp[cur].fill(0)

            val half = (2.toMInt().pow(cnt[num]) / 2).toInt()
            val mask = numMask[num]
            for (state in 0 until maxState) {
                val inc = dp[pre][state] mm half
                dp[cur][state xor mask] += inc
                if (dp[cur][state xor mask] >= globalMod) dp[cur][state xor mask] -= globalMod
                dp[cur][state] += inc
                if (dp[cur][state] >= globalMod) dp[cur][state] -= globalMod
            }
        }
        val ans = dp[cur][0] - 1
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}