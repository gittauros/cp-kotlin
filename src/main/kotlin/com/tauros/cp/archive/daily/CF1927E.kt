package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iao
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
        val (n, k) = rd.ni() to rd.ni()

        val ans = iar(n) { -1 }
        var cur = 1
        for (i in 0 until k) {
            if (i % 2 == 0) {
                for (j in i until n step k) ans[j] = cur++
            } else {
                for (j in (n - 1 - i) / k * k + i downTo i step k) ans[j] = cur++
            }
        }
        for (i in 0 until n) if (ans[i] == -1) ans[i] = cur++

        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}