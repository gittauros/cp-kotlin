package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod
import com.tauros.cp.math.ModComb

/**
 * @author tauros
 * 2023/12/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    withMod(998244353) {
        val (n, k) = rd.na(2)
        val nums = rd.ns(n).map { it - '0' }
        val sums = nums.runningFold(0, Int::plus)
        val comb = ModComb(n)

        var ans = 1.toMInt()
        var (pre, r) = -1 to -1
        for (l in 0 until n) {
            while (r + 1 < n && (r < l - 1 || sums[r + 2] - sums[l] <= k)) r++
            if (sums[r + 1] - sums[l] == k) {
                var res = comb.c(r - l + 1, k) - 1
                if (pre >= l) {
                    val inPreRange = sums[pre + 1] - sums[l]
                    val duplicate = comb.c(pre - l + 1, inPreRange) - 1
                    res -= duplicate
                }
                ans += res
                pre = r
            }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}