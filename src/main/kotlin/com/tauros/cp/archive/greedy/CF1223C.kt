package com.tauros.cp.archive.greedy

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ao
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.lcm
import com.tauros.cp.common.long
import com.tauros.cp.lao

/**
 * @author tauros
 * 2024/12/30
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/submission/1223/299053972
    val q = rd.ni()
    repeat(q) { case ->
        val n = rd.ni()
        val p = rd.na(n).sortedDescending()
        val (x, a) = rd.nl() to rd.nl()
        val (y, b) = rd.nl() to rd.nl()
        val k = rd.nl() * 100

        val sums = p.runningFold(0L, long::plus)
        val lcm = lcm(a, b)

        val ans = findFirst(1, n + 1) { choose ->
            val lcmCnt = (choose / lcm).toInt()
            val xCnt = (choose / a).toInt() - lcmCnt
            val yCnt = (choose / b).toInt() - lcmCnt
            val (big, small) = ao(x to xCnt, y to yCnt)
                .sortedByDescending { it.first }

            val baseSum = (x + y) * sums[lcmCnt]
            val bigSum = big.first * (sums[lcmCnt + big.second] - sums[lcmCnt])
            val smallSum = small.first * (sums[lcmCnt + big.second + small.second] - sums[lcmCnt + big.second])
            val res = baseSum + bigSum + smallSum
            res >= k
        }
        wt.println(if (ans > n) -1 else ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}