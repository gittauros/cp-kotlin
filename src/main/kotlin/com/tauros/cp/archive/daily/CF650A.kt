package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.geometry.IPoint2
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2024/3/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val n = rd.ni()
    val points = ar(n) { IPoint2(rd.ni(), rd.ni()) }

    val xCnt = mmo<int, int>().default { 0 }
    val yCnt = mmo<int, int>().default { 0 }
    val cnt = mmo<IPoint2, int>().default { 0 }
    var ans = 0L
    for (point in points) {
        val (x, y) = point
        ans += xCnt[x] + yCnt[y] - cnt[point]
        xCnt[x] += 1; yCnt[y] += 1
        cnt[point] += 1
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}