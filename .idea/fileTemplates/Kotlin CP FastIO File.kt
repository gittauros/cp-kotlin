#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter

/**
 * @author tauros
 * $DATE
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)

private fun solve() {
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}