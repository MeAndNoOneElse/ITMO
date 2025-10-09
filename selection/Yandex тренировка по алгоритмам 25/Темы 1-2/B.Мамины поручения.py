a, b, c, v0, v1, v2 = map(int, input().split() )
t1 = (min(a, b + c) + min(b, a + c)) / (v0) + (min(a, b + c) + min(b, a + c)) / (v1)
t2 = (min(a, b + c)) / (v0) + (c) / (v1) + (min(b, a + c)) / (v2)
t3 = (min(b, a + c)) / (v0) + (c) / (v1) + (min(a, b + c)) / (v2)
print(min(t1, t2, t3))






     


