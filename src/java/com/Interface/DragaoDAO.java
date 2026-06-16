package com.Interface;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CLASSE DAO (Data Access Object - Camada de Modelo/Persistência).
 * Esta classe isola completamente todas as instruções de banco de dados (SQL) do restante da aplicação.
 * Utiliza o driver de comunicação JDBC nativo e implementa logs estruturados protegidos de exibição explícita.
 */
public class DragaoDAO {

    // Registrador estático (Logger) encapsulado da API java.util.logging.
    // Substitui com excelência os comandos brutos de 'print', garantindo logs padronizados de erros.
    private static final Logger logger = Logger.getLogger(DragaoDAO.class.getName());

    /**
     * OPERAÇÃO DE SELEÇÃO (R - Read).
     * Consulta todas as linhas ativas da tabela de destino e as converte em uma lista de objetos manipuláveis.
     * @return ArrayList carregado com todas as linhas do banco mapeadas em formato de objetos DragaoDTO.
     */
    public ArrayList<DragaoDTO> listarDragoes() {
        String sql = "SELECT * FROM dragao";
        ArrayList<DragaoDTO> listaDragoes = new ArrayList<>();

        // Mecanismo 'Try-With-Resources' do Java:
        // Todas as classes inseridas dentro dos parênteses do Try que implementam AutoCloseable (Connection, PreparedStatement, ResultSet)
        // serão FECHADAS de forma automática e garantida pelo Java ao fim da execução do bloco, ocorrendo um erro ou não.
        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // O ResultSet armazena os registros textuais temporários devolvidos pelo banco

            // Varre o ponteiro do ResultSet enquanto houver uma próxima linha de dados ativa no registro
            while (rs.next()) {
                DragaoDTO dragao = new DragaoDTO();

                // Extrai o valor de cada coluna da tupla atual indicando o nome do campo string e injeta no DTO
                dragao.setId(rs.getInt("id"));
                dragao.setEspecie(rs.getString("especie"));
                dragao.setClasse(rs.getString("classe"));
                dragao.setDescricao(rs.getString("descricao"));
                dragao.setEspecialidade(rs.getString("especialidade"));

                // Adiciona o DTO completamente preenchido ao agrupamento ArrayList de retorno
                listaDragoes.add(dragao);
            }

        } catch (SQLException e) {
            // Emite de forma interna e segura o rastreamento da pilha de erros sem expor falhas críticas no console do usuário final
            logger.log(Level.SEVERE, "Falha na persistência ao listar registros", e);
        }

        return listaDragoes; // Retorna a coleção contendo os objetos mapeados para o Controller
    }

    /**
     * OPERAÇÃO DE INSERÇÃO (C - Create).
     * Recebe uma entidade DTO preenchida e executa uma query de inserção estruturada (INSERT INTO).
     * @param dragao Instância contendo os novos dados inseridos no formulário da interface gráfica.
     */
    public void adicionarDragao(DragaoDTO dragao) {
        // Query utilizando parâmetros coringas (?) para evitar falhas críticas de SQL Injection e padronizar tipos de dados
        String sql = "INSERT INTO dragao (especie, classe, descricao, especialidade) VALUES (?, ?, ?, ?)";

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Preenche de forma ordenada cada ponto de interrogação da query com o valor tipado extraído do DTO
            ps.setString(1, dragao.getEspecie());
            ps.setString(2, dragao.getClasse());
            ps.setString(3, dragao.getDescricao());
            ps.setString(4, dragao.getEspecialidade());

            // Envia a instrução de escrita formatada para consolidação imediata no banco de dados
            ps.execute();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Falha na persistência ao inserir novo registro", e);
        }
    }

    /**
     * OPERAÇÃO DE EDICAO (U - Update).
     * Substitui os atributos de uma linha existente associando-os com base na cláusula restritiva da chave primária (WHERE id = ?).
     * @param dragaoEdit Instância contendo o ID alvo e os novos valores a serem atualizados sobre ele.
     */
    public void alterarDragao(DragaoDTO dragaoEdit) {
        String sql = "UPDATE dragao SET especie = ?, classe = ?, descricao = ?, especialidade = ? WHERE id = ?";

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Define os novos valores textuais informados na tela nas interrogações sequenciais
            ps.setString(1, dragaoEdit.getEspecie());
            ps.setString(2, dragaoEdit.getClasse());
            ps.setString(3, dragaoEdit.getDescricao());
            ps.setString(4, dragaoEdit.getEspecialidade());

            // Define o ID no último parâmetro da query, garantindo que o comando UPDATE restrinja-se unicamente ao registro correto
            ps.setInt(5, dragaoEdit.getId());

            // Transmite a atualização e efetiva a modificação estrutural na tabela física do banco
            ps.execute();

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Falha na persistência ao atualizar registro específico", ex);
        }
    }

    /**
     * OPERAÇÃO DE EXCLUSÃO (D - Delete).
     * Deleta fisicamente e definitivamente uma linha da tabela filtrando-a através de sua chave primária ID.
     * @param id Valor numérico inteiro contendo a identificação do registro a ser eliminado.
     */
    public void deletarDragao(int id) {
        String sql = "DELETE FROM dragao WHERE id = ?";

        try (Connection c = new Conexao().conectaBD();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Vincula o identificador numérico ao critério restritivo do comando de deleção
            ps.setInt(1, id);

            // Consolda a remoção direta do registro de dados na tabela do banco
            ps.execute();

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Falha na persistência ao excluir registro selecionado", ex);
        }
    }
}