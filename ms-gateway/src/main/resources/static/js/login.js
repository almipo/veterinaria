const formLogin = document.getElementById('login-form');
const errorLogin = document.getElementById('login-error');
const botonLogin = document.getElementById('login-button');
formLogin.addEventListener('submit', async event => {
  event.preventDefault();
  errorLogin.classList.add('d-none');
  botonLogin.disabled = true;
  try {
    const respuesta = await fetch('/auth/login', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({
        username: document.getElementById('username').value.trim(),
        password: document.getElementById('password').value
      })
    });
    if (!respuesta.ok) throw new Error('Usuario o contrasena incorrectos');
    const datos = await respuesta.json();
    if (!datos.token || typeof datos.token !== 'string') throw new Error('El servidor no devolvio un JWT');
    sessionStorage.setItem('vet_jwt', datos.token);
    window.location.replace('index.html');
  } catch (error) {
    errorLogin.textContent = error.message || 'No se pudo iniciar sesion';
    errorLogin.classList.remove('d-none');
  } finally { botonLogin.disabled = false; }
});
