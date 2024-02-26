package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

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
    val cases = rd.ni()
    repeat(cases) {
        val s = rd.ns()
        val n = s.length
        var iter = n - 1
        while (s[iter] == '0') iter--
        val ans = s.substring(0, iter)
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}