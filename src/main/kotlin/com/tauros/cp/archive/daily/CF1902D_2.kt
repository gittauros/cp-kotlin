package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.mlo
import com.tauros.cp.mmo

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
    val (n, q) = rd.ni() to rd.ni()
    val seq = rd.ns(n)

    data class Point(val x: int, val y: int) {
        operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
        operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)
    }

    val origin = Point(0, 0)
    val ops = iao(0, 1, 0, -1, 0)
    val op = mmo('U' to 0, 'D' to 2, 'L' to 3, 'R' to 1).map { (c, o) -> c to Point(ops[o], ops[o + 1]) }.toMap()
    val sums = ar(n + 1) { origin }
    val pos = mmo<Point, MutableList<Int>>()
    pos.computeIfAbsent(sums[0]) { mlo() }.add(0)
    for ((i, c) in seq.withIndex()) {
        val d = op[c]!!
        sums[i + 1] = sums[i] + d
        pos.computeIfAbsent(sums[i + 1]) { mlo() }.add(i + 1)
    }
    fun find(point: Point, st: int, ed: int): boolean {
        if (st > ed || point !in pos) return false
        val posList = pos[point]!!
        val idx = findFirst(posList.size) { posList[it] >= st }
        return idx < posList.size && posList[idx] <= ed
    }

    repeat(q) {
        val (x, y, l, r) = rd.na(4)
        val point = Point(x, y)
        val success = find(point, 0, l - 1) ||
                find(point, r + 1, n) ||
                find(sums[l - 1] + sums[r] - point, l - 1, r)
        wt.println(if (success) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}