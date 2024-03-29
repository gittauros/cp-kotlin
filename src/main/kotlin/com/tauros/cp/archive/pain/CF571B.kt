package com.tauros.cp.archive.pain

import com.tauros.cp.ar
import com.tauros.cp.common.string
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/3/20
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/571/B
    // 想出来了贪心，但是没想到nk到k平方的转换
    val (n, k) = readln().split(" ").map(string::toInt)
    val nums = readln().split(" ").map(string::toInt).toIntArray()

    // 排序后选出k段，每段贡献是收尾差值绝对值
    nums.sort()
    // 有 n mod k 段的点数是 n/k+1 个，其余是 n/k 个
    // 多一个的称为长段，普通的称为短段
    val (less, moreCap) = n / k to n % k
    val inf = 0x3f3f3f3f3f3f3f3fL
    // dp[i][j]状态是，选了i组，其中j组是长段
    // 由于前面已经选了i组，并且选了j组长段，那么前i组的长度能直接推出
    // 又由于能刚好把n个数分成k组，所以第i+1组可以O1的选择其终点（往长段转移 或 往短段转移），这样就做完了
    val dp = ar(2) { lar(moreCap + 1) { inf } }
    dp[0][0] = 0
    var cur = 0
    repeat(k) { chosen ->
        val pre = cur; cur = 1 - cur
        dp[cur].fill(inf)

        for (moreCnt in 0 .. minOf(chosen, moreCap)) {
            val lessCnt = chosen - moreCnt
            val first = (less + 1) * moreCnt + less * lessCnt
            dp[cur][moreCnt] = minOf(dp[cur][moreCnt], dp[pre][moreCnt] + nums[first + less - 1] - nums[first])
            if (moreCnt + 1 <= moreCap) {
                dp[cur][moreCnt + 1] = minOf(dp[cur][moreCnt + 1], dp[pre][moreCnt] + nums[first + less] - nums[first])
            }
        }
    }
    val ans = dp[cur][moreCap]
    println(ans)
}