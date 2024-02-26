package com.tauros.cp.string


/**
 * @author tauros
 * 2023/9/10
 */
fun CharArray.pmt(): IntArray {
    val pmt = IntArray(this.size)
    var j = 0
    for (i in 1 until this.size) {
        while (j > 0 && this[i] != this[j]) j = pmt[j - 1]
        pmt[i] = if (this[i] == this[j]) ++j else 0
    }
    // 每个位置的border长度
    return pmt
}
fun String.pmt() = this.toCharArray().pmt()

fun CharArray.fpmt(): IntArray {
    val pmt = IntArray(this.size)
    var j = 0
    for (i in 1 until this.size) {
        while (j > 0 && this[i] != this[j]) j = pmt[j - 1]
        pmt[i] =
            if (this[i] == this[j])
                if (i + 1 < this.size && this[i + 1] == this[j + 1]) pmt[j++]
                else ++j
            else 0
    }
    return pmt
}
fun String.fpmt() = this.toCharArray().fpmt()

fun CharArray.zfunc(): IntArray {
    val z = IntArray(this.size)
    var (l, r) = 0 to 0
    for (i in 1 until this.size) {
        if (i <= r && i + z[i - l] - 1 < r) z[i] = z[i - l]
        else {
            if (i <= r) z[i] = r - i + 1
            while (i + z[i] < this.size && this[i + z[i]] == this[z[i]]) z[i]++
            l = i
            r = i + z[i] - 1
        }
    }
    // 每个位置和前缀的lcp
    return z
}
fun String.zfunc() = this.toCharArray().zfunc()