@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.ge
import com.tauros.cp.common.iter
import com.tauros.cp.common.le
import java.io.FileInputStream

/**
 * @author tauros
 */
private val bufCap = 128
//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (A, B) = rd.nl() to rd.nl()
    if (A == B) {
        wt.println(1)
        return
    }
    val len = B - A + 1
    val first = (A xor B).takeHighestOneBit()
    val c = first - 1
    if (c == 0L) {
        wt.println(len)
        return
    }
    val a = A and c
    val second = (B and c).takeHighestOneBit()
    if (second == 0L) {
        wt.println(c - a + 1 + len)
        return
    }
    val d = second - 1 or second
    val b = B and second - 1 or second
    if (a <= d) {
        wt.println(c - b + len)
    } else {
        wt.println(d - b + c - a + 1 + len)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}