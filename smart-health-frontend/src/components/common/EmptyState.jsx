const EmptyState = ({ message = "No data available." }) => (
    <div className="text-center py-16 text-gray-500 text-lg">{message}</div>
);
export default EmptyState;