package rationalpeak.RP_Sewer;

/**
 * Title:        RP_Sewer
 * Description:  A sewer design tool that integrates design discharge estimation (using concepts from RationalPeak) and standard hydraulic equations accompanied by an interactive spatial analysis tool 
 * Copyright:    Copyright (c) 2011
 * @author Aynom Teweldebrhan
 * @version 1.0
 */
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.dbswing.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;
import java.net.URL;

import rationalpeak.Help.HelpDialog;
import rationalpeak.Model.GUI.About_Box;


public class GeneralDAreaInfoFrame extends JFrame{

SewerDAreaMInputFile_Writer m_IOInputHashTable = new SewerDAreaMInputFile_Writer();
static int subDAreaCounter=1;
static String m = " " ;
int intNoOfSubcts = 0;
String stSubCatOpn = " ";
Object NoOfSubCts;
Object subCatOpn;


  //create the UI container
  JPanel contentPane;
  ImageIcon image1;
  //create menu bar and menu bar items
  JMenuBar menuBar1 = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuToMainWindow = new JMenuItem();
  JMenuItem menuFileExit = new JMenuItem();
  JMenu menuHelp = new JMenu();
  JMenuItem RationalPeak = new JMenuItem();
  JMenuItem About = new JMenuItem();

  JPanel jPanel1 = new JPanel();

//  JLabel image_Label1 = new JLabel();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jPanel2 = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
//  JTextPane jTextPane2 = new JTextPane();
  JButton jButton_AllSubareasDelineated_No = new JButton();

  JScrollPane notesScrollPane = new JScrollPane();
  JdbTextArea notesTextArea = new JdbTextArea();


   //Construct the dialog 1st option
  public GeneralDAreaInfoFrame() {
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

    //initialize content pane
    contentPane = (JPanel) this.getContentPane();
    titledBorder1 = new TitledBorder("");
    this.setSize(new Dimension(700, 550));
    this.setTitle("RationalPeak Model: RP_Sewer");
//    image1 = new ImageIcon(Welcome_Frame.class.getResource("/Images/MVC_001F.gif"));
    //initialize menu items and create events
    menuFile.setText("File");
    menuToMainWindow.setText("Abort");
    menuToMainWindow.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
      }
    });
    menuFileExit.setText("Exit");
    menuFileExit.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
//        fileExit_actionPerformed(e);
     dispose();
      }
    });
    menuHelp.setText("Help");
    RationalPeak.setText("RationalPeak");
    RationalPeak.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RationalPeak_actionPerformed(e);
      }
    });
    About.setText("About");
    About.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        helpAbout_Box(e);
      }
    });



    //initialize panel and checkboxes and set up events
    jPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel1.setOpaque(false);
    jPanel1.setLayout(xYLayout1);

    //add components
    this.setJMenuBar(menuBar1);

    jPanel2.setLayout(xYLayout2);
    jPanel2.setBorder(BorderFactory.createRaisedBevelBorder());

    notesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    notesTextArea.setBackground(SystemColor.activeCaptionBorder);
    notesTextArea.setFont(new Font("Dialog", 0, 12));
    notesTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
    notesTextArea.setEditable(false);
    notesTextArea.setText("Enter the Mouse to any field for further information");

    jButton_AllSubareasDelineated_No.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_AllSubareasDelineated_No.setText("NO");
    jButton_AllSubareasDelineated_No.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_AllSubareasDelineated_No_actionPerformed(e);
      }
    });

    jPanel3.setLayout(xYLayout3);
    jLabel_Heading.setFont(new java.awt.Font("Dialog", 3, 16));
    jLabel_Heading.setForeground(Color.blue);
    jLabel_Heading.setOpaque(true);
    jLabel_Heading.setRequestFocusEnabled(false);
    jLabel_Heading.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel_Heading.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabel_Heading.setText("Drainage Area General Information");
    jLabel_image.setIcon(image1);

    jLabel_RainfallIntensity.setText("RainfallIntensity (mm/hr)");
    jLabel_RainfallIntensity.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel_RainfallIntensity.setForeground(Color.pink);
    jLabel_RainfallIntensity.setBackground(SystemColor.scrollbar);
    jLabel_PipeManning_n.setBackground(SystemColor.scrollbar);
    jLabel_PipeManning_n.setForeground(Color.pink);
    jLabel_PipeManning_n.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel_PipeManning_n.setText("Manning Roughness Coefficient of Pipe");
    jLabel_ManholeDrained.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel_ManholeDrained.setForeground(Color.pink);
    jLabel_ManholeDrained.setText("Manhole No. to Which the Area is Drained");
    jLabel_DAreaCompleteOption.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel_DAreaCompleteOption.setForeground(Color.red);
    jLabel_DAreaCompleteOption.setText("Are all subareas delineated?");
    jButton_AllSubareasDelineated_Yes.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_AllSubareasDelineated_Yes.setToolTipText("");
    jButton_AllSubareasDelineated_Yes.setText("YES");
    jButton_AllSubareasDelineated_Yes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_AllSubareasDelineated_Yes_actionPerformed(e);
      }
    });
    menuFile.add(menuToMainWindow);
    menuFile.add(menuFileExit);
    menuBar1.add(menuFile);
    menuBar1.add(menuHelp);
    menuHelp.add(RationalPeak);
    menuHelp.add(About);
    contentPane.add(jPanel2, BorderLayout.SOUTH);

    jPanel2.add(notesTextArea,   new XYConstraints(12, 19, 357, 86));
    contentPane.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel_Heading,     new XYConstraints(29, 8, 515, 36));
    contentPane.add(jPanel3, BorderLayout.CENTER);
    jPanel3.add(jLabel_image, new XYConstraints(12, 343, 675, 46));
    jPanel3.add(jLabel_RainfallIntensity,     new XYConstraints(26, 74, 220, 27));
    jPanel3.add(jTextField_RainfallIntensity,     new XYConstraints(354, 77, 119, 27));
    jPanel3.add(jTextField_PipeManning_n,      new XYConstraints(354, 159, 119, 27));
    jPanel3.add(jLabel_PipeManning_n,    new XYConstraints(22, 160, 333, 23));
    jPanel3.add(jLabel_ManholeDrained, new XYConstraints(21, 242, 292, 23));
    jPanel3.add(jTextField_DrainedManholeNo,     new XYConstraints(354, 240, 119, 27));
    jPanel2.add(jButton_AllSubareasDelineated_Yes,   new XYConstraints(404, 45, 102, 30));
    jPanel2.add(jLabel_DAreaCompleteOption, new XYConstraints(404, 9, 247, 27));
    jPanel2.add(jButton_AllSubareasDelineated_No,   new XYConstraints(536, 45, 95, 30));
 }


  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
    dispose();
    }
  }

    //File close action performed
  public void fileExit_actionPerformed(ActionEvent e) {
   hide();
  }

      //Toggle | State action performed
  void RationalPeak_actionPerformed(ActionEvent e) {
    HelpDialog dlg = new HelpDialog(this);
    dlg.setSize(600, 450);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frameSize = getSize();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Point loc = getLocation();
    dlg.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    dlg.setModal(true);
    dlg.show();
  }

  void jToggleButton_ExitRP_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

    void helpAbout_Box(ActionEvent e) {
    About_Box dlg = new About_Box(this);
    dlg.setSize(230, 180);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.show();
  }

  boolean packFrame = false;
  TitledBorder titledBorder1;


  JPanel jPanel3 = new JPanel();
  XYLayout xYLayout3 = new XYLayout();
  JLabel jLabel_Heading = new JLabel();
  JLabel jLabel_image = new JLabel();
  JLabel jLabel_RainfallIntensity = new JLabel();
  JLabel jLabel_PipeManning_n = new JLabel();
  JTextField jTextField_RainfallIntensity = new JTextField();
  JLabel jLabel_ManholeDrained = new JLabel();
  JTextField jTextField_PipeManning_n = new JTextField();
  JTextField jTextField_DrainedManholeNo = new JTextField();
  JLabel jLabel_DAreaCompleteOption = new JLabel();
  JButton jButton_AllSubareasDelineated_Yes = new JButton();

 public void setDrainageAreaGeneralInfoValues (){

 //String ReturnPeriod = jTextField_ReturnPeriod.getText();
 String RainfallIntensity = jTextField_RainfallIntensity.getText();
 //String OverlandFlowTime = jTextField_OverlandFlowTime.getText();
 String PipeManning_n = jTextField_PipeManning_n.getText();
 String DrainedManholeNo = jTextField_DrainedManholeNo.getText();

// m_IOInputHashTable.setValueOfParameter("ReturnPeriod", ReturnPeriod);
 m_IOInputHashTable.setValueOfParameter("RainfallIntensity_"+subDAreaCounter, RainfallIntensity);
 m_IOInputHashTable.setValueOfParameter("PipeManning_n", PipeManning_n);
// m_IOInputHashTable.setValueOfParameter("OverlandFlowTime_"+subDAreaCounter, OverlandFlowTime);

//set sub area id and the manhole number (id) to which the sub area is drained
 m_IOInputHashTable.setValueOfParameter("SubareaIDforDrainedToManholeNo_"+subDAreaCounter, DrainedManholeNo);

 }


void jButton_AllSubareasDelineated_No_actionPerformed(ActionEvent e) {

   setDrainageAreaGeneralInfoValues ();
   this.subDAreaCounter = subDAreaCounter+1;
    hide();
   }


  PRunoffCoefficient_Sewer m_PRunoffCoefficient_Sewer = new PRunoffCoefficient_Sewer();
  SewerDAreaMInputFile_Writer m_SewerDAreaMInputFile_Writer = new SewerDAreaMInputFile_Writer();

  void jButton_AllSubareasDelineated_Yes_actionPerformed(ActionEvent e) {

  setDrainageAreaGeneralInfoValues ();
  m_SewerDAreaMInputFile_Writer.setValueOfParameter("totalNoOfDrainageAreas", String.valueOf(subDAreaCounter));

  try{
     m_PRunoffCoefficient_Sewer.runProcess();
     }catch(Exception r){
     System.out.println(r);
     }
  try{

     // write input parameters and values to SewerDAreaInputFile
     m_SewerDAreaMInputFile_Writer.writeHashTable();
     this.subDAreaCounter = 0;

   }catch(Exception r){
   System.out.println(r);
   }
   hide();

//try{
//   m_PRunoffCoefficient_Sewer.runProcess();
//   }catch(Exception r){
//   System.out.println(r);
//   }


  }



}
