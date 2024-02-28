package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.exgcd
import com.tauros.cp.common.gcd
import kotlin.math.abs

/**
 * @author tauros
 * 2024/2/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (x, y) = rd.nl() to rd.nl()
    // b * x - a * y = -2 or b * x - a * y = 2
    if (x == 0L) {
        if (abs(y) !in 1 .. 2) wt.println(-1)
        else wt.println("${2 / y} 0")
    } else if (y == 0L) {
        if (abs(x) !in 1 .. 2) wt.println(-1)
        else wt.println("0 ${2 / x}")
    } else {
        val (gcd, b, a) = exgcd(x, -y)
        if (abs(gcd) !in 1 .. 2) wt.println(-1)
        else {
            val o = 2 / gcd
            wt.println("${a * o} ${b * o}")
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}