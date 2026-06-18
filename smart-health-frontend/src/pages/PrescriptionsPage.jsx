import { useEffect, useState } from 'react';
import API from '../services/api';
import LoadingSpinner from '../components/common/LoadingSpinner';
import EmptyState from '../components/common/EmptyState';
import { toast } from 'react-toastify';
import { useAuth } from '../hooks/useAuth';

const PrescriptionsPage = () => {
    const { user } = useAuth();
    const [prescriptions, setPrescriptions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [form, setForm] = useState({ medicalRecordId: '', medicineName: '', dosage: '', duration: '' });

    const fetchPrescriptions = async () => {
        try {
            const res = await API.get('/prescriptions');
            setPrescriptions(res.data);
        } catch {
            toast.error('Failed to load prescriptions');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPrescriptions();
    }, []);

    const handleCreate = async (e) => {
        e.preventDefault();
        try {
            await API.post('/prescriptions', form);
            toast.success('Prescription added');
            setShowForm(false);
            fetchPrescriptions();
        } catch {
            toast.error('Creation failed');
        }
    };

    if (loading) return <LoadingSpinner />;
    if (prescriptions.length === 0) return <EmptyState message="No prescriptions found." />;

    return (
        <div>
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-bold">Prescriptions</h2>
                {user?.role === 'DOCTOR' && (
                    <button onClick={() => setShowForm(true)} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                        Add Prescription
                    </button>
                )}
            </div>
            <div className="bg-white shadow overflow-hidden rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Medicine</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Dosage</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Duration</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Record ID</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {prescriptions.map(p => (
                        <tr key={p.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap">{p.medicineName}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{p.dosage}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{p.duration}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{p.medicalRecordId || ''}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {showForm && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h3 className="text-lg font-bold mb-4">New Prescription</h3>
                        <form onSubmit={handleCreate} className="space-y-4">
                            <input type="number" placeholder="Medical Record ID" className="w-full p-2 border rounded" onChange={e => setForm({...form, medicalRecordId: e.target.value})} required />
                            <input placeholder="Medicine Name" className="w-full p-2 border rounded" onChange={e => setForm({...form, medicineName: e.target.value})} required />
                            <input placeholder="Dosage" className="w-full p-2 border rounded" onChange={e => setForm({...form, dosage: e.target.value})} />
                            <input placeholder="Duration" className="w-full p-2 border rounded" onChange={e => setForm({...form, duration: e.target.value})} />
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

export default PrescriptionsPage;