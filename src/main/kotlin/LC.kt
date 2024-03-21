

fun main(args: Array<String>) {
    val starTime = System.currentTimeMillis()
    println("duration: ${System.currentTimeMillis() - starTime} ms")
}

private fun strArr(str: String) = str.trimStart('[').trimEnd(']').split(",").map { it.trimStart('\"').trimEnd('\"') }.toTypedArray()
private fun intArr(str: String) = str.trimStart('[').trimEnd(']').split(",").map { it.toInt() }.toIntArray()
private fun intArrArr(str: String) = str.trimStart('[').trimEnd(']').split("],[").map { intArr(it) }.toTypedArray()
data class ListNode(var `val`: Int, var next: ListNode? = null)
data class TreeNode(var `val`: Int, var left: TreeNode? = null, var right: TreeNode? = null)