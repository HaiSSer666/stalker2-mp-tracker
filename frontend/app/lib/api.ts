import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

export interface NewsItem {
  title: string;
  contentSnippet: string;
  url: string;
  publishedDate: string;
}

export interface StatusResponse {
  stalker2ReleaseDate: string;
  mpReleased: boolean;
  mpReleaseDate: string | null;
  daysSinceStalkerRelease: number;
  lastChecked: string;
  latestNews: NewsItem[];
}

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
});

export const fetchStatus = async (): Promise<StatusResponse> => {
  const response = await api.get<StatusResponse>('/status');
  return response.data;
};

export const triggerScrape = async (): Promise<string> => {
  const response = await api.post<string>('/scrape');
  return response.data;
};