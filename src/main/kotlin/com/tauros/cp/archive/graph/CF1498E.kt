package com.tauros.cp.archive.graph

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.structure.KHeap

/**
 * @author tauros
 * 2024/3/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1498/E
    // https://www.luogu.com.cn/article/e6h0nij1
    // https://codeforces.com/blog/entry/89137
    // 竞赛图、哈密顿路径、强连通分量的拓扑序和入度的关系
    // 在这个拓扑序中，相邻的两个强连通分量，在前面的强连通分量的入度一定严格小于之后的
    // 因为在前面的强连通分量与之后的强连通的边的方向一定是从前向后的，否则他们就能成为同一个强连通分量
    // 因为还得争取度数差最大，所以得n^2logn
    // 很奇怪，直接用println和readln是会WA1的
    val n = rd.ni()
    val deg = rd.na(n)

    data class Query(val diff: int, val qFrom: int, val qTo: int)
    val heap = KHeap<Query> { a, b -> -a.diff.compareTo(b.diff) }
    for (i in 0 until n) for (j in i + 1 until n) {
        if (deg[i] < deg[j]) {
            // 那么i一定可达j
            heap.offer(Query(deg[j] - deg[i], j + 1, i + 1))
        } else {
            // 那么j一定可达i
            heap.offer(Query(deg[i] - deg[j], i + 1 , j + 1))
        }
    }

    var success = false
    while (heap.isNotEmpty()) {
        val (_, from, to) = heap.poll()
        wt.println("? $from $to")
        wt.flush()
        val ask = readln()
        if (ask == "Yes") {
            wt.println("! $from $to")
            wt.flush()
            success = true
            break
        }
    }
    if (!success) {
        wt.println("! 0 0")
        wt.flush()
    }
}