package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.structure.IntHeap

/**
 * @author tauros
 * 2023/12/8
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, k) = rd.na(3)
    val props = ar(n) { rd.na(3) }

    val to = iar(n) { it }
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        to[v] = maxOf(to[v], u)
    }
    val graph = ar(n) { mlo<int>() }
    for (v in 0 until n) if (to[v] != -1) graph[to[v]].add(v)

    val heap = IntHeap(n, int::compareTo)
    var (cur, ans) = k to 0
    for (i in 0 until n) {
        val (a, b) = props[i]
        while (heap.isNotEmpty() && cur < a) {
            cur += 1
            ans -= heap.poll()
        }
        if (cur < a) {
            ans = -1
            break
        }

        cur += b
        for (score in graph[i].map { props[it][2] }) {
            cur -= 1
            heap.offer(score)
            ans += score
        }
    }
    while (heap.isNotEmpty() && cur < 0) {
        cur += 1
        ans -= heap.poll()
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}