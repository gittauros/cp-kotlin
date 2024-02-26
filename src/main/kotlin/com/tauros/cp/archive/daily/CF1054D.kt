package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.mmo
import com.tauros.cp.runningFold
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2023/12/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    val nums = rd.na(n)
    val mask = (1 shl k) - 1
    val sums = nums.runningFold(0, Int::xor)
    val cnt = mmo<int, int>().default { 0 }
    for (i in 0 .. n) cnt[minOf(sums[i], sums[i] xor mask)] += 1

    var ans = (1L + n) * n / 2
    for ((_, count) in cnt) {
        val half = count / 2
        val other = count - half
        ans -= half * (half - 1L) / 2
        ans -= other * (other - 1L) / 2
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}