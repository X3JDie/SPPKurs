const userAPI = 'http://localhost:8080/api/finance';
const documentAPI = 'http://localhost:8080/api/finance/documents';
const documentuploadAPI = 'http://localhost:8080/api/documents';
const documentacceptedAPI = 'http://localhost:8080/api/finance/documentsaccepted';
const documentDownloadAPI = 'http://localhost:8080/api/documents';
const userHeader = document.getElementById("navbar-user");
const userInfo = document.getElementById("user-info");

function getUser() {
    fetch(userAPI)
        .then(res => res.json())
        .then(principal => {
            let roles = "";
            principal.roles.forEach(value => {
                roles += value.name + " ";
            });
            userHeader.innerHTML = `<span class="fw-bolder">${principal.email}</span>
                    <span> с ролями: </span>
                    <span>${roles}</span>`;
            userInfo.innerHTML = `
                        <th scope="row">${principal.id}</th>
                        <td>${principal.name}</td>
                        <td>${principal.surname}</td>
                        <td>${principal.email}</td>
                        <td>
                            <span>${roles}</span></td>`;
        })
        .catch(error => console.error("Не удалось загрузить информацию о пользователе:", error));

    // Загрузка документов после загрузки страницы
    $(document).ready(function () {
        loadDocuments();
        loadDocumentsAccepted();

        // Отправка формы загрузки документов
        $('#upload-form').on('submit', function (event) {
            event.preventDefault();

            const formData = new FormData();
            const files = $('#files')[0].files;

            for (let i = 0; i < files.length; i++) {
                formData.append('files', files[i]);
            }
            formData.append('title', $('#title').val());
            formData.append('department', $('#department').val());

            fetch(`${documentuploadAPI}/upload`, {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        alert('Документы успешно загружены.');
                        loadDocuments(); // Обновляем список документов
                        loadDocumentsAccepted();
                    } else {
                        alert('Ошибка при загрузке документов.');
                    }
                })
                .catch(error => console.error('Ошибка при загрузке документов:', error));
        });

        // Загрузка документов
        function loadDocuments() {
            fetch(documentAPI)
                .then(res => res.json())
                .then(documents => {
                    let documentRows = '';
                    documents.forEach(doc => {
                        documentRows += `
                        <tr>
                            <td>${doc.id}</td>
                            <td>${doc.title}</td>
                            <td>${doc.department}</td>
                            <td>${new Date(doc.uploadDate).toLocaleString()}</td>
                            <td>${doc.status}</td>
                            <td>
                                <button class="btn btn-sm btn-primary download-btn" data-id="${doc.id}">Скачать</button>
                            </td>
                        </tr>`;
                    });
                    $('#document-info').html(documentRows);
                })
                .catch(error => console.error("Не удалось загрузить список документов:", error));
        }

        // Загрузка принятых документов
        function loadDocumentsAccepted() {
            fetch(documentacceptedAPI)
                .then(res => res.json())
                .then(documents => {
                    let documentRows = '';
                    documents.forEach(doc => {
                        documentRows += `
                        <tr>
                            <td>${doc.id}</td>
                            <td>${doc.title}</td>
                            <td>${doc.department}</td>
                            <td>${new Date(doc.uploadDate).toLocaleString()}</td>
                            <td>${doc.status}</td>
                            <td>
                                <button class="btn btn-sm btn-primary download-btn" data-id="${doc.id}">Скачать</button>
                            </td>
                        </tr>`;
                    });
                    $('#document-info-accepted').html(documentRows);
                })
                .catch(error => console.error("Не удалось загрузить список принятых документов:", error));
        }
    });
}

getUser();
