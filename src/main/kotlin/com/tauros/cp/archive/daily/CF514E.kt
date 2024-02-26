@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.withMod
import com.tauros.cp.math.Matrix

/**
 * @author tauros
 */
private val bufCap = 128
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() = withMod(1e9.toInt() + 7) {
    val (n, x) = rd.ni() to rd.ni()
    val nums = rd.na(n)
    val max = nums.reduce(::maxOf) + 1
    val cnt = IntArray(max)
    for (num in nums) cnt[num]++
    val top = IntArray(max)
    for (d in 1 until max) {
        top[d] += cnt[d]
    }
    val mat = Matrix(max, elements = IntArray(max * max) {
        val (r, c) = it / max to it % max
        if (r <= 1) {
            top[c] + if (r == 0 && c == 0) 1 else 0
        } else if (c == r - 1) 1 else 0
    })
    val init = Matrix(max, 1, IntArray(max) { if (it <= 1) 1 else 0 })
    val res = mat.pow(x)
    val ans = res * init
    wt.println(ans[0][0])
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}