package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2023/11/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, a) = rd.ni() to rd.nl()
    val nums = rd.nal(n)
    val sum = nums.sum()
    repeat(n) {
        val num = nums[it]
        val (l, r) = n - 1L to sum - num
        val (st, ed) = a - r to a - l
        val (cl, cr) = maxOf(st, 1) to minOf(ed, num)
        val ans = num - maxOf(0, cr - cl + 1)
        wt.print("$ans ")
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}