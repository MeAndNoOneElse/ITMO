def main():
    n = int(input().strip())

    if n == 0:
        print(0.0)
        return

    intervals = []

    for _ in range(n):
        data = input().split()
        b = float(data[0])
        e = float(data[1])
        w = float(data[2])
        intervals.append((b, e, w))

    intervals.sort(key=lambda x: x[1])

    starts = [interval[0] for interval in intervals]
    ends = [interval[1] for interval in intervals]
    weights = [interval[2] for interval in intervals]

    p = [-1] * n
    for j in range(n):
        left, right = 0, j - 1
        while left <= right:
            mid = (left + right) // 2
            if ends[mid] <= starts[j]:
                p[j] = mid
                left = mid + 1
            else:
                right = mid - 1

    dp = [0.0] * n
    dp[0] = weights[0]

    for j in range(1, n):
        option1 = dp[j - 1]

        option2 = weights[j]
        if p[j] != -1:
            option2 += dp[p[j]]

        dp[j] = max(option1, option2)

    print(f"{dp[n - 1]:.6f}")


if __name__ == "__main__":
    main()