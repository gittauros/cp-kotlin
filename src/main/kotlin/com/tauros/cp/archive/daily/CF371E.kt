package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.long
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/4/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/371/E
    val n = rd.ni()
    val nums = rd.na(n)
    val k = rd.ni()

    val sorted = (0 until n).sortedBy { nums[it] }.toIntArray()
    val sum = lar(n + 1)
    var (min, cur) = long.MAX_VALUE to 0L
    var end = -1
    for (i in 1 .. n) {
        sum[i] = sum[i - 1] + nums[sorted[i - 1]]
        val j = maxOf(i - k, 0)
        val add = nums[sorted[i - 1]].toLong() * (i - j) - (sum[i - 1] - if (j == 0) 0 else sum[j - 1])
        cur += add
        if (i >= k) {
            val sub = sum[i] - sum[j] - if (j == 0) 0L else nums[sorted[j - 1]].toLong() * k
            cur -= sub
            if (cur < min) {
                min = cur
                end = i
            }
        }
    }
    val ans = sorted.copyOfRange(end - k, end).sortedArray()
    for (res in ans) wt.print("${res + 1} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}