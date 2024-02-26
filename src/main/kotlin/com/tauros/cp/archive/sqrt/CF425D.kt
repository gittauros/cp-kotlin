package com.tauros.cp.archive.sqrt

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.mmo
import com.tauros.cp.mso
import com.tauros.cp.structure.IntList

/**
 * @author tauros
 * 2024/2/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/425/D
    val n = rd.ni()
    val points = ar(n) { rd.na(2) }

    val xMap = mmo<int, IntList>()
    for ((x, y) in points) xMap.computeIfAbsent(x) { IntList() }.add(y)

    val sq = 400
    fun idx(x: int, y: int) = x * (1e5.toLong() + 1) + y

    val inLineSet = mso<long>()
    val (leXList, gtXList) = IntList() to IntList()
    for ((x, yList) in xMap) {
        yList.sort()
        if (yList.size > sq) {
            gtXList.add(x)
            for (y in yList) inLineSet.add(idx(x, y))
        } else leXList.add(x)
    }
    gtXList.sort()
    leXList.sort()

    var ans = 0L
    // 枚举x值相同的垂直线上两点
    for (x in leXList) {
        val yList = xMap[x]!!
        for (i in yList.indices) for (j in i + 1 until yList.size) {
            val (down, up) = yList[i] to yList[j]
            fun calc(target: int) {
                if (idx(target, down) in inLineSet &&
                    idx(target, up) in inLineSet) {
                    ans += 1
                }
            }
            val len = up - down
            // 计算右边是为了统计 左边<sq的纵轴 和 右边>=sq的纵轴组成的方形
            calc(x - len); calc(x + len)
        }
        for (y in yList) inLineSet.add(idx(x, y))
    }
    // 枚举两个x值垂直线上的
    for (l in gtXList.indices) {
        val lx = gtXList[l]
        val betweenLineSet = mso<long>()
        for (y in xMap[lx]!!) betweenLineSet.add(idx(lx, y))
        for (r in l + 1 until gtXList.size) {
            val rx = gtXList[r]
            val len = rx - lx
            for (y in xMap[rx]!!) {
                // 枚举右上角
                if (idx(rx, y - len) in betweenLineSet &&
                    idx(lx, y) in betweenLineSet &&
                    idx(lx, y - len) in betweenLineSet) {
                    ans += 1
                }
                betweenLineSet.add(idx(rx, y))
            }
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}