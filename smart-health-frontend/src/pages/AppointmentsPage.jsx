import { useEffect, useState } from 'react';
import API from '../services/api';
import LoadingSpinner from '../components/common/LoadingSpinner';
import EmptyState from '../components/common/EmptyState';
import { toast } from 'react-toastify';
import { useAuth } from '../hooks/useAuth';

const AppointmentsPage = () => {
    const { user } = useAuth();
    const [appointments, setAppointments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [patients, setPatients] = useState([]);
    const [doctors, setDoctors] = useState([]);
    const [form, setForm] = useState({
        patientId: '', doctorId: '', appointmentDateTime: '', reason: ''
    });

    const fetchAppointments = async () => {
        try {
            const res = await API.get('/appointments');
            setAppointments(res.data);
        } catch {
            toast.error('Failed to load appointments');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAppointments();
    }, []);

    const openModal = async () => {
        try {
            if (user?.role === 'PATIENT') {
                const [myPatientRes, docRes] = await Promise.all([
                    API.get('/patients/me'),
                    API.get('/doctors'),
                ]);
                setDoctors(docRes.data);
                setForm(prev => ({ ...prev, patientId: myPatientRes.data.id }));
                setShowModal(true);
            } else {
                const [patRes, docRes] = await Promise.all([
                    API.get('/patients'),
                    API.get('/doctors'),
                ]);
                setPatients(patRes.data);
                setDoctors(docRes.data);
                setShowModal(true);
            }
        } catch {
            toast.error('Failed to load data');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await API.post('/appointments', form);
            toast.success('Appointment scheduled!');
            setShowModal(false);
            setForm({ patientId: '', doctorId: '', appointmentDateTime: '', reason: '' });
            fetchAppointments();
        } catch {
            toast.error('Scheduling failed');
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Delete this appointment?')) {
            try {
                await API.delete(`/appointments/${id}`);
                toast.success('Appointment deleted');
                fetchAppointments();
            } catch {
                toast.error('Delete failed');
            }
        }
    };

    const canBook = user?.role === 'RECEPTIONIST' || user?.role === 'PATIENT';

    if (loading) return <LoadingSpinner />;

    return (
        <div>
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-bold">Appointments</h2>
                {canBook && (
                    <button onClick={openModal} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                        New Appointment
                    </button>
                )}
            </div>
            {appointments.length === 0 ? (
                <EmptyState message="No appointments found." />
            ) : (
                <div className="bg-white shadow overflow-hidden rounded-lg">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Patient</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Doctor</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date & Time</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                        </tr>
                        </thead>
                        <tbody className="divide-y divide-gray-200">
                        {appointments.map(app => (
                            <tr key={app.id} className="hover:bg-gray-50">
                                <td className="px-6 py-4 whitespace-nowrap">{app.patientName}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{app.doctorName}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{new Date(app.appointmentDateTime).toLocaleString()}</td>
                                <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                        app.status === 'SCHEDULED' ? 'bg-green-100 text-green-800' :
                            app.status === 'CANCELLED' ? 'bg-red-100 text-red-800' : 'bg-gray-100 text-gray-800'
                    }`}>
                      {app.status}
                    </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {user?.role === 'ADMIN' && (
                                        <button
                                            onClick={() => handleDelete(app.id)}
                                            className="text-red-600 hover:underline"
                                        >
                                            Delete
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}

            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h3 className="text-lg font-bold mb-4">New Appointment</h3>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            {user?.role !== 'PATIENT' && (
                                <select className="w-full p-2 border rounded" onChange={e => setForm({...form, patientId: e.target.value})} required>
                                    <option value="">Select Patient</option>
                                    {patients.map(p => <option key={p.id} value={p.id}>{p.fullName}</option>)}
                                </select>
                            )}
                            <select className="w-full p-2 border rounded" onChange={e => setForm({...form, doctorId: e.target.value})} required>
                                <option value="">Select Doctor</option>
                                {doctors.map(d => <option key={d.id} value={d.id}>{d.fullName}</option>)}
                            </select>
                            <input type="datetime-local" className="w-full p-2 border rounded" onChange={e => setForm({...form, appointmentDateTime: e.target.value})} required />
                            <input placeholder="Reason" className="w-full p-2 border rounded" onChange={e => setForm({...form, reason: e.target.value})} />
                            <div className="flex justify-end space-x-2">
                                <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 border rounded">Cancel</button>
                                <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded">Schedule</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AppointmentsPage;