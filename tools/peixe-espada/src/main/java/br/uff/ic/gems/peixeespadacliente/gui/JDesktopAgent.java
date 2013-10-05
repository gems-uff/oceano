package br.uff.ic.gems.peixeespadacliente.gui;

import br.uff.ic.gems.peixeespadacliente.gui.systray.AboutMenuItem;
import br.uff.ic.gems.peixeespadacliente.gui.systray.SysTrayIcon;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.service.ClientService;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.peixeespada.model.Agent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.OverlayLayout;

/**
 *
 * @author Heliomar
 */
public class JDesktopAgent extends javax.swing.JFrame {

    private static int SCHEDULE = 1;
    private static int CHOOSE = 2;
    private static int RUNNING = 4;
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    private ClientService clientServer = new ClientService();
     
    private LocalManagerAgent agentPeixeEspada = null;
    
    /** Creates new form JAgent */
    public JDesktopAgent() {
        initComponents();
        SysTrayIcon sysTrayIcon = new SysTrayIcon(this, toolTips[0]);
//        jLayeredPane1.setLayout(new OverlayLayout(jLayeredPane1));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle("Peixe-Espada");

        this.setInterface(SCHEDULE);
    }
    
    public static JDesktopAgent createToTest(LocalManagerAgent agentPeixeEspada) {
        JDesktopAgent jAgent = new JDesktopAgent();

        jAgent.setInterface(RUNNING);
        jAgent.fieldProject.setText(agentPeixeEspada.getProjectVCS().getLocalPath());
        jAgent.fieldQualityAtributte.setText("Nothing");
        jAgent.fieldIntervalTime.setText("From: ... until: ...");
        jAgent.fieldStatus.setText(agentPeixeEspada.getStatus());
        jAgent.setTitle("Frame Testing");
        jAgent.setVisible(true);

        agentPeixeEspada.setOutput(jAgent);
        return jAgent;
    }

    public static JDesktopAgent create(LocalManagerAgent agentPeixeEspada) throws PropertyVetoException {
        JDesktopAgent jAgent = new JDesktopAgent();

        jAgent.setInterface(RUNNING);
        jAgent.fieldProject.setText(agentPeixeEspada.getOrchestratorAgent().getProject().getConfigurationItem().getName() + " : " + agentPeixeEspada.getOrchestratorAgent().getProject().getRepositoryUrl());
        jAgent.fieldQualityAtributte.setText(agentPeixeEspada.getOrchestratorAgent().getQualityAttribute().getName());
        jAgent.fieldIntervalTime.setText("From: " + sdf.format(agentPeixeEspada.getInitDate()) + " until: " + sdf.format(agentPeixeEspada.getEndDate()));
        jAgent.fieldStatus.setText(agentPeixeEspada.getStatus());
        jAgent.setTitle("Local Manager [" + agentPeixeEspada.getIdentifier() + "] Window");
        jAgent.setVisible(true);

        agentPeixeEspada.setOutput(jAgent);
        return jAgent;
    }

    public void appendMessage(String message) {
        jOutput.append(message + "\n");
    }

    public void cleanMessage() {
        jOutput.setText("");
    }

    private void setInterface(int value) {
        jTabbedPane1.setEnabledAt(0, value == SCHEDULE);
        jTabbedPane1.setEnabledAt(1, value == CHOOSE);
        jTabbedPane1.setEnabledAt(2, value == RUNNING);
        jTabbedPane1.setSelectedIndex(
                value == SCHEDULE ? 0 :
                value == CHOOSE   ? 1 :
                value == RUNNING  ? 2 :
                3
        );
        
//        jSchedulePanel.setVisible();
//        jChoosePanel.setVisible(value == CHOOSE);
//        jOutputPanel.setVisible(value == RUNNING);
//        if (value == RUNNING) {
//            this.setSize(750, 500);
//        } else {
//            this.setSize(750, 350);
//        }
    }

    private void hideAction() {
        JOptionPane.showMessageDialog(this, "O Peixe Espada Cliente ainda está executando, \n Vefifique o ícone na bandeja", "Peixe Espada Backgound", JOptionPane.INFORMATION_MESSAGE);
        this.setVisible(false);
    }
    
    private void resetTime() {
        dateInit.setValue(new Date(System.currentTimeMillis()+90000));
        dateEnd.setValue(new Date(System.currentTimeMillis()+43200000+90000));
    }
    
    private Date parseDate(String stringDate) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date result = null;
        try {
            result = simpleDateFormat.parse(stringDate);
        } catch (ParseException ex) {
            throw new Exception("["+stringDate+"] is inválid\nFormat: dd/MM/yyyy HH:mm\nex. 23/12/2010 18:40");
        }
        return result;
    } 
    
    private Date parseDateMessage(String stringDate, String title) {
        try {
            return parseDate(stringDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), title, JOptionPane.ERROR_MESSAGE);
            return null;
        }
    } 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSchedulePanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        dateInit = new javax.swing.JFormattedTextField();
        dateEnd = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        passUserOcean = new javax.swing.JPasswordField();
        txtUserOcean = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        btnScheduling = new javax.swing.JButton();
        btnResetTime = new javax.swing.JButton();
        jChoosePanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        jComboAgents = new javax.swing.JComboBox();
        btnBack = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jOutputPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fieldProject = new javax.swing.JLabel();
        fieldQualityAtributte = new javax.swing.JLabel();
        fieldIntervalTime = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fieldStatus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jOutput = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        JM_File = new javax.swing.JMenu();
        jMenuItem_Quit = new javax.swing.JMenuItem();
        JM_Help = new javax.swing.JMenu();
        JMI_About = new javax.swing.JMenuItem();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSchedulePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Schedule"));

        jLabel5.setText("Choose the initial time");

        jLabel6.setText("Choose the final time");

        dateInit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm"))));
        dateInit.setValue(new Date(System.currentTimeMillis()+90000));

        dateEnd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm"))));
        dateEnd.setValue(new Date(System.currentTimeMillis()+43200000+90000));

        jLabel7.setText("Oceano's login");

        jLabel8.setText("Oceano's password");

        passUserOcean.setText("kannkann");

        txtUserOcean.setText("kann");

        btnScheduling.setText("OK");
        btnScheduling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchedulingActionPerformed(evt);
            }
        });

        btnResetTime.setText("Reset time");
        btnResetTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetTimeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jSchedulePanelLayout = new javax.swing.GroupLayout(jSchedulePanel);
        jSchedulePanel.setLayout(jSchedulePanelLayout);
        jSchedulePanelLayout.setHorizontalGroup(
            jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSchedulePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jSchedulePanelLayout.createSequentialGroup()
                        .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                        .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(passUserOcean)
                            .addComponent(txtUserOcean, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)))
                    .addGroup(jSchedulePanelLayout.createSequentialGroup()
                        .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateInit, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                            .addComponent(dateEnd, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnResetTime, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                    .addComponent(btnScheduling, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE))
                .addContainerGap())
        );
        jSchedulePanelLayout.setVerticalGroup(
            jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jSchedulePanelLayout.createSequentialGroup()
                .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jSchedulePanelLayout.createSequentialGroup()
                        .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(dateInit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(dateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jSchedulePanelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(btnResetTime)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtUserOcean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(passUserOcean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnScheduling)
                .addGap(286, 286, 286))
        );

        jTabbedPane1.addTab("Schedule", jSchedulePanel);
        jSchedulePanel.getAccessibleContext().setAccessibleName("");

        jChoosePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Choose Orcherstrator Agent"));

        jLabel9.setText("Choose the initial time");

        jLabel10.setText("Choose the final time");

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        jComboAgents.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Choose the Orchestrator Agent" }));

        btnBack.setText("Reschedule");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jChoosePanelLayout = new javax.swing.GroupLayout(jChoosePanel);
        jChoosePanel.setLayout(jChoosePanelLayout);
        jChoosePanelLayout.setHorizontalGroup(
            jChoosePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jChoosePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jChoosePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jChoosePanelLayout.createSequentialGroup()
                        .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jChoosePanelLayout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jChoosePanelLayout.createSequentialGroup()
                        .addGroup(jChoosePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jChoosePanelLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(131, 131, 131))
                            .addGroup(jChoosePanelLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(137, 137, 137))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                            .addComponent(jComboAgents, 0, 418, Short.MAX_VALUE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
                        .addGap(354, 354, 354))))
        );
        jChoosePanelLayout.setVerticalGroup(
            jChoosePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jChoosePanelLayout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboAgents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOk)
                .addContainerGap(227, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Choose Orchestrator Agent", jChoosePanel);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent data"));

        jLabel1.setText("Project: ");

        jLabel2.setText("Orchestrator Agent:");

        jLabel3.setText("Interval Time:");

        fieldProject.setText("fieldProject");

        fieldQualityAtributte.setText("fieldQualityAtributte");

        fieldIntervalTime.setText("fieldIntervalTime");

        jLabel4.setText("Status:");

        fieldStatus.setFont(new java.awt.Font("Tahoma", 1, 11));
        fieldStatus.setText("fieldStatus");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(67, 67, 67)
                        .addComponent(fieldProject))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldStatus)
                            .addComponent(fieldIntervalTime)
                            .addComponent(fieldQualityAtributte))))
                .addContainerGap(452, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fieldProject))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fieldQualityAtributte))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fieldIntervalTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(fieldStatus))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        jOutput.setColumns(20);
        jOutput.setRows(5);
        jScrollPane1.setViewportView(jOutput);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jOutputPanelLayout = new javax.swing.GroupLayout(jOutputPanel);
        jOutputPanel.setLayout(jOutputPanelLayout);
        jOutputPanelLayout.setHorizontalGroup(
            jOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOutputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jOutputPanelLayout.setVerticalGroup(
            jOutputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jOutputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Running", jOutputPanel);

        JM_File.setText("File");

        jMenuItem_Quit.setText("Quit");
        jMenuItem_Quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_QuitActionPerformed(evt);
            }
        });
        JM_File.add(jMenuItem_Quit);

        jMenuBar1.add(JM_File);

        JM_Help.setText("Help");

        JMI_About.setText("About");
        JMI_About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMI_AboutActionPerformed(evt);
            }
        });
        JM_Help.add(JMI_About);

        jMenuBar1.add(JM_Help);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JMI_AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JMI_AboutActionPerformed
        // TODO add your handling code here:
        AboutMenuItem.aboutActionPerformed();
    }//GEN-LAST:event_JMI_AboutActionPerformed

    private void jMenuItem_QuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_QuitActionPerformed
        // TODO add your handling code here:
        hideAction();
    }//GEN-LAST:event_jMenuItem_QuitActionPerformed

    private void btnResetTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetTimeActionPerformed
        // TODO add your handling code here:
        resetTime();
    }//GEN-LAST:event_btnResetTimeActionPerformed

    private void btnSchedulingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchedulingActionPerformed
        // TODO add your handling code here:
        Date localDateInitial = parseDateMessage(dateInit.getText(), "Invalid initial date");
        Date localDateEnd = parseDateMessage(dateEnd.getText(), "Invalid end date");       
        if(localDateInitial != null && localDateEnd != null){
            try {
                agentPeixeEspada = LocalManagerAgent.create(localDateInitial, localDateEnd, txtUserOcean.getText(), new String(passUserOcean.getPassword()));
                List<Agent> orchestratorAgents = clientServer.getOrchestratorAgents(agentPeixeEspada);
                
                DefaultComboBoxModel model = (DefaultComboBoxModel) jComboAgents.getModel();
                model.removeAllElements();
                model.addElement("Choose the Orchestrator Agent");
                for (Agent agent : orchestratorAgents) {
                    model.addElement(agent);
                }
                
                jLabel9.setText("Initial Time: " + dateInit.getText());
                jLabel10.setText("Final Time: " + dateEnd.getText());
                setInterface(CHOOSE);
            } catch (ServiceException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnSchedulingActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // TODO add your handling code here:
        Object selected = jComboAgents.getSelectedItem();
        if (!(selected instanceof Agent)) {
            JOptionPane.showMessageDialog(this, "Choose a Orchestrator Agent", "Validation error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        agentPeixeEspada.prepareAgent((Agent) selected);

        try {
            clientServer.scheduling(agentPeixeEspada);
            JOptionPane.showMessageDialog(this, "Agent: [" + agentPeixeEspada.getIdentifier() + "] registred with sucessfull", "Sucessfull", JOptionPane.INFORMATION_MESSAGE);
//            Main.jDesktop.add(agentPeixeEspada.prepareOutput());
            agentPeixeEspada.setOutput(this);

            fieldProject.setText(agentPeixeEspada.getOrchestratorAgent().getProject().getConfigurationItem().getName() + " : " + agentPeixeEspada.getOrchestratorAgent().getProject().getRepositoryUrl());
            fieldQualityAtributte.setText(agentPeixeEspada.getOrchestratorAgent().getQualityAttribute().getName());
            fieldIntervalTime.setText("From: " + sdf.format(agentPeixeEspada.getInitDate()) + " until: " + sdf.format(agentPeixeEspada.getEndDate()));
            fieldStatus.setText(agentPeixeEspada.getStatus());
            setTitle("Local Manager [" + agentPeixeEspada.getIdentifier() + "] Window");
            setInterface(RUNNING);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        resetTime();
        setInterface(SCHEDULE);
    }//GEN-LAST:event_btnBackActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        hideAction();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem JMI_About;
    private javax.swing.JMenu JM_File;
    private javax.swing.JMenu JM_Help;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnResetTime;
    private javax.swing.JButton btnScheduling;
    private javax.swing.JFormattedTextField dateEnd;
    private javax.swing.JFormattedTextField dateInit;
    public javax.swing.JLabel fieldIntervalTime;
    public javax.swing.JLabel fieldProject;
    public javax.swing.JLabel fieldQualityAtributte;
    public javax.swing.JLabel fieldStatus;
    private javax.swing.JPanel jChoosePanel;
    private javax.swing.JComboBox jComboAgents;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem_Quit;
    private javax.swing.JTextArea jOutput;
    private javax.swing.JPanel jOutputPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jSchedulePanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPasswordField passUserOcean;
    private javax.swing.JTextField txtUserOcean;
    // End of variables declaration//GEN-END:variables
    private static final String[] toolTips = {
        "Peixe Espada Cliente!",
        "blablabla\n Kann 2010"
    };

    public void restore() {
        if (isVisible()) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }
}
