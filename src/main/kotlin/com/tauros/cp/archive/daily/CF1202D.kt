package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst

/**
 * @author tauros
 * 2023/12/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.nl()
        val idx = findFirst(n) { it * (it + 1) / 2 > n }.toInt() - 1
        val sqr = idx * (idx + 1L) / 2
        val rest = (n - sqr).toInt()
        wt.println(buildString {
            append("133")
            repeat(rest) { append('7') }
            if (idx > 0) {
                repeat(idx - 1) { append('3') }
                append('7')
            }
        })
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}