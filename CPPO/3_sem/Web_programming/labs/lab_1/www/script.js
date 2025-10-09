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

            fetch("/cgi-bin/area.fcgi", {
                method: "POST",
                body: params
            })
                .then(response => response.json())
                .then(data => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                    <td>${data.x}</td>
                    <td>${data.y}</td>
                    <td>${data.r}</td>
                    <td>${data.result}</td>
                    <td>${data.time}</td>
                    <td>${data.execTimeMs}</td>
                `;
                    tableBody.appendChild(row);
                })
                .catch(err => {
                    alert("Ошибка соединения с сервером: " + err);
                });
        }
    });

    function validateForm() {
        const xSelected = document.querySelector('input[name="x"]:checked');
        const rSelected = document.querySelector('input[name="r"]:checked');
        let isValid = true;

        if (!xSelected) {
            isValid = false;
            alert('Выберите X');
        }
        if (!validateY()) {
            isValid = false;
        }
        if (!rSelected) {
            isValid = false;
            alert('Выберите R');
        }
        return isValid;
    }

    function validateY() {
        const value = yInput.value.trim();
        yError.style.display = 'none';

        if (value === '') {
            yError.textContent = 'Поле Y не может быть пустым';
            yError.style.display = 'block';
            return false;
        }
        if (isNaN(value)) {
            yError.textContent = 'Y должно быть числом';
            yError.style.display = 'block';
            return false;
        }
        const numValue = parseFloat(value);
        if (numValue <= -3 || numValue >= 5) {
            yError.textContent = 'Y должно быть от -3 до 5';
            yError.style.display = 'block';
            return false;
        }
        return true;
    }

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
