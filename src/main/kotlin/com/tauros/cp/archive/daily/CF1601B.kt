package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.IIHeap
import com.tauros.cp.structure.KHeap

/**
 * @author tauros
 * 2024/1/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val a = rd.na(n)
    val b = rd.na(n)

    val pre = iar(n + 1) { -1 }
    data class Info(val step: int, val far: int, val before: int)
    val heap = KHeap<Info> { p, q -> p.step.compareTo(q.step) }
    pre[n] = n + 1
    heap.offer(Info(0, n + b[n - 1] - a[n - 1], n))
    for (i in n - 2 downTo 0) {
        val pos = i + 1
        while (heap.isNotEmpty() && heap.peek().far > pos) heap.poll()
        if (heap.isEmpty()) break
        val from = heap.peek()
        pre[pos] = from.before
        heap.offer(Info(from.step + 1, pos + b[i] - a[pos + b[i] - 1], pos))
    }
    while (heap.isNotEmpty() && heap.peek().far > 0) heap.poll()
    if (heap.isEmpty()) {
        wt.println(-1)
        return
    }
    val ans = buildList {
        add(0)
        var iter = heap.peek().before
        while (iter < n) {
            add(iter)
            iter = pre[iter]
        }
    }
    wt.println(ans.size)
    for (res in ans.reversed()) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}