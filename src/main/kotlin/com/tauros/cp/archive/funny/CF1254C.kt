package com.tauros.cp.archive.funny

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.int
import com.tauros.cp.common.long
import com.tauros.cp.dq

/**
 * @author tauros
 * 2024/2/22
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = readln().toInt()

    fun askArea(i: int, j: int, k: int): long {
        println("1 $i $j $k")
        return readln().toLong()
    }
    fun askSign(i: int, j: int, k: int): int {
        println("2 $i $j $k")
        return readln().toInt()
    }

    var side = n
    for (i in 2 until n) {
        val sign = askSign(1, side, i)
        if (sign < 0) side = i
    }

    val areas = (2 .. n).filter { it != side }
        .map { it to askArea(1, side, it) }
        .sortedBy { it.second }.map { it.first }
    val top = areas.last()

    val (left, right) = dq<int>() to dq<int>()
    right.add(side)
    for (i in 0 until areas.lastIndex) {
        val k = areas[i]
        val sign = askSign(1, top, k)
        if (sign > 0) left.addFirst(k)
        else right.addLast(k)
    }
    right.addLast(top)

    val ans = intArrayOf(1) + right.toIntArray() + left.toIntArray()

    val str = buildString {
        append("0 ")
        for (res in ans) append("$res ")
    }
    println(str)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}