package com.tauros.cp.archive.bipartite

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.DSU

/**
 * @author tauros
 * 2024/3/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1012/B
    // 完全不会
    val (n, m, q) = rd.na(3)

    val dsu = DSU(n + m)
    repeat(q) {
        val (r, c) = rd.ni() - 1 to rd.ni() - 1
        dsu.merge(r, c + n)
    }
    val cnt = (0 until n + m).count { dsu.find(it) == it }
    wt.println(cnt - 1)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}