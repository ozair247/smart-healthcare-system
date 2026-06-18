import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import API from '../services/api';
import { AuthContext } from './AuthContext';

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    const initializeAuth = useCallback(() => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);
                const role = decoded.role || decoded.authorities?.[0]?.replace('ROLE_', '') || '';
                console.log('Decoded JWT:', decoded);
                console.log('User role:', role);
                setUser({
                    id: decoded.id,
                    username: decoded.sub,
                    email: decoded.email,
                    role,
                });
                API.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            } catch (e) {
                console.error('JWT decode error:', e);
                localStorage.removeItem('token');
            }
        }
        setLoading(false);
    }, []);

    useEffect(() => {
        initializeAuth();
    }, [initializeAuth]);

    const login = async (username, password) => {
        const res = await API.post('/auth/login', { username, password });
        const { token } = res.data;
        localStorage.setItem('token', token);
        API.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        const decoded = jwtDecode(token);
        const role = decoded.role || decoded.authorities?.[0]?.replace('ROLE_', '') || '';
        console.log('Login - Decoded JWT:', decoded);
        console.log('Login - User role:', role);
        setUser({
            id: decoded.id,
            username: decoded.sub,
            email: decoded.email,
            role,
        });
        navigate('/dashboard');
    };

    const logout = () => {
        localStorage.removeItem('token');
        API.defaults.headers.common['Authorization'] = '';
        setUser(null);
        navigate('/login');
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};