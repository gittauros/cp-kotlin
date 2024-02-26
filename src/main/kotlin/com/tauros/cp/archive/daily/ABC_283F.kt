@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.swap
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import kotlin.math.abs
import kotlin.random.Random

/**
 * @author tauros
 */
private val bufCap = 128
//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/F/in/03_small_52.txt"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private class Seg(val cl: Int, val cr: Int) {
    val mid = cl + cr shr 1
    var l: Seg? = null
    var r: Seg? = null
    var clear = false
    var max = -INF

    fun pushDown() {
        if (cl == cr) return
        if (l == null) l = Seg(cl, mid)
        if (r == null) r = Seg(mid + 1, cr)
        if (clear) {
            l!!.clear = true
            l!!.max = -INF
            r!!.clear = true
            r!!.max = -INF
            clear = false
        }
    }

    fun pushUp() {
        max = maxOf(l!!.max, r!!.max)
    }

    fun update(pos: Int, value: Int) {
        if (pos == cl && cl == cr) {
            this.max = maxOf(this.max, value)
            return
        }
        pushDown()
        if (pos <= mid) l?.update(pos, value)
        else r?.update(pos, value)
        pushUp()
    }

    fun query(st: Int, ed: Int): Int {
        if (cl > ed || cr < st) return -INF
        if (cl >= st && cr <= ed) return max
        pushDown()
        return maxOf(l!!.query(st, ed), r!!.query(st, ed))
    }
}

private fun solve() {
    val n = rd.ni()
    val nums = Array(n) {
        intArrayOf(it + 1, rd.ni())
    }
    val seg = Seg(1, 2 * n)
    val ans = IntArray(n) { INF }
    fun discrete(num: Int) = if (num < 0) num + n + 1 else num + n
    for (x in intArrayOf(1, -1)) for (y in intArrayOf(1, -1)) {
        for (i in 0 until n) {
            nums[i][0] = abs(nums[i][0]) * x
            nums[i][1] = abs(nums[i][1]) * y
        }
        nums.sortBy { it[0] }
        seg.clear = true
        seg.max = -INF
        for ((i, p) in nums) {
            val dp = discrete(p)
            val max = seg.query(dp, 2 * n)
            val ip = i - p
            if (max != -INF) {
                val idx = abs(i) - 1
                ans[idx] = minOf(ans[idx], ip - max)
            }
            seg.update(dp, ip)
        }
    }
    wt.println(buildString {
        for (v in ans) {
            append(v)
            append(' ')
        }
    }.trimEnd(' '))
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}