const userAPI = 'http://localhost:8080/api/sales';
// const documentAPI = 'http://localhost:8080/api/sales/documents';
// const userAPI = 'http://localhost:8080/api/secretary';
const documentAPI = 'http://localhost:8080/api/sales/documents';
const documentacceptedAPI = 'http://localhost:8080/api/sales/documentsaccepted';
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
                    <span> with roles: </span>
                    <span>${roles}</span>`;
            userInfo.innerHTML = `
                        <th scope="row">${principal.id}</th>
                        <td>${principal.name}</td>
                        <td>${principal.surname}</td>
                        <td>${principal.email}</td>
                        <td>
                            <span>${roles}</span></td>`;
        });

    $(document).ready(function () {

        // Функция для загрузки списка документов
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
                                <button class="btn btn-sm btn-primary download-btn" data-id="${doc.id}">Download</button>
                                
                            </td>
                        </tr>`;
                    });
                    $('#document-info').html(documentRows);
                })
                .catch(error => console.error("Failed to load documents:", error));
        }

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
                                <button class="btn btn-sm btn-primary download-btn" data-id="${doc.id}">Download</button>
                                
                            </td>
                        </tr>`;
                    });
                    $('#document-info-accepted').html(documentRows);
                })
                .catch(error => console.error("Failed to load documents:", error));
        }

        // Upload document
        $('#upload-form').on('submit', function (event) {
            event.preventDefault();

            const formData = new FormData();
            const files = $('#files')[0].files; // Получаем все выбранные файлы

            // Перебираем все выбранные файлы и добавляем их в FormData
            for (let i = 0; i < files.length; i++) {
                formData.append('files', files[i]);
            }
            formData.append('title', $('#title').val());
            formData.append('department', $('#department').val());

            fetch(`${documentAPI}/upload`, {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        alert('Documents uploaded successfully.');
                        loadDocuments(); // Reload document list
                        loadDocumentsAccepted();
                    } else {
                        alert('Error uploading documents.');
                    }
                })
                .catch(error => console.error('Error uploading documents:', error));
        });

        // Download document
        $(document).on('click', '.download-btn', function () {
            const docId = $(this).data('id');
            fetch(`${documentDownloadAPI}/${docId}/downloadsales`)
                .then(response => {
                    if (response.ok) {
                        const disposition = response.headers.get('Content-Disposition');
                        const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                        const matches = filenameRegex.exec(disposition);
                        let filename = 'document.zip';
                        if (matches != null && matches[1]) {
                            filename = decodeURIComponent(matches[1].replace(/['"]/g, ''));
                        }
                        return response.blob().then(blob => ({ blob, filename }));
                    } else {
                        throw new Error('Error downloading document.');
                    }
                })
                .then(({ blob, filename }) => {
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = filename; // Используем полученное имя файла
                    document.body.appendChild(a);
                    a.click();
                    a.remove();

                    // После скачивания архива обновляем страницу, чтобы перезагрузить документы
                    location.reload();
                })
                .catch(error => console.error('Error downloading document:', error));
        });

        // Загрузка документов при загрузке страницы
        loadDocuments();
        loadDocumentsAccepted();
    });
}

getUser();

