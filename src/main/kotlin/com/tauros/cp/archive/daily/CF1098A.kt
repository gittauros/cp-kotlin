package com.tauros.cp.archive.daily

import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.common.string
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/3/12
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1098/A
    // 点权是和的差分，儿子的和和父亲的和作差可得当前点的取值最大值
    // 求出所有儿子给出的区间的交集，最大值如果小于0，那么不存在方案
    // 如果存在方案，那么取最大值，这样可以让儿子的值更小，因为可能有多个儿子，这样整体最小值就出来了
    val n = readln().toInt()
    val parents = readln().split(" ").map { it.toInt() - 1 }.toIntArray()
    val sum = readln().split(" ").map(string::toInt).toIntArray()

    val parent = iar(n) { -1 }
    val graph = ar(n) { mlo<int>() }
    for (i in 0 until n - 1) {
        parent[i + 1] = parents[i]
        graph[parents[i]].add(i + 1)
    }

    val vtx = iar(n) { if (it == 0) sum[0] else -1 }
    for (i in 1 until n) if (sum[i] == -1) {
        if (graph[i].isEmpty()) {
            vtx[i] = 0
            continue
        }
        val fa = parent[i]
        var rend = 1e9.toInt()
        for (v in graph[i]) {
            val max = sum[v] - sum[fa]
            rend = minOf(rend, max)
        }
        if (rend < 0) {
            println(-1)
            return
        }
        vtx[i] = rend; sum[i] = sum[fa] + rend
        for (v in graph[i]) vtx[v] = sum[v] - sum[i]
    }

    val ans = vtx.fold(0L, long::plus)
    println(ans)
}