package com.tauros.cp.archive.greedy

import com.tauros.cp.common.IIP
import java.util.PriorityQueue

/**
 * @author tauros
 * 2024/4/18
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1310/A
    // 没想出来。。。
    val n = readln().toInt()
    val c = readln().split(" ").map { it.toInt() }.toIntArray()
    val t = readln().split(" ").map { it.toInt() }.toIntArray()

    val idxes = (0 until n).sortedBy { c[it] }.toIntArray()
    var (ans, cur) = 0L to 0
    val pq = PriorityQueue<IIP> { a, b -> -a.first.compareTo(b.first) }
    for (i in idxes) {
        val (curCost, curNum) = t[i] to c[i]
        while (pq.isNotEmpty() && cur < curNum) {
            val (cost, num) = pq.poll()
            ans += (cur - num).toLong() * cost
            cur += 1
        }
        if (pq.isEmpty()) cur = curNum
        pq.offer(curCost to curNum)
    }
    while (pq.isNotEmpty()) {
        val (cost, num) = pq.poll()
        ans += (cur - num).toLong() * cost
        cur += 1
    }
    println(ans)
}