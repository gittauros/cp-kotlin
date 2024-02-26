package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.mso
import com.tauros.cp.structure.KHeap

/**
 * @author tauros
 * 2024/1/24
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, r, k) = rd.na(4)
    val cols = iar(m) { minOf(it + 1, m - it, r, m - r + 1) }.sortedDescending().toIntArray()
    val rows = iar(n) { minOf(it + 1, n - it, r, n - r + 1) }.sortedDescending().toIntArray()

    data class Position(val cnt: long, val r: int, val c: int)
    val heap = KHeap<Position> { a, b -> -a.cnt.compareTo(b.cnt) }
    val vis = mso<long>()
    fun offer(r: int, c: int) {
        val id = r.toLong() * m + c
        if (r in 0 until n && c in 0 until m && id !in vis) {
            heap.offer(Position(rows[r].toLong() * cols[c], r, c))
            vis.add(id)
        }
    }

    var res = 0L
    offer(0, 0)
    repeat(k) {
        val (cnt, i, j) = heap.poll()
        res += cnt
        offer(i + 1, j); offer(i, j + 1); offer(i + 1, j + 1)
    }

    val possibles = (n - r + 1.0) * (m - r + 1.0)
    wt.printf("%.10f\n", res / possibles)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}