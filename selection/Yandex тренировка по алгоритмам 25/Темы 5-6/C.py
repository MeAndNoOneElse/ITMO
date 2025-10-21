def can_fit(k, N, W, H, words):
    lines_height = []
    current_line_b = -1
    current_sum_a = 0.0

    for a, b in words:
        if current_line_b != b or current_sum_a + a > W / k:
            if current_line_b != -1:
                lines_height.append(current_line_b)
            current_line_b = b
            current_sum_a = a
            if a > W / k:
                return False
        else:
            current_sum_a += a
    if current_line_b != -1:
        lines_height.append(current_line_b)

    total_height = sum(lines_height) * k
    return total_height <= H


def main():
    data = input().split()
    N = int(data[0])
    W = int(data[1])
    H = int(data[2])
    words = []
    for _ in range(N):
        a, b = map(int, input().split())
        words.append((a, b))

    left, right = 0.0, min(W, H) * 10.0
    for _ in range(80):
        mid = (left + right) / 2
        if can_fit(mid, N, W, H, words):
            left = mid
        else:
            right = mid
    print(f"{left:.15f}")


if __name__ == "__main__":
    main()