package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2024/2/23
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
        val nums = rd.na(n)

        val sum = nums.sum()
        val first = (0 until n).first { nums[it] == 1 }
        val last = (0 until n).last { nums[it] == 1 }
        val len = last - first + 1
        val ans = len - sum
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}