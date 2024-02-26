package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
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
    val (q, x) = rd.ni() to rd.ni()
    val cnt = iar(x)
    var iter = 0
    var mex = 0
    repeat(q) {
        val y = rd.ni()
        val mod = y % x
        cnt[mod]++
        while (cnt[iter] > 0) {
            cnt[iter]--
            iter = (iter + 1) % x
            mex++
        }
        wt.println(mex)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}