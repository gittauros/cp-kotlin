package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
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
        val (n, m, k) = rd.na(3)
        val rest = k - (n - 1 + m - 1)
        if (rest < 0 || rest % 2 == 1) {
            wt.println("NO")
            return@repeat
        }
        wt.println("YES")
        val rows = ar(n) { bar(m - 1) }
        val cols = ar(n - 1) { bar(m) }
        for (i in 0 until m - 1 step 2) rows[0][i] = true
        for (i in (if (rows[0][m - 2]) 1 else 0) until n - 1 step 2) cols[i][m - 1] = true
        if (rest != 0) {
            val last = cols[n - 3][m - 1]
            rows[n - 2][m - 3] = last
            rows[n - 2][m - 2] = last xor true
            rows[n - 1][m - 3] = last
            rows[n - 1][m - 2] = last xor true
            cols[n - 2][m - 3] = last xor true
            cols[n - 2][m - 2] = last
            cols[n - 2][m - 1] = last
        }
        for (i in 0 until n) {
            for (j in 0 until m - 1) wt.print(if (rows[i][j]) "R " else "B ")
            wt.println()
        }
        for (i in 0 until n - 1) {
            for (j in 0 until m) wt.print(if (cols[i][j]) "R " else "B ")
            wt.println()
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}