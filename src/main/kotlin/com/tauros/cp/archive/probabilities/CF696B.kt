package com.tauros.cp.archive.probabilities

import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.string
import com.tauros.cp.dar
import com.tauros.cp.iar
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2024/2/28
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/696/B
    // 考虑同一个父节点下的所有子节点，固定考虑当前节点为i，兄弟节点是j，父亲节点是u
    // 那么访问到i时一定会有exp[u] + 1，剩余部分考虑其它j节点排在i之前的期望，答案就是：exp[u] + exp{ j 在 i 之前 } + 1
    // 直接算期望很不好算，结合概率一起来看
    // u所有的子节点的顺序是一个全排列，那么 j在i之前 和 j在i之后 的情况数是完全一样的，因此 j在i之前 的概率为50%
    // 所以i在排列中的排名就是sigma{size[j] * 50%} = 50% * sigma{size[j]} = 50% * (size[u] - size[i] - 1)
    // 所以答案就是：exp[u] + 50% * (size[u] - size[i] - 1) + 1
    val n = readln().toInt()
    val parents = readlnOrNull()?.split(" ")?.map { it.toInt() - 1 }?.toIntArray() ?: iar(0)

    val graph = ar(n) { mlo<int>() }
    for (i in 0 until n - 1) {
        val u = parents[i]
        graph[u].add(i + 1)
    }

    val (exp, size) = dar(n) to iar(n)
    fun dfs1(u: int) {
        size[u] = 1
        for (v in graph[u]) {
            dfs1(v)
            size[u] += size[v]
        }
    }
    dfs1(0)
    exp[0] = 1.0
    fun dfs2(u: int) {
        val chdCnt = graph[u].size
        if (chdCnt == 0) return
        for (v in graph[u]) {
            val sum = size[u] - size[v] - 1
            val avg = exp[u] + sum / 2.0 + 1
            exp[v] = avg
            dfs2(v)
        }
    }
    dfs2(0)

    println(buildString {
        for (e in exp) append(string.format("%.8f ", e))
    })
}