document.addEventListener('DOMContentLoaded', () => {

    const starsContainer = document.querySelector('.stars');
    for (let i = 0; i < 80; i++) {
        const star = document.createElement('div');
        star.classList.add('star');
        star.style.left = Math.random() * 100 + '%';
        star.style.top = Math.random() * 100 + '%';
        star.style.width = (Math.random() * 2 + 1) + 'px';
        star.style.height = star.style.width;
        star.style.animationDelay = (Math.random() * 3) + 's';
        star.style.animationDuration = (Math.random() * 2 + 2) + 's';
        starsContainer.appendChild(star);
    }

    const observerOptions = { threshold: 0.15, rootMargin: '0px 0px -60px 0px' };

    const fadeObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);

    document.querySelectorAll('.card, .character-card, .station').forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(40px)';
        el.style.transition = 'opacity 0.6s ease-out, transform 0.6s ease-out';
        fadeObserver.observe(el);
    });

    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('mousemove', (e) => {
            const rect = card.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            const centerX = rect.width / 2;
            const centerY = rect.height / 2;
            const rotateX = (y - centerY) / 15;
            const rotateY = (centerX - x) / 15;
            card.querySelector('.card-inner').style.transform = 
                `perspective(600px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
        });

        card.addEventListener('mouseleave', () => {
            card.querySelector('.card-inner').style.transform = 'scale(1.05)';
        });
    });

    const navLinks = document.querySelectorAll('.nav-links a');
    const logo = document.querySelector('.nav-logo');
    let lastScroll = 0;

    window.addEventListener('scroll', () => {
        const currentScroll = window.pageYOffset;
        if (currentScroll > 100) {
            logo.style.transform = 'scale(0.9)';
            logo.style.textShadow = '0 0 20px var(--neon-pink), 0 0 50px var(--neon-pink)';
        } else {
            logo.style.transform = 'scale(1)';
            logo.style.textShadow = '0 0 10px var(--neon-pink), 0 0 30px var(--neon-pink)';
        }
        lastScroll = currentScroll;
    });

    document.querySelectorAll('.station').forEach(station => {
        station.addEventListener('click', () => {
            const name = station.querySelector('h4').textContent;
            document.querySelectorAll('.station').forEach(s => s.style.opacity = '0.5');
            station.style.opacity = '1';
            station.style.boxShadow = `0 0 60px ${getComputedStyle(station).borderColor}`;
        });
    });

});
