package view;

import java.awt.event.ActionListener;

public class Login extends javax.swing.JFrame {

    public Login() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        lblGroup = new javax.swing.JLabel();
        cboxGroup = new javax.swing.JComboBox<>();
        lblUser = new javax.swing.JLabel();
        cboxUser = new javax.swing.JComboBox<>();
        btnQuit = new javax.swing.JButton();
        btnLogin = new javax.swing.JToggleButton();
        lblTheme = new javax.swing.JLabel();
        cboxTheme = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bem vindo!");
        setMinimumSize(new java.awt.Dimension(500, 145));
        setName("frame_login"); // NOI18N
        setResizable(false);

        panel.setPreferredSize(new java.awt.Dimension(350, 100));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 133, Short.MAX_VALUE)
        );

        lblGroup.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblGroup.setText("Turma:");
        lblGroup.setToolTipText("");

        cboxGroup.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboxGroup.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Carregar Turmas" }));
        cboxGroup.setToolTipText("Grupos disponíveis.");

        lblUser.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblUser.setText("Nome:");
        lblUser.setToolTipText("");

        cboxUser.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboxUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Carregar Nomes" }));
        cboxUser.setToolTipText("Usuários disponíveis.");

        btnQuit.setText("SAIR");
        btnQuit.setToolTipText("Fechar o aplicativo!");

        btnLogin.setText("ENTRAR");
        btnLogin.setToolTipText("Iniciar o teste.");

        lblTheme.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblTheme.setText("Tema:");
        lblTheme.setToolTipText("Tema refere-se a um assunto que será abordado no teste.");

        cboxTheme.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cboxTheme.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Carregar Temas" }));
        cboxTheme.setToolTipText("Temas disponíveis.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGroup)
                            .addComponent(btnQuit, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblUser))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboxUser, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboxGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTheme)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboxTheme, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 8, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnQuit, lblGroup, lblUser});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnLogin, cboxGroup, cboxUser});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGroup)
                            .addComponent(cboxGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUser)
                            .addComponent(cboxUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTheme)
                            .addComponent(cboxTheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLogin)
                            .addComponent(btnQuit))
                        .addContainerGap())
                    .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnLogin, btnQuit, cboxGroup, cboxTheme, cboxUser, lblGroup, lblTheme, lblUser});

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

   public static void main(String args[]) {
       /* Set the Nimbus look and feel */
       //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
       /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
        * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
        */
       try {
           for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
               if ("Nimbus".equals(info.getName())) {
                   javax.swing.UIManager.setLookAndFeel(info.getClassName());
                   break;
               }
           }
       } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
           java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
       }
       //</editor-fold>

       /* Create and display the form */
       java.awt.EventQueue.invokeLater(() -> {
           new Login().setVisible(true);
       });
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JToggleButton btnLogin;
    public javax.swing.JButton btnQuit;
    public javax.swing.JComboBox<String> cboxGroup;
    public javax.swing.JComboBox<String> cboxTheme;
    public javax.swing.JComboBox<String> cboxUser;
    private javax.swing.JLabel lblGroup;
    public javax.swing.JLabel lblTheme;
    public javax.swing.JLabel lblUser;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables

    // Métodos personalizados
    public void eventLogin(ActionListener ev) {
        this.btnLogin.addActionListener(ev);
        this.btnQuit.addActionListener(ev);
        this.cboxGroup.addActionListener(ev);
        this.cboxUser.addActionListener(ev);
        this.cboxTheme.addActionListener(ev);
    }
}
