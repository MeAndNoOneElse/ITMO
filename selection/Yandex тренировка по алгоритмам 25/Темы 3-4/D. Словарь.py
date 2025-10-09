def main():
    text = input().strip()
    n = int(input().strip())

    dictionary = set()
    for _ in range(n):
        word = input().strip()
        dictionary.add(word)

    length = len(text)

    dp = [False] * (length + 1)
    dp[0] = True

    prev = [-1] * (length + 1)

    for i in range(1, length + 1):
        for j in range(i):
            if dp[j] and text[j:i] in dictionary:
                dp[i] = True
                prev[i] = j

    result = []
    pos = length

    while pos > 0:
        start = prev[pos]
        word = text[start:pos]
        result.append(word)
        pos = start

    result.reverse()

    print(' '.join(result))


if __name__ == "__main__":
    main()