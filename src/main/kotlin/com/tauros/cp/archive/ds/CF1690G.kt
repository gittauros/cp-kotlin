package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/1/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1690/G
    // 除了这个解法的线段树操作外，有更简单的做法：
    // https://codeforces.com/contest/1690/submission/159898395
    // 用TreeSet维护火车头，每次操作过后如果查找小于k的i，如果i的速度小于等于当前k的速度，那么不用操作
    // 否则将k放入set，然后逐个查找大于k的坐标，如果速度大于等于k就删掉
    // 由于初始状态最多有n个点，m次操作最多放入m个点，所以最多删除n + min(n, m)个点，因此复杂度是mlogn的
    data class Info(val st: int, val ed: int, val cnt: int, val speed: int)
    val default = Info(-1, -1, 1, -1)
    infix fun Info.merge(other: Info): Info {
        return Info(this.st, other.ed, cnt + other.cnt - (if (ed == other.st) 1 else 0), minOf(speed, other.speed))
    }
    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var lazy = -1
        var info = default

        fun pushUp() {
            info = (l?.info ?: default) merge (r?.info ?: default)
        }

        fun build(speed: IntArray) {
            if (cl == cr) {
                info = if (speed[cl] == -1) default
                else Info(speed[cl], speed[cr], 1, speed[cl])
                return
            }
            l = Seg(cl, mid)
            r = Seg(mid + 1, cr)
            l?.build(speed)
            r?.build(speed)
            pushUp()
        }

        fun accept(tag: int) {
            lazy = tag
            info = Info(tag, tag, 1, tag)
        }

        fun pushDown() {
            if (cl == cr) return
            if (lazy != -1) {
                l?.accept(lazy)
                r?.accept(lazy)
                lazy = -1
            }
        }

        fun query(pos: int): int {
            if (cl == cr) return info.speed
            pushDown()
            return if (pos <= mid) l!!.query(pos) else r!!.query(pos)
        }

        fun update(st: int, ed: int, speed: int) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(speed)
                return
            }
            pushDown()
            l?.update(st, ed, speed)
            r?.update(st, ed, speed)
            pushUp()
        }

        fun first(st: int, ed: int, lt: int): int {
            if (cl > ed || cr < st) return -1
            if (cl >= st && cr <= ed) {
                var iter = this
                while (iter.cl != iter.cr) {
                    iter.pushDown()
                    iter = if (iter.l!!.info.speed < lt) iter.l!! else iter.r!!
                }
                return if (iter.cl == iter.cr && iter.info.speed < lt) iter.cl else -1
            }
            pushDown()
            val res = l!!.first(st, ed, lt)
            return if (res != -1) res else r!!.first(st, ed, lt)
        }
    }
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val a = rd.na(n) + iao(-1)

        val speed = a.runningReduce(::minOf).toIntArray()
        val seg = Seg(0, n)
        seg.build(speed)

        repeat(m) {
            val (k, d) = rd.ni() - 1 to rd.ni()
            a[k] -= d

            val cur = seg.query(k)
            if (a[k] < cur) {
                val ed = seg.first(k, n, a[k]) - 1
                seg.update(k, ed, a[k])
            }

            wt.print(seg.info.cnt - 1)
            wt.print(" ")
        }
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}