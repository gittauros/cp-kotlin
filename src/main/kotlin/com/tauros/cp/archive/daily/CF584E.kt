package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iar
import kotlin.math.abs

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
    // https://codeforces.com/problemset/problem/584/E
    val n = rd.ni()
    val (p, s) = rd.na(n).map { it - 1 }.toIntArray() to rd.na(n).map { it - 1 }.toIntArray()

    val should = iar(n)
    for ((i, num) in s.withIndex()) should[num] = i

    var ans = 0
    var ops = 0
    val out = buildString {
        val cur = iar(n)
        for ((i, num) in p.withIndex()) cur[num] = i
        fun swap(i: int, j: int) {
            append("${i + 1} ${j + 1}\n")
            ops += 1
            ans += abs(i - j)
            val (x, y) = p[i] to p[j]
            p[j] = x; p[i] = y; cur[x] = j; cur[y] = i
        }

        val stack = iar(n)
        for (i in 0 until n) if (cur[s[i]] != i) {
            // 不一定要用单调栈来优化，直接贪心换答案也是正确的，ans依然保证最少。
            // 用了单调栈ops比直接贪心换稍微少点，输出量小些可以稍微快些
            var top = -1
            for (j in cur[s[i]] - 1 downTo i) {
                while (top >= 0 && should[p[j]] > should[p[stack[top]]]) top--
                stack[++top] = j
            }
            var iter = cur[s[i]]
            for (k in 0..top) {
                swap(iter, stack[k])
                iter = stack[k]
            }
        }
    }
    wt.println(ans)
    wt.println(ops)
    wt.print(out)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}