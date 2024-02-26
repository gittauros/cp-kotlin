package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.avls
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2023/11/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    val nums = rd.na(n)

//    class Seg(val cl: Int, val cr: Int) {
//        val mid = cl + cr shr 1
//        var l: Seg? = null
//        var r: Seg? = null
//        var min = INF
//
//        fun pushDown() {
//            if (cl == cr) return
//            if (l == null) l = Seg(cl, mid)
//            if (r == null) r = Seg(mid + 1, cr)
//        }
//
//        fun pushUp() {
//            min = minOf(l?.min ?: INF, r?.min ?: INF)
//        }
//
//        fun update(pos: Int, add: Int) {
//            if (cl == cr) {
//                if (min == INF) min = 0
//                min += add
//                if (min <= 0) min = INF
//                return
//            }
//            pushDown()
//            if (pos <= mid) l?.update(pos, add)
//            else r?.update(pos, add)
//            pushUp()
//        }
//
//        fun last(st: Int, ed: Int): IIP {
//            if (cl > ed || cr < st) return INF to -1
//            if (cl >= st && cr <= ed) {
//                var iter = this
//                while (iter.cl != iter.cr) {
//                    iter.pushDown()
//                    iter = if (iter.r!!.min == 1) iter.r!! else iter.l!!
//                }
//                return iter.min to iter.cl
//            }
//            pushDown()
//            val res = r!!.last(st, ed)
//            return if (res.first == 1) res else l!!.last(st, ed)
//        }
//    }
//    val distinct = nums.sorted().distinct().toIntArray()
//    val discrete = buildMap { for ((i, d) in distinct.withIndex()) put(d, i) }
//    val seg = Seg(0, distinct.lastIndex)
//    for (i in 0 until k - 1) seg.update(discrete[nums[i]]!!, 1)
//    for (i in k - 1 until n) {
//        if (i - k >= 0) seg.update(discrete[nums[i - k]]!!, -1)
//        seg.update(discrete[nums[i]]!!, 1)
//        val (cnt, res) = seg.last(0, distinct.lastIndex)
//        wt.println(if (cnt == 1) distinct[res] else "Nothing")
//    }

    val cnt = mmo<Int, Int>()
    val set = avls<Int> { a, b -> a.compareTo(b) }
    for (i in 0 until n) {
        if (i - k >= 0) {
            val (count, pre) = cnt[nums[i - k]]!! to nums[i - k]
            if (count == 1) cnt.remove(pre)
            else cnt[pre] = count - 1
            if (count == 2) set.add(pre)
            else set.remove(pre)
        }
        val (count, num) = cnt.getOrDefault(nums[i], 0) to nums[i]
        cnt[num] = count + 1
        if (count == 0) set.add(nums[i])
        else set.remove(nums[i])
        if (i >= k - 1) {
            if (set.isEmpty()) {
                wt.println("Nothing")
            } else {
                wt.println(set.last()!!)
            }
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}