package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, l, r, s) = rd.na(4)

        val len = r - l + 1
        val min = (1 + len) * len / 2
        val max = (n + n - len + 1) * len / 2
        if (s !in min .. max) {
            wt.println(-1)
            return@repeat
        }

        val res = iar(len) { it + 1 }
        var rest = s - res.sum()
        for (i in len - 1 downTo 0) {
            if (rest <= 0) break
            val cap = n - (len - (i + 1))
            val add = minOf(cap - res[i], rest)
            res[i] += add; rest -= add
        }
        val set = res.toSet()
        val others = (1 .. n).filter { it !in set }.toIntArray()
        var head = 0
        val left = buildList { repeat(l - 1) { add(others[head++]) } }
        val right = buildList { repeat(n - r) { add(others[head++]) } }

        val ans = left.toIntArray() + res + right.toIntArray()
        for (num in ans) wt.print("$num ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}