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
//import rationalpeak.Model.ModelCreation.MInputFile_Writer;
import rationalpeak.Model.GUI.About_Box;


public class DrainageAreaInfoFrame extends JFrame{

SewerDAreaMInputFile_Writer m_IOInputHashTable = new SewerDAreaMInputFile_Writer();
//MainMenu_Frame m_MainMenu_Frame = new MainMenu_Frame();
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



  String urbanDrainageAreaType=" ";
  String urbanHydroCondition = " ";
//  String landCover=" ";

  //create panel for checkboxes
  JPanel jPanel1 = new JPanel();

//  JLabel image_Label1 = new JLabel();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jPanel2 = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
//  JTextPane jTextPane2 = new JTextPane();
  JButton jButton_Next = new JButton();

  JScrollPane notesScrollPane = new JScrollPane();
  JdbTextArea notesTextArea = new JdbTextArea();


   //Construct the dialog 1st option
  public DrainageAreaInfoFrame() {
//   Object subCatOpn =m_IOInputHashTable.getValueOfParameter("Sub_Catchments_Opn");
//   stSubCatOpn = String.valueOf(subCatOpn);
//   ctmtCounter = m_MainMenu_Frame.counting_Frame2();
//   if(stSubCatOpn.equalsIgnoreCase("YES")){
//   Object NoOfSubCts =m_IOInputHashTable.getValueOfParameter("No_Of_Sub_Catchments");
//   intNoOfSubcts = Integer.parseInt(String.valueOf(NoOfSubCts));
//   }


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
    this.setTitle("RationalPeak Model");
//    image1 = new ImageIcon(Welcome_Frame.class.getResource("/Images/MVC_001F.gif"));
    //initialize menu items and create events
    menuFile.setText("File");
    menuToMainWindow.setText("Abort");
    menuToMainWindow.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
//        m_MainMenu_Frame.catchmentCounter1 = 0;
//        m_MainMenu_Frame.catchmentCounter2 = 0;
//        MainMenu_FrameOpener();
//        hide();
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
    //jTextPane2.setText("status pane");
    notesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    notesTextArea.setBackground(SystemColor.activeCaptionBorder);
    notesTextArea.setFont(new Font("Dialog", 0, 12));
    notesTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
    notesTextArea.setEditable(false);
    notesTextArea.setText("Enter the Mouse to any field for further information");

    jButton_Next.setFont(new java.awt.Font("Dialog", 1, 14));
    jButton_Next.setText("NEXT");
    jButton_Next.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton_Next_actionPerformed(e);
      }
    });
//    jButton_Back.setFont(new java.awt.Font("Dialog", 1, 14));
//    jButton_Back.setText("BACK");
//    jButton_Back.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jButton_Back_actionPerformed(e);
//      }
//    });
    jPanel3.setLayout(xYLayout3);
    jLabel_Heading.setFont(new java.awt.Font("Dialog", 3, 16));
    jLabel_Heading.setForeground(Color.blue);
    jLabel_Heading.setOpaque(true);
    jLabel_Heading.setRequestFocusEnabled(false);
    jLabel_Heading.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel_Heading.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabel_Heading.setText("Drainage Area Related Information");
    jLabel_image.setIcon(image1);
    jLabel_image.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        jLabel_image_mouseEntered(e);
      }
    });
    jLabel_Business.setBackground(SystemColor.scrollbar);
    jLabel_Business.setForeground(Color.pink);
    jLabel_Business.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel_Business.setText("Business");
    jLabel_Residential.setText("Residential");
    jLabel_Residential.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel_Residential.setForeground(Color.pink);
    jLabel_Residential.setBackground(SystemColor.scrollbar);


//    jComboBox_Business.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jComboBox_Business_actionPerformed(e);
//      }
//    });
//    jComboBox_Residential.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jComboBox_Residential_actionPerformed(e);
//      }
//    });
//    jComboBox_Other.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jComboBox_Other_actionPerformed(e);
//      }
//    });
    jComboBox_Business.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        jComboBox_Business_mouseEntered(e);
      }
    });
    jComboBox_Residential.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        jComboBox_Residential_mouseEntered(e);
      }
    });

    jComboBox_Other.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        jComboBox_Other_mouseEntered(e);
      }
    });
    jLabel_DrainageType.setBackground(SystemColor.scrollbar);
    jLabel_DrainageType.setFont(new java.awt.Font("Dialog", 1, 16));
    jLabel_DrainageType.setText("Type of Drainage Area");
    jComboBox_Other.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        jComboBox_Other_mouseEntered(e);
      }
    });
//    jComboBox_Other.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jComboBox_Other_actionPerformed(e);
//      }
//    });
    jLabel_Others.setBackground(SystemColor.scrollbar);
    jLabel_Others.setForeground(Color.pink);
    jLabel_Others.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel_Others.setText("Other Landuse Types");
    jComboBox_Industrial.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        //jComboBox_Industrial_mouseEntered(e);
      }
    });
//    jComboBox_Industrial.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jComboBox_Industrial_actionPerformed(e);
//      }
//    });
    jLabel_Industrial.setText("Industrial");
    jLabel_Industrial.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel_Industrial.setForeground(Color.pink);
    jLabel_Industrial.setBackground(SystemColor.scrollbar);
    jLabel_Category.setFont(new java.awt.Font("Dialog", 1, 16));
    jLabel_Category.setText("Category");
    jLabel_PercentArea.setFont(new java.awt.Font("Dialog", 1, 16));
    jLabel_PercentArea.setText("Area (%)");
    menuFile.add(menuToMainWindow);
    menuFile.add(menuFileExit);
    menuBar1.add(menuFile);
    menuBar1.add(menuHelp);
    menuHelp.add(RationalPeak);
    menuHelp.add(About);
    contentPane.add(jPanel2, BorderLayout.SOUTH);

    jPanel2.add(notesTextArea,   new XYConstraints(12, 19, 357, 86));
    jPanel2.add(jButton_Next,      new XYConstraints(484, 53, 95, 32));
    contentPane.add(jPanel1, BorderLayout.NORTH);
    jPanel1.add(jLabel_Heading,     new XYConstraints(29, 8, 515, 36));
    contentPane.add(jPanel3, BorderLayout.CENTER);
    jPanel3.add(jComboBox_Business,  new XYConstraints(262, 54, 149, 22));
    jPanel3.add(jComboBox_Residential,   new XYConstraints(262, 121, 149, 23));
    jPanel3.add(jComboBox_Industrial,   new XYConstraints(262, 190, 149, 23));
    jPanel3.add(jLabel_image,    new XYConstraints(3, 326, 675, 46));
    jPanel3.add(jComboBox_Other,   new XYConstraints(262, 258, 149, 23));
    jPanel3.add(jLabel_DrainageType, new XYConstraints(13, 12, 177, 25));
    jPanel3.add(jLabel_Category,  new XYConstraints(261, 16, 161, 27));
    jPanel3.add(jLabel_PercentArea, new XYConstraints(518, 16, 137, 28));
    jPanel3.add(jTextField_BusinessArea,    new XYConstraints(517, 54, 107, 24));
    jPanel3.add(jTextField_ResidentialArea,   new XYConstraints(517, 122, 109, 23));
    jPanel3.add(jTextField_IndustrialArea,   new XYConstraints(516, 190, 111, 24));
    jPanel3.add(jLabel_Residential,  new XYConstraints(32, 123, 104, 27));
    jPanel3.add(jLabel_Industrial,  new XYConstraints(32, 192, 76, 23));
    jPanel3.add(jLabel_Business, new XYConstraints(32, 54, 90, 26));
    jPanel3.add(jLabel_Others,   new XYConstraints(34, 258, 171, 23));
    jPanel3.add(jTextField_OtherLandUseArea,  new XYConstraints(516, 258, 112, 24));
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

//  void jButton_Back_actionPerformed(ActionEvent e) {
//   int currentValueOfctmtCounter = m_MainMenu_Frame.catchmentCounter1;
//   m_MainMenu_Frame.catchmentCounter1 = currentValueOfctmtCounter - 1;
//   m_MainMenu_Frame.catchmentCounter2 = currentValueOfctmtCounter - 1;
//   RunoffCoefficient_Frame1Opener();
//   dispose();
//  }

  boolean packFrame = false;
  TitledBorder titledBorder1;

  Object[] dataObject_Business = {"Downtown areas", "Neighborhood areas"};
  Object[] dataObject_Residential = {"Single-family areas", "Apartment dwelling areas", "Suburban"};
  Object[] dataObject_Industrial = {"Light areas", "Heavy areas"};
  Object[] dataObject_Others = {"Parks", "Playgrounds","Streets"};

  JPanel jPanel3 = new JPanel();
  XYLayout xYLayout3 = new XYLayout();
  JLabel jLabel_Heading = new JLabel();
  JLabel jLabel_image = new JLabel();
  JLabel jLabel_Business = new JLabel();
  JLabel jLabel_Residential = new JLabel();
  JComboBox jComboBox_Business = new JComboBox(dataObject_Business);
  JComboBox jComboBox_Residential = new JComboBox(dataObject_Residential);
  JLabel jLabel_DrainageType = new JLabel();
  JComboBox jComboBox_Other = new JComboBox(dataObject_Others);
  JLabel jLabel_Others = new JLabel();
  JComboBox jComboBox_Industrial = new JComboBox(dataObject_Industrial);
  JLabel jLabel_Industrial = new JLabel();
  JLabel jLabel_Category = new JLabel();
  JLabel jLabel_PercentArea = new JLabel();
  JTextField jTextField_BusinessArea = new JTextField();
  JTextField jTextField_ResidentialArea = new JTextField();
  JTextField jTextField_IndustrialArea = new JTextField();
  JTextField jTextField_OtherLandUseArea = new JTextField();

    //Construct the nex window's application
  public void GeneralDrainageAreaInfoFrameOpener() {
    GeneralDAreaInfoFrame frame = new GeneralDAreaInfoFrame();
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      URL iconURL = getClass().getResource("/Images/RPIcon.gif");
      Image frameIcon = toolkit.getImage(iconURL);
      frame.setIconImage(frameIcon);

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

 double totalPercentage = 0.0;
 public boolean checkPercentageArea(){
       String BusinessArea = jTextField_BusinessArea.getText();
       String ResidentialArea = jTextField_ResidentialArea.getText();
       String IndustrialArea = jTextField_IndustrialArea.getText();
       String OtherLandUseArea = jTextField_OtherLandUseArea.getText();
       //double doubleBusinessArea = Double.parseDouble(BusinessArea);
       if (!BusinessArea.equals("")){
       double doubleBusinessArea = Double.parseDouble(BusinessArea);
       totalPercentage = totalPercentage+doubleBusinessArea;
       }
       if (!ResidentialArea.equals("")){
       double doubleResidentialArea = Double.parseDouble(ResidentialArea);
       totalPercentage = totalPercentage+doubleResidentialArea;
       }
       if (!IndustrialArea.equals("")){
       double doubleIndustrialArea = Double.parseDouble(IndustrialArea);
       totalPercentage = totalPercentage+doubleIndustrialArea;
       }
       if (!OtherLandUseArea.equals("")){
       double doubleOtherLandUseArea = Double.parseDouble(OtherLandUseArea);
       totalPercentage = totalPercentage+doubleOtherLandUseArea;
       }
System.out.println("totalPercentage in DrainageAreaInfoFrame is  "+totalPercentage);
       //message dialog
        if (totalPercentage==100.0) {
             totalPercentage=0.0;
             return true;
        }else{
         JOptionPane.showMessageDialog(this,"Total area is greater or less than 100 percent"
                                     ,"RationalPeak: Data Input Error",
                                     JOptionPane.ERROR_MESSAGE/*,image2*/);
             totalPercentage=0.0;
             return false;
             }//end of message dialog

 }//end of checkPercentageArea() message

 public void setLandUseValues (){

 String BusinessArea = jTextField_BusinessArea.getText();
 String ResidentialArea = jTextField_ResidentialArea.getText();
 String IndustrialArea = jTextField_IndustrialArea.getText();
 String OtherLandUseArea = jTextField_OtherLandUseArea.getText();
 //double doubleBusinessArea = Double.parseDouble(BusinessArea);
 if (!BusinessArea.equals("")){
   Object BusinessAreaCategory = jComboBox_Business.getSelectedItem();
   if(BusinessAreaCategory=="Downtown areas"){BusinessAreaCategory="LanduseDowntown_areas";}
   if(BusinessAreaCategory=="Neighborhood areas"){BusinessAreaCategory="LanduseNeighborhood_areas";}
   m_IOInputHashTable.setValueOfParameter(BusinessAreaCategory+"_"+subDAreaCounter, BusinessArea);
   }
 if (!ResidentialArea.equals("")){
   Object ResidentialAreaCategory = jComboBox_Residential.getSelectedItem();
   if(ResidentialAreaCategory=="Single-family areas"){ResidentialAreaCategory="LanduseSingle_family_areas";}
   if(ResidentialAreaCategory=="Apartment dwelling areas"){ResidentialAreaCategory="LanduseApartment_dwelling_areas";}
   if(ResidentialAreaCategory=="Suburban"){ResidentialAreaCategory="LanduseSuburban";}
   m_IOInputHashTable.setValueOfParameter(ResidentialAreaCategory+"_"+subDAreaCounter, ResidentialArea);
   }
 if (!IndustrialArea.equals("")){
   Object IndustrialAreaCategory = jComboBox_Industrial.getSelectedItem();
   if(IndustrialAreaCategory=="Light areas"){IndustrialAreaCategory="LanduseLight_areas";}
   if(IndustrialAreaCategory=="Heavy areas"){IndustrialAreaCategory="LanduseHeavy_areas";}
   m_IOInputHashTable.setValueOfParameter(IndustrialAreaCategory+"_"+subDAreaCounter, IndustrialArea);
   }
 if (!OtherLandUseArea.equals("")){
   Object OtherLandUseAreaCategory = jComboBox_Other.getSelectedItem();
   if(OtherLandUseAreaCategory=="Parks"){OtherLandUseAreaCategory="LanduseParks";}
   if(OtherLandUseAreaCategory=="Playgrounds"){OtherLandUseAreaCategory="LandusePlaygrounds";}
   if(OtherLandUseAreaCategory=="Streets"){OtherLandUseAreaCategory="LanduseStreets";}
   m_IOInputHashTable.setValueOfParameter(OtherLandUseAreaCategory+"_"+subDAreaCounter, OtherLandUseArea);
   }

 }


void jButton_Next_actionPerformed(ActionEvent e) {
   boolean percentAreaisCorrect = checkPercentageArea();
       if(percentAreaisCorrect){
           setLandUseValues ();
           this.subDAreaCounter = subDAreaCounter+1;

           GeneralDrainageAreaInfoFrameOpener();

           hide();
       }
   }


//  void jComboBox_Business_actionPerformed(ActionEvent e) {
//     if (jComboBox_Business.getSelectedItem() == "Downtown areas") {
//      urbanDrainageAreaType = "Downtown_areas";
//     }
//    else if (jComboBox_Business.getSelectedItem() == "Neighborhood areas") {
//      urbanDrainageAreaType = "Neighborhood_areas";
//     }
//
//  }
//
//
//
//  void jComboBox_Residential_actionPerformed(ActionEvent e) {
//
//     if (jComboBox_Residential.getSelectedItem() == "Single-family areas") {
//      urbanDrainageAreaType = "Single_family_areas";
//     }
//    else if (jComboBox_Residential.getSelectedItem() == "Apartment dwelling areas") {
//      urbanDrainageAreaType = "Apartment_dwelling_areas";
//      }
//    else if (jComboBox_Residential.getSelectedItem() == "Suburban") {
//      urbanDrainageAreaType = "Suburban";
//     }
//
//  }
//
//  void jComboBox_Industrial_actionPerformed(ActionEvent e) {
//
//     if (jComboBox_Industrial.getSelectedItem() == "Light areas") {
//      urbanDrainageAreaType = "Light_areas";
//     }
//    else if (jComboBox_Industrial.getSelectedItem() == "Heavy areas") {
//      urbanDrainageAreaType = "Heavy_areas";
//      }
//  }
//
//    void jComboBox_Other_actionPerformed(ActionEvent e) {
//
//     if (jComboBox_Other.getSelectedItem() == "Parks") {
//      urbanDrainageAreaType = "Parks";
//     }
//    else if (jComboBox_Other.getSelectedItem() == "Playgrounds") {
//      urbanDrainageAreaType = "Playgrounds";
//      }
//    else if (jComboBox_Other.getSelectedItem() == "Streets") {
//      urbanDrainageAreaType = "Streets";
//      }
//
//  }



  void jComboBox_Business_mouseEntered(MouseEvent e) {
    notesTextArea.setText("Urban drainage area of business type. Click the pop up list for catagories ");

  }

  void jComboBox_Residential_mouseEntered(MouseEvent e) {
    notesTextArea.setText("Urban drainage area of residential type. Click the pop up list for catagories");

  }

//  void jComboBox_HydroCondition_mouseEntered(MouseEvent e) {
//    notesTextArea.setText("Hydrological condition of the selected urban drainage area type i.e whether it has poor, fair or good hydrological condition. Click the pop up list for catagories");
//
//  }
//
//  void jTextField_Area_mouseEntered(MouseEvent e) {
//    notesTextArea.setText("Area of the catchment or current sub-catchment in hectar");
//
//  }


  void jComboBox_Other_mouseEntered(MouseEvent e) {
    notesTextArea.setText("Urban drainage area of other types. Click the pop up list for catagories");

  }

  void jLabel_image_mouseEntered(MouseEvent e) {
    notesTextArea.setText("Enter the Mouse to any field for further information");

  }



}
