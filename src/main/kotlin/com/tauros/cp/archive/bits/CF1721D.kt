package com.tauros.cp.archive.bits

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.mmo
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2024/3/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1721/D
    val cases = rd.ni()
    val cap = 29
    repeat(cases) {
        val n = rd.ni()
        val a = rd.na(n)
        val b = rd.na(n)

        var mask = 0
        val cnt = mmo<int, int>().default { 0 }
        for (i in cap downTo 0) {
            mask = 1 shl i xor mask
            for (num in b) cnt[num and mask] += 1
            var success = true
            for (num in a) {
                val ma = num and mask
                val target = mask xor ma
                if (cnt[target] == 0) {
                    success = false
                    break
                }
                cnt[target] -= 1
            }
            if (!success) mask = 1 shl i xor mask
            cnt.clear()
        }
        wt.println(mask)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}