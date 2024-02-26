package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/1/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/309/C
    val (n, m) = rd.ni() to rd.ni()
    val cap = 30

    val vols = iar(cap)
    val weights = iar(cap)
    repeat(n) {
        var vol = rd.ni()
        while (vol > 0) {
            val lowBit = vol.takeLowestOneBit()
            vols[lowBit.countTrailingZeroBits()] += 1
            vol -= lowBit
        }
    }
    repeat(m) { weights[rd.ni()] += 1 }

    var ans = 0
    out@ for (i in 0 until cap) {
        var iter = weights[i]
        while (iter > 0) {
            if (vols[i] <= 0) {
                val split = (i + 1 until cap).firstOrNull { vols[it] > 0 } ?: i
                if (split > i) {
                    vols[split] -= 1
                    vols[i] += 2
                    for (j in split - 1 downTo i + 1) vols[j] += 1
                }
            }
            if (vols[i] <= 0) break@out
            vols[i] -= 1
            ans += 1
            iter -= 1
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}