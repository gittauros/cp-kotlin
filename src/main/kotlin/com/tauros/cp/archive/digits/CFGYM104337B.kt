package com.tauros.cp.archive.digits

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.common.string
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2024/1/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/gym/104337/problem/B
    val stateId = mmo<string, int>()
    var idx = 0
    fun IntArray.state(): int {
        val state = (1 until this.size).map { this[it] }.joinToString("#")
        return stateId.computeIfAbsent(state) { idx++ }
    }
    val dp = ar(19) { lar(1500) { -1 } }
    fun calc(num: long): long {
        if (num < 0) return 0
        if (num == 0L) return 1
        val max = buildList {
            var iter = num
            while (iter > 0) {
                add((iter % 10).toInt())
                iter /= 10
            }
        }.toIntArray()
        val len = max.size
        val cnt = iar(10)
        val freq = iar(19)
        fun dfs(pos: int, limit: boolean = true, leading: boolean = true): long {
            if (pos == 0) return if (leading) 1 else cnt.max().toLong()
            freq.fill(0)
            cnt.filter { it > 0 }.onEach { freq[it] += 1 }
            val state = freq.state()
            if (!limit && dp[pos][state] != -1L) return dp[pos][state]
            var res = 0L
            for (b in 0 .. if (limit) max[pos - 1] else 9) {
                if (!leading || b > 0) cnt[b] += 1
                res += dfs(pos - 1, limit && b == max[pos - 1], leading && b == 0)
                if (!leading || b > 0) cnt[b] -= 1
            }
            if (!limit) dp[pos][state] = res
            return res
        }
        return dfs(len)
    }

    val cases = rd.ni()
    repeat(cases) {
        val (l, r) = rd.nl() - 1 to rd.nl()
        val (cl, cr) = calc(l) to calc(r)
        val ans = cr - cl
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}