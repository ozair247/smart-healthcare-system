const ErrorMessage = ({ message, onRetry }) => (
    <div className="text-center py-16 text-red-500">
        <p>{message}</p>
        {onRetry && (
            <button onClick={onRetry} className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
                Retry
            </button>
        )}
    </div>
);
export default ErrorMessage;