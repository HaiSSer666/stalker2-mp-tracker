# STALKER 2 Multiplayer Tracker

A full-stack web application that tracks the number of days since STALKER 2: Heart of Chornobyl 's release until the official announcement of its multiplayer mode.

## Features

- 🔍 **Automated Web Scraping**: Daily scraping of official STALKER 2 news for multiplayer announcements
- 📊 **Live Counter**: Real-time tracking of days since game release
- 📰 **News Feed**: Latest multiplayer-related news from official sources
- 🎮 **Responsive UI**: Modern, STALKER-themed interface with Tailwind CSS
- ⚡ **Real-time Updates**: Auto-refresh functionality and manual trigger options

## Tech Stack

### Backend
- **Java Spring Boot 3.2.0**
- **PostgreSQL** database
- **Jsoup** for web scraping
- **JPA/Hibernate** for data persistence
- **Scheduled tasks** for automation

### Frontend
- **Next.js 15** with TypeScript
- **Tailwind CSS** for styling
- **Axios** for API communication
- **React Hooks** for state management

## Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Database Setup

```sql
-- Create database
CREATE DATABASE stalker2_tracker;

-- Create user (optional)
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE stalker2_tracker TO postgres;
```

### 2. Backend Setup

```bash
cd backend

# Install dependencies
mvn clean install

# Update application.properties if needed
# Default configuration uses:
# - Database: localhost:5432/stalker2_tracker
# - Username: postgres
# - Password: postgres

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Create environment file
cp .env.local.example .env.local

# Run the development server
npm run dev
```

The frontend will start on `http://localhost:3000`

## API Endpoints

### GET `/api/status`
Returns the current multiplayer status and latest news.

**Response:**
```json
{
  "stalker2ReleaseDate": "2024-11-20",
  "mpReleased": false,
  "mpReleaseDate": null,
  "daysSinceStalkerRelease": 68,
  "lastChecked": "2025-01-27T12:00:00",
  "latestNews": [
    {
      "title": "Latest MP Update",
      "contentSnippet": "Preview of the content...",
      "url": "https://www.stalker2.com/news/...",
      "publishedDate": "2025-01-27"
    }
  ]
}
```

### POST `/api/scrape`
Manually triggers a scraping operation.

## Features in Detail

### Web Scraping
- Scrapes `https://www.stalker2.com/de/news` every 24 hours
- Searches for keywords: "multiplayer", "mp", "online", "coop", etc.
- Detects MP release announcements using pattern matching
- Stores news articles and status updates in the database

### Scheduled Tasks
- **Daily scrape**: Every day at midnight
- **Frequent check**: Every 6 hours
- **Startup scrape**: Runs immediately when the application starts

### Frontend Features
- **Auto-refresh**: Updates every 5 minutes
- **Manual refresh**: Trigger immediate scraping and update
- **Responsive design**: Works on desktop and mobile
- **Error handling**: Graceful fallbacks for network issues
- **Loading states**: User-friendly loading indicators

## Configuration

### Backend Configuration (application.properties)
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/stalker2_tracker
spring.datasource.username=postgres
spring.datasource.password=postgres

# Server
server.port=8080

# CORS
spring.web.cors.allowed-origins=http://localhost:3000
```

### Frontend Configuration (.env.local)
```
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
```

## Production Deployment

### Backend
1. Package the application: `mvn clean package`
2. Set production database configuration
3. Deploy the JAR file to your server
4. Ensure PostgreSQL is running and accessible

### Frontend
1. Build the application: `npm run build`
2. Set production API URL in environment variables
3. Deploy to your hosting platform (Vercel, Netlify, etc.)

## Monitoring

The application includes:
- Comprehensive logging for scraping operations
- Error handling for network failures
- Database connection monitoring
- API endpoint health checks

## Troubleshooting

### Frontend Build (`npm run build`) Fails

If you encounter errors during the frontend build step, try the following:

1. **Check Node.js version**: Ensure you are using Node.js 18 or higher.
2. **Clean install dependencies**:
   ```bash
   rm -rf node_modules package-lock.json
   npm install
   ```
3. **Check environment variables**: Make sure `.env.local` exists and is correctly configured.
4. **Review error logs**: Read the error output for missing modules or syntax errors.
5. **Update dependencies**: Run `npm update` to ensure all packages are compatible.
6. **Clear Next.js cache**:
   ```bash
   npm run clean
   ```
   or manually delete `.next` directory.

If the issue persists, search for the error message in the Next.js documentation or open an issue in this repository.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is for educational purposes and STALKER community use.

## Disclaimer

This application scrapes publicly available information from the official STALKER 2 website. Please respect the website's terms of service and rate limits.