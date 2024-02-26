package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst

/**
 * @author tauros
 * 2023/11/17
 */
private val bufCap = 65536

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val last = findFirst(1, n) {
            it.toLong() * it + 1L > 2L * n
        } - 1
        if (last < 3) {
            wt.println(0)
        } else {
            wt.println((last + 1) / 2 - 1)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}