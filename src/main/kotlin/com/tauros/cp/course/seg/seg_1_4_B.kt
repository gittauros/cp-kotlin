package com.tauros.cp.course.seg

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.withMod
import com.tauros.cp.math.Matrix
import com.tauros.cp.structure.Seg
import com.tauros.cp.structure.SegNonTagNode

/**
 * @author tauros
 * 2024/3/27
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
    val (mod, n, m) = rd.na(3)

    withMod(mod) {
        data class Info(val prod: Matrix = Matrix.one(2)) : SegNonTagNode<Info> {
            override fun update(l: Info, r: Info) {
                (l.prod * r.prod).copyInto(prod)
            }
        }
        val seg = Seg(n, { ar(it) { Info() } }) {
            for (i in 0 until 2) for (j in 0 until 2) {
                prod[i][j] = rd.ni()
            }
        }
        repeat(m) {
            val (l, r) = rd.ni() - 1 to rd.ni() - 1
            val ans = seg.query(l, r + 1, { prod }, Matrix::times)
            for (i in 0 until 2) {
                for (res in ans[i]) wt.print("$res ")
                wt.println()
            }
            wt.println()
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}