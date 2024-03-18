package com.tauros.cp.archive.string

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.iar
import com.tauros.cp.string.manacher

/**
 * @author tauros
 * 2024/3/18
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/problemset/problem/1943/B
    // 挺妙的，推了一半有个猜测却没推到底，其实结论很简单
    // - 最重要的是[l..r]的字符串需要单独讨论，下面讨论除去这部分的
    // - 有三个或以上种类字符时怎么都不可能其它长度全是回文串
    // - 有两种字符时，只有abababab这种循环形式可以让奇数长度全是回文
    // - 剩下只有一种字符时，全是回文串
    // 那么就是判断 奇数位置是否全是同一字符 和 偶数位置是否全是同一字符
    // 两个都满足时，看是否是只有一种字符，是的话答案是0，否则全部偶数长度都能有函数值贡献
    // 两个只要有一个不满足，那么全部长度都有贡献
    // 最后判断[l..r]是回文的话，从贡献中去除即可
    // 用于练习马拉车的坐标映射比较合适
    val cases = rd.ni()
    repeat(cases) {
        val (n, q) = rd.ni() to rd.ni()
        val str = rd.ns(n)

        val rad = str.manacher('#')
        val left = ar(2) { iar(n) { -1 } }
        for (i in 2 until n) {
            val p = i and 1
            left[p][i] = if (str[i] != str[i - 2]) i - 2 else left[p][i - 2]
        }

        repeat(q) {
            val (l, r) = rd.ni() - 1 to rd.ni() - 1
            val p = r and 1
            val (o, e) = (left[1][r - (1 - p)] < l) to (left[0][r - p] < l)
            val (len, mid) = r - l + 1 to (l + r shr 1)
            val pal =
                if (len % 2 == 0) rad[2 * mid + 2] >= len
                else rad[2 * mid + 1] >= len + 1
            val ans =
                if (o && e && str[r] == str[r - 1]) 0L
                else if (o && e) {
                    val last = len - (len and 1)
                    (2L + last) * ((last - 2) / 2 + 1) / 2 - if (len % 2 == 0 && pal) len else 0
                } else (2L + len) * (len - 1) / 2 - if (pal) len else 0
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}