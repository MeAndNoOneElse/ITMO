n = int(input())

ans = [0]*31

ans[0] = 1
ans[1] = 2
ans[2] = 4

for i in range(3, n+1):
    ans[i] = ans[i-1]+ans[i-2]+ans[i-3]
print(ans[n-1])