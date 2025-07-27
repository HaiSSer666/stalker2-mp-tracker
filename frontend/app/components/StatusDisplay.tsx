'use client';

import { StatusResponse } from '@/app/lib/api';

interface StatusDisplayProps {
  status: StatusResponse;
}

export default function StatusDisplay({ status }: StatusDisplayProps) {
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const getStatusMessage = () => {
    if (status.mpReleased) {
      return {
        icon: '🟢',
        message: `Congratulations! MP is finally available ${status.daysSinceStalkerRelease} days after the STALKER 2 release.`,
        className: 'text-green-400'
      };
    } else {
      return {
        icon: '🔴',
        message: `${status.daysSinceStalkerRelease} days have passed since the STALKER 2 release. MP is still not available.`,
        className: 'text-red-400'
      };
    }
  };

  const statusInfo = getStatusMessage();

  return (
    <div className="bg-stalker-gray rounded-lg p-8 shadow-2xl border border-stalker-orange/20">
      <div className="text-center mb-8">
        <h1 className="text-4xl font-bold text-stalker-orange mb-4 font-mono">
          STALKER 2: Heart of Chornobyl
        </h1>
        <h2 className="text-2xl text-gray-300 font-mono">Multiplayer Tracker</h2>
      </div>

      <div className={`text-center mb-8 p-6 rounded-lg bg-black/30 ${statusInfo.className}`}>
        <div className="text-6xl mb-4">{statusInfo.icon}</div>
        <p className="text-xl font-mono leading-relaxed">{statusInfo.message}</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 text-sm font-mono">
        <div className="bg-black/20 p-4 rounded-lg">
          <h3 className="text-stalker-orange font-bold mb-2">📅 STALKER 2 Release Date</h3>
          <p className="text-gray-300">{formatDate(status.stalker2ReleaseDate)}</p>
        </div>
        
        {status.mpReleased && status.mpReleaseDate && (
          <div className="bg-black/20 p-4 rounded-lg">
            <h3 className="text-green-400 font-bold mb-2">🎮 MP Release Date</h3>
            <p className="text-gray-300">{formatDate(status.mpReleaseDate)}</p>
          </div>
        )}
        
        <div className="bg-black/20 p-4 rounded-lg">
          <h3 className="text-stalker-orange font-bold mb-2">⏰ Last Checked</h3>
          <p className="text-gray-300">
            {new Date(status.lastChecked).toLocaleString()}
          </p>
        </div>
        
        <div className="bg-black/20 p-4 rounded-lg">
          <h3 className="text-stalker-orange font-bold mb-2">📊 Days Since Release</h3>
          <p className="text-gray-300 text-2xl font-bold">{status.daysSinceStalkerRelease}</p>
        </div>
      </div>
    </div>
  );
}