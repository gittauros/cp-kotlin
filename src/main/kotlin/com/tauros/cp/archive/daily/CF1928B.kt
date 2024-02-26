package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2024/2/11
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

        val sorted =  nums.sorted().distinct().toIntArray()
        var ans = 0
        var l = 0
        for (r in sorted.indices) {
            while (sorted[r] - sorted[l] >= n) l += 1
            ans = maxOf(ans, r - l + 1)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}