package com.tauros.cp.tasks

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2024/4/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)
    val k = rd.ni()


}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}