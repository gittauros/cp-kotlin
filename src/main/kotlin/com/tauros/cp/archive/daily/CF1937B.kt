package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.IIP
import com.tauros.cp.dq
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.mso

/**
 * @author tauros
 * 2024/2/29
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val grid = ar(2) { rd.ns(n) }
        val str = buildString {
            var (i, j) = 0 to 0
            append(grid[i][j])
            while (i != 1 || j != n - 1) {
                if (j < n - 1 && (i == 0 && grid[0][j + 1] <= grid[1][j] || i == 1)) {
                    j += 1
                } else {
                    i += 1
                }
                append(grid[i][j])
            }
        }
        val dp = ar(2) { lar(n) }
        dp[0][0] = 1L
        val q = dq<IIP>()
        q.addLast(0 to 0)
        val inq = mso<IIP>()
        inq.add(0 to 0)
        var idx = 1
        val ops = iao(0, 1, 0)
        while (q.isNotEmpty()) {
            repeat(q.size) {
                val (i, j) = q.removeFirst()
                for (o in 0 until 2) {
                    val (ni, nj) = i + ops[o] to j + ops[o + 1]
                    if (ni in 0 until 2 && nj in 0 until n) {
                        if (grid[ni][nj] == str[idx]) {
                            dp[ni][nj] += dp[i][j]
                            val next = ni to nj
                            if (next !in q) {
                                q.addLast(next)
                                inq.add(next)
                            }
                        }
                    }
                }
            }
            idx += 1
        }
        wt.println(str)
        wt.println(dp[1][n - 1])
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}