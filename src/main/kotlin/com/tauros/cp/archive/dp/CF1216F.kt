package com.tauros.cp.archive.dp

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long

/**
 * @author tauros
 * 2024/2/8
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1216/F
    // 应该可以单调队列优化
    val (n, k) = rd.ni() to rd.ni()
    val str = rd.ns(n)

    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var lazy = INF_LONG
        var min = INF_LONG

        fun pushUp() {
            min = minOf(l?.min ?: INF_LONG, r?.min ?: INF_LONG)
        }

        fun accept(tag: long) {
            lazy = minOf(lazy, tag)
            min = minOf(min, tag)
        }

        fun pushDown() {
            if (cl == cr) return
            if (l == null) l = Seg(cl, mid)
            if (r == null) r = Seg(mid + 1, cr)
            if (lazy != INF_LONG) {
                l?.accept(lazy)
                r?.accept(lazy)
                lazy = INF_LONG
            }
        }

        fun update(st: int, ed: int, new: long) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(new)
                return
            }
            pushDown()
            l?.update(st, ed, new); r?.update(st, ed, new)
            pushUp()
        }

        fun query(pos: int): long {
            if (cl == cr) return min
            pushDown()
            return if (pos <= mid) l!!.query(pos) else r!!.query(pos)
        }
    }

    val dp = Seg(0, n)
    dp.update(0, 0, 0)
    for (i in 1 .. n) {
        val (st, ed) = if (str[i - 1] == '0') i to i else maxOf(1, i - k) to minOf(n, i + k)
        val new = dp.query(st - 1) + i
        dp.update(st, ed, new)
    }
    val ans = dp.query(n)
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}