package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.mlo
import com.tauros.cp.mmo
import kotlin.math.abs
import kotlin.random.Random

/**
 * @author tauros
 * 2023/12/11
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    val nums = rd.na(n)

    val (left, right) = iar(n) { -1 } to iar(n) { n }
    var top = -1
    val stack = iar(n)
    val posMap = mmo<int, MutableList<int>>()
    for ((i, num) in nums.withIndex()) {
        while (top >= 0 && num > nums[stack[top]]) {
            right[stack[top--]] = i
        }
        if (top >= 0) left[i] = stack[top]
        stack[++top] = i
        posMap.computeIfAbsent(num) { mlo() }.add(i)
    }

    var ans = 0L
    for ((i, num) in nums.withIndex()) {
        val (l, r) = left[i] to right[i]
        val posList = posMap.getOrDefault(num, emptyList())
        val pos = findFirst(posList.size) { posList[it] >= i }
        if (pos + k - 1 >= posList.size) continue
        val end = posList[pos + k - 1]
        if (end >= r) continue
        val res = (i.toLong() - l) * (r - end)
        ans += res
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}