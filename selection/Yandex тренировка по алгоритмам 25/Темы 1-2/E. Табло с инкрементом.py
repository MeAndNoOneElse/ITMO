l = input().split()
n = int(l[0])
m = int(l[1])

last_digit = n % 10

if last_digit == 0:
    print(n)
elif last_digit == 5:
    if m >= 1:
        n += 5
    print(n)
else:
    cycles = {
        1: [2, 4, 8, 6],  # 1 → 2 → 4 → 8 → 6 → цикл
        2: [4, 8, 6, 2],  # 2 → 4 → 8 → 6 → 2 → цикл
        3: [6, 2, 4, 8],  # 3 → 6 → 2 → 4 → 8 → цикл
        4: [8, 6, 2, 4],  # 4 → 8 → 6 → 2 → 4 → цикл
        6: [2, 4, 8, 6],  # 6 → 2 → 4 → 8 → 6 → цикл
        7: [4, 8, 6, 2],  # 7 → 4 → 8 → 6 → 2 → цикл
        8: [6, 2, 4, 8],  # 8 → 6 → 2 → 4 → 8 → цикл
        9: [8, 6, 2, 4]  # 9 → 8 → 6 → 2 → 4 → цикл
    }

    if last_digit in cycles:
        cycle = cycles[last_digit]

        if m >= 1:
            n += last_digit
            m -= 1

        if m > 0:
            cycle_sum = sum(cycle)

            full_cycles = m // 4
            remaining_steps = m % 4

            n += cycle_sum * full_cycles

            for i in range(remaining_steps):
                n += cycle[i]

    print(n)
