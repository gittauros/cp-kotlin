package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import kotlin.math.abs

/**
 * @author tauros
 * 2024/1/31
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/contest/1918/problem/C
    val cases = rd.ni()
    repeat(cases) {
        val (a, b, r) = rd.nal(3)
        val (diff, half) = (a xor b) to (a + b shr 1)
        val low = minOf(a, b)
        val add = half - low
        var (limit, cap) = if (add.takeHighestOneBit() < r.takeHighestOneBit()) false to r else true to minOf(add, r)
        var x = 0L
        if (add > 0 && r > 0) {
            for (bit in minOf(add.takeHighestOneBit().countTrailingZeroBits(), r.takeHighestOneBit().countTrailingZeroBits()) downTo 0) {
                if (diff shr bit and 1 != 1L || low shr bit and 1 == 1L) {
                    if (limit && cap shr bit and 1 == 1L) limit = false
                    continue
                }
                if (!limit || cap shr bit and 1 == 1L) x = 1L shl bit xor x
            }
        }
        val (xa, xb) = (a xor x) to (b xor x)
        wt.println(abs(xa - xb))
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}