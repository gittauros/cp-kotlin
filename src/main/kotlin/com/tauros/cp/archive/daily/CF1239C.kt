package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/2/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, p) = rd.ni() to rd.ni()
    val times = rd.na(n)

    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var min = INF_LONG
        var lazy = INF_LONG

        fun pushUp() {
            min = minOf(l?.min ?: INF_LONG, r?.min ?: INF_LONG)
        }

        fun build() {
            if (cl == cr) {
                min = times[cl].toLong()
                return
            }
            l = Seg(cl, mid); r = Seg(mid + 1, cr)
            l?.build(); r?.build()
            pushUp()
        }

        fun accept(tag: long) {
            lazy = maxOf(tag, lazy)
            min = maxOf(min, tag)
        }

        fun pushDown() {
            if (cl == cr) return
            if (lazy != INF_LONG) {
                l?.accept(lazy)
                r?.accept(lazy)
                lazy = INF_LONG
            }
        }

        fun update(st: int, ed: int, t: long) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(t)
                return
            }
            pushDown()
            l?.update(st, ed, t)
            r?.update(st, ed, t)
            pushUp()
        }

        fun update(pos: int, t: long) {
            if (cl == cr) {
                min = t
                return
            }
            pushDown()
            if (pos <= mid) l?.update(pos, t)
            else r?.update(pos, t)
            pushUp()
        }

        fun first(st: int, ed: int, le: long): int {
            if (cl > ed || cr < st) return -1
            if (cl >= st && cr <= ed) {
                var iter = this
                while (iter.cl != iter.cr && iter.min <= le) {
                    iter.pushDown()
                    iter = if (iter.l!!.min <= le) iter.l!! else iter.r!!
                }
                return if (iter.cl == iter.cr && iter.min <= le) iter.cl else -1
            }
            pushDown()
            val res = l!!.first(st, ed, le)
            return if (res != -1) res else r!!.first(st, ed, le)
        }
    }

    val seg = Seg(0, n - 1).apply { build() }
    val ans = lar(n)
    var iter = maxOf(seg.min, 0)
    repeat(n) {
        val i = seg.first(0, n - 1, seg.min)
        ans[i] = iter + p
        seg.update(i, INF_LONG)
        seg.update(i + 1, n - 1, ans[i])
        iter = maxOf(ans[i], seg.min)
    }

    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}