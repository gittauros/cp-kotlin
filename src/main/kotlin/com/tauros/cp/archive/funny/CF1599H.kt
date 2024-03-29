package com.tauros.cp.archive.funny

import com.tauros.cp.common.int

/**
 * @author tauros
 * 2024/3/14
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/1599/H
    // 算了，不会，学的题解
    // 选两个角，会确定两根直线，其交点的纵坐标或横坐标与坐标轴平行的线可以确定与目标矩形相交
    // 然后在这个坐标上询问，可以确定两个和坐标轴平行的线，两条线和矩形边是重合的
    // 拿重合的线和最早的两个询问求出的直线求交点，就能求出x和y的min max
    fun ask(i: int, j: int): int {
        println("? $i $j")
        return readln().toInt()
    }

    val inf = 1e9.toInt()
    val ru = ask(1, inf)
    val rd = ask(inf, inf)

    val midX = ((ru - rd + 1L + inf) / 2).toInt()
    val ly = ask(midX, 1) + 1
    val ry = inf - ask(midX, inf)

    val xMin = ru + ry + 1L - inf
    val xMax = inf.toLong() + inf - rd - ry

    println("! $xMin $ly $xMax $ry")
}