package com.tauros.cp.archive.dp.bitmask

import com.tauros.cp.ar
import com.tauros.cp.iar
import kotlin.math.abs

/**
 * @author tauros
 * 2024/4/10
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1102/F
    // 学了羊解才会
    // 排列类的可以考虑状压，以后要记住这点
    val (n, m) = readln().split(" ").map { it.toInt() }
    val grid = ar(n) { readln().split(" ").map { it.toInt() }.toIntArray() }

    val inf = 0x3f3f3f3f
    // pre[i][j] 表示相邻两行上下分别为i和j时，最小的差值绝对值
    val pre = ar(n) { iar(n) { inf } }
    for (i in 0 until n) for (j in 0 until n) if (i != j) {
        for (k in 0 until m) {
            pre[i][j] = minOf(pre[i][j], abs(grid[i][k] - grid[j][k]))
        }
    }
    // ht[i][j] 表示第一行为i，最后一行为j时，最小的差值绝对值
    val ht = ar(n) { iar(n) { inf } }
    for (i in 0 until n) for (j in 0 until n) {
        for (k in 1 until m) {
            ht[i][j] = minOf(ht[i][j], abs(grid[i][k] - grid[j][k - 1]))
        }
    }

    // dp[mask][i][j] 表示已选择了mask，第一行为i，最后一行为j时最大的答案
    val maxState = 1 shl n
    val dp = ar(maxState) { ar(n) { iar(n) { -1 } } }
    for (i in 0 until n)
        dp[1 shl i][i][i] = if (n == 1) ht[i][i] else inf
    for (state in 1 until maxState) for (k in 0 until n) if (1 shl k and state == 0) {
        val nextState = 1 shl k or state
        for (i in 0 until n) for (j in 0 until n) {
            var res = minOf(dp[state][i][j], pre[j][k])
            if (nextState == maxState - 1) {
                res = minOf(res, ht[i][k])
            }
            dp[nextState][i][k] = maxOf(dp[nextState][i][k], res)
        }
    }

    val ans = dp[maxState - 1].maxOf { it.max() }
    println(ans)
}