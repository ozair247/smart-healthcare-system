import { useEffect, useState } from 'react';
import API from '../services/api';
import LoadingSpinner from '../components/common/LoadingSpinner';
import EmptyState from '../components/common/EmptyState';
import { toast } from 'react-toastify';
import { useAuth } from '../hooks/useAuth';

const MedicalRecordsPage = () => {
    const { user } = useAuth();
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [form, setForm] = useState({ patientId: '', doctorId: '', visitDate: '', diagnosis: '', treatment: '' });

    const fetchRecords = async () => {
        try {
            const res = await API.get('/medical-records');
            setRecords(res.data);
        } catch {
            toast.error('Failed to load records');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchRecords();
    }, []);

    const handleCreate = async (e) => {
        e.preventDefault();
        try {
            await API.post('/medical-records', form);
            toast.success('Record created');
            setShowForm(false);
            fetchRecords();
        } catch {
            toast.error('Creation failed');
        }
    };

    if (loading) return <LoadingSpinner />;
    if (records.length === 0) return <EmptyState message="No medical records found." />;

    return (
        <div>
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-bold">Medical Records</h2>
                {user?.role === 'DOCTOR' && (
                    <button onClick={() => setShowForm(true)} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                        Add Record
                    </button>
                )}
            </div>
            <div className="bg-white shadow overflow-hidden rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Patient</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Doctor</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Diagnosis</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {records.map(r => (
                        <tr key={r.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap">{r.patientName}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{r.doctorName}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{r.visitDate}</td>
                            <td className="px-6 py-4">{r.diagnosis}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {showForm && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h3 className="text-lg font-bold mb-4">New Medical Record</h3>
                        <form onSubmit={handleCreate} className="space-y-4">
                            <input type="number" placeholder="Patient ID" className="w-full p-2 border rounded" onChange={e => setForm({...form, patientId: e.target.value})} required />
                            <input type="number" placeholder="Doctor ID" className="w-full p-2 border rounded" onChange={e => setForm({...form, doctorId: e.target.value})} required />
                            <input type="date" className="w-full p-2 border rounded" onChange={e => setForm({...form, visitDate: e.target.value})} required />
                            <input placeholder="Diagnosis" className="w-full p-2 border rounded" onChange={e => setForm({...form, diagnosis: e.target.value})} />
                            <input placeholder="Treatment" className="w-full p-2 border rounded" onChange={e => setForm({...form, treatment: e.target.value})} />
                            <div className="flex justify-end space-x-2">
                                <button type="button" onClick={() => setShowForm(false)} className="px-4 py-2 border rounded">Cancel</button>
                                <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded">Save</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default MedicalRecordsPage;