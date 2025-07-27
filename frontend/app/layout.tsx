import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'STALKER 2 MP Tracker',
  description: 'Track the release status of STALKER 2: Heart of Chornobyl multiplayer mode',
  keywords: ['STALKER 2', 'multiplayer', 'tracker', 'GSC Game World'],
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  )
}