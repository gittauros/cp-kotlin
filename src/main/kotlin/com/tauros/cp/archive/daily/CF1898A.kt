package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import java.io.FileInputStream

/**
 * @author tauros
 * 2023/11/20
 */
private val bufCap = 65536

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()
        val str = rd.ns(n)
        val suf = iar(n)
        for (i in n - 1 downTo 0) suf[i] = (if (i == n - 1) 0 else suf[i + 1]) + if (str[i] == 'A') 0 else 1
        if (suf[0] == k) {
            wt.println(0)
        } else {
            for (i in 1 until n) {
                if (suf[i] + i == k) {
                    wt.println(1)
                    wt.println("$i B")
                    return@repeat
                }
                if (suf[i] == k) {
                    wt.println(1)
                    wt.println("$i A")
                    return@repeat
                }
            }
            if (n == k) {
                wt.println(1)
                wt.println("$n B")
                return@repeat
            }
            if (k == 0) {
                wt.println(1)
                wt.println("$n A")
                return@repeat
            }
            wt.println(2)
            wt.println("$n A")
            wt.println("$k B")
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}