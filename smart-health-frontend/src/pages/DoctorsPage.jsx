import { useEffect, useState } from 'react';
import API from '../services/api';
import LoadingSpinner from '../components/common/LoadingSpinner';
import EmptyState from '../components/common/EmptyState';
import { toast } from 'react-toastify';
import { useAuth } from '../hooks/useAuth';

const DoctorsPage = () => {
    const { user } = useAuth();
    const [doctors, setDoctors] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchDoctors = () => {
        API.get('/doctors')
            .then(res => setDoctors(res.data))
            .catch(() => toast.error('Failed to load doctors'))
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchDoctors();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm('Delete this doctor?')) {
            try {
                await API.delete(`/doctors/${id}`);
                toast.success('Doctor deleted');
                fetchDoctors();
            } catch {
                toast.error('Delete failed');
            }
        }
    };

    if (loading) return <LoadingSpinner />;
    if (doctors.length === 0) return <EmptyState message="No doctors available." />;

    return (
        <div>
            <h2 className="text-2xl font-bold mb-4">Doctors</h2>
            <div className="bg-white shadow overflow-hidden rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Specialization</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Available</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {doctors.map(d => (
                        <tr key={d.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap">{d.fullName}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{d.specialization}</td>
                            <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${d.available ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                    {d.available ? 'Yes' : 'No'}
                  </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                                {user?.role === 'ADMIN' && (
                                    <button
                                        onClick={() => handleDelete(d.id)}
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

export default DoctorsPage;