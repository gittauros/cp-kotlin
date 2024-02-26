package com.tauros.cp.archive.pain

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.iar
import kotlin.math.abs

/**
 * @author tauros
 * 2024/1/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1868/B2
    // 先全当做收一个给一个，并统计可以只给一个或只收一个的个数
    // 做完一遍后看哪里还不能平账，使用可以只给一个或只收一个的来填平
    val cases = rd.ni()
    val cap = 31
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        val sum = nums.fold(0L, long::plus)
        if (sum % n != 0L) {
            wt.println("No")
            return@repeat
        }

        val avg = (sum / n).toInt()
        val flow = iar(cap)
        val (giveCnt, receiveCnt) = iar(cap) to iar(cap)
        fun collect(diff: int, op: int) {
            val lowBit = diff.takeLowestOneBit()
            flow[(diff + lowBit).countTrailingZeroBits()] += op
            flow[lowBit.countTrailingZeroBits()] -= op
            if (diff.countOneBits() == 1) {
                val b = diff.countTrailingZeroBits() + 1
                (if (op > 0) giveCnt else receiveCnt)[b] += 1
            }
        }
        for (num in nums) if (num != avg) {
            val diff = abs(num - avg)
            if ((diff + diff.takeLowestOneBit()).countOneBits() != 1) {
                wt.println("No")
                return@repeat
            }
            collect(diff, if (num > avg) 1 else -1)
        }

        var success = true
        for (b in cap - 1 downTo 0) if (flow[b] != 0) {
            val cnt = if (flow[b] < 0) receiveCnt else giveCnt
            if (b == 0 || abs(flow[b]) > abs(cnt[b])) {
                success = false
                break
            }
            flow[b - 1] += flow[b] * 2
            flow[b] = 0
        }
        wt.println(if (success) "Yes" else "No")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}