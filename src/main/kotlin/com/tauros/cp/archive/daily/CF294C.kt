package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod
import com.tauros.cp.math.Comb
import com.tauros.cp.math.ModComb

/**
 * @author tauros
 * 2023/12/12
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    withMod(1000000007) {
        val (n, m) = rd.ni() to rd.ni()
        val comb = ModComb(n)
        val starts = rd.na(m).sortedArray()
        var pre = 0
        var total = 0
        val segs = buildList {
            for (pos in starts) {
                add(pre + 1 to pos - 1)
                val len = pos - pre - 1
                total += len
                pre = pos
            }
            add(pre + 1 to n)
            val len = n - pre
            total += len
        }
        var ans = 1.toMInt()
        for ((l, r) in segs) {
            if (l > r) continue
            val len = r - l + 1
            val res = if (l == 1 || r == n) 1.toMInt()else 2.toMInt().pow(len - 1)
            ans *= res * comb.c(total, len)
            total -= len
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}