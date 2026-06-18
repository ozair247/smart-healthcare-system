import { useEffect, useState } from 'react';
import API from '../services/api';
import LoadingSpinner from '../components/common/LoadingSpinner';
import EmptyState from '../components/common/EmptyState';
import { toast } from 'react-toastify';
import { useAuth } from '../hooks/useAuth';

const BillingPage = () => {
    const { user } = useAuth();
    const [bills, setBills] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [form, setForm] = useState({ patientId: '', amount: '', description: '' });

    const fetchBills = async () => {
        try {
            const res = await API.get('/billing');
            setBills(res.data);
        } catch {
            toast.error('Failed to load bills');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchBills();
    }, []);

    const handleCreate = async (e) => {
        e.preventDefault();
        try {
            await API.post('/billing', { ...form, amount: parseFloat(form.amount) });
            toast.success('Bill created');
            setShowForm(false);
            fetchBills();
        } catch {
            toast.error('Creation failed');
        }
    };

    if (loading) return <LoadingSpinner />;
    if (bills.length === 0) return <EmptyState message="No bills found." />;

    return (
        <div>
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-bold">Billing</h2>
                {(user?.role === 'RECEPTIONIST' || user?.role === 'ADMIN') && (
                    <button onClick={() => setShowForm(true)} className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                        Create Bill
                    </button>
                )}
            </div>
            <div className="bg-white shadow overflow-hidden rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Patient</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Amount</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                    {bills.map(b => (
                        <tr key={b.id} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap">{b.patientName}</td>
                            <td className="px-6 py-4 whitespace-nowrap">${b.amount}</td>
                            <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                      b.paymentStatus === 'PAID' ? 'bg-green-100 text-green-800' :
                          b.paymentStatus === 'CANCELLED' ? 'bg-red-100 text-red-800' : 'bg-yellow-100 text-yellow-800'
                  }`}>
                    {b.paymentStatus}
                  </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">{b.billingDate}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {showForm && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h3 className="text-lg font-bold mb-4">Create Bill</h3>
                        <form onSubmit={handleCreate} className="space-y-4">
                            <input type="number" placeholder="Patient ID" className="w-full p-2 border rounded" onChange={e => setForm({...form, patientId: e.target.value})} required />
                            <input type="number" step="0.01" placeholder="Amount" className="w-full p-2 border rounded" onChange={e => setForm({...form, amount: e.target.value})} required />
                            <input placeholder="Description" className="w-full p-2 border rounded" onChange={e => setForm({...form, description: e.target.value})} />
                            <div className="flex justify-end space-x-2">
                                <button type="button" onClick={() => setShowForm(false)} className="px-4 py-2 border rounded">Cancel</button>
                                <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded">Create</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default BillingPage;