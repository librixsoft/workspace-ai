// Corporate Landing Page - Transacciones

// Mobile navigation toggle
const mobileMenuBtn = document.createElement('button');
mobileMenuBtn.className = 'mobile-menu-btn';
mobileMenuBtn.innerHTML = '<i class="fas fa-bars"></i>';
mobileMenuBtn.style.display = 'none';
mobileMenuBtn.style.position = 'fixed';
mobileMenuBtn.style.top = '20px';
mobileMenuBtn.style.right = '20px';
mobileMenuBtn.style.zIndex = '1001';
mobileMenuBtn.style.background = 'white';
mobileMenuBtn.style.padding = '10px';
mobileMenuBtn.style.borderRadius = '50%';
mobileMenuBtn.style.boxShadow = '0 2px 10px rgba(0,0,0,0.1)';
document.querySelector('.navbar').prepend(mobileMenuBtn);

// Mobile menu creation
const mobileNav = document.createElement('div');
mobileNav.className = 'mobile-nav';
mobileNav.style.position = 'fixed';
mobileNav.style.top = '0';
mobileNav.style.right = '0';
mobileNav.style.width = '250px';
mobileNav.style.height = '100vh';
mobileNav.style.background = 'white';
mobileNav.style.boxShadow = '-10px 0 20px rgba(0,0,0,0.1)';
mobileNav.style.zIndex = '1000';
mobileNav.style.transform = 'translateX(100%)';
mobileNav.style.transition = 'transform 0.3s ease';

const navLinks = document.querySelector('.nav-links').cloneNode(true);
navLinks.style.flexDirection = 'column';
navLinks.style.gap = '20px';
navLinks.style.padding = '80px 20px';
navLinks.style.background = 'var(--primary-color)';

navLinks.querySelectorAll('a').forEach(link => {
    link.style.color = 'white';
    link.style.padding = '10px';
    link.style.margin = '5px 0';
    link.style.borderRadius = '4px';
});

mobileNav.appendChild(navLinks);
document.body.appendChild(mobileNav);

// Mobile menu toggle
mobileMenuBtn.addEventListener('click', () => {
    const isOpen = mobileNav.style.transform === 'translateX(0)';
    mobileNav.style.transform = isOpen ? 'translateX(100%)' : 'translateX(0)';
});

// Smooth scroll for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Navbar background on scroll
const navbar = document.querySelector('.navbar');
let lastScroll = 0;

window.addEventListener('scroll', () => {
    const currentScroll = window.pageYOffset;;
    
    if (currentScroll > 100) {
        navbar.style.background = 'rgba(255, 255, 255, 0.95)';
        navbar.style.boxShadow = '0 2px 20px rgba(0, 0, 0, 0.1)';
    } else {
        navbar.style.background = 'var(--white)';
        navbar.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.05)';
    }
    lastScroll = currentScroll;
});

// Intersection Observer for fade-in animations
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('fade-in-visible');
        }
    });
}, observerOptions);

// Observe all section content
document.querySelectorAll('section').forEach(section => {
    section.style.opacity = '0';
    section.style.transition = 'opacity 0.6s ease';
    observer.observe(section);
});

// Add fade-in class after transition
document.addEventListener('DOMContentLoaded', () => {
    setTimeout(() => {
        document.querySelectorAll('section').forEach(section => {
            section.style.opacity = '1';
        });
    }, 100);
});