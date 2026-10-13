import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Boris AI - Dashboard",
  description: "Panel de control de Boris AI",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="es">
      <body className="antialiased">
        <div className="flex h-screen overflow-hidden">
          {children}
        </div>
      </body>
    </html>
  );
}
