package com.tauros.cp.archive.daily

import com.tauros.cp.bar

/**
 * @author tauros
 * 2024/3/25
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/986/B
    // 环内的任意两个点交换一次都会把环分成两个环（也就是环的个数增加1），两个环间交换一次会将两个环合并（环个数减少1）
    // x个点的置换环需要交换x-1次才能复原（环长可能为1，就是p[i]=i），顺序排列时相当于n个长为1的环
    // 考虑每交换一次环个数的奇偶性一定会变化，那么交换完后的 n-环数 的奇偶性一定和 交换次数 的奇偶性相同
    val n = readln().toInt()
    val p = readln().split(" ").map { it.toInt() - 1 }.toIntArray()

    val vis = bar(n)
    var loopCnt = 0
    for (i in 0 until n) if (!vis[p[i]]) {
        var iter = i
        do {
            vis[iter] = true
            iter = p[iter]
        } while (iter != i)
        loopCnt += 1
    }

    val o = (n - loopCnt) % 2
    println(if (o == n % 2) "Petr" else "Um_nik")
}