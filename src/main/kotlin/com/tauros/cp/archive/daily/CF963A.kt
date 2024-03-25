package com.tauros.cp.archive.daily

import com.tauros.cp.common.ma
import com.tauros.cp.common.md
import com.tauros.cp.common.mm
import com.tauros.cp.common.ms
import com.tauros.cp.common.withMod

/**
 * @author tauros
 * 2024/3/25
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/963/A
    // 以为是二项式定理啥的，想了N久发现直接求就好
    // 服了，等比数列求和公式是：a1*(1-q^n)/(1-q)
    // 注意判断下公比是不是1
    val (n, a, b, k) = readln().split(" ").map { it.toInt() }
    val seq = readln().toCharArray()

    withMod(1e9.toInt() + 9) {
        var ans = 0
        val q = pow(b.toLong(), k.toLong()) md pow(a.toLong(), k.toLong())
        for (i in 0 until k) {
            val first = pow(a.toLong(), n - i.toLong()) mm pow(b.toLong(), i.toLong())
            val cnt = (n + 1) / k
            val sum = if (q == 1) first mm cnt
                else first mm (pow(q.toLong(), cnt.toLong()) ms 1) md (q ms 1)
            ans = sum mm (if (seq[i] == '-') -1 else 1) ma ans
        }
        println(ans)
    }
}