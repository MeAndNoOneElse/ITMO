def main():
    n, k = map(int, input().split())

    A = list(map(int, input().split()))

    prefix_sum = [0] * (n + 1)
    for i in range(1, n + 1):
        prefix_sum[i] = prefix_sum[i - 1] + A[i - 1]

    tower_value = [0] * (n - k + 1)
    for i in range(n - k + 1):
        total = prefix_sum[i + k] - prefix_sum[i]
        min_val = min(A[i:i + k])
        tower_value[i] = total * min_val

    dp = [0] * (n + 1)
    prev = [-1] * (n + 1)

    for i in range(1, n + 1):
        dp[i] = dp[i - 1]
        prev[i] = prev[i - 1]

        if i >= k:
            start = i - k
            candidate = dp[start] + tower_value[start]
            if candidate > dp[i]:
                dp[i] = candidate
                prev[i] = start

    towers = []
    pos = n

    while pos > 0:
        if prev[pos] == -1:
            pos -= 1
        elif prev[pos] == prev[pos - 1] if pos > 0 else False:
            pos -= 1
        else:
            towers.append(prev[pos] + 1)
            pos = prev[pos]

    towers.reverse()

    print(len(towers))
    if towers:
        print(' '.join(map(str, towers)))


if __name__ == "__main__":
    main()
