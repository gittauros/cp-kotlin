package com.tauros.cp.archive.funny

import com.tauros.cp.common.IIP
import com.tauros.cp.common.int
import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/3/8
 */
fun main(args: Array<String>) {
    // https://codeforces.com/contest/1934/problem/C
    // 找特殊点来问，画图试了很久任意位置或(n/2, m/2)，怎么画都有最终的四段线需要根据距离判断，很麻烦
    // 但其实角点才是特殊点，可以让第一问就退化成一条线段
    // 可以这么理解，询问角点相当于多给了一次询问，利用上了答案一定在n*m的矩形区域内这个特点
    // 那么第一问询问(1, 1)这个点，之后推一推就好了
    fun ask(pos: IIP): int {
        val (i, j) = pos
        println("? $i $j")
        return readln().toInt()
    }
    fun out(pos: IIP) {
        val (i, j) = pos
        println("! $i $j")
    }
    val cases = readln().toInt()
    repeat(cases) {
        val (n, m) = readln().split(" ").map(string::toInt)

        val dist = ask(1 to 1)
        if (dist == 0) {
            out(1 to 1)
            return@repeat
        }

        val upPos = if (dist + 1 <= m) 1 to dist + 1 else dist + 1 - m + 1 to m
        val up = ask(upPos)
        if (up == 0) {
            out(upPos)
            return@repeat
        }
        val downPos = if (dist + 1 <= n) dist + 1 to 1 else n to dist + 1 - n + 1
        val down = ask(downPos)
        if (down == 0) {
            out(downPos)
            return@repeat
        }

        val upFixed = upPos.first + up / 2 to upPos.second - up / 2
        val downFixed = downPos.first - down / 2 to downPos.second + down / 2
        if (down % 2 == 1) {
            out(upFixed)
        } else if (up % 2 == 1) {
            out(downFixed)
        } else {
            if (ask(upFixed) == 0) out(upFixed)
            else out(downFixed)
        }
    }
}