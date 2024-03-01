package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2024/2/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        if (n == 1) {
            wt.println(1)
            return@repeat
        }
        val ans = n.takeHighestOneBit()
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}