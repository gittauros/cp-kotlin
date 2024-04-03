package com.tauros.cp.archive.funny

/**
 * @author tauros
 * 2024/4/2
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1299/A
    // 这一位只要出现两次及以上，就留不下来
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toInt() }.toIntArray()

    val heads = buildList {
        for (b in 30 downTo 0) {
            var (cnt, j) = 0 to -1
            for (i in 0 until n) if (1 shl b and nums[i] != 0) {
                cnt += 1
                if (cnt > 0) j = i
                if (cnt >= 2) break
            }
            if (cnt == 1) add(j)
        }
    }.distinct().toIntArray()

    val set = heads.toSet()
    val ans = heads + (0 until n).filter { it !in set }.toIntArray()
    println(buildString { for (res in ans) append("${nums[res]} ") })
}