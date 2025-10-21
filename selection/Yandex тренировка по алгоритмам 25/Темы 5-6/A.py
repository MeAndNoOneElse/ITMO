a, b, S = map(int, input().split())


D = (a - b) ** 2 + 4 * S

if D < 0:
    print(-1)
else:
    sqrt_D = int(D ** 0.5)

    if sqrt_D * sqrt_D != D:
        print(-1)
    else:
        L1 = (a + b + sqrt_D) // 2
        L2 = (a + b - sqrt_D) // 2

        result = -1
        if L1 > 0 and (L1 - a) * (L1 - b) == S and L1 >= a and L1 >= b:
            result = L1
        elif L2 > 0 and (L2 - a) * (L2 - b) == S and L2 >= a and L2 >= b:
            result = L2

        print(result)