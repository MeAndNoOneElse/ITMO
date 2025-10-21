def main():
    n = int(input())
    a = list(map(int, input().split()))
    b = list(map(int, input().split()))

    if sum(a) > sum(b):
        print(-1)
        return

    possible_with_zero = True
    for i in range(n):
        if a[i] > b[i]:
            possible_with_zero = False
            break


    if possible_with_zero:
        print(0)
        return

    left, right = 0, n

    while right - left >1:
        mid = (left + right) // 2

        capacity = b.copy()
        possible = True
        current_day = 0

        for i in range(n):
            remaining = a[i]
            current_day = max(current_day, i-mid)
            right_range = min(n - 1, i + mid)

            while remaining > 0 and current_day <= right_range:
                if capacity[current_day] > 0:
                    use = min(remaining, capacity[current_day])
                    capacity[current_day] -= use
                    remaining -= use

                if remaining == 0:
                    break
                if capacity[current_day] == 0:
                    current_day += 1


            if remaining > 0:
                possible = False
                break

        if possible:
            right = mid
        else:
            left = mid

    print(right)


if __name__ == "__main__":
    main()