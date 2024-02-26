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
        val (a, b) = rd.na(2)

        val (w, h) = minOf(a, b) to maxOf(a, b)
        var ans = false
        if (w % 2 == 0) {
            val (a1, b1) = w / 2 to h * 2
            val (w1, h1) = minOf(a1, b1) to maxOf(a1, b1)
            ans = w1 != w || h1 != h
        }
        if (h % 2 == 0) {
            val (a1, b1) = w * 2 to h / 2
            val (w1, h1) = minOf(a1, b1) to maxOf(a1, b1)
            ans = ans or (w1 != w || h1 != h)
        }
        wt.println(if (ans) "Yes" else "No")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}