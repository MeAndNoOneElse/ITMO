s = input().strip()

# Случай пустой строки - сразу переправляемся на правый берег
if not s:
    print(1)
    exit()

n = len(s)

dp = [[0] * 2 for _ in range(n + 1)]

dp[0][0] = 0
dp[0][1] = 1

for i in range(1, n + 1):
    river = s[i - 1]
    if river == "L":
        dp[i][1] = dp[i - 1][1]
        dp[i][0] = min(dp[i][1], dp[i-1][0])+1

    if river == "R":
        dp[i][0] = dp[i-1][0]
        dp[i][1] = min(dp[i][0], dp[i-1][1])+1
    if river == "B":
        dp[i][0] = dp[i - 1][0]+1
        dp[i][1] = dp[i - 1][1]+1
result = dp[n][1]
print(result)