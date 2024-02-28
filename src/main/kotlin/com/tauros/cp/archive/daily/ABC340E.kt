package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long

/**
 * @author tauros
 * 2024/2/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val a = rd.na(n)
    val b = rd.na(m)

    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var cnt = 0L

        fun build(balls: IntArray) {
            if (cl == cr) {
                cnt = balls[cl].toLong()
                return
            }
            l = Seg(cl, mid); r = Seg(mid + 1, cr)
            l?.build(balls); r?.build(balls)
        }

        fun accept(tag: long) {
            cnt += tag
        }

        fun pushDown() {
            if (cl == cr) return
            if (cnt != 0L) {
                l?.accept(cnt)
                r?.accept(cnt)
                cnt = 0
            }
        }

        fun update(st: int, ed: int, add: long) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(add)
                return
            }
            pushDown()
            l?.update(st, ed, add)
            r?.update(st, ed, add)
        }

        fun query(pos: int): long {
            if (cl == cr) return cnt
            pushDown()
            return if (pos <= mid) l!!.query(pos) else r!!.query(pos)
        }

        fun update(pos: int, ball: long) {
            if (cl == cr) {
                cnt = ball
                return
            }
            pushDown()
            if (pos <= mid) l!!.update(pos, ball)
            else r!!.update(pos, ball)
        }
    }

    val seg = Seg(0, n - 1).apply { build(a) }
    for (op in b) {
        val cnt = seg.query(op)
        seg.update(op, 0)
        val times = cnt / n
        seg.update(0, n - 1, times)
        var rest = (cnt % n).toInt()
        if (rest > 0) {
            val cut = minOf(n - 1 - op, rest)
            if (cut > 0) seg.update(op + 1, op + cut, 1)
            rest -= cut
            if (rest > 0) seg.update(0, rest - 1, 1)
        }
    }

    for (i in 0 until n) {
        val res = seg.query(i)
        wt.print("$res ")
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}