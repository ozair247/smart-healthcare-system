import { useAuth } from '../../hooks/useAuth';

const Header = () => {
    const { user } = useAuth();
    return (
        <header className="bg-white shadow px-6 py-4 flex justify-between items-center">
            <h1 className="text-xl font-semibold text-gray-700">Dashboard</h1>
            <div className="flex items-center gap-4">
                <span className="text-gray-600">{user?.username}</span>
                <div className="w-8 h-8 bg-blue-500 text-white rounded-full flex items-center justify-center">
                    {user?.username?.charAt(0).toUpperCase()}
                </div>
            </div>
        </header>
    );
};

export default Header;