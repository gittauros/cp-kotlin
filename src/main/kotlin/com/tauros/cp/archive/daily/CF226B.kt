package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.runningFold

/**
 * @author tauros
 * 2024/4/8
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/226/B
    // 猜一个贪心试试，越小的数应该分配越多的贡献次数
    // 然后k可能会重复，重要的是会有多次1出现，缓存一下即可
    val n = rd.ni()
    val nums = rd.na(n).sortedArray()

    val sums = nums.runningFold(0L, long::plus).toLongArray()
    val q = rd.ni()
    val ans = lar(n) {
        val k = it + 1
        var res = 0L
        var (iter, cnt) = n - 1 to 1
        var range = k.toLong()
        while (iter > 0) {
            val end = maxOf(0, iter - range).toInt()
            val sum = sums[iter] - sums[end]
            res += sum * cnt
            iter = end; cnt += 1; range *= k
        }
        res
    }
    repeat(q) {
        val k = minOf(rd.ni(), n)
        wt.print("${ans[k - 1]} ")
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}