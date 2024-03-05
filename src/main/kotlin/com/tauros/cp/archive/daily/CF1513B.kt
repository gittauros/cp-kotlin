package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.withMod
import com.tauros.cp.math.ModComb

/**
 * @author tauros
 * 2024/3/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        val target = nums.reduce(int::and)
        withMod(1e9.toInt() + 7) {
            val comb = ModComb(n)

            val count = nums.count { it == target }
            if (count < 2) {
                wt.println(0)
                return@repeat
            }

            val ans = comb.a(count, 2) * comb.a(n - 2, n - 2)
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}