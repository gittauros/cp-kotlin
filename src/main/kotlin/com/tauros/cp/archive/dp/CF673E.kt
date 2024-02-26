package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.double
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.dar
import com.tauros.cp.dq
import com.tauros.cp.runningFold

/**
 * @author tauros
 * 2024/2/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/673/problem/E
    // 斜率优化概率期望dp
    val (n, k) = rd.ni() to rd.ni()
    val t = rd.na(n)

    val sums = t.runningFold(0L, long::plus)
    val ratios = (0 until n).map { sums[it + 1].toDouble() / t[it] }.runningFold(0.0, double::plus)
    val inverses = (0 until n).map { 1.0 / t[it] }.runningFold(0.0, double::plus)

    val dp = ar(2) { dar(n + 1) { ratios[it] } }
    var cur = 0

    fun dpi(bi: double, i: int) = bi + ratios[i]
    fun ki(i: int) = inverses[i]
    fun xj(j: int) = sums[j].toDouble()
    fun yj(j: int) = inverses[j] * sums[j] - ratios[j] + dp[1 - cur][j]

    data class Point(val x: double, val y: double) {
        fun ratio(other: Point) = (y - other.y) / (x - other.x)
    }
    val q = dq<Point>()
    fun offer(x: double, y: double) {
        val point = Point(x, y)
        if (q.size < 2) q.addLast(point)
        else {
            while (q.size >= 2) {
                val (tail, second) = q.last() to q[q.lastIndex - 1]
                val (topRatio, secondRatio) = point.ratio(tail) to point.ratio(second)
                if (topRatio < secondRatio) q.removeLast() else break
            }
            q.addLast(point)
        }
    }
    fun best(ki: double): Point {
        while (q.size > 1) {
            val (head, second) = q.first() to q[1]
            val ratio = second.ratio(head)
            if (ratio < ki) q.removeFirst() else break
        }
        return q.first()
    }
    repeat(k - 1) {
        cur = 1 - cur
        q.clear()
        offer(xj(it + 1), yj(it + 1))
        for (i in it + 2 .. n) {
            val ki = ki(i)
            val (x, y) = best(ki)
            val bi = y - ki * x
            dp[cur][i] = dpi(bi, i)
            offer(xj(i), yj(i))
        }
    }

    wt.printf("%.10f\n", dp[cur][n])
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}