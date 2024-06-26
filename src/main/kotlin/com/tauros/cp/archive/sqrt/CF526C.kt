package com.tauros.cp.archive.sqrt

import com.tauros.cp.common.sqrt

/**
 * @author tauros
 * 2024/4/25
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/526/C
    // 如果 hr/wr >= hb/wb 则r的性价比更高
    // 也就是 hr*wb >= hb*wr
    // 但是因为只能整数个整数个的吃，会有余数，这个余数会导致浪费
    // 自己分析了很久，关于这个余数的函数和最大价值的函数都不是单调或者单峰的，无法二分/三分
    // 学了羊解原来是根号枚举
    // 假设r是性价比更低的那个，先全部拿r，然后将r逐步兑换成b
    // 由于 wr*wb = wb*wr 所以wb个r一定能兑换出wr个b，只要 cntR >= wb 就能一直兑换，直到 cntR < wb
    // 假设 wb <= c^0.5 那么枚举cntR=[0,c^0.5]即可
    // 假设 wb > c^0.5 那么 cntB <= c/wb <= c^0.5 枚举cntB=[0,c^0.5]即可
    // 反过来假设b是性价比更低的也是一样，因此只用分别枚举cntR和cntB在[0,c^0.5]即可
    val (c, hr, hb, wr, wb) = readln().split(" ").map { it.toInt() }

    var ans = 0L
    for (cnt in 0 .. sqrt(c.toLong())) {
        val rJoy = if (wr * cnt <= c) hr * cnt + (c - wr * cnt) / wb * hb else 0
        val bJoy = if (wb * cnt <= c) hb * cnt + (c - wb * cnt) / wr * hr else 0
        ans = maxOf(ans, rJoy, bJoy)
    }
    println(ans)
}