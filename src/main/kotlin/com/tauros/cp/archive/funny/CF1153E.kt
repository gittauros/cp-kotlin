package com.tauros.cp.archive.funny

import com.tauros.cp.common.int
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/22
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1153/E
    // 你有这么长的贪吃蛇进入方块，记住我给出的原理：
    // 进出边界偶数次表名要么头尾都在内要么头尾都不在内
    // 暴力找头尾是否在哪行，找不到就去找列，花掉2n-2次，还剩最少21次询问
    // 对找到的行或者列进行二分，花掉剩余20次即可
    val n = readln().toInt()

    fun ask(x1: int, y1: int, x2: int, y2: int): int {
        println("? $x1 $y1 $x2 $y2")
        return readln().toInt()
    }
    fun out(x1: int, y1: int, x2: int, y2: int) {
        println("! $x1 $y1 $x2 $y2")
    }

    var (row1, row2) = run {
        val res = iar(2) { -1 }
        for (row in n - 1 downTo 1) {
            val cnt = ask(1, 1, row, n)
            if (cnt % 2 == 1) {
                res[0] = row + 1; break
            }
        }
        if (res[0] != -1) {
            for (row in res[0] - 1 downTo 1) {
                val cnt = ask(1, 1, row, n)
                if (cnt % 2 == 0) {
                    res[1] = row + 1; break
                }
            }
            if (res[1] == -1) res[1] = 1
        }
        res
    }
    if (row1 != -1) {
        fun find(row: int): int {
            var (l, r) = 1 to n
            while (l < r) {
                val mid = l + r shr 1
                val cnt = ask(row, 1, row, mid)
                if (cnt % 2 == 0) {
                    l = mid + 1
                } else {
                    r = mid
                }
            }
            return l
        }
        val col1 = find(row1)
        val col2 = find(row2)
        out(row1, col1, row2, col2)
        return
    }
    val (col1, col2) = run {
        val res = iar(2) { -1 }
        for (col in n - 1 downTo 1) {
            val cnt = ask(1, 1, n, col)
            if (cnt % 2 == 1) {
                res[0] = col + 1; break
            }
        }
        for (col in res[0] - 1 downTo 1) {
            val cnt = ask(1, 1, n, col)
            if (cnt % 2 == 0) {
                res[1] = col + 1; break
            }
        }
        if (res[1] == -1) res[1] = 1
        res
    }
    fun find(col: int): int {
        var (l, r) = 1 to n
        while (l < r) {
            val mid = l + r shr 1
            val cnt = ask(1, col, mid, col)
            if (cnt % 2 == 0) {
                l = mid + 1
            } else {
                r = mid
            }
        }
        return l
    }
    row1 = find(col1)
    row2 = find(col2)
    out(row1, col1, row2, col2)
}