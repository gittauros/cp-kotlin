package com.tauros.cp.archive.sqrt

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod
import java.io.FileInputStream

/**
 * @author tauros
 * 2023/11/20
 */
private val bufCap = 65536

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val str = rd.ns()
    withMod(1e9.toInt() + 7) {
        var cntB = 0.toMInt()
        var ans = 0.toMInt()
        for (c in str.reversed()) {
            if (c == 'b') cntB += 1
            else {
                ans += cntB
                cntB += cntB
            }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}