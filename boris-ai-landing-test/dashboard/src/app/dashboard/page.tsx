'use client';

import {
  TrendingUp,
  TrendingDown,
  Users,
  DollarSign,
  Eye,
  ShoppingCart,
  Menu,
} from 'lucide-react';
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';
import { useState } from 'react';
import Sidebar from '@/components/Sidebar';
import Navbar from '@/components/Navbar';

const revenueData = [
  { name: 'Ene', revenue: 4000 },
  { name: 'Feb', revenue: 3000 },
  { name: 'Mar', revenue: 5000 },
  { name: 'Abr', revenue: 4500 },
  { name: 'May', revenue: 6000 },
  { name: 'Jun', revenue: 7500 },
  { name: 'Jul', revenue: 8000 },
];

const stats = [
  {
    title: 'Ingresos Totales',
    value: '$84,232',
    change: '+12.5%',
    trend: 'up' as const,
    icon: DollarSign,
    color: 'from-violet-500 to-purple-600',
  },
  {
    title: 'Usuarios Activos',
    value: '2,847',
    change: '+8.2%',
    trend: 'up' as const,
    icon: Users,
    color: 'from-blue-500 to-cyan-600',
  },
  {
    title: 'Visitas',
    value: '142,394',
    change: '+23.1%',
    trend: 'up' as const,
    icon: Eye,
    color: 'from-emerald-500 to-teal-600',
  },
  {
    title: 'Conversiones',
    value: '1,423',
    change: '-3.4%',
    trend: 'down' as const,
    icon: ShoppingCart,
    color: 'from-orange-500 to-red-600',
  },
];

export default function DashboardPage() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <>
      {/* Sidebar */}
      <Sidebar />

      {/* Mobile overlay */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-40 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Main content */}
      <div className="flex-1 flex flex-col min-w-0">
        {/* Navbar */}
        <Navbar />

        {/* Mobile menu button (visible only when sidebar is closed on mobile) */}
        <div className="lg:hidden px-4 pt-4">
          <button
            onClick={() => setSidebarOpen(true)}
            className="p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-lg transition"
          >
            <Menu size={20} />
          </button>
        </div>

        {/* Dashboard content */}
        <main className="flex-1 p-4 lg:p-6 overflow-y-auto">
          {/* Header */}
          <div className="mb-8">
            <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
            <p className="text-gray-500 mt-1">
              Bienvenido de vuelta. Aquí tienes un resumen de tu actividad.
            </p>
          </div>

          {/* Stats Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-6 mb-8">
            {stats.map((stat) => {
              const Icon = stat.icon;
              return (
                <div
                  key={stat.title}
                  className="bg-white rounded-xl border border-gray-200 p-6 hover:shadow-md transition-shadow"
                >
                  <div className="flex items-center justify-between mb-4">
                    <div
                      className={`w-11 h-11 rounded-lg bg-gradient-to-br ${stat.color} flex items-center justify-center`}
                    >
                      <Icon className="text-white" size={20} />
                    </div>
                    <div
                      className={`flex items-center gap-1 text-sm font-medium ${
                        stat.trend === 'up' ? 'text-emerald-600' : 'text-red-600'
                      }`}
                    >
                      {stat.trend === 'up' ? (
                        <TrendingUp size={16} />
                      ) : (
                        <TrendingDown size={16} />
                      )}
                      {stat.change}
                    </div>
                  </div>
                  <p className="text-2xl font-bold text-gray-900">{stat.value}</p>
                  <p className="text-sm text-gray-500 mt-1">{stat.title}</p>
                </div>
              );
            })}
          </div>

          {/* Charts Row */}
          <div className="grid grid-cols-1 xl:grid-cols-3 gap-6 mb-8">
            {/* Revenue Chart */}
            <div className="xl:col-span-2 bg-white rounded-xl border border-gray-200 p-6">
              <h2 className="text-lg font-semibold text-gray-900 mb-4">
                Ingresos
              </h2>
              <div className="h-80">
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={revenueData}>
                    <defs>
                      <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
                        <stop
                          offset="5%"
                          stopColor="#8b5cf6"
                          stopOpacity={0.3}
                        />
                        <stop
                          offset="95%"
                          stopColor="#8b5cf6"
                          stopOpacity={0}
                        />
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                    <XAxis dataKey="name" stroke="#9ca3af" fontSize={12} />
                    <YAxis stroke="#9ca3af" fontSize={12} />
                    <Tooltip
                      contentStyle={{
                        backgroundColor: '#1e293b',
                        border: 'none',
                        borderRadius: '8px',
                        color: '#fff',
                      }}
                    />
                    <Area
                      type="monotone"
                      dataKey="revenue"
                      stroke="#8b5cf6"
                      strokeWidth={2}
                      fillOpacity={1}
                      fill="url(#colorRevenue)"
                    />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </div>

            {/* Quick Stats */}
            <div className="bg-white rounded-xl border border-gray-200 p-6">
              <h2 className="text-lg font-semibold text-gray-900 mb-4">
                Resumen Rápido
              </h2>
              <div className="space-y-5">
                {[
                  { label: 'Tareas completadas', value: '234', sub: 'Esta semana' },
                  { label: 'Proyectos activos', value: '12', sub: 'En curso' },
                  { label: 'Horas facturables', value: '186h', sub: 'Del mes' },
                  { label: 'Satisfacción cliente', value: '98%', sub: 'Promedio' },
                  { label: 'Tickets abiertos', value: '7', sub: 'Requieren atención' },
                ].map((item) => (
                  <div key={item.label} className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-gray-500">{item.label}</p>
                      <p className="text-lg font-semibold text-gray-900">{item.value}</p>
                    </div>
                    <span className="text-xs text-gray-400 bg-gray-100 px-2 py-1 rounded-full">
                      {item.sub}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* Recent Activity Table */}
          <div className="bg-white rounded-xl border border-gray-200">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-semibold text-gray-900">
                Actividad Reciente
              </h2>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-gray-200">
                    <th className="text-left px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Proyecto
                    </th>
                    <th className="text-left px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Responsable
                    </th>
                    <th className="text-left px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Estado
                    </th>
                    <th className="text-left px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Progreso
                    </th>
                    <th className="text-right px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Fecha
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {[
                    {
                      project: 'Rediseño Landing Page',
                      person: 'María García',
                      status: 'En progreso',
                      statusColor: 'bg-blue-100 text-blue-700',
                      progress: 65,
                      progressColor: 'bg-blue-500',
                      date: '2024-01-15',
                    },
                    {
                      project: 'API REST v2',
                      person: 'Carlos López',
                      status: 'Completado',
                      statusColor: 'bg-emerald-100 text-emerald-700',
                      progress: 100,
                      progressColor: 'bg-emerald-500',
                      date: '2024-01-12',
                    },
                    {
                      project: 'App Mobile iOS',
                      person: 'Ana Martínez',
                      status: 'En revisión',
                      statusColor: 'bg-amber-100 text-amber-700',
                      progress: 85,
                      progressColor: 'bg-amber-500',
                      date: '2024-01-10',
                    },
                    {
                      project: 'Dashboard Analytics',
                      person: 'Pedro Sánchez',
                      status: 'En progreso',
                      statusColor: 'bg-blue-100 text-blue-700',
                      progress: 40,
                      progressColor: 'bg-blue-500',
                      date: '2024-01-08',
                    },
                    {
                      project: 'Integración Pagos',
                      person: 'Laura Torres',
                      status: 'Pendiente',
                      statusColor: 'bg-gray-100 text-gray-700',
                      progress: 15,
                      progressColor: 'bg-gray-400',
                      date: '2024-01-05',
                    },
                  ].map((row, i) => (
                    <tr key={i} className="hover:bg-gray-50">
                      <td className="px-6 py-4 text-sm font-medium text-gray-900">
                        {row.project}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {row.person}
                      </td>
                      <td className="px-6 py-4">
                        <span
                          className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${row.statusColor}`}
                        >
                          {row.status}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        <div className="w-full bg-gray-200 rounded-full h-2">
                          <div
                            className={`${row.progressColor} h-2 rounded-full transition-all`}
                            style={{ width: `${row.progress}%` }}
                          />
                        </div>
                        <span className="text-xs text-gray-500 mt-1 inline-block">
                          {row.progress}%
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500 text-right">
                        {row.date}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </main>
      </div>
    </>
  );
}
