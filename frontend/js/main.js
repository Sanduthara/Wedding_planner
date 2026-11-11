function getRedirectPrefix() {
    return window.location.pathname.includes('/customer/') ? '../' : '';
}

function requireAuth(allowedRoles) {
    const user = JSON.parse(localStorage.getItem('ethereal_user') || 'null');
    const prefix = getRedirectPrefix();

    if (!user || !allowedRoles.includes(user.role)) {
        window.location.href = prefix + 'login.html';
        return null;
    }

    return user;
}

function logout() {
    localStorage.removeItem('ethereal_user');
    window.location.href = getRedirectPrefix() + 'login.html';
}

document.addEventListener('DOMContentLoaded', () => {
    document.body.classList.add('fade-in');
});

const utils = {
    setLoading(button, isLoading, originalText = 'Submit') {
        if (isLoading) {
            button.innerHTML = '<span class="loader" style="width: 20px; height: 20px; border-width: 2px;"></span>';
            button.disabled = true;
        } else {
            button.innerText = originalText;
            button.disabled = false;
        }
    },

    formatCurrency(amount) {
        return new Number(amount || 0).toLocaleString('en-US', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    }
};
