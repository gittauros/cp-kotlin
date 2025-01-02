package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import java.util.TreeMap

/**
 * @author tauros
 * 2024/1/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1690/G
    // 除了这个解法的线段树操作外，有更简单的做法：
    // https://codeforces.com/contest/1690/submission/159898395
    // 用TreeSet维护火车头，每次操作过后如果查找小于k的i，如果i的速度小于等于当前k的速度，那么不用操作
    // 否则将k放入set，然后逐个查找大于k的坐标，如果速度大于等于k就删掉
    // 由于初始状态最多有n个点，m次操作最多放入m个点，所以最多删除n + min(n, m)个点，因此复杂度是mlogn的
    val cases = rd.ni()
    repeat(cases) {
        val (n, m) = rd.ni() to rd.ni()
        val a = rd.na(n)
        val vMap = TreeMap<int, int>()
        fun addCarriage(i: int, v: int) {
            if (vMap.isEmpty() || vMap.firstKey() >= i || vMap.lowerEntry(i).value > v) {
                vMap[i] = v
            } else return
            var iter = i
            while (vMap.isNotEmpty() && vMap.lastKey() > iter) {
                val higher = vMap.higherEntry(iter)
                iter = higher.key; val nextV = higher.value
                if (nextV >= v) {
                    vMap.remove(iter)
                } else break
            }
        }
        for ((i, v) in a.withIndex()) {
            addCarriage(i, v)
        }

        repeat(m) {
            val (k, d) = rd.ni() - 1 to rd.ni()
            a[k] -= d
            addCarriage(k, a[k])

            wt.print(vMap.size)
            wt.print(" ")
        }
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}