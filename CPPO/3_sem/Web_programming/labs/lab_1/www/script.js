document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('dataForm');
    const tableBody = document.getElementById("resultsBody");

    // Загрузка истории при старте страницы
    loadHistoryFromLocalStorage();

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        if (validateForm()) {
            const formData = new FormData(form);
            const params = new URLSearchParams(formData);

            fetch("/calculate", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params.toString()
            })
                .then(response => response.json())
                .then(data => {
                    // Добавляем строку в таблицу
                    addRowToTable(data);

                    // Сохраняем в localStorage
                    saveToLocalStorage(data);
                })
                .catch(err => {
                    alert("Ошибка соединения с сервером: " + err);
                });
        }
    });

    // Функция добавления строки в таблицу
    function addRowToTable(data) {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${escapeHtml(data.x)}</td>
            <td>${escapeHtml(data.y)}</td>
            <td>${escapeHtml(data.r)}</td>
            <td>${escapeHtml(data.result)}</td>
            <td>${escapeHtml(data.time)}</td>
            <td>${data.execTimeMs}</td>
        `;
        tableBody.appendChild(row);
    }

    // Сохранение в localStorage
    function saveToLocalStorage(data) {
        try {
            // Получаем существующую историю
            let history = JSON.parse(localStorage.getItem('areaCheckHistory') || '[]');

            // Добавляем новую запись
            history.push({
                x: data.x,
                y: data.y,
                r: data.r,
                result: data.result,
                time: data.time,
                execTimeMs: data.execTimeMs
            });

            // Ограничиваем размер истории (последние 50 записей)
            if (history.length > 50) {
                history = history.slice(-50);
            }

            // Сохраняем обратно
            localStorage.setItem('areaCheckHistory', JSON.stringify(history));

            console.log('История сохранена в localStorage:', history.length, 'записей');
        } catch (e) {
            console.error('Ошибка сохранения в localStorage:', e);
        }
    }

    // Загрузка из localStorage
    function loadHistoryFromLocalStorage() {
        try {
            const history = JSON.parse(localStorage.getItem('areaCheckHistory') || '[]');

            console.log('Загружено из localStorage:', history.length, 'записей');

            // Добавляем каждую запись в таблицу
            history.forEach(data => {
                addRowToTable(data);
            });
        } catch (e) {
            console.error('Ошибка загрузки из localStorage:', e);
        }
    }

    // Защита от XSS
    function escapeHtml(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return String(text).replace(/[&<>"']/g, m => map[m]);
    }

    // Остальной код (validateForm, drawGraph и т.д.)
    // ...
});