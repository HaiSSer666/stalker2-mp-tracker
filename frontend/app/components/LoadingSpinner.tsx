'use client';

export default function LoadingSpinner() {
  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-center">
        <div className="inline-block animate-spin rounded-full h-16 w-16 border-b-2 border-stalker-orange mb-4"></div>
        <p className="text-stalker-orange font-mono text-lg">Loading STALKER 2 MP status...</p>
      </div>
    </div>
  );
}