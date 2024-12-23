// ================= Login -> Recuperar password  =======================
document.addEventListener('DOMContentLoaded', function() {

    var recoverPasswordLink = document.querySelector('.js-recover-password');
    // Adiciona evento de clique ao link
    recoverPasswordLink.addEventListener('click', function(event) {
        // Impede o comportamento default do link de redirecionar
        event.preventDefault();

        // Obtém referências para dos formulários de login e de recuperação
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
    // Obtém a referência para o link "Voltar ao login?"
    var gotoLoginLink = document.querySelector('.js-goto-login');

    // Adiciona evento de clique ao link
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
// ================= Registar supplier =======================
document.addEventListener('DOMContentLoaded', function() {
    var registerButton = document.querySelector('.register--button.js-submit');
    var registerForm = document.getElementById('js-registerSection');
    
    registerButton.addEventListener('click', function(event) {
        //impede o comportamento default do botão de enviar formulário
        event.preventDefault();

        // Obtém referências para dos formulários de login e de recuperação
        var loginForm = document.getElementById('js-loginForm');
        
        // Esconde o formulário de login
        loginForm.style.display = 'none';
        // exibir o formulário
        registerForm.style.display = 'block';
    });
});
//go back to supplier login 
document.addEventListener('DOMContentLoaded', function() {
    var backToLoginLink = document.querySelector('.js-back-login');

    backToLoginLink.addEventListener('click', function(event) {
        //impede o comportamento default do botão de enviar formulário
        event.preventDefault();

        //oculta o formulário de registo
        var registerForm = document.getElementById('js-registerSection');
        registerForm.style.display = 'none';

        // exibir o formulário de login
        var loginForm = document.getElementById('js-loginForm');
        loginForm.style.display = 'block';

    });
   
});
// ================= Supplier profile page =======================
function showSupplierInfo(){
    var editButton = document.getElementById('edit-button');
    editButton.style.display = 'none';

    var editSupplierInfo = document.getElementById('edit-supplier-details');
    editSupplierInfo.style.display = 'block';
}
function goBack(){
    var editButton = document.getElementById('edit-button');
    editButton.style.display = 'block';

    var editSupplierInfo = document.getElementById('edit-supplier-details');
    editSupplierInfo.style.display = 'none';
}

// ================= Alert Message para botões submit =======================

//Script para usar o Alert Messges nos botões de submit
document.addEventListener('DOMContentLoaded', function() {
    function addSubmitConfirmation(button) {
        button.addEventListener('click', function(event) {
            // Verifica se o botão tem a classe register--button e, se tiver, não aplica a confirmação
            if (button.classList.contains('register--button') || button.classList.contains('login--button')) {
                return;
            }

            // Previne o comportamento padrão do formulário
            event.preventDefault();

            // Exibe a mensagem de alerta e verifica se o usuário clicou em "OK"
            if (confirm('Você deseja realmente submeter as alterações?')) {
                // Se clicou em "OK", submete o formulário
                button.closest('form').submit();
            }
        });
    }

    function initializeSubmitButtons() {
        // Seleciona todos os botões do tipo submit
        var submitButtons = document.querySelectorAll('input[type="submit"], button[type="submit"]');

        // Adiciona o evento de clique a cada botão de submit
        submitButtons.forEach(addSubmitConfirmation);
    }

    // Inicializa os botões de submit na carga inicial da página
    initializeSubmitButtons();

    // Observa mudanças no DOM para capturar botões de submit adicionados dinamicamente
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.addedNodes.length) {
                mutation.addedNodes.forEach(function(node) {
                    if (node.nodeType === 1) {
                        // Seleciona novos botões de submit adicionados
                        var newSubmitButtons = node.querySelectorAll('input[type="submit"], button[type="submit"]');
                        newSubmitButtons.forEach(addSubmitConfirmation);
                    }
                });
            }
        });
    });

    observer.observe(document.body, {
        childList: true,
        subtree: true
    });
});