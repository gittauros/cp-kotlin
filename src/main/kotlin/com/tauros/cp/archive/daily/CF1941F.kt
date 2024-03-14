package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.mo

/**
 * @author tauros
 * 2024/3/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/contest/1941/problem/F
    val cases = rd.ni()
    repeat(cases) {
        val (n, m, k) = rd.na(3)
        val nums = rd.na(n)
        val models = rd.na(m)
        val functions = rd.na(k).sortedArray()

        fun diff(right: int) = nums[right].toLong() - nums[right - 1]
        var (m1, m2) = -1 to -1
        for (i in 1 until n) {
            val diff = diff(i)
            if (m1 == -1 || diff > diff(m1)) {
                m2 = m1; m1 = i
            } else if (m2 == -1 || diff > diff(m2)) {
                m2 = i
            }
        }

        val (m1Diff, m2Diff) = diff(m1) to if (m2 == -1) Long.MAX_VALUE else diff(m2)
        var ans = m1Diff
        if (m2 != -1 && m1Diff == m2Diff) {
            wt.println(ans)
            return@repeat
        }
        val target = nums[m1].toLong() + nums[m1 - 1] shr 1
        for (model in models) {
            fun check(i: int) {
                if (i !in 0 until k) return
                val mid = model + functions[i].toLong()
                val max = maxOf(mid - nums[m1 - 1], nums[m1] - mid)
                ans = minOf(ans, if (m2 == -1) max else maxOf(m2Diff, max))
            }
            val i = findFirst(k) { functions[it] + model > target }
            check(i); check(i - 1)
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}