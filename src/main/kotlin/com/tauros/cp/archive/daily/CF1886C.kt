package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.car
import com.tauros.cp.common.findFirst

/**
 * @author tauros
 * 2023/12/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()
    val stack = car(1e6.toInt())
    repeat(cases) {
        val str = rd.ns()
        val pos = rd.nl()
        val n = str.length
        val deleteTarget = findFirst(n) { (n.toLong() + n - it) * (it + 1) / 2 >= pos }
        val i = (pos - (n + n - deleteTarget + 1L) * deleteTarget / 2).toInt()
        var (top, deleted) = -1 to 0
        for (c in str) {
            while (top >= 0 && deleted < deleteTarget && c < stack[top]) {
                top -= 1
                deleted += 1
            }
            stack[++top] = c
        }
        wt.print(stack[i - 1])
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}