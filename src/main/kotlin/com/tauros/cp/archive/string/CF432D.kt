package com.tauros.cp.archive.string

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import com.tauros.cp.string.zfunc

/**
 * @author tauros
 * 2024/2/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/432/D
    val str = rd.ns().toCharArray()

    val z = str.zfunc()
    val n = str.size

    z[0] = n
    val sum = iar(n + 1)
    for (i in 0 until n) sum[z[i]] += 1
    for (i in 1 .. n) sum[i] += sum[i - 1]

    val ans = buildList {
        for (i in n - 1 downTo 0) if (z[i] >= n - i) {
            val cnt = sum[n] - sum[n - i - 1]
            add(n - i to cnt)
        }
    }.sortedBy { it.first }

    wt.println(ans.size)
    for ((len, cnt) in ans) {
        wt.println("$len $cnt")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}