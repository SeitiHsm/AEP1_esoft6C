
const API = {
    usuarios: "/usuarios",
    doacoes: "/doacoes"
};

const statusConfig = {
    PENDENTE: { label: "Pendente", classe: "status-pendente", color: "#dc9235" },
    RECEBIDA: { label: "Recebida", classe: "status-recebida", color: "#2874bd" },
    EM_TRIAGEM: { label: "Em triagem", classe: "status-em-triagem", color: "#8064c9" },
    DISPONIVEL: { label: "Disponível", classe: "status-disponivel", color: "#1c9b70" },
    CONCLUIDA: { label: "Concluída", classe: "status-concluida", color: "#64748b" }
};

let usuarios = [];
let doacoes = [];
let usuarioEditandoId = null;
let doacaoEditandoId = null;

// NAVEGAÇÃO

const navItems = document.querySelectorAll(".nav-item");

const pages = {
    dashboard: document.getElementById("dashboardPage"),
    usuarios: document.getElementById("usuariosPage"),
    doacoes: document.getElementById("doacoesPage")
};

const pageNames = {
    dashboard: "Painel",
    usuarios: "Usuários",
    doacoes: "Doações"
};

navItems.forEach(item => {
    item.addEventListener("click", () => {
        const selectedPage = item.dataset.page;

        navItems.forEach(nav => nav.classList.remove("active"));
        item.classList.add("active");

        Object.entries(pages).forEach(([name, page]) => {
            page.hidden = name !== selectedPage;
        });

        document.getElementById("pageTitle").textContent =
            selectedPage === "dashboard" ? "Painel geral" : pageNames[selectedPage];

        document.getElementById("breadcrumbPage").textContent =
            pageNames[selectedPage];

        document.getElementById("sidebar").classList.remove("open");

        if (selectedPage === "usuarios") carregarUsuarios();
        if (selectedPage === "doacoes") carregarDoacoes();
        if (selectedPage === "dashboard") carregarPainel();
    });
});

document.getElementById("mobileMenu").addEventListener("click", () => {
    document.getElementById("sidebar").classList.toggle("open");
});

// REQUISIÇÕES HTTP

async function requisicao(url, opcoes = {}) {
    const response = await fetch(url, opcoes);

    if (!response.ok) {
        let mensagem = `Erro HTTP ${response.status}`;

        try {
            const texto = await response.text();

            if (texto) {
                try {
                    const dados = JSON.parse(texto);
                    mensagem = dados.message || dados.error || texto;
                } catch {
                    mensagem = texto;
                }
            }
        } catch {
            // Mantém a mensagem HTTP original.
        }

        throw new Error(mensagem);
    }

    if (response.status === 204) return null;

    const texto = await response.text();
    return texto ? JSON.parse(texto) : null;
}

function opcoesJson(metodo, dados) {
    return {
        method: metodo,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(dados)
    };
}

// UTILITÁRIOS

function mostrarToast(mensagem, tipo = "success") {
    const container = document.getElementById("toastContainer");
    const toast = document.createElement("div");

    toast.className = `toast ${tipo}`;
    toast.textContent = mensagem;
    container.appendChild(toast);

    setTimeout(() => toast.remove(), 4000);
}

function escaparHtml(valor) {
    return String(valor ?? "").replace(/[&<>"']/g, caractere => {
        const entidades = {
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': "&quot;",
            "'": "&#039;"
        };

        return entidades[caractere];
    });
}

function iniciais(nome) {
    return String(nome || "?")
        .trim()
        .split(/\s+/)
        .slice(0, 2)
        .map(parte => parte.charAt(0).toUpperCase())
        .join("");
}

function obterUsuario(id) {
    return usuarios.find(usuario => String(usuario.id) === String(id));
}

function nomeUsuario(id) {
    return obterUsuario(id)?.nome || "Não informado";
}

// PAINEL

async function carregarPainel() {
    try {
        const [listaUsuarios, listaDoacoes] = await Promise.all([
            requisicao(API.usuarios),
            requisicao(API.doacoes)
        ]);

        document.getElementById("totalUsuarios").textContent = listaUsuarios.length;
        document.getElementById("totalDoacoes").textContent = listaDoacoes.length;

        document.getElementById("totalPendentes").textContent =
            listaDoacoes.filter(d => d.status === "PENDENTE").length;

        document.getElementById("totalConcluidas").textContent =
            listaDoacoes.filter(d => d.status === "CONCLUIDA").length;

        carregarStatus(listaDoacoes);

    } catch (error) {
        console.error("Erro ao carregar painel:", error);

        ["totalUsuarios", "totalDoacoes", "totalPendentes", "totalConcluidas"]
            .forEach(id => document.getElementById(id).textContent = "—");

        document.getElementById("statusList").innerHTML =
            `<p class="empty-message">Não foi possível carregar os dados. Verifique o backend.</p>`;
    }
}

function carregarStatus(listaDoacoes) {
    const statusList = document.getElementById("statusList");
    const total = listaDoacoes.length;

    if (total === 0) {
        statusList.innerHTML =
            `<p class="empty-message">Ainda não existem doações cadastradas.</p>`;
        return;
    }

    statusList.innerHTML = Object.entries(statusConfig).map(([status, config]) => {
        const quantidade = listaDoacoes.filter(d => d.status === status).length;
        const porcentagem = quantidade / total * 100;

        return `
            <div class="status-row">
                <div class="status-row-top">
                    <span>${config.label}</span>
                    <strong>${quantidade} (${porcentagem.toFixed(0)}%)</strong>
                </div>
                <div class="progress-track">
                    <div class="progress-fill"
                         style="width:${porcentagem}%;background:${config.color}"></div>
                </div>
            </div>
        `;
    }).join("");
}

// USUÁRIOS

async function carregarUsuarios() {
    const tbody = document.getElementById("usuariosTableBody");

    tbody.innerHTML =
        `<tr><td colspan="5" class="table-message">Carregando usuários...</td></tr>`;

    try {
        usuarios = await requisicao(API.usuarios);
        atualizarResumoUsuarios();
        renderizarUsuarios();
    } catch (error) {
        console.error("Erro ao carregar usuários:", error);

        tbody.innerHTML = `
            <tr><td colspan="5" class="table-message">
                Não foi possível carregar os usuários: ${escaparHtml(error.message)}
            </td></tr>
        `;
        document.getElementById("usuariosResultado").textContent = "Erro ao carregar.";
    }
}

function atualizarResumoUsuarios() {
    document.getElementById("usuariosTotal").textContent = usuarios.length;

    document.getElementById("usuariosDoadores").textContent =
        usuarios.filter(u => u.tipoUsuario === "DOADOR").length;

    document.getElementById("usuariosResponsaveis").textContent =
        usuarios.filter(u => u.tipoUsuario === "RESPONSAVEL").length;
}

function renderizarUsuarios() {
    const tbody = document.getElementById("usuariosTableBody");
    const pesquisa = document.getElementById("pesquisaUsuario").value.trim().toLowerCase();
    const filtro = document.getElementById("filtroTipoUsuario").value;

    const filtrados = usuarios.filter(usuario => {
        const nome = (usuario.nome || "").toLowerCase();
        const email = (usuario.email || "").toLowerCase();

        return (nome.includes(pesquisa) || email.includes(pesquisa)) &&
            (!filtro || usuario.tipoUsuario === filtro);
    });

    document.getElementById("usuariosResultado").textContent =
        `Exibindo ${filtrados.length} de ${usuarios.length} usuário(s)`;

    if (filtrados.length === 0) {
        tbody.innerHTML =
            `<tr><td colspan="5" class="table-message">Nenhum usuário encontrado.</td></tr>`;
        return;
    }

    tbody.innerHTML = filtrados.map(usuario => {
        const tipo = usuario.tipoUsuario;
        const tipoLabel = tipo === "DOADOR" ? "Doador" : "Responsável";
        const classeTipo = tipo === "DOADOR" ? "doador" : "responsavel";

        return `
            <tr>
                <td>
                    <div class="user-name-cell">
                        <div class="user-avatar">${escaparHtml(iniciais(usuario.nome))}</div>
                        <span class="user-name">${escaparHtml(usuario.nome)}</span>
                    </div>
                </td>
                <td><span class="user-email">${escaparHtml(usuario.email)}</span></td>
                <td>${escaparHtml(usuario.telefone)}</td>
                <td><span class="user-type ${classeTipo}">${tipoLabel}</span></td>
                <td>
                    <div class="action-buttons">
                        <button class="icon-button" title="Editar" data-action="editar-usuario"
                            data-id="${escaparHtml(usuario.id)}">✎</button>
                        <button class="icon-button delete" title="Excluir" data-action="excluir-usuario"
                            data-id="${escaparHtml(usuario.id)}">⌫</button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

document.getElementById("pesquisaUsuario").addEventListener("input", renderizarUsuarios);
document.getElementById("filtroTipoUsuario").addEventListener("change", renderizarUsuarios);
document.getElementById("btnAtualizarUsuarios").addEventListener("click", carregarUsuarios);


function abrirModalUsuario(usuario = null) {
    document.getElementById("usuarioForm").reset();
    usuarioEditandoId = usuario ? usuario.id : null;

    // Placeholders dos campos
    document.getElementById("nomeUsuario").placeholder = "Ex.: João Silva";
    document.getElementById("emailUsuario").placeholder = "Ex.: joao@email.com";
    document.getElementById("telefoneUsuario").placeholder = "Ex.: (44) 99999-9999";

    document.getElementById("modalTitulo").textContent =
        usuario ? "Editar usuário" : "Novo usuário";

    document.getElementById("btnSalvarUsuario").textContent =
        usuario ? "Salvar alterações" : "Salvar usuário";

    if (usuario) {
        document.getElementById("nomeUsuario").value = usuario.nome || "";
        document.getElementById("emailUsuario").value = usuario.email || "";
        document.getElementById("telefoneUsuario").value = usuario.telefone || "";
        document.getElementById("tipoUsuario").value = usuario.tipoUsuario || "";
    }

    document.getElementById("usuarioModal").hidden = false;
    document.getElementById("nomeUsuario").focus();
}

function fecharModalUsuario() {
    document.getElementById("usuarioModal").hidden = true;
    document.getElementById("usuarioForm").reset();
    usuarioEditandoId = null;
}

document.getElementById("btnNovoUsuario").addEventListener("click", () => abrirModalUsuario());
document.getElementById("btnFecharModal").addEventListener("click", fecharModalUsuario);
document.getElementById("btnCancelarModal").addEventListener("click", fecharModalUsuario);

document.getElementById("usuarioModal").addEventListener("click", event => {
    if (event.target.id === "usuarioModal") fecharModalUsuario();
});

document.getElementById("usuarioForm").addEventListener("submit", async event => {
    event.preventDefault();

    const usuario = {
        nome: document.getElementById("nomeUsuario").value.trim(),
        email: document.getElementById("emailUsuario").value.trim(),
        telefone: document.getElementById("telefoneUsuario").value.trim(),
        tipoUsuario: document.getElementById("tipoUsuario").value
    };

    const botao = document.getElementById("btnSalvarUsuario");
    botao.disabled = true;

    try {
        if (usuarioEditandoId) {
            await requisicao(
                `${API.usuarios}/${encodeURIComponent(usuarioEditandoId)}`,
                opcoesJson("PUT", usuario)
            );
            mostrarToast("Usuário atualizado com sucesso!");
        } else {
            await requisicao(API.usuarios, opcoesJson("POST", usuario));
            mostrarToast("Usuário cadastrado com sucesso!");
        }

        fecharModalUsuario();
        await carregarUsuarios();
        await carregarPainel();
    } catch (error) {
        console.error(error);
        mostrarToast(error.message || "Não foi possível salvar o usuário.", "error");
    } finally {
        botao.disabled = false;
        botao.textContent = usuarioEditandoId ? "Salvar alterações" : "Salvar usuário";
    }
});

document.getElementById("usuariosTableBody").addEventListener("click", async event => {
    const botao = event.target.closest("button[data-action]");
    if (!botao) return;

    const usuario = usuarios.find(u => String(u.id) === String(botao.dataset.id));
    if (!usuario) return;

    if (botao.dataset.action === "editar-usuario") {
        abrirModalUsuario(usuario);
        return;
    }

    if (botao.dataset.action === "excluir-usuario") {
        if (!confirm(`Tem certeza que deseja excluir "${usuario.nome}"?`)) return;

        try {
            await requisicao(`${API.usuarios}/${encodeURIComponent(usuario.id)}`, {
                method: "DELETE"
            });

            mostrarToast("Usuário excluído com sucesso!");
            await carregarUsuarios();
            await carregarPainel();
        } catch (error) {
            mostrarToast(error.message || "Não foi possível excluir o usuário.", "error");
        }
    }
});

// DOAÇÕES

async function carregarDoacoes() {
    const tbody = document.getElementById("doacoesTableBody");

    tbody.innerHTML =
        `<tr><td colspan="6" class="table-message">Carregando doações...</td></tr>`;

    try {
        const [listaDoacoes, listaUsuarios] = await Promise.all([
            requisicao(API.doacoes),
            requisicao(API.usuarios)
        ]);

        doacoes = listaDoacoes;
        usuarios = listaUsuarios;

        atualizarResumoDoacoes();
        renderizarDoacoes();

    } catch (error) {
        console.error("Erro ao carregar doações:", error);

        tbody.innerHTML = `
            <tr><td colspan="6" class="table-message">
                Não foi possível carregar as doações: ${escaparHtml(error.message)}
            </td></tr>
        `;
        document.getElementById("doacoesResultado").textContent = "Erro ao carregar.";
    }
}

function atualizarResumoDoacoes() {
    document.getElementById("doacoesTotal").textContent = doacoes.length;

    document.getElementById("doacoesPendentes").textContent =
        doacoes.filter(d => d.status === "PENDENTE").length;

    document.getElementById("doacoesConcluidas").textContent =
        doacoes.filter(d => d.status === "CONCLUIDA").length;
}

function formatarData(data) {
    if (!data) return "—";

    const partes = String(data).split("-");
    if (partes.length !== 3) return escaparHtml(data);

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

function renderizarDoacoes() {
    const tbody = document.getElementById("doacoesTableBody");
    const pesquisa = document.getElementById("pesquisaDoacao").value.trim().toLowerCase();
    const filtro = document.getElementById("filtroStatusDoacao").value;

    const filtradas = doacoes.filter(doacao => {
        const nomeDoador = nomeUsuario(doacao.doadorId).toLowerCase();

        return nomeDoador.includes(pesquisa) &&
            (!filtro || doacao.status === filtro);
    });

    document.getElementById("doacoesResultado").textContent =
        `Exibindo ${filtradas.length} de ${doacoes.length} doação(ões)`;

    if (filtradas.length === 0) {
        tbody.innerHTML =
            `<tr><td colspan="6" class="table-message">Nenhuma doação encontrada.</td></tr>`;
        return;
    }

    tbody.innerHTML = filtradas.map(doacao => {
        const config = statusConfig[doacao.status] || {
            label: doacao.status || "Sem status",
            classe: ""
        };

        const quantidadeRoupas = Array.isArray(doacao.roupas)
            ? doacao.roupas.length
            : 0;

        return `
            <tr>
                <td>
                    <div class="user-name-cell">
                        <div class="user-avatar">${escaparHtml(iniciais(nomeUsuario(doacao.doadorId)))}</div>
                        <span class="user-name">${escaparHtml(nomeUsuario(doacao.doadorId))}</span>
                    </div>
                </td>
                <td>${escaparHtml(doacao.responsavelId ? nomeUsuario(doacao.responsavelId) : "Não atribuído")}</td>
                <td>${formatarData(doacao.data)}</td>
                <td><span class="donation-count">${quantidadeRoupas} peça(s)</span></td>
                <td><span class="status-badge ${config.classe}">${escaparHtml(config.label)}</span></td>
                <td>
                    <div class="action-buttons">
                        <button class="icon-button" title="Editar" data-action="editar-doacao"
                            data-id="${escaparHtml(doacao.id)}">✎</button>
                        <button class="icon-button delete" title="Excluir" data-action="excluir-doacao"
                            data-id="${escaparHtml(doacao.id)}">⌫</button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

document.getElementById("pesquisaDoacao").addEventListener("input", renderizarDoacoes);
document.getElementById("filtroStatusDoacao").addEventListener("change", renderizarDoacoes);
document.getElementById("btnAtualizarDoacoes").addEventListener("click", carregarDoacoes);

// PREENCHER AS LISTAS DE DOADOR E RESPONSÁVEL

function preencherSeletoresUsuarios() {
    const seletorDoador = document.getElementById("doacaoDoador");
    const seletorResponsavel = document.getElementById("doacaoResponsavel");

    const doadores = usuarios.filter(u => u.tipoUsuario === "DOADOR");
    const responsaveis = usuarios.filter(u => u.tipoUsuario === "RESPONSAVEL");

    seletorDoador.innerHTML =
        `<option value="">Selecione um doador</option>` +
        doadores.map(u =>
            `<option value="${escaparHtml(u.id)}">${escaparHtml(u.nome)}</option>`
        ).join("");

    seletorResponsavel.innerHTML =
        `<option value="">Nenhum responsável</option>` +
        responsaveis.map(u =>
            `<option value="${escaparHtml(u.id)}">${escaparHtml(u.nome)}</option>`
        ).join("");
}

// ADICIONAR E REMOVER ROUPAS DO FORMULÁRIO

function adicionarRoupa(roupa = {}) {
    const lista = document.getElementById("listaRoupas");
    const numero = lista.children.length + 1;

    const card = document.createElement("div");
    card.className = "clothing-card";

    card.innerHTML = `
        <div class="clothing-card-header">
            <strong>Peça ${numero}</strong>
            <button type="button" class="remove-clothing" title="Remover roupa">×</button>
        </div>

        <div class="clothing-fields">
            <div class="form-group">
                <label>Tipo de roupa</label>
                <input type="text" class="roupa-tipo" maxlength="60"
                    placeholder="Ex.: Camiseta" required>
            </div>

            <div class="form-group">
                <label>Tamanho</label>
                <input type="text" class="roupa-tamanho" maxlength="20"
                    placeholder="Ex.: M, G, 40" required>
            </div>

            <div class="form-group">
                <label>Cor</label>
                <input type="text" class="roupa-cor" maxlength="40"
                    placeholder="Ex.: Azul" required>
            </div>

            <div class="form-group">
                <label>Estado de conservação</label>
                <input type="text" class="roupa-estado" maxlength="60"
                    placeholder="Ex.: Bom estado" required>
            </div>
        </div>
    `;

    card.querySelector(".roupa-tipo").value = roupa.tipo || "";
    card.querySelector(".roupa-tamanho").value = roupa.tamanho || "";
    card.querySelector(".roupa-cor").value = roupa.cor || "";
    card.querySelector(".roupa-estado").value = roupa.estado || "";

    card.querySelector(".remove-clothing").addEventListener("click", () => {
        if (lista.children.length <= 1) {
            mostrarToast("A doação precisa ter pelo menos uma roupa.", "error");
            return;
        }

        card.remove();
        atualizarNumeracaoRoupas();
    });

    lista.appendChild(card);
}

function atualizarNumeracaoRoupas() {
    document.querySelectorAll(".clothing-card").forEach((card, index) => {
        card.querySelector(".clothing-card-header strong").textContent =
            `Peça ${index + 1}`;
    });
}

function obterRoupasFormulario() {
    return Array.from(document.querySelectorAll(".clothing-card")).map(card => ({
        tipo: card.querySelector(".roupa-tipo").value.trim(),
        tamanho: card.querySelector(".roupa-tamanho").value.trim(),
        cor: card.querySelector(".roupa-cor").value.trim(),
        estado: card.querySelector(".roupa-estado").value.trim()
    }));
}

// ABRIR E FECHAR MODAL DE DOAÇÃO

async function abrirModalDoacao(doacao = null) {
    try {
        usuarios = await requisicao(API.usuarios);
        preencherSeletoresUsuarios();

        document.getElementById("doacaoForm").reset();
        document.getElementById("listaRoupas").innerHTML = "";

        doacaoEditandoId = doacao ? doacao.id : null;

        document.getElementById("doacaoModalTitulo").textContent =
            doacao ? "Editar doação" : "Nova doação";

        document.getElementById("btnSalvarDoacao").textContent =
            doacao ? "Salvar alterações" : "Salvar doação";

        document.getElementById("grupoStatusDoacao").hidden = !doacao;

        if (doacao) {
            document.getElementById("doacaoDoador").value =
                doacao.doadorId || "";

            document.getElementById("doacaoResponsavel").value =
                doacao.responsavelId || "";

            document.getElementById("doacaoStatus").value =
                doacao.status || "PENDENTE";

            (doacao.roupas || []).forEach(roupa => adicionarRoupa(roupa));
        } else {
            adicionarRoupa();
        }

        document.getElementById("doacaoModal").hidden = false;

    } catch (error) {
        console.error(error);
        mostrarToast("Não foi possível carregar os usuários para o formulário.", "error");
    }
}

function fecharModalDoacao() {
    document.getElementById("doacaoModal").hidden = true;
    document.getElementById("doacaoForm").reset();
    document.getElementById("listaRoupas").innerHTML = "";
    doacaoEditandoId = null;
}

document.getElementById("btnNovaDoacao").addEventListener("click", () => abrirModalDoacao());
document.getElementById("btnFecharDoacaoModal").addEventListener("click", fecharModalDoacao);
document.getElementById("btnCancelarDoacao").addEventListener("click", fecharModalDoacao);
document.getElementById("btnAdicionarRoupa").addEventListener("click", () => adicionarRoupa());

document.getElementById("doacaoModal").addEventListener("click", event => {
    if (event.target.id === "doacaoModal") fecharModalDoacao();
});

// SALVAR DOAÇÃO

document.getElementById("doacaoForm").addEventListener("submit", async event => {
    event.preventDefault();

    const roupas = obterRoupasFormulario();

    if (roupas.length === 0) {
        mostrarToast("Adicione pelo menos uma roupa à doação.", "error");
        return;
    }

    const doacao = {
        doadorId: document.getElementById("doacaoDoador").value,
        responsavelId: document.getElementById("doacaoResponsavel").value || null,
        roupas: roupas
    };

    if (doacaoEditandoId) {
        doacao.status = document.getElementById("doacaoStatus").value;
    }

    if (!doacao.doadorId) {
        mostrarToast("Selecione um doador.", "error");
        return;
    }

    if (roupas.some(roupa =>
        !roupa.tipo || !roupa.tamanho || !roupa.cor || !roupa.estado
    )) {
        mostrarToast("Preencha todos os campos de todas as roupas.", "error");
        return;
    }

    const botao = document.getElementById("btnSalvarDoacao");
    botao.disabled = true;
    botao.textContent = "Salvando...";

    try {
        if (doacaoEditandoId) {
            await requisicao(
                `${API.doacoes}/${encodeURIComponent(doacaoEditandoId)}`,
                opcoesJson("PUT", doacao)
            );

            mostrarToast("Doação atualizada com sucesso!");
        } else {
            await requisicao(API.doacoes, opcoesJson("POST", doacao));
            mostrarToast("Doação cadastrada com sucesso!");
        }

        fecharModalDoacao();

        await carregarDoacoes();
        await carregarPainel();

    } catch (error) {
        console.error("Erro ao salvar doação:", error);
        mostrarToast(error.message || "Não foi possível salvar a doação.", "error");
    } finally {
        botao.disabled = false;
        botao.textContent = doacaoEditandoId ? "Salvar alterações" : "Salvar doação";
    }
});

// AÇÕES DA TABELA DE DOAÇÕES

document.getElementById("doacoesTableBody").addEventListener("click", async event => {
    const botao = event.target.closest("button[data-action]");
    if (!botao) return;

    const doacao = doacoes.find(d => String(d.id) === String(botao.dataset.id));
    if (!doacao) return;

    if (botao.dataset.action === "editar-doacao") {
        await abrirModalDoacao(doacao);
        return;
    }

    if (botao.dataset.action === "excluir-doacao") {
        if (!confirm("Tem certeza que deseja excluir esta doação?")) return;

        try {
            await requisicao(
                `${API.doacoes}/${encodeURIComponent(doacao.id)}`,
                { method: "DELETE" }
            );

            mostrarToast("Doação excluída com sucesso!");
            await carregarDoacoes();
            await carregarPainel();

        } catch (error) {
            console.error(error);
            mostrarToast(error.message || "Não foi possível excluir a doação.", "error");
        }
    }
});

// FECHAR MODAIS COM ESC

document.addEventListener("keydown", event => {
    if (event.key === "Escape") {
        fecharModalUsuario();
        fecharModalDoacao();
    }
});

// INICIAR

carregarPainel();
