import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../services/api';
import { toast } from 'react-toastify';

const RegisterPage = () => {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        username: '', email: '', password: '',
        role: 'PATIENT', fullName: '', phone: '',
        dateOfBirth: '', bloodGroup: '',
        specialization: '', licenseNumber: '', experienceYears: '',
        employeeId: ''
    });
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            await API.post('/auth/register', form);
            toast.success('Registration successful!');
            navigate('/login');
        } catch {
            toast.error('Registration failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100 py-8">
            <div className="bg-white p-8 rounded shadow-md w-full max-w-lg">
                <h2 className="text-2xl font-bold mb-6">Register</h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <input name="username" placeholder="Username" className="w-full p-2 border rounded" onChange={handleChange} required />
                    <input name="email" type="email" placeholder="Email" className="w-full p-2 border rounded" onChange={handleChange} required />
                    <input name="password" type="password" placeholder="Password" className="w-full p-2 border rounded" onChange={handleChange} required />
                    <input name="fullName" placeholder="Full Name" className="w-full p-2 border rounded" onChange={handleChange} />
                    <input name="phone" placeholder="Phone" className="w-full p-2 border rounded" onChange={handleChange} />
                    <select name="role" className="w-full p-2 border rounded" onChange={handleChange}>
                        <option value="PATIENT">Patient</option>
                        <option value="DOCTOR">Doctor</option>
                        <option value="RECEPTIONIST">Receptionist</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                    {form.role === 'PATIENT' && (
                        <>
                            <input name="dateOfBirth" placeholder="Date of Birth" className="w-full p-2 border rounded" onChange={handleChange} />
                            <input name="bloodGroup" placeholder="Blood Group" className="w-full p-2 border rounded" onChange={handleChange} />
                        </>
                    )}
                    {form.role === 'DOCTOR' && (
                        <>
                            <input name="specialization" placeholder="Specialization" className="w-full p-2 border rounded" onChange={handleChange} />
                            <input name="licenseNumber" placeholder="License Number" className="w-full p-2 border rounded" onChange={handleChange} />
                            <input name="experienceYears" placeholder="Experience Years" className="w-full p-2 border rounded" onChange={handleChange} />
                        </>
                    )}
                    {form.role === 'RECEPTIONIST' && (
                        <input name="employeeId" placeholder="Employee ID" className="w-full p-2 border rounded" onChange={handleChange} />
                    )}
                    <button disabled={loading} className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700">
                        {loading ? 'Registering...' : 'Register'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default RegisterPage;