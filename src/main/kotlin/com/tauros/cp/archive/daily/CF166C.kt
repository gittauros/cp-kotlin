package com.tauros.cp.archive.daily

/**
 * @author tauros
 * 2024/3/26
 */
fun main(args: Array<String>) {
    val (n, x) = readln().split(" ").map { it.toInt() }
    val nums = readln().split(" ").map { it.toInt() }.toIntArray()

    val lt = nums.count { it < x }
    val gt = nums.count { it > x }
    val eq = n - lt - gt
    val res1 = maxOf(n - 1 - 2 * (lt + eq), 2 * lt + 1 - n + 1).let { if (it % 2 == n % 2) it else it + 1 }
    val res2 = maxOf(2 * lt + 1 - n, n - 2 * (lt + eq)).let { if (it % 2 == n % 2) it + 1 else it }
    println(maxOf(0, minOf(res1, res2)))
}