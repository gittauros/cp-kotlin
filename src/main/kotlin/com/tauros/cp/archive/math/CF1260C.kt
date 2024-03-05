package com.tauros.cp.archive.math

import com.tauros.cp.common.gcd
import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/2/28
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1260/C
    // 小值为x，大值为y，出来的结果形如 x x x y x x y x x x y
    // 因为y比x稀疏，所以能涂y就涂y，然后考虑y之间最多有多少个x
    // 两个y之间最多填 y - 1 个数
    // 设每个y的部位模x为c，c最小值为lcm时取到为0，然后有 2c 3c 4c ...
    // 以下的除法都是向下取整除法
    // c的取值即为 n*c%x 周期可以推出为 x/gcd ，那么最大的c取值就为 min((x-1)/c*c, (x/gcd-1)*c)
    // 当c值取最大时，y - 1个数中最可能容纳最多的x倍数，相当于 y - 1 的区间延长了 c最大值 长度
    // 所以最多容纳 (y-1+最大c) / x 个，判断是否小于k
    val cases = readln().toInt()
    repeat(cases) {
        val (r, b, k) = readln().split(" ").map(string::toInt)
        val (x, y) = minOf(r, b) to maxOf(r, b)
        val (c, gcd) = y % x to gcd(x, y)
        val maxC = if (c == 0) 0 else minOf((x - 1L) / c * c, (x / gcd - 1L) * c)
        val range = y - 1 + maxC
        val cnt = range / x
        val success = cnt < k
        println(if (success) "OBEY" else "REBEL")
    }
}