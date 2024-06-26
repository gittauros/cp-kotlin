package com.tauros.cp.archive.funny

/**
 * @author tauros
 * 2024/4/9
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/301/A
    // 负数个数x：
    // - x小于n，x只能是偶数，如果n是奇数且x是奇数，可以将x个数变成偶数
    // - x大于等于n，x可以减等于n
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toInt() }.toIntArray()

    nums.sort()
    if (nums.count { it < 0 } >= n) for (i in 0 until n) nums[i] *= -1
    nums.sort()
    if (n % 2 == 1 && nums.count { it < 0 } % 2 == 1) for (i in 0 until n) nums[i] *= -1
    nums.sort()
    val (neg, pos) = nums.partition { it < 0 }
    val ans = if (neg.size % 2 == 0) -neg.sum() + pos.sum()
    else {
        val res1 = -neg.sum() + pos.sum() - pos.first() * 2
        val res2 = -(neg.sum() - neg.last() * 2) + pos.sum()
        maxOf(res1, res2)
    }
    println(ans)
}