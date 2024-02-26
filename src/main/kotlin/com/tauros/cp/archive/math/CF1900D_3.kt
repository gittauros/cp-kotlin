package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.heapSort
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.lar
import com.tauros.cp.math.Phi
import com.tauros.cp.mlo

/**
 * @author tauros
 * 2023/11/28
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1900/problem/D
    // https://zhuanlan.zhihu.com/p/669131917
    val cases = rd.ni()
    val cap = 1e5.toInt()
    val phi = Phi(cap)
    val divs = ar(cap + 1) { mutableListOf<int>() }
    for (i in 1 .. cap) {
        for (j in i .. cap step i) {
            divs[j].add(i)
        }
    }
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)
        nums.heapSort()

        val cnt = iar(cap + 1)
        var ans = 0L
        for (i in 0 until n) {
            for (d in divs[nums[i]]) {
                ans += (n - 1L - i) * cnt[d] * phi[d]
                cnt[d] += 1
            }
        }

        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}