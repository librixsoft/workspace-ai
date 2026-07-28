// ========================================
// Mobile Navigation Toggle
// ========================================
const navToggle = document.querySelector('.nav-toggle');
const navLinks = document.querySelector('.nav-links');
const navActions = document.querySelector('.nav-actions');

if (navToggle) {
  navToggle.addEventListener('click', () => {
    const isOpen = navToggle.classList.toggle('active');

    if (isOpen) {
      Object.assign(navLinks.style, {
        display: 'flex',
        flexDirection: 'column',
        position: 'absolute',
        top: '100%',
        left: 0,
        right: 0,
        padding: '1rem',
        background: 'rgba(10, 10, 26, 0.95)',
        backdropFilter: 'blur(24px)',
        WebkitBackdropFilter: 'blur(24px)',
        borderRadius: '0 0 16px 16px',
        border: '1px solid rgba(255,255,255,0.08)',
        borderTop: 'none',
      });

      Object.assign(navActions.style, {
        display: 'flex',
        flexDirection: 'column',
        position: 'absolute',
        top: navLinks.offsetHeight + 16,
        left: 0,
        right: 0,
        padding: '0 1rem 1rem',
      });

      Object.assign(navToggle.children[0], { transform: 'rotate(45deg) translate(4px, 4px)' });
      Object.assign(navToggle.children[1], { opacity: '0' });
      Object.assign(navToggle.children[2], { transform: 'rotate(-45deg) translate(4px, -4px)' });
    } else {
      navLinks.removeAttribute('style');
      navActions.removeAttribute('style');

      navToggle.children[0].removeAttribute('style');
      navToggle.children[1].removeAttribute('style');
      navToggle.children[2].removeAttribute('style');
    }
  });
}

// ========================================
// Smooth scroll for navigation links
// ========================================
document.querySelectorAll('.nav-links a, .footer-col a[href^="#"]').forEach(link => {
  link.addEventListener('click', e => {
    const href = link.getAttribute('href');
    if (href && href.startsWith('#') && href.length > 1) {
      const target = document.querySelector(href);
      if (target) {
        e.preventDefault();
        target.scrollIntoView({ behavior: 'smooth', block: 'start' });

        // Close mobile menu if open
        if (navToggle.classList.contains('active')) {
          navToggle.click();
        }
      }
    }
  });
});

// ========================================
// Parallax effect on hero cards
// ========================================
const heroVisual = document.querySelector('.hero-visual');
const heroCards = document.querySelectorAll('.hero-card');

if (heroVisual && heroCards.length) {
  window.addEventListener('mousemove', e => {
    const rect = heroVisual.getBoundingClientRect();
    if (rect.top > window.innerHeight || rect.bottom < 0) return;

    const centerX = rect.left + rect.width / 2;
    const centerY = rect.top + rect.height / 2;

    const deltaX = (e.clientX - centerX) / rect.width;
    const deltaY = (e.clientY - centerY) / rect.height;

    heroCards.forEach((card, i) => {
      const factor = (i + 1) * 6;
      const moveX = deltaX * factor;
      const moveY = deltaY * factor;
      card.style.transform = `translate(${moveX}px, ${moveY}px)`;
    });
  });
}

// ========================================
// Intersection Observer for scroll reveal
// ========================================
const observerOptions = {
  threshold: 0.15,
  rootMargin: '0px 0px -60px 0px',
};

const revealObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.style.opacity = '1';
      entry.target.style.transform = 'translateY(0)';
      revealObserver.unobserve(entry.target);
    }
  });
}, observerOptions);

document.querySelectorAll('.features, .pricing, .testimonials, .cta-section').forEach(section => {
  const cards = section.querySelectorAll('.glass-card');
  cards.forEach(card => {
    card.style.opacity = '0';
    card.style.transform = 'translateY(30px)';
    revealObserver.observe(card);
  });
});

// ========================================
// CTA Form interaction
// ========================================
const ctaForm = document.querySelector('.cta-form');
if (ctaForm) {
  const emailInput = ctaForm.querySelector('input[type="email"]');
  const submitBtn = ctaForm.querySelector('button[type="submit"]');

  emailInput.addEventListener('keypress', e => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSignup(emailInput.value, submitBtn);
    }
  });

  ctaForm.addEventListener('submit', e => {
    e.preventDefault();
    handleSignup(emailInput.value, submitBtn);
  });

  function handleSignup(email, btn) {
    if (!email || !btn) return;

    const originalText = btn.textContent;
    btn.textContent = '✓ ¡Cuenta creada!';
    btn.style.background = 'linear-gradient(135deg, #4ECDC4, #2ecc71)';
    emailInput.value = '';

    setTimeout(() => {
      btn.textContent = originalText;
      btn.style.background = '';
    }, 3000);
  }
}

// ========================================
// Nav background on scroll
// ========================================
const nav = document.querySelector('.glass-nav');

window.addEventListener('scroll', () => {
  if (nav) {
    if (window.scrollY > 50) {
      nav.style.background = 'rgba(10, 10, 26, 0.8)';
    } else {
      nav.style.background = 'rgba(255, 255, 255, 0.06)';
    }
  }
}, { passive: true });
