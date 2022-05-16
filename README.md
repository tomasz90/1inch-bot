# 1inch

This project is implementation of simple trading bot which looks for differences between stablecoin prices.
It uses 1inch api to quote popular stablecoins: DAI, USDC, USDT, UST, TUSD requesting for different trade sizes.

Features:
* request rate limiter
* gas station client
* telegram client which sends message after successful trade
* possiblity to choose between different chains
* setting minimal swap quote / maximal swap quote
* setting minimal trade advantage
* excludeing tokens / protocols
* setting maximal token share in terms of all stablecoins

This in many cases naive implementation was able to earn some real profits. However in most cases using api like 1inch is too slow to compete with other specialized bots and in long run can cause loses because of many failed transactions.
