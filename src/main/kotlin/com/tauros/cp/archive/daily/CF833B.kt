package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2023/12/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    val nums = rd.na(n)

    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var max = 0
        var lazy = 0

        fun build(dp: IntArray, st: int) {
            lazy = 0
            if (cl == cr) {
                max = if (cl >= st) dp[cl] else 0
                return
            }
            max = 0
            if (l == null) l = Seg(cl, mid)
            if (r == null) r = Seg(mid + 1, cr)
            l?.build(dp, st)
            r?.build(dp, st)
            pushUp()
        }

        fun accept(tag: int) {
            lazy += tag
            max += tag
        }

        fun pushDown() {
            if (cl == cr) return
            if (lazy != 0) {
                l?.accept(lazy)
                r?.accept(lazy)
                lazy = 0
            }
        }

        fun pushUp() {
            max = maxOf(l?.max ?: 0, r?.max ?: 0)
        }

        fun update(st: int, ed: int, add: int) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(add)
                return
            }
            pushDown()
            l?.update(st, ed, add)
            r?.update(st, ed, add)
            pushUp()
        }

        fun query(st: int, ed: int): int {
            if (cl > ed || cr < st) return 0
            if (cl >= st && cr <= ed) return max
            pushDown()
            return maxOf(l!!.query(st, ed), r!!.query(st, ed))
        }
    }

    val dp = iar(n + 1)
    val seg = Seg(0, n)
    repeat(k) {
        seg.build(dp, it)
        val new = iar(n + 1)
        val pre = mmo<int, int>().default { 0 }
        for (i in it + 1 .. n) {
            val num = nums[i - 1]
            val pos = pre[num]
            seg.update(pos, i - 1, 1)
            new[i] = seg.query(it, i - 1)
            pre[num] = i
        }
        new.copyInto(dp)
    }
    wt.println(dp[n])
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}