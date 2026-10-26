const BASE_URL = 'http://localhost:8080/api';

const api = {
    async request(method, endpoint, body = null, auth = true) {
        const headers = { 'Content-Type': 'application/json' };
        
        if (auth) {
            const user = JSON.parse(localStorage.getItem('ethereal_user') || '{}');
            if (user.token) {
                // If the backend doesn't support Bearer tokens yet, this might need adjustment
                // But following the prompt's requirement:
                headers['Authorization'] = `Bearer ${user.token}`;
            }
        }

        const config = {
            method,
            headers,
            body: body ? JSON.stringify(body) : null
        };

        try {
            const res = await fetch(`${BASE_URL}${endpoint}`, config);
            
            if (!res.ok) {
                let errorData;
                const contentType = res.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    try {
                        errorData = await res.json();
                    } catch (e) {
                        errorData = { message: `Error ${res.status}` };
                    }
                } else {
                    errorData = { message: `Request failed with status ${res.status}` };
                }
                throw errorData;
            }

            const contentType = res.headers.get('content-type');
            const contentLength = res.headers.get('content-length');

            if (
                res.status === 204 || 
                contentLength === '0' ||
                !contentType || 
                !contentType.includes('application/json')
            ) {
                return null;
            }

            return await res.json();
        } catch (error) {
            console.error('API Request Error:', error);
            
            // Handle network errors
            if (error instanceof TypeError && error.message === 'Failed to fetch') {
                showToast('Connection failed. Is the server running?', 'error');
                throw { message: 'Connection failed. Is the server running?' };
            }
            
            throw error;
        }
    },

    get: (url, auth = true) => api.request('GET', url, null, auth),
    post: (url, body, auth = true) => api.request('POST', url, body, auth),
    put: (url, body, auth = true) => api.request('PUT', url, body, auth),
    delete: (url, auth = true) => api.request('DELETE', url, null, auth),

    // Convenience methods for auth/registration
    customers: {
        register: (data) => api.post('/customers', data, false),
    },
    vendors: {
        register: (data) => api.post('/vendors', data, false),
    }
};
