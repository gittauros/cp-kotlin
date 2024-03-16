package com.tauros.cp.archive.funny

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/15
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    // https://codeforces.com/contest/1948/problem/E
    // 赛时没做出来，想到了一半，但是推错了陷入了思维陷阱，以为直接12346...就是最大的
    // |i-j|最多到k-1，这时是连续的k个数，他们之间的差值绝对值一定小于等于k-1（赛时就想到这里，后面就全想错了），接下来看看让k个直接成为一组
    // 假设就考虑 1~k，a1和ak只差1的话那么满足|1-k|+|a1-ak|<=k，2到k可以支持|a2-ak|=2，3到k可以支持|a3-ak|=3
    // 考虑在1~k的中间的两个数l和r，|l-r|=1，那么支持|l-r|=k-1
    // 那么a1~al可以递增，ak~ar可以递减，下标越近的两项，差值越大，验证下是否可以成立
    // k为偶数情况下，1~l和r~k都是k/2个数; 那么r=k-k/2+1=k/2+1，l=k/2; |a1-ak|=1，al-a1=k/2-1，ak-ar=k/2-1，那么|al-ar|=1+k/2-1+k/2-1=k-1，刚好满足条件
    // k为奇数情况下，中间是(1+k)/2，l=(1+k)/2，r=(1+k)/2+1; 那么al-a1=(1+k)/2-1，ak-ar=k-(1+k)/2-1=(k-3)/2，那么|al-ar|=1+(1+k)/2-1+(k-3)/2=k-1，也刚好满足条件
    // 也就是中间互相靠近的两项两两都能连边，考虑固定一个左端点下标，移动右端点时，数值只差1，下标也只差1，也是刚好满足的，因此k个一组完全成立
    // 方法就是求出l和r，a1~al递增，ak~ar递减，同时al和ar差距为k-1
    // 那么al就是k，ar就是1，就可以把1~k个数安排在这k个下标上
    val cases = rd.ni()
    repeat(cases) {
        val (n, k) = rd.ni() to rd.ni()

        val nums = iar(n) { it + 1 }
        var ans = iar(0)
        var st = 0
        while (st < n) {
            val ed = minOf(n, st + k)
            val mid = (st + ed) / 2
            val res = nums.copyOfRange(mid, ed) + nums.copyOfRange(st, mid)
            ans += res; st = ed
        }

        for (res in ans) wt.print("$res ")
        wt.println()
        wt.println((n + k - 1) / k)
        for (i in 0 until n) wt.print("${i / k + 1} ")
        wt.println()
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}