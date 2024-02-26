package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.graph.Graph
import com.tauros.cp.iar
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2024/1/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1270/G
    // i为1-indexed
    // a[i] <= i - 1 && a[i] >= i - n
    // 变换后为 i - a[i] >= 1 && i - a[i] <= n
    // 考虑建图从 i -> i - a[i]
    // 这个图每个节点有一条出边，必定存在环，考虑把环上的边列出来
    // i1 -> i1 - a[i1]
    // i2 -> i2 - a[i2]
    // ...
    // ik -> ik - a[ik]
    // 其中 ik - a[ik] == i1，构成一个环
    // 将左右两边分别求和即为 左边：i1 + i2 ... + ik 右边：i1 - a[i1] + i2 - a[i2] ... + ik - a[ik]
    // 由于 i1 - a[i1] = i2, i2 - a[i2] = i3, ... ik - a[ik] = i1，因此左右两式求和相等
    // 即∑i = ∑(i - a[i])，同时去掉i的部分，即为∑a[i] = 0
    // 因此只需找出一个环即可，由于出度为1，任一一个点走出去一定会找到环
    val cases = rd.ni()
    val cap = 1e6.toInt()
    val next = iar(cap + 1)
    val vis = bar(cap + 1)
    repeat(cases) {
        val n = rd.ni()
        for (i in 1 .. n) {
            next[i] = i - rd.ni()
            vis[i] = false
        }

        val ans = dq<int>()
        var iter = 1
        while (!vis[iter]) {
            vis[iter] = true
            ans.addLast(iter)
            iter = next[iter]
        }
        while (ans.first() != iter) ans.removeFirst()

        wt.println(ans.size)
        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}