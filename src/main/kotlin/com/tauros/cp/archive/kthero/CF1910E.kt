package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2023/12/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = ar(2) { rd.na(n) }
        val sums = ar(2) { lar(n + 1) }

        var ans = 0L
        val max = lar(2)
        val minSum = lar(2)
        var pre = -INF_LONG
        for (i in 0 until n) {
            val (big, small) = maxOf(nums[0][i], nums[1][i]) to minOf(nums[0][i], nums[1][i])
            nums[0][i] = small
            nums[1][i] = big
            sums[0][i + 1] = sums[0][i] + small
            sums[1][i + 1] = sums[1][i] + big

            if (i >= 1) {
                val res = sums[1][i + 1] + pre
                ans = maxOf(ans, res)
            }
            pre = maxOf(pre, max[1] - sums[1][i + 1])

            max[0] = maxOf(max[0], sums[0][i + 1] - minSum[0])
            max[1] = maxOf(max[1], sums[1][i + 1] - minSum[1])
            ans = maxOf(ans, max[0] + max[1])

            minSum[0] = minOf(minSum[0], sums[0][i + 1])
            minSum[1] = minOf(minSum[1], sums[1][i + 1])
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}