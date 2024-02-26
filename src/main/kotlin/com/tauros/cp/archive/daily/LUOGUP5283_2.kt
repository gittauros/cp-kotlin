package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar
import com.tauros.cp.runningFold
import com.tauros.cp.structure.KHeap

/**
 * @author tauros
 * 2023/11/24
 */
private val bufCap = 512
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, k) = rd.ni() to rd.ni()
    val nums = rd.nal(n)
    val sums = nums.runningFold(0L, Long::xor)
    val cap = 32
    val nodeCap = (n + 2) * (cap + 2) + 1
    val trie = ar(2) { iar(nodeCap) }
    val size = iar(nodeCap)
    var idx = 1
    val roots = iar(n + 3)
    var rootIdx = 1
    fun insert(o: Int, n: Int, num: Long) {
        var (old, new) = o to n
        for (b in cap downTo 0) {
            val p = (num shr b and 1).toInt()
            trie[p][new] = idx++
            size[trie[p][new]] = size[trie[p][old]] + 1
            trie[p xor 1][new] = trie[p xor 1][old]
            old = trie[p][old]
            new = trie[p][new]
        }
    }
    fun kth(l: Int, r: Int, num: Long, k: Int): Long {
        var (left, right) = l to r
        var gt = 0
        var ans = 0L
        for (b in cap downTo 0) {
            val p = (num shr b and 1).toInt() xor 1
            val cnt = size[trie[p][right]] - size[trie[p][left]]
            val next = if (cnt + gt < k) {
                gt += cnt
                p xor 1
            } else {
                ans = 1L shl b or ans
                p
            }
            left = trie[next][left]
            right = trie[next][right]
        }
        return ans
    }
    fun addNum(num: Long) {
        roots[rootIdx] = idx++
        size[roots[rootIdx]] = size[roots[rootIdx - 1]] + 1
        insert(roots[rootIdx - 1], roots[rootIdx], num)
        rootIdx++
    }

    data class Pkg(val v: Long, val i: Int, val k: Int)
    val heap = KHeap<Pkg>(n + 1) { a, b -> -a.v.compareTo(b.v) }
    addNum(0)
    for ((i, sum) in sums.withIndex()) {
        addNum(sum)
        heap.offer(Pkg(kth(roots[1], roots[i + 1], sum, 1), i, 1))
    }
    var ans = 0L
    repeat(k) {
        val (top, i, pre) = heap.poll()
        val cur = pre + 1
        if (cur <= minOf(k, i)) heap.offer(Pkg(kth(roots[1], roots[i + 1], sums[i], cur), i, cur))
        ans += top
    }
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}