from collections import Counter
from math import comb

start = input()
n = len(start)
cnt = Counter(start)

# Все пары позиций
total_pairs = comb(n, 2)

# Пары с одинаковыми буквами
same_letter_pairs = 0
for freq in cnt.values():
    if freq >= 2:
        same_letter_pairs += comb(freq, 2)

# Пары с разными буквами
different_letter_pairs = total_pairs - same_letter_pairs

# Уникальных паролей = исходный + пары с разными буквами
result = 1 + different_letter_pairs
print(result)