// Enhanced Todo App JavaScript

// Dark mode functionality
function toggleTheme() {
    const body = document.body;
    const themeIcon = document.getElementById('theme-icon');
    const currentTheme = body.getAttribute('data-theme');
    
    if (currentTheme === 'dark') {
        body.removeAttribute('data-theme');
        themeIcon.className = 'fas fa-moon';
        localStorage.setItem('theme', 'light');
        showToast('Switched to light mode', 'success');
    } else {
        body.setAttribute('data-theme', 'dark');
        themeIcon.className = 'fas fa-sun';
        localStorage.setItem('theme', 'dark');
        showToast('Switched to dark mode', 'success');
    }
}

// Apply saved theme on page load
document.addEventListener('DOMContentLoaded', function() {
    const savedTheme = localStorage.getItem('theme');
    const themeIcon = document.getElementById('theme-icon');
    
    if (savedTheme === 'dark') {
        document.body.setAttribute('data-theme', 'dark');
        if (themeIcon) themeIcon.className = 'fas fa-sun';
    } else {
        if (themeIcon) themeIcon.className = 'fas fa-moon';
    }
});

// Toast notification system
function showToast(message, type = 'info') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
        <div class="toast-content">
            <i class="fas ${getToastIcon(type)}"></i>
            <span>${message}</span>
        </div>
    `;
    
    document.body.appendChild(toast);
    
    // Trigger animation
    setTimeout(() => toast.classList.add('show'), 100);
    
    // Auto remove
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => document.body.removeChild(toast), 300);
    }, 3000);
}

function getToastIcon(type) {
    switch(type) {
        case 'success': return 'fa-check-circle';
        case 'error': return 'fa-exclamation-circle';
        case 'warning': return 'fa-exclamation-triangle';
        default: return 'fa-info-circle';
    }
}

// Auto-hide alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-20px)';
            setTimeout(() => {
                if (alert.parentNode) {
                    alert.remove();
                }
            }, 300);
        }, 5000);
    });
});

// Add loading state to forms
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function() {
            const submitButton = form.querySelector('button[type="submit"]');
            if (submitButton && !submitButton.disabled) {
                submitButton.classList.add('loading');
                submitButton.disabled = true;
                
                // Re-enable after 5 seconds as a fallback
                setTimeout(() => {
                    submitButton.classList.remove('loading');
                    submitButton.disabled = false;
                }, 5000);
            }
        });
    });
});

// Auto-resize textarea
document.addEventListener('DOMContentLoaded', function() {
    const textareas = document.querySelectorAll('.todo-textarea');
    textareas.forEach(textarea => {
        function autoResize() {
            textarea.style.height = 'auto';
            textarea.style.height = textarea.scrollHeight + 'px';
        }
        
        textarea.addEventListener('input', autoResize);
        autoResize(); // Initial resize
    });
});

// Smooth scrolling for anchor links
document.addEventListener('DOMContentLoaded', function() {
    const links = document.querySelectorAll('a[href^="#"]');
    links.forEach(link => {
        link.addEventListener('click', function(e) {
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
});

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl/Cmd + D: Toggle dark mode
    if ((e.ctrlKey || e.metaKey) && e.key === 'd') {
        e.preventDefault();
        toggleTheme();
    }
    
    // Ctrl/Cmd + Enter: Submit the add todo form
    if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
        const todoInput = document.querySelector('.todo-input');
        if (todoInput && document.activeElement === todoInput) {
            e.preventDefault();
            const form = todoInput.closest('form');
            if (form) form.submit();
        }
    }
    
    // Escape: Clear search input
    if (e.key === 'Escape') {
        const searchInput = document.querySelector('.search-input');
        if (searchInput && document.activeElement === searchInput) {
            searchInput.value = '';
            searchInput.blur();
        }
    }
});

// Focus management
document.addEventListener('DOMContentLoaded', function() {
    // Auto-focus the todo input on page load
    const todoInput = document.querySelector('.todo-input');
    if (todoInput && window.location.pathname === '/') {
        setTimeout(() => todoInput.focus(), 500);
    }
    
    // Auto-focus textarea on edit page
    const textarea = document.querySelector('.todo-textarea');
    if (textarea && window.location.pathname.includes('/edit')) {
        setTimeout(() => {
            textarea.focus();
            textarea.setSelectionRange(textarea.value.length, textarea.value.length);
        }, 500);
    }
});

// Add ripple effect to buttons
document.addEventListener('DOMContentLoaded', function() {
    const buttons = document.querySelectorAll('.btn, .filter-btn, .toggle-btn');
    
    buttons.forEach(button => {
        button.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;
            
            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.classList.add('ripple');
            
            this.appendChild(ripple);
            
            setTimeout(() => {
                if (ripple.parentNode) {
                    ripple.remove();
                }
            }, 600);
        });
    });
});

// Stats animation counter
function animateCounter(element, target, duration = 1000) {
    const start = parseInt(element.textContent) || 0;
    const increment = (target - start) / (duration / 16);
    let current = start;
    
    const timer = setInterval(() => {
        current += increment;
        if ((increment > 0 && current >= target) || (increment < 0 && current <= target)) {
            current = target;
            clearInterval(timer);
        }
        element.textContent = Math.floor(current);
    }, 16);
}

// Animate stats on page load
document.addEventListener('DOMContentLoaded', function() {
    const statElements = document.querySelectorAll('.stat-item span');
    statElements.forEach(element => {
        const target = parseInt(element.textContent) || 0;
        element.textContent = '0';
        setTimeout(() => animateCounter(element, target), 500);
    });
});

// Add intersection observer for animations
document.addEventListener('DOMContentLoaded', function() {
    if ('IntersectionObserver' in window) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-in');
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.1,
            rootMargin: '20px'
        });
        
        const elements = document.querySelectorAll('.todo-item, .card');
        elements.forEach(el => observer.observe(el));
    }
});

// Add CSS for additional effects
const additionalStyles = `
    .toast {
        position: fixed;
        top: 20px;
        right: 20px;
        backdrop-filter: blur(20px);
        border-radius: var(--radius-lg);
        padding: 18px 24px;
        box-shadow: var(--shadow-xl);
        transform: translateX(400px);
        transition: transform 0.3s ease;
        z-index: 1000;
        min-width: 280px;
        border: 2px solid;
    }
    
    .toast.show {
        transform: translateX(0);
    }
    
    .toast-content {
        display: flex;
        align-items: center;
        gap: 12px;
        font-weight: 700;
        font-size: 0.95rem;
    }
    
    /* Success notifications - much darker green */
    .toast-success {
        background: rgba(20, 50, 35, 0.98);
        border-color: #1a3d2e;
        color: #4fd671;
        box-shadow: 0 4px 20px rgba(20, 50, 35, 0.8);
    }
    .toast-success .toast-content i { 
        color: #6ee887; 
        font-size: 1.2rem;
        text-shadow: 0 0 8px rgba(110, 232, 135, 0.5);
    }
    
    /* Error notifications - much darker red */
    .toast-error {
        background: rgba(80, 25, 25, 0.98);
        border-color: #4a1515;
        color: #ff6b6b;
        box-shadow: 0 4px 20px rgba(80, 25, 25, 0.8);
    }
    .toast-error .toast-content i { 
        color: #ff8a8a; 
        font-size: 1.2rem;
        text-shadow: 0 0 8px rgba(255, 138, 138, 0.5);
    }
    
    /* Warning notifications - much darker orange */
    .toast-warning {
        background: rgba(85, 55, 25, 0.98);
        border-color: #553719;
        color: #ffb347;
        box-shadow: 0 4px 20px rgba(85, 55, 25, 0.8);
    }
    .toast-warning .toast-content i { 
        color: #ffc773; 
        font-size: 1.2rem;
        text-shadow: 0 0 8px rgba(255, 199, 115, 0.5);
    }
    
    /* Info notifications - much darker blue */
    .toast-info {
        background: rgba(25, 40, 70, 0.98);
        border-color: #192846;
        color: #63b3ed;
        box-shadow: 0 4px 20px rgba(25, 40, 70, 0.8);
    }
    .toast-info .toast-content i { 
        color: #90cdf4; 
        font-size: 1.2rem;
        text-shadow: 0 0 8px rgba(144, 205, 244, 0.5);
    }
    
    .ripple {
        position: absolute;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.3);
        transform: scale(0);
        animation: ripple-animation 0.6s linear;
        pointer-events: none;
    }
    
    @keyframes ripple-animation {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }
    
    .animate-in {
        animation: fadeInUp 0.6s ease-out;
    }
`;

// Inject additional styles
document.addEventListener('DOMContentLoaded', function() {
    const styleSheet = document.createElement('style');
    styleSheet.textContent = additionalStyles;
    document.head.appendChild(styleSheet);
});
