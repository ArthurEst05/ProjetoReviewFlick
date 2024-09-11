package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Controller.db.DB;
import Controller.db.DbException;
import Model.entites.Usuario;
import Model.modelDao.DaoFactory;
import Model.modelDao.UsuarioDao;

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

        ImageIcon imgIcon = new ImageIcon("C:\\Users\\arthur\\Downloads\\ProjetoReviewFlick\\ProjetoReviewFlick\\imagePhantom.png");
        frame.setIconImage(imgIcon.getImage());

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\arthur\\Downloads\\ProjetoReviewFlick\\ProjetoReviewFlick\\image\\telaCadastro.png");
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
        txtNome.setBackground(Color.WHITE);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Melted Monster", Font.PLAIN, 30));
        lblEmail.setForeground(new Color(255,63,63));
        txtEmail = new JTextField(15);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 30));
        txtEmail.setForeground(Color.BLACK);
        txtEmail.setBackground(Color.WHITE); 

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Melted Monster", Font.PLAIN, 30));
        lblSenha.setForeground(new Color(255,63,63));
        txtSenha = new JPasswordField(15);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 30));
        txtSenha.setForeground(Color.BLACK);
        txtSenha.setBackground(Color.WHITE);  

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
    try {
      
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Todos os campos devem ser preenchidos.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!(email.endsWith("@gmail.com") || email.endsWith("@hotmail.com") || email.endsWith("@outlook.com"))) {
            JOptionPane.showMessageDialog(frame, "Por favor, use um email com os seguintes domínios: @gmail.com, @hotmail.com, ou @outlook.com.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UsuarioDao usuarioDao = DaoFactory.createUsuarioDao();

        Usuario existente = findByEmail(email);
        if (existente != null) {
            JOptionPane.showMessageDialog(frame, "Email já registrado. Por favor, use outro email.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario novoUsuario = new Usuario(null, nome, email, senha);

        usuarioDao.insert(novoUsuario);
        JOptionPane.showMessageDialog(frame, "Usuário cadastrado com sucesso.", "Cadastro", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(frame, "Erro ao cadastrar usuário: " + ex.getMessage(), "Cadastro", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

public Usuario findByEmail(String email) {
    Connection conn = DB.gConnection();
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
        st = conn.prepareStatement("SELECT * FROM usuario WHERE Email = ?");
        st.setString(1, email);

        rs = st.executeQuery();
        if (rs.next()) {
            Usuario obj = new Usuario();
            obj.setId(rs.getInt("Id"));
            obj.setNome(rs.getString("Nome"));
            obj.setEmail(rs.getString("Email"));
            obj.setSenha(rs.getString("Senha"));
            return obj;
        }
        return null;
    } catch (SQLException e) {
        throw new DbException(e.getMessage());
    } finally {
        DB.closeResultSet(rs);
        DB.closeStatement(st);
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
