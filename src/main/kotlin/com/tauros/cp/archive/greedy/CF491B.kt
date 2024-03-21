package com.tauros.cp.archive.greedy

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.iao
import kotlin.math.abs

/**
 * @author tauros
 * 2024/3/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/491/B
    // 想象一下一维的时候，判断点p和一个group的点的最大距离，只用考虑这个group最左和最右即可
    // 回到二维曼哈顿距离上，应该也是只用判断一个group有限的点的
    // 考虑从点p触发只能行走len，这个终点的图形就是由两条斜率为1和两条斜率为-1的线段包住的
    // 也就是只用考虑斜率为±1的最上和最下的直线切到的点，总共只用考虑四个点即可
    val (n, m) = rd.ni() to rd.ni()
    val c = rd.ni()
    val points = ar(c) { rd.na(2) }
    val h = rd.ni()
    val hotels = ar(h) { rd.na(2) }

    // 0: max b1, 1 min b1
    // 2: max b2, 3 min b2
    val bounds = ar(4) { iao(0, 0, n + m + 1) }
    for ((x, y) in points) {
        val b1 = y + x
        if (bounds[0][2] > n + m || bounds[0][2] < b1) {
            bounds[0][0] = x; bounds[0][1] = y; bounds[0][2] = b1
        }
        if (bounds[1][2] > n + m || bounds[1][2] > b1) {
            bounds[1][0] = x; bounds[1][1] = y; bounds[1][2] = b1
        }
        val b2 = y - x
        if (bounds[2][2] > n + m || bounds[2][2] < b2) {
            bounds[2][0] = x; bounds[2][1] = y; bounds[2][2] = b2
        }
        if (bounds[3][2] > n + m || bounds[3][2] > b2) {
            bounds[3][0] = x; bounds[3][1] = y; bounds[3][2] = b2
        }
    }

    fun IntArray.dist(other: IntArray) = abs(this[0] - other[0]) + abs(this[1] - other[1])
    fun IntArray.maxDist() = (0 until 4).maxOf { bounds[it].dist(this) }
    var ans = -1
    for (i in 0 until h) {
        val dist = hotels[i].maxDist()
        if (ans == -1 || dist < hotels[ans].maxDist()) {
            ans = i
        }
    }

    wt.println(hotels[ans].maxDist())
    wt.println("${ans + 1}")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}