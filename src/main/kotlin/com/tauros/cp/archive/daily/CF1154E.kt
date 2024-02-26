package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.DSU
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.IntHeap

/**
 * @author tauros
 * 2023/12/3
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    val nums = rd.na(n)
    val removed = bar(n)
    val heap = IntHeap(n) { a, b -> -nums[a].compareTo(nums[b]) }
    repeat(n) { heap.offer(it) }

    val (left, right) = DSU(n + 2) to DSU(n + 2)
    fun remove(idx: int) {
        removed[idx - 1] = true
        left.merge(idx, idx - 1)
        right.merge(idx, idx + 1)
    }
    val ans = iar(n)
    fun fill(dsu: DSU, st: int, team: int) {
        var iter = dsu.find(st)
        for (i in 0 until k) {
            if (iter !in 1 .. n) return
            ans[iter - 1] = team
            remove(iter)
            iter = dsu.find(iter)
        }
    }

    var res = 1
    while (heap.isNotEmpty()) {
        val cur = heap.poll()
        if (removed[cur]) continue
        res = res xor 1
        ans[cur] = res + 1
        remove(cur + 1)
        fill(left, cur + 1, res + 1)
        fill(right, cur + 1, res + 1)
    }
    for (i in 0 until n) wt.print(ans[i])
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}