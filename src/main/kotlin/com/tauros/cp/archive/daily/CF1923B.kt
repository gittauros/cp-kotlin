package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.lar
import kotlin.math.abs

/**
 * @author tauros
 * 2024/2/23
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
        val health = rd.na(n)
        val pos = rd.na(n)

        val sum = lar(n + 1)
        for (i in 0 until n) {
            sum[abs(pos[i])] += health[i].toLong()
        }
        var success = true
        for (i in 1 .. n) {
            sum[i] += sum[i - 1]
            if (k.toLong() * i < sum[i]) {
                success = false
                break
            }
        }
        wt.println(if (success) "YES" else "NO")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}