package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/25
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val cnt = iar(2)
        val lens = iar(n) {
            val str = rd.ns()
            str.onEach { cnt[it - '0'] += 1 }
            str.length
        }
        val odds = iar(2)
        var rest = lens.sum()
        var ans = 0
        for (len in lens) {
            for (p in 0 until 2) {
                if (cnt[p] >= len) {
                    cnt[p] -= len
                    rest -= len
                    if (len % 2 == 1) odds[p] = 1
                    ans += 1
                    break
                }
            }
        }
        if (rest > 0) {
            val r = rest and 1
            val p = cnt.map { it and 1 }
            out@ for (x in iao(0) + odds.reduce(int::or)) for (o in 0 until 2)
                if (p[o] xor x == r && p[1 - o] xor x == 0) {
                    ans += 1
                    break@out
                }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}