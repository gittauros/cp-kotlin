package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.MInt
import com.tauros.cp.common.MIntArray
import com.tauros.cp.common.lastIndex
import com.tauros.cp.common.toMInt
import com.tauros.cp.common.withMod
import com.tauros.cp.iar
import com.tauros.cp.miar

/**
 * @author tauros
 * 2023/11/20
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        withMod(1e9.toInt() + 7) {
            val (n, m) = rd.ni() to rd.ni()
            val nums = rd.na(n).sorted().toIntArray()
            val ori = mutableListOf<Int>()
            val cnt = mutableListOf<Int>()
            var iter = 0
            while (iter < n) {
                val st = iter
                while (iter < n && nums[iter] == nums[st]) iter++
                ori.add(nums[st])
                cnt.add(iter - st)
            }
            val prods = MIntArray(cnt.size + 1)
            prods[0] = 1.toMInt()
            for (i in 1 .. cnt.size) prods[i] = prods[i - 1] * cnt[i - 1]
            var ans = 0.toMInt()
            for (i in m .. cnt.size) {
                if (ori[i - m] == ori[i - 1] - m + 1) {
                    ans += prods[i] / prods[i - m]
                }
            }
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}