def r(M, B):
    return sum(B)-sum(M)
n = int(input())
l = input().split()
M = []
B = []
for i in range(n):
    if i%2==0: B.append(int(l[i]))
    else: M.append(int(l[i]))
res1 = r(M, B)
mm = max(M)
mb = min(B)
if mm>mb:
    B.append(mm)
    B.remove(mb)
    M.append(mb)
    M.remove(mm)
res2 = r(M, B)
if res2>=res1:
    print(res2)