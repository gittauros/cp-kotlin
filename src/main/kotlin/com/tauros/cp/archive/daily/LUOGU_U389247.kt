package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod
import com.tauros.cp.iar
import com.tauros.cp.miar
import com.tauros.cp.mso

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
    val n = rd.ni()
    val nums = rd.na(n)
    withMod(1e9.toInt() + 7) {
        val end = iar(n + 1)
        val set = mso<int>()
        var l = n
        for (r in n downTo 1) {
            while (l >= 1 && nums[l - 1] !in set) set.add(nums[--l])
            end[r] = l
            set.remove(nums[r - 1])
        }

        val sums = miar(n + 1)
        val dp = miar(n + 1)
        sums[0] = 1.toMInt()
        dp[0] = 1.toMInt()
        for (i in 1 .. n) {
            dp[i] = sums[i - 1]
            val left = end[i]
            if (left > 0) dp[i] -= sums[left - 1]
            sums[i] = sums[i - 1] + dp[i]
        }
        wt.println(dp[n])
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}