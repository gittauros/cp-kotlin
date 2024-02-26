@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.MIntArray
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod

/**
 * @author tauros
 */
private val bufCap = 128
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

fun main(args: Array<String>) {
    solve()
    wt.flush()
}

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)
    withMod(2) {
        val cnt = MIntArray(nums.sum() + 1)
        cnt[0] = 1.toMInt()
        for (num in nums) {
            for (sum in cnt.size - 1 downTo num) {
                cnt[sum] += cnt[sum - num]
            }
        }
        var ans = 0
        for (sum in 1 until cnt.size) {
            if (cnt[sum].toInt() == 1) ans = ans xor sum
        }
        wt.println(ans)
    }
}
