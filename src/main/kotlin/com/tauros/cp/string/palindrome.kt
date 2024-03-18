package com.tauros.cp.string


/**
 * @author tauros
 */
fun CharArray.manacher(): IntArray {
    // rad为半径
    val rad = IntArray(this.size)
    var (l, r) = 0 to -1
    for (i in rad.indices) {
        // j为i关于l r中心对称的位置
        val j = l + r - i
        if (j >= l && j - rad[j] + 1 > l) rad[i] = rad[j]
        else {
            if (i < r) rad[i] = r - i + 1
            while (i - rad[i] >= 0 && i + rad[i] < rad.size && this[i + rad[i]] == this[i - rad[i]]) rad[i]++
            l = i - rad[i] + 1; r = i + rad[i] - 1
        }
    }
    return rad
}
fun String.manacher() = this.toCharArray().manacher()
fun CharArray.manacher(delimiter: Char) = CharArray(this.size * 2 + 1) { if (it and 1 == 1) this[it shr 1] else delimiter }.manacher()
fun String.manacher(delimiter: Char) = this.toCharArray().manacher(delimiter)