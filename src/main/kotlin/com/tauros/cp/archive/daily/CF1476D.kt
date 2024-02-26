package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.char
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/20
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
        val str = rd.ns(n)

        val suf = ar(2) { iar(n + 1) }
        fun c(c: char) = if (c == 'L') 0 else 1
        for (i in n - 1 downTo 0) {
            val cur = c(str[i])
            suf[cur][i] = suf[1 - cur][i + 1] + 1
            suf[1 - cur][i] = 0
        }

        wt.print("${suf[1][0] + 1} ")
        val pre = iar(2)
        for (i in 1 .. n) {
            val cur = c(str[i - 1])
            pre[cur] = pre[1 - cur] + 1
            pre[1 - cur] = 0

            val res = pre[0] + suf[1][i] + 1
            wt.print("$res ")
        }
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}