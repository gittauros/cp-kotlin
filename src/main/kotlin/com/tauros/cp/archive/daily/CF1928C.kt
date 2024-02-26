package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.mso

/**
 * @author tauros
 * 2024/2/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    fun fraction(num: int): List<int> {
        return buildList {
            var iter = 1
            while (iter * iter <= num) {
                if (num % iter == 0) {
                    add(iter)
                    if (iter * iter != num) add(num / iter)
                }
                iter += 1
            }
        }.sorted()
    }
    repeat(cases) {
        val (n, x) = rd.na(2)

        val valid = maxOf(2, 2 * x - 2) until 2 * n - 2
        val ans = mso<int>()
        if (x >= 1) {
            val divs = fraction(n - x)
            for (div in divs) if (div % 2 == 0 && div in valid) ans.add(div)
        }
        if (x >= 2) {
            val divs = fraction(n + x - 2)
            for (div in divs) if (div % 2 == 0 && div in valid) ans.add(div)
        }
        wt.println(ans.size)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}