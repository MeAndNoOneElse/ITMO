def main():
    n = int(input())
    a = list(map(int, input().split()))
    m = int(input())
    b = list(map(int, input().split()))

    b_sorted = sorted(b)
    prefix_b = [0] * (m + 1)
    for i in range(m):
        prefix_b[i + 1] = prefix_b[i] + b_sorted[i]

    total = 0

    for i in range(n):
        lo, hi = 0, m
        while lo < hi:
            mid = (lo + hi) // 2
            if b_sorted[mid] <= a[i]:
                lo = mid + 1
            else:
                hi = mid
        p = lo
        left_sum = p * a[i] - prefix_b[p]
        right_sum = (prefix_b[m] - prefix_b[p]) - (m - p) * a[i]
        S_A = left_sum + right_sum
        total += (i + 1) * S_A

    a_sorted = sorted(a)
    prefix_a = [0] * (n + 1)
    for i in range(n):
        prefix_a[i + 1] = prefix_a[i] + a_sorted[i]

    for j in range(m):
        lo, hi = 0, n
        while lo < hi:
            mid = (lo + hi) // 2
            if a_sorted[mid] <= b[j]:
                lo = mid + 1
            else:
                hi = mid
        q = lo

        left_sum = q * b[j] - prefix_a[q]
        right_sum = (prefix_a[n] - prefix_a[q]) - (n - q) * b[j]
        S_B = left_sum + right_sum
        total -= (j + 1) * S_B

    print(total)

if __name__ == "__main__":
    main()