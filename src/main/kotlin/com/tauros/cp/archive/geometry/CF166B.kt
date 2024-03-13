package com.tauros.cp.archive.geometry

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.findFirst
import com.tauros.cp.geometry.IPoint2

/**
 * @author tauros
 * 2024/3/13
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/166/B
    // A是凸多边形，那么B只要每个点都在A内就好
    // 多边形可以切成若干个三角形，二分点在那个三角形，判断下在不在里面，叉乘搞搞就好
    // 给出的点的顺序是CW的，reverse一下就是CCW
    val n = rd.ni()
    val aPoints = ar(n) { IPoint2(rd.ni(), rd.ni()) }.apply { reverse() }
    val m = rd.ni()
    val bPoints = ar(m) { IPoint2(rd.ni(), rd.ni()) }

    //            /|← 点z
    //          /  |
    // out    /    |
    //      /  in  |
    //    /        |  out
    //  /__________|
    // ↑ 点x  out  ↑ 点y
    // fixed点x为A的CCW序的第一个点，通过叉乘关系二分出点y和点z
    // 如果二分到 xz边在A之外 或 xy边在A之外 那么失败
    // 如果 xy边为A的边界 或 xz边为A的边界 此时如果 xb和xy共线 或 xb和xz共线 那么失败
    // 其它情况下，仅需判断 yb向量在yz向量左侧 否则失败
    val fixed = aPoints[0]
    for (point in bPoints) {
        val bVector = point - fixed
        val left = findFirst(1, n) { bVector cross (aPoints[it] - fixed) > 0 }
        if (left == 1 || left == n) {
            // xz边在A之外 或 xy边在A之外
            wt.println("NO"); return
        }
        if (left == n - 1 && (aPoints[left] - fixed) isParallel bVector ||
            left == 2 && (aPoints[left - 1] - fixed) isParallel bVector) {
            // (xz边为A的边界 且 xb和xz共线) 或 (xy边为A的边界 且 xb和xy共线)
            wt.println("NO"); return
        }
        val bound = aPoints[left] - aPoints[left - 1]
        val inner = point - aPoints[left - 1]
        if (bound cross inner <= 0) {
            // yb向量不在yz向量左侧
            wt.println("NO"); return
        }
    }
    wt.println("YES")
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}