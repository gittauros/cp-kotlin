package com.tauros.cp.archive.funny

import com.tauros.cp.common.IIP
import com.tauros.cp.common.int

/**
 * @author tauros
 * 2024/2/29
 */

fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1937/C
    // 赛时没做出来，因为没想到如何找最小值，漏掉了重要信息
    // 重要信息：
    // - i or i = i，所以询问 i i j j 可以比较i和j的大小关系
    // - xor最大值和or最大值都是从最大值最高位开始往下二进制每个1都填满
    // - 方法一: i x i y的询问可以找出和i或出来的最大值，设为j，但是最大值可能是i也可能是j
    // -        并且收集和i与j分别取到最大值的下标列表，比较一下i取到最大值和j取到最大值哪个更大，留下那个为a和a的最大值下标列表
    // - 方法二: 或者直接用 i i j j 找出最大值a，然后用 a x a y 收集a取最大值的下标列表
    // - 方法一找到的不一定是 n-1 这个最大值，但是也能构成异或最大值，因为最高位的1它依然是有的
    // - 手玩下对于a的最大值下标取值列表，这个集合是肯定包含与a取得异或最大值的那个下标的
    // - 另一个下标b就是a最大值下标取值列表中的最小值，用 i i j j 方法求出来就好了
    val cases = readln().toInt()
    fun out(i: int, j: int) {
        println("! $i $j")
    }
    fun cmp(l: IIP, r: IIP): int {
        val (a, b) = l
        val (c, d) = r
        println("? $a $b $c $d")
        val char = readln()[0]
        return if (char == '>') 1 else if (char == '=') 0 else -1
    }
    repeat(cases) {
        val n = readln().toInt()
        if (n == 2) {
            out(0, 1)
            return@repeat
        }

        val x = 0
        var max0 = 1
        for (q in 2 until n) {
            val cmp = cmp(x to q, x to max0)
            if (cmp == 1) max0 = q
        }

        val m0gt0 = cmp(max0 to max0, 0 to 0)
        val a = if (m0gt0 >= 0) max0 else 0
        val candidates = buildList {
            var iter = max0 - a
            add(iter)
            for (q in 0 until n) {
                val cmp = cmp(a to q, a to iter)
                if (cmp == 1) {
                    clear()
                    iter = q
                }
                if (cmp >= 0) add(q)
            }
        }

        val b = if (candidates.size == 1) candidates[0] else {
            var min = candidates[0]
            for (i in 1 until candidates.size) {
                val cmp = cmp(candidates[i] to candidates[i], min to min)
                if (cmp <= 0) min = candidates[i]
            }
            min
        }

        out(a, b)
    }
}