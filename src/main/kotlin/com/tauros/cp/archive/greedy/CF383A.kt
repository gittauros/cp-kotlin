package com.tauros.cp.archive.greedy

import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/3/20
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/383/A
    // 没做出来
    // 把 0 视作左箭头 ←，把 1 视作右箭头 →。
    // 考虑两个箭头标记的先后顺序：
    // 1. 两个箭头背靠背：先标记谁都不影响答案。
    // 2. 两个箭头面对面：先标记谁都会把答案加一。
    // 3. 两个箭头都朝右：先标记左边的，这样不影响答案。
    // 4. 两个箭头都朝左：先标记右边的，这样不影响答案。
    //
    // 对于朝右的箭头，我们从左往右标记。
    // 对于朝左的箭头，我们从右往左标记。
    //
    // 在这种操作顺序下，只有面对面的箭头会对答案有贡献。
    // 所以只需要统计有多少对箭头是面对面的。
    val n = readln().toInt()
    val nums = readln().split(" ").map(string::toInt).toIntArray()

    var (ans, cnt) = 0L to 0L
    for (num in nums) {
        if (num == 1) cnt += 1
        else ans += cnt
    }
    println(ans)
}