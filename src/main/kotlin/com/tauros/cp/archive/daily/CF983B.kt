package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)

    val f = ar(n) { iar(n) }
    val max = ar(n) { iar(n) }
    for (i in 0 until n) {
        f[i][i] = nums[i]
        max[i][i] = nums[i]
    }
    for (len in 2 .. n) for (l in 0 .. n - len) {
        val r = l + len - 1
        f[l][r] = f[l][r - 1] xor f[l + 1][r]
        max[l][r] = maxOf(max[l][r - 1], max[l + 1][r], f[l][r])
    }

    val q = rd.ni()
    repeat(q) {
        val (l, r) = rd.ni() - 1 to rd.ni() - 1
        wt.println(max[l][r])
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}