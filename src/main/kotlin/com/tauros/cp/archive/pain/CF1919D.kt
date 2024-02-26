package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.IIHeap

/**
 * @author tauros
 * 2024/1/23
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1919/D
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val dist = rd.na(n)

        val (l, r) = iar(n) { it - 1 } to iar(n) { it + 1 }
        fun removable(pos: int) = l[pos] >= 0 && dist[l[pos]] == dist[pos] - 1 || r[pos] < n && dist[r[pos]] == dist[pos] - 1
        val vis = bar(n)
        val heap = IIHeap { a, b -> -a.compareTo(b) }
        fun offer(pos: int) {
            if (pos in dist.indices && !vis[pos] && removable(pos)) {
                heap.offer(dist[pos] to pos)
                vis[pos] = true
            }
        }
        for (i in dist.indices) offer(i)

        while (heap.isNotEmpty()) {
            val (d, i) = heap.poll()
            val (il, ir) = l[i] to r[i]
            if (il >= 0 && dist[il] == d - 1) {
                if (ir < n) l[ir] = il
                r[il] = ir
            } else if (ir < n && dist[ir] == d - 1) {
                if (il >= 0) r[il] = ir
                l[ir] = il
            } else break
            offer(il)
            offer(ir)
        }
        val idxes = dist.indices.filter { !vis[it] }
        val ans = idxes.size == 1 && dist[idxes[0]] == 0
        wt.println(if (ans) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}