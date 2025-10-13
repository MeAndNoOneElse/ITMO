# Морской бой 
- Попытка написать морской бой в консоли на C.
- Вдвоём через гит.

# Как комитить?
1. Перейдите в папку submodule
```bash
cd CPPO/3_sem/Programming_languages/practics/sea_battle
```
2. Зафиксируйте в репозитории sea_battle
```bash
git add .
git commit -m "Ваши изменения"
git push
```
3. Обновите ссылку в основном репозитории
```bash
cd ../../../../../  # вернуться в ITMO
git add CPPO/3_sem/Programming_languages/practics/sea_battle
git commit -m "Подгузил изменения в основную ветку"
git push
```
4. Перед тем как работать, нужно сделать ```pull``` из папки sea_battle
```bash
cd CPPO/3_sem/Programming_languages/practics/sea_battle
git pull
```
