const AMP_DATA = {
  amps: [
    {
      id: 'tweed-deux',
      name: 'Tweed Deluxe',
      type: 'Amp',
      category: 'classic',
      desc: '60s spring reverb tube combo',
      color: '#C8A45C',
      knobs: [
        { label: 'Volume', value: 65, min: 0, max: 100 },
        { label: 'Treble', value: 55, min: 0, max: 100 },
        { label: 'Middle', value: 50, min: 0, max: 100 },
        { label: 'Bass', value: 60, min: 0, max: 100 },
        { label: 'Reverb', value: 40, min: 0, max: 100 },
        { label: 'Intensity', value: 35, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Channel', active: true, options: ['Normal', 'Vibrato'] },
        { label: 'Reverb', active: true, options: ['Off', 'On'] }
      ]
    },
    {
      id: 'blackface-pro',
      name: 'Blackface Pro',
      type: 'Amp',
      category: 'classic',
      desc: '85W tube head with tremolo',
      color: '#D4C8A0',
      knobs: [
        { label: 'Volume', value: 70, min: 0, max: 100 },
        { label: 'Treble', value: 60, min: 0, max: 100 },
        { label: 'Middle', value: 45, min: 0, max: 100 },
        { label: 'Bass', value: 55, min: 0, max: 100 },
        { label: 'Presence', value: 50, min: 0, max: 100 },
        { label: 'Tremolo', value: 30, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Channel', active: true, options: ['Channel 1', 'Channel 2'] },
        { label: 'Tremolo', active: true, options: ['Off', 'On'] }
      ]
    },
    {
      id: 'silverface',
      name: 'Silverface Twin',
      type: 'Amp',
      category: 'classic',
      desc: '100W dual channel combo',
      color: '#B8B8C0',
      knobs: [
        { label: 'Volume A', value: 60, min: 0, max: 100 },
        { label: 'Volume B', value: 50, min: 0, max: 100 },
        { label: 'Treble', value: 55, min: 0, max: 100 },
        { label: 'Middle', value: 50, min: 0, max: 100 },
        { label: 'Bass', value: 50, min: 0, max: 100 },
        { label: 'Reverb', value: 45, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Channel', active: true, options: ['Normal', 'Vibrato'] },
        { label: 'Reverb', active: true, options: ['Off', 'On'] },
        { label: 'Spring', active: false, options: ['Twin Reverb', 'Super Reverb'] }
      ]
    },
    {
      id: 'blues-dev',
      name: 'Blues Dev',
      type: 'Amp',
      category: 'modern',
      desc: '20W all-tube boutique',
      color: '#A08050',
      knobs: [
        { label: 'Gain', value: 75, min: 0, max: 100 },
        { label: 'Volume', value: 55, min: 0, max: 100 },
        { label: 'Treble', value: 65, min: 0, max: 100 },
        { label: 'Middle', value: 60, min: 0, max: 100 },
        { label: 'Bass', value: 50, min: 0, max: 100 },
        { label: 'Presence', value: 45, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Mode', active: true, options: ['Clean', 'Crunch'] },
        { label: 'Power', active: true, options: ['20W', '5W'] }
      ]
    },
    {
      id: 'super-sonic',
      name: 'Super Sonic',
      type: 'Amp',
      category: 'modern',
      desc: '60W high-gain combo',
      color: '#8A8A90',
      knobs: [
        { label: 'Gain', value: 80, min: 0, max: 100 },
        { label: 'Volume', value: 50, min: 0, max: 100 },
        { label: 'Bass', value: 55, min: 0, max: 100 },
        { label: 'Mid', value: 40, min: 0, max: 100 },
        { label: 'Treble', value: 60, min: 0, max: 100 },
        { label: 'Presence', value: 50, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Channel', active: true, options: ['Clean', 'Crunch', 'Lead'] },
        { label: 'Boost', active: false, options: ['Off', 'On'] }
      ]
    },
    {
      id: 'princeton-clean',
      name: 'Princeton Clean',
      type: 'Amp',
      category: 'classic',
      desc: '15W blackface boutique reissue',
      color: '#E0D8C0',
      knobs: [
        { label: 'Volume', value: 50, min: 0, max: 100 },
        { label: 'Treble', value: 45, min: 0, max: 100 },
        { label: 'Bass', value: 55, min: 0, max: 100 },
        { label: 'Reverb', value: 30, min: 0, max: 100 },
        { label: 'Depth', value: 40, min: 0, max: 100 },
        { label: 'Speed', value: 35, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Tremolo', active: true, options: ['Off', 'On'] },
        { label: 'Reverb', active: true, options: ['Off', 'On'] }
      ]
    },
    {
      id: 'vibro-king',
      name: 'Vibro-King',
      type: 'Amp',
      category: 'classic',
      desc: '100W slab with vibrato',
      color: '#C0B8A0',
      knobs: [
        { label: 'Volume', value: 75, min: 0, max: 100 },
        { label: 'Treble', value: 60, min: 0, max: 100 },
        { label: 'Bass', value: 65, min: 0, max: 100 },
        { label: 'Reverb', value: 50, min: 0, max: 100 },
        { label: 'Intensity', value: 40, min: 0, max: 100 },
        { label: 'Vibrato', value: 45, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Channel', active: true, options: ['Normal', 'Vibrato'] },
        { label: 'Reverb', active: true, options: ['Off', 'On'] }
      ]
    },
    {
      id: 'tweed-preamp',
      name: 'Tweed Preamp',
      type: 'Pre-Amp',
      category: 'preamp',
      desc: 'Classic tweed drive stage',
      color: '#B89830',
      knobs: [
        { label: 'Drive', value: 60, min: 0, max: 100 },
        { label: 'Volume', value: 55, min: 0, max: 100 },
        { label: 'Tone', value: 50, min: 0, max: 100 },
        { label: 'Bias', value: 45, min: 0, max: 100 },
        { label: 'Output', value: 70, min: 0, max: 100 },
        { label: 'Noise', value: 10, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Mode', active: true, options: ['Clean', 'Edge'] },
        { label: 'Color', active: false, options: ['Bright', 'Normal'] }
      ]
    },
    {
      id: 'tube-screamer',
      name: 'Tube Screamer',
      type: 'Pre-Amp',
      category: 'preamp',
      desc: 'Overdrive preamp stage',
      color: '#D4A030',
      knobs: [
        { label: 'Drive', value: 40, min: 0, max: 100 },
        { label: 'Tone', value: 55, min: 0, max: 100 },
        { label: 'Level', value: 65, min: 0, max: 100 },
        { label: 'Attack', value: 50, min: 0, max: 100 },
        { label: 'Sustain', value: 45, min: 0, max: 100 },
        { label: 'Color', value: 60, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Boost', active: false, options: ['Off', 'On'] },
        { label: 'Mode', active: true, options: ['Soft', 'Hard'] }
      ]
    },
    {
      id: 'marsh-preamp',
      name: 'Stack Preamp',
      type: 'Pre-Amp',
      category: 'preamp',
      desc: 'High gain stacking preamp',
      color: '#907020',
      knobs: [
        { label: 'Pre Gain', value: 70, min: 0, max: 100 },
        { label: 'Post Gain', value: 55, min: 0, max: 100 },
        { label: 'Bass', value: 60, min: 0, max: 100 },
        { label: 'Mid', value: 50, min: 0, max: 100 },
        { label: 'Treble', value: 65, min: 0, max: 100 },
        { label: 'Master', value: 75, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Channel', active: true, options: ['Clean', 'Gain'] },
        { label: 'Boost', active: true, options: ['Off', 'On'] }
      ]
    },
    {
      id: 'brit-cab-12',
      name: 'Brit 1x12',
      type: 'Cab',
      category: 'cab',
      desc: 'British 12" cabinet IR',
      color: '#808088',
      knobs: [
        { label: 'Level', value: 70, min: 0, max: 100 },
        { label: 'Position', value: 50, min: 0, max: 100 },
        { label: 'Angle', value: 45, min: 0, max: 100 },
        { label: 'Room', value: 30, min: 0, max: 100 },
        { label: 'Distance', value: 55, min: 0, max: 100 },
        { label: 'Width', value: 60, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Mic Type', active: true, options: ['SM57', 'R121', 'U47'] },
        { label: 'HPF', active: false, options: ['Off', '80Hz'] }
      ]
    },
    {
      id: 'american-2x12',
      name: 'Amer 2x12',
      type: 'Cab',
      category: 'cab',
      desc: 'American 12" stereo cab',
      color: '#909098',
      knobs: [
        { label: 'Level L', value: 65, min: 0, max: 100 },
        { label: 'Level R', value: 60, min: 0, max: 100 },
        { label: 'Pan', value: 50, min: 0, max: 100 },
        { label: 'Room', value: 35, min: 0, max: 100 },
        { label: 'Distance', value: 50, min: 0, max: 100 },
        { label: 'Width', value: 70, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Mic Type', active: true, options: ['SM57', 'MD421'] },
        { label: 'Stereo', active: true, options: ['Mono', 'Stereo'] }
      ]
    },
    {
      id: 'vintage-chorus',
      name: 'Vibe Chorus',
      type: 'Effects',
      category: 'fx',
      desc: 'Vintage spring reverb chorus',
      color: '#6080C0',
      knobs: [
        { label: 'Rate', value: 40, min: 0, max: 100 },
        { label: 'Depth', value: 55, min: 0, max: 100 },
        { label: 'Tone', value: 50, min: 0, max: 100 },
        { label: 'Mix', value: 45, min: 0, max: 100 },
        { label: 'Reverb', value: 35, min: 0, max: 100 },
        { label: 'Hall', value: 30, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Mode', active: true, options: ['Chorus', 'Vibe'] },
        { label: 'Reverb', active: true, options: ['Off', 'On'] }
      ]
    },
    {
      id: 'analog-delay',
      name: 'Echo Delay',
      type: 'Effects',
      category: 'fx',
      desc: 'Analog tape echo',
      color: '#7090A0',
      knobs: [
        { label: 'Time', value: 35, min: 0, max: 100 },
        { label: 'Feedback', value: 40, min: 0, max: 100 },
        { label: 'Tone', value: 55, min: 0, max: 100 },
        { label: 'Level', value: 50, min: 0, max: 100 },
        { label: 'Mod', value: 20, min: 0, max: 100 },
        { label: 'Density', value: 45, min: 0, max: 100 }
      ],
      switches: [
        { label: 'Mode', active: true, options: ['Tape', 'Digital'] },
        { label: 'Mod', active: false, options: ['Off', 'On'] }
      ]
    }
  ]
};
