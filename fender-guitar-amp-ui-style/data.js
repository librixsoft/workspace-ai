const AMP_DATA = [
  {
    id: 'deluxe-reverb',
    name: 'Deluxe Reverb',
    brand: 'Fender',
    type: 'Tube Combo',
    watts: 22,
    desc: 'Classic blackface combo with lush spring reverb and crisp cleans.',
    color: '#c0c0c0',
    knobs: [
      { id: 'volume', label: 'Volume', min: 0, max: 10, default: 6 },
      { id: 'treble', label: 'Treble', min: 0, max: 10, default: 5 },
      { id: 'mid', label: 'Mid', min: 0, max: 10, default: 5 },
      { id: 'bass', label: 'Bass', min: 0, max: 10, default: 5 },
      { id: 'reverb', label: 'Reverb', min: 0, max: 10, default: 4 },
      { id: 'presence', label: 'Presence', min: 0, max: 10, default: 5 },
    ]
  },
  {
    id: 'twin-reverb',
    name: 'Twin Reverb',
    brand: 'Fender',
    type: 'Tube Combo',
    watts: 85,
    desc: 'The definitive clean amp. Two channels, two speakers, infinite headroom.',
    color: '#d4a017',
    knobs: [
      { id: 'volume', label: 'Volume', min: 0, max: 10, default: 7 },
      { id: 'treble', label: 'Treble', min: 0, max: 10, default: 6 },
      { id: 'mid', label: 'Mid', min: 0, max: 10, default: 5 },
      { id: 'bass', label: 'Bass', min: 0, max: 10, default: 4 },
      { id: 'reverb', label: 'Reverb', min: 0, max: 10, default: 5 },
      { id: 'brilliance', label: 'Brilliance', min: 0, max: 10, default: 3 },
    ]
  },
  {
    id: 'bassman',
    name: 'Bassman',
    brand: 'Fender',
    type: 'Tube Head',
    watts: 50,
    desc: 'The amp that birthed the Marshall sound. Punchy low end, singing mids.',
    color: '#b87333',
    knobs: [
      { id: 'volume', label: 'Volume', min: 0, max: 10, default: 5 },
      { id: 'treble', label: 'Treble', min: 0, max: 10, default: 4 },
      { id: 'mid', label: 'Mid', min: 0, max: 10, default: 6 },
      { id: 'bass', label: 'Bass', min: 0, max: 10, default: 7 },
      { id: 'reverb', label: 'Reverb', min: 0, max: 10, default: 3 },
    ]
  },
  {
    id: 'princeton-reverb',
    name: 'Princeton Reverb',
    brand: 'Fender',
    type: 'Tube Combo',
    watts: 12,
    desc: 'Crumbling clean to sweet breakup. The player\'s secret weapon.',
    color: '#e8e8e8',
    knobs: [
      { id: 'volume', label: 'Volume', min: 0, max: 10, default: 4 },
      { id: 'treble', label: 'Treble', min: 0, max: 10, default: 5 },
      { id: 'bass', label: 'Bass', min: 0, max: 10, default: 5 },
      { id: 'reverb', label: 'Reverb', min: 0, max: 10, default: 6 },
      { id: 'presence', label: 'Presence', min: 0, max: 10, default: 4 },
    ]
  },
  {
    id: 'super-spring',
    name: 'Super Reverb',
    brand: 'Fender',
    type: 'Tube Combo',
    watts: 45,
    desc: 'Sixty watts of pure Fender sparkle through eight speakers.',
    color: '#c0c0c0',
    knobs: [
      { id: 'volume', label: 'Volume', min: 0, max: 10, default: 6 },
      { id: 'treble', label: 'Treble', min: 0, max: 10, default: 6 },
      { id: 'mid', label: 'Mid', min: 0, max: 10, default: 5 },
      { id: 'bass', label: 'Bass', min: 0, max: 10, default: 5 },
      { id: 'reverb', label: 'Reverb', min: 0, max: 10, default: 5 },
      { id: 'vibrato', label: 'Vibrato', min: 0, max: 10, default: 3 },
    ]
  },
  {
    id: 'tweed-5f',
    name: 'Tweed 5F',
    brand: 'Fender',
    type: 'Vintage P.A.C.',
    watts: 18,
    desc: 'All-tweed warmth. Early blackface tones with natural compression.',
    color: '#b87333',
    knobs: [
      { id: 'volume', label: 'Volume', min: 0, max: 10, default: 5 },
      { id: 'treble', label: 'Treble', min: 0, max: 10, default: 4 },
      { id: 'bass', label: 'Bass', min: 0, max: 10, default: 6 },
      { id: 'reverb', label: 'Reverb', min: 0, max: 10, default: 2 },
    ]
  },
];

const PREAMP_DATA = [
  { id: 'preamplifier', name: 'Preamp', brand: 'Fender', type: 'Clean Boost', desc: 'Transparent gain staging.', color: '#d4a017' },
  { id: 'overdrive', name: 'Brown Comp', brand: 'Fender', type: 'Compression OD', desc: 'Vintage compression overdrive.', color: '#b87333' },
  { id: 'distortion', name: 'Metal Driver', brand: 'Fender', type: 'High Gain', desc: 'Aggressive distortion stage.', color: '#8a8e92' },
  { id: 'boost', name: 'Turbo Boost', brand: 'Fender', type: 'Clean Boost', desc: 'Push the preamp harder.', color: '#c0c0c0' },
  { id: 'preamp-tube', name: 'Tube Screamer', brand: 'Fender', type: 'Tubescreamer', desc: 'Classic mid-hump boost.', color: '#d4a017' },
  { id: 'preamp-fuzz', name: 'Fuzz Face', brand: 'Fender', type: 'Vintage Fuzz', desc: 'Smooth velvet fuzz tone.', color: '#2a2e32' },
  { id: 'preamp-wah', name: 'Wah Pre', brand: 'Fender', type: 'Envelope Filter', desc: 'Auto-wah envelope follower.', color: '#b87333' },
  { id: 'preamp-compressor', name: 'Opto Comp', brand: 'Fender', type: 'Optical Comp', desc: 'Pump and sustain control.', color: '#c0c0c0' },
];

const CABINET_DATA = [
  { id: 'cab-4x12', name: '4x12 British', brand: 'Fender', type: 'Closed Back', desc: 'Celestion Greenbacks. Big and aggressive.', speakers: '4x12"', color: '#8a8e92' },
  { id: 'cab-2x12', name: '2x12 American', brand: 'Fender', type: 'Open Back', desc: 'Jensen P12Q. Warm and articulate.', speakers: '2x12"', color: '#b87333' },
  { id: 'cab-1x12', name: '1x12 Deluxe', brand: 'Fender', type: 'Open Back', desc: '12" Jensen for the classic combo tone.', speakers: '1x12"', color: '#c0c0c0' },
  { id: 'cab-4x12-v30', name: '4x12 V30', brand: 'Fender', type: 'Closed Back', desc: 'Celestion Vintage 30. Tight low end.', speakers: '4x12"', color: '#d4a017' },
  { id: 'cab-2x12-6v6', name: '2x12 6V6', brand: 'Fender', type: 'Open Back', desc: 'Matched 6V6 tubes. Smooth breakup.', speakers: '2x12"', color: '#e8e8e8' },
  { id: 'cab-1x10', name: '1x10 Bass', brand: 'Fender', type: 'Closed Back', desc: '10" bass driver. Punch and definition.', speakers: '1x10"', color: '#6a6e72' },
];

const EFFECTS_DATA = [
  { id: 'fx-reverb', name: 'Reverb', icon: '◉', default: false },
  { id: 'fx-delay', name: 'Delay', icon: '◷', default: false },
  { id: 'fx-compressor', name: 'Compressor', icon: '◐', default: false },
  { id: 'fx-chorus', name: 'Chorus', icon: '◎', default: false },
  { id: 'fx-flanger', name: 'Flanger', icon: '◑', default: false },
  { id: 'fx-phaser', name: 'Phaser', icon: '◆', default: false },
  { id: 'fx-tremolo', name: 'Tremolo', icon: '◈', default: false },
  { id: 'fx-wah', name: 'Wah', icon: '▲', default: false },
];
