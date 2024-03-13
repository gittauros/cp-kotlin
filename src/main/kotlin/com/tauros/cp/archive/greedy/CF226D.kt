package com.tauros.cp.archive.greedy

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/12
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/226/D
    // 不会做，太神奇了
    // 每次找一个负数的行或者列，进行翻转
    // 考虑翻转后的矩阵总和一定是上升的，最小上升2（-1变成1）
    // 每次操作一定会让矩阵总和上升，由于值域是[-100, 100]，所以总和最大不会超过100*n*m
    // 从-100*n*m到100*n*m最多不会超过100*n*m次操作
    // 要么最后没有一个为负数的行或列，要么就最多做100*n*m次操作
    // 每次操作O(n)或O(m)，所以总复杂度是100*n*m*max(n, m)，最多不超过100的4次方
    val (n, m) = rd.ni() to rd.ni()
    val grid = ar(n) { rd.na(m) }

    val (rows, cols) = iar(n) to iar(m)
    for (i in 0 until n) for (j in 0 until m) {
        rows[i] += grid[i][j]
        cols[j] += grid[i][j]
    }

    val (rOp, cOp) = bar(n) to bar(m)
    while (true) {
        val r = (0 until n).firstOrNull { rows[it] < 0 } ?: -1
        if (r != -1) {
            rows[r] *= -1
            for (j in 0 until m) {
                cols[j] -= grid[r][j] shl 1
                grid[r][j] *= -1
            }
            rOp[r] = !rOp[r]
            continue
        }
        val c = (0 until m).firstOrNull { cols[it] < 0 } ?: -1
        if (c == -1) break
        cols[c] *= -1
        for (i in 0 until n) {
            rows[i] -= grid[i][c] shl 1
            grid[i][c] *= -1
        }
        cOp[c] = !cOp[c]
    }

    val ans1 = (0 until n).filter { rOp[it] }.map { it + 1 }
    val ans2 = (0 until m).filter { cOp[it] }.map { it + 1 }
    wt.print(ans1.size)
    for (res in ans1) wt.print(" $res")
    wt.println()
    wt.print(ans2.size)
    for (res in ans2) wt.print(" $res")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}