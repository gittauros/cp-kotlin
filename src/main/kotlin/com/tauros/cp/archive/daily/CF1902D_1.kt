package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2023/12/10
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, q) = rd.ni() to rd.ni()
    val ops = iao(0, 1, 0, -1, 0)
    val opMap = mmo('U' to 0, 'R' to 1, 'D' to 2, 'L' to 3)
    val str = rd.ns(n)
    val (pre, suf) = ar(2) { iar(n + 1) } to ar(2) { iar(n + 2) }
    for (i in 1 .. n) {
        val op = opMap[str[i - 1]]!!
        for (j in 0 until 2) pre[j][i] = pre[j][i - 1] + ops[op + j]
    }
    for (i in n downTo 1) {
        val op = opMap[str[i - 1]]!!
        for (j in 0 until 2) suf[j][i] = suf[j][i + 1] + ops[op + j]
    }

    val ans = bar(q)
    data class Query(val end: int, val pos: IIP, val idx: int)
    val (preQueries, sufQueries) = mmo<int, MutableList<Query>>() to mmo<int, MutableList<Query>>()
    repeat(q) {
        val (x, y, l, r) = rd.na(4)
        if (x == 0 && y == 0) {
            ans[it] = true
            return@repeat
        }
        val leftQuery = Query(0, x to y, it)
        preQueries.computeIfAbsent(l - 1) { mlo() }.add(leftQuery)

        val targetMid = x - pre[0][l - 1] + suf[0][r + 1] to y - pre[1][l - 1] + suf[1][r + 1]
        val midQuery = Query(r,  targetMid, it)
        sufQueries.computeIfAbsent(l) { mlo() }.add(midQuery)

        if (r < n) {
            val midEnd = pre[0][l - 1] + suf[0][l] - suf[0][r + 1] to pre[1][l - 1] + suf[1][l] - suf[1][r + 1]
            val targetRight = x - midEnd.first + pre[0][r] to y - midEnd.second + pre[1][r]
            val rightQuery = Query(r + 1, targetRight, it)
            preQueries.computeIfAbsent(n) { mlo() }.add(rightQuery)
        }
    }

    val preMap = mmo<IIP, int>()
    for (i in 0 .. n) {
        preMap[pre[0][i] to pre[1][i]] = i
        if (i in preQueries) {
            for ((end, pos, idx) in preQueries[i]!!) {
                if (ans[idx]) continue
                val find = preMap[pos]
                if (find != null && find >= end) ans[idx] = true
            }
        }
    }
    val sufMap = mmo<IIP, int>()
    for (i in n downTo 1) {
        sufMap[suf[0][i] to suf[1][i]] = i
        if (i in sufQueries) {
            for ((end, pos, idx) in sufQueries[i]!!) {
                if (ans[idx]) continue
                val find = sufMap[pos]
                if (find != null && find <= end) ans[idx] = true
            }
        }
    }
    for (res in ans) {
        wt.println(if (res) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}