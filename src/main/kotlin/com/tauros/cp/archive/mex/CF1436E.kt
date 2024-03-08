package com.tauros.cp.archive.mex

import com.tauros.cp.bar
import com.tauros.cp.common.int
import com.tauros.cp.common.string
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/3/8
 */
fun main(args: Array<String>) {
    // https://codeforces.com/contest/1436/problem/E
    // 由求出每个mex是否存在的思路思考到了针对每个位置i，收集前面每个数字最后出现的位置（最大的下标），然后就不知道怎么处理了
    // 由此继续出发，求每个位置a[i]是否能成为子数组的mex要求排除a[i]
    // 于是可以求上一个a[i]的值出现到当前i之间是否有[1, a[i]-1]所有的数
    // 可以随着遍历用线段树更新每个a[i]的下标，查询时查询[1, a[i]-1]的最小下标
    // 如果最小下标大于上次a[i]出现的下标，那么说明[1, a[i]-1]在这个区间全部出现了，所以a[i]可以为mex
    // mex=1需要特殊处理下，因为1的出现不要求中间隔任何数，条件是只要出现过任何不为1的数字就能有
    // 注意处理数组结束后的分段，尾部还有一段，结束后的分段需要对2到n+1都求一次
    // 最终mex的取值范围是[1, n+2]
    val n = readln().toInt()
    val nums = readln().trim().split(" ").map(string::toInt).toIntArray()

    class Seg(val cl: int, val cr: int) {
        val mid = cl + cr shr 1
        var l: Seg? = null
        var r: Seg? = null
        var min = -1

        fun build() {
            if (cl == cr) return
            l = Seg(cl, mid)
            r = Seg(mid + 1, cr)
            l?.build()
            r?.build()
        }

        fun update(pos: int, idx: int) {
            if (cl == cr) {
                min = idx
                return
            }
            if (pos <= mid) l?.update(pos, idx)
            else r?.update(pos, idx)
            min = minOf(l?.min ?: (n + 1), r?.min ?: (n + 1))
        }

        fun query(st: int, ed: int): int {
            if (cl > ed || cr < st) return n + 1
            if (cl >= st && cr <= ed) return min
            return minOf(l!!.query(st, ed), r!!.query(st, ed))
        }
    }

    val seg = Seg(1, n).apply { build() }
    val mex = bar(n + 2)
    val last = iar(n + 2) { -1 }
    for (i in 1 .. n) {
        val num = nums[i - 1]
        if (num != 1) {
            val far = seg.query(1, num - 1)
            if (far > last[num]) mex[num] = true
        }
        last[num] = i; seg.update(num, i)
    }
    for (num in 2 .. n + 1) {
        val far = seg.query(1, num - 1)
        if (far > last[num]) mex[num] = true
    }
    mex[1] = nums.any { it != 1 }

    val ans = (1 .. n + 1).firstOrNull { !mex[it] } ?: (n + 2)
    println(ans)
}