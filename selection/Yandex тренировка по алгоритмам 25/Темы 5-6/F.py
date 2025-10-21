import sys

sys.setrecursionlimit(200000)


def main():
    n = int(sys.stdin.readline())
    parents = list(map(int, sys.stdin.readline().split()))

    children = [[] for _ in range(n + 1)]
    root = None
    for i in range(1, n + 1):
        p = parents[i - 1]
        if p == 0:
            root = i
        else:
            children[p].append(i)

    timer = 0
    tin = [0] * (n + 1)
    tout = [0] * (n + 1)

    stack = [(root, 0)]
    while stack:
        u, st = stack.pop()
        if st == 0:
            timer += 1
            tin[u] = timer
            stack.append((u, 1))
            for v in reversed(children[u]):
                stack.append((v, 0))
        else:
            timer += 1
            tout[u] = timer

    m = int(sys.stdin.readline())
    results = []
    for _ in range(m):
        a, b = map(int, sys.stdin.readline().split())
        if tin[a] < tin[b] and tout[a] > tout[b]:
            results.append("1")
        else:
            results.append("0")

    sys.stdout.write("\n".join(results))


if __name__ == "__main__":
    main()