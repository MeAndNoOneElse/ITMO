#include <iostream>
#include <vector>
#include <string>
#include <climits>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;

    vector<string> grid(n);
    for (int i = 0; i < n; i++) {
        cin >> grid[i];
    }

    // Предподсчёт: max_S[r][c] = макс S_r если исключить столбец c
    vector<vector<int>> max_S(n, vector<int>(m, 0));
    vector<vector<int>> min_C(n, vector<int>(m, 0));

    for (int r = 0; r < n; r++) {
        // Сначала считаем сумму строки r если все ? -> +1
        int base_row = 0;
        for (int j = 0; j < m; j++) {
            if (grid[r][j] == '?') base_row += 1;
            else if (grid[r][j] == '+') base_row += 1;
            else base_row += -1;
        }

        // Для каждого исключённого столбца c
        for (int c = 0; c < m; c++) {
            // Вычитаем вклад столбца c и добавляем фиксированный (произвольный)
            int contrib_c;
            if (grid[r][c] == '?') contrib_c = 1;  // произвольный выбор
            else if (grid[r][c] == '+') contrib_c = 1;
            else contrib_c = -1;

            int without_c;
            if (grid[r][c] == '?') without_c = 1;  // было +1 при base_row
            else if (grid[r][c] == '+') without_c = 1;
            else without_c = -1;

            max_S[r][c] = base_row - without_c + contrib_c;
        }
    }

    for (int c = 0; c < m; c++) {
        // Считаем сумму столбца c если все ? -> -1
        int base_col = 0;
        for (int i = 0; i < n; i++) {
            if (grid[i][c] == '?') base_col += -1;
            else if (grid[i][c] == '+') base_col += 1;
            else base_col += -1;
        }

        // Для каждого исключённой строки r
        for (int r = 0; r < n; r++) {
            int contrib_r;
            if (grid[r][c] == '?') contrib_r = 1;  // произвольный выбор
            else if (grid[r][c] == '+') contrib_r = 1;
            else contrib_r = -1;

            int without_r;
            if (grid[r][c] == '?') without_r = -1;  // было -1 при base_col
            else if (grid[r][c] == '+') without_r = 1;
            else without_r = -1;

            min_C[r][c] = base_col - without_r + contrib_r;
        }
    }

    int best = INT_MIN;
    for (int r = 0; r < n; r++) {
        for (int c = 0; c < m; c++) {
            best = max(best, max_S[r][c] - min_C[r][c]);
        }
    }

    cout << best << endl;

    return 0;
}