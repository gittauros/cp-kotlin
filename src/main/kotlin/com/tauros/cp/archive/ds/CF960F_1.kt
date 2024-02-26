package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iao
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2023/12/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/960/F
    val (n, m) = rd.ni() to rd.ni()
    val base = 1e5.toInt() + 1L
    val nums = mlo<long>()
    val edges = ar(m) {
        iao(rd.ni() - 1, rd.ni() - 1, rd.ni()).also { (u, v, w) ->
            nums.add(u * base)
            nums.add(v * base)
            nums.add(u * base + w)
            nums.add(v * base + w)
        }
    }

    val sorted = nums.sorted().distinct().toLongArray()
    fun discrete(num: long) = findFirst(sorted.size) { sorted[it] >= num } + 1

    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var lazy = 0
        var max = 0

        fun accept(tag: int) {
            lazy = maxOf(tag, lazy)
            max = maxOf(max, tag)
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
            max = maxOf(l?.max ?: 0, r?.max ?: 0)
        }

        fun update(st: int, ed: int, len: int) {
            if (cl > ed || cr < st) return
            if (cl >= st && cr <= ed) {
                accept(len)
                return
            }
            pushDown()
            l?.update(st, ed, len)
            r?.update(st, ed, len)
            pushUp()
        }

        fun query(st: int, ed: int): int {
            if (cl > ed || cr < st) return 0
            if (cl >= st && cr <= ed) return max
            pushDown()
            return maxOf(l!!.query(st, ed), r!!.query(st, ed))
        }
    }
    val seg = Seg(1, sorted.size + 1)

    var ans = 0
    for ((u, v, w) in edges) {
        val (cl, cr) = discrete(u * base) to discrete(u * base + w) - 1
        val dp = seg.query(cl, cr) + 1
        ans = maxOf(dp, ans)

        val (st, ed) = discrete(v * base + w) to discrete((v + 1) * base) - 1
        seg.update(st, ed, dp)
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}