package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iao
import com.tauros.cp.iar
import com.tauros.cp.mso
import com.tauros.cp.structure.AVLSet

/**
 * @author tauros
 * 2023/11/21
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val pairs = ar(m) { iao(rd.ni(), rd.ni()) }
    val cnt = buildMap<Int, Int> {
        for ((a, b) in pairs) {
            put(a, getOrDefault(a, 0) + 1)
            put(b, getOrDefault(b, 0) + 1)
        }
    }
    fun calc(a: Int, b: Int): Boolean = cnt[a]!! + cnt[b]!! - pairs.count { a in it && b in it } == m
    val (a, b) = pairs[0]
    if (calc(a, b)) {
        wt.println("YES")
        return
    }
    val (c, d) = pairs.find { (x, y) -> x != a && x != b && y != a && y != b }!!
    if (calc(a, c) || calc(a, d) || calc(b, c) || calc(b, d)) {
        wt.println("YES")
        return
    }
    wt.println("NO")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}