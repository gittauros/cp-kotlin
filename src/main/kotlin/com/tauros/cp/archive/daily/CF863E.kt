package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.lo
import com.tauros.cp.structure.Seg
import com.tauros.cp.structure.SegNonTagNode

/**
 * @author tauros
 * 2024/4/17
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/863/E
    // 离散化差分区间覆盖，然后求区间最小值大于等于2，这样可以无脑过
    // 离散化注意把 r+1 或 l-1 也离散化
    val n = rd.ni()
    val ranges = ar(n) { rd.na(2) }

    val sorted = ranges.flatMap { lo(it[0], it[1], it[1] + 1) }.sorted().distinct()
    val m = sorted.size

    val covers = iar(m + 1)
    for ((l, r) in ranges) {
        val dl = findFirst(m) { sorted[it] >= l }
        val dr = findFirst(m) { sorted[it] >= r }
        covers[dl] += 1; covers[dr + 1] -= 1
    }
    for (i in 1 .. m) covers[i] += covers[i - 1]

    data class Info(var min: int = 0) : SegNonTagNode<Info> {
        override fun update(l: Info, r: Info) {
            min = minOf(l.min, r.min)
        }
    }
    val seg = Seg(m + 1, { ar(it) { Info() } }) {
        min = covers[it]
    }
    for (i in 0 until n) {
        val (l, r) = ranges[i]
        val dl = findFirst(m) { sorted[it] >= l }
        val dr = findFirst(m) { sorted[it] >= r }

        val min = seg.query(dl, dr + 1, { min }, ::minOf)
        if (min >= 2) {
            wt.println("${i + 1}")
            return
        }
    }
    wt.println(-1)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}