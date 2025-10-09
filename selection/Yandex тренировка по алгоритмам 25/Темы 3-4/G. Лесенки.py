def main():
    n = int(input().strip())

    dp = [0] * (n + 1)
    dp[0] = 1

    for k in range(1, n + 1):
        for i in range(n, k - 1, -1):
            dp[i] += dp[i - k]

    print(dp[n])


if __name__ == "__main__":
    main()