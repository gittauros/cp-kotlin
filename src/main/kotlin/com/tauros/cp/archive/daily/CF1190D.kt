package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2024/3/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1190/D
    // 不难，当成网格图来算就好了，注意避免重复计算
    val n = rd.ni()
    val points = ar(n) { rd.na(2) }

    val sortedX = points.map { it[0] }.sorted().distinct()
    fun IntArray.query(pos: int) = this.bitQuery(pos, 0, int::plus)
    fun IntArray.update(pos: int) {
        if (query(pos) - query(pos - 1) == 0) this.bitUpdateWithIndex(pos) { this[it] += 1 }
    }
    val cap = sortedX.size
    val bit = iar(cap + 1)

    val sorted = points.sortedWith { a, b -> if (a[1] == b[1]) a[0].compareTo(b[0]) else -a[1].compareTo(b[1]) }
    var (i, ans) = 0 to 0L
    while (i < sorted.size) {
        var j = i
        val line = buildList {
            while (j < sorted.size && sorted[j][1] == sorted[i][1]) {
                val x = findFirst(cap) { sortedX[it] >= sorted[j][0] } + 1
                add(x); bit.update(x)
                j += 1
            }
        }
        var res = 0L
        for (k in line.indices) {
            val x = line[k]
            val next = if (k + 1 < line.size) line[k + 1] else cap + 1
            res += bit.query(x) * (bit.query(next - 1) - bit.query(x) + 1L)
        }
        ans += res; i = j
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}