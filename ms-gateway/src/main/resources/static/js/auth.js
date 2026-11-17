// Autenticacion del frontend para el Sprint 14.
// sessionStorage limita el token a la sesion de la pestana. No es un sustituto de cookies HttpOnly para produccion.
const VetAuth = (() => {
  const key = 'vet_jwt';
  const token = () => sessionStorage.getItem(key);
  const loginPage = () => 'login.html';
  const logout = () => { sessionStorage.removeItem(key); window.location.assign(loginPage()); };
  const requireLogin = () => {
    if (!token()) { window.location.replace(loginPage()); return false; }
    return true;
  };
  const request = async (url, options = {}) => {
    const jwt = token();
    if (!jwt) { window.location.replace(loginPage()); throw new Error('Inicia sesion para continuar'); }
    const headers = new Headers(options.headers || {});
    headers.set('Authorization', 'Bearer ' + jwt);
    const response = await fetch(url, { ...options, headers });
    if (response.status === 401) {
      sessionStorage.removeItem(key);
      window.location.replace(loginPage());
      throw new Error('Sesion vencida o token invalido');
    }
    return response;
  };
  const addLogout = () => {
    const nav = document.querySelector('.navbar-nav');
    if (!nav || document.getElementById('vet-logout')) return;
    const item = document.createElement('li');
    item.className = 'nav-item';
    const button = document.createElement('button');
    button.id = 'vet-logout';
    button.className = 'btn btn-outline-light btn-sm ms-lg-3 mt-1';
    button.type = 'button';
    button.textContent = 'Cerrar sesion';
    button.addEventListener('click', logout);
    item.appendChild(button);
    nav.appendChild(item);
  };
  return { token, logout, requireLogin, request, addLogout };
})();
window.addEventListener('DOMContentLoaded', () => VetAuth.addLogout());
