package com.tauros.cp.archive.revert

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.mso
import com.tauros.cp.structure.IIHeap

/**
 * @author tauros
 * 2024/4/6
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/730/I
    // 返回贪心例题
    val (n, p, s) = rd.na(3)
    val pVal = rd.na(n)
    val sVal = rd.na(n)

    val (sHeap, pHeap) = IIHeap { a, b -> -a.compareTo(b) } to IIHeap { a, b -> -a.compareTo(b) }
    val psHeap = IIHeap { a, b -> -a.compareTo(b) }
    val (inp, ins) = bar(n) to bar(n)
    var ans = 0
    val sorted = (0 until n).sortedBy { -sVal[it] }
    for ((cnt, i) in sorted.withIndex()) {
        if (cnt < s) {
            // 先选s个运动最强的选手
            ans += sVal[i]
            psHeap.offer(pVal[i] - sVal[i] to i)
            ins[i] = true
        } else {
            // 其余的放入两个大根堆中待选
            pHeap.offer(pVal[i] to i)
            sHeap.offer(sVal[i] to i)
        }
    }
    repeat(p) {
        // 清空已选择的
        while (pHeap.isNotEmpty() && pHeap.peek().let { (_, j) -> ins[j] || inp[j] }) pHeap.poll()
        while (sHeap.isNotEmpty() && sHeap.peek().let { (_, j) -> ins[j] || inp[j] }) sHeap.poll()
        // 比较两个策略哪个更大：
        // - 直接选最大的编程高手
        // - 从运动高手中挖人
        if (pHeap.peek().first > psHeap.peek().first + sHeap.peek().first) {
            // 直接选最大的编程高手
            val (top, j) = pHeap.poll()
            ans += top; inp[j] = true
        } else {
            // 从运动高手中挖人
            val (psTop, psj) = psHeap.poll()
            val (sTop, sj) = sHeap.poll()
            ans += psTop + sTop
            ins[psj] = false; inp[psj] = true;
            psHeap.offer(pVal[sj] - sVal[sj] to sj)
            ins[sj] = true
        }
    }

    val (sList, pList) = (0 until n).filter { ins[it] } to (0 until n).filter { inp[it] }
    wt.println(ans)
    for (res in pList) wt.print("${res + 1} ")
    wt.println()
    for (res in sList) wt.print("${res + 1} ")
    wt.println()
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}