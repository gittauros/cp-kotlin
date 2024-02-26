package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.bar
import com.tauros.cp.iao

/**
 * @author tauros
 * 2024/2/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)
    val q = rd.ni()

    val ops = buildList {
        repeat(q) {
            val op = rd.ni()
            if (op == 1) add(iao(op, rd.ni() - 1, rd.ni()))
            else add(iao(op, rd.ni()))
        }
    }.reversed()

    val modifiable = bar(n) { true }
    var max = 0
    for (o in ops) {
        val op = o[0]
        if (op == 1) {
            val (_, p, x) = o
            if (modifiable[p]) {
                nums[p] = maxOf(x, max)
                modifiable[p] = false
            }
        } else {
            val (_, x) = o
            max = maxOf(max, x)
        }
    }

    for ((i, num) in nums.withIndex()) {
        val res = if (modifiable[i]) maxOf(max, num) else num
        wt.print("$res ")
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}