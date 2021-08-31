import java.math.BigDecimal
import java.math.BigInteger
import kotlin.properties.Delegates

class Amount private constructor(private val decimals: Int) {

    lateinit var origin: BigInteger
    var readable by Delegates.notNull<Double>()
    private lateinit var multiplication: BigDecimal

    constructor(readable: Double, decimals: Int) : this(decimals) {
        this.readable = readable
        multiplication = calcMultiply()
        origin = calcOrigin()
    }

    constructor(origin: BigInteger, decimals: Int) : this(decimals) {
        this.origin = origin
        multiplication = calcMultiply()
        readable = calcReadable()
    }

    private fun calcMultiply() = BigDecimal.valueOf(10L).pow(decimals)


    private fun calcOrigin(): BigInteger {
        val bigDec = readable.toBigDecimal()
        return bigDec.multiply(multiplication).toBigInteger()
    }

    private fun calcReadable(): Double {
        val bigDec = origin.toBigDecimal()
        return bigDec.divide(multiplication).toDouble()
    }
}