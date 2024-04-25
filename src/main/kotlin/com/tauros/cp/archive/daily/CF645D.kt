package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.so

/**
 * @author tauros
 * 2024/4/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/645/D
    // https://codeforces.com/problemset/submission/645/257717188
    // 拓扑排序分析下排出来的序列
    val (n, m) = rd.ni() to rd.ni()
    val graph = ar(n) { mlo<IIP>() }
    val deg = iar(n)
    repeat(m) {
        val (u, v) = rd.ni() - 1 to rd.ni() - 1
        graph[u].add(v to it)
        deg[v] += 1
    }

    val q = dq<int>()
    for (i in 0 until n) if (deg[i] == 0) q.add(i)
    val sorted = mlo<int>()
    var (ans, success) = -1 to true
    while (q.isNotEmpty()) {
        val size = q.size
        if (size > 1) {
            success = false; break
        }
        val u = q.removeFirst()
        sorted.add(u)
        for ((v, _) in graph[u]) if (--deg[v] == 0) q.add(v)
    }
    if (!success) {
        wt.println(ans)
        return
    }
    for ((u, v) in sorted.zipWithNext()) {
        var res = m
        for ((w, i) in graph[u]) if (w == v) {
            res = minOf(res, i)
        }
        ans = maxOf(ans, res)
    }
    wt.println("${ans + 1}")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}