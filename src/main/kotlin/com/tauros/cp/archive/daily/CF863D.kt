package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/3/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/863/D
    // m <= 100，暴力倒序试试捏
    val (n, q, m) = rd.na(3)
    val nums = rd.na(n)
    val ops = ar(q) { iao(rd.ni(), rd.ni() - 1, rd.ni() - 1) }.apply { reverse() }
    val idxes = rd.na(m).map { it - 1 }.toIntArray()

    for (id in idxes) {
        var i = id
        for ((op, l, r) in ops) {
            if (i !in l .. r) continue
            i = if (op == 1) {
                val (len, pos) = r - l + 1 to i - l
                val j = (pos - 1 + len) % len
                j + l
            } else {
                l + r - i
            }
        }
        wt.print("${nums[i]} ")
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}