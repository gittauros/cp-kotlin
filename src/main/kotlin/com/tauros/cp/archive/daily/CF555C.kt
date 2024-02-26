package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.char
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/2/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    data class Op(val x: int, val y: int, val d: char)
    val (_, q) = rd.na(2)
    val nums = mlo<int>()
    val ops = mlo<Op>()
    repeat(q) {
        val op = Op(rd.ni(), rd.ni(), rd.nc())
        ops.add(op); nums.add(op.x); nums.add(op.y)
    }

    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var lazy = 0
        var max = 0

        fun accept(tag: int) {
            lazy = maxOf(lazy, tag)
            max = maxOf(max, tag)
        }

        fun pushDown() {
            if (cl == cr) return
            if (l == null) l = Seg(cl, mid)
            if (r == null) r = Seg(mid + 1, cr)
            if (lazy != 0) {
                l?.accept(lazy); r?.accept(lazy)
                lazy = 0
            }
        }

        fun pushUp() {
            max = maxOf(l?.max ?: 0, r?.max ?: 0)
        }

        fun update(st: int, ed: int, far: int) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(far)
                return
            }
            pushDown()
            l?.update(st, ed, far)
            r?.update(st, ed, far)
            pushUp()
        }

        fun query(pos: int): int {
            if (cl == cr) return max
            pushDown()
            return if (pos <= mid) l!!.query(pos) else r!!.query(pos)
        }
    }

    val sorted = nums.sorted().distinct().toIntArray()
    val n = sorted.size
    val vis = bar(n)

    val (row, col) = Seg(1, n) to Seg(1, n)
    for ((x, y, d) in ops) {
        val r = findFirst(n) { sorted[it] >= y } + 1
        if (vis[r - 1]) {
            wt.println(0)
            continue
        }
        vis[r - 1] = true
        val c = findFirst(n) { sorted[it] >= x } + 1
        if (d == 'L') {
            val max = row.query(r)
            if (max + 1 <= c) col.update(max + 1, c, r)
            wt.println(if (max == 0) x else x - sorted[max - 1])
        } else {
            val max = col.query(c)
            if (max + 1 <= r) row.update(max + 1, r, c)
            wt.println(if (max == 0) y else y - sorted[max - 1])
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}