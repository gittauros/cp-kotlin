package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import java.io.FileInputStream

/**
 * @author tauros
 * 2023/11/18
 */
private val bufCap = 65536

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    val pow = IntArray(10)
    pow[0] = 1
    for (i in 1 .. 9) pow[i] = pow[i - 1] * 10
    repeat(cases) {
        val (s, n) = rd.ni() to rd.ni()
        if (n == 1) {
            wt.println(s)
            return@repeat
        }
        val nums = s.toString().map { it - '0' }.toIntArray()
        val x = nums.lastIndex
        val ans = IntArray(n)
        var rest = s
        var st = 0
        for (i in 0 .. x) {
            val b = x - i
            while (st < n && rest - pow[b] >= n - 1 - st) {
                ans[st++] += pow[b]
                rest -= pow[b]
            }
            if (st == n) {
                val r = (rest / (n.toLong() * pow[b])).toInt()
                if (r > 0) {
                    for (j in 0 until n) {
                        ans[j] += pow[b] * r
                        rest -= pow[b] * r
                    }
                }
                for (j in 0 until n) if (rest >= pow[b]) {
                    ans[j] += pow[b]
                    rest -= pow[b]
                } else break
            }
            if (rest == 0) break
        }
        wt.println(buildString {
            for (res in ans) append("$res ")
        })
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}