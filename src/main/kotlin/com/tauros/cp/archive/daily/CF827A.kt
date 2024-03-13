package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/3/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/827/A
    // 排个序直接开拼，应该直接预估整体长度后进行类似区间差分的覆盖操作也可以的
    val n = rd.ni()
    val segments = ar(n) { "" }

    val starts = buildList {
        repeat(n) { i ->
            segments[i] = rd.ns()
            val m = rd.ni()
            repeat(m) { add(iao(rd.ni() - 1, i)) }
        }
    }.sortedBy { it[0] }.toTypedArray()

    val ans = buildString {
        for (i in starts.indices) {
            val str = segments[starts[i][1]]
            val (st, ed) = starts[i][0] to starts[i][0] + str.length
            while (length < st) append('a')
            while (length < ed) append(str[length - st])
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}