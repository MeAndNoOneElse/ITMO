// Form validation and submission
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('pointForm');
    const resultsTable = document.getElementById('resultsTable').getElementsByTagName('tbody')[0];
    
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Get form values
        const x = parseFloat(document.querySelector('input[name="x"]:checked')?.value);
        const y = parseFloat(document.getElementById('y').value);
        const r = parseFloat(document.querySelector('input[name="r"]:checked')?.value);
        
        // Validate inputs
        if (isNaN(x)) {
            alert('Please select X value');
            return;
        }
        
        if (isNaN(r)) {
            alert('Please select R value');
            return;
        }
        
        if (!validateY(y)) {
            return;
        }
        
        // Send request to server
        sendRequest(x, y, r);
    });
    
    // Y input validation
    const yInput = document.getElementById('y');
    yInput.addEventListener('input', function() {
        validateY(parseFloat(this.value));
    });
});

function validateY(numValue) {
    const yInput = document.getElementById('y');
    const errorMsg = document.getElementById('yError');
    
    if (isNaN(numValue)) {
        errorMsg.textContent = 'Please enter a valid number';
        yInput.classList.add('error');
        return false;
    }
    
    // Y must be in range [-3, 5] - inclusive boundaries
    if (numValue < -3 || numValue > 5) {
        errorMsg.textContent = 'Y must be in range [-3, 5]';
        yInput.classList.add('error');
        return false;
    }
    
    errorMsg.textContent = '';
    yInput.classList.remove('error');
    return true;
}

function sendRequest(x, y, r) {
    const startTime = new Date().getTime();
    
    fetch('/cgi-bin/area.fcgi', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `x=${x}&y=${y}&r=${r}`
    })
    .then(response => response.json())
    .then(data => {
        const endTime = new Date().getTime();
        const executionTime = (endTime - startTime) / 1000;
        
        if (data.success) {
            addResultToTable(data.x, data.y, data.r, data.result, executionTime);
            drawPoint(data.x, data.y, data.r, data.result);
        } else {
            alert('Error: ' + data.error);
        }
    })
    .catch(error => {
        alert('Request failed: ' + error);
    });
}

function addResultToTable(x, y, r, result, executionTime) {
    const resultsTable = document.getElementById('resultsTable').getElementsByTagName('tbody')[0];
    const row = resultsTable.insertRow(0);
    
    row.insertCell(0).textContent = x;
    row.insertCell(1).textContent = y;
    row.insertCell(2).textContent = r;
    row.insertCell(3).textContent = result ? 'Hit' : 'Miss';
    row.insertCell(4).textContent = new Date().toLocaleTimeString();
    row.insertCell(5).textContent = executionTime.toFixed(3) + 's';
    
    row.className = result ? 'hit' : 'miss';
}

function drawPoint(x, y, r, result) {
    const canvas = document.getElementById('canvas');
    const ctx = canvas.getContext('2d');
    
    // Calculate point position on canvas
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 40; // pixels per unit
    
    const pointX = centerX + x * scale;
    const pointY = centerY - y * scale;
    
    // Draw point
    ctx.fillStyle = result ? 'green' : 'red';
    ctx.beginPath();
    ctx.arc(pointX, pointY, 3, 0, 2 * Math.PI);
    ctx.fill();
}

// Draw the coordinate system and area
function drawArea() {
    const canvas = document.getElementById('canvas');
    const ctx = canvas.getContext('2d');
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const scale = 40;
    const r = 5; // Default R for visualization
    
    // Clear canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // Draw area
    ctx.fillStyle = 'rgba(0, 100, 255, 0.3)';
    
    // Rectangle in second quadrant
    ctx.fillRect(centerX - r * scale, centerY - r * scale / 2, r * scale, r * scale / 2);
    
    // Triangle in fourth quadrant
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.lineTo(centerX + r * scale, centerY);
    ctx.lineTo(centerX, centerY + r * scale);
    ctx.closePath();
    ctx.fill();
    
    // Quarter circle in third quadrant
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, r * scale, Math.PI / 2, Math.PI);
    ctx.closePath();
    ctx.fill();
    
    // Draw axes
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 2;
    
    // X axis
    ctx.beginPath();
    ctx.moveTo(0, centerY);
    ctx.lineTo(canvas.width, centerY);
    ctx.stroke();
    
    // Y axis
    ctx.beginPath();
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX, canvas.height);
    ctx.stroke();
    
    // Draw axis labels
    ctx.fillStyle = 'black';
    ctx.font = '12px Arial';
    
    for (let i = -5; i <= 5; i++) {
        if (i !== 0) {
            // X axis labels
            ctx.fillText(i.toString(), centerX + i * scale - 5, centerY + 15);
            // Y axis labels
            ctx.fillText(i.toString(), centerX + 5, centerY - i * scale + 5);
        }
    }
}

// Initialize canvas on page load
window.addEventListener('load', drawArea);
