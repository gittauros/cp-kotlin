package com.tauros.cp.archive.funny

import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/3
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/870/D
    // 记录下 p[0~n-1] xor b[0] 和 b[0~n-1] xor p[0] 的值
    // 计算每个 p[i]与p[0] 和 b[i]与b[0] 的异或差值
    // 然后可以暴力尝试p[0]的n种值，再根据diff计算出对应的b和p数组，然后验证query的内容和数据是否超出范围即可
    fun ask(i: int, j: int): int {
        println("? $i $j")
        return readln().toInt()
    }

    val n = readln().toInt()

    val (diffP, askP) = run {
        val (diff, ask) = iar(n) to iar(n)
        for (i in 0 until n) ask[i] = ask(i, 0)
        for (i in n - 1 downTo 1) diff[i] = ask[i] xor ask[0]
        diff to ask
    }
    val (diffB, askB) = run {
        val (diff, ask) = iar(n) to iar(n)
        for (i in 0 until n) ask[i] = ask(0, i)
        for (i in n - 1 downTo 1) diff[i] = ask[i] xor ask[0]
        diff to ask
    }

    var (cnt, head) = 0 to 0
    run {
        val (p, b) = iar(n) to iar(n)
        out@ for (i in 0 until n) {
            p[0] = i
            for (j in 1 until n) p[j] = p[0] xor diffP[j]
            if (p.any { it !in 0 until n }) continue
            b[p[0]] = 0
            if (p[0] != 0) b[0] = diffB[p[0]]
            for (j in 1 until n) b[j] = b[0] xor diffB[j]
            if (b.any { it !in 0 until n }) continue
            for (j in 0 until n) if (b[p[j]] != j || askP[j] != p[j] xor b[0] || askB[j] != b[j] xor p[0]) continue@out
            head = i; cnt += 1
        }
    }
    val ans = iar(n) { if (it == 0) head else diffP[it] xor head }
    println("!\n$cnt")
    println(buildString { for (res in ans) append("$res ") })
}