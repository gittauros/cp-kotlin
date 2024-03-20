import com.tauros.cp.common.pow

class Solution {
    fun minNonZeroProduct(p: Int): Int {
        val mod = 1e9.toInt() + 7
        val cap = (1L shl p) - 1
        val res = pow((cap - 1) % mod, (cap - 1) / 2, mod)
        val ans = cap % mod * res % mod
        return ans.toInt()
    }
}

fun main(args: Array<String>) {
    val starTime = System.currentTimeMillis()
    Solution().minNonZeroProduct(35)
    println("duration: ${System.currentTimeMillis() - starTime} ms")
}

private fun strArr(str: String) = str.trimStart('[').trimEnd(']').split(",").map { it.trimStart('\"').trimEnd('\"') }.toTypedArray()
private fun intArr(str: String) = str.trimStart('[').trimEnd(']').split(",").map { it.toInt() }.toIntArray()
private fun intArrArr(str: String) = str.trimStart('[').trimEnd(']').split("],[").map { intArr(it) }.toTypedArray()
data class ListNode(var `val`: Int, var next: ListNode? = null)
data class TreeNode(var `val`: Int, var left: TreeNode? = null, var right: TreeNode? = null)