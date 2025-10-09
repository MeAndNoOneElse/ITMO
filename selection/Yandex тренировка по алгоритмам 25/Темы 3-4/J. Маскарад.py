def main():
    N, L = map(int, input().split())

    shops = []
    total_fabric = 0

    for _ in range(N):
        P, R, Q, F = map(int, input().split())
        shops.append((P, R, Q, F))
        total_fabric += F

    if total_fabric < L:
        print(-1)
        return
    max_L = min(L + 100, total_fabric)

    INF = 10 ** 18
    dp = [INF] * (max_L + 1)
    dp[0] = 0
    purchase = [[0] * (max_L + 1) for _ in range(N)]

    for i in range(N):
        P, R, Q, F = shops[i]
        new_dp = dp[:]

        for current in range(max_L, -1, -1):
            if dp[current] == INF:
                continue

            max_buy = min(F, max_L - current)

            for k in range(max_buy + 1):
                next_amount = current + k

                if k < R:
                    cost = k * P
                else:
                    cost = k * Q

                total_cost = dp[current] + cost

                if total_cost < new_dp[next_amount]:
                    new_dp[next_amount] = total_cost
                    for j in range(i):
                        purchase[j][next_amount] = purchase[j][current]
                    purchase[i][next_amount] = k

        dp = new_dp

    min_cost = INF
    best_amount = -1
    for amount in range(L, max_L + 1):
        if dp[amount] < min_cost:
            min_cost = dp[amount]
            best_amount = amount

    if min_cost == INF:
        print(-1)
    else:
        print(min_cost)
        result = [purchase[i][best_amount] for i in range(N)]
        print(' '.join(map(str, result)))


if __name__ == "__main__":
    main()