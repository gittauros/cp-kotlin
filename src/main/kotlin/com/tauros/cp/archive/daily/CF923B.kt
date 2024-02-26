package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.structure.IIHeap
import com.tauros.cp.structure.IntHeap
import com.tauros.cp.structure.LongHeap

/**
 * @author tauros
 * 2023/12/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val v = rd.na(n)
    val t = rd.na(n)

    val ans = lar(n)
    val heap = LongHeap(n)
    var sum = 0L
    for (i in 0 until n) {
        heap.offer(v[i] + sum)
        while (heap.isNotEmpty() && heap.peek() <= sum + t[i]) {
            val vol = heap.poll()
            ans[i] += vol - sum
        }
        ans[i] += t[i].toLong() * heap.size
        sum += t[i]
    }

    for (i in 0 until n) wt.print("${ans[i]} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}