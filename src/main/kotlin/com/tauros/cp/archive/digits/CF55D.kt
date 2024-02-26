package com.tauros.cp.archive.digits

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.common.lcm
import com.tauros.cp.common.long
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2024/2/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/55/D
    // 直接dp记录对 2 .. 9 的所有余数还需要记录出现了哪些数，状态数过多了(19 * 9! * 2^8)
    // 考虑 a|x 且 b|x 可得 lcm(a, b)|x，而考虑num对 2 .. 9 的余数仅需要考虑num对 lcm(2, .., 9)的余数
    // 因为：num = lcm(2, .., 9)*x + r；因为 2 .. 9 都能整除 lcm(2, .., 9) 所以，num对 2 .. 9 的余数仅需要考虑r
    // 并且从 lcm(2 .. 9 的子序列) 也是整除 lcm(2, .., 9) 的，所以仅需要记录数字数位的 digitLcm，最后再算 r % digitLcm 是否为0即可
    // digitLcm可以离散化一下减少记忆化的数量，这样这题状态就减少很多了
    val cap = (2 .. 9).reduce(::lcm)
    val lcmMap = mmo<int, int>()
    val discrete = buildSet {
        val maxState = 1 shl 9
        for (state in 0 until maxState) {
            val lcm = (0 until 9).filter { state shr it and 1 != 0 }.map { it + 1 }.reduceOrNull(::lcm) ?: 1
            add(lcm); lcmMap[state] = lcm
        }
    }.mapIndexed { i, lcm -> lcm to i }.toMap()
    val dp = ar(19) { ar(discrete.size) { lar(cap) { -1 } } }
    fun calc(num: long): long {
        if (num <= 0L) return 1
        val max = num.toString().map { it - '0' }.reversed().toIntArray()
        fun dfs(pos: int = max.lastIndex, limit: boolean = true, lcm: int = 0, r: int = 0): long {
            val lcmNum = lcmMap[lcm]!!
            if (pos == -1) return if (r % lcmNum == 0) 1 else 0
            val lcmState = discrete[lcmNum]!!
            if (!limit && dp[pos][lcmState][r] != -1L) return dp[pos][lcmState][r]
            var res = 0L
            for (i in 0 .. if (limit) max[pos] else 9) {
                res += dfs(pos - 1, limit && i == max[pos],
                    if (i == 0) lcm else 1 shl i - 1 or lcm, (r * 10 + i) % cap)
            }
            if (!limit) dp[pos][lcmState][r] = res
            return res
        }
        return dfs()
    }

    val cases = rd.ni()
    repeat(cases) {
        val (l, r) = rd.nl() to rd.nl()
        val dpr = calc(r)
        val dpl1 = calc(l - 1)
        wt.println(dpr - dpl1)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}