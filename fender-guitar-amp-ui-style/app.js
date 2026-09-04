let selectedAmp = null;
let knobValues = {};
let signalLevel = 0;

function init() {
  renderGrid('ampsGrid', AMP_DATA, 'amp');
  renderGrid('preampsGrid', PREAMP_DATA, 'preamp');
  renderGrid('cabinetsGrid', CABINET_DATA, 'cab');
  renderEffects();
  updateCounts();
  setupNavigation();
  animateSignal();
}

function renderGrid(containerId, data, type) {
  const container = document.getElementById(containerId);
  container.innerHTML = '';
  data.forEach(item => {
    const card = document.createElement('div');
    card.className = 'amp-card';
    card.dataset.id = item.id;
    card.dataset.type = type;

    const visualStyle = `background: linear-gradient(135deg, ${item.color}22 0%, ${item.color}11 100%);`;

    card.innerHTML = `
      <div class="amp-card-visual" style="${visualStyle}">
        <svg width="60" height="40" viewBox="0 0 60 40">
          <rect x="2" y="2" width="56" height="36" rx="3" fill="none" stroke="${item.color}44" stroke-width="1"/>
          <rect x="8" y="6" width="44" height="10" rx="1" fill="${item.color}33" stroke="${item.color}55" stroke-width="0.5"/>
          ${[14,20,26,32].map((y, i) => `<circle cx="${16 + i*8}" cy="${y}" r="3" fill="none" stroke="${item.color}66" stroke-width="0.5"/>`).join('')}
        </svg>
      </div>
      <div class="amp-card-body">
        <div class="amp-card-brand">${item.brand}</div>
        <div class="amp-card-name">${item.name}</div>
        <div class="amp-card-type">${item.type}</div>
      </div>
      <div class="amp-card-select">Select</div>
    `;

    card.addEventListener('click', () => selectAmp(item, type));
    container.appendChild(card);
  });
}

function renderEffects() {
  const container = document.getElementById('effectsGrid');
  container.innerHTML = '';
  EFFECTS_DATA.forEach(fx => {
    const slot = document.createElement('div');
    slot.className = 'fx-slot' + (fx.default ? ' active' : '');
    slot.dataset.id = fx.id;
    slot.innerHTML = `
      <div class="fx-slot-icon">${fx.icon}</div>
      <div class="fx-slot-name">${fx.name}</div>
      <div class="fx-slot-status">${fx.default ? 'Active' : 'Empty'}</div>
    `;
    slot.addEventListener('click', () => toggleEffect(slot, fx));
    container.appendChild(slot);
  });
}

function toggleEffect(slot, fx) {
  fx.default = !fx.default;
  slot.classList.toggle('active', fx.default);
  slot.querySelector('.fx-slot-status').textContent = fx.default ? 'Active' : 'Empty';
}

function updateCounts() {
  document.getElementById('ampCount').textContent = `${AMP_DATA.length} models`;
  document.getElementById('preampCount').textContent = `${PREAMP_DATA.length} models`;
  document.getElementById('cabCount').textContent = `${CABINET_DATA.length} models`;
  document.getElementById('fxCount').textContent = `${EFFECTS_DATA.filter(e => e.default).length} active`;
}

function setupNavigation() {
  document.querySelectorAll('.nav-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      const section = btn.dataset.section;
      document.querySelectorAll('.panel').forEach(p => p.classList.add('hidden'));
      document.getElementById(`section-${section}`).classList.remove('hidden');
    });
  });
}

function selectAmp(item, type) {
  document.querySelectorAll('.amp-card').forEach(c => c.classList.remove('selected'));
  const card = document.querySelector(`.amp-card[data-id="${item.id}"]`);
  if (card) card.classList.add('selected');

  selectedAmp = item;
  knobValues = {};
  item.knobs.forEach(k => knobValues[k.id] = k.default);

  openKnobPanel(item);
  updateDetail(item);
}

function openKnobPanel(item) {
  const panel = document.getElementById('knobPanel');
  const title = document.getElementById('knobPanelTitle');
  const row = document.getElementById('knobRow');

  title.textContent = item.name;
  row.innerHTML = '';

  item.knobs.forEach(knob => {
    const val = knobValues[knob.id] || knob.default;
    const angle = (val / 10) * 270 - 135;

    const wrapper = document.createElement('div');
    wrapper.className = 'knob-item';
    wrapper.innerHTML = `
      <div class="knob" data-knob="${knob.id}" data-angle="${angle}">
        <div class="knob-indicator" style="transform: translateX(-50%) rotate(${angle}deg); transform-origin: center 30px;"></div>
      </div>
      <div class="knob-label">${knob.label}</div>
      <div class="knob-value">${val.toFixed(1)}</div>
    `;

    wrapper.addEventListener('click', (e) => {
      if (e.target.closest('.knob')) {
        adjustKnob(wrapper, knob);
      }
    });

    row.appendChild(wrapper);
  });

  panel.classList.add('open');
  drawWaveform();
}

function adjustKnob(wrapper, knob) {
  const current = knobValues[knob.id] || knob.default;
  const newVal = Math.min(knob.max, Math.max(knob.min, current + 1));
  knobValues[knob.id] = newVal;

  const angle = (newVal / knob.max) * 270 - 135;
  const knobEl = wrapper.querySelector('.knob');
  const indicator = wrapper.querySelector('.knob-indicator');
  const valueEl = wrapper.querySelector('.knob-value');

  knobEl.dataset.angle = angle;
  indicator.style.transform = `translateX(-50%) rotate(${angle}deg)`;
  indicator.style.transformOrigin = 'center 30px';
  valueEl.textContent = newVal.toFixed(1);

  signalLevel = newVal / 10;
  drawWaveform();
}

function updateDetail(item) {
  const detail = document.getElementById('ampDetail');
  const nameEl = document.getElementById('detailAmpName');
  const descEl = document.getElementById('detailAmpDesc');

  nameEl.textContent = item.name;
  descEl.textContent = `${item.brand} | ${item.type} | ${item.watts ? item.watts + 'W' : ''} — ${item.desc}`;

  detail.classList.add('open');
}

function closeDetail() {
  document.getElementById('ampDetail').classList.remove('open');
  document.getElementById('knobPanel').classList.remove('open');
  selectedAmp = null;
}

function drawWaveform() {
  const canvas = document.getElementById('knobCanvas');
  const ctx = canvas.getContext('2d');
  const w = canvas.width;
  const h = canvas.height;
  const amp = signalLevel * h * 0.4;

  ctx.clearRect(0, 0, w, h);

  ctx.strokeStyle = '#2a2e32';
  ctx.lineWidth = 1;
  ctx.beginPath();
  ctx.moveTo(0, h / 2);
  ctx.lineTo(w, h / 2);
  ctx.stroke();

  const gradient = ctx.createLinearGradient(0, 0, w, 0);
  gradient.addColorStop(0, '#d4a017');
  gradient.addColorStop(0.5, '#e8c040');
  gradient.addColorStop(1, '#d4a017');

  ctx.strokeStyle = gradient;
  ctx.lineWidth = 2;
  ctx.beginPath();

  const freq = 0.02;
  const time = Date.now() * 0.002;

  for (let x = 0; x <= w; x++) {
    const y = h / 2 + Math.sin(x * freq + time) * amp * Math.sin(x / w * Math.PI);
    if (x === 0) ctx.moveTo(x, y);
    else ctx.lineTo(x, y);
  }
  ctx.stroke();

  if (selectedAmp) {
    requestAnimationFrame(drawWaveform);
  }
}

function animateSignal() {
  const meter = document.getElementById('signalMeter');
  setInterval(() => {
    const base = signalLevel || 0;
    const noise = Math.random() * 0.15;
    const level = Math.min(1, Math.max(0, base + noise - 0.05));
    meter.style.width = (level * 100) + '%';

    if (level > 0.8) {
      meter.style.background = 'linear-gradient(90deg, #ff4040, #ff6060)';
    } else if (level > 0.5) {
      meter.style.background = 'linear-gradient(90deg, #d4a017, #ff8040)';
    } else {
      meter.style.background = 'linear-gradient(90deg, #40d440, #80ff80)';
    }
  }, 100);
}

function updatePointer(item) {
  if (!selectedAmp) return;
  const pointer = document.getElementById('detailPointer');
  const angle = (knobValues['volume'] || 5) / 10 * 270 - 135;
  const rad = (angle - 90) * Math.PI / 180;
  const x2 = 60 + 38 * Math.cos(rad);
  const y2 = 60 + 38 * Math.sin(rad);
  pointer.setAttribute('x2', x2);
  pointer.setAttribute('y2', y2);
}

document.getElementById('knobPanelClose').addEventListener('click', closeDetail);
document.getElementById('detailKnob').addEventListener('click', () => {
  if (!selectedAmp) return;
  knobValues['volume'] = Math.min(10, (knobValues['volume'] || 5) + 1);
  updatePointer(selectedAmp);
  drawWaveform();
});

init();
