package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.iter
import com.tauros.cp.common.le
import com.tauros.cp.structure.IntHeap

/**
 * @author tauros
 * 2023/12/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    if (k > n) {
        wt.println("NO")
        return
    }
    val heap = IntHeap(k) { a, b -> -a.compareTo(b) }
    for (b in 1 iter { it shl 1 } le n) {
        if (n and b != 0) heap.offer(b)
    }
    if (heap.size > k) {
        wt.println("NO")
        return
    }
    while (heap.size < k) {
        val max = heap.poll()
        val res = max shr 1
        heap.offer(res)
        heap.offer(res)
    }
    wt.println("YES")
    while (heap.isNotEmpty()) {
        wt.print(heap.poll())
        wt.print(" ")
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}