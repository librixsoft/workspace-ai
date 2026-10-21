// ===== CUSTOM CURSOR =====
const cursor = document.querySelector('.cursor');
const follower = document.querySelector('.cursor-follower');

if (window.innerWidth > 768) {
  document.addEventListener('mousemove', (e) => {
    cursor.style.left = e.clientX + 'px';
    cursor.style.top = e.clientY + 'px';
    follower.style.left = e.clientX + 'px';
    follower.style.top = e.clientY + 'px';
  });

  const interactiveElements = document.querySelectorAll('a, button, .model-card, .tech-card');
  interactiveElements.forEach(el => {
    el.addEventListener('mouseenter', () => {
      cursor.classList.add('active');
      follower.classList.add('active');
    });
    el.addEventListener('mouseleave', () => {
      cursor.classList.remove('active');
      follower.classList.remove('active');
    });
  });
}

// ===== PRELOADER =====
window.addEventListener('load', () => {
  const preloader = document.querySelector('.preloader');
  setTimeout(() => {
    preloader.classList.add('hidden');
    setTimeout(() => preloader.remove(), 800);
  }, 1500);
});

// ===== NAVIGATION SCROLL =====
const nav = document.querySelector('.nav');
let lastScroll = 0;

window.addEventListener('scroll', () => {
  const currentScroll = window.pageYOffset;
  
  if (currentScroll > 50) {
    nav.classList.add('scrolled');
  } else {
    nav.classList.remove('scrolled');
  }
  
  lastScroll = currentScroll;
});

// ===== MOBILE MENU =====
const navToggle = document.getElementById('navToggle');
const mobileMenu = document.getElementById('mobileMenu');
const mobileClose = document.getElementById('mobileClose');

navToggle.addEventListener('click', () => {
  navToggle.classList.add('active');
  mobileMenu.classList.add('active');
  document.body.style.overflow = 'hidden';
});

mobileClose.addEventListener('click', () => {
  navToggle.classList.remove('active');
  mobileMenu.classList.remove('active');
  document.body.style.overflow = '';
});

document.querySelectorAll('.mobile-menu__link').forEach(link => {
  link.addEventListener('click', () => {
    navToggle.classList.remove('active');
    mobileMenu.classList.remove('active');
    document.body.style.overflow = '';
  });
});

// ===== ACTIVE NAV LINK =====
const sections = document.querySelectorAll('section[id]');
const navLinks = document.querySelectorAll('.nav__link');

window.addEventListener('scroll', () => {
  let current = '';
  sections.forEach(section => {
    const sectionTop = section.offsetTop;
    const sectionHeight = section.clientHeight;
    if (window.pageYOffset >= sectionTop - 200) {
      current = section.getAttribute('id');
    }
  });

  navLinks.forEach(link => {
    link.classList.remove('active');
    if (link.getAttribute('href') === '#' + current) {
      link.classList.add('active');
    }
  });
});

// ===== SCROLL REVEAL =====
const revealElements = document.querySelectorAll('.model-card, .tech-card, .heritage__title, .heritage__text, .heritage__timeline, .timeline-item, .section-header');

const revealObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.classList.add('active');
      entry.target.style.opacity = '1';
      entry.target.style.transform = 'translateY(0)';
    }
  });
}, {
  threshold: 0.1,
  rootMargin: '0px 0px -50px 0px'
});

revealElements.forEach(el => {
  el.style.opacity = '0';
  el.style.transform = 'translateY(40px)';
  el.style.transition = 'opacity 0.8s ease, transform 0.8s ease';
  revealObserver.observe(el);
});

// Stagger animation for model cards
const modelCards = document.querySelectorAll('.model-card');
modelCards.forEach((card, index) => {
  card.style.transitionDelay = `${index * 0.15}s`;
});

// Stagger animation for tech cards
const techCards = document.querySelectorAll('.tech-card');
techCards.forEach((card, index) => {
  card.style.transitionDelay = `${index * 0.1}s`;
});

// Stagger animation for timeline items
const timelineItems = document.querySelectorAll('.timeline-item');
timelineItems.forEach((item, index) => {
  item.style.transitionDelay = `${index * 0.1}s`;
  item.style.opacity = '0';
  item.style.transform = 'translateX(-30px)';
});

// ===== COUNTER ANIMATION =====
const statValues = document.querySelectorAll('.stat-card__value');

const counterObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      const target = entry.target;
      const finalValue = parseFloat(target.getAttribute('data-count'));
      const isDecimal = finalValue % 1 !== 0;
      const duration = 2000;
      const startTime = performance.now();

      const animate = (currentTime) => {
        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);
        
        // Easing function (ease-out)
        const eased = 1 - Math.pow(1 - progress, 3);
        const currentValue = eased * finalValue;

        if (isDecimal) {
          target.textContent = currentValue.toFixed(1);
        } else {
          target.textContent = Math.floor(currentValue);
        }

        if (progress < 1) {
          requestAnimationFrame(animate);
        } else {
          target.textContent = isDecimal ? finalValue.toFixed(1) : finalValue;
        }
      };

      requestAnimationFrame(animate);
      counterObserver.unobserve(target);
    }
  });
}, { threshold: 0.5 });

statValues.forEach(val => counterObserver.observe(val));

// ===== HERO CAR PARALLAX =====
const heroCar = document.querySelector('.hero__car-img');

if (heroCar) {
  window.addEventListener('scroll', () => {
    const scrolled = window.pageYOffset;
    const heroHeight = document.querySelector('.hero').offsetHeight;
    
    if (scrolled < heroHeight) {
      const parallaxValue = scrolled * 0.15;
      const rotateValue = scrolled * 0.01;
      heroCar.style.transform = `translateY(${parallaxValue}px) rotate(${rotateValue}deg)`;
    }
  });
}

// ===== SMOOTH SCROLL =====
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
  anchor.addEventListener('click', function(e) {
    e.preventDefault();
    const target = document.querySelector(this.getAttribute('href'));
    if (target) {
      const offsetTop = target.offsetTop - 80;
      window.scrollTo({
        top: offsetTop,
        behavior: 'smooth'
      });
    }
  });
});

// ===== TILT EFFECT ON MODEL CARDS =====
if (window.innerWidth > 768) {
  document.querySelectorAll('.model-card').forEach(card => {
    card.addEventListener('mousemove', (e) => {
      const rect = card.getBoundingClientRect();
      const x = e.clientX - rect.left;
      const y = e.clientY - rect.top;
      const centerX = rect.width / 2;
      const centerY = rect.height / 2;
      const rotateX = (y - centerY) / 20;
      const rotateY = (centerX - x) / 20;

      card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) translateY(-5px)`;
    });

    card.addEventListener('mouseleave', () => {
      card.style.transform = 'perspective(1000px) rotateX(0) rotateY(0) translateY(0)';
    });
  });
}

// ===== BACKGROUND PARTICLES =====
const heroSection = document.querySelector('.hero');
if (heroSection) {
  const particleContainer = document.createElement('div');
  particleContainer.className = 'particles';
  particleContainer.style.cssText = `
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1;
    pointer-events: none;
    overflow: hidden;
  `;

  for (let i = 0; i < 30; i++) {
    const particle = document.createElement('div');
    const size = Math.random() * 3 + 1;
    const x = Math.random() * 100;
    const y = Math.random() * 100;
    const duration = Math.random() * 20 + 10;
    const delay = Math.random() * 10;

    particle.style.cssText = `
      position: absolute;
      width: ${size}px;
      height: ${size}px;
      background: rgba(239, 213, 12, ${Math.random() * 0.3 + 0.1});
      left: ${x}%;
      top: ${y}%;
      border-radius: 50%;
      animation: floatParticle ${duration}s ease-in-out ${delay}s infinite;
    `;
    particleContainer.appendChild(particle);
  }

  heroSection.appendChild(particleContainer);

  // Add particle keyframes
  const style = document.createElement('style');
  style.textContent = `
    @keyframes floatParticle {
      0%, 100% {
        transform: translate(0, 0) scale(1);
        opacity: 0.3;
      }
      25% {
        transform: translate(${Math.random() * 100 - 50}px, ${Math.random() * 100 - 50}px) scale(1.5);
        opacity: 0.6;
      }
      50% {
        transform: translate(${Math.random() * 100 - 50}px, ${Math.random() * 100 - 50}px) scale(0.8);
        opacity: 0.2;
      }
      75% {
        transform: translate(${Math.random() * 100 - 50}px, ${Math.random() * 100 - 50}px) scale(1.2);
        opacity: 0.5;
      }
    }
  `;
  document.head.appendChild(style);
}

// ===== DYNAMIC GRADIENT FOLLOW CURSOR =====
if (window.innerWidth > 768) {
  const heroGradient = document.querySelector('.hero__bg-gradient');
  if (heroGradient) {
    document.addEventListener('mousemove', (e) => {
      const x = (e.clientX / window.innerWidth) * 100;
      const y = (e.clientY / window.innerHeight) * 100;
      heroGradient.style.background = `
        radial-gradient(ellipse at ${x}% ${y}%, rgba(239, 213, 12, 0.06) 0%, transparent 50%),
        radial-gradient(ellipse at ${100 - x}% ${100 - y}%, rgba(237, 76, 80, 0.04) 0%, transparent 50%),
        radial-gradient(ellipse at 50% 50%, rgba(239, 213, 12, 0.02) 0%, transparent 70%)
      `;
    });
  }
}
