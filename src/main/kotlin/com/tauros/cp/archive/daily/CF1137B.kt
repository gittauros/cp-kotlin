package com.tauros.cp.archive.daily

import com.tauros.cp.iar
import com.tauros.cp.string.pmt

/**
 * @author tauros
 * 2024/3/27
 */
fun main(args: Array<String>) {
    val s = readln()
    val t = readln()

    val cnt = iar(2)
    cnt[0] = s.count { it - '0' == 0 }
    cnt[1] = s.length - cnt[0]

    val pmt = t.pmt()
    val ans = buildString {
        var i = 0
        while (length < s.length) {
            val c = t[i] - '0'
            if (cnt[c] > 0) {
                cnt[c] -= 1
                append(t[i])
                if (++i >= t.length) i = pmt.last()
            } else {
                while (cnt[1 - c]-- > 0) append('0' + (1 - c))
            }
        }
    }
    println(ans)
}