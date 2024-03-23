package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int

/**
 * @author tauros
 * 2024/3/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/145/E
    // 可AC代码
    val (n, m) = rd.ni() to rd.ni()
    val str = rd.ns(n)
    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var lazy = false
        var c00 = 0
        var c11 = 0
        var c01 = 0
        var c10 = 0

        fun pushUp() {
            c00 = l!!.c00 + r!!.c00
            c11 = l!!.c11 + r!!.c11
            c01 = maxOf(l!!.c00 + r!!.c01, l!!.c01 + r!!.c11)
            c10 = maxOf(l!!.c11 + r!!.c10, l!!.c10 + r!!.c00)
        }

        fun build() {
            if (cl == cr) {
                c00 = if (str[cl] == '4') 1 else 0
                c11 = if (str[cl] == '4') 0 else 1
                c01 = 1; c10 = 1
                return
            }
            l = Seg(cl, mid); r = Seg(mid + 1, cr)
            l?.build(); r?.build()
            pushUp()
        }

        fun accept() {
            lazy = !lazy
            val t1 = c00; c00 = c11; c11 = t1
            val t2 = c01; c01 = c10; c10 = t2
        }

        fun pushDown() {
            if (cl == cr) return
            if (lazy) {
                l?.accept(); r?.accept()
                lazy = false
            }
        }

        fun flip(st: int, ed: int) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(); return
            }
            pushDown()
            l?.flip(st, ed); r?.flip(st, ed);
            pushUp()
        }
    }
    val seg = Seg(0, n - 1).apply { build() }
    repeat(m) {
        val op = rd.ns()
        if (op == "count") {
            wt.println(seg.c01)
        } else {
            val (l, r) = rd.ni() - 1 to rd.ni() - 1
            seg.flip(l, r)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}