import java.math.BigDecimal
import java.math.BigInteger

class Amount private constructor(private val decimals: Int, var origin: BigInteger?, var readable: Double?) {

    lateinit var multiplication: BigDecimal

    constructor(readable: Double, decimals: Int) : this(decimals, null, readable) {
        multiplication = calcMultiply()
        origin = calcOrigin()
    }

    constructor(origin: BigInteger, decimals: Int) : this(decimals, origin, null) {
        multiplication = calcMultiply()
        readable = calcReadable()
    }

    private fun calcMultiply() = BigDecimal.valueOf(10L).pow(decimals)


    private fun calcOrigin(): BigInteger {
        val bigDec = readable!!.toBigDecimal()
        return bigDec.multiply(multiplication).toBigInteger()
    }

    private fun calcReadable(): Double {
        val bigDec = origin!!.toBigDecimal()
        return bigDec.divide(multiplication).toDouble()
    }
}