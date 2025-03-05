package com.tauros.cp.archive.young

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.gcd
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2025/3/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (x, y) = rd.ni() to rd.ni()
        val (low, high) = minOf(x, y) to maxOf(x, y)
        if (high == 1 || low == high && low == 2) {
            wt.println(-1)
        } else {
            for (num in high - 1 downTo 2) {
                if (gcd(num, high) == 1 && gcd(num, low) == 1) {
                    wt.println(num)
                    return@repeat
                }
            }
            wt.println(-1)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}