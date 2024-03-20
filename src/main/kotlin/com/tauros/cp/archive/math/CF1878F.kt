package com.tauros.cp.archive.math

import com.tauros.cp.common.int
import com.tauros.cp.common.sqrt
import com.tauros.cp.common.string
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2024/3/19
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1878/F
    // d()是积性函数，当gcd(n, a)=1s时，d(n*a)=d(n)*d(a)
    // 然后d(n)*d(a)=n，那么d(n)一定是n的约数
    // d可以用质因数分解后求下组合数，很好做
    fun frac(num: int) = buildMap {
        var iter = num
        for (i in 2 .. sqrt(num)) {
            if (iter % i == 0) {
                var cnt = 0
                while (iter % i == 0) {
                    iter /= i; cnt += 1
                }
                put(i, cnt)
            }
        }
        if (iter > 1) put(iter, 1)
    }.default { 0 }
    val cases = readln().toInt()
    repeat(cases) {
        val (n, q) = readln().split(" ").map(string::toInt)

        val initFrac = frac(n)
        val curFrac = mmo<int, int>().default { 0 }.apply { putAll(initFrac) }
        println(buildString {
            repeat(q) {
                val line = readln().split(" ").map(string::toInt)
                if (line.size == 1) {
                    curFrac.clear()
                    curFrac.putAll(initFrac)
                } else {
                    val x = line[1]
                    val xFrac = frac(x)
                    for ((p, cnt) in xFrac) curFrac[p] += cnt

                    val d = curFrac.map { (_, cnt) -> cnt + 1 }.fold(1, int::times)
                    val dFrac = frac(d)
                    val ans = dFrac.all { (p, cnt) -> cnt <= curFrac[p] }
                    append(if (ans) "YES" else "NO")
                    append("\n")
                }
            }
        })
    }
}