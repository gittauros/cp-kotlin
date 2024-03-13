package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar

/**
 * @author tauros
 * 2024/3/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val n = rd.ni()
    val ranges = ar(n) { rd.na(2) }

    ranges.sortBy { it[1] }
    var (ed1, ed2) = -1 to -1
    for ((l, r) in ranges) {
        if (l > ed1 && (l <= ed2 || ed1 > ed2)) {
            ed1 = r
        } else if (l > ed2) {
            ed2 = r
        } else {
            wt.println("NO")
            return
        }
    }
    wt.println("YES")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}