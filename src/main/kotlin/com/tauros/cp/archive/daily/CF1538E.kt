package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.common.string
import com.tauros.cp.mmo

/**
 * @author tauros
 * 2023/12/7
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val cases = rd.ni()

    val haha = "haha"
    fun string.haha() = (0..length - 4)
        .map { if (substring(it, it + 4) == haha) 1L else 0 }
        .reduceOrNull(Long::plus) ?: 0

    data class Seg(val cnt: long, val pre: string, val suf: string)
    fun string.build(): Seg {
        return Seg(haha(), substring(0, minOf(length, 3)), substring(length - minOf(length, 3), length))
    }
    infix fun Seg.merge(other: Seg): Seg {
        val res = cnt + other.cnt
        val mid = suf + other.pre
        val tot = res + mid.haha()
        val newPre = if (pre.length >= 3) pre else mid.substring(0, minOf(3, mid.length))
        val newSuf = if (other.suf.length >= 3) other.suf else mid.substring(mid.length - minOf(mid.length, 3), mid.length)
        return Seg(tot, newPre, newSuf)
    }
    repeat(cases) {
        val n = rd.ni()
        val vars = mmo<string, Seg>()
        var ans = 0L
        repeat(n) {
            val name = rd.ns()
            val op = rd.ns()
            if (op == ":=") {
                val seg = rd.ns().build()
                vars[name] = seg
                ans = seg.cnt
            } else {
                val a = rd.ns()
                val plus = rd.ns()
                val b = rd.ns()
                val res = vars[a]!! merge vars[b]!!
                vars[name] = res
                ans = res.cnt
            }
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}