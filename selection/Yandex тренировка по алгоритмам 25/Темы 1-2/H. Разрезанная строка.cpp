#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>

using namespace std;

using ll = long long;
const ll BASE = 911382323;
const ll MOD = 1e9 + 7;

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m;
    cin >> n >> m;
    string s;
    cin >> s;

    int len_part = n / m;

    // Заранее посчитаем хэши для всех частей t_i
    unordered_map<ll, vector<int>> hash_to_indices;

    for (int idx = 1; idx <= m; idx++) {
        string t;
        cin >> t;
        ll hash_val = 0;
        for (char c : t) {
            hash_val = (hash_val * BASE + c) % MOD;
        }
        hash_to_indices[hash_val].push_back(idx);
    }

    // Посчитаем префикс-хэши для s
    vector<ll> pref_hash(n + 1, 0);
    vector<ll> base_pow(n + 1, 1);
    for (int i = 0; i < n; i++) {
        pref_hash[i + 1] = (pref_hash[i] * BASE + s[i]) % MOD;
        base_pow[i + 1] = (base_pow[i] * BASE) % MOD;
    }

    // Функция для получения хэша подстроки s[l:r)
    auto get_hash = [&](int l, int r) -> ll {
        ll res = (pref_hash[r] - pref_hash[l] * base_pow[r - l]) % MOD;
        if (res < 0) res += MOD;
        return res;
    };

    vector<int> result;

    for (int start = 0; start < n; start += len_part) {
        ll h = get_hash(start, start + len_part);
        // Находим индекс части с таким хэшем
        auto& indices = hash_to_indices[h];
        int id = indices.back();
        indices.pop_back();
        result.push_back(id);
    }

    for (int i = 0; i < m; i++) {
        cout << result[i] << (i == m - 1 ? "\n" : " ");
    }

    return 0;
}