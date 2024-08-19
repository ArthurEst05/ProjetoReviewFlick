package com.example.servico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import entites.Comentario;
import entites.Usuario;
import modelDaoImpl.ComentarioDaoJDBC;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import db.DB;

public class JanelaPrincipal {

    private JFrame frame;
    private JTextField textField;
    private JTextArea textArea;
    private JLabel posterLabel;
    private JList<String> movieList;
    private DefaultListModel<String> listModel;
    private Filmes filmeService;
    private JButton btnComentarios;
    private JPanel detailsPanel;
    private Connection conn;
    private Usuario usuarioAtual;

    public JanelaPrincipal(Connection conn, Usuario usuarioAtual) {
        this.conn = conn;
        this.usuarioAtual = usuarioAtual;
        filmeService = new Filmes();
        initialize();
    }

    private void initialize() {
        configurarTema();

        frame = new JFrame("Informações do Filme");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel inputPanel = criarPainelEntrada();
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 18));
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        listModel = new DefaultListModel<>();
        movieList = criarListaFilmes();
        JScrollPane listScrollPane = new JScrollPane(movieList);
        listScrollPane.setPreferredSize(new Dimension(200, 0));
        listScrollPane.setBackground(new Color(30, 30, 30));
        mainPanel.add(listScrollPane, BorderLayout.WEST);

        detailsPanel = criarPainelDetalhes();
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        btnComentarios = criarBotaoComentarios();
        detailsPanel.add(btnComentarios, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void configurarTema() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", new Color(64, 64, 64));
            UIManager.put("info", new Color(64, 64, 64));
            UIManager.put("nimbusBase", new Color(18, 18, 18));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 115, 115));
            UIManager.put("nimbusGreen", new Color(176, 176, 176));
            UIManager.put("nimbusInfoBlue", new Color(64, 64, 64));
            UIManager.put("nimbusLightBackground", new Color(18, 18, 18));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(64, 64, 64));
            UIManager.put("text", new Color(230, 230, 230));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private JPanel criarPainelEntrada() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(30, 30, 30));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblNomeFilme = new JLabel("Nome do Filme:");
        lblNomeFilme.setFont(new Font("Melted Monster", Font.BOLD, 20));
        lblNomeFilme.setForeground(Color.WHITE);
        inputPanel.add(lblNomeFilme, gbc);

        gbc.gridx++;
        textField = new JTextField(30);
        textField.setBackground(new Color(50, 50, 50));
        textField.setForeground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(new Color(255, 63, 63), 2));
        inputPanel.add(textField, gbc);

        gbc.gridx++;
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Melted Monster", Font.BOLD, 20));
        btnBuscar.setBackground(new Color(255, 63, 63));
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFilmes();
            }
        });
        inputPanel.add(btnBuscar, gbc);

        return inputPanel;
    }

    private JList<String> criarListaFilmes() {
        JList<String> movieList = new JList<>(listModel);
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieList.setForeground(Color.WHITE);
        movieList.setBackground(new Color(40, 40, 40));
        movieList.setSelectionBackground(new Color(255, 63, 63));
        movieList.setBorder(BorderFactory.createLineBorder(new Color(64, 64, 64), 1));
        movieList.setFont(new Font("Arial", Font.PLAIN, 14));
        movieList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedMovie = movieList.getSelectedValue();
                    if (selectedMovie != null) {
                        exibirFilmeSelecionado(selectedMovie);
                    }
                }
            }
        });
        return movieList;
    }

    private JPanel criarPainelDetalhes() {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(new Color(18, 18, 18));
        detailsPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 

        posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(JLabel.CENTER);
        posterLabel.setVerticalAlignment(JLabel.TOP);
        posterLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        posterLabel.setBackground(new Color(18, 18, 18));
        detailsPanel.add(posterLabel, BorderLayout.WEST);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(new Color(18, 18, 18));
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 63, 63), 1));
        detailsPanel.add(textScrollPane, BorderLayout.CENTER);

        return detailsPanel;
    }

    private JButton criarBotaoComentarios() {
        JButton btnComentarios = new JButton("Comentários");
        btnComentarios.setFont(new Font("Melted Monster", Font.BOLD, 20));
        btnComentarios.setBackground(new Color(255, 63, 63));
        btnComentarios.setForeground(Color.BLACK);
        btnComentarios.setFocusPainted(false);
        btnComentarios.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnComentarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirJanelaComentarios(movieList.getSelectedValue());
            }
        });

        return btnComentarios;
    }

    private void abrirJanelaComentarios(String filmeSelecionado) {
        if (filmeSelecionado == null) {
            JOptionPane.showMessageDialog(frame, "Selecione um filme primeiro!");
            return;
        }

        EnderecoDto detalhesFilme;
        try {
            detalhesFilme = filmeService.buscarDetalhesFilme(filmeSelecionado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao buscar detalhes do filme: " + e.getMessage());
            return;
        }

        String imdbID = detalhesFilme.getImdbID();

        JFrame comentariosFrame = new JFrame("Comentários para " + filmeSelecionado);
        comentariosFrame.setSize(500, 400);
        comentariosFrame.setLayout(new BorderLayout());

        JTextArea areaComentarios = new JTextArea();
        areaComentarios.setEditable(false);
        areaComentarios.setBackground(new Color(18, 18, 18));
        areaComentarios.setForeground(Color.WHITE);
        areaComentarios.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollComentarios = new JScrollPane(areaComentarios);
        comentariosFrame.add(scrollComentarios, BorderLayout.CENTER);

        JTextField campoComentario = new JTextField();
        campoComentario.setBackground(new Color(30, 30, 30));
        campoComentario.setForeground(Color.WHITE);
        campoComentario.setFont(new Font("Arial", Font.PLAIN, 14));
        campoComentario.setBorder(BorderFactory.createLineBorder(new Color(255, 63, 63), 1));

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(255, 63, 63));
        btnEnviar.setForeground(Color.BLACK);
        btnEnviar.setFont(new Font("Melted Monster", Font.BOLD, 20));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textoComentario = campoComentario.getText();
                if (!textoComentario.isEmpty()) {
                    Connection conn = DB.gConnection();
                    ComentarioDaoJDBC comentarioDao = new ComentarioDaoJDBC(conn);
                    Comentario comentario = new Comentario();
                    comentario.setComentario(textoComentario);
                    comentario.setUsuario(usuarioAtual);
                    comentario.setData(new Date(System.currentTimeMillis()));
                    comentario.setImdbID(imdbID);
                    comentarioDao.save(comentario);

                    areaComentarios.append(usuarioAtual.getNome() + " (" + comentario.getData() + "): " + textoComentario + "\n");
                    campoComentario.setText("");
                } else {
                    JOptionPane.showMessageDialog(comentariosFrame, "O comentário não pode estar vazio!");
                }
            }
        });

        Connection conn = DB.gConnection();
        ComentarioDaoJDBC comentarioDao = new ComentarioDaoJDBC(conn);
        List<Comentario> comentarios = comentarioDao.findAllByImdbID(imdbID);
        for (Comentario comentario : comentarios) {
            areaComentarios.append(comentario.getUsuario().getNome() + " (" + comentario.getData() + "): " + comentario.getComentario() + "\n");
        }

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        southPanel.add(campoComentario, BorderLayout.CENTER);
        southPanel.add(btnEnviar, BorderLayout.EAST);
        comentariosFrame.add(southPanel, BorderLayout.SOUTH);

        comentariosFrame.setVisible(true);
    }

    private void buscarFilmes() {
        String filmeBuscado = textField.getText();
        if (filmeBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite o nome de um filme!");
            return;
        }

        try {
            List<EnderecoDto> resultados = filmeService.buscarFilmes(filmeBuscado);
            listModel.clear();
            for (EnderecoDto endereco : resultados) {
                listModel.addElement(endereco.getTitle());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao buscar filmes: " + e.getMessage());
        }
    }

    private void exibirFilmeSelecionado(String filme) {
        try {
            EnderecoDto detalhes = filmeService.buscarDetalhesFilme(filme);

            textArea.setText(
                "Título: " + detalhes.getTitle() + "\n" +
                "Ano: " + detalhes.getYear() + "\n" +
                "Classificação: " + detalhes.getRated() + "\n" +
                "Data de Lançamento: " + detalhes.getReleased() + "\n" +
                "Duração: " + detalhes.getRuntime() + "\n" +
                "Gênero: " + detalhes.getGenre() + "\n" +
                "Diretor: " + detalhes.getDirector() + "\n" +
                "Roteirista: " + detalhes.getWriter() + "\n" +
                "Elenco: " + detalhes.getActors() + "\n" +
                "Enredo: " + detalhes.getPlot() + "\n" +
                "Metascore: " + detalhes.getMetascore() + "\n"
            );

            String posterUrl = detalhes.getPoster();
            if (posterUrl != null && !posterUrl.isEmpty()) {
                try {
                    ImageIcon posterIcon = new ImageIcon(new java.net.URL(posterUrl));
                    posterLabel.setIcon(new ImageIcon(posterIcon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH)));
                } catch (MalformedURLException e) {
                    JOptionPane.showMessageDialog(frame, "URL da imagem inválida: " + posterUrl);
                    posterLabel.setIcon(null);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Erro ao carregar a imagem: " + e.getMessage());
                    posterLabel.setIcon(null);
                }
            } else {
                posterLabel.setIcon(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao exibir detalhes do filme: " + e.getMessage());
        }
    }
}
