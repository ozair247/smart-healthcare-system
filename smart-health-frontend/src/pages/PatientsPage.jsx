import { useEffect, useState } from 'react';
import API from '../services/api';
import LoadingSpinner from '../components/common/LoadingSpinner';
import EmptyState from '../components/common/EmptyState';
import { toast } from 'react-toastify';
import { useAuth } from '../hooks/useAuth';

const PatientsPage = () => {
    const { user } = useAuth();
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchPatients = () => {
        API.get('/patients')
            .then(res => setPatients(res.data))
            .catch(() => toast.error('Failed to load patients'))
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchPatients();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm('Delete this patient?')) {
            try {
                await API.delete(`/patients/${id}`);
                toast.success('Patient deleted');
                fetchPatients();
            } catch {
                toast.error('Delete failed');
            }
        }
    };

    const handleDownloadPdf = (patientId) => {
        const token = localStorage.getItem('token');
        fetch(`http://localhost:8080/api/patients/${patientId}/report`, {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(res => res.blob())
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = `patient-${patientId}-report.pdf`;
                a.click();
                window.URL.revokeObjectURL(url);
            })
            .catch(() => toast.error('Failed to download report'));
    };

    if (loading) return <LoadingSpinner />;
    if (patients.length === 0) return <EmptyState message="No patients registered." />;

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">Patients</h2>
            <div className="bg-white shadow overflow-hidden rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Email</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Blood Group</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Phone</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {patients.map(p => (
                        <tr key={p.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap">{p.fullName}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{p.email}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{p.bloodGroup}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{p.phone}</td>
                            <td className="px-6 py-4 whitespace-nowrap space-x-2">
                                <button
                                    onClick={() => handleDownloadPdf(p.id)}
                                    className="text-blue-600 hover:underline"
                                >
                                    PDF
                                </button>
                                {user?.role === 'ADMIN' && (
                                    <button
                                        onClick={() => handleDelete(p.id)}
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
        </div>
    );
};

export default PatientsPage;