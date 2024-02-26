package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod

/**
 * @author tauros
 * 2023/12/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    withMod(1e9.toInt() + 7) {
        val (n, m) = rd.ni() to rd.ni()
        val res = 2.toMInt().pow(m) - 1
        val ans = res.pow(n)
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}