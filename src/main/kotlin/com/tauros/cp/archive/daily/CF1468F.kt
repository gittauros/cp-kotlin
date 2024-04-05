package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ao
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.string
import com.tauros.cp.geometry.IPoint2
import com.tauros.cp.math.FracLong
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2024/4/4
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1468/F
    // 匹配一下反向的向量数量就可以
    val cases = rd.ni()
    fun id(x: FracLong, y: FracLong) = "$x#$y"
    val neg = FracLong(-1, 1)
    repeat(cases) {
        val n = rd.ni()
        val vectors = ar(n) {
            val (x, y, u, v) = rd.na(4)
            val vector = IPoint2(u, v) - IPoint2(x, y)
            val len2 = vector.len2()
            val (a, b) = vector
            ao(FracLong(a.toLong() * a * (if (a < 0) -1 else 1), len2), FracLong(b.toLong() * b * (if (b < 0 ) -1 else 1), len2))
        }
        val cnt = mmo<string, int>().default { 0 }
        var ans = 0L
        for ((a, b) in vectors) {
            val find = id(a * neg, b * neg)
            ans += cnt[find]
            cnt[id(a, b)] += 1
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}