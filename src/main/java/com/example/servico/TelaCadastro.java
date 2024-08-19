package com.example.servico;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import entites.Usuario;
import modelDao.DaoFactory;
import modelDao.UsuarioDao;

public class TelaCadastro {
    private JFrame frame;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnCadastrar;
    private JButton btnVoltar;

    public TelaCadastro() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Flick Review");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\arthur\\Documents\\ProjetoFilmesPOO\\ProjetoFilmesPOO\\image\\telaCadastro.png");
        backgroundPanel.setLayout(new BorderLayout());

   
        JPanel cadastroPanel = new JPanel();
        cadastroPanel.setLayout(new GridBagLayout());
        cadastroPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cadastroPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Melted Monster", Font.PLAIN, 30));
        lblNome.setForeground(new Color(255,63,63)); 
        txtNome = new JTextField(15);
        txtNome.setFont(new Font("Arial", Font.PLAIN, 30));
        txtNome.setForeground(Color.BLACK);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Melted Monster", Font.PLAIN, 30));
        lblEmail.setForeground(new Color(255,63,63));
        txtEmail = new JTextField(15);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 30));
        txtEmail.setForeground(Color.BLACK);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Melted Monster", Font.PLAIN, 30));
        lblSenha.setForeground(new Color(255,63,63));
        txtSenha = new JPasswordField(15);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 30));
        txtSenha.setForeground(Color.BLACK);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setIcon(new ImageIcon("path/to/register_icon.png"));
        btnCadastrar.setBackground(new Color(255, 63, 63));
        btnCadastrar.setForeground(Color.BLACK);
        btnCadastrar.setFont(new Font("Melted Monster", Font.BOLD, 30));

        gbc.anchor = GridBagConstraints.CENTER;
        cadastroPanel.add(lblNome, gbc);

        gbc.gridx = 1;
        cadastroPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        cadastroPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        cadastroPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        cadastroPanel.add(lblSenha, gbc);

        gbc.gridx = 1;
        cadastroPanel.add(txtSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        cadastroPanel.add(btnCadastrar, gbc);

        backgroundPanel.add(cadastroPanel, BorderLayout.CENTER);

        
        btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 20));
        btnVoltar.setBackground(new Color(255, 63, 63));
        btnVoltar.setForeground(Color.BLACK);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); 
                new TelaInicial().show(); 
            }
        });

        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false); 
        bottomPanel.add(btnVoltar);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(backgroundPanel);

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarUsuario();
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    private void cadastrarUsuario() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        UsuarioDao usuarioDao = DaoFactory.createUsuarioDao();
        Usuario novoUsuario = new Usuario(null, nome, email, senha);

        try {
            usuarioDao.insert(novoUsuario);
            JOptionPane.showMessageDialog(frame, "Usuário cadastrado com sucesso.", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose(); 
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao cadastrar usuário.", "Cadastro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaCadastro().show();
            }
        });
    }
}
