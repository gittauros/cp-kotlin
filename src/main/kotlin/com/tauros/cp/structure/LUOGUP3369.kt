package com.tauros.cp.structure

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2024/3/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://www.luogu.com.cn/problem/P3369
    // 普通平衡树
    val n = rd.ni()
    val sortedList = IntBlockSortedList()
    repeat(n) {
        val op = rd.ni()
        if (op == 1) {
            val x = rd.ni()
            sortedList.add(x)
        } else if (op == 2) {
            val x = rd.ni()
            sortedList.remove(x)
        } else if (op == 3) {
            val x = rd.ni()
            val rank = sortedList.ceilingIndex(x) + 1
            wt.println(rank)
        } else if (op == 4) {
            val kth = rd.ni() - 1
            val x = sortedList[kth]
            wt.println(x)
        } else if (op == 5) {
            val x = rd.ni()
            val idx = sortedList.lowerIndex(x)
            val y = sortedList[idx]
            wt.println(y)
        } else if (op == 6) {
            val x = rd.ni()
            val idx = sortedList.higherIndex(x)
            val y = sortedList[idx]
            wt.println(y)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}