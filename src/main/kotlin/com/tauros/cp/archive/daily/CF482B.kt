package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iao

/**
 * @author tauros
 * 2023/11/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val ranges = ar(m) { iao(rd.ni() - 1, rd.ni() - 1, rd.ni()) }
    class Seg(val cl: Int, val cr: Int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var lazy: Int = 0
        var num: Int = 0

        fun accept(lazy: Int) {
            this.lazy = lazy or this.lazy
            num = num or lazy
        }

        fun pushDown() {
            if (cl == cr) return
            if (l == null) l = Seg(cl, mid)
            if (r == null) r = Seg(mid + 1, cr)
            if (lazy != 0) {
                l?.accept(lazy)
                r?.accept(lazy)
                lazy = 0
            }
        }

        fun pushUp() {
            this.num = (l?.num ?: -1) and (r?.num ?: -1)
        }

        fun update(st: Int, ed: Int, num: Int) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(num)
                return
            }
            pushDown()
            l?.update(st, ed, num)
            r?.update(st, ed, num)
            pushUp()
        }

        fun query(pos: Int): Int {
            if (pos == cl && cl == cr) return num
            pushDown()
            if (pos <= mid) return l!!.query(pos)
            return r!!.query(pos)
        }

        fun query(st: Int, ed: Int): Int {
            if (cl > ed || cr < st) return -1
            if (cl >= st && cr <= ed) {
                return num
            }
            pushDown()
            return l!!.query(st, ed) and r!!.query(st, ed)
        }
    }

    val seg = Seg(0, n - 1)
    for ((l, r, q) in ranges) {
        seg.update(l, r, q)
    }
    for ((l, r, q) in ranges) {
        val res = seg.query(l, r)
        if (res != q) {
            wt.println("NO")
            return
        }
    }
    wt.println("YES")
    for (i in 0 until n) {
        if (i > 0) wt.print(" ")
        wt.print(seg.query(i))
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}