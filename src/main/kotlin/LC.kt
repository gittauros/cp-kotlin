import com.tauros.cp.structure.IntBlockSortedList

class Solution {
    fun resultArray(nums: IntArray): IntArray {
        val (list1, list2) = IntBlockSortedList(intArrayOf(nums[0])) to IntBlockSortedList(intArrayOf(nums[1]))
        val (arr1, arr2) = mutableListOf(nums[0]) to mutableListOf(nums[1])
        for (i in 2 until nums.size) {
            val gt1 = list1.size - 1 - list1.floorIndex(nums[i])
            val gt2 = list2.size - 1 - list2.floorIndex(nums[i])
            if (gt1 > gt2 || gt1 == gt2 && arr1.size <= arr2.size) {
                arr1.add(nums[i]); list1.add(nums[i])
            } else {
                arr2.add(nums[i]); list2.add(nums[i])
            }
        }
        return arr1.toIntArray() + arr2.toIntArray()
    }
}

fun main(args: Array<String>) {
    val starTime = System.currentTimeMillis()
    println("duration: ${System.currentTimeMillis() - starTime} ms")
}

private fun strArr(str: String) = str.trimStart('[').trimEnd(']').split(",").map { it.trimStart('\"').trimEnd('\"') }.toTypedArray()
private fun intArr(str: String) = str.trimStart('[').trimEnd(']').split(",").map { it.toInt() }.toIntArray()
private fun intArrArr(str: String) = str.trimStart('[').trimEnd(']').split("],[").map { intArr(it) }.toTypedArray()
data class ListNode(var `val`: Int, var next: ListNode? = null)
data class TreeNode(var `val`: Int, var left: TreeNode? = null, var right: TreeNode? = null)