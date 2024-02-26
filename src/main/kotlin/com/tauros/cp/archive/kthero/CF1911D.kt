package com.tauros.cp.archive.kthero

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.avlm
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mlo
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
    val cap = 2e5.toInt()
    val n = rd.ni()
    val nums = rd.na(n)
    val cnt = iar(cap + 1)
    for (num in nums) {
        cnt[num] += 1
        if (cnt[num] > 2) {
            wt.println("NO")
            return
        }
    }
    wt.println("YES")
    val inc = buildList { for (i in 0 .. cap) if (cnt[i] > 0) add(i).also { cnt[i] -= 1 } }
    val dec = buildList { for (i in cap downTo 0) if (cnt[i] > 0) add(i).also { cnt[i] -= 1 } }
    wt.println(inc.size)
    for (i in inc) wt.print("$i ")
    wt.println()
    wt.println(dec.size)
    for (i in dec) wt.print("$i ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}