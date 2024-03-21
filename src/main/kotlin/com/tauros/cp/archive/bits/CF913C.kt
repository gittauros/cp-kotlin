package com.tauros.cp.archive.bits

import com.tauros.cp.common.long
import com.tauros.cp.common.string
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/3/21
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/913/C
    // 不会，乱搞试试
    // 还真是这么做的，考虑算出每个bit最小的花费
    // 然后刚好买L的答案能算出来，接下来考虑大于L的情况
    // 考虑L每一位为0的bit，将其置为1，低位置为0，这时新的数字为M，肯定M>L
    // 并且这个M会是这个bit为1时最小的M，计算其花费，这些M的答案和刚好L的答案都取min即可
    val (n, l) = readln().split(" ").map(string::toInt)
    val cost = readln().split(" ").map(string::toInt).toIntArray()

    val (cap, inf) = 31 to 0x3f3f3f3f3f3f3f3fL
    val minCost = lar(cap) { inf }
    for (i in 0 until cap) {
        minCost[i] = minOf(if (i < n) cost[i].toLong() else inf, if (i == 0) inf else minCost[i - 1] * 2)
    }

    val res = (0 until cap).map { if (1 shl it and l != 0) minCost[it] else 0 }.fold(0L, long::plus)
    var (ans, pre) = res to 0L
    for (i in 0 until cap) {
        if (1 shl i and l == 0) {
            ans = minOf(ans, res - pre + minCost[i])
        } else pre += minCost[i]
    }
    println(ans)
}