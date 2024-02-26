package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val str = rd.ns()
    val pre = iar(26) { -1 }
    val period = iar(26)
    for ((i, c) in str.withIndex()) {
        val idx = c - 'a'
        val len = i - pre[idx]
        period[idx] = maxOf(period[idx], len)
        pre[idx] = i
    }
    for (i in 0 until 26) period[i] = maxOf(period[i], str.length - pre[i])
    val ans = period.minOrNull() ?: str.length
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}