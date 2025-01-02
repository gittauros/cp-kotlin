package com.tauros.cp.archive.ds

import com.tauros.cp.common.int
import java.util.TreeMap

/**
 * @author tauros
 * 2024/5/13
 */
fun main(args: Array<String>) {
    val cases = readln().toInt()
    repeat(cases) {
        var line = ""
        while (line.isBlank()) { line = readln() }
        val (n, m) = line.split(" ").map { it.toInt() }
        val a = readln().split(" ").map { it.toInt() }.toIntArray()
        val vMap = TreeMap<int, int>()
        fun addCarriage(i: int, v: int) {
            if (vMap.isEmpty() || vMap.firstKey() >= i || vMap.lowerEntry(i).value > v) {
                vMap[i] = v
            } else return
            var iter = i
            while (vMap.isNotEmpty() && vMap.lastKey() > iter) {
                val higher = vMap.higherEntry(iter)
                iter = higher.key; val nextV = higher.value
                if (nextV >= v) {
                    vMap.remove(iter)
                } else break
            }
        }
        for ((i, v) in a.withIndex()) {
            addCarriage(i, v)
        }
        repeat(m) {
            val (k, d) = readln().split(" ").map { it.toInt() }
            a[k - 1] -= d
            addCarriage(k - 1, a[k - 1])

            print("${vMap.size} ")
        }
        println()
    }
}