package com.tauros.cp.archive.ds

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.common.findFirst
import com.tauros.cp.common.int
import com.tauros.cp.iar
import com.tauros.cp.structure.IntHeap

/**
 * @author tauros
 * 2024/3/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1837/F
    // 学的灵神做法 https://codeforces.com/contest/1837/submission/250279590
    // 想了二分也想了分段，但是往前后缀分解想
    // 最主要没想出来怎么求不超过sum的最长子序列怎么求，原来可以反悔贪心
    // 用构造好的int heap过不去两个log的，java pq比我的构造好的int heap速度更快
    // 但是offer poll使用inline fun方式的int heap可以过，非常快
    val cases = rd.ni()
    val cap = 1e9.toLong()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()
        val nums = rd.na(n)

        val heap = IntHeap(n)
        val len = iar(n)
        val ans = findFirst(cap * k) { res ->
            var sum = 0L; heap.clear()
            fun offer(num: int) {
                if (sum + num <= res) {
                    sum += num
                    heap.offer(num) { a, b -> -a.compareTo(b) }
                } else if (heap.isNotEmpty() && num < heap.peek()) {
                    sum += num - heap.poll { a, b -> -a.compareTo(b) }
                    heap.offer(num) { a, b -> -a.compareTo(b) }
                }
            }
            for (i in n - 1 downTo 0) {
                offer(nums[i])
                len[i] = heap.size
                if (len[i] >= k) return@findFirst true
            }
            sum = 0L; heap.clear()
            for (i in 0 until n) {
                offer(nums[i])
                if (heap.size + (if (i == n - 1) 0 else len[i + 1]) >= k) return@findFirst true
            }
            false
        }
        wt.println(ans)
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}