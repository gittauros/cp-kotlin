package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.boolean
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.lo
import com.tauros.cp.structure.bitQuery
import com.tauros.cp.structure.bitUpdateWithIndex

/**
 * @author tauros
 * 2024/4/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1883/E
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        var (preNum, preTail) = nums[0] to 0L
        fun go(cur: int, target: int, eq: boolean = false): long {
            var (iter, cnt) = cur.toLong() to 0L
            while (iter < target || eq && iter == target.toLong()) {
                iter *= 2; cnt += 1
            }
            return cnt
        }
        var ans = 0L
        for (i in 1 until n) {
            val num = nums[i]
            if (num < preNum) {
                val tail = go(num, preNum)
                preTail += tail
            } else {
                val tail = go(preNum, num, true) - 1
                preTail = maxOf(0, preTail - tail)
            }
            ans += preTail
            preNum = num
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}