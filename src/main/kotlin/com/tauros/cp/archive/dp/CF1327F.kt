package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.ma
import com.tauros.cp.common.mm
import com.tauros.cp.common.ms
import com.tauros.cp.common.withMod
import com.tauros.cp.iar

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
    // https://codeforces.com/problemset/problem/1327/F
    val (n, cap, m) = rd.na(3)
    val ranges = ar(m) { rd.na(3) }

    withMod(998244353) {
        val (one, zero) = iar(n + 2) to iar(n + 2)
        val (dp, sum) = iar(n + 2) to iar(n + 2)
        var ans = 1

        for (b in 0 until cap) {
            one.fill(0)
            zero.fill(0)
            for ((l, r, x) in ranges) if (1 shl b and x != 0) {
                // deal one
                one[l] += 1
                one[r + 1] -= 1
            } else {
                // deal zero
                zero[r + 1] = maxOf(l, zero[r + 1])
            }
            for (i in 1 .. n + 1) one[i] += one[i - 1]

            dp.fill(0)
            sum.fill(0)
            dp[0] = 1
            sum[0] = 1
            var st = 0
            for (i in 1 .. n + 1) {
                st = maxOf(st, zero[i])
                if (one[i] == 0) {
                    dp[i] = sum[i - 1] ms (if (st == 0) 0 else sum[st - 1])
                }
                sum[i] = sum[i - 1] ma dp[i]
            }
            ans = ans mm dp[n + 1]
        }

        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}