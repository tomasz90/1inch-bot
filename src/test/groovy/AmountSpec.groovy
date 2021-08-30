import spock.lang.Specification

class AmountSpec extends Specification {

    def "should convert readable to origin"(double readable, int decimals, BigInteger origin) {
        given:
            def amount = new Amount(readable, decimals)
        expect:
            amount.origin == origin
        where:
            readable  | decimals || origin
            0.00002   | 18        | new BigInteger("20000000000000")
            1.0       | 6        || new BigInteger("1000000")
            200000    | 9        || new BigInteger("200000000000000")
            300000.00 | 18       || new BigInteger("300000000000000000000000")
    }

    def "should convert origin to readable"(BigInteger origin, int decimals, double readable) {
        given:
            def amount = new Amount(readable, decimals)
        expect:
            amount.readable == readable
        where:
            origin                                     | decimals || readable
            new BigInteger("1000000")                  | 6        || 1.0
            new BigInteger("200000000000000")          | 9        || 200000
            new BigInteger("300000000000000000000000") | 18       || 300000.00
    }
}
