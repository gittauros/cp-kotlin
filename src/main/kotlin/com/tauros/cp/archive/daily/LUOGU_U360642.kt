@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.iter
import com.tauros.cp.common.le
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
    val cap = nums.reduce(::maxOf).takeHighestOneBit()
    withMod(1e9.toInt() + 7) {
        var ans = 0.toMInt()
        for (b in 1 iter { it shl 1 } le cap) {
            val cnt = nums.count { it and b != 0 }
            val restPow = 2.toMInt().pow(n - cnt)
            val res = restPow * if (cnt == 0) 0.toMInt() else 2.toMInt().pow(cnt - 1)
            ans += res * b
        }
        wt.println(ans)
    }
}