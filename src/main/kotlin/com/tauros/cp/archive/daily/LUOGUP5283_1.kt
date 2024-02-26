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
    val cap = 31
    var idx = 0
    val trie = ar(2) { iar((n + 1) * (cap + 1) + 1) }
    val size = iar((n + 1) * (cap + 1) + 1)

    fun insert(num: Long) {
        var iter = 0
        for (b in cap downTo 0) {
            val p = (num shr b and 1).toInt()
            if (trie[p][iter] == 0) trie[p][iter] = ++idx
            iter = trie[p][iter]
            size[iter]++
        }
    }
    for (sum in sums) insert(sum)

    fun kth(num: Long, kth: Int): Long {
        var ans = 0L
        var gt = 0
        var iter = 0
        for (b in cap downTo 0) {
            val choose = (num shr b and 1).toInt() xor 1
            val cnt = size[trie[choose][iter]]
            val next = if (cnt + gt < kth) {
                gt += cnt
                choose xor 1
            } else {
                ans = 1L shl b or ans
                choose
            }
            iter = trie[next][iter]
        }
        return ans
    }

    data class Pkg(val v: Long, val idx: Int, val kth: Int)
    val heap = KHeap<Pkg>(n + 1) { a, b -> -a.v.compareTo(b.v) }
    for ((i, sum) in sums.withIndex()) heap.offer(Pkg(kth(sum, 1), i, 1))
    var ans = 0L
    repeat(2 * k) {
        val (top, i, pre) = heap.poll()
        val cur = pre + 1
        if (cur <= minOf(2 * k, n)) heap.offer(Pkg(kth(sums[i], cur), i, cur))
        ans += top
    }
    ans /= 2
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}