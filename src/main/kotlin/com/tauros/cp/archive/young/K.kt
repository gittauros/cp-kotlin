package com.tauros.cp.archive.young

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.math.Prime

/**
 * @author tauros
 * 2025/3/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val prime = Prime(2e5.toInt())
    fun factors(num: int): List<int> {
        val list = mutableListOf<int>()
        var iter = num
        for (p in prime) {
            if (iter <= 1) break
            if (iter % p != 0) continue
            list.add(p)
            while (iter > 1 && iter % p == 0) {
                iter /= p
            }
        }
        return list
    }

    val cases = rd.ni()
    repeat(cases) {
        val (x, y) = rd.ni() to rd.ni()
        if (x == 1 || y == 1) {
            wt.println(-1)
            return@repeat
        }

        val xFac = factors(x)
        val yFac = factors(y)

        if (xFac[0] == yFac[0]) {
            wt.println(xFac[0])
        } else {
            val ans1 = xFac[0].toLong() * yFac[0]
            var ans2 = long.MAX_VALUE
            val set = yFac.toSet()
            for (fac in xFac) {
                if (fac in set) {
                    ans2 = fac.toLong()
                    break
                }
            }
            wt.println(minOf(ans1, ans2))
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}