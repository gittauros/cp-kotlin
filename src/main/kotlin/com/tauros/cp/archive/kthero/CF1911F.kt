package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.avls
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mmo
import com.tauros.cp.mso
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2023/12/5
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n).sortedArray()
    val cap = 150001
    val cnt = iar(cap + 1)
    val set = mso<int>()
    for (num in nums) {
        cnt[num] += 1
        set.add(num)
    }
    var iter = 1
    while (iter <= cap) {
        if (cnt[iter] == 0) {
            iter++
            continue
        }
        val st = iter
        var sum = cnt[iter]
        while (iter + 1 <= cap && cnt[iter + 1] > 0) {
            sum += cnt[++iter]
        }
        val ed = iter++
        val len = iter - st
        if (st != 1 && st - 1 !in set && sum > len) {
            sum -= 1
            set.add(st - 1)
        }
        if (sum > len) {
            sum -= 1
            set.add(ed + 1)
        }
    }
    wt.println(set.size)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}