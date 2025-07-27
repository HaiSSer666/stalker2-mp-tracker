'use client';

interface ErrorMessageProps {
  message: string;
  onRetry?: () => void;
}

export default function ErrorMessage({ message, onRetry }: ErrorMessageProps) {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="bg-stalker-gray rounded-lg p-8 shadow-2xl border border-red-500/20 max-w-md mx-auto">
        <div className="text-center">
          <div className="text-6xl mb-4">⚠️</div>
          <h2 className="text-2xl font-bold text-red-400 mb-4 font-mono">Error</h2>
          <p className="text-gray-300 mb-6 font-mono">{message}</p>
          {onRetry && (
            <button
              onClick={onRetry}
              className="bg-stalker-orange hover:bg-stalker-orange/80 text-white px-6 py-3 rounded-lg font-mono transition-colors"
            >
              Try Again
            </button>
          )}
        </div>
      </div>
    </div>
  );
}