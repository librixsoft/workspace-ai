/* =============================================
   Fedora Core Linux — Retro Desktop (2003)
   ============================================= */

// ============================
// Boot Sequence
// ============================
const bootScreen = document.getElementById('boot-screen');
const bootText = document.getElementById('boot-text');
const bootCount = document.getElementById('boot-count');

function runBootSequence() {
    const steps = [
        { text: '0', delay: 400 },
        { text: '256', delay: 800 },
        { text: '512', delay: 600 },
        { text: '768', delay: 500 },
        { text: '1024', delay: 300 },
    ];

    let i = 0;
    function next() {
        if (i < steps.length) {
            bootCount.textContent = steps[i].text;
            i++;
            setTimeout(next, steps[i - 1].delay);
        } else {
            // Boot complete — show login after a pause
            setTimeout(() => {
                bootScreen.classList.add('hidden');
                document.getElementById('login-screen').classList.remove('hidden');
                document.getElementById('login-pass').focus();
            }, 800);
        }
    }

    next();
}

// Start boot on load
runBootSequence();

// ============================
// Login Screen
// ============================
function doLogin() {
    const user = document.getElementById('login-user').value;
    const pass = document.getElementById('login-pass').value;

    if (user === 'root' && pass === '') {
        // Success!
        document.getElementById('login-screen').classList.add('hidden');
        bootScreen.classList.add('hidden');
        enterDesktop();
    } else if (pass.length > 0) {
        alert('Login incorrect.\n\nFedora Core 1 — Access denied.');
        document.getElementById('login-pass').value = '';
        document.getElementById('login-pass').focus();
    }
}

// Enter desktop with some retro flair
function enterDesktop() {
    const desktop = document.getElementById('desktop');
    const taskbar = document.getElementById('taskbar');

    // Show desktop & taskbar
    desktop.classList.remove('hidden');
    taskbar.classList.remove('hidden');

    // Open welcome window by default
    setTimeout(() => openWindow('welcome'), 300);

    // Populate system info
    populateSystemInfo();
}

// ============================
// System Info (My Computer)
// ============================
function populateSystemInfo() {
    const el = document.getElementById('system-info');
    if (!el) return;

    const now = new Date();
    const uptimeSeconds = Math.floor((now - sessionStart) / 1000);
    const mins = String(Math.floor(uptimeSeconds / 60)).padStart(2, '0');
    const secs = String(uptimeSeconds % 60).padStart(2, '0');

    el.textContent = `Linux fedora 2.4.28-1.2115.nptelsg #1 SMP PREEMPT
GNU/Linux

 System uptime: ${mins}:${secs}
 User logged in as: root@fedora
 Terminal: /dev/pts/0
 Hostname: fedora
 Domain: (none)
 Timezone: EST (${now.getTimezoneOffset() / -60} UTC)
 Kernel command line: ro root=LABEL=/ hda=ide-scsi
 PCI Devices: 7 total
 USB Devices: 2 found

 Filesystems mounted:
   /dev/hda1    /     ext3      rw,errors=remount-ro
   /dev/hda2    swap  swap      defaults
   /dev/hdc1    /cdrom iso9660   ro,noauto
   none         /proc proc       defaults
   none         /dev tmpfs      defaults`;
}

const sessionStart = Date.now();

// ============================
// Window Management
// ============================
let windowZIndex = 10;
const openWindows = {};

function openWindow(name) {
    const winEl = document.getElementById('win-' + name);
    if (!winEl || !winEl.classList.contains('hidden') === false) {
        // Already open — bring to front
        if (openWindows[name]) {
            bringToFront(name);
            return;
        }
    }

    winEl.classList.remove('hidden');
    openWindows[name] = true;
    bringToFront(name);

    // Add taskbar item
    const title = winEl.getAttribute('data-title') || name;
    addTaskbarItem(name, title);

    // Focus terminal if applicable
    if (name === 'terminal') {
        setTimeout(() => document.getElementById('terminal-input').focus(), 100);
    }
}

function closeWindow(name) {
    const winEl = document.getElementById('win-' + name);
    winEl.classList.add('hidden');
    delete openWindows[name];

    // Remove taskbar item
    removeTaskbarItem(name);
}

function minimizeWindow(name) {
    const winEl = document.getElementById('win-' + name);
    winEl.classList.add('hidden');
}

function maximizeWindow(name) {
    const winEl = document.getElementById('win-' + name);
    if (winEl.dataset.maximized === 'true') {
        // Restore
        winEl.style.top = winEl.dataset.prevTop;
        winEl.style.left = winEl.dataset.prevLeft;
        winEl.style.width = winEl.dataset.prevWidth || '85vw';
        winEl.style.height = winEl.dataset.prevHeight || '75vh';
        winEl.dataset.maximized = 'false';
    } else {
        // Maximize
        winEl.dataset.prevTop = winEl.style.top;
        winEl.dataset.prevLeft = winEl.style.left;
        winEl.dataset.prevWidth = winEl.style.width;
        winEl.dataset.prevHeight = winEl.style.height;

        winEl.style.top = '0';
        winEl.style.left = '0';
        winEl.style.width = '100%';
        winEl.style.height = 'calc(100% - 38px)';
        winEl.dataset.maximized = 'true';
    }
}

function bringToFront(name) {
    const winEl = document.getElementById('win-' + name);
    if (winEl) {
        windowZIndex++;
        winEl.style.zIndex = windowZIndex;
    }

    // Update taskbar active state
    document.querySelectorAll('.taskbar-item').forEach(item => {
        item.classList.remove('active');
    });
    const ti = document.getElementById('tb-' + name);
    if (ti) ti.classList.add('active');
}

// ============================
// Taskbar Items
// ============================
function addTaskbarItem(name, title) {
    const container = document.getElementById('taskbar-items');
    const item = document.createElement('div');
    item.className = 'taskbar-item active';
    item.id = 'tb-' + name;
    item.textContent = title.split(' - ')[0]; // Shorten
    item.onclick = () => {
        bringToFront(name);
        openWindow(name);
    };
    container.appendChild(item);
}

function removeTaskbarItem(name) {
    const item = document.getElementById('tb-' + name);
    if (item) item.remove();

    // If closing the active window, activate next
    if (!openWindows[name]) {
        const remaining = Object.keys(openWindows);
        if (remaining.length > 0) {
            bringToFront(remaining[remaining.length - 1]);
        }
    }
}

// ============================
// Window Dragging
// ============================
let dragWindow = null;
let dragOffsetX = 0;
let dragOffsetY = 0;

function startDrag(event, windowId) {
    const winEl = document.getElementById(windowId);
    if (winEl.dataset.maximized === 'true') return; // Can't drag maximized

    dragWindow = winEl;
    const rect = winEl.getBoundingClientRect();
    dragOffsetX = event.clientX - rect.left;
    dragOffsetY = event.clientY - rect.top;
    bringToFront(windowId.replace('win-', ''));

    event.preventDefault();
}

document.addEventListener('mousemove', (e) => {
    if (!dragWindow) return;
    const x = e.clientX - dragOffsetX;
    const y = e.clientY - dragOffsetY;
    dragWindow.style.left = x + 'px';
    dragWindow.style.top = y + 'px';
    dragWindow.style.transform = 'none'; // Remove initial centering transform
});

document.addEventListener('mouseup', () => {
    if (dragWindow) {
        dragWindow.dataset.prevTop = dragWindow.style.top;
        dragWindow.dataset.prevLeft = dragWindow.style.left;
    }
    dragWindow = null;
});

// ============================
// Start Menu
// ============================
function toggleStartMenu() {
    const menu = document.getElementById('start-menu');
    const btn = document.getElementById('start-button');

    if (menu.classList.contains('hidden')) {
        menu.classList.remove('hidden');
        btn.classList.add('active');
    } else {
        menu.classList.add('hidden');
        btn.classList.remove('active');
    }
}

// Close start menu on outside click
document.addEventListener('click', (e) => {
    const menu = document.getElementById('start-menu');
    const btn = document.getElementById('start-button');
    if (!menu.contains(e.target) && !btn.contains(e.target)) {
        menu.classList.add('hidden');
        btn.classList.remove('active');
    }

    // Close context menu
    document.getElementById('context-menu').classList.add('hidden');
});

// ============================
// Clock
// ============================
function updateClock() {
    const now = new Date();
    let hours = now.getHours();
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12 || 12;
    document.getElementById('clock').textContent = `${hours}:${minutes} ${ampm}`;
}

setInterval(updateClock, 1000);
updateClock();

// ============================
// Terminal Emulator
// ============================
const terminalInput = document.getElementById('terminal-input');
const terminalOutput = document.getElementById('terminal-output');

const fileSystem = {
    '/': ['bin', 'boot', 'dev', 'etc', 'home', 'lib', 'proc', 'root', 'sbin', 'tmp', 'usr', 'var'],
    '/home': ['user'],
    '/home/user': ['Desktop', 'Documents', 'Downloads', 'Music', 'Pictures', 'Public', 'Templates', '.bashrc', '.vimrc'],
    '/etc': ['passwd', 'hosts', 'fstab', 'shadow', 'group', 'resolv.conf', 'hostname', 'issue', 'motd'],
    '/root': ['.bash_history', '.bash_profile', '.ssh'],
};

const commands = {
    help: () => `Available commands:
  help          - Show this help message
  ls            - List directory contents
  cd [dir]      - Change directory
  pwd           - Print working directory
  cat [file]    - Display file contents
  whoami        - Current username
  hostname      - System hostname
  uname [-a]    - System information
  date          - Current date/time
  uptime        - System uptime
  free          - Memory usage (simulated)
  df            - Disk space (simulated)
  ps            - Process list (simulated)
  neofetch      - Fancy system info display
  fortune       - Random linux fortune
  cowsay [msg]  - Cow says...
  clear         - Clear terminal screen
  echo [text]   - Print text to output
  lsmod         - Loaded kernel modules
  ifconfig      - Network interfaces
  history       - Command history

Try "neofetch" for the full retro experience!`,

    ls: (args) => {
        const dir = currentDir || '/home/user';
        const files = fileSystem[dir] || [];
        let output = '';
        if (files.length === 0) return '(empty directory)';

        // Filter hidden files unless -a flag
        const showHidden = args.includes('-a') || args.includes('-la');
        const visibleFiles = showHidden ? files : files.filter(f => !f.startsWith('.'));

        visibleFiles.forEach(f => {
            const isDir = ['bin', 'boot', 'dev', 'etc', 'home', 'lib', 'proc', 'root', 'sbin',
                'tmp', 'usr', 'var', 'Desktop', 'Documents', 'Downloads', 'Music',
                'Pictures', 'Public', 'Templates'].includes(f);
            output += isDir ? `\x1b[34m${f}/\x1b[0m  ` : `${f}  `;
        });
        return output.trim();
    },

    cd: (args) => {
        if (!args[0] || args[0] === '~') {
            currentDir = '/home/user';
            updateTerminalPrompt();
            return '';
        }
        if (args[0] === '..') {
            const parts = currentDir.split('/');
            if (parts.length > 1) {
                parts.pop();
                currentDir = parts.join('/') || '/';
            }
            updateTerminalPrompt();
            return '';
        }

        // Try to change directory
        const newPath = currentDir === '/' ? `/${args[0]}` : `${currentDir}/${args[0]}`;
        if (fileSystem[newPath]) {
            currentDir = newPath;
            updateTerminalPrompt();
            return '';
        }
        return `bash: cd: ${args[0]}: No such file or directory`;
    },

    pwd: () => currentDir || '/home/user',

    cat: (args) => {
        if (!args[0]) return 'cat: missing operand';
        const filePath = args[0];
        if (filePath === '/etc/issue') return 'Fedora Core release 1 (Yarrow)\nKernel \\r on an \\m\n';
        if (filePath === '/etc/motd') return '\nWelcome to Fedora Core 1 "Yarrow"!\nRemember: With great power comes great responsibility.\n\n';
        if (filePath === '/etc/hostname') return 'fedora\n';
        if (filePath === '.bashrc' || filePath.startsWith('./')) {
            return '# .bashrc — Bash configuration file for Fedora Core 1\nexport PS1="[user@fedora \\w]\\$ "\nalias ll="ls -la"\nalias ls="ls --color=auto"\n';
        }
        if (filePath === '/etc/passwd') {
            return 'root:x:0:0:root:/root:/bin/bash\nnobody:x:65534:65534:nobody:/nonexistent:/usr/bin/nologin\nuser:x:1000:1000:User,,,:/home/user:/bin/bash\n';
        }
        if (filePath === '/etc/hosts') {
            return '127.0.0.1\tlocalhost.localdomain localhost\n::1\t\tlocalhost6.localdomain6 localhost\n';
        }
        return `cat: ${args[0]}: No such file or directory`;
    },

    whoami: () => 'root',
    hostname: () => 'fedora',

    uname: (args) => {
        if (args.includes('-a')) {
            return 'Linux fedora 2.4.28-1.2115.nptelsg #1 SMP PREEMPT Thu May 5 03:42:03 EDT 2004 i686 Athlon tm2 Intel(R) Xeon(tm) CPU 2.40GHz GenuineIntel GNU/Linux';
        }
        return 'Linux';
    },

    date: () => new Date().toString(),
    uptime: () => {
        const s = Math.floor((Date.now() - sessionStart) / 1000);
        const m = String(Math.floor(s / 60)).padStart(2, '0');
        const sec = String(s % 60).padStart(2, '0');
        return ` ${new Date().toLocaleTimeString()} up ${m}:${sec}, 1 user, load average: 0.47, 0.38, 0.15`;
    },

    free: () => {
        return `              total       used       free     shared    buffers     cached
Mem:         1024696     342156     682540          0      45200     178628
-/+ buffers/cache:     118328     906368
Swap:        524284           0    524284`;
    },

    df: () => {
        return `Filesystem     1K-blocks      Used Available Use% Mounted on
/dev/hda1       118369476  23456789  89234567  21% /
none               512348         0    512348   0% /dev/shm`;
    },

    ps: () => {
        return '  PID TTY          TIME CMD\n' +
            '    1 pts/0    00:00:01 init\n' +
            '    59 ?        00:00:00 kthreadd\n' +
            '   234 tty1     00:00:00 Xfree86\n' +
            '   312 pts/0    00:00:00 bash\n' +
            `   ${Math.floor(Math.random() * 9000 + 1000)} pts/0    00:00:00 ps`;
    },

    ifconfig: () => {
        return 'eth0      Link encap:Ethernet  HWaddr 00:0C:29:A3:B4:C5\n' +
            '          inet addr:192.168.1.100  Bcast:192.168.1.255  Mask:255.255.255.0\n' +
            '          inet6 addr: fe80::20c:29ff:fea3:b4c5/64 Scope:Link\n' +
            '          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1\n' +
            '          RX packets:45678 errors:0 dropped:0 overruns:0 frame:0\n' +
            '          TX packets:23456 errors:0 dropped:0 overruns:0 carrier:0\n' +
            '          collisions:0 txqueuelen:1000\n';
    },

    lsmod: () => {
        return `Module                  Size  Used by    Tainted: P
ext3                   98564   1  Active
jbd                    52876   2  [ext3]
ext2                   75620   0  [permanent]
nls_utf8                3124   1  Active
nls_iso8859-1           2876   1  Active
usbcore               105628   2  [ohci-hcd ehci-hcd]
agpgart                 42680   0  Active
via_agp                 8364   1  Active`;
    },

    history: () => {
        const recent = terminalHistory.slice(-15);
        return recent.map((cmd, i) => `  ${terminalHistory.length - recent.length + i + 1}  ${cmd}`).join('\n');
    },

    clear: () => {
        terminalOutput.textContent = '';
        return '';
    },

    echo: (args) => args.join(' '),

    fortune: () => {
        const fortunes = [
            '"The only way to get rid of a temptation is to yield to it." — Oscar Wilde',
            '"In three words I can sum up everything I\'ve learned about life: it goes on." — Robert Frost',
            '"To err is human; to forgive, divine." — Alexander Pope',
            '"Linux: because rebooting is for upgrading, not fixing." — Anonymous',
            '"There are only 10 types of people in the world: those who understand binary and those who don\'t."',
            '"A computer once killed me. But accidentally." — Anonymous',
            '"The good thing about Unix is that you can lose control of your computer almost as easily as with any other system." — Linus Torvalds',
        ];
        return fortunes[Math.floor(Math.random() * fortunes.length)];
    },

    cowsay: (args) => {
        const msg = args.join(' ') || 'Moo!';
        const line = '-'.repeat(msg.length + 2);
        return ` ${line}\n| ${msg} |\n ${line}\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||`;
    },

    neofetch: () => {
        return `\x1b[36m          .--.          \x1b[0m user@fedora
\x1b[36m         |o_o |         \x1b[0m ──────────────
\x1b[36m         |:_/ |         \x1b[0m OS: Fedora Core 1 "Yarrow" i686
\x1b[36m        //   \\ \\        \x1b[0m Kernel: 2.4.28-1.2115.nptelsg
\x1b[36m       (|     | )       \x1b[0m Uptime: ${Math.floor((Date.now() - sessionStart) / 60000)} mins
\x1b[36m      /'\\_   _/\`\\       \x1b[0m Shell: bash 2.05b
\x1b[36m      \\___)=(___/       \x1b[0m DE: GNOME 2.4 (Beta)
                            WM: Metacity
                            Terminal: pts/0
                            CPU: Intel Xeon @ 2.4GHz
                            Memory: 342MB / 1024MB

\x1b[37m███\x1b[0m   \x1b[31m███\x1b[0m   \x1b[32m███\x1b[0m   \x1b[34m███\x1b[0m
\x1b[37m███\x1b[0m   \x1b[31m███\x1b[0m   \x1b[32m███\x1b[0m   \x1b[34m███\x1b[0m`;
    },

    // Default unknown command handler will be below
};

let currentDir = '/home/user';
const terminalHistory = [];
let historyIndex = -1;

function updateTerminalPrompt() {
    const promptEl = document.querySelector('.prompt');
    if (promptEl) {
        promptEl.textContent = `[user@fedora ${currentDir}]$ `;
    }
}

function executeCommand(input) {
    const trimmed = input.trim();
    if (!trimmed) return '';

    // Add to history
    terminalHistory.push(trimmed);
    historyIndex = -1;

    // Parse command and args
    const parts = trimmed.match(/(?:[^\s"]+|"[^"]*")+/g) || [];
    const cmd = parts.shift();
    if (!cmd) return '';

    const handler = commands[cmd];
    if (!handler) {
        return `bash: ${cmd}: command not found. Type "help" for available commands.`;
    }

    try {
        const result = handler(parts);
        return result || '';
    } catch (e) {
        return `Error executing "${cmd}": ${e.message}`;
    }
}

function printToTerminal(text) {
    if (!text) return;
    // Convert ANSI-like colors to spans
    const formatted = text.replace(/\x1b\[(\d+)m/g, (match, code) => {
        const colors = {
            '0': '#33ff33', '34': '#5588ff', '31': '#ff5555',
            '32': '#55ff55', '33': '#ffff55', '36': '#55ffff', '37': '#ffffff'
        };
        return `</span><span style="color:${colors[code] || '#33ff33'}">`;
    });

    const div = document.createElement('div');
    div.innerHTML = formatted;
    terminalOutput.appendChild(div);

    // Scroll to bottom
    const bodyEl = document.querySelector('#win-terminal .window-body');
    if (bodyEl) bodyEl.scrollTop = bodyEl.scrollHeight;
}

// Terminal input handler
terminalInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
        const command = terminalInput.value;
        // Print the entered command with prompt
        const cmdLine = document.createElement('div');
        cmdLine.innerHTML = `<span class="prompt">[user@fedora ${currentDir}]$ </span>${command}`;
        terminalOutput.appendChild(cmdLine);

        // Execute and print result
        const result = executeCommand(command);
        if (result) printToTerminal(result);

        terminalInput.value = '';

        // Scroll to bottom
        const bodyEl = document.querySelector('#win-terminal .window-body');
        if (bodyEl) bodyEl.scrollTop = bodyEl.scrollHeight;
    } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        if (terminalHistory.length > 0 && historyIndex < terminalHistory.length - 1) {
            historyIndex++;
            terminalInput.value = terminalHistory[terminalHistory.length - 1 - historyIndex];
        }
    } else if (e.key === 'ArrowDown') {
        e.preventDefault();
        if (historyIndex > 0) {
            historyIndex--;
            terminalInput.value = terminalHistory[terminalHistory.length - 1 - historyIndex];
        } else {
            historyIndex = -1;
            terminalInput.value = '';
        }
    } else if (e.key === 'Tab') {
        e.preventDefault();
        // Basic tab completion for known commands
        const partial = terminalInput.value.split(' ').pop().toLowerCase();
        const matches = Object.keys(commands).filter(c => c.startsWith(partial));
        if (matches.length === 1) {
            terminalInput.value += matches[0];
        }
    } else if (e.key === 'l' && e.ctrlKey) {
        e.preventDefault();
        printToTerminal('clear');
        executeCommand('clear');
    }
});

// ============================
// Context Menu
// ============================
document.getElementById('desktop').addEventListener('contextmenu', (e) => {
    if (e.target.closest('.window')) return; // Don't show on windows

    e.preventDefault();

    const ctx = document.getElementById('context-menu');
    ctx.innerHTML = `
        <div class="ctx-item" onclick="openWindow('terminal')">🖥️ Open Terminal</div>
        <div class="ctx-item" onclick="openWindow('about')">💻 System Properties</div>
        <hr>
        <div class="ctx-item" onclick="location.reload()">⟳ Refresh Desktop</div>
        <hr>
        <div class="ctx-item">🎨 Change Background...</div>
        <div class="ctx-item">⚙️ Display Properties</div>
    `;

    ctx.style.left = e.clientX + 'px';
    ctx.style.top = e.clientY + 'px';
    ctx.classList.remove('hidden');
});

// ============================
// Keyboard shortcuts
// ============================
document.addEventListener('keydown', (e) => {
    // Alt+F4 → close focused window
    if (e.altKey && e.key === 'F4') {
        e.preventDefault();
        const lastOpen = Object.keys(openWindows).pop();
        if (lastOpen) closeWindow(lastOpen);
    }

    // Escape → close start menu
    if (e.key === 'Escape') {
        document.getElementById('start-menu').classList.add('hidden');
        document.getElementById('context-menu').classList.add('hidden');
    }
});

// ============================
// Auto-focus terminal when opened via taskbar
// ============================
document.addEventListener('click', (e) => {
    if (e.target.closest('#win-terminal')) {
        document.getElementById('terminal-input').focus();
    }
});
