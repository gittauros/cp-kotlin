package com.tauros.cp.archive.daily

import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/3/6
 */
fun main(args: Array<String>) {
    val cases = readln().toInt()
    repeat(cases) {
        val (n, x) = readln().split(" ").map(string::toLong)

        val xLowMask = x.takeLowestOneBit() - 1
        val nHigh = xLowMask.inv() and n
        val xHigh = xLowMask.inv() and x
        if (nHigh != xHigh) {
            println(-1)
            return@repeat
        }

        val nLow = xLowMask and n
        if (nLow.takeHighestOneBit() shl 1 and n != 0L) {
            println(-1)
            return@repeat
        }
        val inc = (nLow.takeHighestOneBit() shl 1) - nLow
        val m = n + inc
        println(m)
    }
}