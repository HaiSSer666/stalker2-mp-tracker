'use client';

import { useState, useEffect } from 'react';
import { fetchStatus, triggerScrape, StatusResponse } from '@/app/lib/api';
import StatusDisplay from '@/app/components/StatusDisplay';
import NewsSection from '@/app/components/NewsSection';
import LoadingSpinner from '@/app/components/LoadingSpinner';
import ErrorMessage from '@/app/components/ErrorMessage';

export default function Home() {
  const [status, setStatus] = useState<StatusResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [refreshing, setRefreshing] = useState(false);

  const loadStatus = async () => {
    try {
      setError(null);
      const data = await fetchStatus();
      setStatus(data);
    } catch (err) {
      console.error('Error fetching status:', err);
      setError('Failed to load status. Backend server may be unavailable.');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  const handleManualRefresh = async () => {
    setRefreshing(true);
    try {
      await triggerScrape();
      await new Promise(resolve => setTimeout(resolve, 3000)); // Wait 3 seconds for scraping
      await loadStatus();
    } catch (err) {
      console.error('Error triggering scrape:', err);
      setError('Failed to trigger manual refresh.');
      setRefreshing(false);
    }
  };

  const handleRetry = () => {
    setLoading(true);
    setError(null);
    loadStatus();
  };

  useEffect(() => {
    loadStatus();
    
    // Auto-refresh every 5 minutes
    const interval = setInterval(loadStatus, 5 * 60 * 1000);
    
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error && !status) {
    return <ErrorMessage message={error} onRetry={handleRetry} />;
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-stalker-dark to-stalker-gray p-4">
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-8">
          <h1 className="text-5xl font-bold text-stalker-orange mb-4 font-mono glow">
            STALKER 2 MP TRACKER
          </h1>
          <p className="text-gray-400 font-mono">
            Tracking multiplayer release since {status?.stalker2ReleaseDate ? 
              new Date(status.stalker2ReleaseDate).toLocaleDateString() : 'November 2024'}
          </p>
        </div>

        {error && (
          <div className="mb-6 bg-red-900/20 border border-red-500/50 rounded-lg p-4">
            <p className="text-red-400 font-mono text-center">{error}</p>
          </div>
        )}

        {status && (
          <>
            <div className="mb-8">
              <StatusDisplay status={status} />
            </div>

            <div className="mb-8">
              <NewsSection news={status.latestNews} />
            </div>
          </>
        )}

        <div className="text-center">
          <button
            onClick={handleManualRefresh}
            disabled={refreshing}
            className={`px-6 py-3 rounded-lg font-mono transition-all ${
              refreshing 
                ? 'bg-gray-600 text-gray-400 cursor-not-allowed' 
                : 'bg-stalker-orange hover:bg-stalker-orange/80 text-white glow hover:pulse-glow'
            }`}
          >
            {refreshing ? '🔄 Refreshing...' : '🔄 Manual Refresh'}
          </button>
          <p className="text-gray-500 text-sm mt-2 font-mono">
            Auto-refreshes every 5 minutes | Last update: {status?.lastChecked ? 
              new Date(status.lastChecked).toLocaleTimeString() : 'Unknown'}
          </p>
        </div>

        <footer className="text-center mt-12 py-8 border-t border-stalker-orange/20">
          <p className="text-gray-500 font-mono text-sm">
            Created for the STALKER community | Data from{' '}
            <a 
              href="https://www.stalker2.com/de/news" 
              target="_blank" 
              rel="noopener noreferrer"
              className="text-stalker-orange hover:underline"
            >
              Official STALKER 2 News
            </a>
          </p>
        </footer>
      </div>
    </div>
  );
}