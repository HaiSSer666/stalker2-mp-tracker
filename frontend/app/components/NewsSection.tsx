'use client';

import { NewsItem } from '@/app/lib/api';

interface NewsSectionProps {
  news: NewsItem[];
}

export default function NewsSection({ news }: NewsSectionProps) {
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  if (news.length === 0) {
    return (
      <div className="bg-stalker-gray rounded-lg p-8 shadow-2xl border border-stalker-orange/20">
        <h2 className="text-2xl font-bold text-stalker-orange mb-6 font-mono">
          📰 Latest Multiplayer News
        </h2>
        <div className="text-center py-8">
          <div className="text-4xl mb-4">🔍</div>
          <p className="text-gray-400 font-mono">No news about multiplayer yet.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-stalker-gray rounded-lg p-8 shadow-2xl border border-stalker-orange/20">
      <h2 className="text-2xl font-bold text-stalker-orange mb-6 font-mono">
        📰 Latest Multiplayer News
      </h2>
      
      <div className="space-y-6">
        {news.map((item, index) => (
          <article 
            key={index}
            className="bg-black/30 rounded-lg p-6 border border-stalker-orange/10 hover:border-stalker-orange/30 transition-colors"
          >
            <div className="flex flex-col md:flex-row md:items-start md:justify-between mb-3">
              <h3 className="text-lg font-bold text-white mb-2 md:mb-0 font-mono">
                <a 
                  href={item.url} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className="hover:text-stalker-orange transition-colors underline decoration-stalker-orange/50 hover:decoration-stalker-orange"
                >
                  {item.title}
                </a>
              </h3>
              <span className="text-stalker-orange text-sm font-mono whitespace-nowrap">
                {formatDate(item.publishedDate)}
              </span>
            </div>
            
            <p className="text-gray-300 leading-relaxed font-mono text-sm">
              {item.contentSnippet}
            </p>
            
            <div className="mt-4 pt-4 border-t border-stalker-orange/10">
              <a 
                href={item.url} 
                target="_blank" 
                rel="noopener noreferrer"
                className="inline-flex items-center text-stalker-orange hover:text-stalker-orange/80 transition-colors font-mono text-sm"
              >
                Read full article →
              </a>
            </div>
          </article>
        ))}
      </div>
    </div>
  );
}