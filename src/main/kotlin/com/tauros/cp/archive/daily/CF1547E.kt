package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/26
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()
        val pos = rd.na(k)
        val temp = rd.na(k)

        val idx = (0 until k).sortedBy { pos[it] }.toIntArray()
        val ans = iar(n) { INF }
        var (min, j) = INF to 0
        for (i in 0 until n) {
            while (j < k && pos[idx[j]] <= i + 1) {
                min = minOf(min, temp[idx[j]] - pos[idx[j]])
                j += 1
            }
            ans[i] = minOf(ans[i], min + i + 1)
        }
        min = INF; j = k - 1
        for (i in n - 1 downTo 0) {
            while (j >= 0 && pos[idx[j]] >= i + 1) {
                min = minOf(min, temp[idx[j]] - (n + 1 - pos[idx[j]]))
                j -= 1
            }
            ans[i] = minOf(ans[i], min + (n - i))
        }

        for (res in ans) wt.print("$res ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}