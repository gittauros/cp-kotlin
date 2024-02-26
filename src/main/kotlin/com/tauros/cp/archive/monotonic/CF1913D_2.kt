package com.tauros.cp.archive.monotonic

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.int
import com.tauros.cp.common.mint
import com.tauros.cp.common.withMod
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1913/D
    val cases = rd.ni()
    repeat(cases) {
        val n = rd.ni()
        val nums = rd.na(n)

        val tree = ar(n) { iar(2) { -1 } }
        val stack = iar(n)
        var top = -1
        for ((i, num) in nums.withIndex()) {
            while (top >= 0 && num < nums[stack[top]]) tree[i][0] = stack[top--]
            if (top >= 0) tree[stack[top]][1] = i
            stack[++top] = i
        }
        val root = stack[0]

        withMod(998244353) {
            fun dp(u: int, st: int, ed: int): mint {
                if (u == -1) return mint.ONE
                val l = dp(tree[u][0], st, u - 1)
                val r = dp(tree[u][1], u + 1, ed)
                // u没被删除，可由左子树配合右子树组成
                var res = l * r
                // u左边有比其小的数，u可被左边删除，此时多出右边子树组成的答案
                if (st > 0) res += r
                // 与上面相反
                if (ed < n - 1) res += l
                // 重复统计了完全删除的空数组
                if (st > 0 && ed < n - 1) res -= mint.ONE
                return res
            }
            val ans = dp(root, 0, n - 1)
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}