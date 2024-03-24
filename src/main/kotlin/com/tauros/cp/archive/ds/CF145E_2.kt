package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.car
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.randomInt
import com.tauros.cp.structure.LazySegmentData
import com.tauros.cp.structure.LazySegmentTree
import kotlin.math.abs
import kotlin.system.measureTimeMillis

/**
 * @author tauros
 * 2024/3/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/145/E
//    val (n, m) = rd.ni() to rd.ni()
//    val str = rd.ns(n)

    val n = 1e6.toInt()
    val m = 3e5.toInt()
    val str = car(n) { if (randomInt() and 1 == 1) '4' else '7' }
    val ops = ar(m) {
        val (a, b) = abs(randomInt()) % n to abs(randomInt()) % n
        iao(minOf(a, b), maxOf(a, b))
    }

    data class Info(var c00: int = 0, var c11: int = 0, var c01: int = 0, var c10: int = 0) : LazySegmentData<Info, boolean> {
        override var tag: boolean = false
        override fun tagAvailable() = tag
        override fun clearTag() { tag = false }
        override fun acceptTag(other: boolean) {
            if (!other) return
            tag = !tag
            val t1 = c00; c00 = c11; c11 = t1
            val t2 = c01; c01 = c10; c10 = t2
        }
        override fun update(l: Info, r: Info) {
            c00 = l.c00 + r.c00; c11 = l.c11 + r.c11
            c01 = maxOf(l.c00 + r.c01, l.c01 + r.c11)
            c10 = maxOf(l.c11 + r.c10, l.c10 + r.c00)
        }
    }
    val seg = LazySegmentTree(0, n - 1) { Info() }
    println(buildString {
        val time = measureTimeMillis {
            seg.build {
                if (str[it] == '4') c00 = 1 else c11 = 1
                c01 = 1; c10 = 1
            }
        }
        append("$time ms")
    })
//    seg.build {
//        if (str[it] == '4') c00 = 1 else c11 = 1
//        c01 = 1; c10 = 1
//    }
    println(buildString {
        val time = measureTimeMillis {
            for ((cl, cr) in ops) seg.update(cl, cr, true)
        }
        append("$time ms")
    })
//    repeat(m) {
//        val op = rd.ns()
//        if (op == "count") {
//            val ans = seg.queryAll { this }
//            wt.println(ans.c01)
//        } else {
//            val (l, r) = rd.ni() - 1 to rd.ni() - 1
//            seg.update(l, r, true)
//        }
//    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}