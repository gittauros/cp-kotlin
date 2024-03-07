package com.tauros.cp.archive.digits

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.lar

/**
 * @author tauros
 * 2024/3/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/855/E
    // leading=false和limit=false的状态可以缓存下来
    // 每次询问计算时只用计算limit=true和leading=true的状态
    // b=2的时候1e18有60位，最大mask=3
    // ...
    // b=10的时候1e18有19位，最大mask=1023
    // 记忆化的总状态数不是很多的，可以统一记忆化，记忆化时需要从高位到低位来记忆化
    val cap = 1e18.toLong()
    val mem = ar(11) { b ->
        if (b < 2) ar(0) { lar(0) } else {
            val len = cap.toString(b).length
            ar(len) { lar(1 shl b) { -1 } }
        }
    }
    fun calc(num: long, b: int): long {
        val dp = mem[b]
        val posMax = num.toString(b).reversed().map { it - '0' }.toIntArray()
        val len = posMax.size
        fun dfs(pos: int = len - 1, mask: int = 0, limit: boolean = true, leading: boolean = true): long {
            if (pos < 0) {
                return if (mask == 0 && !leading) 1 else 0
            }
            if (!leading && !limit && dp[pos][mask] != -1L) return dp[pos][mask]
            var res = 0L
            for (i in 0 .. if (limit) posMax[pos] else b - 1) {
                res += dfs(
                    pos - 1,
                    if (!leading || i > 0) 1 shl i xor mask else mask,
                    limit && i == posMax[pos], leading && i == 0
                )
            }
            if (!leading && !limit) dp[pos][mask] = res
            return res
        }
        return dfs()
    }

    val q = rd.ni()
    repeat(q) {
        val b = rd.ni()
        val (l, r) = rd.nal(2)
        val ans = calc(r, b) - calc(l - 1, b)
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}