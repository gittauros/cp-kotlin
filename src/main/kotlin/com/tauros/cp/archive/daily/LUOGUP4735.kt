package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iao
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/11/24
 */
private val bufCap = 512
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val (n, m) = rd.ni() to rd.ni()
    val cap = 23
    val nodeCap = (n + m + 1) * (cap + 2) + 1
    val trie = ar(2) { iar(nodeCap) }
    val size = iar(nodeCap)
    val roots = iar(nodeCap)
    var newNode = 1
    fun insert(old: Int, new: Int, num: Int) {
        var (iterOld, iterNew) = old to new
        for (b in cap downTo 0) {
            val bit = num shr b and 1
            trie[bit][iterNew] = newNode
            size[newNode] = size[trie[bit][iterOld]] + 1
            trie[bit xor 1][iterNew] = trie[bit xor 1][iterOld]

            iterNew = trie[bit][iterNew]
            iterOld = trie[bit][iterOld]
            newNode++
        }
    }
    fun max(l: Int, r: Int, num: Int): Int {
        var ans = 0
        var (iterL, iterR) = l to r
        for (b in cap downTo 0) {
            val p = num shr b and 1 xor 1
            val lCnt = size[trie[p][iterL]]
            val rCnt = size[trie[p][iterR]]
            val next = if (rCnt - lCnt > 0) {
                ans = 1 shl b or ans
                p
            } else p xor 1
            iterL = trie[next][iterL]
            iterR = trie[next][iterR]
        }
        return ans
    }
    var sum = 0
    var idx = 1
    fun addNum(num: Int) {
        sum = sum xor num
        roots[idx] = newNode++
        size[roots[idx]] = size[roots[idx - 1]] + 1
        insert(roots[idx - 1], roots[idx], sum)
        idx++
    }
    addNum(0)
    repeat(n) { addNum(rd.ni()) }
    repeat(m) {
        val op = rd.nc()
        if (op == 'A') addNum(rd.ni())
        else {
            val (l, r, x) = iao(maxOf(rd.ni(), 1), minOf(rd.ni(), idx - 1), rd.ni())
            val find = x xor sum
            val ans = max(roots[l - 1], roots[r], find)
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}