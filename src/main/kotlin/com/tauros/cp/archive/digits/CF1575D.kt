package com.tauros.cp.archive.digits

import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.pow
import com.tauros.cp.iar

/**
 * @author tauros
 * 2025/1/2
 */
fun main(args: Array<String>) {
    val s = readln()

    val mem = ar(s.length) { ar(25) { iar(11) { -1 } } }
    fun dfs(pos: int = 0, mod: int = 0, xNum: int = -1): int {
        if (pos == s.length) {
            return if (mod == 0) 1 else 0
        }
        if (mem[pos][mod][xNum + 1] != -1) return mem[pos][mod][xNum + 1]
        var ans = 0
        val base = pow(10, s.length - 1 - pos, 25)
        val st = if (pos == 0 && s.length > 1) 1 else 0
        if (s[pos] == '_') {
            for (num in st until 10) {
                ans += dfs(pos + 1, (mod + num * base % 25) % 25, xNum)
            }
        } else if (s[pos] == 'X') {
            if (xNum == -1) {
                for (num in st until 10) {
                    ans += dfs(pos + 1, (mod + num * base % 25) % 25, num)
                }
            } else {
                ans += dfs(pos + 1, (mod + xNum * base % 25) % 25, xNum)
            }
        } else {
            val num = s[pos] - '0'
            ans += dfs(pos + 1, (mod + num * base % 25) % 25, xNum)
        }
        mem[pos][mod][xNum + 1] = ans
        return mem[pos][mod][xNum + 1]
    }
    if (s == "0") {
        println(1)
    } else if (s[0] == '0') {
        println(0)
    } else {
        val ans = dfs()
        println(ans)
    }
}