package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.long
import kotlin.math.abs

/**
 * @author tauros
 * 2024/3/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1898/D
    // 以前做过，这次还是WA2，好在又反应过来了
    // 求的是 |a[i] - b[i]| + |a[j] - b[j]| - (|a[i] - b[j]| + |a[j] - b[i]|) 的最大值
    // 设 a[i] b[i] a[j] b[j] 排好序后从小到大分别是 x y p q
    // 那么求的其实是这四个数两两距离和最大值，取到最大值时是 p - y + q - x
    // 相当于是有两个区间 [l1, r1] [l2, r2]，取到最大值时是这两个区间有交集
    // 那么相对于原值有增量的情况就是，原始俩区间没有交集，交换后有交集了，在这个情况下找右端点最靠左或左端点最靠右的区间即可
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val a = rd.na(n)
        val b = rd.na(n)

        val sum = (0 until n).map { abs(a[it] - b[it]) }.fold(0L, long::plus)
        var ans = sum
        var (rendMin, lendMax) = 0x3f3f3f3f to -0x3f3f3f3f
        for (i in 0 until n) {
            val (l, r) = minOf(a[i], b[i]) to maxOf(a[i], b[i])
            if (rendMin < l) {
                val inc = l.toLong() - rendMin shl 1
                ans = maxOf(ans, sum + inc)
            }
            if (lendMax > r) {
                val inc = lendMax.toLong() - r shl 1
                ans = maxOf(ans, sum + inc)
            }
            rendMin = minOf(rendMin, r)
            lendMax = maxOf(lendMax, l)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}