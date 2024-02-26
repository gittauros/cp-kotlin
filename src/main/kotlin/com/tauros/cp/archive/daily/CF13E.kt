package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/11/3
 */
private val bufCap = 65536
//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val vtx = rd.na(n)
    val sz = 316
    val count = iar(n)
    val next = iar(n)
    val last = iar(n)
    fun calc(pos: Int) {
        val reach = pos + vtx[pos]
        if (reach >= n || reach / sz != pos / sz) {
            count[pos] = 1
            last[pos] = pos
            next[pos] = reach
        } else {
            last[pos] = last[reach]
            next[pos] = next[reach]
            count[pos] = count[reach] + 1
        }
    }
    for (i in n - 1 downTo 0) {
        calc(i)
    }
    for (cas in 0 until m) {
        val op = rd.ni()
        val a = rd.ni() - 1
        if (op == 0) {
            val b = rd.ni()
            vtx[a] = b
            for (i in a downTo (a - a % sz)) {
                calc(i)
            }
        } else {
            var (cnt, iter) = 0 to a
            var pos = iter
            while (iter < n) {
                cnt += count[iter]
                pos = last[iter]
                iter = next[iter]
            }
            wt.println("${pos + 1} $cnt")
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}