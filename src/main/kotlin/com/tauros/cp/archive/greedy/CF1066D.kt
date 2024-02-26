package com.tauros.cp.archive.greedy

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * 2024/2/20
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1066/D
    // 太奇妙了
    // 解法一：首先可以用前缀和+二分来查找每个位置能跳到的下一个位置，然后每个位置搞个st，算下不超过m个箱子最远的下标，nlogn，但是很蠢
    // 解法二：观察一下，从后往前找每个位置能跳最远的时候，肯定有个分界点，在该分界点的左边全都到不了数组尾部，而分界点右边全都能到数组尾部，最左边的分界点就是最佳答案，nlogn
    // 解法三：再仔细观察下，解法二中的最佳方案，最后一个box中相当于就是把数组反过来装箱刚好装到小于等于k，然后是倒数第二、倒数第三...，也就是直接倒过来模拟即可
    val (n, m, k) = rd.na(3)
    val nums = rd.na(n)

    var ans = 0
    var (sum, rest) = 0L to m
    for (num in nums.reversed()) {
        if (sum + num > k) {
            sum = 0
            rest -= 1
        }
        if (rest == 0) break
        sum += num
        ans += 1
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}