package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2023/12/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n).sortedArray()
    var ans = 0
    for (i in 0 until n - 1 step 2) ans += nums[i + 1] - nums[i]
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}