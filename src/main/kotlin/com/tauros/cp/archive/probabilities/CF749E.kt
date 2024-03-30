package com.tauros.cp.archive.probabilities

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.dar
import com.tauros.cp.lar
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2024/3/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/749/E
    // 推了很久都不会，想按长度进行讨论，发现同长度子数组逆序对总数不会统计
    // 最终的做法是完全用概率来考虑贡献，按 i,j都在选中段内 和 i,j不都在选中段内 两种情况来讨论概率和贡献
    // https://www.luogu.com.cn/article/ev63p40w
    val n = rd.ni()
    val nums = rd.na(n).map { it - 1 }.toIntArray()

    val nested = (1 .. n).sumOf { it * (1L + n - it) * (n - it) / ((n + 1L) * n * 2.0) }

    fun LongArray.update(pos: int, add: long) = this.bitUpdateWithIndex(pos) { this[it] += add }
    fun LongArray.query(pos: int) = this.bitQuery(pos, 0L, long::plus)
    val notNested = run {
        val (bit1, bit2) = lar(n + 2) to lar(n + 2)
        var (part1, part2) = 0L to 0L
        for ((i, num) in nums.withIndex()) {
            part1 += bit1.query(n + 1) - bit1.query(num + 1)
            bit1.update(num + 1, 1)

            part2 -= (bit2.query(n + 1) - bit2.query(num + 1)) * 2 * (n - i)
            bit2.update(num + 1, i + 1L)
        }
        part1 + part2 / (n + 1.0) / n
    }

    val ans = nested + notNested
    wt.printf("%.12f\n", ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}