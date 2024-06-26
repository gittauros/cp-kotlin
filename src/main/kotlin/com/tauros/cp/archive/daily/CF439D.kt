package com.tauros.cp.archive.daily

import com.tauros.cp.common.findFirst
import com.tauros.cp.common.long
import com.tauros.cp.runningFold
import kotlin.math.abs

/**
 * @author tauros
 * 2024/4/10
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/439/D
    // 一开始还搞了个错的中位数贪心
    // 不过由中位数贪心来理解，最佳分界值一定可以在a b两个数组原有的数值中取到
    // 所以合并成一个数组去重后二分计算即可，避免有同段相同值的三分
    // 由于枚举过程也是递增的，分界值在a b中的位置也是单调变化的，也可以双指针
    // 但整体复杂度受限于排序，就没有太大所谓了
    val (n, m) = readln().split(" ").map { it.toInt() }
    val a = readln().split(" ").map { it.toInt() }.sorted().toIntArray()
    val b = readln().split(" ").map { it.toInt() }.sorted().toIntArray()

    val nums = (a + b).sorted().distinct().toIntArray()
    val aSum = a.runningFold(0L, long::plus)
    val bSum = b.runningFold(0L, long::plus)
    var ans = 0x3f3f3f3f3f3f3f3fL
    for (num in nums) {
        val bCnt = m - findFirst(m) { b[it] > num }
        val bCost = bSum[m] - bSum[m - bCnt] - num * bCnt.toLong()
        val aCnt = findFirst(n) { a[it] >= num }
        val aCost = num * aCnt.toLong() - aSum[aCnt]
        val res = aCost + bCost
        ans = minOf(res, ans)
    }
    println(ans)
}