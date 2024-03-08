package com.tauros.cp.archive.funny

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/3/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/contest/1934/problem/B
    // 1做多拿两个，3最多拿1个，6最多拿2个，10最多拿2个
    // 3个1不如1个3，2个3不如1个6，3个6不如15+3，3个10不如15+15
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        var ans = 0x3f3f3f3f
        for (one in iao(0, 1, 2)) for (three in iao(0, 3))
            for (six in iao(0, 6, 12)) for (ten in iao(0, 10, 20)) {
                val rest = n - one - three - six - ten
                if (rest >= 0 && rest % 15 == 0) {
                    val res = rest / 15 + one + three / 3 + six / 6 + ten / 10
                    ans = minOf(ans, res)
                }
            }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}