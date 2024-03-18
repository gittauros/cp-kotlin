package com.tauros.cp.archive.geometry

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.geometry.DPoint2
import kotlin.math.abs

/**
 * @author tauros
 * 2024/3/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/772/B
    // 只用考虑相邻的两个边不会凹
    // 一开始想和中间的点的切点连线是角平分线，实际上公切线和 直线：前驱->后继 是平行的
    // 所以这个中间的点的切点的距离就等于三个点形成的三角形以 直线：前驱->后继 为底的高的一半
    // 求最小值即可
    val n = rd.ni()
    val points = ar(n) { DPoint2(rd.ni().toDouble(), rd.ni().toDouble()) }

    var ans = 1e9 * 2
    for (i in 0 until n) {
        val cur = points[i]
        val pre = if (i == 0) points.last() else points[i - 1]
        val nex = if (i == n - 1) points.first() else points[i + 1]

        val (toPre, toNex) = pre - cur to nex - cur
        val area = abs(toPre cross toNex)
        val distance = area / (nex - pre).len()
        ans = minOf(ans, distance / 2)
    }
    wt.printf("%.10f\n", ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}