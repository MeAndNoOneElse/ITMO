def main():
    n = int(input().strip())

    is_prime = [True] * (n + 1)
    is_prime[0] = is_prime[1] = False
    for i in range(2, int(n ** 0.5) + 1):
        if is_prime[i]:
            for j in range(i * i, n + 1, i):
                is_prime[j] = False

    win = [False] * (n + 1)

    win[0] = False

    for i in range(1, n + 1):
        for take in [1, 2, 3]:
            if i >= take:
                remaining = i - take
                if remaining == 0 or not is_prime[remaining]:
                    if not win[remaining]:
                        win[i] = True
                        break

    if win[n]:
        print(1)
    else:
        print(2)


if __name__ == "__main__":
    main()
