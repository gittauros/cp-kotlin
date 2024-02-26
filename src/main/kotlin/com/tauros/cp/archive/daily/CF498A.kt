package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2024/1/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (x1, y1) = rd.nl() to rd.nl()
    val (x2, y2) = rd.nl() to rd.nl()
    val n = rd.ni()
    var ans = 0
    for (i in 1 .. n) {
        val (a, b, c) = rd.nal(3)
        val x = a * x1 + b * y1 + c
        val y = a * x2 + b * y2 + c
        if (x > 0 && y < 0 || x < 0 && y > 0) {
            ans += 1
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}