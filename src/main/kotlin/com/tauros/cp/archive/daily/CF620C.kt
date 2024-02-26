package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.dq
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.mmo
import com.tauros.cp.mso
import com.tauros.cp.structure.default

/**
 * @author tauros
 * 2023/12/12
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)

    val set = mso<int>()
    val ans = dq<IIP>()
    var iter = 0
    while (iter < n) {
        val st = iter
        while (iter < n) {
            val success = nums[iter] in set
            set.add(nums[iter++])
            if (success) {
                ans.add(st + 1 to iter)
                set.clear()
                break
            }
        }
    }
    if (ans.isEmpty()) {
        wt.println("-1")
        return
    }
    val (st, _) = ans.removeLast()
    ans.addLast(st to n)
    wt.println(ans.size)
    for ((l, r) in ans) {
        wt.println("$l $r")
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}