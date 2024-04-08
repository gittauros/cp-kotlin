package com.tauros.cp.archive.revert

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.car
import com.tauros.cp.structure.IIHeap

/**
 * @author tauros
 * 2024/3/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/3/D
    // 反悔贪心，正确性在于每个前缀都必须满足括号差大于等于0
    // 因此前缀的花费考虑优先级比后缀的高，所以在最低限度满足前缀条件时可以是最优的
    val str = rd.ns()
    val m = str.count { it == '?' }
    val cost = ar(m) { rd.na(2) }

    var head = 0
    val heap = IIHeap()
    var (top, ans) = 0 to 0L
    val chars = car(str.length)
    for ((i, c) in str.withIndex()) {
        if (c == '?') {
            val (l, r) = cost[head++]
            ans += r
            heap.offer(l - r to i)
            top -= 1
        } else {
            top += if (c == '(') 1 else -1
        }
        chars[i] = if (c == '(') '(' else ')'
        while (top < 0 && heap.isNotEmpty()) {
            val (add, pos) = heap.poll()
            ans += add
            chars[pos] = '('
            top += 2
        }
        if (top < 0) break
    }

    if (top != 0) {
        wt.println(-1)
    } else {
        wt.println(ans)
        wt.println(String(chars))
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}