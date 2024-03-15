package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.common.string
import com.tauros.cp.geometry.IPoint2
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2024/3/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/660/D
    // 把带模长的向量哈希一下，统计数量
    // 注意保证向量的正反关系，需要排序保证下
    val n = rd.ni()
    val points = ar(n) { IPoint2(rd.ni(), rd.ni()) }

    points.sortWith { a, b -> if (a.x == b.x) a.y.compareTo(b.y) else a.x.compareTo(b.x) }

    val cap = 1e9.toInt()
    val base = cap * 2L + 2
    val cnt = mmo<long, int>().default { 0 }
    var ans = 0L
    for (i in 0 until n) {
        for (j in 0 until i) {
            val vector = points[j] - points[i]
            val (x, y) = vector
            val (mx, my) = x + cap to y + cap
            val id = mx * base + my
            ans += cnt[id]
            cnt[id] += 1
        }
    }
    wt.println(ans / 2)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}