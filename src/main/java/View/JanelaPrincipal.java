package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Controller.db.DB;
import Controller.servico.EnderecoDto;
import Controller.servico.Filmes;
import Model.entites.Comentario;
import Model.entites.Usuario;
import Model.modelDaoImpl.ComentarioDaoJDBC;

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
    private Usuario usuarioAtual;
    private Connection conn;

    public JanelaPrincipal(Connection conn, Usuario usuarioAtual) {
        this.conn = conn;
        this.usuarioAtual = usuarioAtual;
        filmeService = new Filmes();
        initialize();
    }

    private void initialize() {
        configurarTema();
        frame = new JFrame("Informações do Filme");

        ImageIcon imgIcon = new ImageIcon(
                "C:\\Users\\arthur\\Downloads\\ProjetoReviewFlick\\ProjetoReviewFlick\\image\\Phantom.png");
        frame.setIconImage(imgIcon.getImage());

        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Melted Monster", Font.BOLD, 20));
        btnVoltar.setBackground(new Color(255, 63, 63));
        btnVoltar.setForeground(Color.BLACK);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarTelaInicial();
            }
        });
        topPanel.add(btnVoltar, BorderLayout.WEST);

        JPanel inputPanel = criarPainelEntrada();
        topPanel.add(inputPanel, BorderLayout.CENTER);

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
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 50));
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
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
    
        ImageIcon imgIcon = new ImageIcon("C:\\Users\\arthur\\Downloads\\ProjetoReviewFlick\\ProjetoReviewFlick\\image\\Phantom.png");
        comentariosFrame.setIconImage(imgIcon.getImage());
    
        DefaultListModel<Comentario> listModelComentarios = new DefaultListModel<>();
        JList<Comentario> listComentarios = new JList<>(listModelComentarios);
        listComentarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listComentarios.setCellRenderer(new ComentarioListCellRenderer()); 
        listComentarios.setForeground(Color.WHITE);
        listComentarios.setBackground(new Color(40, 40, 40));
        listComentarios.setSelectionBackground(new Color(255, 63, 63));
        listComentarios.setFont(new Font("Arial", Font.PLAIN, 14));
    
        JScrollPane scrollComentarios = new JScrollPane(listComentarios);
        comentariosFrame.add(scrollComentarios, BorderLayout.CENTER);
    
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editarMenuItem = new JMenuItem("Editar");
        JMenuItem excluirMenuItem = new JMenuItem("Excluir");
    
        popupMenu.add(editarMenuItem);
        popupMenu.add(excluirMenuItem);
    
        excluirMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Comentario comentarioSelecionado = listComentarios.getSelectedValue();
                if (comentarioSelecionado != null && comentarioSelecionado.getUsuario().getId().equals(usuarioAtual.getId())) {
                    int confirm = JOptionPane.showConfirmDialog(comentariosFrame, "Tem certeza que deseja excluir este comentário?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        ComentarioDaoJDBC comentarioDao = new ComentarioDaoJDBC(conn);
                        comentarioDao.deleteById(comentarioSelecionado.getId());
                        listModelComentarios.removeElement(comentarioSelecionado);
                    }
                } else {
                    JOptionPane.showMessageDialog(comentariosFrame, "Você só pode excluir seus próprios comentários!");
                }
            }
        });
    
        editarMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Comentario comentarioSelecionado = listComentarios.getSelectedValue();
                if (comentarioSelecionado != null && comentarioSelecionado.getUsuario().getId().equals(usuarioAtual.getId())) {
                    String novoComentario = JOptionPane.showInputDialog(comentariosFrame, "Edite seu comentário:", comentarioSelecionado.getComentario());
                    if (novoComentario != null && !novoComentario.trim().isEmpty()) {
                        comentarioSelecionado.setComentario(novoComentario);
                        ComentarioDaoJDBC comentarioDao = new ComentarioDaoJDBC(conn);
                        comentarioDao.update(comentarioSelecionado);
                        listComentarios.repaint();
                    }
                } else {
                    JOptionPane.showMessageDialog(comentariosFrame, "Você só pode editar seus próprios comentários!");
                }
            }
        });
    
        listComentarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = listComentarios.locationToIndex(e.getPoint());
                    if (index != -1) {
                        listComentarios.setSelectedIndex(index);
                        popupMenu.show(listComentarios, e.getX(), e.getY());
                    }
                }
            }
        });
    
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
                    ComentarioDaoJDBC comentarioDao = new ComentarioDaoJDBC(conn);
                    Comentario comentario = new Comentario();
                    comentario.setComentario(textoComentario);
                    comentario.setUsuario(usuarioAtual);
                    comentario.setData(new Date(System.currentTimeMillis()));
                    comentario.setImdbID(imdbID);
                    comentario.setCurtidas(0); 
                    comentarioDao.save(comentario);
    
                    listModelComentarios.addElement(comentario);
                    campoComentario.setText("");
                } else {
                    JOptionPane.showMessageDialog(comentariosFrame, "O comentário não pode estar vazio!");
                }
            }
        });
    
        // Botão Curtir/Descurtir dinâmico
        JButton btnCurtirDescurtir = new JButton();
        btnCurtirDescurtir.setBackground(new Color(255, 63, 63));
        btnCurtirDescurtir.setForeground(Color.BLACK);
        btnCurtirDescurtir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCurtirDescurtir.setFocusPainted(false);
    
        btnCurtirDescurtir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Comentario comentarioSelecionado = listComentarios.getSelectedValue();
                if (comentarioSelecionado != null) {
                    ComentarioDaoJDBC comentarioDao = new ComentarioDaoJDBC(conn);
                    if (comentarioDao.jaCurtiu(usuarioAtual.getId(), comentarioSelecionado.getId())) {
                        comentarioSelecionado.setCurtidas(comentarioSelecionado.getCurtidas() - 1);
                        comentarioDao.update(comentarioSelecionado);
                        comentarioDao.removerCurtida(usuarioAtual.getId(), comentarioSelecionado.getId());
                        btnCurtirDescurtir.setText("Curtir"); // Alterar para "Curtir"
                    } else {
                        comentarioSelecionado.setCurtidas(comentarioSelecionado.getCurtidas() + 1);
                        comentarioDao.update(comentarioSelecionado);
                        comentarioDao.registrarCurtida(usuarioAtual.getId(), comentarioSelecionado.getId());
                        btnCurtirDescurtir.setText("Descurtir"); // Alterar para "Descurtir"
                    }
                    listComentarios.repaint(); // Atualiza a lista para mostrar o número de curtidas atualizado
                }
            }
        });
    
        // Atualizar o botão quando o comentário é selecionado
        listComentarios.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Comentario comentarioSelecionado = listComentarios.getSelectedValue();
                if (comentarioSelecionado != null) {
                    ComentarioDaoJDBC comentarioDao = new ComentarioDaoJDBC(conn);
                    if (comentarioDao.jaCurtiu(usuarioAtual.getId(), comentarioSelecionado.getId())) {
                        btnCurtirDescurtir.setText("Descurtir");
                    } else {
                        btnCurtirDescurtir.setText("Curtir");
                    }
                }
            }
        });
    
        Connection conn = DB.gConnection();
        ComentarioDaoJDBC comentarioDao = new ComentarioDaoJDBC(conn);
        List<Comentario> comentarios = comentarioDao.findAllByImdbID(imdbID);
        for (Comentario comentario : comentarios) {
            listModelComentarios.addElement(comentario);
        }
    
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        southPanel.add(campoComentario, BorderLayout.CENTER);
        southPanel.add(btnEnviar, BorderLayout.EAST);
        comentariosFrame.add(southPanel, BorderLayout.SOUTH);
    
        JPanel curtidasPanel = new JPanel(new BorderLayout());
        curtidasPanel.add(btnCurtirDescurtir, BorderLayout.SOUTH);
        comentariosFrame.add(curtidasPanel, BorderLayout.NORTH);
    
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
                            "Metascore: " + detalhes.getMetascore() + "\n");

            String posterUrl = detalhes.getPoster();
            if (posterUrl != null && !posterUrl.isEmpty()) {
                try {
                    ImageIcon posterIcon = new ImageIcon(new java.net.URL(posterUrl));
                    posterLabel.setIcon(
                            new ImageIcon(posterIcon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH)));
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

    private void voltarTelaInicial() {
        frame.dispose();
        new TelaInicial().show();
    }

    private class ComentarioListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Comentario) {
                Comentario comentario = (Comentario) value;
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(comentario.getData());
                setText("(" + formattedDate + ") " + comentario.getUsuario().getNome() + ": "
                        + comentario.getComentario() + " [" + comentario.getCurtidas() + " curtidas ]");
            }
            return component;
        }
    }
}
