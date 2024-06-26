package com.tauros.cp.archive.daily

import com.tauros.cp.common.findFirst
import com.tauros.cp.common.long

/**
 * @author tauros
 * 2024/4/9
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/613/B
    // 枚举最大值，二分最小值，但是输出方案很麻烦，要注意处理下最大值最小值相同的情况
    val (n, a, cf, cm, m) = readln().split(" ").map { it.toLong() }.toLongArray()
    val nums = readln().split(" ").map { it.toInt() }.toIntArray()

    val sorted = (0 until n.toInt()).sortedBy { nums[it] }
    val sums = sorted.map { nums[it] }.runningFold(0L, long::plus).toLongArray()
    var minIdx = findFirst(n.toInt()) { (it + 1L) * nums[sorted[it]] - sums[it + 1] > m } - 1
    var max = run {
        val rest = m - ((minIdx + 1L) * nums[sorted[minIdx]] - sums[minIdx + 1])
        val add = minOf((if (minIdx == n.toInt() - 1) a else nums[sorted[minIdx + 1]].toLong()) - nums[sorted[minIdx]], rest / (minIdx + 1))
        val min = (nums[sorted[minIdx]] + add)
        (if (min == a) minIdx + 1 else 0) * cf + min * cm
    }
    var last = n.toInt()
    for (i in n.toInt() - 1 downTo 0) {
        val capCnt = n - i.toLong()
        val capRest = m - (capCnt * a - (sums[n.toInt()] - sums[i]))
        if (capRest < 0) break
        val idx = findFirst(i) { (it + 1L) * nums[sorted[it]] - sums[it + 1] > capRest } - 1
        val res = if (idx < 0) capCnt * cf
        else {
            val rest = capRest - ((idx + 1L) * nums[sorted[idx]] - sums[idx + 1])
            val add = minOf((if (idx == i - 1) a else nums[sorted[idx + 1]].toLong()) - nums[sorted[idx]], rest / (idx + 1))
            val min = nums[sorted[idx]] + add
            (capCnt + if (min == a) idx + 1 else 0) * cf + min * cm
        }
        if (res > max) {
            max = res; last = i; minIdx = idx
        }
    }

    if (minIdx >= 0) {
        val rest = m - ((n - last) * a - (sums[n.toInt()] - sums[last])) - ((minIdx + 1L) * nums[sorted[minIdx]] - sums[minIdx + 1])
        val add = minOf((if (minIdx == last - 1) a else nums[sorted[minIdx + 1]].toLong()) - nums[sorted[minIdx]], rest / (minIdx + 1))
        val min = nums[sorted[minIdx]] + add
        for (i in 0 .. minIdx) nums[sorted[i]] = min.toInt()
    }
    for (i in n.toInt() - 1 downTo last) nums[sorted[i]] = a.toInt()
    println(max)
    println(buildString {
        for (res in nums) append("$res ")
    })
}