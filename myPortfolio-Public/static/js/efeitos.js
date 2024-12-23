// Modal dos projetos

document.addEventListener('DOMContentLoaded', () => {
  const modal = document.getElementById('project-modal');

  modal.addEventListener('show.bs.modal', function (event) {
    // Elemento que acionou o modal
    const trigger = event.relatedTarget;
    const projectId = trigger.getAttribute('data-project-id');

    // Atualize o modal com as informações do projeto
    fetch(`/project-details/${projectId}`) // Endpoint para obter detalhes do projeto
      .then(response => response.json())
      .then(data => {
        document.getElementById('modal-project-name').textContent = data.name;
        document.getElementById('modal-project-description').textContent = data.description;

        // Atualizar imagem do projeto
        const projectImage = document.getElementById('modal-project-image');
        if (data.photo1 && data.photo1.trim() !== "") {
          console.log(data.photo1)
          projectImage.src = data.photo1;
          projectImage.alt = data.name;
        } else {
          projectImage.src = "default-image.jpg"; // Substitua pela imagem padrão
          projectImage.alt = "No image available";
        }

        // Atualizar a lista de tecnologias
        const technologiesList = document.getElementById('modal-project-technologies');
        technologiesList.innerHTML = ""; // Limpa a lista antes de preenchê-la

        console.log(data.technologies);

        // Remove caracteres [ e ] e converte para array se for uma string
        let technologiesArray = [];
        if (typeof data.technologies === "string") {
          technologiesArray = data.technologies
            .replace(/\[|\]/g, "") // Remove os caracteres [ e ]
            .split(',')
            .map(tech => tech.trim());
        } else if (Array.isArray(data.technologies)) {
          technologiesArray = data.technologies;
        }

        // Preenche a lista de tecnologias
        if (technologiesArray.length > 0) {
          technologiesArray.forEach(tech => {
            const listItem = document.createElement('li');
            listItem.textContent = tech;
            technologiesList.appendChild(listItem);
          });
        } else {
          const noTechMessage = document.createElement('li');
          noTechMessage.textContent = "No technologies listed.";
          technologiesList.appendChild(noTechMessage);
        }

        // Configurações para redirecionar os botões
        const gitButton = document.getElementById('modal-project-git');
        const linkButton = document.getElementById('modal-project-link');

        if (data.github && data.github.trim() !== "") {
          gitButton.setAttribute('data-href', data.github);
          gitButton.style.display = "inline-block"; // Mostra o botão caso esteja escondido
        } else {
          gitButton.style.display = "none"; // Esconde o botão se não houver link
        }

        linkButton.setAttribute('data-href', data.link);
      })
      .catch(error => {
        console.error('Erro ao carregar detalhes do projeto:', error);
        document.getElementById('modal-project-name').textContent = 'Erro';
        document.getElementById('modal-project-description').textContent = 'Não foi possível carregar os detalhes do projeto.';
      });
  });
});

//Funções para redirecionar ao clicar nos botões
function redirectToGitHub() {
  const gitButton = document.getElementById('modal-project-git');
  const href = gitButton.getAttribute('data-href');
  if (href) {
    window.open(href, '_blank'); 
  }
}

function redirectToProject() {
  const linkButton = document.getElementById('modal-project-link');
  const href = linkButton.getAttribute('data-href');
  if (href) {
    window.open(href, '_blank'); 
  }
}

