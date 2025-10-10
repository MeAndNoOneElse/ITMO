document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('dataForm');
    const yInput = document.getElementById('y');
    const yError = document.getElementById('y-error');
    const canvas = document.getElementById('graphCanvas');
    const ctx = canvas.getContext('2d');
    const tableBody = document.getElementById("resultsBody");

    let currentR = 2;

    drawGraph(currentR);

    document.querySelectorAll('input[name="r"]').forEach(radio => {
        radio.addEventListener('change', function() {
            currentR = parseFloat(this.value);
            drawGraph(currentR);
        });
    });

    yInput.addEventListener('input', validateY);

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        if (validateForm()) {
            const formData = new FormData(form);
            const params = new URLSearchParams(formData);

            // Показываем индикатор загрузки
            const submitBtn = document.querySelector('.submit-btn');
            const originalText = submitBtn.textContent;
            submitBtn.textContent = 'Отправка...';
            submitBtn.disabled = true;

            fetch("/calculate", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params.toString()
            })

                .then(response => {
                    console.log('Статус ответа:', response.status);

                    // Проверяем статус ответа
                    if (!response.ok) {
                        throw new Error(`HTTP ошибка! Статус: ${response.status}`);
                    }

                    return response.json();
                })
                .then(data => {
                    console.log('Получены данные:', data);

                    // Проверяем успешность ответа
                    if (data.success === false) {
                        alert("Ошибка сервера: " + (data.error || "Неизвестная ошибка"));
                        return;
                    }

                    // Добавляем строку в таблицу
                    const row = document.createElement("tr");

                    // Подсветка результата
                    if (data.result === "Попадание") {
                        row.style.backgroundColor = "rgba(76, 175, 80, 0.1)"; // Зеленый
                    } else {
                        row.style.backgroundColor = "rgba(244, 67, 54, 0.1)"; // Красный
                    }

                    row.innerHTML = `
                        <td>${escapeHtml(data.x)}</td>
                        <td>${escapeHtml(data.y)}</td>
                        <td>${escapeHtml(data.r)}</td>
                        <td><strong>${escapeHtml(data.result)}</strong></td>
                        <td>${escapeHtml(data.time)}</td>
                        <td>${data.execTimeMs}</td>
                    `;
                    tableBody.appendChild(row);

                    // Сохраняем в localStorage для сохранения истории
                    saveToHistory(data);
                })
                .catch(err => {
                    console.error('Ошибка запроса:', err);
                    alert("Ошибка соединения с сервером:\n" + err.message +
                        "\n\nПроверьте:\n1. Запущен ли FastCGI сервер\n2. Правильно ли настроен Apache\n3. Консоль браузера (F12) для деталей");
                })
                .finally(() => {
                    // Возвращаем кнопку в исходное состояние
                    submitBtn.textContent = originalText;
                    submitBtn.disabled = false;
                });
        }
    });

    function validateForm() {
        const xSelected = document.querySelector('input[name="x"]:checked');
        const rSelected = document.querySelector('input[name="r"]:checked');
        let isValid = true;

        if (!xSelected) {
            isValid = false;
            alert('⚠️ Выберите значение X');
        }
        if (!validateY()) {
            isValid = false;
        }
        if (!rSelected) {
            isValid = false;
            alert('⚠️ Выберите значение R');
        }
        return isValid;
    }

    function validateY() {
        const value = yInput.value.trim();
        yError.style.display = 'none';

        if (value === '') {
            yError.textContent = '⚠️ Поле Y не может быть пустым';
            yError.style.display = 'block';
            return false;
        }
        if (isNaN(value)) {
            yError.textContent = '⚠️ Y должно быть числом';
            yError.style.display = 'block';
            return false;
        }
        const numValue = parseFloat(value);
        if (numValue <= -3 || numValue >= 5) {
            yError.textContent = '⚠️ Y должно быть в диапазоне (-3, 5)';
            yError.style.display = 'block';
            return false;
        }
        return true;
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

    // Сохранение истории в localStorage
    function saveToHistory(data) {
        try {
            let history = JSON.parse(localStorage.getItem('areaCheckHistory') || '[]');
            history.push(data);
            // Храним последние 50 записей
            if (history.length > 50) {
                history = history.slice(-50);
            }
            localStorage.setItem('areaCheckHistory', JSON.stringify(history));
        } catch (e) {
            console.warn('Не удалось сохранить в localStorage:', e);
        }
    }

    // Загрузка истории при старте
    function loadHistory() {
        try {
            const history = JSON.parse(localStorage.getItem('areaCheckHistory') || '[]');
            history.forEach(data => {
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
            });
        } catch (e) {
            console.warn('Не удалось загрузить историю:', e);
        }
    }

    // Загружаем историю при старте
    loadHistory();

    function drawGraph(r) {
        const width = canvas.width;
        const height = canvas.height;
        const centerX = width / 2;
        const centerY = height / 2;
        const scale = 30;

        ctx.clearRect(0, 0, width, height);
        ctx.fillStyle = 'rgba(100, 200, 255, 0.3)';

        // Прямоугольник
        ctx.beginPath();
        ctx.rect(centerX - r * scale, centerY, r * scale, r * scale);
        ctx.fill();

        // Треугольник
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX + r * scale, centerY);
        ctx.lineTo(centerX, centerY + r * scale);
        ctx.closePath();
        ctx.fill();

        // Четверть круга
        ctx.beginPath();
        ctx.arc(centerX, centerY, r * scale, Math.PI, 3 * Math.PI / 2, false);
        ctx.lineTo(centerX, centerY);
        ctx.closePath();
        ctx.fill();

        // Оси и сетка
        ctx.strokeStyle = '#ccc';
        for (let x = centerX % scale; x < width; x += scale) {
            ctx.beginPath(); ctx.moveTo(x, 0); ctx.lineTo(x, height); ctx.stroke();
        }
        for (let y = centerY % scale; y < height; y += scale) {
            ctx.beginPath(); ctx.moveTo(0, y); ctx.lineTo(width, y); ctx.stroke();
        }

        ctx.strokeStyle = '#000';
        ctx.beginPath(); ctx.moveTo(0, centerY); ctx.lineTo(width, centerY); ctx.stroke();
        ctx.beginPath(); ctx.moveTo(centerX, 0); ctx.lineTo(centerX, height); ctx.stroke();

        ctx.fillStyle = '#000';
        ctx.beginPath(); ctx.moveTo(width-10, centerY-5); ctx.lineTo(width, centerY); ctx.lineTo(width-10, centerY+5); ctx.fill();
        ctx.beginPath(); ctx.moveTo(centerX-5, 10); ctx.lineTo(centerX, 0); ctx.lineTo(centerX+5, 10); ctx.fill();

        ctx.font = '14px Arial';
        ctx.fillText('X', width - 15, centerY - 10);
        ctx.fillText('Y', centerX + 10, 15);
        ctx.fillText(`R = ${r}`, centerX + 10, centerY - 10);
    }
});