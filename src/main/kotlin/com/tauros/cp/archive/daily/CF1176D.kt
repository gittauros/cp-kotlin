package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.math.Prime
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2024/2/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val b = rd.na(2 * n).sorted().toIntArray()

    val primes = Prime(2750131)
    val deleted = bar(2 * n)
    val pos = mmo<int, ArrayDeque<int>>()
    for ((i, num) in b.withIndex()) {
        pos.computeIfAbsent(num) { dq() }.add(i)
    }

    val ans = buildList {
        for (i in 2 * n - 1 downTo 0) if (!deleted[i] && b[i] !in primes) {
            add(b[i])
            val delete = b[i] / primes.min(b[i])
            val j = pos[delete]!!.removeFirst()
            deleted[j] = true
        }
        for (i in 0 until 2 * n) if (!deleted[i] && b[i] in primes) {
            add(b[i])
            val delete = primes[b[i] - 1]
            val j = pos[delete]!!.removeFirst()
            deleted[j] = true
        }
    }

    for (res in ans) wt.print("$res ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}