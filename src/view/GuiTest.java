package view;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class GuiTest extends javax.swing.JFrame {

    /**
     * Creates new form FrameInput
     */
    public GuiTest() {
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

        panelTop = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        btnUser = new javax.swing.JButton();
        tablePanel = new javax.swing.JTabbedPane();
        panelTest = new javax.swing.JPanel();
        lblTimer = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbOptions = new javax.swing.JTable();
        lblImage = new javax.swing.JLabel();
        lblInform = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaQuestion = new javax.swing.JTextArea();
        panelBtn = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnFinish = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        lblTimerText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("TESTE");
        setAlwaysOnTop(true);
        setResizable(false);

        lblUser.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblUser.setText("Usuário : Grupo");
        lblUser.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        btnUser.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnUser.setText("Voltar ao Login");
        btnUser.setToolTipText("Fecha o teste atual sem salvar resultados.");

        javax.swing.GroupLayout panelTopLayout = new javax.swing.GroupLayout(panelTop);
        panelTop.setLayout(panelTopLayout);
        panelTopLayout.setHorizontalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createSequentialGroup()
                .addComponent(lblUser, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelTopLayout.setVerticalGroup(
            panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnUser))
        );

        panelTopLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnUser, lblUser});

        tablePanel.setToolTipText("");
        tablePanel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tablePanel.setPreferredSize(new java.awt.Dimension(970, 640));

        panelTest.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelTest.setAutoscrolls(true);

        lblTimer.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        lblTimer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTimer.setText("00:00:00");
        lblTimer.setToolTipText("Tempo para encerrar o teste!");
        lblTimer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        tbOptions.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tbOptions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Alternativas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbOptions.setToolTipText("Clique em um item para selecionar.");
        tbOptions.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbOptions.setRowHeight(20);
        tbOptions.setSelectionForeground(new java.awt.Color(0, 0, 204));
        tbOptions.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbOptions);

        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setText("IMAGEM");
        lblImage.setToolTipText("Imagem correspondente a questão.");

        lblInform.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblInform.setText("Informativo...");

        txtAreaQuestion.setEditable(false);
        txtAreaQuestion.setColumns(20);
        txtAreaQuestion.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        txtAreaQuestion.setLineWrap(true);
        txtAreaQuestion.setRows(5);
        txtAreaQuestion.setText("Vazio");
        txtAreaQuestion.setToolTipText("Questão atual. Leia com atenção!");
        txtAreaQuestion.setWrapStyleWord(true);
        txtAreaQuestion.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jScrollPane2.setViewportView(txtAreaQuestion);

        btnFirst.setText("Primeira");
        btnFirst.setToolTipText("Ir para a primeira questão.");

        btnPrevious.setText("<< Voltar");
        btnPrevious.setToolTipText("Voltar para a questão anterior.");

        btnNext.setText("Avançar >>");
        btnNext.setToolTipText("Ir para a próxima questão.");

        btnFinish.setText("Salvar e Sair");
        btnFinish.setToolTipText("Sair do teste e salvar respostas.");

        btnLast.setText("Última");
        btnLast.setToolTipText("Ir para a última questão.");

        javax.swing.GroupLayout panelBtnLayout = new javax.swing.GroupLayout(panelBtn);
        panelBtn.setLayout(panelBtnLayout);
        panelBtnLayout.setHorizontalGroup(
            panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBtnLayout.createSequentialGroup()
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelBtnLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnFinish, btnFirst, btnLast, btnNext, btnPrevious});

        panelBtnLayout.setVerticalGroup(
            panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBtnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelBtnLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnFinish, btnFirst, btnLast, btnNext, btnPrevious});

        lblTimerText.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        lblTimerText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTimerText.setText("Tempo Restante:");
        lblTimerText.setToolTipText("Tempo para encerrar o teste!");
        lblTimerText.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelTestLayout = new javax.swing.GroupLayout(panelTest);
        panelTest.setLayout(panelTestLayout);
        panelTestLayout.setHorizontalGroup(
            panelTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(panelTestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTestLayout.createSequentialGroup()
                .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelTestLayout.createSequentialGroup()
                        .addComponent(lblTimerText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelTestLayout.setVerticalGroup(
            panelTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTestLayout.createSequentialGroup()
                .addGroup(panelTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelTestLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lblTimerText, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInform, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelTestLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lblTimer, lblTimerText});

        lblTimer.getAccessibleContext().setAccessibleName("Timer");

        tablePanel.addTab("TESTE", panelTest);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(656, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(45, Short.MAX_VALUE)
                    .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(GuiTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new GuiTest().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnFinish;
    public javax.swing.JButton btnFirst;
    public javax.swing.JButton btnLast;
    public javax.swing.JButton btnNext;
    public javax.swing.JButton btnPrevious;
    public javax.swing.JButton btnUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lblImage;
    public javax.swing.JLabel lblInform;
    public javax.swing.JLabel lblTimer;
    public javax.swing.JLabel lblTimerText;
    public javax.swing.JLabel lblUser;
    private javax.swing.JPanel panelBtn;
    public javax.swing.JPanel panelTest;
    private javax.swing.JPanel panelTop;
    public javax.swing.JTabbedPane tablePanel;
    public javax.swing.JTable tbOptions;
    public javax.swing.JTextArea txtAreaQuestion;
    // End of variables declaration//GEN-END:variables

    // Métodos personalizados
    public void eventOptions (MouseListener ev) {
        this.tbOptions.addMouseListener(ev);
    }
    
    public void eventTest(ActionListener ev) {
        this.btnNext.addActionListener(ev);
        this.btnFinish.addActionListener(ev);
        this.btnFirst.addActionListener(ev);
        this.btnUser.addActionListener(ev);
        this.btnPrevious.addActionListener(ev);
        this.btnLast.addActionListener(ev);
    }
}
