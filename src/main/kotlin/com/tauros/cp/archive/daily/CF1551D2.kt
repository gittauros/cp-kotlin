package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar
import kotlin.math.round

/**
 * @author tauros
 * 2023/12/9
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (n, m, k) = rd.na(3)
        val lower = n % 2 * m / 2
        val upper = m / 2 * n
        if (k !in lower .. upper) {
            wt.println("NO")
            return@repeat
        }
        if ((k - lower) % 2 == 1) {
            wt.println("NO")
            return@repeat
        }
        wt.println("YES")
        val grid = ar(n) { iar(m) { -1 } }
        if (m % 2 == 1) {
            for (i in 0 until n step 2) {
                grid[i][m - 1] = i / 2 % 2
                grid[i + 1][m - 1] = i / 2 % 2
            }
        }
        if (n % 2 == 1) {
            for (i in 0 until m step 2) {
                grid[n - 1][i] = i / 2 % 2
                grid[n - 1][i + 1] = i / 2 % 2
            }
        }
        var vertical = n * m / 2 - k - m % 2 * n / 2
        val (rows, cols) = n - n % 2 to m - m % 2
        for (i in 0 until rows step 2) {
            for (j in 0 until cols step 2) {
                if (vertical > 0) {
                    val (l, r) = i / 2 % 2 * 2 + 2 to i / 2 % 2 * 2 + 3
                    grid[i][j] = l
                    grid[i + 1][j] = l
                    grid[i][j + 1] = r
                    grid[i + 1][j + 1] = r
                    vertical -= 2
                } else {
                    val (u, d) = j / 2 % 2 * 2 + 6 to j / 2 % 2 * 2 + 7
                    grid[i][j] = u
                    grid[i][j + 1] = u
                    grid[i + 1][j] = d
                    grid[i + 1][j + 1] = d
                }
            }
            if (vertical == 0) break
        }
        val stRow = (0 until rows step 2).firstOrNull { grid[it][0] == -1 } ?: n
        for (i in stRow until rows step 2) {
            for (j in 0 until cols step 2) {
                val (u, d) = j / 2 % 2 * 2 + 10 to j / 2 % 2 * 2 + 11
                grid[i][j] = u
                grid[i][j + 1] = u
                grid[i + 1][j] = d
                grid[i + 1][j + 1] = d
            }
        }
        for (i in 0 until n) {
            for (j in 0 until m) wt.print('a' + grid[i][j])
            wt.println()
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}