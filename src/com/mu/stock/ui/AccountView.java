/*
 * DataAnalyserAboutBox.java
 */

package com.mu.stock.ui;

import com.mu.stock.dao.AccountDAO;
import com.mu.stock.entity.Account;
import com.mu.util.DateUtil;
import com.mu.util.ui.TableUtil;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Action;

public class AccountView extends javax.swing.JDialog {

    public AccountView(java.awt.Frame parent) {
        super(parent);
        initComponents();
        initAcctTable();
    }

    @Action public void closeAboutBox() {
        dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbCpy = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        tfCapital = new javax.swing.JTextField();
        tfFrom = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAcct = new javax.swing.JTable();
        tfTo = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        taStrategy = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.mu.stock.ui.DataAnalyserApp.class).getContext().getResourceMap(AccountView.class);
        setTitle(resourceMap.getString("title")); // NOI18N
        setName("AccountView"); // NOI18N

        lbCpy.setText(resourceMap.getString("lbCpy.text")); // NOI18N
        lbCpy.setName("lbCpy"); // NOI18N

        btnAdd.setText(resourceMap.getString("btnAdd.text")); // NOI18N
        btnAdd.setName("btnAdd"); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        tfName.setText(resourceMap.getString("tfName.text")); // NOI18N
        tfName.setName("tfName"); // NOI18N

        tfCapital.setText(resourceMap.getString("tfCapital.text")); // NOI18N
        tfCapital.setName("tfCapital"); // NOI18N

        tfFrom.setText(resourceMap.getString("tfFrom.text")); // NOI18N
        tfFrom.setName("tfFrom"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tblAcct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "null", "null"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAcct.setName("tblAcct"); // NOI18N
        tblAcct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAcctMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAcct);
        tblAcct.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tblAcct.columnModel.title0")); // NOI18N
        tblAcct.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tblAcct.columnModel.title1")); // NOI18N
        tblAcct.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("tblAcct.columnModel.title2")); // NOI18N
        tblAcct.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("tblAcct.columnModel.title3")); // NOI18N

        tfTo.setText(resourceMap.getString("tfTo.text")); // NOI18N
        tfTo.setName("tfTo"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        taStrategy.setColumns(20);
        taStrategy.setRows(5);
        taStrategy.setName("taStrategy"); // NOI18N
        jScrollPane2.setViewportView(taStrategy);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbCpy, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(169, 169, 169)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfCapital, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                    .addComponent(tfFrom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfTo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tfCapital, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                    .addComponent(tfFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnAdd)
                                .addComponent(jLabel3))))
                    .addComponent(lbCpy, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
        AccountDAO.createAcct(tfName.getText(), Float.valueOf(tfCapital.getText()), tfFrom.getText(), tfTo.getText(), taStrategy.getText());
        initAcctTable();
    }//GEN-LAST:event_btnAddActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jScrollPane1MouseClicked
    {//GEN-HEADEREND:event_jScrollPane1MouseClicked

    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void tblAcctMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblAcctMouseClicked
    {//GEN-HEADEREND:event_tblAcctMouseClicked
        if(evt.getClickCount()>1)
        {
            List<Account> list = (List<Account>)TableUtil.getSelectedObj(tblAcct, Account.class, 0);
            if(list.isEmpty())
                return;
            JFrame mainFrame = DataAnalyserApp.getApplication().getMainFrame();
            AccountTxnView av = new AccountTxnView(mainFrame, list.get(0));
            av.setLocationRelativeTo(mainFrame);
            //av.setSize(800, 800);
            DataAnalyserApp.getApplication().show(av);
            dispose();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tblAcctMouseClicked
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbCpy;
    private javax.swing.JTextArea taStrategy;
    private javax.swing.JTable tblAcct;
    private javax.swing.JTextField tfCapital;
    private javax.swing.JTextField tfFrom;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfTo;
    // End of variables declaration//GEN-END:variables

    public void initAcctTable()
    {
        String[] col = {"Name", "Cash", "MaketVal", "Profit", "Strategy", "Duration", "Create Time"};
        List<Account> all = AccountDAO.getAll();
        Object[][] data = new Object[all.size()][col.length];
        int i=0;
        for(Account a : all)
        {
            int j=0;
            data[i][j++] = a;
            data[i][j++] = a.getCash();
            data[i][j++] = a.getPortfolioValue();
            data[i][j++] = a.getProfitPerc();
            data[i][j++] = a.getStrategy();
            data[i][j++] = a.getDuration();
            data[i][j++] = DateUtil.dateToTimestamp(a.getCreateTime());
            i++;
        }
        DefaultTableModel model = (DefaultTableModel)tblAcct.getModel();
        model.setDataVector(data, col);


    }
}
