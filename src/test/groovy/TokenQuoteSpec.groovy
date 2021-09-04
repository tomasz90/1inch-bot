import com.oneinch.object.Token
import com.oneinch.object.TokenQuote
import spock.lang.Specification

class TokenQuoteSpec extends Specification {

    def symbol = "USDC"
    def address = "0x0fad488c45e44B72A17e4eBFc20ce16ff284de3E"

    def "should convert readable to origin"(double readable, int decimals, BigInteger origin) {
        given:
            def token = new Token(symbol, address, decimals)
            def tokenQuote = new TokenQuote(token, readable)
        expect:
            tokenQuote.origin == origin
        where:
            readable  | decimals || origin
            0.00002   | 18        | new BigInteger("20000000000000")
            1.0       | 6        || new BigInteger("1000000")
            200000    | 9        || new BigInteger("200000000000000")
            300000.00 | 18       || new BigInteger("300000000000000000000000")
    }

    def "should convert origin to readable"(BigInteger origin, int decimals, double readable) {
        given:
            def token = new Token(symbol, address, decimals)
            def tokenQuote = new TokenQuote(token, origin)
        expect:
            tokenQuote.readable == readable
        where:
            origin                                     | decimals || readable
            new BigInteger("1")                        | 18       || 0.000000000000000001
            new BigInteger("1000000")                  | 6        || 1.0
            new BigInteger("200000000000000")          | 9        || 200000
            new BigInteger("300000000000000000000000") | 18       || 300000.00
    }
}
