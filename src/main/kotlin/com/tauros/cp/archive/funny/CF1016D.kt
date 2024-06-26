package com.tauros.cp.archive.funny

import com.tauros.cp.common.int

/**
 * @author tauros
 * 2024/4/1
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1016/D
    // 只要异或和相等就有解，grid[0][0]取个特殊值，其它位置直接填
    val (n, m) = readln().split(" ").map { it.toInt() }
    val rows = readln().split(" ").map { it.toInt() }.toIntArray()
    val cols = readln().split(" ").map { it.toInt() }.toIntArray()

    if (rows.reduce(int::xor) != cols.reduce(int::xor)) {
        println("NO")
        return
    }
    println("YES")
    val first = rows[0] xor (1 until m).map { cols[it] }.reduce(int::xor)
    println(buildString {
        for (i in 0 until n) {
            for (j in 0 until m) {
                if (i == 0 && j == 0) append("$first ")
                else if (i == 0) append("${cols[j]} ")
                else if (j == 0) append("${rows[i]} ")
                else append("0 ")
            }
            append("\n")
        }
    })
}