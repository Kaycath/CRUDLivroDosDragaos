package com.Interface;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class MainController
{

    @FXML private Button btnAdicionar;
    @FXML private Button btnBuscar;
    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;

    @FXML private TextField textEspecie;
    @FXML private TextField textClasse;
    @FXML private TextField textDescricao;
    @FXML private TextField textEspecialidade;

    @FXML private Label lblEspecie;
    @FXML private Label lblClasse;
    @FXML private Label lblDescricao;
    @FXML private Label lblEspecialidade;


    @FXML private TableView<DragaoDTO> tblLivroDosDragoes;
    @FXML private TableColumn<DragaoDTO, Integer> colId;
    @FXML private TableColumn<DragaoDTO, String> colEspecie;
    @FXML private TableColumn<DragaoDTO, String> colClasse;
    @FXML private TableColumn<DragaoDTO, String> colDescricao;

    @FXML
    private void initialize()
    {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colClasse.setCellValueFactory(new PropertyValueFactory<>("classe"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        carregarDragoes();
    }

    @FXML
    private void carregarDragoes(){
        DragaoDAO objDragaoDAO = new DragaoDAO();
        List<DragaoDTO> listaDragoes = objDragaoDAO.listarDragoes();
        tblLivroDosDragoes.setItems(FXCollections.observableArrayList(listaDragoes));
    }

    private void btnSalvar(ActionEvent event){
        String especie = textEspecie.getText();
        String classificacao = textClasse.getText();
        String descricao = textDescricao.getText();
        String especialidade = textEspecialidade.getText();

        DragaoDTO objDragaoDTO = new DragaoDTO();
        objDragaoDTO.setEspecie(especie);
        objDragaoDTO.setClasse(classificacao);
        objDragaoDTO.setDescricao(descricao);
        objDragaoDTO.setEspecialidade(especialidade);

        DragaoDAO objPlantaDAO = new DragaoDAO();
        objPlantaDAO.adicionarDragao(objDragaoDTO);

        carregarDragoes();
    }

}
