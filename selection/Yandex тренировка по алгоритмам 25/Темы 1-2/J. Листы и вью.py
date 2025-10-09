import sys


def main():
    n = int(sys.stdin.readline().strip())

    lists = {}
    output_lines = []

    def extract_params(s):
        """Извлекает параметры из скобок"""
        start = s.find('(') + 1
        end = s.find(')')
        return s[start:end].split(',')

    for _ in range(n):
        line = sys.stdin.readline().strip()
        if not line:
            continue

        if line.startswith("List "):
            # Создание нового списка
            parts = line.split('=')
            name = parts[0].split()[1].strip()
            right_side = parts[1].strip()

            if right_side.startswith("new List"):
                # List a = new List(2,3,5)
                params = extract_params(right_side)
                numbers = list(map(int, params)) if params[0] else []
                lists[name] = {
                    'type': 'original',
                    'data': numbers,
                    'start': 0,
                    'end': len(numbers) - 1,
                    'parent': None
                }

            elif ".subList(" in right_side:
                # List b = a.subList(from,to)
                src_name = right_side.split('.')[0].strip()
                params = extract_params(right_side)
                from_idx = int(params[0]) - 1  # 1-based to 0-based
                to_idx = int(params[1]) - 1  # 1-based to 0-based

                src_list = lists[src_name]

                # Вычисляем абсолютные индексы в родительском списке
                if src_list['type'] == 'sublist':
                    abs_start = src_list['start'] + from_idx
                    abs_end = src_list['start'] + to_idx
                    parent = src_list['parent']
                else:
                    abs_start = from_idx
                    abs_end = to_idx
                    parent = src_list

                lists[name] = {
                    'type': 'sublist',
                    'parent': parent,
                    'start': abs_start,
                    'end': abs_end
                }

        elif ".set(" in line:
            # a.set(i,x)
            name_end = line.find('.')
            name = line[:name_end].strip()
            params = extract_params(line)
            idx = int(params[0]) - 1  # 1-based to 0-based
            value = int(params[1])

            lst = lists[name]
            if lst['type'] == 'sublist':
                actual_idx = lst['start'] + idx
                lst['parent']['data'][actual_idx] = value
            else:
                lst['data'][idx] = value

        elif ".add(" in line:
            # a.add(x)
            name_end = line.find('.')
            name = line[:name_end].strip()
            params = extract_params(line)
            value = int(params[0])

            lst = lists[name]
            if lst['type'] == 'original':
                lst['data'].append(value)
                lst['end'] = len(lst['data']) - 1

        elif ".get(" in line:
            # a.get(i)
            name_end = line.find('.')
            name = line[:name_end].strip()
            params = extract_params(line)
            idx = int(params[0]) - 1  # 1-based to 0-based

            lst = lists[name]
            if lst['type'] == 'sublist':
                actual_idx = lst['start'] + idx
                value = lst['parent']['data'][actual_idx]
            else:
                value = lst['data'][idx]

            output_lines.append(str(value))

    # Выводим все результаты операций get
    for result in output_lines:
        print(result)


if __name__ == "__main__":
    main()