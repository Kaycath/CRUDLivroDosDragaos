package com.Interface;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;

/**
 * Classe Controladora da Interface Gráfica.
 * Faz a mediação entre as ações do usuário na tela e as operações no banco de dados.
 */
public class MainController {

    // Vinculação dos campos de texto do arquivo FXML (para instanciar e injetar o botão de forma automática)
    @FXML private TextField textId; // Armazena o ID do registro selecionado para fins de consulta e alteração
    @FXML private TextField textEspecie;
    @FXML private TextField textClasse;
    @FXML private TextField textDescricao;
    @FXML private TextField textEspecialidade;

    // Vinculação dos botões da interface
    @FXML private Button btnAdicionar;
    @FXML private Button btnSalvar;
    @FXML private Button btnBuscar; // Atua no disparo da remoção de dados
    @FXML private Button btnLimpar;

    // Vinculação dos rótulos de identificação
    @FXML private Label lblEspecie;
    @FXML private Label lblClasse;
    @FXML private Label lblDescricao;
    @FXML private Label lblEspecialidade;

    // Vinculação da tabela de exibição e suas respectivas colunas
    @FXML private TableView<DragaoDTO> tblLivroDosDragaos;
    @FXML private TableColumn<DragaoDTO, Integer> colId;
    @FXML private TableColumn<DragaoDTO, String> colEspecie;
    @FXML private TableColumn<DragaoDTO, String> colClasse;
    @FXML private TableColumn<DragaoDTO, String> colDescricao;
    @FXML private TableColumn<DragaoDTO, String> colEspecialidade;

    /**
     * Método executado automaticamente pelo JavaFX assim que a tela é carregada.
     * Realiza a configuração inicial dos componentes.
     */
    @FXML
    private void initialize() {
        System.out.println("FXML carregado com sucesso!");

        // Mapeia cada coluna da tabela para ler o atributo correspondente dentro do objeto DragaoDTO
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colClasse.setCellValueFactory(new PropertyValueFactory<>("classe"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        if (colEspecialidade != null) {
            colEspecialidade.setCellValueFactory(new PropertyValueFactory<>("especialidade"));
        }

        // Faz a primeira carga de dados do banco para preencher a tabela visual
        carregarDragoes();
    }

    /**
     * Consulta a camada de persistência para obter a lista de dragões e atualiza a Tabela.
     */
    @FXML
    private void carregarDragoes() {
        DragaoDAO objDragaoDAO = new DragaoDAO();
        ArrayList<DragaoDTO> listaDragoes = objDragaoDAO.listarDragoes();

        // Converte o ArrayList padrão para uma lista observável do JavaFX, permitindo a renderização em tela
        tblLivroDosDragaos.setItems(FXCollections.observableArrayList(listaDragoes));
    }

    /**
     * Captura a linha selecionada na tabela e distribui seus valores nos campos de texto correspondentes.
     * Ativado através do clique do mouse sobre os registros da tabela.
     */
    @FXML
    private void carregarCampos() {
        // Obtém o objeto completo contido na linha selecionada pelo usuário
        DragaoDTO objDragaoDTO = tblLivroDosDragaos.getSelectionModel().getSelectedItem();

        // Garante que o processamento ocorra apenas se houver uma seleção válida
        if (objDragaoDTO != null) {
            // Repassa os valores das propriedades do objeto para as caixas de texto visuais
            textId.setText(String.valueOf(objDragaoDTO.getId()));
            textEspecie.setText(objDragaoDTO.getEspecie());
            textClasse.setText(objDragaoDTO.getClasse());
            textDescricao.setText(objDragaoDTO.getDescricao());
            textEspecialidade.setText(objDragaoDTO.getEspecialidade());
        }
    }

    /**
     * Captura os dados digitados nos campos de texto e realiza a inserção de um novo registro.
     */
    @FXML
    private void btnAdicionar(ActionEvent event) {
        // Coleta as informações textuais inseridas pelo usuário
        String especie = textEspecie.getText();
        String classe = textClasse.getText();
        String descricao = textDescricao.getText();
        String especialidade = textEspecialidade.getText();

        // Agrupa os dados coletados em uma única instância de transferência (DTO)
        DragaoDTO objDragaoDTO = new DragaoDTO();
        objDragaoDTO.setEspecie(especie);
        objDragaoDTO.setClasse(classe);
        objDragaoDTO.setDescricao(descricao);
        objDragaoDTO.setEspecialidade(especialidade);

        // Instancia o objeto de acesso a dados para disparar a gravação no banco
        DragaoDAO objDragaoDAO = new DragaoDAO();
        objDragaoDAO.adicionarDragao(objDragaoDTO);

        // Atualiza a visualização da tabela e limpa o formulário
        carregarDragoes();
        btnLimpar(event);
    }

    /**
     * Captura os dados dos campos de texto, incluindo o ID numérico, e executa a atualização.
     */
    @FXML
    private void btnSalvar(ActionEvent event) {
        // Impede o processamento se não houver um ID válido carregado na tela
        if (textId.getText().isEmpty()) return;

        // Recupera o ID da tela e converte para tipo inteiro, junto aos demais campos modificados
        int id = Integer.parseInt(textId.getText());
        String especie = textEspecie.getText();
        String classe = textClasse.getText();
        String descricao = textDescricao.getText();
        String especialidade = textEspecialidade.getText();

        // Estrutura o objeto DTO contendo a chave primária (ID) e as novas propriedades
        DragaoDTO objDragaoDTO = new DragaoDTO();
        objDragaoDTO.setId(id);
        objDragaoDTO.setEspecie(especie);
        objDragaoDTO.setClasse(classe);
        objDragaoDTO.setDescricao(descricao);
        objDragaoDTO.setEspecialidade(especialidade);

        // Encaminha a entidade estruturada para receber a atualização no banco de dados
        DragaoDAO objDragaoDAO = new DragaoDAO();
        objDragaoDAO.alterarDragao(objDragaoDTO);

        // Sincroniza a tabela e redefine os campos de texto
        carregarDragoes();
        btnLimpar(event);
    }

    /**
     * Recupera o identificador do registro atual e solicita a sua remoção definitiva.
     */
    @FXML
    private void btnBuscar(ActionEvent event) {
        // Bloqueia a execução se nenhum ID estiver presente na caixa de texto do formulário
        if (textId.getText().isEmpty()) return;

        // Lê o identificador numérico diretamente do campo correspondente da interface
        int id = Integer.parseInt(textId.getText());

        // Repassa a chave primária ao método responsável pela exclusão física
        DragaoDAO objDragaoDAO = new DragaoDAO();
        objDragaoDAO.deletarDragao(id);

        // Atualiza os dados expostos na interface gráfica
        carregarDragoes();
        btnLimpar(event);
    }

    /**
     * Redefine o estado visual limpando todos os campos de entrada e seleções ativas.
     */
    @FXML
    private void btnLimpar(ActionEvent event) {
        textId.clear();
        textEspecie.clear();
        textClasse.clear();
        textDescricao.clear();
        textEspecialidade.clear();

        // Remove qualquer linha que tenha permanecido com o foco ou marcação visual na tabela
        tblLivroDosDragaos.getSelectionModel().clearSelection();
    }
}