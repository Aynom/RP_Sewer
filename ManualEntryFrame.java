package rationalpeak.RP_Sewer;

import java.net.URL;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.dx.dataset.*;
import com.borland.dbswing.*;

/**
 * Title:        RP_Sewer
 * Description:  A sewer design tool that integrates design discharge estimation (using concepts from RationalPeak) and standard hydraulic equations accompanied by an interactive spatial analysis tool 
 * Copyright:    Copyright (c) 2011
 * @author Aynom Teweldebrhan
 * @version 1.0
 */

public class ManualEntryFrame extends JFrame {
  JPanel contentPane;
  TextDataFile textDataFile1 = new TextDataFile();
  TableDataSet tableDataSet1 = new TableDataSet();
  Column pipeNo = new Column();
  Column us_MH_No = new Column();
  Column ds_MH_No = new Column();
  Column us_MH_x_coord = new Column();
  Column us_MH_y_coord = new Column();
  Column us_MH_z_coord = new Column();
  Column remark = new Column();
  JdbNavToolBar jdbNavToolBar1 = new JdbNavToolBar();
  TableScrollPane tableScrollPane1 = new TableScrollPane();
  JdbTable jdbTable1 = new JdbTable();
  JButton saveChangesButton = new JButton();
  JButton saveAsButton = new JButton();


 //boolean fileNameGiven = false;
 static String fileNamePath = " ";
  //Construct the frame

  // this constructor is used to open ManholeFile with specific name
//  public ManualEntryFrame(String completeFileName) {
//    fileNamePath = completeFileName;
//     System.out.println("filePathName when Load submenu is invoked at ManualEntryFrame Class is"+fileNamePath);
//    this.m_ManualEntry_FrameOpner();
//  }

  public ManualEntryFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception  {

  pipeNo.setCaption("PipeNo");
   pipeNo.setColumnName("PipeNo");
   pipeNo.setDataType(com.borland.dx.dataset.Variant.STRING);
   pipeNo.setSqlType(0);
  us_MH_No.setCaption("US_MH_No");
   us_MH_No.setColumnName("US_MH_No");
   us_MH_No.setDataType(com.borland.dx.dataset.Variant.STRING);
   us_MH_No.setSqlType(0);
  ds_MH_No.setCaption("DS_MH_No");
   ds_MH_No.setColumnName("DS_MH_No");
   ds_MH_No.setDataType(com.borland.dx.dataset.Variant.STRING);
   ds_MH_No.setSqlType(0);
  us_MH_x_coord.setCaption("US_MH_x_coord");
   us_MH_x_coord.setColumnName("US_MH_x_coord");
   us_MH_x_coord.setDataType(com.borland.dx.dataset.Variant.STRING);
   us_MH_x_coord.setSqlType(0);
  us_MH_y_coord.setCaption("US_MH_y_coord");
   us_MH_y_coord.setColumnName("US_MH_y_coord");
   us_MH_y_coord.setDataType(com.borland.dx.dataset.Variant.STRING);
   us_MH_y_coord.setSqlType(0);
  us_MH_z_coord.setCaption("US_MH_z_coord");
   us_MH_z_coord.setColumnName("US_MH_z_coord");
   us_MH_z_coord.setDataType(com.borland.dx.dataset.Variant.STRING);
   us_MH_z_coord.setSqlType(0);
    tableDataSet1.setDataFile(textDataFile1);

    String currentDirectory = System.getProperty("user.dir");







     System.out.println("filePathName when Load submenu is invoked at ManualEntryFrame Class close to setting direcrory is"+fileNamePath);
    if(fileNamePath.equals(" ")){
    textDataFile1.setFileName(currentDirectory+"/Input_Output/RP_Sewer/ManholeFile.rp");
    }else{
    textDataFile1.setFileName(fileNamePath);
      }

    textDataFile1.setSeparator(",");
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(null);
    this.setSize(new Dimension(635, 375));
    this.setTitle("RP_Sewer:  Manhole Input File");

    tableDataSet1.setColumns(new Column[] {pipeNo, us_MH_No,ds_MH_No,us_MH_x_coord,us_MH_y_coord,us_MH_z_coord});

    jdbTable1.setDataSet(tableDataSet1);
    tableScrollPane1.setBounds(new Rectangle(16, 44, 466, 201));
    jdbNavToolBar1.setBounds(new Rectangle(1, 1, 511, 39));
    saveChangesButton.setText("Save changes");
    saveChangesButton.setBounds(new Rectangle(489, 51, 123, 23));
    saveChangesButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        saveChangesButton_actionPerformed(e);
      }
    });
    closeButton.setText("Close");
    closeButton.setBounds(new Rectangle(527, 203, 69, 23));
    closeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        closeButton_actionPerformed(e);
      }
    });
    saveAsButton.setText("Save As");
    saveAsButton.setBounds(new Rectangle(513, 116, 86, 24));
    saveAsButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveAsButton_actionPerformed(e);
      }
    });
    jdbTextArea1.setToolTipText("Status Bar");
    jdbTextArea1.setEditable(false);
    jdbTextArea1.setBounds(new Rectangle(18, 294, 483, 55));
    jdbTextArea1.setBackground(SystemColor.activeCaptionBorder);
    jdbTextArea1.setFont(new Font("Dialog", 0, 12));
    jdbTextArea1.setBorder(BorderFactory.createLoweredBevelBorder());
    jdbTextArea1.setText("* Use the Tool Bar to add, insert, edit or delete records\n"+"** Save before you close the Manual Entry Frame");

    contentPane.add(tableScrollPane1, null);
    contentPane.add(jdbNavToolBar1, null);
    contentPane.add(jdbTextArea1, null);
    contentPane.add(saveChangesButton, null);
    contentPane.add(saveAsButton, null);
    contentPane.add(closeButton, null);
    tableScrollPane1.getViewport().add(jdbTable1, null);
  }

  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
  }

  static int noOfRecords = 0;
  JButton closeButton = new JButton();
  void saveChangesButton_actionPerformed(ActionEvent e) {
    saveChanges();
  }

  public int getNoOfRecords(){
  return noOfRecords;
  }
//  public void getUSMHRecord(){
//  Column USMHRecord = tableDataSet1.getColumn(1);
//
//  System.out.println("USMHRecord   "+USMHRecord);
//  //return USMHRecord.getHash();
//  }




  void closeButton_actionPerformed(ActionEvent e) {
     close();
  }

  void close(){
     if(okToAbandon()){
        dispose();
       }
  }

  boolean saveChanges(){
      try {
      noOfRecords = tableDataSet1.getRowCount();
      tableDataSet1.getDataFile().save(tableDataSet1);
      jdbTextArea1.setText("Changes Saved");
       return true;
    }
    catch (Exception ex) {
      jdbTextArea1.setText("Changes NOT saved : "+ex);
      return false;
    }

  }


   String fileName = "";
  JdbTextArea jdbTextArea1 = new JdbTextArea();
   boolean saveAsFile(){
         try
        {
        JFileChooser chooser = new JFileChooser();
              String currentDirectory = System.getProperty("user.dir");
              File file = new File(currentDirectory + "/Input_Output/RP_Sewer/*.*");
              chooser.setSelectedFile(file);
        int i = chooser.showSaveDialog(this);
        if(i == JFileChooser.APPROVE_OPTION)
        file = new File(chooser.getSelectedFile().getAbsolutePath());
        String f = file.getAbsolutePath();
        f += ".rp";

        textDataFile1.setFileName(f);
        tableDataSet1.getDataFile().save(tableDataSet1);
        noOfRecords = tableDataSet1.getRowCount();
        jdbTextArea1.setText("Changes Saved");
        file = new File(f);
        fileName = f;
        file = null;
        return true;
        }

        catch(Exception ioe)
        {
        jdbTextArea1.setText("Error Saving "+fileName);
        ioe.printStackTrace();
        return false;
        }
     }

    //This method is simply a browser to locate ManholeFile for lading to the viewer using the Load submenu of Manhole Menu
    public String openManholeFile(){
         try
        {
        JFileChooser chooser = new JFileChooser();
              String currentDirectory = System.getProperty("user.dir");
              File file = new File(currentDirectory + "/Input_Output/*.*");
              chooser.setSelectedFile(file);
        int i = chooser.showOpenDialog(this);
        if(i == JFileChooser.APPROVE_OPTION)
        file = new File(chooser.getSelectedFile().getAbsolutePath());
        String f = file.getAbsolutePath();
//        f += ".rp";

//        textDataFile1.setFileName(f);
//        tableDataSet1.getDataFile().save(tableDataSet1);
//        noOfRecords = tableDataSet1.getRowCount();
//        jdbTextArea1.setText("Changes Saved");
        file = new File(f);
        fileName = f;
//        file = null;
//        return true;
          return f;
        }

        catch(Exception ioe)
        {
        jdbTextArea1.setText("Error Openning "+fileName);
        ioe.printStackTrace();
        return null;
        }

     }



  boolean okToAbandon() {
       int value =  JOptionPane.showConfirmDialog(this, "Save Changes?",
                                             "RationalPeak: RP_Sewer", JOptionPane.YES_NO_CANCEL_OPTION) ;
        switch (value) {
           case JOptionPane.YES_OPTION:
             return /*saveAsFile()*/saveChanges();
           case JOptionPane.NO_OPTION:
             return true;
           case JOptionPane.CANCEL_OPTION:
           default:
             // cancel
             return false;
        }
    }

  void saveAsButton_actionPerformed(ActionEvent e) {
    saveAsFile();
  }


   boolean  packFrame = false;
   public void m_ManualEntry_FrameOpner(String completeFileName) {
   fileNamePath = completeFileName;
    ManualEntryFrame frame = new ManualEntryFrame();
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      URL iconURL = getClass().getResource("/Images/RPIcon.gif");
      Image frameIcon = toolkit.getImage(iconURL);
      frame.setIconImage(frameIcon);
     System.out.println("filePathName when Load submenu is invoked at ManualEntryFrame Class in frameOpener method is"+fileNamePath);
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
     if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }



}
