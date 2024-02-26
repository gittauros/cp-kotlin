package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.graph.IGraph
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/3
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)
        val stack = iar(n)
        var top = -1

        val (maxLeft, maxRight) = iar(n) { -1 } to iar(n) { n }
        for (i in 0 until n) {
            while (top >= 0 && nums[i] > nums[stack[top]]) maxRight[stack[top--]] = i
            if (top >= 0) maxLeft[i] = stack[top]
            stack[++top] = i
        }
        top = -1
        val (minLeft, minRight) = iar(n) { -1 } to iar(n) { n }
        for (i in 0 until n) {
            while (top >= 0 && nums[i] <= nums[stack[top]]) minRight[stack[top--]] = i
            if (top >= 0) minLeft[i] = stack[top]
            stack[++top] = i
        }

        val log = iar(n + 1)
        for (i in 2 .. n) log[i] = log[i / 2] + 1
        fun Array<IntArray>.init(arr: IntArray) {
            for (i in 0 until n) this[0][i] = arr[i] + 1
            for (b in 1 .. log[n]) {
                for (i in 0 .. n - (1 shl b)) {
                    this[b][i] = minOf(this[b - 1][i], this[b - 1][i + (1 shl b - 1)])
                }
            }
        }
        fun Array<IntArray>.query(st: int, ed: int): int {
            val len = ed - st + 1
            return minOf(this[log[len]][st], this[log[len]][ed - (1 shl log[len]) + 1])
        }
        fun Array<IntArray>.last(st: int, ed: int, le: int): int {
            var (l, r) = st to ed
            while (l < r && query(l, r) <= le) {
                val mid = l + r shr 1
                if (query(mid + 1, r) <= le) l = mid + 1
                else r = mid
            }
            return if (query(l, l) <= le) l else n
        }
        val (stMin, stMax) = ar(log[n] + 1) { iar(n) } to ar(log[n] + 1) { iar(n) }
        stMin.init(minLeft)
        stMax.init(maxLeft)
        var (iter, ans) = 0 to 0
        while (iter < n - 1) {
            val next = if (nums[iter + 1] < nums[iter]) {
                val (cl, cr) = iter + 1 to maxRight[iter] - 1
                stMin.last(cl, cr, iter)
            } else {
                val (cl, cr) = iter + 1 to minRight[iter] - 1
                stMax.last(cl, cr, iter)
            }
            iter = next
            ans++
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}