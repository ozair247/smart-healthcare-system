import { NavLink } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const Sidebar = () => {
    const { user, logout } = useAuth();

    const linkClass = ({ isActive }) =>
        `flex items-center p-2 rounded ${
            isActive ? 'bg-blue-700 text-white' : 'text-gray-300 hover:bg-blue-600 hover:text-white'
        }`;

    return (
        <aside className="w-64 bg-blue-800 min-h-screen p-4 text-white flex flex-col">
            <h2 className="text-2xl font-bold mb-8">SmartHealth</h2>
            <nav className="flex-1 space-y-1">
                <NavLink to="/dashboard" className={linkClass}>Dashboard</NavLink>

                {user?.role === 'ADMIN' && (
                    <>
                        <NavLink to="/doctors" className={linkClass}>Doctors</NavLink>
                        <NavLink to="/patients" className={linkClass}>Patients</NavLink>
                        <NavLink to="/appointments" className={linkClass}>Appointments</NavLink>
                        <NavLink to="/billing" className={linkClass}>Billing</NavLink>
                    </>
                )}

                {user?.role === 'RECEPTIONIST' && (
                    <>
                        <NavLink to="/patients" className={linkClass}>Patients</NavLink>
                        <NavLink to="/appointments" className={linkClass}>Appointments</NavLink>
                        <NavLink to="/billing" className={linkClass}>Billing</NavLink>
                    </>
                )}

                {user?.role === 'DOCTOR' && (
                    <>
                        <NavLink to="/appointments" className={linkClass}>Appointments</NavLink>
                        <NavLink to="/medical-records" className={linkClass}>Medical Records</NavLink>
                        <NavLink to="/prescriptions" className={linkClass}>Prescriptions</NavLink>
                    </>
                )}

                {user?.role === 'PATIENT' && (
                    <>
                        <NavLink to="/appointments" className={linkClass}>Appointments</NavLink>
                        <NavLink to="/medical-records" className={linkClass}>Medical Records</NavLink>
                        <NavLink to="/billing" className={linkClass}>Bills</NavLink>
                    </>
                )}
            </nav>
            <button onClick={logout} className="mt-auto p-2 bg-red-600 rounded hover:bg-red-700 text-sm">
                Logout
            </button>
        </aside>
    );
};

export default Sidebar;