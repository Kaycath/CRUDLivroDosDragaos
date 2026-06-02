package com.Interface;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe DAO atualizada para o Passo 3 (Padrão JavaFX / MVC)
 */
public class DragaoDAO {

    // Método Adicionar: Agora retorna boolean para o Controller saber se deu certo
    public boolean adicionarDragao(DragaoDTO dragao){
        String sql = "INSERT INTO dragao (especie, classe, descricao, especialidade) VALUES (?, ?, ?, ?)";

        // Usamos o try-with-resources para abrir e fechar a conexão automaticamente e com segurança
        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, dragao.getEspecie());
            ps.setString(2, dragao.getClasse());
            ps.setString(3, dragao.getDescricao());
            ps.setString(4, dragao.getEspecialidade());

            ps.execute();
            return true; // Retorna true se gravou no banco com sucesso

        } catch (SQLException e) {
            Logger.getLogger(DragaoDAO.class.getName()).log(Level.SEVERE, null, e);
            return false; // Retorna false se houve alguma falha
        }
    }

    // Método de Seleção: Agora retorna a Lista que o Passo 5 da tabela vai exigir
    public List<DragaoDTO> listarDragoes() {
        String sql = "SELECT * FROM dragao";
        List<DragaoDTO> listaDragoes = new ArrayList<>();

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DragaoDTO dragao = new DragaoDTO();
                dragao.setId(rs.getInt("id"));
                dragao.setEspecie(rs.getString("especie"));
                dragao.setClasse(rs.getString("classe"));
                dragao.setDescricao(rs.getString("descricao"));
                dragao.setEspecialidade(rs.getString("especialidade"));

                listaDragoes.add(dragao); // Guarda o dragão na lista
            }

        } catch (SQLException e) {
            Logger.getLogger(DragaoDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return listaDragoes; // Devolve a lista preenchida para o Controller
    }

    // Método Alterar: Retorna boolean e não exibe prints na consola
    public boolean alterarDragao(DragaoDTO dragaoEdit){
        String sql = "UPDATE dragao SET especie = ?, classe = ?, descricao = ?, especialidade = ? WHERE id = ?";

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, dragaoEdit.getEspecie());
            ps.setString(2, dragaoEdit.getClasse());
            ps.setString(3, dragaoEdit.getDescricao());
            ps.setString(4, dragaoEdit.getEspecialidade());
            ps.setInt(5, dragaoEdit.getId());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0; // Retorna true se encontrou o ID e atualizou

        } catch(SQLException ex){
            Logger.getLogger(DragaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // Método Deletar: Retorna se a remoção foi concluída
    public boolean deletarDragao(int id) {
        String sql = "DELETE FROM dragao WHERE id = ?";

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0; // Retorna true se o registro foi deletado

        } catch (SQLException ex) {
            Logger.getLogger(DragaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}