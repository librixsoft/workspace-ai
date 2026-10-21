/* ===== MOBILE MENU ===== */
const showMenu = (toggleId, navId) => {
    const toggle = document.getElementById(toggleId),
          nav = document.getElementById(navId);

    if (toggle && nav) {
        toggle.addEventListener('click', () => {
            nav.classList.toggle('show');
        });
        
        // Close menu when clicking a link
        nav.querySelectorAll('.nav__link').forEach(link => {
            link.addEventListener('click', () => {
                nav.classList.remove('show');
            });
        });
    }
};

showMenu('nav__toggle', 'nav-menu');

/* ===== LOADING SCREEN ===== */
window.addEventListener('load', () => {
    const loader = document.getElementById('loader');
    setTimeout(() => {
        loader.classList.add('loader-hidden');
        setTimeout(() => loader.style.display = 'none', 800);
    }, 1500);
});

/* ===== PARTICLE SYSTEM ===== */
(function initParticles() {
    const canvas = document.getElementById('particles');
    const ctx = canvas.getContext('2d');
    if (!canvas || !ctx) return;

    let particles = [];
    const PARTICLE_COUNT = 60;
    const COLORS = ['#EFD50C', '#00f0ff', '#ff006e', '#ff6600'];

    function resize() {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    }

    class Particle {
        constructor() {
            this.reset();
        }
        reset() {
            this.x = Math.random() * canvas.width;
            this.y = Math.random() * canvas.height;
            this.size = Math.random() * 2 + 0.5;
            this.speedX = (Math.random() - 0.5) * 0.3;
            this.speedY = (Math.random() - 0.5) * 0.3;
            this.color = COLORS[Math.floor(Math.random() * COLORS.length)];
            this.opacity = Math.random() * 0.5 + 0.1;
            this.pulse = Math.random() * Math.PI * 2;
        }
        update() {
            this.x += this.speedX;
            this.y += this.speedY;
            this.pulse += 0.02;
            this.opacity = 0.1 + Math.sin(this.pulse) * 0.15;
            if (this.x < 0 || this.x > canvas.width || this.y < 0 || this.y > canvas.height) {
                this.reset();
            }
        }
        draw() {
            ctx.save();
            ctx.globalAlpha = Math.max(0, this.opacity);
            ctx.fillStyle = this.color;
            ctx.shadowBlur = 10;
            ctx.shadowColor = this.color;
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
            ctx.fill();
            ctx.restore();
        }
    }

    function init() {
        resize();
        particles = [];
        for (let i = 0; i < PARTICLE_COUNT; i++) {
            particles.push(new Particle());
        }
    }

    function animate() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        particles.forEach(p => {
            p.update();
            p.draw();
        });
        requestAnimationFrame(animate);
    }

    window.addEventListener('resize', resize);
    init();
    animate();
})();

/* ===== GSAP ANIMATIONS ===== */

// Title vertical reveal
gsap.from('.home__title', {
    opacity: 0,
    duration: 1.5,
    delay: 2.5,
    rotation: -90,
    transformOrigin: 'left center',
    ease: 'power2.out'
});

// Car entrance - scale from center with glow
gsap.from('.home__img', {
    opacity: 0,
    duration: 1.5,
    delay: 2.8,
    scale: 0.7,
    filter: 'brightness(2) blur(10px)',
    ease: 'power3.out'
});

// Information slide from left
gsap.from('.home__information', {
    opacity: 0,
    x: -60,
    duration: 1,
    delay: 3.2,
    ease: 'power2.out'
});

// Medium stat box entrance
gsap.from('.box__medium', {
    opacity: 0,
    y: 50,
    scale: 0.8,
    duration: 1,
    delay: 3.5,
    ease: 'elastic.out(1, 0.6)'
});

// Small stat boxes stagger
gsap.from('.box__small', {
    opacity: 0,
    y: 40,
    duration: 0.8,
    delay: 3.7,
    stagger: 0.15,
    ease: 'power2.out'
});

/* ===== COUNTER ANIMATION ===== */
function animateCounter(element, target, duration, isDecimal) {
    const start = 0;
    const startTime = performance.now();

    function update(currentTime) {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        const eased = 1 - Math.pow(1 - progress, 3); // ease out cubic
        const current = start + (target - start) * eased;

        if (isDecimal) {
            element.textContent = current.toFixed(1);
        } else {
            element.textContent = Math.floor(current).toLocaleString();
        }

        if (progress < 1) {
            requestAnimationFrame(update);
        }
    }

    requestAnimationFrame(update);
}

// Trigger counters after loading
setTimeout(() => {
    document.querySelectorAll('[data-target]').forEach(el => {
        const target = parseFloat(el.getAttribute('data-target'));
        const isDecimal = target < 100 && target > 1;
        animateCounter(el, target, 2000, isDecimal);
    });
}, 3500);

/* ===== PERIODIC GLITCH ON SUBTITLE ===== */
const subtitle = document.querySelector('.subtitle-accent');
if (subtitle) {
    setInterval(() => {
        gsap.to(subtitle, {
            x: 6,
            duration: 0.04,
            yoyo: true,
            repeat: 4,
            onComplete: () => gsap.set(subtitle, { x: 0 })
        });
    }, 5000);
}

/* ===== NEON PULSE ON SPEC BOXES ===== */
gsap.to('.box__content', {
    boxShadow: '0 0 30px rgba(239, 213, 12, 0.2), inset 0 0 20px rgba(0, 0, 0, 0.5)',
    borderColor: 'rgba(239, 213, 12, 0.3)',
    duration: 2.5,
    repeat: -1,
    yoyo: true,
    ease: 'sine.inOut',
    stagger: 0.4
});
