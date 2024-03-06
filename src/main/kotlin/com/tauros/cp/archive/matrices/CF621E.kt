package com.tauros.cp.archive.matrices

import com.tauros.cp.ar
import com.tauros.cp.common.ma
import com.tauros.cp.common.mm
import com.tauros.cp.common.string
import com.tauros.cp.common.withMod
import com.tauros.cp.iar
import com.tauros.cp.math.Matrix

/**
 * @author tauros
 * 2024/3/6
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/621/E
    // 一开始还讨论了每个10^y对x取模的周期，分组进行转移，结果搞了个x^4的复杂度T了
    // 回头想发现直接添加数位转移更方便，每个转移就是选前一个数的状态乘10再加最后一个数位
    // 复杂度x^3*logb
    val (n, b, k, x) = readln().split(" ").map(string::toInt)
    val a = readln().split(" ").map(string::toInt)

    val cnt = iar(10)
    for (num in a) cnt[num - 1] += 1

    withMod(1e9.toInt() + 7) {
        val transferArr = ar(x) { iar(x) }
        withMod(x) {
            for (from in 0 until x) for (num in 1..9) if (cnt[num - 1] > 0) {
                val to = from mm 10 ma num
                transferArr[to][from] += cnt[num - 1]
            }
        }
        val transferMat = Matrix(transferArr).pow(b - 1)
        val initArr = iar(x)
        for (num in 1 .. 9) if (cnt[num - 1] > 0) {
            initArr[num % x] += cnt[num - 1]
        }
        val init = Matrix(x, 1, initArr)
        val ans = transferMat * init
        println(ans[k][0])
    }
}