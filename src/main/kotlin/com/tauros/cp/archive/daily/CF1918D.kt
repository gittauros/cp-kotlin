package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.lao
import com.tauros.cp.lar
import com.tauros.cp.runningFold

/**
 * @author tauros
 * 2024/2/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1918/problem/D
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        val sum = nums.runningFold(0L, long::plus).toLongArray()
        val q = ar(n + 1) { lar(2) }
        val ans = findFirst(nums.max().toLong(), sum.last()) { target ->
            var (head, tail) = 0 to 0
            var dp = 0L
            q[tail++] = lao(0, 0)
            for (i in 1 .. n + 1) {
                while (head < tail && sum[i - 1] - sum[q[head][1].toInt()] > target) head++
                dp = q[head][0] + sum[i - 1]
                if (i == n + 1) break
                while (head < tail && dp - sum[i] > q[tail - 1][0]) tail--
                q[tail][0] = dp - sum[i]
                q[tail++][1] = i.toLong()
            }
            sum.last() - dp <= target
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}