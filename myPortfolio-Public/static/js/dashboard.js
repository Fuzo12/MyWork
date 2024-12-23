// ================= Login -> Recuperar password  =======================
document.addEventListener('DOMContentLoaded', function() {

    var recoverPasswordLink = document.querySelector('.js-recover-password');
    recoverPasswordLink.addEventListener('click', function(event) {
        // Impede o comportamento default do link de redirecionar
        event.preventDefault();

        var loginForm = document.getElementById('js-loginForm');
        var recoverSectionForm = document.getElementById('js-recoverSection');

        // Esconde o formulário de login
        loginForm.style.display = 'none';
        // Exibe o formulário de recuperação 
        recoverSectionForm.style.display = 'block';
    });
});
// ================= Recuperar password -> Login =======================
document.addEventListener('DOMContentLoaded', function() {
    var gotoLoginLink = document.querySelector('.js-goto-login');

    gotoLoginLink.addEventListener('click', function(event) {
        // Impede o comportamento default do link de redirecionar
        event.preventDefault();

        // Obtém referências para os formulários de login e de recuperação 
        var loginForm = document.getElementById('js-loginForm');
        var recoverSectionForm = document.getElementById('js-recoverSection');

        // Esconde o formulário de recuperação
        recoverSectionForm.style.display = 'none';
        // Exibe o formulário de login
        loginForm.style.display = 'block';
    });
});