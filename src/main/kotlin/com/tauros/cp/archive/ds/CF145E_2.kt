package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.car
import com.tauros.cp.common.boolean
import com.tauros.cp.common.int
import com.tauros.cp.iao
import com.tauros.cp.randomInt
import com.tauros.cp.structure.SegmentOpArray
import com.tauros.cp.structure.lazySeg
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
//    val (n, m) = rd.ni() to rd.ni()
//    val str = rd.ns(n)

    val n = 1e6.toInt()
    val m = 3e5.toInt()
    val str = car(n) { if (randomInt() and 1 == 1) '4' else '7' }
    val ops = ar(m) {
        val (a, b) = abs(randomInt()) % n to abs(randomInt()) % n
        iao(minOf(a, b), maxOf(a, b))
    }

    data class Info(val c00: int = 0, val c11: int = 0, val c01: int = 0, val c10: int = 0)
    fun op(l: Info, r: Info) = Info(
        l.c00 + r.c00, l.c11 + r.c11,
        maxOf(l.c00 + r.c01, l.c01 + r.c11),
        maxOf(l.c11 + r.c10, l.c10 + r.c00)
    )
    fun Info.mapping(tag: boolean) = if (tag) Info(c11, c00, c10, c01) else this
    fun boolean.composition(tag: boolean) = this xor tag
    val seg = lazySeg(n, {
        Info(if (str[it] == '4') 1 else 0, if (str[it] == '4') 0 else 1, 1, 1)
    }, ::op, { Info() }, Info::mapping, boolean::composition, { false },
        {
            object : SegmentOpArray<Info> {
                val array = ar(it) { Info() }
                override fun set(pos: Int, data: Info) { array[pos] = data }
                override fun get(pos: Int) = array[pos]
            }
        }, {
            object : SegmentOpArray<boolean> {
                val array = bar(it)
                override fun set(pos: Int, data: boolean) { array[pos] = data }
                override fun get(pos: Int) = array[pos]
            }
        })
    println(buildString {
        val time = measureTimeMillis {
            for ((cl, cr) in ops) seg.apply(cl, cr + 1, true)
        }
        append("$time ms")
    })
//    repeat(m) {
//        val op = rd.ns()
//        if (op == "count") {
//            val ans = seg.prod(0, n)
//            wt.println(ans.c01)
//        } else {
//            val (l, r) = rd.ni() - 1 to rd.ni() - 1
//            seg.apply(l, r + 1, true)
//        }
//    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}