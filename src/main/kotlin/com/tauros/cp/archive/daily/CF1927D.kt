package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/7
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

        var (right1, right2) = -1 to -1
        val left = iar(n) { -1 }
        for ((i, num) in nums.withIndex()) {
            left[i] = if (right1 != -1 && nums[right1] != num) right1 else right2
            if (right1 == -1 || nums[right1] != num) right2 = right1
            right1 = i
        }

        val q = rd.ni()
        repeat(q) {
            val (l, r) = rd.ni() - 1 to rd.ni() - 1
            val (i, j) = if (left[r] < l) -1 to -1 else left[r] + 1 to r + 1
            wt.println("$i $j")
        }
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}