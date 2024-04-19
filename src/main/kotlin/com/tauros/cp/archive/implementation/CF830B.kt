package com.tauros.cp.archive.implementation

import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.mmo
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2024/4/13
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/830/B
    // 模拟实现
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toInt() }.toIntArray()

    val pos = mmo<int, MutableList<int>>()
    for (i in 0 until n) {
        pos.computeIfAbsent(nums[i]) { mlo() }.add(i)
    }
    val list = pos.toList().sortedBy { it.first }.map { it.second.toIntArray() }.toTypedArray()

    fun IntArray.update(pos: int) = this.bitUpdateWithIndex(pos) { this[it] += 1 }
    fun IntArray.query(pos: int) = this.bitQuery(pos, 0, int::plus)

    val bit = iar(n + 1)
    var ans = 0L
    fun transfer(from: int, to: int) {
        val res = if (to >= from) {
            val deleted = bit.query(to + 1) - bit.query(from + 1)
            to - from - deleted
        } else {
            val deleted = bit.query(n) - bit.query(from + 1) + bit.query(to + 1)
            n - (from - to) - deleted
        }
        bit.update(to + 1)
        ans += res
    }
    transfer(-1, list[0][0])
    var (numi, posi) = 0 to 0
    var pre = list[0][0]
    while (true) {
        for (j in posi + 1 until list[numi].size) {
            val next = list[numi][j]
            transfer(pre, next); pre = next
        }
        for (j in 0 until posi) {
            val next = list[numi][j]
            transfer(pre, next); pre = next
        }
        if (numi == list.size - 1) break
        val posj = findFirst(list[numi + 1].size) { list[numi + 1][it] > pre }
        numi += 1; posi = if (posj == list[numi].size) 0 else posj
        transfer(pre, list[numi][posi]); pre = list[numi][posi]
    }
    println(ans)
}