package com.Interface;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe especializada no gerenciamento de persistência de dados.
 * Isola os comandos estruturados SQL da lógica operacional do fluxo de telas.
 */
public class DragaoDAO {

    // Componente nativo usado para emitir mensagens técnicas estruturadas diretamente no console
    private static final Logger logger = Logger.getLogger(DragaoDAO.class.getName());

    /**
     * Efetua uma consulta SQL do tipo SELECT para retornar todas as tuplas existentes.
     * @return ArrayList preenchido com instâncias do tipo DragaoDTO.
     */
    public ArrayList<DragaoDTO> listarDragoes() {
        String sql = "SELECT * FROM dragao";
        ArrayList<DragaoDTO> listaDragoes = new ArrayList<>();

        // Abre a conexão e prepara a declaração de execução de forma encadeada e segura
        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Percorre as linhas do conjunto de resultados enquanto houver um próximo registro
            while (rs.next()) {
                DragaoDTO dragao = new DragaoDTO();

                // Mapeia o dado vindo da coluna da tabela do banco para o atributo do DTO
                dragao.setId(rs.getInt("id"));
                dragao.setEspecie(rs.getString("especie"));
                dragao.setClasse(rs.getString("classe"));
                dragao.setDescricao(rs.getString("descricao"));
                dragao.setEspecialidade(rs.getString("especialidade"));

                // Insere a entidade populada no agrupamento dinâmico
                listaDragoes.add(dragao);
            }

        } catch (SQLException e) {
            // Registra a exceção capturada com nível de severidade alto
            logger.log(Level.SEVERE, "Erro ao listar dragões", e);
        }

        return listaDragoes;
    }

    /**
     * Realiza a inserção de um novo registro na base de dados por meio de comando preparado.
     * @param dragao Objeto de transferência contendo os valores textuais a serem gravados.
     */
    public void adicionarDragao(DragaoDTO dragao) {
        String sql = "INSERT INTO dragao (especie, classe, descricao, especialidade) VALUES (?, ?, ?, ?)";

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Substitui os parâmetros coringas (?) pelos dados extraídos da instância recebida
            ps.setString(1, dragao.getEspecie());
            ps.setString(2, dragao.getClasse());
            ps.setString(3, dragao.getDescricao());
            ps.setString(4, dragao.getEspecialidade());

            // Executa a instrução INSERT diretamente no SGDB conectado
            ps.execute();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao inserir dragão", e);
        }
    }

    /**
     * Atualiza os campos de um registro existente tomando como base a chave primária fornecida.
     * @param dragaoEdit Instância contendo os novos dados informados e o ID alvo.
     */
    public void alterarDragao(DragaoDTO dragaoEdit) {
        String sql = "UPDATE dragao SET especie = ?, classe = ?, descricao = ?, especialidade = ? WHERE id = ?";

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Vincula as modificações textuais sequencialmente
            ps.setString(1, dragaoEdit.getEspecie());
            ps.setString(2, dragaoEdit.getClasse());
            ps.setString(3, dragaoEdit.getDescricao());
            ps.setString(4, dragaoEdit.getEspecialidade());

            // Define o ID no critério de restrição WHERE para certificar qual registro sofrerá o impacto
            ps.setInt(5, dragaoEdit.getId());

            // Executa a operação de atualização de dados
            ps.execute();

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao atualizar dragão", ex);
        }
    }

    /**
     * Remove fisicamente um registro da tabela associada com base no identificador recebido.
     * @param id Chave primária do elemento que sofrerá a exclusão.
     */
    public void deletarDragao(int id) {
        String sql = "DELETE FROM dragao WHERE id = ?";

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Vincula o identificador recebido ao parâmetro de restrição da exclusão
            ps.setInt(1, id);

            // Consolida o comando de exclusão na base de dados
            ps.execute();

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao excluir dragão", ex);
        }
    }
}