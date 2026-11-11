// Toast notifications
function showToast(message, type = 'success') {
    const container = document.querySelector('.toast-container') || createToastContainer();
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    const icon = type === 'success' ? '✓' : type === 'error' ? '✕' : 'ℹ';
    
    toast.innerHTML = `
        <span class="toast-icon">${icon}</span>
        <span class="toast-message">${message}</span>
    `;
    
    container.appendChild(toast);
    
    setTimeout(() => {
        toast.style.animation = 'slideIn 0.3s ease reverse forwards';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function createToastContainer() {
    const container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
    return container;
}

// Confirmation modal
function confirmModal(message, onConfirm, type = 'primary') {
    const overlay = document.querySelector('.modal-overlay') || createModal();
    const card = overlay.querySelector('.modal-card');
    
    card.innerHTML = `
        <h3 class="mb-1">Confirm Action</h3>
        <p class="mb-2">${message}</p>
        <div style="display: flex; gap: 1rem; justify-content: flex-end;">
            <button class="btn btn-outline" id="modalCancel">Cancel</button>
            <button class="btn btn-${type}" id="modalConfirm">Confirm</button>
        </div>
    `;
    
    overlay.style.display = 'flex';
    
    const cleanup = () => {
        overlay.style.display = 'none';
    };
    
    overlay.querySelector('#modalCancel').onclick = cleanup;
    overlay.querySelector('#modalConfirm').onclick = () => {
        onConfirm();
        cleanup();
    };
}

function createModal() {
    const overlay = document.createElement('div');
    overlay.className = 'modal-overlay';
    overlay.innerHTML = '<div class="modal-card"></div>';
    document.body.appendChild(overlay);
    return overlay;
}

// Loading spinner
function showLoader() {
    const loader = document.querySelector('.loader-overlay') || createLoader();
    loader.style.display = 'flex';
}

function hideLoader() {
    const loader = document.querySelector('.loader-overlay');
    if (loader) loader.style.display = 'none';
}

function createLoader() {
    const loader = document.createElement('div');
    loader.className = 'loader-overlay';
    loader.innerHTML = '<span class="loader"></span>';
    document.body.appendChild(loader);
    return loader;
}

// Sidebar active state
function setActiveSidebarItem(pageName) {
    document.querySelectorAll('.sidebar-item').forEach(item => {
        if (item.dataset.page === pageName) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });
}

// Format currency
function formatLKR(amount) {
    return new Intl.NumberFormat('en-LK', {
        style: 'currency',
        currency: 'LKR',
        minimumFractionDigits: 2
    }).format(amount);
}

// Format date
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
}

// Status badge HTML
function statusBadge(status) {
    const s = status ? status.toUpperCase() : 'PENDING';
    const className = `badge badge-${s.toLowerCase()}`;
    return `<span class="${className}">${s}</span>`;
}

// Star rating renderer
function starRating(value, editable = false, onRate = null) {
    let html = '<div class="star-rating">';
    for (let i = 1; i <= 5; i++) {
        const active = i <= value ? 'active' : '';
        const cursor = editable ? 'pointer' : 'default';
        html += `<span class="star ${active}" style="cursor: ${cursor}" data-value="${i}">★</span>`;
    }
    html += '</div>';
    
    // Note: If editable, event listeners would need to be added after rendering
    return html;
}
