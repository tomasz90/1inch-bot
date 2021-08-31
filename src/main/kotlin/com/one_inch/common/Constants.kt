import com.one_inch.common.Chain
import com.one_inch.quote_request.api.data.Token

const val BASE_URL = "https://api.1inch.exchange/"
const val MATIC_RPC_URL = "***REMOVED***"
const val BSC_RPC_URL = "https://bsc-dataseed.binance.org/"
const val DEFAULT_DECIMALS = 18
const val WAIT_MESSAGE = "\n---------------- WAIT ----------------"

val BSC_DAI = Token("BSC_DAI", "0x1af3f329e8be154074d8769d1ffa4ee058b1dbc3", DEFAULT_DECIMALS)
val BSC_UST = Token("BSC_UST", "0x23396cf899ca06c4472205fc903bdb4de249d6fc", DEFAULT_DECIMALS)
val BSC_USDC = Token("BSC_USDC", "0x8ac76a51cc950d9822d68b83fe1ad97b32cd580d", DEFAULT_DECIMALS)
val BSC_USDT= Token("BSC_USDT", "0x55d398326f99059ff775485246999027b3197955", DEFAULT_DECIMALS)
val BSC_TUSD = Token("BSC_TUSD", "0x14016e85a25aeb13065688cafb43044c2ef86784", DEFAULT_DECIMALS)

val MATIC_DAI = Token("MATIC_DAI", "0x8f3cf7ad23cd3cadbd9735aff958023239c6a063", DEFAULT_DECIMALS)
val MATIC_UST = Token("MATIC_UST", "0x692597b009d13c4049a947cab2239b7d6517875f", DEFAULT_DECIMALS)
val MATIC_USDC = Token("MATIC_USDC", "0x2791bca1f2de4661ed88a30c99a7a9449aa84174", 6)
val MATIC_USDT= Token("MATIC_USDT", "0xc2132d05d31c914a87c6611c10748aeb04b58e8f", 6)

val BSC_TOKENS = listOf(BSC_DAI, BSC_UST, BSC_USDC, BSC_USDT, BSC_TUSD)
val MATIC_TOKENS = listOf(MATIC_DAI, MATIC_UST, MATIC_USDC, MATIC_USDT)

val BSC = Chain(56, BSC_TOKENS, BSC_RPC_URL)
val MATIC = Chain(137, MATIC_TOKENS, MATIC_RPC_URL)
