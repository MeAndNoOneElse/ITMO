В папке CPPO предметы, которые раньше встретил на СППО (англ там)

В папке CYUR предметы, которые раньше встретил на СУиРе в 1 семе только 3

В папке selection лежат всякие разные отборы, которые я писал
# Как сделать ссылку на репозиторий?
- сначала добавляем папку и инициализируем её
  ```bash
  git init
  git remote add origin https://github.com/MeAndNoOneElse/lab_5.git
  git pull origin master
  ```
- затем из ITMO:
```bash
git submodule add "../lab_7" "CPPO\old\1-2 Programming\programms\lab_7"
```
  lab_7 это репозиторий, лежит рядом с ITMO (ну или где-то ещё, можно ссылку вставлять), а второй путь это то, где надо создать ссылку
- Удалить, если она уже есть:
 ```bash
 Remove-Item -Recurse -Force "CPPO\old\1-2 Programming\programms\lab_5"
 ```
- Ну и всё:
``` bash
git add .
git commit -m "feat: add Lab_5 as submodule"
git push
```
