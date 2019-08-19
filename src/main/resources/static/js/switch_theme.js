$(function () {
    'use strict';
    if (localStorage.getItem('themeStyle') === 'dark') {
        document.documentElement.setAttribute("theme", "dark");
        document.getElementById('switch-theme').setAttribute('checked', true);
    } else {
        document.documentElement.removeAttribute("theme");
    }

    document.getElementById('switch-theme').addEventListener('change', ev => {
        let btn = ev.target;
        if (btn.checked) {
            document.documentElement.setAttribute("theme", "dark");
            localStorage.setItem('themeStyle', 'dark');
        } else {
            document.documentElement.removeAttribute("theme");
            localStorage.setItem('themeStyle', 'light');
        }
    });
})

