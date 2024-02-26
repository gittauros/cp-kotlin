package com.tauros.cp.archive.geometry

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.geometry.IPoint2

/**
 * @author tauros
 * 2024/1/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/166/B
    val n = rd.ni()
    val pointsA = ar(n) { IPoint2(rd.ni(), rd.ni()) }
    val m = rd.ni()
    val pointsB = ar(m) { IPoint2(rd.ni(), rd.ni()) }

    val bottom = (0 until n).minBy { pointsA[it].y }
    val vectors = (0 until n).filter { it != bottom }.map {
        pointsA[it] - pointsA[bottom] to it
    }.sortedWith { a, b -> a.first side b.first }

    fun IPoint2.inside(): Boolean {
        if (this == pointsA[bottom]) return false
        val curVector = this - pointsA[bottom]
        var (l, r) = 0 to vectors.lastIndex
        if (curVector side vectors[r].first >= 0 || curVector side vectors[l].first <= 0) return false
        while (l < r - 1) {
            val mid = l + r shr 1
            if (curVector side vectors[mid].first >= 0) l = mid
            else r = mid
        }
        val bound = vectors[l].first - vectors[r].first
        val (v1, v2) = pointsA[bottom] - pointsA[vectors[r].second] to this - pointsA[vectors[r].second]
        return bound side v1 == bound side v2
    }

    val ans = pointsB.all(IPoint2::inside)
    wt.println(if (ans) "YES" else "NO")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}