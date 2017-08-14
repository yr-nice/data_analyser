/*
 * DataAnalyserView.java
 */
package com.mu.stock.ui;

import com.mu.stock.chart.CandlestickChart;
import com.mu.stock.dao.CompanyDAO;
import com.mu.stock.dao.DailyPriceDAO;
import com.mu.stock.data.collector.SGDailyDownloader;
import com.mu.stock.data.DataReader;
import com.mu.stock.data.ProcessedDailyData;
import com.mu.stock.data.collector.SGDailyInserter;
import com.mu.stock.entity.Company;
import com.mu.stock.entity.DailyPriceLog;
import com.mu.stock.indicator.CandleStick;
import com.mu.stock.indicator.processor.MACDPrc;
import com.mu.stock.indicator.processor.MFIPrc;
import com.mu.stock.indicator.processor.MomentumPrc;
import com.mu.stock.indicator.processor.OBVPrc;
import com.mu.stock.indicator.processor.PriceROCPrc;
import com.mu.stock.indicator.processor.PriceTrendAnglePrc;
import com.mu.stock.indicator.processor.ProcessorMgr;
import com.mu.stock.indicator.processor.RSIPrc;
import com.mu.stock.indicator.processor.StochasticRPrc;
import com.mu.stock.indicator.processor.WilliamsRPrc;
import com.mu.stock.init.Initiator;
import com.mu.util.MathUtil;
import com.mu.util.StringUtil;
import com.mu.util.log.Log;
import com.mu.util.ui.TableUtil;
import java.awt.Dialog.ModalityType;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;

/**
 * The application's main frame.
 */
public class DataAnalyserView extends FrameView
{

    public DataAnalyserView(SingleFrameApplication app)
    {
        super(app);

        initComponents();

        init();
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++)
        {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {

            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName))
                {
                    if (!busyIconTimer.isRunning())
                    {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                }
                else if ("done".equals(propertyName))
                {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                }
                else if ("message".equals(propertyName))
                {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                }
                else if ("progress".equals(propertyName))
                {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox()
    {
        if (aboutBox == null)
        {
            JFrame mainFrame = DataAnalyserApp.getApplication().getMainFrame();
            aboutBox = new DataAnalyserAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DataAnalyserApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTblCompany = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        tfNameFilter = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDailyPrice = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        ltFilter = new javax.swing.JList();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        miDownloadHistoricalData = new javax.swing.JMenuItem();
        miProcessData = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        miAddCompany = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        miShowAccts = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        ppmCpy = new javax.swing.JPopupMenu();
        mi1m1 = new javax.swing.JMenuItem();
        mi3m1 = new javax.swing.JMenuItem();
        mi6m1 = new javax.swing.JMenuItem();
        mi1y1 = new javax.swing.JMenuItem();
        mi3y1 = new javax.swing.JMenuItem();
        mi5y1 = new javax.swing.JMenuItem();
        miAll1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miCpyCalc = new javax.swing.JMenuItem();
        miCpyUpdate = new javax.swing.JMenuItem();
        miCpyReset = new javax.swing.JMenuItem();
        miRemoveCpy = new javax.swing.JMenuItem();
        ppmDp = new javax.swing.JPopupMenu();
        miShowFinder = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        miDPChart = new javax.swing.JMenuItem();
        mi1m = new javax.swing.JMenuItem();
        mi3m = new javax.swing.JMenuItem();
        mi6m = new javax.swing.JMenuItem();
        mi1y = new javax.swing.JMenuItem();
        mi3y = new javax.swing.JMenuItem();
        mi5y = new javax.swing.JMenuItem();
        miAll = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jSplitPane2.setDividerLocation(300);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTblCompany.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Code", "Last", "%", "Previous", "Tags"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblCompany.setName("jTblCompany"); // NOI18N
        jTblCompany.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblCompanyMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTblCompanyMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTblCompany);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.mu.stock.ui.DataAnalyserApp.class).getContext().getResourceMap(DataAnalyserView.class);
        jTblCompany.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTblCompany.columnModel.title0")); // NOI18N
        jTblCompany.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTblCompany.columnModel.title1")); // NOI18N
        jTblCompany.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTblCompany.columnModel.title2")); // NOI18N
        jTblCompany.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTblCompany.columnModel.title4")); // NOI18N
        jTblCompany.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTblCompany.columnModel.title5")); // NOI18N
        jTblCompany.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("jTblCompany.columnModel.title6")); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        tfNameFilter.setText(resourceMap.getString("tfNameFilter.text")); // NOI18N
        tfNameFilter.setName("tfNameFilter"); // NOI18N
        tfNameFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfNameFilterKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfNameFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
        );

        jSplitPane2.setTopComponent(jPanel1);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        tblDailyPrice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Close", "High", "Low", "%", "Gravity"
            }
        ));
        tblDailyPrice.setName("tblDailyPrice"); // NOI18N
        tblDailyPrice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDailyPriceMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblDailyPrice);
        tblDailyPrice.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tblDailyPrice.columnModel.title0")); // NOI18N
        tblDailyPrice.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tblDailyPrice.columnModel.title1")); // NOI18N
        tblDailyPrice.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("tblDailyPrice.columnModel.title2")); // NOI18N
        tblDailyPrice.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("tblDailyPrice.columnModel.title3")); // NOI18N
        tblDailyPrice.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("tblDailyPrice.columnModel.title4")); // NOI18N
        tblDailyPrice.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("tblDailyPrice.columnModel.title5")); // NOI18N

        jSplitPane2.setRightComponent(jScrollPane3);

        jSplitPane1.setRightComponent(jSplitPane2);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        ltFilter.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        ltFilter.setName("ltFilter"); // NOI18N
        ltFilter.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ltFilterValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(ltFilter);

        jSplitPane1.setLeftComponent(jScrollPane1);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.mu.stock.ui.DataAnalyserApp.class).getContext().getActionMap(DataAnalyserView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        miDownloadHistoricalData.setText(resourceMap.getString("miDownloadHistoricalData.text")); // NOI18N
        miDownloadHistoricalData.setName("miDownloadHistoricalData"); // NOI18N
        miDownloadHistoricalData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miDownloadHistoricalDataActionPerformed(evt);
            }
        });
        fileMenu.add(miDownloadHistoricalData);

        miProcessData.setText(resourceMap.getString("miProcessData.text")); // NOI18N
        miProcessData.setName("miProcessData"); // NOI18N
        miProcessData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miProcessDataActionPerformed(evt);
            }
        });
        fileMenu.add(miProcessData);

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        miAddCompany.setText(resourceMap.getString("miAddCompany.text")); // NOI18N
        miAddCompany.setName("miAddCompany"); // NOI18N
        miAddCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAddCompanyActionPerformed(evt);
            }
        });
        fileMenu.add(miAddCompany);

        menuBar.add(fileMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        miShowAccts.setText(resourceMap.getString("miShowAccts.text")); // NOI18N
        miShowAccts.setName("miShowAccts"); // NOI18N
        miShowAccts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miShowAcctsActionPerformed(evt);
            }
        });
        jMenu1.add(miShowAccts);

        menuBar.add(jMenu1);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 793, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        ppmCpy.setName("ppmCpy"); // NOI18N

        mi1m1.setText(resourceMap.getString("mi1m1.text")); // NOI18N
        mi1m1.setName("mi1m1"); // NOI18N
        mi1m1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi1m1ActionPerformed(evt);
            }
        });
        ppmCpy.add(mi1m1);

        mi3m1.setText(resourceMap.getString("mi3m1.text")); // NOI18N
        mi3m1.setName("mi3m1"); // NOI18N
        mi3m1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi3m1ActionPerformed(evt);
            }
        });
        ppmCpy.add(mi3m1);

        mi6m1.setText(resourceMap.getString("mi6m1.text")); // NOI18N
        mi6m1.setName("mi6m1"); // NOI18N
        mi6m1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi6m1ActionPerformed(evt);
            }
        });
        ppmCpy.add(mi6m1);

        mi1y1.setText(resourceMap.getString("mi1y1.text")); // NOI18N
        mi1y1.setName("mi1y1"); // NOI18N
        mi1y1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi1y1ActionPerformed(evt);
            }
        });
        ppmCpy.add(mi1y1);

        mi3y1.setText(resourceMap.getString("mi3y1.text")); // NOI18N
        mi3y1.setName("mi3y1"); // NOI18N
        mi3y1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi3y1ActionPerformed(evt);
            }
        });
        ppmCpy.add(mi3y1);

        mi5y1.setText(resourceMap.getString("mi5y1.text")); // NOI18N
        mi5y1.setName("mi5y1"); // NOI18N
        mi5y1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi5y1ActionPerformed(evt);
            }
        });
        ppmCpy.add(mi5y1);

        miAll1.setText(resourceMap.getString("miAll1.text")); // NOI18N
        miAll1.setName("miAll1"); // NOI18N
        miAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAll1ActionPerformed(evt);
            }
        });
        ppmCpy.add(miAll1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        ppmCpy.add(jSeparator1);

        miCpyCalc.setText(resourceMap.getString("miCpyCalc.text")); // NOI18N
        miCpyCalc.setName("miCpyCalc"); // NOI18N
        miCpyCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCpyCalcActionPerformed(evt);
            }
        });
        ppmCpy.add(miCpyCalc);

        miCpyUpdate.setText(resourceMap.getString("miCpyUpdate.text")); // NOI18N
        miCpyUpdate.setName("miCpyUpdate"); // NOI18N
        miCpyUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCpyUpdateActionPerformed(evt);
            }
        });
        ppmCpy.add(miCpyUpdate);

        miCpyReset.setText(resourceMap.getString("miCpyReset.text")); // NOI18N
        miCpyReset.setName("miCpyReset"); // NOI18N
        miCpyReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miCpyResetActionPerformed(evt);
            }
        });
        ppmCpy.add(miCpyReset);

        miRemoveCpy.setText(resourceMap.getString("miRemoveCpy.text")); // NOI18N
        miRemoveCpy.setName("miRemoveCpy"); // NOI18N
        miRemoveCpy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRemoveCpyActionPerformed(evt);
            }
        });
        ppmCpy.add(miRemoveCpy);

        ppmDp.setName("ppmDp"); // NOI18N

        miShowFinder.setText(resourceMap.getString("miShowFinder.text")); // NOI18N
        miShowFinder.setName("miShowFinder"); // NOI18N
        miShowFinder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miShowFinderActionPerformed(evt);
            }
        });
        ppmDp.add(miShowFinder);

        jSeparator2.setName("jSeparator2"); // NOI18N
        ppmDp.add(jSeparator2);

        miDPChart.setText(resourceMap.getString("miDPChart.text")); // NOI18N
        miDPChart.setName("miDPChart"); // NOI18N
        miDPChart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miDPChartActionPerformed(evt);
            }
        });
        ppmDp.add(miDPChart);

        mi1m.setText(resourceMap.getString("mi1m.text")); // NOI18N
        mi1m.setName("mi1m"); // NOI18N
        mi1m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi1mActionPerformed(evt);
            }
        });
        ppmDp.add(mi1m);

        mi3m.setText(resourceMap.getString("mi3m.text")); // NOI18N
        mi3m.setName("mi3m"); // NOI18N
        mi3m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi3mActionPerformed(evt);
            }
        });
        ppmDp.add(mi3m);

        mi6m.setText(resourceMap.getString("mi6m.text")); // NOI18N
        mi6m.setName("mi6m"); // NOI18N
        mi6m.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi6mActionPerformed(evt);
            }
        });
        ppmDp.add(mi6m);

        mi1y.setText(resourceMap.getString("mi1y.text")); // NOI18N
        mi1y.setName("mi1y"); // NOI18N
        mi1y.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi1yActionPerformed(evt);
            }
        });
        ppmDp.add(mi1y);

        mi3y.setText(resourceMap.getString("mi3y.text")); // NOI18N
        mi3y.setName("mi3y"); // NOI18N
        mi3y.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi3yActionPerformed(evt);
            }
        });
        ppmDp.add(mi3y);

        mi5y.setText(resourceMap.getString("mi5y.text")); // NOI18N
        mi5y.setName("mi5y"); // NOI18N
        mi5y.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mi5yActionPerformed(evt);
            }
        });
        ppmDp.add(mi5y);

        miAll.setText(resourceMap.getString("miAll.text")); // NOI18N
        miAll.setName("miAll"); // NOI18N
        miAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAllActionPerformed(evt);
            }
        });
        ppmDp.add(miAll);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void miDownloadHistoricalDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDownloadHistoricalDataActionPerformed
    {//GEN-HEADEREND:event_miDownloadHistoricalDataActionPerformed
        SGDailyDownloader.downloadFromYahoo();
    }//GEN-LAST:event_miDownloadHistoricalDataActionPerformed

    private void miProcessDataActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miProcessDataActionPerformed
    {//GEN-HEADEREND:event_miProcessDataActionPerformed
        LinkedList<ProcessedDailyData> list = DataReader.read("historical_data/DBS_D05.csv");
        PriceTrendAnglePrc.process(list, 1, 17);
        MACDPrc.process(list, 12, 26, 9);
        RSIPrc.process(list, 14);
        OBVPrc.process(list);
        PriceROCPrc.process(list, 12);
        MomentumPrc.process(list, 12);
        MFIPrc.process(list, 14);
        WilliamsRPrc.process(list, 14);
        StochasticRPrc.process(list, 14, 3, 3);
        for (ProcessedDailyData p : list)
        {
            Log.log("%s, MACD:%s, Signal:%s, RSI:%s, OBV:%d, ROC:%s, Momentum:%s, MFI:%s, WilliamsR:%s, Stochastic:%s, Stochastic signal:%s",
                    p.getDate(), p.getMacd(), p.getMacdSignal(), p.getRsi(), p.getObv(), p.getRoc(), p.getMomentum(),
                    p.getMfi(), p.getWilliams(), p.getStochastic(), p.getStochasticSignal());
        }
    }//GEN-LAST:event_miProcessDataActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        int i = TableUtil.getFirstVisibleRow(tblDailyPrice);
        tblDailyPrice.getSelectionModel().setSelectionInterval(i, i);

        /*if (dpFinder == null)
        {
        JFrame mainFrame = DataAnalyserApp.getApplication().getMainFrame();
        dpFinder = new DailyPriceFinder(mainFrame);
        dpFinder.setLocationRelativeTo(mainFrame);
        dpFinder.setTblTarget(tblDailyPrice);
        dpFinder.setModalityType(ModalityType.MODELESS);
        }
        DataAnalyserApp.getApplication().show(dpFinder);
        /*
        tblDailyPrice.getSelectionModel().setSelectionInterval(100, 102);
        TableUtil.scrollToVisible(tblDailyPrice, 100, 0);
        /*TagFilter tf = new TagFilter("Finance");

        sorter.setRowFilter(new TagFilter("Finance"));

        
        /*for (int i = 0; i < 7; i++)
        {
        Log.info("Col%d, editable:%b", i, jTblCompany.getModel().isCellEditable(10, i));
        }
        Log.info("editCellAt:%b", jTblCompany.editCellAt(10, 6));
        //TableColumn col = jTblCompany.getColumn("Tags");
        //col.setCellEditor(null);
        Log.info("getCellSelectionEnabled:%b", jTblCompany.getCellSelectionEnabled());


        /*Company dbs = CompanyDAO.getOrCreate("500", "sgx", "DBS");
        List<DailyPriceLog> data = DailyPriceDAO.getByCompany(dbs, DateUtil.toDate("2011-08-04"), null);
        DailyPriceDAO.remove(data);
        //List<DailyPriceLog> data = DailyPriceDAO.getByCompany(dbs, DateUtil.toDate("2010-06-01"), DateUtil.toDate("2011-01-01"));
        /*List<DailyPriceLog> data = DailyPriceDAO.getByCompany(dbs, null, null);
        SMACalc.reset(data);
        SMACalc.process(data);
        for(DailyPriceLog d : data)
        {
        if(d.getLow()<1F)
        {
        d.setLow(d.getClose());
        DailyPriceDAO.update(d);
        }
        Log.info(ReflectionUtil.toString(d));
        }*/
        /* //DailyPriceLog last = data.get(data.size()-1);
        //Log.info(ReflectionUtil.toString(last));
        //DailyPriceDAO.update(data);
        //DailyPriceDAO.update(data);
        
        Log.info("retrieved %d records", data.size());
        JFreeChart c = CandlestickChart.createChart(data);
        ChartPanel chPanel = new ChartPanel(c);
        //chPanel.setPreferredSize(new Dimension(785, 440));
        //chPanel.setVisible(true);
        jSplitPane2.setRightComponent(chPanel);
         */

        /*java.awt.image.BufferedImage image = c.createBufferedImage(600,400);
        JLabel label = new JLabel(new ImageIcon(image));  
        jpChart.add(label);
         *
         */
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTblCompanyMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTblCompanyMouseReleased
    {//GEN-HEADEREND:event_jTblCompanyMouseReleased
        if (evt.isPopupTrigger())
        {
            ppmCpy.show(evt.getComponent(), evt.getX(), evt.getY());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTblCompanyMouseReleased

    private void miCpyCalcActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miCpyCalcActionPerformed
    {//GEN-HEADEREND:event_miCpyCalcActionPerformed
        List<Company> list = getSelectedCpy();
        for (Company c : list)
        {
            Log.info("Calculating %s.%s...", c, c.getQuote());
            List<DailyPriceLog> l = DailyPriceDAO.getByCompany(c, null, null);
            ProcessorMgr.process(l);
            DailyPriceDAO.update(l);
        }
    }//GEN-LAST:event_miCpyCalcActionPerformed

    private void miCpyUpdateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miCpyUpdateActionPerformed
    {//GEN-HEADEREND:event_miCpyUpdateActionPerformed
        List<Company> list = getSelectedCpy();
        for (Company c : list)
        {
            File f = SGDailyDownloader.downloadUpdates(c);
            List<DailyPriceLog> l = DailyPriceDAO.getByCompany(c, null, null);
            List<DailyPriceLog> inserted = SGDailyInserter.proccessFile(f);
            l.addAll(inserted);
            ProcessorMgr.process(l);
            DailyPriceDAO.update(inserted);
        }
    }//GEN-LAST:event_miCpyUpdateActionPerformed

    private void miCpyResetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miCpyResetActionPerformed
    {//GEN-HEADEREND:event_miCpyResetActionPerformed
        List<Company> list = getSelectedCpy();
        for (Company c : list)
        {
            Log.info("Resetting %s.%s...", c, c.getQuote());
            List<DailyPriceLog> l = DailyPriceDAO.getByCompany(c, null, null);
            ProcessorMgr.reset(l);
            DailyPriceDAO.update(l);
        }
    }//GEN-LAST:event_miCpyResetActionPerformed

    private void ltFilterValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_ltFilterValueChanged
    {//GEN-HEADEREND:event_ltFilterValueChanged
        filterCpyTbl();
    }//GEN-LAST:event_ltFilterValueChanged

    private void tfNameFilterKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tfNameFilterKeyReleased
    {//GEN-HEADEREND:event_tfNameFilterKeyReleased
        filterCpyTbl();
    }//GEN-LAST:event_tfNameFilterKeyReleased

    private void miShowFinderActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miShowFinderActionPerformed
    {//GEN-HEADEREND:event_miShowFinderActionPerformed
        if (dpFinder == null)
        {
            JFrame mainFrame = DataAnalyserApp.getApplication().getMainFrame();
            dpFinder = new DailyPriceFinder(mainFrame);
            dpFinder.setLocationRelativeTo(mainFrame);
            dpFinder.setTblTarget(tblDailyPrice);
            dpFinder.setModalityType(ModalityType.MODELESS);
        }
        DataAnalyserApp.getApplication().show(dpFinder);
        // TODO add your handling code here:
    }//GEN-LAST:event_miShowFinderActionPerformed

    private void tblDailyPriceMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblDailyPriceMouseReleased
    {//GEN-HEADEREND:event_tblDailyPriceMouseReleased
        if (evt.isPopupTrigger())
        {
            ppmDp.show(evt.getComponent(), evt.getX(), evt.getY());
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDailyPriceMouseReleased

    private void miAddCompanyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miAddCompanyActionPerformed
    {//GEN-HEADEREND:event_miAddCompanyActionPerformed
        if (cpyAdder == null)
        {
            JFrame mainFrame = DataAnalyserApp.getApplication().getMainFrame();
            cpyAdder = new AddCompanyView(mainFrame);
            cpyAdder.setLocationRelativeTo(mainFrame);
            cpyAdder.setMainView(this);
        }
        DataAnalyserApp.getApplication().show(cpyAdder);
        // TODO add your handling code here:
    }//GEN-LAST:event_miAddCompanyActionPerformed

    private void miRemoveCpyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miRemoveCpyActionPerformed
    {//GEN-HEADEREND:event_miRemoveCpyActionPerformed
        List<Company> list = getSelectedCpy();
        for (Company c : list)
        {
            CompanyDAO.remove(c);
        }
        initCpyTbl();
    }//GEN-LAST:event_miRemoveCpyActionPerformed

    private void miDPChartActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miDPChartActionPerformed
    {//GEN-HEADEREND:event_miDPChartActionPerformed
        showSelectedChart();
        // TODO add your handling code here:
    }//GEN-LAST:event_miDPChartActionPerformed

    private void jTblCompanyMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTblCompanyMouseClicked
    {//GEN-HEADEREND:event_jTblCompanyMouseClicked
        if (evt.getClickCount() > 1)
        {
            populateDpTable();
        }
    }//GEN-LAST:event_jTblCompanyMouseClicked

    private void miShowAcctsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miShowAcctsActionPerformed
    {//GEN-HEADEREND:event_miShowAcctsActionPerformed
        JFrame mainFrame = DataAnalyserApp.getApplication().getMainFrame();
        AccountView acctView = new AccountView(mainFrame);
        acctView.setLocationRelativeTo(mainFrame);
        DataAnalyserApp.getApplication().show(acctView);

    }//GEN-LAST:event_miShowAcctsActionPerformed

    private void mi1m1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi1m1ActionPerformed
    {//GEN-HEADEREND:event_mi1m1ActionPerformed
        showCpyChart(23);
        // TODO add your handling code here:
}//GEN-LAST:event_mi1m1ActionPerformed

    private void mi3m1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi3m1ActionPerformed
    {//GEN-HEADEREND:event_mi3m1ActionPerformed
        showCpyChart(23 * 3);
        // TODO add your handling code here:
}//GEN-LAST:event_mi3m1ActionPerformed

    private void mi6m1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi6m1ActionPerformed
    {//GEN-HEADEREND:event_mi6m1ActionPerformed
        showCpyChart(23 * 6);
        // TODO add your handling code here:
}//GEN-LAST:event_mi6m1ActionPerformed

    private void mi1y1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi1y1ActionPerformed
    {//GEN-HEADEREND:event_mi1y1ActionPerformed
        showCpyChart(255);
}//GEN-LAST:event_mi1y1ActionPerformed

    private void mi3y1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi3y1ActionPerformed
    {//GEN-HEADEREND:event_mi3y1ActionPerformed
        showCpyChart(255 * 3);
}//GEN-LAST:event_mi3y1ActionPerformed

    private void mi5y1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi5y1ActionPerformed
    {//GEN-HEADEREND:event_mi5y1ActionPerformed
        showCpyChart(255 * 5);
}//GEN-LAST:event_mi5y1ActionPerformed

    private void miAll1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miAll1ActionPerformed
    {//GEN-HEADEREND:event_miAll1ActionPerformed
        showCpyChart(Integer.MAX_VALUE);
}//GEN-LAST:event_miAll1ActionPerformed

    private void mi1mActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi1mActionPerformed
    {//GEN-HEADEREND:event_mi1mActionPerformed
        showChart(23);
}//GEN-LAST:event_mi1mActionPerformed

    private void mi3mActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi3mActionPerformed
    {//GEN-HEADEREND:event_mi3mActionPerformed
        showChart(23 * 3);
}//GEN-LAST:event_mi3mActionPerformed

    private void mi6mActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi6mActionPerformed
    {//GEN-HEADEREND:event_mi6mActionPerformed
        showChart(23 * 6);
}//GEN-LAST:event_mi6mActionPerformed

    private void mi1yActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi1yActionPerformed
    {//GEN-HEADEREND:event_mi1yActionPerformed
        showChart(255);
}//GEN-LAST:event_mi1yActionPerformed

    private void mi3yActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi3yActionPerformed
    {//GEN-HEADEREND:event_mi3yActionPerformed
        showChart(255 * 3);
}//GEN-LAST:event_mi3yActionPerformed

    private void mi5yActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mi5yActionPerformed
    {//GEN-HEADEREND:event_mi5yActionPerformed
        showChart(255 * 5);
}//GEN-LAST:event_mi5yActionPerformed

    private void miAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_miAllActionPerformed
    {//GEN-HEADEREND:event_miAllActionPerformed
        showChart(Integer.MAX_VALUE);
}//GEN-LAST:event_miAllActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTblCompany;
    private javax.swing.JList ltFilter;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mi1m;
    private javax.swing.JMenuItem mi1m1;
    private javax.swing.JMenuItem mi1y;
    private javax.swing.JMenuItem mi1y1;
    private javax.swing.JMenuItem mi3m;
    private javax.swing.JMenuItem mi3m1;
    private javax.swing.JMenuItem mi3y;
    private javax.swing.JMenuItem mi3y1;
    private javax.swing.JMenuItem mi5y;
    private javax.swing.JMenuItem mi5y1;
    private javax.swing.JMenuItem mi6m;
    private javax.swing.JMenuItem mi6m1;
    private javax.swing.JMenuItem miAddCompany;
    private javax.swing.JMenuItem miAll;
    private javax.swing.JMenuItem miAll1;
    private javax.swing.JMenuItem miCpyCalc;
    private javax.swing.JMenuItem miCpyReset;
    private javax.swing.JMenuItem miCpyUpdate;
    private javax.swing.JMenuItem miDPChart;
    private javax.swing.JMenuItem miDownloadHistoricalData;
    private javax.swing.JMenuItem miProcessData;
    private javax.swing.JMenuItem miRemoveCpy;
    private javax.swing.JMenuItem miShowAccts;
    private javax.swing.JMenuItem miShowFinder;
    private javax.swing.JPopupMenu ppmCpy;
    private javax.swing.JPopupMenu ppmDp;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTable tblDailyPrice;
    private javax.swing.JTextField tfNameFilter;
    // End of variables declaration//GEN-END:variables

    private void init()
    {
        Initiator.init();
        initCpyTbl();
        initFilter();
    }

    private void initFilter()
    {
        List<String> filters = CompanyDAO.getTags();
        DefaultListModel modle = new DefaultListModel();
        modle.clear();
        for (String s : filters)
        {
            modle.addElement(s);
        }
        ltFilter.setModel(modle);
        ltFilter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    public void initCpyTbl()
    {
        List<Company> l = CompanyDAO.getAll();

        Object[][] data = new Object[l.size()][jTblCompany.getColumnCount()];
        int i = 0;
        for (Company p : l)
        {
            data[i][0] = p;
            data[i][1] = p.getQuote();
            data[i][5] = p.getTags();
            i++;
        }
        DefaultTableModel m = (DefaultTableModel) jTblCompany.getModel();
        m.setDataVector(data, cpyCol);
        //jTblCompany.setCellSelectionEnabled(true);
        //jTblCompany.getColumnModel().
        jTblCompany.setAutoCreateRowSorter(true);
        jTblCompany.getModel().addTableModelListener(new CompanyUpdater());
        sorter = new TableRowSorter(jTblCompany.getModel());
        jTblCompany.setRowSorter(sorter);
    }

    private void filterCpyTbl()
    {
        String tag = (String) ltFilter.getSelectedValue();
        TagFilter tg = new TagFilter(tag);
        NameFilter nf = new NameFilter(tfNameFilter.getText());
        sorter.setRowFilter(RowFilter.andFilter(Arrays.asList(tg, nf)));
    }

    private void populateDpTable()
    {
        List<Company> list = getSelectedCpy();
        if (list.isEmpty())
        {
            return;
        }
        Company cpy = list.get(0);
        List<DailyPriceLog> data = DailyPriceDAO.getByCompany(cpy, null, null);
        //SMACalc.reset(data);
        //SMACalc.process(data);
        DefaultTableModel model = (DefaultTableModel) tblDailyPrice.getModel();
        String[] col =
        {
            "Date", "Close", "High", "Low", "Open", "%P", "Gravity", "%G", "Vol", "Vol Ratio", "CO", "HL", "Type"
        };
        Object[][] rows = new Object[data.size()][col.length];
        int i = 0;
        for (DailyPriceLog d : data)
        {
            Float gp = (d.getAdjClose() - d.getHLYGravity()) * 100 / d.getHLYGravity();
            CandleStick c = new CandleStick(d.getAdjOpen(), d.getAdjClose(), d.getAdjHigh(), d.getAdjLow());
            int j = 0;
            rows[i][j++] = d;
            rows[i][j++] = d.getAdjClose();
            rows[i][j++] = d.getAdjHigh();
            rows[i][j++] = d.getAdjLow();
            rows[i][j++] = d.getAdjOpen();
            rows[i][j++] = d.getChangePer();
            rows[i][j++] = d.getHLYGravity();
            rows[i][j++] = MathUtil.round(gp, 2);
            rows[i][j++] = d.getVolume();
            rows[i][j++] = d.getVolRatio();
            rows[i][j++] = MathUtil.round(c.getDiffPerCO(), 2);
            rows[i][j++] = MathUtil.round(c.getDiffPerHL(), 2);
            rows[i][j++] = c.getType();
            i++;
            //Log.info(ReflectionUtil.toString(d));
        }
        model.setDataVector(rows, col);

    }

    private void showCpyChart(int lastNday)
    {
        List<Company> list = getSelectedCpy();
        if (list.isEmpty())
        {
            return;
        }
        Company cpy = list.get(0);
        showChart(cpy, new Date(), lastNday);
    }

    private void showChart(Company c, Date showTo, int lastNday)
    {
        JFrame mainFrame = DataAnalyserApp.getApplication().getMainFrame();
        ChartView  cv = new ChartView(mainFrame, c, showTo, lastNday);
        cv.setLocationRelativeTo(mainFrame);
        DataAnalyserApp.getApplication().show(cv);
    }
    private void showSelectedChart()
    {
        List<DailyPriceLog> list = TableUtil.getSelectedObj(tblDailyPrice, DailyPriceLog.class, 0);
        int lastNday = list.size();
        showChart(list.get(0).getCompany(), list.get(lastNday-1).getDate(), lastNday);
    }

    private void showChart(int lastNday)
    {
        DailyPriceLog d = (DailyPriceLog)TableUtil.getLastObj(tblDailyPrice, DailyPriceLog.class, 0);
        Company cpy = d.getCompany();
        showChart(cpy, new Date(), lastNday);
    }

    private List<Company> getSelectedCpy()
    {
        ArrayList<Company> re = new ArrayList<Company>();
        int[] arr = jTblCompany.getSelectedRows();
        for (int i : arr)
        {
            Object o = jTblCompany.getModel().getValueAt(jTblCompany.convertRowIndexToModel(i), 0);
            if (o instanceof Company)
            {
                re.add((Company) o);
            }
            else
            {
                Log.info("Unknown Selected Cpy, " + o);
            }
        }
        return re;
    }

    private List<DailyPriceLog> getSelectedDp()
    {
        ArrayList<DailyPriceLog> re = new ArrayList<DailyPriceLog>();
        int[] arr = tblDailyPrice.getSelectedRows();
        for (int i : arr)
        {
            Object o = tblDailyPrice.getModel().getValueAt(tblDailyPrice.convertRowIndexToModel(i), 0);
            if (o instanceof DailyPriceLog)
            {
                re.add((DailyPriceLog) o);
            }
            else
            {
                Log.info("Unknown Selected Dp, " + o);
            }
        }
        return re;
    }

    /*private void drawChart(List<DailyPriceLog> data)
    {
        JFreeChart c = CandlestickChart.createChart(data);
        ((XYPlot) c.getPlot()).addRangeMarker(new ValueMarker(2));
        ChartPanel chPanel = new ChartPanel(c);
        //chPanel.setPreferredSize(new Dimension(785, 440));
        //chPanel.setVisible(true);
        jSplitPane3.setRightComponent(chPanel);

    }*/
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private String[] cpyCol = new String[]
    {
        "Name", "Quote", "Last", "%", "Previous", "Tags"
    };
    private TableRowSorter sorter;
    private DailyPriceFinder dpFinder;
    private AddCompanyView cpyAdder;

    class CompanyUpdater implements TableModelListener
    {

        public void tableChanged(TableModelEvent e)
        {
            TableModel model = (TableModel) e.getSource();
            if (e.getFirstRow() < 0)
            {
                return;
            }
            Company c = (Company) model.getValueAt(e.getFirstRow(), 0);
            String tags = (String) model.getValueAt(e.getFirstRow(), e.getColumn());
            c.setTags(tags);
            CompanyDAO.update(c);
            initFilter();
        }
    }

    class TagFilter extends RowFilter<Object, Object>
    {

        String tag;

        public TagFilter(String tag)
        {
            this.tag = tag;
        }

        public boolean include(Entry<? extends Object, ? extends Object> entry)
        {
            String tags = entry.getStringValue(5) + ",";
            if (StringUtil.isEmpty(tag) || tag.equalsIgnoreCase("all") || tags.indexOf(tag) != -1)
            {
                return true;
            }
            return false;
        }
    }

    class NameFilter extends RowFilter<Object, Object>
    {

        String name;

        public NameFilter(String name)
        {
            this.name = name.trim();
        }

        public boolean include(Entry<? extends Object, ? extends Object> entry)
        {
            String n = entry.getStringValue(0).toString();
            if (StringUtil.isEmpty(name) || n.toLowerCase().startsWith(name))
            {
                return true;
            }
            return false;
        }
    }
}
