def main():
    n = int(input().strip())
    grid = []

    for _ in range(n):
        row = input().strip()
        grid.append(list(row))

    dp = [[-1] * 3 for _ in range(n)]

    for j in range(3):
        if grid[0][j] != 'W':
            dp[0][j] = 1 if grid[0][j] == 'C' else 0

    for i in range(1, n):
        for j in range(3):
            if grid[i][j] == 'W':
                continue

            max_from_above = -1
            for dj in [-1, 0, 1]:
                prev_j = j + dj
                if 0 <= prev_j < 3 and dp[i - 1][prev_j] != -1:
                    max_from_above = max(max_from_above, dp[i - 1][prev_j])

            if max_from_above != -1:
                dp[i][j] = max_from_above + (1 if grid[i][j] == 'C' else 0)

    result = 0
    for i in range(n):
        for j in range(3):
            if dp[i][j] != -1:
                result = max(result, dp[i][j])

    print(result)


if __name__ == "__main__":
    main()
