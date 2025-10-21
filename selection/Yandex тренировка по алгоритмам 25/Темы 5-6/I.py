class Node:
    __slots__ = ('val', 'left', 'right', 'h', 'w', 'root_x', 'lines')

    def __init__(self, val, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
        self.h = 0
        self.w = 0
        self.root_x = 0
        self.lines = []


def parse_expression(s, idx=0):
    node, idx = parse_term(s, idx)
    while idx < len(s) and s[idx] in '+-':
        op = s[idx]
        idx += 1
        right, idx = parse_term(s, idx)
        node = Node(op, node, right)
    return node, idx


def parse_term(s, idx):
    node, idx = parse_factor(s, idx)
    while idx < len(s) and s[idx] in '*/':
        op = s[idx]
        idx += 1
        right, idx = parse_factor(s, idx)
        node = Node(op, node, right)
    return node, idx


def parse_factor(s, idx):
    node, idx = parse_element(s, idx)
    if idx < len(s) and s[idx] == '^':
        idx += 1
        right, idx = parse_factor(s, idx)
        node = Node('^', node, right)
    return node, idx


def parse_element(s, idx):
    if idx < len(s) and s[idx] == '(':
        idx += 1
        node, idx = parse_expression(s, idx)
        if idx < len(s) and s[idx] == ')':
            idx += 1
        return node, idx
    var = s[idx]
    idx += 1
    return Node(var), idx


def compute_tree(node):
    if node.left is None and node.right is None:
        node.h = 1
        node.w = 1
        node.root_x = 0
        node.lines = [node.val]
        return

    compute_tree(node.left)
    compute_tree(node.right)

    node.w = node.left.w + 5 + node.right.w
    node.root_x = node.left.w + 2
    node.h = max(node.left.h, node.right.h) + 2

    # Оптимизация: создаем только необходимые строки
    top_line = [' '] * node.w
    second_line = [' '] * node.w
    child_lines = []

    left_joint = node.left.root_x
    right_joint = node.left.w + 5 + node.right.root_x
    op = f"[{node.val}]"
    op_len = len(op)

    # Оптимизированное рисование линий
    left_end = node.root_x + op_len - 5
    right_start = node.root_x + op_len - 1

    # Рисуем только нужные символы
    for x in range(left_joint + 1, min(left_end + 1, node.w)):
        top_line[x] = '-'
    for x in range(max(right_start, 0), min(right_joint, node.w)):
        top_line[x] = '-'

    if left_joint < node.w:
        top_line[left_joint] = '.'
    if right_joint < node.w:
        top_line[right_joint] = '.'

    for i, ch in enumerate(op):
        pos = node.root_x + i - 1
        if 0 <= pos < node.w:
            top_line[pos] = ch

    if left_joint < node.w:
        second_line[left_joint] = '|'
    if right_joint < node.w:
        second_line[right_joint] = '|'

    # Оптимизация: объединяем линии детей без вложенных циклов
    max_child_height = max(node.left.h, node.right.h)
    for i in range(max_child_height):
        left_part = node.left.lines[i] if i < node.left.h else ' ' * node.left.w
        right_part = node.right.lines[i] if i < node.right.h else ' ' * node.right.w
        child_lines.append(left_part + ' ' * 5 + right_part)

    node.lines = [''.join(top_line), ''.join(second_line)] + child_lines


def main():
    s = input().strip()
    root, _ = parse_expression(s, 0)
    compute_tree(root)
    for line in root.lines:
        print(line)


if __name__ == "__main__":
    main()