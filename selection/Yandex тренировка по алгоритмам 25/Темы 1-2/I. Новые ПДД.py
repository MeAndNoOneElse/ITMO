x, y = map(int, input().split())
f, g = map(int, input().split())

dx = abs(f - x)
dy = abs(g - y)

if (dx + dy == 0) or (dx + dy == 1):
    print(0)
elif (dx == 0 and dy != 0) or (dy == 0 and dx != 0):
    print(3 * (dx + dy - 1))
else:
    print(3 * (dx + dy) - 5)