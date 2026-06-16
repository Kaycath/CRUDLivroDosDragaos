package com.Interface;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;

/**
 * CLASSE CONTROLADORA (Controller do padrão arquitetural MVC).
 * Esta classe é responsável por gerenciar a lógica de interação da interface gráfica.
 * Ela captura as ações do usuário (como cliques em botões ou seleção de linhas),
 * valida os dados de entrada e aciona a camada de persistência (DAO).
 */
public class MainController {

    // --- MAPEAMENTO DOS COMPONENTES VISUAIS (Injetados via FXML) ---

    @FXML private TextField textId;            // Caixa de texto que retém o ID do dragão selecionado (Apenas Leitura)
    @FXML private TextField textEspecie;       // Campo de entrada de texto para o nome da espécie
    @FXML private TextField textClasse;        // Campo de entrada de texto para a categoria do dragão
    @FXML private TextField textDescricao;     // Campo de entrada de texto para o comportamento do dragão
    @FXML private TextField textEspecialidade; // Campo de entrada de texto para a habilidade suprema

    @FXML private Button btnAdicionar;         // Gatilho para criar um novo registro no banco de dados
    @FXML private Button btnSalvar;            // Gatilho para atualizar um registro existente na base de dados
    @FXML private Button btnBuscar;            // Gatilho para deletar um registro com base no ID visível na tela
    @FXML private Button btnLimpar;            // Gatilho para esvaziar os campos e resetar o estado da aplicação

    @FXML private Label lblStatus;             // Rótulo para fornecer feedbacks em tempo real (Erros, Sucessos, Alertas)
    @FXML private Label lblContador;           // Rótulo dinâmico posicionado na base para exibir o total de linhas do banco

    @FXML private TableView<DragaoDTO> tblLivroDosDragaos; // Grade visual que gerencia o mapeamento de objetos em linhas
    @FXML private TableColumn<DragaoDTO, Integer> colId;   // Coluna específica para renderizar números inteiros de ID
    @FXML private TableColumn<DragaoDTO, String> colEspecie; // Coluna específica para renderizar textos de espécies
    @FXML private TableColumn<DragaoDTO, String> colClasse;  // Coluna específica para renderizar textos de classes
    @FXML private TableColumn<DragaoDTO, String> colDescricao; // Coluna específica para renderizar textos de descrições
    @FXML private TableColumn<DragaoDTO, String> colEspecialidade; // Coluna específica para renderizar textos de habilidades

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colClasse.setCellValueFactory(new PropertyValueFactory<>("classe"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        if (colEspecialidade != null) {
            colEspecialidade.setCellValueFactory(new PropertyValueFactory<>("especialidade"));
        }

        // UX: Bloqueia os botões inicialmente
        btnSalvar.setDisable(true);
        btnBuscar.setDisable(true);

        // Alimenta a tabela
        carregarDragoes();

        // ----------------------------------------------------------------------------------
        // UI (Layout): CUSTOMIZAÇÃO DE SELEÇÃO E CORES DA TABELA
        // ----------------------------------------------------------------------------------
        tblLivroDosDragaos.setRowFactory(tv -> {
            TableRow<DragaoDTO> row = new TableRow<>();
            row.itemProperty().addListener((obs, antigo, novo) -> {
                if (novo != null) {
                    // Estilo padrão da linha medieval
                    row.setStyle("-fx-background-color: #dfc8a5; -fx-text-background-color: #2b1d12;");
                }
            });

            // Quando a linha for selecionada, ela ganha um fundo Ouro Velho com texto escuro destacado
            row.selectedProperty().addListener((obs, foiSelecionado, selecionadoAgora) -> {
                if (selecionadoAgora) {
                    row.setStyle("-fx-background-color: #b58d3d; -fx-text-background-color: #ffffff; -fx-font-weight: bold;");
                } else {
                    row.setStyle("-fx-background-color: #dfc8a5; -fx-text-background-color: #2b1d12; -fx-font-weight: normal;");
                }
            });
            return row;
        });
    }



    /**
     * ATUALIZAÇÃO DA GRADE VISUAL.
     * Instancia a camada de banco de dados, solicita o ArrayList atualizado e o converte
     * para uma estrutura ObservableList aceita nativamente pelo JavaFX.
     */
    @FXML
    private void carregarDragoes() {
        // Instanciação local da camada de modelo (DAO) para evitar acoplamento duradouro na memória
        DragaoDAO objDragaoDAO = new DragaoDAO();
        ArrayList<DragaoDTO> listaDragoes = objDragaoDAO.listarDragoes();

        // Converte a coleção pura do Java em uma coleção observável para refletir as linhas dinamicamente na tela
        tblLivroDosDragaos.setItems(FXCollections.observableArrayList(listaDragoes));

        // UX (Usabilidade): Calcula dinamicamente o total de elementos da lista e atualiza o rótulo informativo
        lblContador.setText("Total de Dragões Catalogados: " + listaDragoes.size());
    }

    /**
     * CAPTURA DE SELEÇÃO (On Mouse Clicked).
     * Disparado quando o usuário clica sobre qualquer registro exposto na tabela gráfica.
     * Transfere os dados da linha selecionada direto para os inputs, eliminando variáveis globais.
     */
    @FXML
    private void carregarCampos() {
        // Coleta o objeto DTO mapeado especificamente na linha que recebeu o foco do clique
        DragaoDTO objDragaoDTO = tblLivroDosDragaos.getSelectionModel().getSelectedItem();

        // Executa o bloco condicional apenas se o usuário não tiver clicado em uma zona vazia da tabela
        if (objDragaoDTO != null) {
            // Converte o valor inteiro do ID para representação textual String e popula o campo bloqueado
            textId.setText(String.valueOf(objDragaoDTO.getId()));

            // Popula os demais inputs com as strings recuperadas do banco de dados
            textEspecie.setText(objDragaoDTO.getEspecie());
            textClasse.setText(objDragaoDTO.getClasse());
            textDescricao.setText(objDragaoDTO.getDescricao());
            textEspecialidade.setText(objDragaoDTO.getEspecialidade());

            // UX (Usabilidade): Ativa os botões de edição e exclusão na tela, agora que há um alvo definido
            btnSalvar.setDisable(false);
            btnBuscar.setDisable(false);

            // UI (Interface): Atualiza o rótulo de feedback com instruções em cor neutra/padrão
            lblStatus.setText("Registro ID " + objDragaoDTO.getId() + " carregado. Pronto para alterações.");
            lblStatus.setStyle("-fx-text-fill: #dfc8a5; -fx-font-style: italic;");
        }
    }

    /**
     * OPERAÇÃO DE CADASTRO (C - Create).
     * Acionada no evento de clique do botão 'Adicionar Dragão'.
     * Valida as restrições obrigatórias e invoca a persistência de inserção.
     */
    @FXML
    private void btnAdicionar(ActionEvent event) {
        // Resgata o texto inserido removendo espaços sobressalentes nas pontas (.trim())
        String especie = textEspecie.getText().trim();
        String classse = textClasse.getText().trim();

        // UX (Usabilidade): Valida regras de negócio básicas diretamente na interface para poupar processamento no SGBD
        if (especie.isEmpty() || classse.isEmpty()) {
            // UI (Interface): Informa o erro trocando a folha de estilo inline para vermelho vivo
            lblStatus.setText("Erro: Os campos marcados com (*) são de preenchimento obrigatório!");
            lblStatus.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            return; // Interrompe o método de forma prematura impedindo o envio do formulário vazio
        }

        // Empacota os dados das caixas em uma nova instância limpa de DTO (Objeto de Transferência)
        DragaoDTO objDragaoDTO = new DragaoDTO();
        objDragaoDTO.setEspecie(especie);
        objDragaoDTO.setClasse(classse);
        objDragaoDTO.setDescricao(textDescricao.getText());
        objDragaoDTO.setEspecialidade(textEspecialidade.getText());

        // Despacha a entidade para a execução do INSERT no banco de dados
        DragaoDAO objDragaoDAO = new DragaoDAO();
        objDragaoDAO.adicionarDragao(objDragaoDTO);

        // Atualiza a sincronia dos dados na tabela para exibir o novo dragão imediatamente
        carregarDragoes();

        // Limpa o formulário preparando-o para novos fluxos de digitação
        btnLimpar(event);

        // UI (Interface): Sinaliza a conclusão positiva alterando o rótulo inline para verde
        lblStatus.setText("Sucesso: Novo dragão inserido nas crônicas!");
        lblStatus.setStyle("-fx-text-fill: #81c784; -fx-font-weight: bold;");
    }

    /**
     * OPERAÇÃO DE ATUALIZAÇÃO (U - Update).
     * Acionada no clique do botão 'Salvar Alterações'.
     * Utiliza o ID fixado no campo de texto para orientar a modificação física.
     */
    @FXML
    private void btnSalvar(ActionEvent event) {
        // Cláusula de segurança: Aborta o fluxo caso o campo de texto do ID esteja nulo/vazio
        if (textId.getText().isEmpty()) return;

        String especie = textEspecie.getText().trim();
        String classe = textClasse.getText().trim();

        // UX (Usabilidade): Impede que o usuário limpe os campos de uma linha existente e tente salvar dados nulos
        if (especie.isEmpty() || classe.isEmpty()) {
            lblStatus.setText("Erro: Espécie e Classe não podem ficar vazias durante a alteração!");
            lblStatus.setStyle("-fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            return; //void, nao retorna nada
        }

        // Converte a String do ID de volta para inteiro para passá-la como parâmetro de chave primária
        int id = Integer.parseInt(textId.getText());

        // Agrupa os valores atualizados das caixas de texto no DTO
        DragaoDTO objDragaoDTO = new DragaoDTO();
        objDragaoDTO.setId(id);
        objDragaoDTO.setEspecie(especie);
        objDragaoDTO.setClasse(classe);
        objDragaoDTO.setDescricao(textDescricao.getText());
        objDragaoDTO.setEspecialidade(textEspecialidade.getText());

        // Dispara o comando UPDATE da camada de acesso a dados
        DragaoDAO objDragaoDAO = new DragaoDAO();
        objDragaoDAO.alterarDragao(objDragaoDTO);

        // Atualiza a tabela gráfica com os novos estados textuais
        carregarDragoes();
        btnLimpar(event);

        lblStatus.setText("Sucesso: Registro de dragão modificado com êxito!");
        lblStatus.setStyle("-fx-text-fill: #81c784; -fx-font-weight: bold;");
    }

    /**
     * OPERAÇÃO DE EXCLUSÃO (D - Delete).
     * Vinculada ao botão 'Deletar Dragão' (Identificado no código herdado como btnBuscar).
     * Lê a chave primária exposta na tela para efetuar o comando DELETE.
     */
    @FXML
    private void btnBuscar(ActionEvent event) {
        // Cláusula de segurança: Bloqueia a exclusão se não houver um ID ativo carregado na interface
        if (textId.getText().isEmpty()) return;

        // Recupera o ID numérico diretamente da caixa de texto do layout
        int id = Integer.parseInt(textId.getText());

        // Repassa a chave primária para a remoção física no banco de dados
        DragaoDAO objDragaoDAO = new DragaoDAO();
        objDragaoDAO.deletarDragao(id);

        // Sincroniza e reseta a tela
        carregarDragoes();
        btnLimpar(event);

        lblStatus.setText("Sucesso: O dragão foi removido do livro oficial.");
        lblStatus.setStyle("-fx-text-fill: #81c784; -fx-font-weight: bold;");
    }

    /**
     * RESET DA INTERFACE (Limpeza).
     * Restaura as caixas de entrada para o estado vazio, desmarca a seleção da tabela,
     * re-bloqueia botões inválidos e força a ancoragem do cursor de digitação.
     */
    @FXML
    private void btnLimpar(ActionEvent event) {
        // Apaga o conteúdo textual contido em todas as caixas de texto do layout
        textId.clear();
        textEspecie.clear();
        textClasse.clear();
        textDescricao.clear();
        textEspecialidade.clear();

        // Limpa qualquer linha que estivesse marcada com a cor azul de seleção na tabela
        tblLivroDosDragaos.getSelectionModel().clearSelection();

        // UX (Usabilidade): Desabilita os botões de ação estrutural (Update/Delete) pois o formulário voltou a ficar vazio
        btnSalvar.setDisable(true);
        btnBuscar.setDisable(true);

        // UX (Usabilidade): Joga o foco do teclado (cursor piscando) direto no primeiro campo.
        // Permite preencher novos registros imediatamente utilizando a navegação por TAB, sem necessitar de cliques de mouse.
        textEspecie.requestFocus();
    }

    // ----------------------------------------------------------------------------------
    // UI (Layout): MÉTODOS DE HOVER (Efeito Iluminação ao Passar o Mouse nos Botões)
    // ----------------------------------------------------------------------------------

    @FXML
    private void btnAdicionarHover() {
        btnAdicionar.setStyle("-fx-background-color: #637a68; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: #e6c687; -fx-border-radius: 20; -fx-border-width: 2;");
    }
    @FXML
    private void btnAdicionarExit() {
        btnAdicionar.setStyle("-fx-background-color: #4a5d4e; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: #637a68; -fx-border-radius: 20; -fx-border-width: 1.5;");
    }

    @FXML
    private void btnSalvarHover() {
        if (!btnSalvar.isDisable()) {
            btnSalvar.setStyle("-fx-background-color: #cfab5b; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: #e6c687; -fx-border-radius: 20; -fx-border-width: 2;");
        }
    }
    @FXML
    private void btnSalvarExit() {
        btnSalvar.setStyle("-fx-background-color: #b58d3d; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: #cfab5b; -fx-border-radius: 20; -fx-border-width: 1.5;");
    }

    @FXML
    private void btnDeletarHover() {
        if (!btnBuscar.isDisable()) {
            btnBuscar.setStyle("-fx-background-color: #9c3f34; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: #e6c687; -fx-border-radius: 20; -fx-border-width: 2;");
        }
    }
    @FXML
    private void btnDeletarExit() {
        btnBuscar.setStyle("-fx-background-color: #7c2d23; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: #9c3f34; -fx-border-radius: 20; -fx-border-width: 1.5;");
    }

    @FXML
    private void btnLimparHover() {
        btnLimpar.setStyle("-fx-background-color: #756b63; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: #e6c687; -fx-border-radius: 20; -fx-border-width: 2;");
    }
    @FXML
    private void btnLimparExit() {
        btnLimpar.setStyle("-fx-background-color: #5c534c; -fx-text-fill: white; -fx-background-radius: 20; -fx-cursor: hand; -fx-border-color: #756b63; -fx-border-radius: 20; -fx-border-width: 1.5;");
    }

}