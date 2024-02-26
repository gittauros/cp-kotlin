package com.tauros.cp.archive.monotonic

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.MInt
import com.tauros.cp.common.withMod
import com.tauros.cp.iar
import com.tauros.cp.miar

/**
 * @author tauros
 * 2023/12/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1913/D
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        withMod(998244353) {
            val stack = iar(n)
            var top = -1
            val sum = miar(n + 1)
            val dp = miar(n)

            var inStack = MInt.ZERO
            for (i in 0 until n) {
                while (top >= 0 && nums[i] < nums[stack[top]]) {
                    inStack -= dp[stack[top--]]
                }

                val pre = if (top == -1) 0 else stack[top] + 1
                dp[i] += sum[i] - sum[pre]
                dp[i] += inStack
                if (top == -1) dp[i] += MInt.ONE
                sum[i + 1] = sum[i] + dp[i]

                stack[++top] = i
                inStack += dp[i]
            }
            wt.println(inStack)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}