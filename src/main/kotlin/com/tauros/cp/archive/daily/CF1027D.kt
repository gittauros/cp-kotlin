package com.tauros.cp.archive.daily

import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/30
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1027/D
    // 剥掉非环的部分，每个环求最小值相加
    val n = readln().toInt()
    val costs = readln().split(" ").map { it.toInt() }.toIntArray()
    val next = readln().split(" ").map { it.toInt() - 1 }.toIntArray()

    val deg = iar(n)
    for (i in 0 until n) deg[next[i]] += 1

    val q = dq<int>()
    for (i in 0 until n) if (deg[i] == 0) {
        q.add(i)
    }
    val vis = bar(n)
    while (q.isNotEmpty()) {
        val u = q.removeFirst()
        vis[u] = true
        if (--deg[next[u]] == 0) {
            q.addLast(next[u])
        }
    }

    var ans = 0
    for (i in 0 until n) if (!vis[i]) {
        var iter = i
        var res = int.MAX_VALUE
        do {
            res = minOf(res, costs[iter])
            vis[iter] = true
            iter = next[iter]
        } while (iter != i)
        ans += res
    }
    println(ans)
}