package com.tauros.cp.archive.daily

import com.tauros.cp.cao

/**
 * @author tauros
 * 2024/12/31
 */
fun main(args: Array<String>) {
    val n = readln().toInt()
    if (n % 2 == 1) {
        println(-1)
    } else {
        val ch = cao('w', 'b')
        print(buildString {
            for (i in 0 until n) {
                for (j in 0 until n) {
                    for (k in 0 until n) {
                        val bel = minOf(j, k, n - 1 - j, n - 1 - k)
                        val c = (bel and 1) xor (i and 1)
                        append(ch[c])
                    }
                    append('\n')
                }
                append('\n')
            }
        })
    }
}