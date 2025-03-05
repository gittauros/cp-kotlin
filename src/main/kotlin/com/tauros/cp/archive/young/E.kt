package com.tauros.cp.archive.young

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int

/**
 * @author tauros
 * 2025/3/2
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val cases = rd.ni()
    repeat(cases) {
        val (a, b, c) = rd.na(3)
        var ans = 3

        fun judge(x: int, y: int, z: int) = x + y > z && x + z > y && y + z > x
        if (judge(a, a, b)) ans++
        if (judge(a, a, c)) ans++
        if (judge(b, b, a)) ans++
        if (judge(b, b, c)) ans++
        if (judge(c, c, a)) ans++
        if (judge(c, c, b)) ans++
        if (judge(a, b, c)) ans++
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}