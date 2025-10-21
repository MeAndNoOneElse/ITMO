import sys

sys.setrecursionlimit(200000)


def solve():
    n = int(sys.stdin.readline())
    parent = [0] * n
    children = [[] for _ in range(n)]
    for i in range(1, n):
        p = int(sys.stdin.readline())
        parent[i] = p
        children[p].append(i)

    a = list(map(int, sys.stdin.readline().split()))

    s = [-x for x in a]

    op = [0] * n

    stack = [(0, 0)]
    order = []
    while stack:
        u, st = stack.pop()
        if st == 0:
            stack.append((u, 1))
            for v in children[u]:
                stack.append((v, 0))
        else:
            order.append(u)

    for u in order:
        total_children_s = 0
        for v in children[u]:
            total_children_s += s[v]
        op[u] = s[u] - total_children_s

    total_ops = sum(abs(x) for x in op)
    print(total_ops)


if __name__ == "__main__":
    solve()