n, k = map(int, input().split())
l = list(map(int, input().split(' ')))
only = set(l)
m= len(only)
w = list(only)
if m>=k:
    ans = []
    n = 0
    while len(ans)<k:
        ans.append(w[n])
        n+=1
else:
    ans = []
    for q in w:
        ans.append(q)
        l.remove(q)
    i = 0
    while len(ans) < k:
        f = l[i]
        ans.append(f)
        l.remove(f)
        i+=1
print(*ans)