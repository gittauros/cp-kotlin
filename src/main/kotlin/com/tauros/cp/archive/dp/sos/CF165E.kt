package com.tauros.cp.archive.dp.sos

import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/20
 */
fun main(args: Array<String>) {
    // https://codeforces.com/contest/165/problem/E
    // 每个数字取反的子集
    val n = readln().toInt()
    val nums = readln().split(" ").map { it.toInt() }.toIntArray()

    val high = nums.max().takeHighestOneBit().countTrailingZeroBits()
    val cap = (1 shl high + 1) - 1
    val other = iar(cap + 1) { -1 }
    for (num in nums) other[num] = num

    for (b in 0 .. high) {
        val bit = 1 shl b
        for (num in 1 .. cap) if (bit and num != 0 && other[num xor bit] != -1) {
            other[num] = other[num xor bit]
        }
    }

    println(buildString {
        for (num in nums) {
            append(other[num xor cap])
            append(' ')
        }
    })
}