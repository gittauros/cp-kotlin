package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/1/31
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1710/problem/B
    // 一次函数差分，不等式变换
    val cases = rd.ni()
    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var max = -INF_LONG

        fun pushUp() {
            max = maxOf(l?.max ?: 0, r?.max ?: 0)
        }

        fun build(vals: LongArray): Seg {
            if (cl == cr) {
                max = vals[cl]
                return this
            }
            l = Seg(cl, mid)
            r = Seg(mid + 1, cr)
            l?.build(vals)
            r?.build(vals)
            pushUp()
            return this
        }

        fun query(st: int, ed: int): long {
            if (cl > ed || cr < st) return -INF_LONG
            if (cl >= st && cr <= ed) return max
            return maxOf(l!!.query(st, ed), r!!.query(st, ed))
        }
    }
    data class Rain(val x: int, val p: int, val idx: int)
    data class Line(var k: long, var b: long) {
        fun add(k: int, b: int) = add(k.toLong(), b.toLong())
        fun add(k: long, b: long) {
            this.k += k
            this.b += b
        }
    }
    repeat(cases) {
        val (n, m) = rd.ni() to rd.nl()
        val rain = ar(n) { idx -> Rain(rd.ni(), rd.ni(), idx) }

        rain.sortBy { it.x }
        val diff = ar(n) { Line(0, 0) }
        for (i in 0 until n) {
            val (x, p) = rain[i]
            val stL = findFirst(0, i) { rain[it].x >= x - p }
            diff[stL].add(1, p - x)
            diff[i].add(-1, -(p - x))

            diff[i].add(0, p)
            if (i + 1 < n) diff[i + 1].add(0, -p)

            val edR = findFirst(i, n) { rain[it].x > x + p }
            if (i + 1 < n) diff[i + 1].add(-1, p + x)
            if (edR < n) diff[edR].add(1, -(p + x))
        }
        for (i in 1 until n) diff[i].add(diff[i - 1].k, diff[i - 1].b)

        val full = lar(n) { diff[it].k * rain[it].x + diff[it].b }
        val (pre, suf) = lar(n) to lar(n)
        for (i in 0 until n) pre[i] = if (i == 0) full[i] else maxOf(pre[i - 1], full[i])
        for (i in n - 1 downTo 0) suf[i] = if (i == n - 1) full[i] else maxOf(suf[i + 1], full[i])

        val (inRangeL, inRangeR) = lar(n) { full[it] - rain[it].x } to lar(n) { full[it] + rain[it].x }
        val (segL, segR) = Seg(0, n - 1).build(inRangeL) to Seg(0, n - 1).build(inRangeR)

        val ans = iar(n)
        for (i in 0 until n) {
            val (x, p, idx) = rain[i]
            val stL = findFirst(0, i) { rain[it].x >= x - p } - 1
            if (stL >= 0 && pre[stL] > m) continue
            val edR = findFirst(i, n) { rain[it].x > x + p }
            if (edR < n && suf[edR] > m) continue

            val limitL = p + m - x
            val l = segL.query(stL + 1, i)
            if (l > limitL) continue
            val limitR = p + m + x
            val r = segR.query(i, edR - 1)
            if (r > limitR) continue
            ans[idx] = 1
        }
        wt.println(ans.joinToString(""))
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}