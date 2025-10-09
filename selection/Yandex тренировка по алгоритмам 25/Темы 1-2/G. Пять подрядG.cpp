#include <iostream>
#include <vector>
#include <string>

using namespace std;

int main() {
    int n, m;
    cin >> n >> m;

    vector<string> field(n);
    for (int i = 0; i < n; i++) {
        cin >> field[i];
    }

    // Проверяем все возможные линии длины 5
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            // Горизонталь вправо
            if (j + 4 < m) {
                char c = field[i][j];
                if (c != '.' &&
                    field[i][j+1] == c &&
                    field[i][j+2] == c &&
                    field[i][j+3] == c &&
                    field[i][j+4] == c) {
                    cout << "Yes" << endl;
                    return 0;
                }
            }

            // Вертикаль вниз
            if (i + 4 < n) {
                char c = field[i][j];
                if (c != '.' &&
                    field[i+1][j] == c &&
                    field[i+2][j] == c &&
                    field[i+3][j] == c &&
                    field[i+4][j] == c) {
                    cout << "Yes" << endl;
                    return 0;
                }
            }

            // Диагональ вправо-вниз
            if (i + 4 < n && j + 4 < m) {
                char c = field[i][j];
                if (c != '.' &&
                    field[i+1][j+1] == c &&
                    field[i+2][j+2] == c &&
                    field[i+3][j+3] == c &&
                    field[i+4][j+4] == c) {
                    cout << "Yes" << endl;
                    return 0;
                }
            }

            // Диагональ влево-вниз
            if (i + 4 < n && j - 4 >= 0) {
                char c = field[i][j];
                if (c != '.' &&
                    field[i+1][j-1] == c &&
                    field[i+2][j-2] == c &&
                    field[i+3][j-3] == c &&
                    field[i+4][j-4] == c) {
                    cout << "Yes" << endl;
                    return 0;
                }
            }
        }
    }

    cout << "No" << endl;
    return 0;
}