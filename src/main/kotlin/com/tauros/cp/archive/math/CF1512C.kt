package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1512/G
    // 1e7暴力筛能过，居然也能积性函数线性筛，佛了
    val cap = 1e7.toInt()
    val sum = iar(cap + 1)
    val min = iar(cap + 1) { -1 }
    for (i in 1 .. cap) {
        for (j in i .. cap step i) sum[j] += i
        if (sum[i] <= cap && (min[sum[i]] == -1 || min[sum[i]] > i)) min[sum[i]] = i
    }

    val cases = rd.ni()
    repeat(cases) {
        val c = rd.ni()
        val num = min[c]
        wt.println(num)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}