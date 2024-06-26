package com.tauros.cp.archive.funny

/**
 * @author tauros
 * 2024/4/1
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1220/D
    // 真的很难，还看错题了
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toLong() }.toLongArray()

    val map = nums.groupBy { it.takeLowestOneBit().countTrailingZeroBits() }
    val (cnt, _) = map.maxBy { (_, list) -> list.size }
    val ans = buildList {
        for ((cur, list) in map) if (cur != cnt) {
            addAll(list)
        }
    }

    println(ans.size)
    println(buildString {
        for (res in ans) append("$res ")
    })
}