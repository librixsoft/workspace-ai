(function() {
  let currentTab = 'amps';
  let activeRig = null;
  let currentKnobs = [];
  let knobDrag = null;

  function init() {
    renderLibrary();
    setupNavigation();
    setupSearch();
    setupChainSlots();
  }

  function setupNavigation() {
    document.querySelectorAll('.nav-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        currentTab = btn.dataset.tab;
        renderLibrary();
      });
    });
  }

  function setupSearch() {
    const input = document.getElementById('search-input');
    input.addEventListener('input', () => renderLibrary(input.value));
  }

  function setupChainSlots() {
    document.querySelectorAll('.chain-slot').forEach(slot => {
      slot.addEventListener('click', () => {
        if (activeRig) {
          const slotType = slot.dataset.slot;
          if (activeRig.type.toLowerCase().includes(slotType) || slotType === 'amp' && activeRig.type === 'Amp') {
            selectRig(activeRig);
          }
        }
      });
    });
  }

  function getFilteredRigs(filter) {
    let rigs = AMP_DATA.amps;
    if (currentTab !== 'all') {
      const tabMap = { amps: 'Amp', cabs: 'Cab', pedals: 'Pre-Amp', fx: 'Effects' };
      rigs = rigs.filter(r => r.type === tabMap[currentTab] || currentTab === 'all');
    }
    if (filter) {
      const f = filter.toLowerCase();
      rigs = rigs.filter(r => r.name.toLowerCase().includes(f) || r.desc.toLowerCase().includes(f));
    }
    return rigs;
  }

  function renderLibrary(filter) {
    const grid = document.getElementById('library-grid');
    const title = document.getElementById('library-title');
    const tabNames = { amps: 'Amp Library', cabs: 'Cab Library', pedals: 'Pre-Amp Library', fx: 'Effects Library' };
    title.textContent = tabNames[currentTab] || 'Library';

    const rigs = getFilteredRigs(filter || '');
    grid.innerHTML = '';

    rigs.forEach(rig => {
      const card = document.createElement('div');
      card.className = 'rig-card' + (activeRig && activeRig.id === rig.id ? ' active' : '');
      card.innerHTML = `
        <div class="rig-card-icon" style="background: linear-gradient(145deg, ${rig.color}22, ${rig.color}11); border: 1px solid ${rig.color}44;">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="${rig.color}" stroke-width="1.5">
            ${getIconForType(rig.type)}
          </svg>
        </div>
        <div class="rig-card-info">
          <div class="rig-card-name">${rig.name}</div>
          <div class="rig-card-type">${rig.type}</div>
          <div class="rig-card-desc">${rig.desc}</div>
        </div>
      `;
      card.addEventListener('click', () => selectRig(rig));
      grid.appendChild(card);
    });
  }

  function getIconForType(type) {
    switch(type) {
      case 'Amp': return '<circle cx="12" cy="12" r="8"/><line x1="12" y1="8" x2="12" y2="16"/>';
      case 'Pre-Amp': return '<polygon points="12,2 22,12 12,22 2,12"/>';
      case 'Cab': return '<rect x="3" y="3" width="18" height="18" rx="2"/>';
      case 'Effects': return '<path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/>';
      default: return '<circle cx="12" cy="12" r="10"/>';
    }
  }

  function selectRig(rig) {
    activeRig = rig;

    document.getElementById('display-title').textContent = rig.name;
    document.getElementById('display-meta').textContent = rig.type + ' — ' + rig.desc;
    document.getElementById('plate-model').textContent = rig.name;

    const powerDot = document.getElementById('power-dot');
    powerDot.classList.add('on');

    renderKnobs(rig);
    renderSwitches(rig);
    renderLibrary();

    document.querySelectorAll('.chain-slot').forEach(slot => {
      slot.classList.remove('selected', 'has-gear');
      const slotType = slot.dataset.slot;
      if (rig.type.toLowerCase().includes(slotType) || (slotType === 'amp' && rig.type === 'Amp')) {
        slot.classList.add('has-gear', 'selected');
        slot.querySelector('.slot-inner').innerHTML = `
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#C8A45C" stroke-width="1.5">
            ${getIconForType(rig.type)}
          </svg>
          <span>${rig.name}</span>
        `;
      } else {
        const defaults = {
          preamp: { svg: '<circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/>', label: 'Preamp' },
          amp: { svg: '<rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="12" cy="12" r="5"/>', label: 'Amp' },
          cab: { svg: '<rect x="2" y="4" width="20" height="16" rx="1"/><line x1="2" y1="8" x2="22" y2="8"/>', label: 'Cab' },
          fx: { svg: '<polygon points="12,2 22,8.5 22,15.5 12,22 2,15.5 2,8.5"/>', label: 'FX' }
        };
        const d = defaults[slotType] || defaults.amp;
        slot.querySelector('.slot-inner').innerHTML = `<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#555" stroke-width="1.5">${d.svg}</svg><span>${d.label}</span>`;
      }
    });

    animateMeter();
  }

  function renderKnobs(rig) {
    const container = document.getElementById('plate-knobs');
    container.innerHTML = '';
    currentKnobs = [];

    rig.knobs.forEach((knob, i) => {
      const wrap = document.createElement('div');
      wrap.className = 'knob-container';

      const knobEl = document.createElement('div');
      knobEl.className = 'knob';
      knobEl.dataset.index = i;
      knobEl.dataset.value = knob.value;
      knobEl.style.transform = `rotate(${knobToAngle(knob.value, knob.min, knob.max)}deg)`;

      const label = document.createElement('div');
      label.className = 'knob-label';
      label.textContent = knob.label;

      const value = document.createElement('div');
      value.className = 'knob-value';
      value.textContent = knob.value;

      wrap.appendChild(knobEl);
      wrap.appendChild(label);
      wrap.appendChild(value);
      container.appendChild(wrap);

      currentKnobs.push({ el: knobEl, valueEl: value, data: knob });

      knobEl.addEventListener('mousedown', (e) => startKnobDrag(e, i));
      knobEl.addEventListener('touchstart', (e) => startKnobDrag(e, i), { passive: false });
    });
  }

  function knobToAngle(value, min, max) {
    const ratio = (value - min) / (max - min);
    return -135 + ratio * 270;
  }

  function angleToValue(angle, min, max) {
    let norm = ((angle + 135) % 360 + 360) % 360;
    if (norm > 270) norm = 270;
    if (norm < 0) norm = 0;
    const ratio = norm / 270;
    return Math.round(min + ratio * (max - min));
  }

  function startKnobDrag(e, index) {
    e.preventDefault();
    knobDrag = { index, startY: e.clientY || e.touches[0].clientY, startValue: currentKnobs[index].data.value };
    document.addEventListener('mousemove', onKnobDrag);
    document.addEventListener('mouseup', endKnobDrag);
    document.addEventListener('touchmove', onKnobDrag, { passive: false });
    document.addEventListener('touchend', endKnobDrag);
  }

  function onKnobDrag(e) {
    if (!knobDrag) return;
    e.preventDefault();
    const y = e.clientY || e.touches[0].clientY;
    const delta = knobDrag.startY - y;
    const newValue = Math.max(0, Math.min(100, knobDrag.startValue + delta * 0.8));
    const knob = currentKnobs[knobDrag.index];
    knob.el.style.transform = `rotate(${knobToAngle(newValue, knob.data.min, knob.data.max)}deg)`;
    knob.el.dataset.value = Math.round(newValue);
    knob.valueEl.textContent = Math.round(newValue);
    knob.data.value = Math.round(newValue);
  }

  function endKnobDrag() {
    knobDrag = null;
    document.removeEventListener('mousemove', onKnobDrag);
    document.removeEventListener('mouseup', endKnobDrag);
    document.removeEventListener('touchmove', onKnobDrag);
    document.removeEventListener('touchend', endKnobDrag);
  }

  function renderSwitches(rig) {
    const container = document.getElementById('plate-switches');
    container.innerHTML = '';

    rig.switches.forEach((sw, i) => {
      const wrap = document.createElement('div');
      wrap.className = 'toggle-switch';

      const track = document.createElement('div');
      track.className = 'switch-track' + (sw.active ? ' active' : '');
      track.dataset.index = i;

      const thumb = document.createElement('div');
      thumb.className = 'switch-thumb';
      track.appendChild(thumb);

      const label = document.createElement('div');
      label.className = 'switch-label';
      label.textContent = sw.label;

      track.addEventListener('click', () => {
        sw.active = !sw.active;
        track.classList.toggle('active', sw.active);
      });

      wrap.appendChild(track);
      wrap.appendChild(label);
      container.appendChild(wrap);
    });
  }

  function animateMeter() {
    const fill = document.getElementById('meter-level');
    let level = 0;
    const target = activeRig ? (activeRig.knobs[0]?.value || 50) : 50;
    const interval = setInterval(() => {
      level += (target - level) * 0.08;
      fill.style.width = Math.min(100, Math.max(0, level)) + '%';
      if (Math.abs(level - target) < 0.5) {
        fill.style.width = target + '%';
        clearInterval(interval);
      }
    }, 30);
  }

  document.addEventListener('DOMContentLoaded', init);
})();
