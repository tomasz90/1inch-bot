fun String.addDecimals(int: Int): String {
    var decimals = ""
    repeat(int) {
        decimals += "0"
    }
    return this + decimals
}

fun String.removeDecimals(int: Int) = this.subSequence(0, this.length - int)

fun calculateAdvantage(value1: String, value2: String)
     = (value2.toDouble() - value1.toDouble()) / value1.toDouble() * 100
