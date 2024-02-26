@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily


import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar
import com.tauros.cp.structure.AVLSet

/**
 * @author tauros
 * 2023/10/21
 */
private val bufCap = 65536

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val (a, b) = rd.na(n) to rd.na(n)
    val set = AVLSet<Int>(Int::compareTo)
    val cnt = iar(n)
    b.forEach {
        set.add(it)
        cnt[it]++
    }
    for (num in a) {
        if (num > 0) {
            var x = set.ceiling(n - num)
            if (x != null) {
                if (--cnt[x] == 0) {
                    set.remove(x)
                }
                wt.print("${(num + x) % n} ")
            } else {
                x = set.min()!!
                if (--cnt[x] == 0) {
                    set.remove(x)
                }
                wt.print("${(num + x) % n} ")
            }
        } else {
            val x = set.min()!!
            if (--cnt[x] == 0) {
                set.remove(x)
            }
            wt.print("$x ")
        }
    }
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}