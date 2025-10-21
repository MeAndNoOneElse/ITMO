def main():
    n, p = map(int, input().split())
    arr = list(map(int, input().split()))

    indexed = [(arr[i], i + 1) for i in range(n)]
    indexed.sort()

    sorted_vals = [x[0] for x in indexed]
    sorted_indices = [x[1] for x in indexed]

    best_diff = float('inf')
    best_i = -1
    best_j = -1

    for j in range(n):
        target = p * sorted_vals[j]

        left, right = 0, n - 1
        while left < right:
            mid = (left + right) // 2
            if sorted_vals[mid] < target:
                left = mid + 1
            else:
                right = mid

        candidates = []
        if left < n:
            candidates.append(left)
        if left > 0:
            candidates.append(left - 1)

        for idx in candidates:
            if idx == j:
                continue  # i ≠ j
            diff = abs(sorted_vals[idx] / sorted_vals[j] - p)
            if diff < best_diff:
                best_diff = diff
                best_i = sorted_indices[idx]
                best_j = sorted_indices[j]

    print(best_i, best_j)


if __name__ == "__main__":
    main()