package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2023/11/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val str = rd.ns()
    val ans = buildString {
        var iter = 0
        val cnt = IntArray(3)
        while (iter < str.length) {
            val target = str[iter]
            val tail = buildString {
                while (iter < str.length && target - str[iter] >= 0) {
                    val c = str[iter++] - '0'
                    if (target == '2' && c != 1) append(c)
                    else cnt[c]++
                }
            }
            if (target == '2') {
                repeat(cnt[1]) { append(1) }
            } else {
                repeat(cnt[0]) { append(0) }
                repeat(cnt[1]) { append(1) }
            }
            append(tail)
            cnt.fill(0)
        }
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}