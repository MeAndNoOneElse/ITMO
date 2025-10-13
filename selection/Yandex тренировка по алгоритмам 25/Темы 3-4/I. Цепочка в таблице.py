def main():
    n, m = map(int, input().split())
    grid = []
    for _ in range(n):
        row = list(map(int, input().split()))
        grid.append(row)

    cells = []
    for i in range(n):
        for j in range(m):
            cells.append((grid[i][j], i, j))

    cells.sort(reverse=True)

    dp = [[1] * m for _ in range(n)]
    max_chain = 1

    for num, i, j in cells:
        neighbors = [
            (i - 1, j),  # верх
            (i + 1, j),  # низ
            (i, j - 1),  # лево
            (i, j + 1)  # право
        ]

        for ni, nj in neighbors:
            if 0 <= ni < n and 0 <= nj < m:
                if grid[ni][nj] == num + 1:
                    dp[i][j] = max(dp[i][j], dp[ni][nj] + 1)
                    max_chain = max(max_chain, dp[i][j])

    print(max_chain)


if __name__ == "__main__":
    main()