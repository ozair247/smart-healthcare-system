import { useEffect, useState } from 'react';
import { useAuth } from '../hooks/useAuth';
import API from '../services/api';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, PieChart, Pie, Cell } from 'recharts';
import LoadingSpinner from '../components/common/LoadingSpinner';

const DashboardPage = () => {
    const { user } = useAuth();
    const [stats, setStats] = useState({
        patients: 0,
        doctors: 0,
        appointments: 0,
        appointmentsByStatus: {},
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const [patientsRes, doctorsRes, appointmentsRes] = await Promise.all([
                    API.get('/patients'),
                    API.get('/doctors'),
                    API.get('/appointments'),
                ]);
                setStats({
                    patients: patientsRes.data.length,
                    doctors: doctorsRes.data.length,
                    appointments: appointmentsRes.data.length,
                    appointmentsByStatus: appointmentsRes.data.reduce((acc, app) => {
                        acc[app.status] = (acc[app.status] || 0) + 1;
                        return acc;
                    }, {}),
                });
            } catch (error) {
                console.error('Failed to load stats', error);
                // keep default zeros – page still renders
            } finally {
                setLoading(false);
            }
        };
        fetchStats();
    }, []);

    if (loading) return <LoadingSpinner />;

    const appointmentData = stats.appointmentsByStatus
        ? Object.entries(stats.appointmentsByStatus).map(([name, value]) => ({ name, value }))
        : [];

    const COLORS = ['#0088FE', '#00C49F', '#FFBB28'];

    return (
        <div>
            <h2 className="text-2xl font-bold mb-6">Welcome, {user?.username}!</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
                <div className="bg-white p-6 rounded shadow">
                    <p className="text-gray-500">Total Patients</p>
                    <p className="text-3xl font-bold">{stats.patients}</p>
                </div>
                <div className="bg-white p-6 rounded shadow">
                    <p className="text-gray-500">Total Doctors</p>
                    <p className="text-3xl font-bold">{stats.doctors}</p>
                </div>
                <div className="bg-white p-6 rounded shadow">
                    <p className="text-gray-500">Total Appointments</p>
                    <p className="text-3xl font-bold">{stats.appointments}</p>
                </div>
            </div>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <div className="bg-white p-6 rounded shadow">
                    <h3 className="font-semibold mb-4">Appointment Status Distribution</h3>
                    {appointmentData.length > 0 ? (
                        <PieChart width={400} height={300}>
                            <Pie data={appointmentData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={100} fill="#8884d8" label>
                                {appointmentData.map((entry, index) => (
                                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                ))}
                            </Pie>
                            <Tooltip />
                            <Legend />
                        </PieChart>
                    ) : (
                        <p className="text-gray-400">No data yet</p>
                    )}
                </div>
                <div className="bg-white p-6 rounded shadow">
                    <h3 className="font-semibold mb-4">Monthly Overview (demo)</h3>
                    <BarChart width={400} height={300} data={[{ month: 'Jan', appointments: 10 }, { month: 'Feb', appointments: 15 }]}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="month" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="appointments" fill="#8884d8" />
                    </BarChart>
                </div>
            </div>
        </div>
    );
};

export default DashboardPage;