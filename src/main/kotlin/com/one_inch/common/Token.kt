import java.math.BigDecimal
import java.math.BigInteger
import kotlin.properties.Delegates

open class Token constructor(val symbol: String, val address: String, private val decimals: Int) {

    var readable by Delegates.notNull<Double>()
    lateinit var origin: BigInteger
    private var multiplication: BigDecimal

    init {
        multiplication = calcMultiply()
    }

    constructor(symbol: String, address: String, decimals: Int, origin: BigInteger) : this(symbol, address, decimals) {
        this.origin = origin
        this.readable = calcReadable()
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

    fun setQuote(readable: Double) {
        this.readable = readable
        this.origin = calcOrigin()
    }
}