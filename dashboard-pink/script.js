/* ============================================================
   CYBER//DASHBOARD — JavaScript (Refined)
   Real-time charts, logs, particles, network map + boot overlay
   Pink theme throughout.
   ============================================================ */

(function () {
  'use strict';

  // ── Helpers ──
  const $ = (sel) => document.querySelector(sel);
  const $$ = (sel) => document.querySelectorAll(sel);
  const rand = (min, max) => Math.random() * (max - min) + min;
  const randInt = (min, max) => Math.floor(rand(min, max + 1));

  // ── Boot Overlay ──
  function initBootOverlay() {
    const overlay = $('#bootOverlay');
    if (!overlay) return;
    const bar = $('#bootBar');
    let progress = 0;
    const messages = [
      'NEXUS v3.0 — INITIALIZING...',
      'LOADING CORE MODULES... ██████████',
      'ESTABLISHING ENCRYPTED CHANNEL...',
      'SYNCING NETWORK TOPOLOGY...',
      'SYSTEM ONLINE — WELCOME, OPERATOR'
    ];

    const interval = setInterval(() => {
      progress += rand(8, 18);
      if (progress > 100) progress = 100;
      bar.style.width = progress + '%';

      const msgIdx = Math.min(Math.floor(progress / 25), messages.length - 1);
      overlay.querySelector('.boot-text').textContent = messages[msgIdx];

      if (progress >= 100) {
        clearInterval(interval);
        setTimeout(() => overlay.classList.add('hidden'), 600);
      }
    }, 200);
  }

  // ── Particles Background (Pink Theme) ──
  function initParticles() {
    const canvas = document.getElementById('particles');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    let w, h, particles = [];
    const COUNT = 50;

    function resize() {
      w = canvas.width = window.innerWidth;
      h = canvas.height = window.innerHeight;
    }

    class Particle {
      constructor() { this.reset(); }
      reset() {
        this.x = rand(0, w);
        this.y = rand(0, h);
        this.vx = rand(-0.3, 0.3);
        this.vy = rand(-0.3, 0.3);
        this.r = rand(1, 2.5);
        this.alpha = rand(0.1, 0.35);
        // Mix of pink and cyan particles
        this.color = Math.random() > 0.4 ? '255,110,199' : '255,0,110';
      }
      update() {
        this.x += this.vx; this.y += this.vy;
        if (this.x < 0 || this.x > w) this.vx *= -1;
        if (this.y < 0 || this.y > h) this.vy *= -1;
      }
      draw() {
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
        ctx.fillStyle = `rgba(${this.color},${this.alpha})`;
        ctx.fill();
      }
    }

    for (let i = 0; i < COUNT; i++) particles.push(new Particle());

    function connectParticles() {
      for (let a = 0; a < particles.length; a++) {
        for (let b = a + 1; b < particles.length; b++) {
          const dx = particles[a].x - particles[b].x;
          const dy = particles[a].y - particles[b].y;
          const dist = Math.sqrt(dx * dx + dy * dy);
          if (dist < 160) {
            ctx.beginPath();
            ctx.moveTo(particles[a].x, particles[a].y);
            ctx.lineTo(particles[b].x, particles[b].y);
            const alpha = 0.07 * (1 - dist / 160);
            ctx.strokeStyle = `rgba(255,0,110,${alpha})`;
            ctx.lineWidth = 0.5;
            ctx.stroke();
          }
        }
      }
    }

    function animate() {
      ctx.clearRect(0, 0, w, h);
      particles.forEach(p => { p.update(); p.draw(); });
      connectParticles();
      requestAnimationFrame(animate);
    }

    window.addEventListener('resize', resize);
    resize();
    animate();
  }

  // ── Memory Bars (Pink) ──
  function initMemoryBars() {
    const container = document.getElementById('memoryBars');
    if (!container) return;
    for (let i = 0; i < 24; i++) {
      const bar = document.createElement('div');
      bar.className = 'bar-segment';
      bar.style.height = rand(15, 95) + '%';
      container.appendChild(bar);
    }

    setInterval(() => {
      $$('.bar-segment').forEach(bar => {
        bar.style.height = rand(15, 95) + '%';
      });
    }, 2000);
  }

  // ── Network Sparkline (Pink Theme) ──
  function initNetworkSparkline() {
    const container = document.getElementById('networkSparkline');
    if (!container) return;
    const canvas = document.createElement('canvas');
    canvas.width = 300; canvas.height = 40;
    container.appendChild(canvas);
    const ctx = canvas.getContext('2d');
    let data = Array.from({ length: 60 }, () => rand(10, 90));

    function draw() {
      ctx.clearRect(0, 0, 300, 40);
      const grad = ctx.createLinearGradient(0, 0, 0, 40);
      grad.addColorStop(0, 'rgba(255,0,110,0.3)');
      grad.addColorStop(1, 'rgba(255,0,110,0.01)');

      ctx.beginPath();
      const step = 300 / (data.length - 1);
      data.forEach((v, i) => {
        const x = i * step;
        const y = 40 - (v / 100) * 36;
        if (i === 0) ctx.moveTo(x, y); else ctx.lineTo(x, y);
      });
      ctx.strokeStyle = '#ff6ec7';
      ctx.lineWidth = 1.5;
      ctx.shadowColor = 'rgba(255,0,110,0.4)';
      ctx.shadowBlur = 6;
      ctx.stroke();
      ctx.shadowBlur = 0;

      ctx.lineTo(300, 40);
      ctx.lineTo(0, 40);
      ctx.closePath();
      ctx.fillStyle = grad;
      ctx.fill();

      data.shift();
      data.push(rand(10, 95));
    }

    draw();
    setInterval(draw, 800);
  }

  // ── Threat Fill Animation ──
  function initThreatMeter() {
    const fill = document.getElementById('threatFill');
    if (!fill) return;
    let val = 35 + rand(-10, 10);
    setInterval(() => {
      val += rand(-8, 6);
      val = Math.max(5, Math.min(92, val));
      fill.style.width = val + '%';
    }, 3000);
  }

  // ── Sidebar Waveform ──
  function initWaveform() {
    const canvas = document.getElementById('waveCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    let phase = 0;

    function draw() {
      ctx.clearRect(0, 0, 210, 40);
      const colors = ['#ff6ec7', '#ff006e', '#c084fc'];

      for (let c = 0; c < 3; c++) {
        ctx.beginPath();
        for (let x = 0; x <= 210; x += 2) {
          const freq = 0.05 + c * 0.02;
          const amp = 8 - c * 2;
          const y = 20 + Math.sin(x * freq + phase + c * 1.5) * amp;
          if (x === 0) ctx.moveTo(x, y); else ctx.lineTo(x, y);
        }
        ctx.strokeStyle = colors[c];
        ctx.globalAlpha = 0.3 - c * 0.08;
        ctx.lineWidth = 1.5 - c * 0.2;
        ctx.stroke();
      }
      ctx.globalAlpha = 1;
      phase += 0.04;
      requestAnimationFrame(draw);
    }

    draw();
  }

  // ── Network Topology Canvas (Pink Theme) ──
  function initNetworkMap() {
    const container = document.querySelector('.network-panel');
    if (!container) return;
    const canvas = document.createElement('canvas');
    canvas.style.width = '100%';
    canvas.style.height = '100%';
    canvas.style.display = 'block';
    container.appendChild(canvas);

    const ctx = canvas.getContext('2d');
    let nodes = [];
    const NODE_COUNT = 24;

    function resize() {
      const rect = container.getBoundingClientRect();
      canvas.width = rect.width * window.devicePixelRatio;
      canvas.height = rect.height * window.devicePixelRatio;
      ctx.scale(window.devicePixelRatio, window.devicePixelRatio);
    }

    class NetNode {
      constructor() {
        this.x = rand(40, 600);
        this.y = rand(40, 320);
        this.vx = rand(-0.5, 0.5);
        this.vy = rand(-0.5, 0.5);
        this.r = rand(3, 7);
        this.type = ['core', 'edge', 'server'][randInt(0, 2)];
      }
      update() {
        this.x += this.vx; this.y += this.vy;
        if (this.x < 20 || this.x > canvas.width / window.devicePixelRatio - 20) this.vx *= -1;
        if (this.y < 20 || this.y > canvas.height / window.devicePixelRatio - 20) this.vy *= -1;
      }
      draw() {
        const color = this.type === 'core' ? '#ff6ec7' : this.type === 'server' ? '#c084fc' : '#ff006e';
        // Glow
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r + 6, 0, Math.PI * 2);
        ctx.fillStyle = color.replace(')', ',0.1)').replace('#', 'rgba(').length > 7 ? color : `rgba(${parseInt(color.slice(1,3),16)},${parseInt(color.slice(3,5),16)},${parseInt(color.slice(5,7),16)},0.1)`;
        ctx.fill();

        // Node dot
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r, 0, Math.PI * 2);
        ctx.fillStyle = color;
        ctx.shadowColor = color;
        ctx.shadowBlur = 14;
        ctx.fill();
        ctx.shadowBlur = 0;

        // Ring
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r + 3, 0, Math.PI * 2);
        ctx.strokeStyle = `rgba(255,110,199,0.15)`;
        ctx.lineWidth = 1;
        ctx.stroke();
      }
    }

    for (let i = 0; i < NODE_COUNT; i++) nodes.push(new NetNode());

    function drawConnections() {
      const MAX_DIST = 220;
      const pw = canvas.width / window.devicePixelRatio;
      const ph = canvas.height / window.devicePixelRatio;

      for (let a = 0; a < nodes.length; a++) {
        for (let b = a + 1; b < nodes.length; b++) {
          const dx = nodes[a].x - nodes[b].x;
          const dy = nodes[a].y - nodes[b].y;
          const dist = Math.sqrt(dx * dx + dy * dy);
          if (dist < MAX_DIST) {
            ctx.beginPath();
            ctx.moveTo(nodes[a].x, nodes[a].y);
            ctx.lineTo(nodes[b].x, nodes[b].y);
            const alpha = 0.12 * (1 - dist / MAX_DIST);
            ctx.strokeStyle = `rgba(255,0,110,${alpha})`;
            ctx.lineWidth = 0.7;
            ctx.stroke();

            // Animated packet
            if (Math.random() < 0.004) {
              const t = rand(0, 1);
              const px = nodes[a].x + (nodes[b].x - nodes[a].x) * t;
              const py = nodes[a].y + (nodes[b].y - nodes[a].y) * t;
              ctx.beginPath();
              ctx.arc(px, py, 2.5, 0, Math.PI * 2);
              ctx.fillStyle = 'rgba(192,132,252,0.85)';
              ctx.shadowColor = '#c084fc';
              ctx.shadowBlur = 6;
              ctx.fill();
              ctx.shadowBlur = 0;
            }
          }
        }
      }

      // Update network stats overlay
      const packetsEl = $('#netPackets');
      if (packetsEl) packetsEl.textContent = (12000 + randInt(0, 5000)).toLocaleString() + '/s';
      const latencyEl = $('#netLatency');
      if (latencyEl) latencyEl.textContent = randInt(3, 20) + 'ms';
    }

    function animate() {
      const pw = canvas.width / window.devicePixelRatio;
      const ph = canvas.height / window.devicePixelRatio;
      ctx.clearRect(0, 0, pw, ph);
      drawConnections();
      nodes.forEach(n => { n.update(); n.draw(); });
      requestAnimationFrame(animate);
    }

    window.addEventListener('resize', resize);
    resize();
    animate();
  }

  // ── System Logs (Pink-themed levels) ──
  function initLogs() {
    const panel = document.getElementById('logPanel');
    if (!panel) return;

    const messages = [
      { level: 'info', msg: 'Connection established to node alpha-7' },
      { level: 'ok', msg: 'System health check passed — all nominal' },
      { level: 'warn', msg: 'High latency detected on interface eth2 (45ms)' },
      { level: 'error', msg: 'Failed handshake from 192.168.44.101 — invalid cert' },
      { level: 'info', msg: 'Auto-scaling triggered — spinning up instance-3f' },
      { level: 'ok', msg: 'Backup snapshot completed (4.2 GB compressed)' },
      { level: 'warn', msg: 'Memory threshold warning — pool: database-shard-05' },
      { level: 'info', msg: 'Certificate rotation scheduled for 2026-08-14' },
      { level: 'error', msg: 'Auth failure from IP 10.0.7.33 — rate limited' },
      { level: 'ok', msg: 'Firewall rules updated — 3 new deny entries applied' },
      { level: 'info', msg: 'Network topology refreshed — 24 nodes active' },
      { level: 'warn', msg: 'Disk I/O bottleneck on /dev/sda1 — queue depth > 64' },
    ];

    function addLogEntry() {
      const entry = document.createElement('div');
      entry.className = 'log-entry';

      const now = new Date();
      const timeStr = now.toTimeString().split(' ')[0] + '.' + String(now.getMilliseconds()).padStart(3, '0').slice(0, 2);

      const msg = messages[randInt(0, messages.length - 1)];
      entry.innerHTML = `
        <span class="log-time">${timeStr}</span>
        <span class="log-level ${msg.level}">[${msg.level.toUpperCase()}]</span>
        <span class="log-msg">${msg.msg}</span>
      `;

      panel.appendChild(entry);

      while (panel.children.length > 50) {
        panel.removeChild(panel.firstChild);
      }

      panel.scrollTop = panel.scrollHeight;
    }

    for (let i = 0; i < 12; i++) addLogEntry();
    setInterval(addLogEntry, 2000);
  }

  // ── Comms Feed (Pink themed) ──
  function initComms() {
    const panel = document.getElementById('commsPanel');
    if (!panel) return;

    const senders = ['OP_DELTA', 'HQ_NEXUS', 'GHOST_SHARK', 'VIGILANTE', 'SYS_ALERT'];
    const texts = [
      'Rerouting through secondary node — ETA 12s',
      'Packet intercepted — decrypting...',
      'New cipher key distributed to all nodes',
      'Perimeter scan complete — no anomalies detected',
      'Incoming transmission from sector 7G encrypted',
      'Signal boost relayed via satellite uplink',
      'Encrypted burst received — parsing payload',
    ];

    function addComm() {
      const div = document.createElement('div');
      div.className = 'comm-item';
      const now = new Date();
      const timeStr = now.toTimeString().split(' ')[0].slice(0, 5);
      const sender = senders[randInt(0, senders.length - 1)];
      const text = texts[randInt(0, texts.length - 1)];

      div.innerHTML = `
        <span class="comm-time">${timeStr}</span>
        <div><span class="comm-sender">&lt;${sender}&gt;</span></div>
        <div class="comm-text">${text}</div>
      `;

      panel.appendChild(div);
      while (panel.children.length > 30) {
        panel.removeChild(panel.firstChild);
      }
      panel.scrollTop = panel.scrollHeight;
    }

    // Initial batch
    for (let i = 0; i < 5; i++) addComm();
    setInterval(addComm, 4000);
  }

  // ── Active Nodes (Pink theme) ──
  function initNodes() {
    const panel = document.getElementById('nodesPanel');
    if (!panel) return;

    const nodeData = [
      { name: 'alpha-7', loc: 'US-EAST', status: 'online', metric: '12ms' },
      { name: 'beta-3', loc: 'EU-WEST', status: 'online', metric: '45ms' },
      { name: 'gamma-x9', loc: 'AP-SOUTH', status: 'warning', metric: '120ms' },
      { name: 'delta-1a', loc: 'US-WEST', status: 'online', metric: '8ms' },
      { name: 'epsilon-v4', loc: 'SA-NORTH', status: 'offline', metric: '—' },
      { name: 'zeta-core', loc: 'GLOBAL-CDN', status: 'online', metric: '3ms' },
    ];

    function render() {
      panel.innerHTML = '';
      nodeData.forEach(n => {
        n.metric = (n.status === 'online') ? randInt(1, 80) + 'ms' : n.metric;
        const div = document.createElement('div');
        div.className = 'node-item';
        div.innerHTML = `
          <div class="node-info">
            <span class="node-status-dot ${n.status}"></span>
            <div>
              <div class="node-name">${n.name}</div>
              <div class="node-loc">${n.loc}</div>
            </div>
          </div>
          <div class="node-metric">
            <div class="node-metric-val">${n.metric}</div>
            <div class="node-metric-label">${n.status.toUpperCase()}</div>
          </div>
        `;
        panel.appendChild(div);
      });

      const onlineEl = $('#onlineCount');
      if (onlineEl) {
        const count = nodeData.filter(n => n.status === 'online').length;
        onlineEl.textContent = `${count}/${nodeData.length} ONLINE`;
      }
    }

    render();
    setInterval(render, 3000);
  }

  // ── Main Performance Chart (Pink Theme) ──
  function initMainChart() {
    const container = document.querySelector('.chart-panel');
    if (!container) return;
    const canvas = document.createElement('canvas');
    canvas.style.width = '100%';
    canvas.style.height = '100%';
    canvas.style.display = 'block';
    container.appendChild(canvas);

    const ctx = canvas.getContext('2d');
    let cpuData = Array.from({ length: 60 }, () => rand(30, 80));
    let memData = Array.from({ length: 60 }, () => rand(40, 70));
    let netData = Array.from({ length: 60 }, () => rand(20, 90));

    function resize() {
      const rect = container.getBoundingClientRect();
      canvas.width = rect.width * window.devicePixelRatio;
      canvas.height = rect.height * window.devicePixelRatio;
      ctx.scale(window.devicePixelRatio, window.devicePixelRatio);
    }

    function drawLine(data, color, maxVal) {
      const w = container.getBoundingClientRect().width;
      const h = container.getBoundingClientRect().height;
      const step = (w - 60) / (data.length - 1);

      ctx.beginPath();
      data.forEach((v, i) => {
        const x = 40 + i * step;
        const y = h - 30 - ((v / maxVal) * (h - 50));
        if (i === 0) ctx.moveTo(x, y); else ctx.lineTo(x, y);
      });

      // Glow stroke
      ctx.shadowColor = color;
      ctx.shadowBlur = 10;
      ctx.strokeStyle = color;
      ctx.lineWidth = 2;
      ctx.stroke();
      ctx.shadowBlur = 0;

      // Fill under curve
      const r = parseInt(color.slice(1,3), 16);
      const g = parseInt(color.slice(3,5), 16);
      const b = parseInt(color.slice(5,7), 16);
      const grad2 = ctx.createLinearGradient(0, 0, 0, h);
      grad2.addColorStop(0, `rgba(${r},${g},${b},0.18)`);
      grad2.addColorStop(1, `rgba(${r},${g},${b},0.01)`);

      ctx.lineTo(40 + (data.length - 1) * step, h - 30);
      ctx.lineTo(40, h - 30);
      ctx.closePath();
      ctx.fillStyle = grad2;
      ctx.fill();
    }

    function drawGrid(w, h) {
      ctx.strokeStyle = 'rgba(255,0,110,0.06)';
      ctx.lineWidth = 0.5;
      for (let i = 0; i < 5; i++) {
        const y = 30 + (i / 4) * (h - 60);
        ctx.beginPath();
        ctx.moveTo(40, y);
        ctx.lineTo(w - 20, y);
        ctx.stroke();

        ctx.fillStyle = 'rgba(255,255,255,0.15)';
        ctx.font = '9px "Share Tech Mono"';
        ctx.textAlign = 'right';
        ctx.fillText(Math.round(100 - (i / 4) * 100) + '%', 36, y + 4);
      }
    }

    function drawLegend() {
      const w = container.getBoundingClientRect().width;
      let x = w - 250;
      const items = [
        { label: 'CPU', color: '#ff6ec7' },
        { label: 'MEM', color: '#c084fc' },
        { label: 'NET', color: '#ff006e' }
      ];
      items.forEach(item => {
        ctx.fillStyle = item.color;
        ctx.fillRect(x, 6, 22, 3);
        ctx.font = '9px "Share Tech Mono"';
        ctx.textAlign = 'left';
        ctx.fillText(item.label, x + 28, 11);
        x += 70;
      });
    }

    function animate() {
      const w = container.getBoundingClientRect().width;
      const h = container.getBoundingClientRect().height;
      ctx.clearRect(0, 0, w, h);

      drawGrid(w, h);
      drawLine(cpuData, '#ff6ec7', 100);
      drawLine(memData, '#c084fc', 100);
      drawLine(netData, '#ff006e', 100);
      drawLegend();

      cpuData.shift(); cpuData.push(Math.max(10, Math.min(98, cpuData[cpuData.length - 1] + rand(-12, 12))));
      memData.shift(); memData.push(Math.max(20, Math.min(95, memData[memData.length - 1] + rand(-8, 8))));
      netData.shift(); netData.push(Math.max(5, Math.min(99, netData[netData.length - 1] + rand(-15, 15))));

      requestAnimationFrame(animate);
    }

    window.addEventListener('resize', resize);
    resize();
    animate();
  }

  // ── Disk Usage Panel (Pink theme) ──
  function initDiskPanel() {
    const panel = document.getElementById('diskPanel');
    if (!panel) return;

    const disks = [
      { name: '/ (root)', used: 62, total: '500GB', color: '#ff6ec7' },
      { name: '/var/log', used: 34, total: '100GB', color: '#c084fc' },
      { name: '/home/data', used: 81, total: '2TB', color: '#ff006e' },
      { name: '/tmp/cache', used: 12, total: '50GB', color: '#22d3ee' },
      { name: 'swap-sda1', used: 47, total: '8GB', color: '#fbbf24' },
    ];

    disks.forEach(d => {
      const div = document.createElement('div');
      div.className = 'disk-item';
      div.innerHTML = `
        <div class="disk-label-row">
          <span class="disk-name">${d.name}</span>
          <span class="disk-info">${Math.round(d.used)}% — ${d.total}</span>
        </div>
        <div class="disk-bar-track">
          <div class="disk-bar-fill" style="width:${d.used}%; background: linear-gradient(90deg, ${d.color}, ${d.color}88); box-shadow: 0 0 10px ${d.color}44;"></div>
        </div>
      `;
      panel.appendChild(div);
    });

    setInterval(() => {
      const fills = $$('.disk-bar-fill');
      const infos = $$('.disk-info');
      disks.forEach((d, i) => {
        d.used += rand(-5, 4);
        d.used = Math.max(10, Math.min(98, d.used));
        if (fills[i]) fills[i].style.width = d.used + '%';
        if (infos[i]) infos[i].textContent = Math.round(d.used) + '% — ' + d.total;
      });
    }, 4000);
  }

  // ── Security Feed Panel (Pink theme) ──
  function initSecurityPanel() {
    const panel = document.getElementById('securityPanel');
    if (!panel) return;

    const events = [
      { icon: 'blocked', label: '🛡️', text: 'Blocked intrusion attempt from 185.220.xx.x' },
      { icon: 'safe', label: '✓', text: 'SSL certificate validated — expires in 47 days' },
      { icon: 'warning', label: '! ', text: 'Unusual traffic spike on port 8443' },
      { icon: 'blocked', label: '🛡️', text: 'DDoS mitigation active — 2.1K req/s dropped' },
      { icon: 'safe', label: '✓', text: 'Firewall rules synchronized across all nodes' },
    ];

    events.forEach(e => {
      const div = document.createElement('div');
      div.className = 'sec-item';
      const ago = randInt(1, 58) + 'm ago';
      div.innerHTML = `
        <span class="sec-icon ${e.icon}">${e.label}</span>
        <span class="sec-text">${e.text}</span>
        <span class="sec-time">${ago}</span>
      `;
      panel.appendChild(div);
    });

    // Periodic new security events
    setInterval(() => {
      const newEvents = [
        { icon: 'blocked', label: '🛡️', text: 'Port scan detected from 45.33.xx.x' },
        { icon: 'warning', label: '! ', text: 'Unusual outbound traffic on port 4444' },
        { icon: 'safe', label: '✓', text: 'Vulnerability patch applied — CVE-2026-1234' },
        { icon: 'blocked', label: '🛡️', text: 'Brute force blocked — 89 attempts from single IP' },
      ];
      const evt = newEvents[randInt(0, newEvents.length - 1)];
      const ago = randInt(1, 5) + 'm ago';

      const div = document.createElement('div');
      div.className = 'sec-item';
      div.innerHTML = `
        <span class="sec-icon ${evt.icon}">${evt.label}</span>
        <span class="sec-text">${evt.text}</span>
        <span class="sec-time">${ago}</span>
      `;
      panel.insertBefore(div, panel.firstChild);

      while (panel.children.length > 20) {
        panel.removeChild(panel.lastChild);
      }
    }, 6000);
  }

  // ── Process Monitor Panel (Pink theme) ──
  function initProcessPanel() {
    const panel = document.getElementById('processPanel');
    if (!panel) return;

    const procs = [
      'nginx', 'systemd-journald', 'docker-proxy', 'node --max-old-size=4096',
      'postgres: writer process', 'redis-server *:6379', 'python3 ml_pipeline.py',
      'java -jar auth-service.jar', 'elasticsearch', 'kibana',
    ];

    function render() {
      panel.innerHTML = '';
      procs.forEach(name => {
        const cpu = rand(0.1, 45).toFixed(1);
        const mem = rand(12, 890).toFixed(0);
        const row = document.createElement('div');
        row.className = 'proc-row';
        row.innerHTML = `
          <span class="proc-name">${name}</span>
          <span class="proc-cpu">${cpu}%</span>
          <span class="proc-mem">${mem}M</span>
        `;
        panel.appendChild(row);
      });

      const countEl = $('#procCount');
      if (countEl) countEl.textContent = randInt(140, 165);
    }

    render();
    setInterval(render, 3000);
  }

  // ── Hex Stream Panel ──
  function initHexStream() {
    const panel = document.getElementById('hexPanel');
    if (!panel) return;

    function generateHexRow() {
      const addr = '0x' + randInt(0x1000, 0xFFFF).toString(16).toUpperCase().padStart(4, '0');
      let hexBytes = '';
      for (let i = 0; i < 8; i++) {
        hexBytes += randInt(0, 255).toString(16).padStart(2, '0').toUpperCase() + ' ';
      }
      const ascii = Array.from(hexBytes.trim().split(' '), b =>
        String.fromCharCode(parseInt(b, 16) > 31 && parseInt(b, 16) < 127 ? parseInt(b, 16) : 46)
      ).join('');

      return `<div class="hex-row"><span class="hex-addr">${addr}:</span><span class="hex-data">${hexBytes}</span><span class="hex-ascii">"${ascii}"</span></div>`;
    }

    function addHex() {
      const div = document.createElement('div');
      div.innerHTML = generateHexRow();
      panel.appendChild(div);
      while (panel.children.length > 40) {
        panel.removeChild(panel.firstChild);
      }
      panel.scrollTop = panel.scrollHeight;
    }

    for (let i = 0; i < 15; i++) addHex();
    setInterval(addHex, 300);

    // Update stream rate
    const rateEl = $('#streamRate');
    if (rateEl) {
      setInterval(() => {
        const val = rand(0.8, 2.4).toFixed(1);
        rateEl.textContent = val + ' TB/s';
      }, 3000);
    }
  }

  // ── Glitch Overlay Trigger ──
  function initGlitchOverlay() {
    const overlay = document.getElementById('glitchOverlay');
    if (!overlay) return;

    function trigger() {
      overlay.classList.add('active');
      setTimeout(() => overlay.classList.remove('active'), 350);
    }

    function scheduleNext() {
      const delay = rand(8000, 25000);
      setTimeout(() => {
        trigger();
        scheduleNext();
      }, delay);
    }
    scheduleNext();
  }

  // ── Sidebar Toggle ──
  function initSidebar() {
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');
    const menuBtn = document.getElementById('menuBtn');
    const toggleBtn = document.getElementById('toggleSidebar');

    if (toggleBtn) {
      toggleBtn.addEventListener('click', () => {
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('expanded');
      });
    }

    if (menuBtn) {
      menuBtn.addEventListener('click', () => {
        sidebar.classList.toggle('mobile-open');
      });
    }

    $$('.nav-item').forEach(item => {
      item.addEventListener('click', (e) => {
        e.preventDefault();
        $$('.nav-item').forEach(n => n.classList.remove('active'));
        item.classList.add('active');
        sidebar.classList.remove('mobile-open');

        const label = item.querySelector('.nav-label')?.textContent || '';
        const breadcrumb = document.querySelector('.breadcrumb');
        if (breadcrumb) breadcrumb.textContent = `// ${label.toLowerCase()} //`;
      });
    });
  }

  // ── Chart Range Buttons ──
  function initChartButtons() {
    $$('.chart-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        $$('.chart-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
      });
    });
  }

  // ── Clock & Uptime ──
  function initClock() {
    const timeEl = $('#clockTime');
    const dateEl = $('#clockDate');
    const uptimeEl = $('#uptimeValue');

    if (timeEl) {
      setInterval(() => {
        const now = new Date();
        timeEl.textContent = now.toTimeString().split(' ')[0];
        dateEl.textContent = now.toISOString().split('T')[0];
      }, 1000);
    }

    // Simulate uptime from random start
    if (uptimeEl) {
      const startTime = Date.now() - rand(86400000, 604800000);
      setInterval(() => {
        const elapsed = Math.floor((Date.now() - startTime) / 1000);
        const h = String(Math.floor(elapsed / 3600)).padStart(2, '0');
        const m = String(Math.floor((elapsed % 3600) / 60)).padStart(2, '0');
        const s = String(elapsed % 60).padStart(2, '0');
        uptimeEl.textContent = `${h}:${m}:${s}`;
      }, 1000);
    }

    // Signal bars animation
    const signalBars = document.querySelector('.signal-bars');
    if (signalBars) {
      setInterval(() => {
        const activeCount = randInt(3, 5);
        signalBars.querySelectorAll('.bar').forEach((bar, i) => {
          bar.style.opacity = i < activeCount ? '1' : '0.2';
          bar.style.boxShadow = i < activeCount ? `0 0 4px rgba(255,110,199,0.3)` : 'none';
        });
      }, 2000);
    }

    // Encryption key display animation
    const encKeyEl = $('#encKeyDisplay');
    if (encKeyEl) {
      setInterval(() => {
        const hex = Array.from({ length: 4 }, () => randInt(0, 65535).toString(16).padStart(4, '0')).join('');
        encKeyEl.textContent = `KEY: ${hex.slice(0,8)}...${hex.slice(-4)}`;
      }, 15000);
    }

    // Fingerprint animation
    const fpEl = $('#sysFingerprint');
    if (fpEl) {
      setInterval(() => {
        fpEl.textContent = 'FP: ' + randInt(0, 65535).toString(16) + '...';
      }, 20000);
    }

    // Notification badge animation
    const notifBadge = $('#notifBadge');
    if (notifBadge) {
      setInterval(() => {
        notifBadge.textContent = randInt(1, 7);
      }, 30000);
    }
  }

  // ── Initialize All ──
  document.addEventListener('DOMContentLoaded', () => {
    initBootOverlay();
    initParticles();
    initMemoryBars();
    initNetworkSparkline();
    initThreatMeter();
    initWaveform();
    initNetworkMap();
    initLogs();
    initComms();
    initNodes();
    initMainChart();
    initDiskPanel();
    initSecurityPanel();
    initProcessPanel();
    initHexStream();
    initGlitchOverlay();
    initSidebar();
    initChartButtons();
    initClock();
  });

})();
