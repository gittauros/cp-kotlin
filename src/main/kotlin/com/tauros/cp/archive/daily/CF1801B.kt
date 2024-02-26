package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.avls
import com.tauros.cp.iar
import kotlin.math.abs

/**
 * @author tauros
 * 2023/11/21
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
        val nums = ar(n) { rd.na(2) }
        nums.sortWith { a, b -> if (a[0] == b[0]) -a[1].compareTo(b[1]) else a[0].compareTo(b[0]) }

        val suf = iar(n + 1)
        for (i in n - 1 downTo 0) suf[i] = maxOf(suf[i + 1], nums[i][1])

        val leftPart = avls<Int> { a, b -> a.compareTo(b) }
        var ans = 1e9.toInt()
        for (i in 0 until n) {
            val (a, b) = nums[i]
            val bound = suf[i + 1]
            if (i < n - 1) {
                ans = minOf(ans, abs(bound - a))
            }
            if (a > bound) {
                val low = leftPart.floor(a)
                if (low != null) ans = minOf(ans, a - low)
                val high = leftPart.ceiling(a)
                if (high != null) ans = minOf(ans, high - a)
            } else {
                val num = leftPart.ceiling(bound)
                if (num != null) ans = minOf(ans, num - a)
            }
            leftPart.add(b)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}