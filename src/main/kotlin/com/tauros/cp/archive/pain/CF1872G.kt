package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.common.minOf
import com.tauros.cp.iar
import com.tauros.cp.runningFold

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
    // https://codeforces.com/problemset/problem/1872/G
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        var (first, last) = -1 to -1
        var prod = 1L
        for ((i, num) in nums.withIndex()) if (num != 1) {
            if (first == -1) first = i
            last = i
            if (prod <= 2 * n) prod *= num
        }
        var (l, r) = if (prod > 2 * n) first + 1 to last + 1 else 1 to 1
        if (prod <= 2 * n) {
            val (sums, prods) = nums.runningFold(0L, long::plus) to nums.runningFold(1L, long::times)
            val (sum, pre) = sums.last() to iar(n)
            for (i in 0 until n) pre[i] = if (nums[i] != 1) i else if (i == 0) -1 else pre[i - 1]
            var max = sum
            for (i in 1 .. n) if (nums[i - 1] != 1) {
                var (iter, step) = i.toLong() to nums[i - 1].toLong()
                while (true) {
                    val j = pre[minOf(n.toLong(), iter).toInt() - 1] + 1
                    if (j > i && prods[j] / prods[i - 1] + sum - (sums[j] - sums[i - 1]) > max) {
                        max = prods[j] / prods[i - 1] + sum - (sums[j] - sums[i - 1])
                        l = i; r = j
                        step = prods[j] / prods[i - 1]
                    }
                    if (iter > n) break
                    iter += step
                }
            }
        }
        wt.println("$l $r")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}