//====== FILTRO DA PÁGINA TICKETS
// Seleciona o botão de filtro e o elemento a ser mostrado/ocultado
const filterButton = document.querySelector('.js--filter-button');
const ticketsFilter = document.querySelector('.js--tickets-filter');

// Adiciona o evento de clique ao botão
filterButton.addEventListener('click', function() {
    // Alterna a exibição do filtro
    ticketsFilter.classList.toggle('hidden');

    // Alterna a classe 'active' no botão para manter o estilo de hover
    filterButton.classList.toggle('active-button');
});

//=========== Confirmar a eliminação do ticket
function confirmDelete() {
    return confirm('Você tem certeza que deseja excluir este ticket? Esta ação não pode ser desfeita.');
}

//========== Filtros Status e Tipo da página Tickets
