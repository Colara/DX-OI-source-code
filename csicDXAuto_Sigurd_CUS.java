//+++<CP> MTK HSMCode copy function add Device: AXD10699 by Cola. 20180123+++
//+++<FT> Summary add Sockets No. for Socket Contact Counter. by Cola. 20171229+++
//+++<FT> Revise Summary LOT START TIME to STDF naming timeStr by Cola. 20171226+++ 
//+++<CP> For D10 unison: barcode_stationStr will randomly be clear issue. 20171205+++
//+++<CP> Load Probe Card RCP add Device:BM10797 20171204+++
//+++<FT> For FT Recovery Summary by Cola. 20171121+++
//+++<FT> L227 save and revise CORR STDF file by Cola. 20171101+++
//+++<CP> MTK STDF tester_type change to Fusion_EX by Cola. 20171027+++
//+++<CP> MTK HSMCode copy function add Device:AZA10699 by Cola. 20171025+++
//+++<FT> Add Smart Bin Tray functionality by Cola. 20171012+++
//+++<FT> Adjust STDF TimeZone setting by Cola. 20171011+++
//+++<FT> Reset Lot ID after set_start_of_lot command sending by Cola. 20171002+++
//+++<FT> FT set delay time for Lot end by Cola. 20170925+++
//+++<CP> MTK HSMCode copy function add Device:AM10690 by Cola. 20170920+++
//+++<CP> Load Probe Card RCP add Device:BM10213B by Cola. 20170913+++
//+++<CP> Add WaferID_detector flag by Cola. 20170912+++
//+++<FT> Add RCP file Item for SBC control by Cola. 20170912+++
//+++<CP> Add Manual_Test command when start test by Cola. 20170907+++
//+++<FT> L227 can't be Production after HW station used by Cola. 20170821+++
//+++<CP> Add MTK HSMCode copy function by Cola. 20170808+++
//+++<FT> Add set_start_of_lot command when Ready to Run by Cola. 20170719+++
//+++<FT> L227 add info.spil file copy & DEVICE_CODE check by Cola. 20170710+++
//+++FT version imported by Cola. 20170619+++
//+++Add flag for TryRun usage by Cola. 20170605+++
//+++Add DataCollection STDF update function to btexit button by Cola. 20170524+++ 
//+++Clean /tmp/pulse folder add to /bin/autoload_Sigurd_CUS.csh 20170505+++
//+++Add ENG mode button for open launcher. 20170323+++
//+++Add userFlag content for link DataCollection map_start & map_end by Cola. 20170220+++
//+++Change RealTimelog path to local by Cola. 20170109+++
//+++Add Lock ENG path & naming for load Program. 20161229+++
//+++For launcher & OICu all open problem. 20161227+++
//+++Add SBC check for SPIL. 20161209+++
//+++Revise Probe Card RCP naming rule to index of key word Device and add STR rule. 20160914+++
//+++Add New Device:AZA10333B to Probe Card RCP naming rule. 20160830+++
//+++change L121 STDF PartType only show 7 digits. 20160817+++
//Clear Test Program Datalog to Window Print. 20160629

// Date: 2008-03-01
// 
// Creator: Ching Hsu @ CSIC
// 
// It is the Java GUI application to support Automatic production mode
// - Integrate Diamond OIC tool, BarCodeReader and "dmd_cmd" functionality
//
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.text.*;
import java.lang.*;
import java.lang.String.*;
import java.lang.Object.*;
import java.lang.Process.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.net.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.util.Date;
import java.util.Timer; 
import java.util.TimerTask;

public class csicDXAuto_Sigurd_CUS extends JFrame 
implements ActionListener, MouseMotionListener, KeyListener, WindowListener,
Runnable, AdjustmentListener, ItemListener, DocumentListener {

	private static String oiversion = "1.1.40";
	private static String oidate = "2018-01-23";
	private static String TryRunTester[] = {"DX-24"};	//20170605. Add Tester Name for TryRun
	private static boolean SigurdFlag = true;
	
	//
	// User parameters maintain here
	//
	private static String csicAutoPath = "";
	// User maintain End
	protected static boolean EQU_CP_check_ProbeCard_flag = true;//EQU CP flag
	protected static boolean EQU_FT_check_ProbeCard_flag = true;//EQU FT flag
	protected static boolean EQU_CP_Barcode_AutoKeyin_flag = false;//EQU CP flag
	protected static boolean EQU_FT_Barcode_AutoKeyin_flag = false;//EQU FT flag
	// These parameters are set in the file "autoload"
	// They are extracted by the cmd "getSystem_Sigurd"
	private static String SiteStr = "";            // SiteName
	private static String SITENAME = "";           // the subcon name
	private static String autoResultPath = "";     // auto production result save path
	private static String autoLocalResultPath = "/tmp/Sum/";     // 20100107 auto production result save path on local side
	private static String messagePath = "";        // save message path
	private static String testerBarcodePath = "";  // Tester Barcode XML file path
	private static String recipefilePath = "";     // Recipe file path
	private static String DMDSWVerStr = "";        // System Diamond SW version
	//
	private static String barcodeFile = "Tester_barcode.XML"; //org = Tester_barcode.xml //20080522
	private static int autonewlotCnt = 0; // used on runConfirmation(), genDmdCmdFile()

	private static boolean RTFlag = false; //20080724  //20081021
	private static String RTStr = "A1";
	//
	//private static File inf;
	//private static File stpf;
	private static File outf;
	private static File outfReal;
	private static File outcheckSummary;    
	private static File outcheckSocketData;
	private static File barcode_OI_file;       
	private static BufferedReader br;
	private static BufferedReader br_tmp;//20090225
	private static BufferedReader br_STDinfo; //20090504
	private static BufferedReader data_info; //20091229
	private static int dataFlag=0; //20091229
	private static int SitesNum; //20090504
	private static FileWriter fw_tmp; //20090225
	private static String summaryfileStr;//20090225
	private static String summaryfileStr_MES;//20170619
//	private static String summaryfileStr_DataCollection;//20090225
	private static String summaryfileName_tmp;//20090311

	//// Tester BarCode File parameters variable 
	private static String barcode_sitenameStr = "";
	private static String barcode_testeridStr = "";
	private static String barcode_recipepathStr = "";
	private static String barcode_summarypathStr = "";
	private static String barcode_customerStr = "";
	private static String barcode_programStr = "";
	private static String barcode_stationStr = ""; // FT1_FTn
	private static String barcode_proberidStr = "";
	private static String barcode_handleridStr = "";  //Revise by Cola. 20160629
	private static String barcode_lotidStr = "";
	private static String barcode_lotidStr_sub = "";//jj
	private static String barcode_opidStr = "";
	private static String barcode_temperatureStr = "";
	private static String barcode_SoaktimeStr = ""; //20110614   
	private static String barcode_flowStr = "";
	private static String barcode_devicetypeStr = "";
	private static String barcode_devicetypeStr_sub = "";//jj
	private static String barcode_devicetypeStr__ = ""; // change - or / to _ (underline)
	private static String barcode_sgidStr = "";
	private static String barcode_LBC1Str = "";//20120412
	private static String barcode_LBC2Str = "";//20120412
	private static String barcode_lbidStr = "";
	private static String barcode_stage = "";//20110824
	private static String barcode_datecode = "";//20130314
	private static String barcode_duts = "";//20130416    
	private static int barcode_duts_int = 0; //20130416
	private static boolean stageFlag  = false;//201100824 
	private static String barcode_RTbin_Str = ""; //Add by Cola. 20150817-----Start
	private static String barcode_SiteDiffYield_Str = "";
	private static String barcode_AlarmYield_Str = "";
	private static String barcode_AlarmBinYield_Str = "";
	private static String barcode_BinDefine_Str = ""; //20151209 //Add by Cola. 20150817-----End
	private static String barcode_locationCode;//20140709          
	private static String barcode_ProbeDeviceName;//20140709  
	private static String F191_SALESID = "";
	private static String barcode_TestFlowNumber_Str = ""; //Add by Cola. 20160504
	private static String barcode_ReferenceDoc_Str = ""; //Add by Cola. 20160624
	private static String barcode_FormerLOTID_Str = ""; //Add by Cola. 20160630
	private static String barcode_DESIGN_TYPE_Str = ""; //Add by Cola. 20160630
	private static String barcode_customerTO_Str = ""; //Add by Cola. 20160630
	private static String barcode_orderNO_Str = ""; //Add by Cola. 20161103
	private static String barcode_TangoSBCset_Str = ""; //Add by Cola. 20161209
	private static int TangoSBC_Rule_index = 0; //Add by Cola. 20161209
	private static String[] barcode_TangoSBCrule_Str; //Add by Cola. 20161209
	private static String barcode_OCR_ID_Str = "";	//Add by Cola. 20170220
	private static String barcode_SPIL_LOTNO_Str = "";	//20170710
	private static String barcode_SPIL_C_Item_Str = "";	//20170710
	private static String barcode_Wafer_Number_Str = "";	//20170808
	private static String[] barcode_Wafer_Number;	//20170808

	//gpib info 20110614
	private static String gpibFTType = "";//
	private static String gpibFTTemp = "";//
	private static String gpibFTAllTemp[] = new String[20]; //Add for all temperature of FT Equipment by Cola. 2015/05/28 
	private static String gpibFTSoakTime = "";//
	private static String gpibFTSiteMap = "";//    
	private static String gpibFTAlarmSetup = "";
	//gpib info Prober 201204
	private static String gpibCPWaferNo = "";//    
	private static String gpibCPTemp = "";//
	private static String gpibCPLotNum = "";//
	private static String gpibCPProberDevice = "";//20131104 prober device check    
	private static String gpibCPProberLocation = "";//20140620 prober location check
	// Recipe and Load File parameters variable
	private static String recipeFile         = "";
	private static String correctloadfileStr = "";
	private static String JobPathStr         = ""; // Job_path
	private static String userSummaryPath    = ""; // Lot summary save path
	private static String RobotStr           = ""; // Job_RobotType
	private static String RobotStr2          = ""; // Job_RobotType2//20081021
	private static String RobotStr3          = ""; // Job_RobotType3//20081021
	//20100722 for MTK
	private static String loadfilePath       = ""; // loadfile path
	private static String loadfileStr        = ""; // loadfile name

	private static String testTypeStr        = ""; // Test_Type name (Final or Wafer)
	private static String userSummaryfinalPath = "";//20080908
	private static String userSummaryfinalPath_bak = "";//20100107
	private static String userPathforCP = "";//20100107
	private static String userSTDFfinalPath = "";//20090427
	private static String userTXTfinalPath = "";//20170619
	private static String userSTDFserverPath = "";//20170619
	private static String userTXTserverPath = "";//20170619
	//put the load file parameters into recipe file
	private static String JobStr             = ""; // Job_name
	private static String JobCmpStr          = ""; // Job_name 20110311
	private static String TPSWVerStr         = ""; // DMD SW Version for the Test Program
	private static String FamilyIDStr        = ""; // Job_Family_ID
	private static String BondingStr         = ""; // Bonding
	private static String TPSWVer_prefix     = ""; // used as the prefix of the job test program name
	//
	private static String JobSoStr = "";
	private static String beforeJobStr = "";
	private static boolean lotSummaryFlag = false;
	private static int firstFlag = 1;
	private static int btCnt = 0;
	private static boolean oicFlag = false;
	//
	private static String SGStr   = ""; // SG Number
	private static String LBStr   = ""; // Loadboard / ProbeCard Number
	private static String TPStr   = ""; // TestProgram name
	private static String LotStr  = ""; // LotID
	private static String LocalPath= ""; //20091103
	private static String autoSendPath = "";//20100107
	private static String unCompressPath = "";//20100107
	private static String show_data_path = "/usr/local/home/autoload/Barcode/";//20100107
	private static boolean stdExistFlag;//20100107
	private static boolean sumExistFlag;//20100107
	private static boolean sumSizeFlag;//20100107
	//
	private static String STDfileStrTime = ""; 
	private static String STDfileEndTime = ""; 
	private static String STDfileStr = ""; 
	private static String STDfilefinalStr = ""; 
	private static String STDfiletmpStr = "";
	private static String TXTfileStr = ""; //20170619
	private static boolean STDFlag = false;      //A1,RT need
	private static String STDFlogStr = "";       // 20090506
	private static String NotchStr = "";         //jj
	private static String TesterSiteMapStr = ""; //20110614
	private static String JobStrRcp = "";        //20110311
	private static String probeDeviceRcp = "";        //20110311 
	private static boolean STDFlogFlag = true;  //decided by rcp file   // 20090506
	protected static JFrame frame = new JFrame("Welcome to Diamond Automation System" + " (Version = " + oiversion + ")");
	protected static JFrame FT_GPIB_warnning_frame = new JFrame("FT GPIB warnning");

	protected static JTextArea FT_GPIB_warnning_JText = new JTextArea();  //20140120   
	protected static JPanel FT_GPIB_warnning_p1= new JPanel();   //20140120  
	protected static JLabel FT_GPIB_warnning_lab1 = new JLabel();//20140120  
	protected static Container c = new Container();
	protected static Container subc = new Container();
	protected static Container FT_GPIB_warnning_container = new Container();

	protected static JPanel FT_GPIB_warnning_panel = new JPanel(); //20140120
	protected static JPanel p1 = new JPanel(); // Panel 1
	protected static JPanel p2 = new JPanel(); // Panel 2
	protected static JPanel p3 = new JPanel(); // Panel 3

	protected static JPanel p1sub1 = new JPanel(); // Panel 1 - sub1 panel
	protected static JPanel p1sub2 = new JPanel(); // Panel 1 - sub2 panel

	protected static JPanel p3sub = new JPanel();  // Panel 3 - sub panel

	protected static JPanel p1sub1sub1 = new JPanel(); // Panel 1 - sub1 panel - sub1
	protected static JPanel p1sub1sub2 = new JPanel(); // Panel 1 - sub1 panel - sub2
	protected static JPanel p1sub1sub3 = new JPanel(); // Panel 1 - sub1 panel - sub3
	protected static JPanel p1sub1sub4 = new JPanel(); // Panel 1 - sub1 panel - sub4
	protected static JPanel p1sub1sub2sub = new JPanel(); //20080724//20080902 20081021
	protected static JPanel p1sub1sub2subsub = new JPanel();//20080724//20080902 20081021
	protected static JPanel p_data1 = new JPanel();//20091229
	protected static JPanel p_data2 = new JPanel();//20091229
	protected static JPanel p_data3 = new JPanel();//20091229

	protected static JButton bt1   = new JButton(); // Get Tester BarCode File button
	protected static JButton bt2_1 = new JButton(); // autoNewLot  button
	protected static JButton bt2_2 = new JButton(); // autoSummary button
	//protected static JButton bt2_3 = new JButton(); // autoEndLot  button //20080826
	protected static JButton bt2_4 = new JButton(); // autoStop    button
	protected static boolean bt2_4Flag = false;//20110831
	protected static JButton bt_Clean = new JButton(); // Clean    button    
	protected static JButton bt3   = new JButton(); // SaveMessage Button
	protected static JButton btOIC = new JButton(); // launch OIC button
	protected static JButton btExit= new JButton(); // Exit button
	protected static JButton btVer = new JButton(); // OI Version button
	protected static JButton d_show  = new JButton(); //20091229
	protected static JButton d_print = new JButton(); //20091229
	protected static JButton btd   = new JButton(); // SaveMessage Button

	protected static ButtonGroup grpbt = new ButtonGroup(); // Group button for RadioButton
	protected static String[] rbtname = {"FT1", "FT2", "FT3", "CP1", "CP2", "CP3", "PreFT"}; // JRadioButton button name array  20080826
	protected static JRadioButton[] rbt = new JRadioButton[rbtname.length]; // JRadioButton button array
	protected static Object myRBT = new Object(); // 

	//20080902
	protected static ButtonGroup rtgrpbt = new ButtonGroup();
	protected static String[] rtrbtname = {"NA", "A1", "RT","EQC","CORR","HW"};//20100428
	protected static JRadioButton[] rtrbt = new JRadioButton[rtrbtname.length];

	//20081106
	protected static String[] rtBinname = {"1","2","3","4","5","6","7","8","9","10"}; //2016/08/24	Dustin add 9&10
	protected static String[] QRTBinname = {"B1","B2","B3","B4","B5","B6","B7","B8","B9","B10"};//20100618 //2016/08/24	Dustin add 9&10
	protected static JCheckBox[] rtBinbox = new JCheckBox[rtBinname.length];
	protected static JCheckBox[] QRTBinbox = new JCheckBox[QRTBinname.length];//20100618
	protected static JCheckBox[] ANFrtBinbox = new JCheckBox[rtBinname.length];//20161026
	protected static JPanel p1sub1sub4sub1 = new JPanel();
	protected static JPanel p1sub1sub4sub2 = new JPanel();
	protected static JPanel p1sub1sub4sub2sub = new JPanel();
	protected static JPanel p1sub1sub4sub2sub1 = new JPanel();

	protected static JTextField tx4_1 = new JTextField();
	protected static int sumRepNum = 1;
	protected static String sumRepNumStr = "";
	//20081107
	protected static int rtBinnum = 0;
	protected static int QrtBinnum = 0;
	protected static String rtBinStr = "";
	protected static String QrtBinStr = "";
	protected static String rtBinSumStr = "";
	protected static String QrtBinSumStr = "";
//	protected static int ANFrtBinnum = 0; //20161026
//	protected static int ANFQrtBinnum = 0; //20161026
	protected static String ANFrtBinSumStr = ""; //20161026
	protected static String ANFQrtBinSumStr = ""; //20161026
	
	//20081110
	protected static boolean sumRepFlag = false;
	protected static int sumTmpNum  = 0;
	protected static boolean bt2_2Flag = false;

	//20100709
	protected static String systemCleanCode = "abcd";
	protected static String userCleanCode = "abcd";

	//20100722 for MTK
	protected static boolean EFuseErrorFlag = false;
	protected static String EfuseStr = "";
	protected static boolean EfuseFlag = false;
	protected static String EngStr = "";
	protected static boolean EngFlag = false;

	//2012 2
	private static boolean startTestOk = false;
	private static String SGInputStr = "";    
	private static String LBC1InputStr = ""; //20120412   
	private static String LBC2InputStr = ""; //20120412   
	private static boolean SGInputFlag = false;    
	private static boolean SGInputPassFlag = false;    
	private static long lasttime=0;    
	private static long duration=0;    
	private static String RTCheckStr = "";    
	private static boolean ChangeNewLotFlag = false; 

	protected static JTextArea textArea1 = new JTextArea();
	protected static JTextArea textArea1_2 = new JTextArea();
	protected static JTextArea textArea2 = new JTextArea();
	protected static JTextArea textAread = new JTextArea();//20091229
	protected static JScrollPane sp_d;//20091229
	protected static JLabel lb0 = new JLabel();
	protected static JLabel lb1 = new JLabel();
	protected static JLabel lb2 = new JLabel();
	protected static JLabel lb3 = new JLabel();
	protected static JLabel lb4 = new JLabel();
	protected static JLabel lb5 = new JLabel();

	protected static JTextField tx0 = new JTextField();
	protected static JTextField tx0b= new JTextField();
	protected static JTextField kit_No_panel = new JTextField();
	protected static JTextField socket_No_panel = new JTextField();
	protected static JTextField socket_No_pane2 = new JTextField();    
	protected static JTextField socket_No_pane3 = new JTextField();
	protected static JTextField socket_No_pane4 = new JTextField();
	protected static JTextField socket_No_pane5 = new JTextField();       
	protected static JTextField socket_No_pane6 = new JTextField();    
	protected static JTextField socket_No_pane7 = new JTextField();
	protected static JTextField socket_No_pane8 = new JTextField();             
	protected static JTextField tx1 = new JTextField();
	protected static JTextField tx2 = new JTextField();
	protected static JTextField tx21= new JTextField();//20080724
	protected static JTextField tx3 = new JTextField();
	protected static JTextField tx4 = new JTextField();
	protected static JTextField tx5 = new JTextField();

	//public JScrollBar(int orientation,int value,int extent,int min,int max)
	//protected static JScrollBar sb1 = new JScrollBar(JScrollBar.VERTICAL, 0, 2, 0, 50);
	//protected static JScrollBar sb2 = new JScrollBar(JScrollBar.HORIZONTAL, 0, 2, 0, 50);

	protected static String infStr = "";  // input file name
	protected static String outfStr = ""; // output file name
	protected static String outfStrReal = ""; // output file name
	protected static String outfStrRealError = ""; // output file name
	protected static String outfStrRealStSp = ""; // output file name
	protected static String checkSummaryFile = ""; // for MES check CP map & FT summary  20130429 by ChiaHui 
	protected static String checkSocketData = ""; // for EQU socket Calculatation 20130514 by ChiaHui
	protected static int  outfRealFlag = 0; // 

	protected static SimpleDateFormat myformatter;    // Formats the date displayed
	protected static String mydatetime = "";
	protected static String autoNewLotDateTime   = "";
	protected static String autoEndLotDateTime   = "";
	protected static String autoEndLotDateTime2  = "";
	protected static String autoSummaryDateTime  = "";
	protected static String autoSummaryDateTime2 = "";
	protected static String autoStopDateTime     = "";
	protected static String nullStr     = "";

	protected static String resultStr = null;
	protected static String homeStr = null;
	protected static String whoamiStr = null;
	protected static String hostidStr = null;
	protected static String hostnameStr = null;
	protected static long hostid;

	protected static String lic_hostidStr = null;
	protected static String lic_hostnameStr = null;
	protected static String lic_keyStr = null;
	protected static long lic_hostid;
	protected static long lic_keyvalue1;
	protected static long lic_keyvalue2;
	protected static long lic_keyvalue3;

	//20081021 robot
	protected static boolean init_flag = false;//only init once
	protected static ButtonGroup robot_grpbt = new ButtonGroup(); // Group button for RadioButton 
	protected static String[] robot_btname; // JRadioButton button name array
	protected static JRadioButton[] robot_bt; // JRadioButton button array  

	protected static Container c1= new Container();
	protected static Container d1= new Container();//20091229
	protected static JFrame robotframe = new JFrame("Robot type options");
	protected static JFrame dataframe = new JFrame("show data");//20091229
	protected static Label robot_lab = new Label(" Select Robot type "); 
	protected static JPanel robot_p1= new JPanel();                         
	protected static JPanel robot_p2= new JPanel();          
	protected static JPanel robot_p3= new JPanel();          
	protected static int robot_type_num = 0;
	protected static String RobotStr_set = "";
	protected static boolean mutiRobot_flag = false;
	protected static Button robot_frame_bt = new Button("OK"); 
	protected static Button data_frame_bt = new Button("CLOSE");
	//20100428 RT frame
	protected static boolean RT_flag = false;// 
	protected static ButtonGroup RT_grpbt = new ButtonGroup();
	protected static String[] RT_btname = {"R1", "R2", "R3","R4", "R5", "R6", "R7", "R8", "R9", "R10" ,"R11", "R12", "R13","R14", "R15", "R16", "R17", "R18", "R19", "R20"};//2016/08/24 Dustin add R9-R20
	protected static JRadioButton[] RT_bt = new JRadioButton[RT_btname.length];
	protected static Container RT_c = new Container();
	protected static JFrame RT_frame = new JFrame("RT options");
	protected static Label RT_lab = new Label(" Select RT Number & Retest Bin Number ");//20100507
	protected static JPanel RT_p1= new JPanel();                         
	protected static JPanel RT_p2= new JPanel();          
	protected static JPanel RT_p3= new JPanel(); 
	protected static JPanel RT_p4= new JPanel(); 
	protected static Button RT_frame_bt = new Button("OK");
	protected static Button RT_frame_bt2 = new Button("Select ANF Bin");
	protected static String RTNumStr = "";
	protected static String RTNumStr2 = "";
	protected static int RT_num = 0; //for detect RT window weather be open
	protected static int rtBinCount = 0;
	protected static int QrtBinCount = 0;
	protected static String L010_RT_string = "";    	 
	//////////////////////////////////////// 
	//Add frame to select ANF RT Bin by Cola. 20161026
	protected static boolean ANFRT_flag = false;// 
//	protected static ButtonGroup ANFRT_grpbt = new ButtonGroup();
//	protected static String[] ANFRT_btname = {"R1", "R2", "R3","R4", "R5", "R6", "R7", "R8", "R9", "R10" ,"R11", "R12", "R13","R14", "R15", "R16", "R17", "R18", "R19", "R20"};//2016/08/24 Dustin add R9-R20
//	protected static JRadioButton[] ANFRT_bt = new JRadioButton[RT_btname.length];
//	protected static Container ANFRT_c = new Container();
	protected static JFrame ANFRT_frame = new JFrame("ANF RT options");
	protected static Label ANFRT_lab = new Label(" Select ANF Input RT Bin Number ");//20100507
	protected static JPanel ANFRT_p1= new JPanel();                         
	protected static JPanel ANFRT_p2= new JPanel();          
	protected static JPanel ANFRT_p3= new JPanel(); 
	protected static JPanel ANFRT_p4= new JPanel(); 
	protected static Button ANFRT_frame_bt = new Button("    OK    ");
//	protected static int ANFRT_num = 0; //for detect RT window weather be open
	protected static int ANFrtBinCount = 0;
	protected static int ANFQrtBinCount = 0;
	
	//20081219 EQC frame
	protected static boolean EQC_flag = false;// 
	protected static ButtonGroup EQC_grpbt = new ButtonGroup();
	protected static ButtonGroup EQC_sub_grpbt = new ButtonGroup();//20100120
	protected static ButtonGroup QNum_grpbt = new ButtonGroup();//20100618
	protected static String[] EQC_btname = {"QBIN1", "QBIN2", "QBIN3","QBIN4", "QBIN5", "QBIN6","QBIN7","QBIN8"};//20100618
	protected static String[] QNum_btname = {"QA1", "QRT1", "QRT2","QRT3"};//20100618
	protected static String[] EQC_sub_btname = {"EQC1", "EQC2", "EQC3", "EQC4", "EQC5"};//20110209
	protected static String[] SocketCount; //20130411 count socket contact 
	protected static String SocketCount2[] = new String[8]; //20130411 count socket contact 
	protected static JRadioButton[] EQC_bt = new JRadioButton[EQC_btname.length];
	protected static JRadioButton[] EQC_sub_bt = new JRadioButton[EQC_btname.length]; //20100120
	protected static JRadioButton[] QNum_bt = new JRadioButton[QNum_btname.length]; //20100618
	protected static Container EQC_c = new Container();
	protected static JFrame EQC_frame = new JFrame("EQC bin options");
	protected static Label EQC_lab = new Label(" Please select QBIN ");//20100618
	protected static Label Qnum_lab = new Label(" Please select Q number ");//20100618
	protected static Label QRT_lab = new Label(" Please select QRT bin ");//20100618
	protected static JPanel EQC_p1= new JPanel();                         
	protected static JPanel EQC_p2= new JPanel();          
	protected static JPanel EQC_p3= new JPanel(); 
	protected static JPanel EQC_p4= new JPanel(); 
	protected static JPanel EQC_p5= new JPanel(); 
	protected static JPanel EQC_p6= new JPanel(); 
	protected static JPanel EQC_p7= new JPanel(); 
	protected static JPanel EQC_p8= new JPanel(); 
	protected static Button EQC_frame_bt = new Button("OK");
	protected static Button EQC_frame_bt2 = new Button("Select ANF Bin");
	protected static String EQCStr_qbin_set = "";
	protected static String EQCStr_bin_set = "";//20100120
	protected static int EQC_num = 0;
	protected static String EQC_qbin_Str = "";
	protected static String EQC_bin_Str = "";
	protected static String EQC_bin_F128_Str = "";
	protected static String EQC_QNum_Str = "";
	protected static String EQC_QNum_Str2 = "";
	protected static String EQC_QRT_Str = "";
	//20081219 CORR frame 
	protected static boolean CORR_flag = false;//
	protected static boolean checkCORR_flag = false;//
	protected static boolean CORR_L176_flag = false;//
	protected static ButtonGroup CORR_grpbt = new ButtonGroup();
	protected static ButtonGroup L176_Corr_grpbt = new ButtonGroup();
	protected static String[] CORR_btname = {"PASS", "FAIL" };
	protected static String[] L176_corr_str = {"A. New setup                                                                                                    ","B. Equipments stop working or crash over 24 hour and then re-work","C. After equipments have been retumed from engineers.            ","D. The yield rate is abnormal(<hold yield,Yield=100%)            ","E. EQC fail                                                      ","F. Continue test over 72 hours(machine never stop)               "};
	//    private static JComboBox[] L176_corr_comboBoxList;// = new JComboBox[L176_corr_str.length];
	private static JRadioButton[] L176_corr_JRadioButtonList = new JRadioButton[L176_corr_str.length];
	protected static JRadioButton[] CORR_bt = new JRadioButton[CORR_btname.length];
	protected static Container CORR_c = new Container();
	protected static JFrame CORR_frame = new JFrame("Correlation options");
	protected static Label CORR_lab = new Label(" Select Correlation type ");
	protected static JPanel CORR_p1= new JPanel();                         
	protected static JPanel CORR_p2= new JPanel();          
	protected static JPanel CORR_p3= new JPanel(); 
	protected static Button CORR_frame_bt = new Button("OK");
	protected static String CORRStr_set = "";
	protected static int CORR_num = 0;
	protected static int checkCORR_num = 0;

	protected static ButtonGroup CORRbin_grpbt = new ButtonGroup();//For Select Corr Bin Frame add by Cola. 20160407----Start
	protected static String[] CORRbin_btname = {"B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10" };
	protected static JRadioButton[] CORRbin_bt = new JRadioButton[CORRbin_btname.length];
	protected static Container CORRbin_c = new Container();
	protected static JFrame CORRbin_frame = new JFrame("Correlation Bin options");
	protected static Label CORRbin_lab = new Label(" Select Correlation Bin ");
	protected static JPanel CORRbin_p1= new JPanel();                         
	protected static JPanel CORRbin_p2= new JPanel();          
	protected static JPanel CORRbin_p3= new JPanel(); 
	protected static Button CORRbin_frame_bt = new Button("OK");
	protected static String CORRbinStr_set = "";
	protected static int CORRbin_num = 0;//For Select Corr Bin Frame add by Cola. 20160407-----End
	// 20150303 new frame corr check by ChiaHui start 
	protected static ButtonGroup checkCorr_grpbt = new ButtonGroup();
	protected static String[] checkCorr_btname = {"NA","YES", "NO", "DO NOT Need" };
	protected static JRadioButton[] checkCorr_bt = new JRadioButton[checkCorr_btname.length];
	protected static Container checkCorr_c = new Container();
	protected static JFrame checkCorr_frame = new JFrame("Check Corr options");
	protected static Label checkCorr_lab = new Label("Confirm whether the CORR is completed?");
	protected static JPanel checkCorr_p1= new JPanel();                         
	protected static JPanel checkCorr_p2= new JPanel();          
	protected static JPanel checkCorr_p3= new JPanel(); 
	protected static Button checkCorr_frame_bt = new Button("OK");
	protected static String checkCorrStr_set = "";
	// 20150303 new frame corr check by ChiaHui end

	//20100722 Efuse frame for MTK
	protected static JFrame EfuseCheck_error_frame = new JFrame("WRONG");//20100722
	protected static Label EfuseCheck_error1_lab = new Label("WRONG");//20100722
	protected static JTextArea EfuseCheck_error2_JText = new JTextArea("There are some IC had been tested again," + "\n" + "please call leader to check any Quality issue.");//20100722
	protected static Button EfuseCheck_error_frame_bt = new Button("OK");//20100722
	protected static Container EfuseCheck_error_c = new Container();
	protected static JPanel EfuseCheck_error_p1= new JPanel();
	protected static JPanel EfuseCheck_error_p2= new JPanel();
	protected static JPanel EfuseCheck_error_p3= new JPanel();

	protected static JFrame EfuseCheck_pass_frame = new JFrame("PASS");//20100722
	protected static Label EfuseCheck_pass1_lab = new Label("PASS");//20100722
	protected static Label EfuseCheck_pass2_lab = new Label("The result is pass");//20100722
	protected static Button EfuseCheck_pass_frame_bt = new Button("OK");//20100722
	protected static Container EfuseCheck_pass_c = new Container();
	protected static JPanel EfuseCheck_pass_p1= new JPanel();
	protected static JPanel EfuseCheck_pass_p2= new JPanel();
	protected static JPanel EfuseCheck_pass_p3= new JPanel();

	//20101026
	protected static int barFlagVal = 0;
	protected static String tmpCPFilePath = "/tmp/CPData/";

	//20101013
	protected static boolean JobFlag = false;
	protected static int XMLCnt = 0;
	protected static String JobCheckStr = "";

	//20110613    gpib check//20110614  //201204
	protected static JFrame GpibCheck_frame;
	protected static JLabel GpibCheck_lab;  
	protected static JLabel[][] GpibCheck_lab2 = new JLabel[15][3]; 
	protected static JTextArea GpibCheck_JText;
	protected static Button GpibCheck_frame_bt;
	protected static Container GpibCheck_c;
	protected static JPanel GpibCheck_p[] = new JPanel[15];  //Add by Cola. for Handler GPIB error frame.
	//gpib cp 
	protected static JFrame GpibCheck_CP_frame;
	protected static JLabel GpibCheck_CP_lab;  
	protected static JLabel GpibCheck_CP_lab11;
	protected static JLabel GpibCheck_CP_lab12;
	protected static JLabel GpibCheck_CP_lab20;
	protected static JLabel GpibCheck_CP_lab21;
	protected static JLabel GpibCheck_CP_lab22;
	protected static JLabel GpibCheck_CP_lab30;
	protected static JLabel GpibCheck_CP_lab31;
	protected static JLabel GpibCheck_CP_lab32;
	protected static JLabel GpibCheck_CP_lab40;
	protected static JLabel GpibCheck_CP_lab41;
	protected static JLabel GpibCheck_CP_lab42;
	protected static JLabel GpibCheck_CP_lab50;
	protected static JLabel GpibCheck_CP_lab51;
	protected static JLabel GpibCheck_CP_lab52;
	protected static JLabel GpibCheck_CP_lab60;
	protected static JLabel GpibCheck_CP_lab61;
	protected static JLabel GpibCheck_CP_lab62;    
	protected static JTextArea GpibCheck_CP_JText;
	protected static Button GpibCheck_frame_CP_bt;
	protected static Container GpibCheck_CP_c;
	protected static JPanel GpibCheck_CP_p1;
	protected static JPanel GpibCheck_CP_p2;
	protected static JPanel GpibCheck_CP_p3;
	protected static JPanel GpibCheck_CP_p4;
	protected static JPanel GpibCheck_CP_p5;
	protected static JPanel GpibCheck_CP_p6;

	protected static JLabel[][] ConfigCheck_Label = new JLabel[4][11];	//20150707 cal file check by ChiaHui
	protected static JFrame ConfigCheck_frame;

	protected static boolean jobFlag = false;
	protected static boolean LB_PCflag = false;//20081104

	protected static JButton bt_GPIB = new JButton(); //Clean button//20110712
	protected static JButton bt_EQU = new JButton(); //EQU check kit,LB,socket button by ChiaHui 20130516
	protected static int gpib_cnt=0;                                 //20110712
	protected static boolean gpibFlag=true;                          //20110712
	protected static String recipeFileforComp="";                    //20120828
	protected static String loadFileforComp="";                      //20120828
	protected static int MTKFlag=1;                                  //20120828
	protected static String barcode_CorrlotidStr = "";//20120828
	protected static String autoSendPathCP_MAP = "";//20120828
	protected static String autoSendPathCP_Summary = "";//20130121
	protected static boolean autoSend_CP = true;//20120828 enable the function of copy CP summary file to server in csicDataCollection.cpp
	protected static int mailFlag = 0;//20120828 enable the function of mail errorMsg to server
	protected static String mailStr = "cola_shen@sigurd.com.tw";//20120828 the mail address which receive the errorMsg
	protected static String dateCodeStr = "";//20130131 datecode       
	protected static String barcode_LB = "";//20130416 save LB barcode add by ChiaHui
	protected static String barcode_KIT = "";//20130416 save KIT barcode ass by ChiaHui
	protected static String barcode_LB2 = "";//20130416 save LB barcode add by ChiaHui
	protected static String barcode_KIT2 = "";//20130416 save KIT barcode ass by ChiaHui 
	protected static String barcode_L100_Tar_File = "";//20150303 save xml tar file name by ChiaHui	
	protected static String barcode_OI_content  = "";   //20140703 barcode_OI_content to dataCollection.cpp
	protected static String STDF_start_time  = "";   //20140704 barcode_OI_content to dataCollection.cpp    
	protected static int FT_EQUendlot = 0;//20130516 if value=1 then enable bt2_1 bt2_2, or only enable bt2_2 
	protected static String probeCard_string = "";//20131224 cp probeCard
	protected static String[] probeCard_array_string;//20131224 cp probeCard
	protected static String L176_FT_corr_reason = "";//20140428 by ChiaHui

	private static   boolean online  = true; //--hh, online  == true, run online;      online  == false, do not run online; 
	private static   boolean offline = false;  //--hh, offline == true, run offline;     offline == false, do not run offline; 
	private static 	 boolean engMode = false; //for using launcher. 20161227
	protected static boolean openOICu_flag = false; //Add by Cola. 20150714
	protected static int F137_corr_flag = 0;//20140613 by ChiaHui
	protected static String NO_Corr_Reason = "";//20150304 by ChiaHui for save Corr Reason
	protected static boolean AutoEndLot_flag;  //Add by Cola for Auto End Lot enable. 20150611
	protected static boolean AutoEndLot_Timeout; //Add by Cola for Auto End Lot when Timeout. 20151016
	protected Timer EndLotTimer;  //Add by Cola for Auto End Lot Timer 20150611  
	protected static String HandlerAlarmSetup_Command = "";  //Add by Cola for Handler Bin Alarm Setup. 20150901
	protected static String HandlerSW_sendGPIB = ""; //Add by Cola. Send new GPIB command for Different Handler Software. 20151020 
	protected static String HandlerSW_Version = ""; //Add by Cola for Different Handler Software. 20150925
	protected static String LBNo_with_OldLBName = ""; //Save for STDF and Summary new L/B, P/C content. by Cola. 20160307
	protected static boolean Enable_AlarmSetup = true; //Add by Cola. 20160325
	protected static boolean STR_TestFlow_flag = false; //Add by Cola for MTK STR load file. 20160506
	protected static String InfoFileName = ""; //Add by Cola for F186 InfoFile path with file name. 20160629
	protected static String InfoFilePath = ""; //Add by Cola for F186 InfoFile path with file name. 20160629
	protected static String F186_RTbin = "", JOB_REV_Str_Rcp = "", F186_NoTimeStr_SumName = "";
	protected static boolean NewHandler_GPIBnoSupport = false; //Add by Cola for new Handler no GPIB check. 20160823
    protected static boolean DC_MapStartLink_Flag = true; //20170220
    protected static boolean DC_MapEndLink_Flag = true; //20170220
    protected static boolean DC_DeviceEotLink_Flag = true; //20170220
    protected static boolean DC_DeviceEotRunAll_Flag = false; //20170220
    protected static boolean Load_ProbeCard_RCP = false;
    protected static boolean HWstationUsage_UnloadOI = false; //20170821
    protected static boolean WaferID_detector_RCP = true;	//20170824 Default is enable
    protected static boolean SBC_Enable_RCP = false; //20170912
    protected static String SBC_Rule_RCP = ""; //20170912
    protected static String Smartbintray_mode = ""; //20171012
    protected static boolean MTK_series = false; //20171020
    protected static String Summary_LOT_START_TIME = "";	//20171226
    
    protected static boolean TryRunOI = false; //20170605
    
	public csicDXAuto_Sigurd_CUS() {
		super("csicDMDAuto_Sigurd_CUS");

		if (firstFlag==1) {
			preFrameBarCode();
		} else {
			firstFlag = 0;
		}
	}
	
	static boolean MTK_HSMCode_Copy(){	//20170808
		String cmd = "";
		String SourcePath = "/dx_profile/autoload/Barcode/" + barcode_testeridStr + "/";
		String LocalPath = "/dx_profile/prod/MTK_HSMCode/" + barcode_testeridStr + "/";
//		String LocalPath = "/home/MTK_HSMCode/" + barcode_testeridStr + "/";
		String file = barcode_lotidStr + "*.dat"; //Lot_ID + Slot_NO
		
		cmd = "rm -rf " + LocalPath;
		javaExecSystemCmd2(cmd, 500);
		File localDir = new File(LocalPath);
		if(localDir.exists()){
			JOptionPane.showMessageDialog(null, "Local MTK_HSMCode File can not be delete , please call PE to check code file Permission");
			return(false);
		}
		
		cmd = "mkdir " + LocalPath;
		javaExecSystemCmd2(cmd, 500);
		
		javaExecSystemCmd2("MTK_HSM_File_Copy.csh " + LocalPath +" "+ SourcePath +" "+ file , 2000);
		
//		cmd = "cp " + SourcePath + file + " " + LocalPath;
//		javaExecSystemCmd2(cmd, 2000);
//		cmd = "rm -f " + SourcePath + file;
//		javaExecSystemCmd2(cmd, 500);
//		cmd = "chmod 444 " + LocalPath + file;
//		javaExecSystemCmd(cmd);
		
		File sourceDir = new File(SourcePath);
		String[] sourceDir_list;
		sourceDir_list = sourceDir.list();
		for(int i=0 ; i<sourceDir_list.length ; i++){

			if(sourceDir_list[i].indexOf(".dat") != -1){

				JOptionPane.showMessageDialog(null, "Source MTK_HSMCode File can not be delete , please call PE to check code file Permission");
//				JOptionPane.showMessageDialog(null, "Source MTK_HSMCode File can not be delete "+"("+sourceDir_list[i]+")"+", please call PE to check code file Permission");
				return(false);
//				System.exit(1); // to quit Java app for Linux
			}
		}
		
	//Check HSMCode file Exist-----Start
		
		String infileStr = "", tmpStr = "", ErrorFile = "";
//		String paramStr[] = new String[3];
		boolean unloadOI = false;
		try {
			for(int i=0 ; i<barcode_Wafer_Number.length ; i++){
				infileStr = barcode_lotidStr + "-" + barcode_Wafer_Number[i] + ".dat";
				File HSMcodeFile = new File(LocalPath + infileStr);
//				br = new BufferedReader(new FileReader(LocalPath + infileStr));//open file
				if(!HSMcodeFile.exists()){
					unloadOI = true;
					ErrorFile += infileStr + "\n";
				}
			}
		} catch (Exception err) {
			tmpStr  = "<HSMcode get File> get dat file Error: " + err + "\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
//			saveErrorMessageRealtime("Msg:MTK HSM_CODE file is not found");
//			killproc(); System.exit(1); // to quit Java app for Linux
			return(false);
		}

		if (unloadOI){
			tmpStr = "<Warning>  "/* + infileStr*/ + "   HSM CODE File is not found\n";
			tmpStr += "+=====================================================+ \n";
			tmpStr += "         Please call Supervisor to Check	 \n";
			tmpStr += "         Unload OI now !!!			 \n";
			tmpStr += "+=====================================================+ \n"+" "+"\n";
			tmpStr += "File loss List:\n" + ErrorFile;
			JOptionPane.showMessageDialog(null, tmpStr);	
			return(false);
//			killproc();System.exit(1);	// to quit Java app for Linux
		} 
	//Check HSMCode file Exist-----End	
		return(true);
	}
	
	static void L227_infoFIle_Copy(){	//20170710
		String cmd = "";
		String SourcePath = "/dx_profile/autoload/Barcode/" + barcode_testeridStr + "/";
		String LocalPath = "/home/swcpd/STPWIN/";
		String file = "info.spil";
		
		cmd = "rm -rf " + LocalPath;
		javaExecSystemCmd2(cmd, 500);
		cmd = "mkdir " + LocalPath;
		javaExecSystemCmd2(cmd, 500);
		cmd = "cp " + SourcePath + file + " " + LocalPath;
		javaExecSystemCmd2(cmd, 2000);
		cmd = "chmod 444 " + LocalPath + file;
		javaExecSystemCmd(cmd);
		
	//Check info.spil file with Barcode file-----Start
		String infileStr = LocalPath + file;
		String tmpStr = "", infoFile_Device_Name = "", infoFile_DEVICE_CODE = "";
		String paramStr[] = new String[3];
		boolean unloadOI = false;
		try {
			br = new BufferedReader(new FileReader(infileStr));//open file

			while ((tmpStr = br.readLine()) != null) {

				if (tmpStr.length()!=0) {

					tmpStr = stringRemoveSpaceHeadTail(tmpStr);
//					JOptionPane.showMessageDialog(null, "tmpStr = " + tmpStr);

					paramStr = tmpStr.split(" ");

					for(int i = 0 ; i < paramStr.length ; i++){
						paramStr[i] = stringRemoveSpaceHeadTail(paramStr[i]);
						paramStr[i] = paramStr[i].substring(1, paramStr[i].length()-1);
					}
//					JOptionPane.showMessageDialog(null, "paramStr[0] = " + paramStr[0]);
//					JOptionPane.showMessageDialog(null, "paramStr[1] = " + paramStr[1]);
//					JOptionPane.showMessageDialog(null, "paramStr[2] = " + paramStr[2]);

					if (paramStr[1].equals("Device_Name")) {
						infoFile_Device_Name = paramStr[2];
//						JOptionPane.showMessageDialog(null, "infoFile_Device_Name = " + infoFile_Device_Name);
					} else if (paramStr[1].equals("DEVICE_CODE")) {
						infoFile_DEVICE_CODE = paramStr[2];
//						JOptionPane.showMessageDialog(null, "infoFile_DEVICE_CODE = " + infoFile_DEVICE_CODE);
					} 
				}
			}
			br.close(); // close file
		} catch (FileNotFoundException event) {

			tmpStr  = "<L227 get File> get info.spil: " + event + "\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			saveErrorMessageRealtime("Msg:L227 info.spil file is not found");
			killproc(); System.exit(1); // to quit Java app for Linux

		} catch (java.io.IOException err) {
			tmpStr  = "<L227 get File> get info.spil: " + err + "\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			saveErrorMessageRealtime("Msg:L227 info.spil file is not found");
			killproc(); System.exit(1); // to quit Java app for Linux
		}
		
		if(!infoFile_Device_Name.equals(barcode_devicetypeStr__)) {
			unloadOI = true;
			tmpStr = "<Warning>  "/* + infileStr*/ + "   Device Item Check is Fail\n";
			tmpStr += "+=====================================================+ \n";
			tmpStr += "         Please call Supervisor to Check	 \n";
			tmpStr += "         Unload OI now !!!			 \n";
			tmpStr += "+=====================================================+ \n"+" "+"\n";
			tmpStr += "Barcode    Device_Name	: " + barcode_devicetypeStr__ + "\n";
			tmpStr += "info.spil    Device_Name	: " + infoFile_Device_Name + "\n";
			JOptionPane.showMessageDialog(null, tmpStr);	
		} 
		if(!infoFile_DEVICE_CODE.equals(barcode_SPIL_C_Item_Str) || infoFile_DEVICE_CODE.equals("NA")) {
			unloadOI = true;
			tmpStr = "<Warning>  "/* + infileStr*/ + "   CODE Item Check is Fail\n";
			tmpStr += "+=====================================================+ \n";
			tmpStr += "         Please call Supervisor to Check	 \n";
			tmpStr += "         Unload OI now !!!			 \n";
			tmpStr += "+=====================================================+ \n"+" "+"\n";
			tmpStr += "Barcode    Device_CODE	: " + barcode_SPIL_C_Item_Str + "\n";
			tmpStr += "info.spil    Device_CODE	: " + infoFile_DEVICE_CODE + "\n";
			JOptionPane.showMessageDialog(null, tmpStr);
		}
		if (unloadOI)
		{killproc();System.exit(1);} // to quit Java app for Linux
	//Check info.spil file with Barcode file-----End	
	}
	private static String getF186InfoFileData_TestMode(){		
		String test_mode = "";
		String X1 = "", X2 = "", X3 = "", X4 = "";

		if(testTypeStr.equalsIgnoreCase("Final"))
			X1 = "B";
		else if(testTypeStr.equalsIgnoreCase("Wafer"))
			X1 = "S";

		int length = barcode_stationStr.length();
		X2 = barcode_stationStr.substring(length-1, length);

		//    	if(Double.parseDouble(barcode_temperatureStr) >= 25)
		//    		X2 = "1";
		//    	else
		//    		X2 = "2";

		//    	if(sumRepNum == 1)
		X3 = "1";
		//    	else
		//    		X3 = "2";

		//    	if(barcode_FormerLOTID_Str.equals(""))
		//    		X4 = "X";
		//    	else{
		//    		if(RTStr.equalsIgnoreCase("Corr"))
		//    			X4 = "E";
		//    		else if(testTypeStr.equalsIgnoreCase("Wafer") && RTStr.equalsIgnoreCase("RT"))
		//    			X4 = "A";
		//    		else
		X4 = "P";		
		//    	}   	
		//    	STDfileStr
		test_mode = X1 + X2 + X3 + X4;

		return test_mode; 	
	}
	private static String getF186InfoFileData_TestType(){
		String test_type = "";
		if(RTStr.equalsIgnoreCase("RT"))
			test_type = "W";
		else{
			if(sumRepNum == 1)
				test_type = "N";
			else
				test_type = "F";
		}
		return test_type;
	}

	static public void generate_userFlag_file() {	//20140704 by Chia_Hui

		String tmpStr = "";
		try{

			javaExecSystemCmd2("mkdir /tmp/barcode_file/",200);
			javaExecSystemCmd2("chmod 777 /tmp/barcode_file/",200);			
			javaExecSystemCmd2("rm -f /tmp/barcode_file/userFlag.txt",200);				
			javaExecSystemCmd2("touch /tmp/barcode_file/userFlag.txt",200); 
			javaExecSystemCmd2("chmod 777 /tmp/barcode_file/userFlag.txt",200);
			if(testTypeStr.equalsIgnoreCase("Wafer")){
				javaExecSystemCmd2("mkdir /dx_summary/CP/" + barcode_customerStr + "_STDF/" ,200);
				javaExecSystemCmd2("chmod 777 /dx_summary/CP/" + barcode_customerStr + "_STDF/" ,200);			
				javaExecSystemCmd2("mkdir /dx_summary/CP/" + barcode_customerStr + "_STDF/" + barcode_devicetypeStr__ ,200);
				javaExecSystemCmd2("chmod 777 /dx_summary/CP/" + barcode_customerStr + "_STDF/" + barcode_devicetypeStr__,200);
				javaExecSystemCmd2("mkdir /dx_summary/CP/" + barcode_customerStr +"_summary/",200);
				javaExecSystemCmd2("chmod 777 /dx_summary/CP/" + barcode_customerStr +"_summary/",200);
				javaExecSystemCmd2("mkdir /dx_summary/CP/" + barcode_customerStr +"_summary/" + barcode_devicetypeStr__ ,200);
				javaExecSystemCmd2("chmod 777 /dx_summary/CP/" + barcode_customerStr +"_summary/" + barcode_devicetypeStr__,200);
				javaExecSystemCmd2("mkdir /dx_summary/CP/" + barcode_customerStr + "/",200);  //20170109
				javaExecSystemCmd2("chmod 777 /dx_summary/CP/" + barcode_customerStr + "/",200); //20170109
				javaExecSystemCmd2("mkdir /dx_summary/CP/" + barcode_customerStr + "/" + barcode_devicetypeStr__,200); //20170109
				javaExecSystemCmd2("chmod 777 /dx_summary/CP/" + barcode_customerStr + "/" + barcode_devicetypeStr__,200); //20170109
			} else if(testTypeStr.equalsIgnoreCase("Final")){ //20170619
				javaExecSystemCmd2("mkdir /dx_summary/FT/" + barcode_customerStr + "_STDF/" ,200);
				javaExecSystemCmd2("chmod 777 /dx_summary/FT/" + barcode_customerStr + "_STDF/" ,200);			
				javaExecSystemCmd2("mkdir /dx_summary/FT/" + barcode_customerStr + "_STDF/" + barcode_devicetypeStr__ ,200);
				javaExecSystemCmd2("chmod 777 /dx_summary/FT/" + barcode_customerStr + "_STDF/" + barcode_devicetypeStr__,200);
				javaExecSystemCmd2("mkdir /dx_summary/FT/" + barcode_customerStr +"_summary/",200);
				javaExecSystemCmd2("chmod 777 /dx_summary/FT/" + barcode_customerStr +"_summary/",200);
				javaExecSystemCmd2("mkdir /dx_summary/FT/" + barcode_customerStr +"_summary/" + barcode_devicetypeStr__ ,200);
				javaExecSystemCmd2("chmod 777 /dx_summary/FT/" + barcode_customerStr +"_summary/" + barcode_devicetypeStr__,200);
				javaExecSystemCmd2("mkdir /dx_summary/FT/" + barcode_customerStr + "/",200);
				javaExecSystemCmd2("chmod 777 /dx_summary/FT/" + barcode_customerStr + "/",200);
				javaExecSystemCmd2("mkdir /dx_summary/FT/" + barcode_customerStr + "/" + barcode_devicetypeStr__,200);
				javaExecSystemCmd2("chmod 777 /dx_summary/FT/" + barcode_customerStr + "/" + barcode_devicetypeStr__,200);
			}
//			STDF_start_time = getDateTime3(); Remark. 20171121

			barcode_OI_content = "lotID_Str=" + barcode_lotidStr + "\n";
			barcode_OI_content += "CP temp=" + barcode_temperatureStr + "\n";
			barcode_OI_content += "TestStation_Str=" + barcode_stationStr + "\n";
			barcode_OI_content += "runCardStr=" + barcode_sgidStr + "\n";
			barcode_OI_content += "OI_timeStr=" + STDF_start_time + "\n";
			barcode_OI_content += "OI_stdf_file_path=" + "/tmp/" + STDF_start_time + "_" + barcode_lotidStr + "_" + barcode_stationStr + "D__" + barcode_sgidStr + "\n";				
			barcode_OI_content += "final_stdf_file_path=" + "/dx_summary/CP/" + barcode_customerStr +"_STDF/" + barcode_devicetypeStr__ + "\n";	
			barcode_OI_content += "final_summary_file_path=" + "/dx_summary/CP/" + barcode_customerStr +"_summary/" + barcode_devicetypeStr__ + "\n";
			barcode_OI_content += "OI_txt_file_name=" + "/tmp/" + STDF_start_time + "_" + barcode_lotidStr + "_" + barcode_stationStr + "D__" + barcode_sgidStr + "\n"; //Add local path by Cola. 20170109
			if(barcode_devicetypeStr.equals("BHH10326CW") || barcode_devicetypeStr.equals("BHH10326DW")){

				if (probeDeviceRcp.equals(""))
				{
					JOptionPane.showMessageDialog(null, "BHH10326CW or BHH10326DW rcp file no probe device setting! Call PE check!");
					autoSaveMessageBeforeExit();
					saveErrorMessageRealtime("Msg:BHH10326CW or BHH10326DW rcp no probe device setting!");
					killproc(); 
					System.exit(1); // to quit Java app for Linux
				}else
				{
					barcode_OI_content += "barcode_ProbeDeviceName=" + probeDeviceRcp + "\n";
				}
			}else{
				barcode_OI_content += "barcode_ProbeDeviceName=" + barcode_ProbeDeviceName + "\n";
			}
			barcode_OI_content += "barcode_locationCode=" + barcode_locationCode + "\n";

			barcode_OI_content += "Device Name=" + barcode_devicetypeStr + "\n";
			barcode_OI_content += "SG_Customer=" + barcode_customerStr + "\n";
			barcode_OI_content += "SG_LoadFile=" + barcode_programStr + "\n";
			barcode_OI_content += "SG_LotNumber=" + barcode_lotidStr + "\n";
			barcode_OI_content += "SG_Number=" + barcode_sgidStr + "\n";
			//barcode_OI_content += "SG_ProbeCard_ID=" + tx0b.getText() + "\n";
			barcode_OI_content += "SG_ProbeCard_ID=" + barcode_lbidStr + "\n";
			barcode_OI_content += "SG_BARCODE_PC=" + barcode_lbidStr + "\n";
			barcode_OI_content += "SG_RecipeFile=" + recipeFile + "\n";
			barcode_OI_content += "SG_TestStation=" + barcode_stationStr + "\n";				
			barcode_OI_content += "SG_TesterHostname=" + barcode_testeridStr + "\n";
			barcode_OI_content += "SG_EfuseFlag=" + EfuseFlag + "\n";

			//Add F186 InfoFile data to CP Datacollection by Cola. 20160525-----Start
			if(testTypeStr.equalsIgnoreCase("Wafer") && (barcode_customerStr.equals("F186") || barcode_customerStr.equals("F191"))){          	
				barcode_OI_content += "PROBER=" + barcode_proberidStr + "\n";
				barcode_OI_content += "PROBERID=" + barcode_handleridStr + "\n";
				barcode_OI_content += "F186_SIFOLOTID=" + barcode_FormerLOTID_Str + "\n";
				barcode_OI_content += "F186_MANUFACTURINGID=" + barcode_DESIGN_TYPE_Str + "\n";
				barcode_OI_content += "F186_TESTMODE=" + getF186InfoFileData_TestMode() + "\n";
				barcode_OI_content += "F186_TESTTYPE=" + "N" + "\n";
				barcode_OI_content += "F186_PRODUCTIONSITE=" + "SDK" + "\n";
				barcode_OI_content += "F186_REVISION=" + JOB_REV_Str_Rcp + "\n";
				if(!barcode_customerTO_Str.equals(""))
					barcode_OI_content += "F186_customerTO=" + barcode_customerTO_Str + "\n";
				if(barcode_programStr.endsWith(".job"))
					barcode_OI_content += "F186_TESTPROGRAM=" + barcode_programStr.substring(0,barcode_programStr.length()-4) +"\n";
				else
					barcode_OI_content += "F186_TESTPROGRAM=" + barcode_programStr +"\n";
			} //Add F186 InfoFile data to CP Datacollection by Cola. 20160525-----End
            //Add content for SBC control by Cola. 20161209-----Start
			if(!barcode_TangoSBCset_Str.equals("") && barcode_customerStr.equals("L121"))
			{
				String barcode_TangoSBCset_Str_sub[];
				barcode_TangoSBCset_Str_sub = barcode_TangoSBCset_Str.split("_");
				
				barcode_OI_content += "SBC_Rule_Index=" + TangoSBC_Rule_index;
				
				for(int in=0 ; in<TangoSBC_Rule_index ; in++){
					String barcode_TangoSBCrule_Str_sub[];
					barcode_TangoSBCrule_Str_sub = barcode_TangoSBCrule_Str[in].split("|");
//					Integer.parseInt("").parseDouble
//					if(barcode_TangoSBCset_Str_sub[in].indexOf("DUT") != -1){
						barcode_OI_content += "barcode_SBC_Rule_" + (in+1) + "=" + barcode_TangoSBCset_Str_sub[in] + "|" + barcode_TangoSBCrule_Str_sub[0] + "|" + barcode_TangoSBCrule_Str_sub[1] + "\n";				
//					}
	
				}
			}
			//Add content for SBC control by Cola. 20161209-----End
			barcode_OI_content += "SG_DataCollection_map_start_link=" + DC_MapStartLink_Flag + "\n"; //20170220
			barcode_OI_content += "SG_DataCollection_map_end_link=" + DC_MapEndLink_Flag + "\n"; //20170220
			barcode_OI_content += "SG_DataCollection_device_eot_link=" + DC_DeviceEotLink_Flag + "\n"; //20170220
			barcode_OI_content += "SG_DataCollection_device_eot_runAll=" + DC_DeviceEotRunAll_Flag + "\n"; //20170220
			barcode_OI_content += "SG_OCR_ID=" + barcode_OCR_ID_Str + "\n";	//20170220
			if(!SBC_Rule_RCP.equals("")){ //20170912
				barcode_OI_content += "barcode_SBC_Enable=" + SBC_Enable_RCP + "\n"; 
				barcode_OI_content += "barcode_SBC_Rule=" + SBC_Rule_RCP + "\n";
			}
			if(testTypeStr.equalsIgnoreCase("Final")){	//20171121
				barcode_OI_content += "SG_Summary_Naming=" + summaryfileStr_MES.substring(0, summaryfileStr_MES.length()-4) + "\n";
				barcode_OI_content += "SG_Summary_LOT_START_TIME=" + Summary_LOT_START_TIME + "\n";	//20171226
				
				barcode_OI_content += "SG_KIT_Number=" + kit_No_panel.getText() + "\n";	//20171229-----Start
				for(int x=0; x < barcode_duts_int;x++){	
					if (!SocketCount[x].equals("")){		            		
//						tmpStr += "ProductionSetUserValue " + "site" + x + ": " + SocketCount[x] + "\n";									
						checkSocketData += "site" + x + " = " + SocketCount[x] + "\n";
						barcode_OI_content += "site" + x + "=" + SocketCount[x] + "\n";
					}
				}	//20171229-----End
				
			}
			barcode_OI_file = new File("/tmp/barcode_file/userFlag.txt");

			FileWriter myFW;

			myFW = new FileWriter(barcode_OI_file,true);
			myFW.write(barcode_OI_content);
			myFW.close();

		}catch (java.io.IOException err) {

			tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
		}
	}

	static public void EQUsocketCheckdata(String inStr) {	//20130514 by Chia_Hui

		String tmpStr = "";
		try {

			checkSocketData = "/tmp/socket.txt";

			outcheckSocketData = new File(checkSocketData);
			FileWriter myFW;

			myFW = new FileWriter(outcheckSocketData);
			myFW.write(inStr);
			myFW.close();

			tmpStr = "save socket data To " + outcheckSocketData + "\n";
			textArea2.append(tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
		}
	}

	/*static public void ProductCheckSummary(String inStr) {

		String tmpStr = "";
		try {

			Date myDateTime = new Date();	//20100107

			myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
			mydatetime = myformatter.format(myDateTime);    
			checkSummaryFile = "/usr/local/home/prod/02ChiaHui/" + hostnameStr + "_" + SGStr + "_" + mydatetime + ".txt";

			outcheckSummary = new File(checkSummaryFile);
			FileWriter myFW;

			myFW = new FileWriter(outcheckSummary, true);
			myFW.write(inStr);
			myFW.close();

			tmpStr = "save Product Check Summary Message To " + outcheckSummary + "\n";
			textArea2.append(tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
		}
}*/   

	static public void SaveNoCorrReason(String inStr) {

		String tmpStr;
		try {

			Date myDateTime = new Date();

			outfStrRealError = autoResultPath + "FT" + "/NoCorrReason.txt";
			File dmdauto_ErrorMsg = new File(outfStrRealError);                              //20100107

			myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
			mydatetime = myformatter.format(myDateTime);    

			String outStr = "";
			outStr += hostnameStr + " ";
			outStr += mydatetime + " ";           
			if(barcode_sgidStr.equals("")) {
				outStr += "NULL ";
			}
			else{
				outStr += barcode_sgidStr + " ";
			}
			outStr += barcode_opidStr + " ";	//20130909 ChiaHui update,new add OP id             
			outStr += inStr +"\n";

			FileWriter myFW;

			myFW = new FileWriter(dmdauto_ErrorMsg, true);
			myFW.write(outStr);
			myFW.close();

			tmpStr = "save Message To " + outfStrRealError + "\n";
			textArea2.append(tmpStr);
			//JOptionPane.showMessageDialog(null, tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
		}

	}   

	static public void saveMessageRealtime(String inStr) {

		String tmpStr;
		try {

			Date myDateTime = new Date();
			//startTime = thisDate.getTime();
			//or startTime = thisDate.toString();
			//startTime = Tue May 16 16:11:33 CST 2006

			if(outfRealFlag==0) {
				myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
				mydatetime = myformatter.format(myDateTime);    
				outfStrReal = messagePath + "dmdauto_msgRealTime_" + mydatetime + ".txt";
				outfRealFlag =1;
			}

			outfReal = new File(outfStrReal);
			FileWriter myFW;

			myFW = new FileWriter(outfReal, true);
			myFW.write(inStr);
			myFW.close();

			tmpStr = "save Message To " + outfReal + "\n";
			textArea2.append(tmpStr);
			//JOptionPane.showMessageDialog(null, tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
		}

	}

	static public void saveErrorMessageRealtime(String inStr) {

		String tmpStr;
		try {

			Date myDateTime = new Date();

			outfStrRealError = autoResultPath + "dmdauto_ErrorMsgRealTime.txt";
			File dmdauto_ErrorMsg = new File(outfStrRealError);                              //20100107

			myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
			mydatetime = myformatter.format(myDateTime);    
			/*if(outfRealFlag==0) {
                myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                mydatetime = myformatter.format(myDateTime);    
                outfRealFlag =1;
            }*/
			String outStr = "";
			outStr += hostnameStr + " ";
			outStr += mydatetime + " ";
			if(barcode_sgidStr.equals("")) {
				outStr += "NULL ";
			}
			else{
				outStr += barcode_sgidStr + " ";
			}
			outStr += barcode_opidStr + " ";	//20130909 ChiaHui update,new add OP id             
			outStr += inStr +"\n";

			FileWriter myFW;

			myFW = new FileWriter(dmdauto_ErrorMsg, true);
			myFW.write(outStr);
			myFW.close();

			tmpStr = "save Message To " + outfStrRealError + "\n";
			textArea2.append(tmpStr);
			//JOptionPane.showMessageDialog(null, tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
		}

	}
	static public void saveStartEndRealtime(String inStr) {

		String tmpStr;
		try {

			Date myDateTime = new Date();

			outfStrRealStSp = autoResultPath + "dmdauto_StartEndRealTime.txt";
			File dmdauto_StSpMsg = new File(outfStrRealStSp);                              //20100107

			myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
			mydatetime = myformatter.format(myDateTime);    
			/*if(outfRealFlag==0) {
                myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                mydatetime = myformatter.format(myDateTime);    
                outfRealFlag =1;
            }*/
			String outStr = "";
			outStr += hostnameStr + " ";
			outStr += mydatetime + " ";
			outStr += inStr +"\n";

			FileWriter myFW;

			myFW = new FileWriter(dmdauto_StSpMsg, true);
			myFW.write(outStr);
			myFW.close();

			tmpStr = "save Message To " + dmdauto_StSpMsg + "\n";
			textArea2.append(tmpStr);
			//JOptionPane.showMessageDialog(null, tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
		}

	}
	protected static void primaryHeader() {
		String tmpStr = null;

		tmpStr = "\n" + "    ========================================\n"
				+ "      Welcome to DiamondX Automation System  \n"
				+ "    ========================================\n"
				+ "\n";

		System.out.print(tmpStr); 

		tmpStr = "\n"
				+ "    ==========================\n"
				+ "      Welcome to DiamondX Automation System  \n"
				+ "    ==========================\n"
				+ "\n";
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

//		barcode_stationStr = "";	20171205

	}


	public void preFrameBarCode() {

		String tmpStr = null;
		int y = 0;

		Container c = frame.getContentPane();
		//c.setLayout(null);
		//c.setLayout(new BorderLayout());
		c.setLayout(new GridLayout(3,1));
		c.setBackground(Color.PINK);
		c.add(p1);
		c.add(p2);
		c.add(p3);

		// Build Panel 1
		p1.setLayout(new BorderLayout());
		p1sub1.setLayout(new GridLayout(4,1));
		p1sub2.setLayout(new GridLayout(1,5));

		p1sub1sub1.setLayout(new GridLayout(1,4));
		p1sub1sub2.setLayout(new GridLayout(1,2));
		p1sub1sub3.setLayout(new GridLayout(1,2));
		p1sub1sub4.setLayout(new GridLayout(1,3));//20081106

		p1sub1sub2sub.setLayout(new BorderLayout(1,3));//20081104
		p1sub1sub4sub1.setLayout(new GridLayout(1,6));//20081106
		p1sub1sub4sub2.setLayout(new GridLayout(1,2));//20081106
		p1sub1sub4sub2sub.setLayout(new GridLayout(1,2));//20110712
		p1sub1sub2subsub.setLayout(new GridLayout(1,6));

		p1.add(p1sub1, BorderLayout.CENTER);
		p1.add(p1sub2, BorderLayout.SOUTH);

		//  TestSite Location
		tmpStr = "TestSite Location:";
		TitledBorder tb0a = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0a.setTitleColor(new Color(248,252,88));
		SiteStr = SITENAME;
		tx0.setBorder(tb0a);
		tx0.setText(SiteStr);
		tx0.setFont(new Font("Courier New", Font.BOLD, 16));
		tx0.setForeground(Color.WHITE);
		//tx0.setBackground(new Color(250,160,80)); // as ligh orange
		//tx0.setBackground(new Color(250,148,255)); // as ligh purple
		//tx0.setBackground(new Color(0,128,250)); // as blue
		tx0.setBackground(new Color(48,172,238));// as blue
		tx0.setEditable(false);
		p1sub1sub1.add(tx0);

		// Production Station
		tmpStr = "Production Station:";
		TitledBorder tb4_1 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb4_1.setTitleColor(new Color(100,100,255));// as blue  //20081106
		//tb1e.setTitleColor(new Color(248,252,88));//light yellow
		tx5.setBorder(tb4_1);
		tx5.setText(barcode_stationStr);
		tx5.setFont(new Font("Courier New", Font.BOLD, 16));
		tx5.setForeground(Color.RED);
		tx5.setBackground(new Color(248,252,88));// as blue
		tx5.setEditable(false);
		p1sub1sub1.add(tx5);

		// SG Number
		tmpStr = "SG Number:";
		TitledBorder tb1a = new TitledBorder(new EtchedBorder(), tmpStr);
		tb1a.setTitleColor(new Color(248,252,88));//light yellow
		tx1.setBorder(tb1a);
		tx1.setText(SGStr);
		tx1.setFont(new Font("Courier New", Font.BOLD, 16));
		tx1.setForeground(Color.WHITE);
		tx1.setBackground(new Color(48,172,238));// as blue
		tx1.setEditable(false);
		p1sub1sub1.add(tx1);

		// LotNo
		tmpStr = "Lot Number:";
		TitledBorder tb1d = new TitledBorder(new EtchedBorder(), tmpStr);
		tb1d.setTitleColor(new Color(248,62,62));//as red
		tx3.setBorder(tb1d);
		tx3.setText(LotStr);
		tx3.setFont(new Font("Courier New", Font.BOLD, 16));
		tx3.setForeground(Color.WHITE);
		tx3.setBackground(new Color(48,172,238));// as blue
		tx3.setEditable(false);
		p1sub1sub1.add(tx3);

		// TestProgram
		tmpStr = "TestProgram:";
		TitledBorder tb1e = new TitledBorder(new EtchedBorder(), tmpStr);
		tb1e.setTitleColor(new Color(248,62,62));//as red
		tx4.setBorder(tb1e);
		tx4.setText(TPStr);
		tx4.setFont(new Font("Courier New", Font.BOLD, 16));
		tx4.setForeground(Color.WHITE);
		tx4.setBackground(new Color(48,172,238));// as blue
		tx4.setEditable(false);
		p1sub1sub2.add(tx4);

		// Customer:
		tmpStr = "Customer:";
		TitledBorder tb1c = new TitledBorder(new EtchedBorder(), tmpStr);
		tb1c.setTitleColor(new Color(248,252,88));//light yellow
		tx2.setBorder(tb1c);
		tx2.setText(barcode_customerStr);
		tx2.setFont(new Font("Courier New", Font.BOLD, 16));
		tx2.setForeground(Color.WHITE);
		tx2.setBackground(new Color(48,172,238));
		tx2.setEditable(false);
		p1sub1sub2sub.add(tx2, BorderLayout.CENTER);//20080724 20080826 20081021 20081104


		tmpStr = "RT Station";//20080724 //20080902 //20081021
		TitledBorder tb1c1 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb1c1.setTitleColor(new Color(100,100,255));// as blue
		p1sub1sub2subsub.setBorder(tb1c1);
		p1sub1sub2subsub.setBackground(new Color(248,252,88));// as yellow

		for (int i=0; i<rtrbtname.length; i++) { //20080724 add this  //20080902  //20081021
			if(i==0)
				rtrbt[i] = new JRadioButton(rtrbtname[i], true);     
			else 
				rtrbt[i] = new JRadioButton(rtrbtname[i], false);     
			rtrbt[i].setBackground(new Color(248,252,88));// as yellow
			rtrbt[i].setForeground(Color.RED);

			rtgrpbt.add(rtrbt[i]);
			rtrbt[i].addItemListener(this);
			p1sub1sub2subsub.add(rtrbt[i]);
		}

		p1sub1sub2sub.add(p1sub1sub2subsub,BorderLayout.EAST);  //20080724 20080826 20081021 20081104
		p1sub1sub2.add(p1sub1sub2sub);//20080724//20080902//20081021

		//  Kit No
		tmpStr = "Kit No:";
		TitledBorder tb0c = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0c.setTitleColor(new Color(248,252,88));
		kit_No_panel.setBorder(tb0c);
		kit_No_panel.setText(barcode_KIT2);
		kit_No_panel.setFont(new Font("Courier New", Font.BOLD, 16));
		kit_No_panel.setForeground(Color.WHITE);
		kit_No_panel.setBackground(new Color(48,172,238));// as blue
		kit_No_panel.setEditable(true);
		p1sub1sub4.add(kit_No_panel);

		//  Loadboard/PC No
		tmpStr = "Loadboard No:";
		TitledBorder tb0b = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0b.setTitleColor(new Color(248,252,88));
		tx0b.setBorder(tb0b);
		tx0b.setText(barcode_LB2);
		tx0b.setFont(new Font("Courier New", Font.BOLD, 16));
		tx0b.setForeground(Color.WHITE);
		tx0b.setBackground(new Color(48,172,238));// as blue
		tx0b.setEditable(true);
		p1sub1sub4.add(tx0b);

		// EQU check button
		bt_EQU.setText("CP/FT EQU check ok");//20110712
		bt_EQU.setToolTipText("please check kit,socket and LB/probecard barcode, if they are right , then press this button");
		bt_EQU.setFont(new Font("Courier New", Font.BOLD, 14));
		bt_EQU.setForeground(Color.WHITE);
		bt_EQU.setBackground(Color.red);
		bt_EQU.addActionListener(this);              
		p1sub1sub4.add(bt_EQU);

		//  Socket No1
		tmpStr = "site1:";
		TitledBorder tb0s1 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0s1.setTitleColor(new Color(248,252,88));
		socket_No_panel.setBorder(tb0s1);
		socket_No_panel.setText(SocketCount2[0]);
		socket_No_panel.setFont(new Font("Courier New", Font.BOLD, 12));
		socket_No_panel.setForeground(Color.WHITE);
		socket_No_panel.setBackground(new Color(48,172,238));// as blue
		socket_No_panel.setEditable(true);
		p1sub1sub3.add(socket_No_panel);				

		//  Socket No2
		tmpStr = "site2:";
		TitledBorder tb0s2 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0s2.setTitleColor(new Color(248,252,88));
		socket_No_pane2.setBorder(tb0s2);
		socket_No_pane2.setText(SocketCount2[0]);
		socket_No_pane2.setFont(new Font("Courier New", Font.BOLD, 12));
		socket_No_pane2.setForeground(Color.WHITE);
		socket_No_pane2.setBackground(new Color(48,172,238));// as blue
		socket_No_pane2.setEditable(true);
		p1sub1sub3.add(socket_No_pane2);	

		//  Socket No3
		tmpStr = "site3:";
		TitledBorder tb0s3 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0s3.setTitleColor(new Color(248,252,88));
		socket_No_pane3.setBorder(tb0s3);
		socket_No_pane3.setText(SocketCount2[0]);
		socket_No_pane3.setFont(new Font("Courier New", Font.BOLD, 12));
		socket_No_pane3.setForeground(Color.WHITE);
		socket_No_pane3.setBackground(new Color(48,172,238));// as blue
		socket_No_pane3.setEditable(true);
		p1sub1sub3.add(socket_No_pane3);	

		//  Socket No4
		tmpStr = "site4:";
		TitledBorder tb0s4 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0s4.setTitleColor(new Color(248,252,88));
		socket_No_pane4.setBorder(tb0s4);
		socket_No_pane4.setText(SocketCount2[0]);
		socket_No_pane4.setFont(new Font("Courier New", Font.BOLD, 12));
		socket_No_pane4.setForeground(Color.WHITE);
		socket_No_pane4.setBackground(new Color(48,172,238));// as blue
		socket_No_pane4.setEditable(true);
		p1sub1sub3.add(socket_No_pane4);	                

		//  Socket No5
		tmpStr = "site5:";
		TitledBorder tb0s5 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0s5.setTitleColor(new Color(248,252,88));
		socket_No_pane5.setBorder(tb0s5);
		socket_No_pane5.setText(SocketCount2[0]);
		socket_No_pane5.setFont(new Font("Courier New", Font.BOLD, 12));
		socket_No_pane5.setForeground(Color.WHITE);
		socket_No_pane5.setBackground(new Color(48,172,238));// as blue
		socket_No_pane5.setEditable(true);
		p1sub1sub3.add(socket_No_pane5);	

		//  Socket No6
		tmpStr = "site6:";
		TitledBorder tb0s6 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0s6.setTitleColor(new Color(248,252,88));
		socket_No_pane6.setBorder(tb0s6);
		socket_No_pane6.setText(SocketCount2[0]);
		socket_No_pane6.setFont(new Font("Courier New", Font.BOLD, 12));
		socket_No_pane6.setForeground(Color.WHITE);
		socket_No_pane6.setBackground(new Color(48,172,238));// as blue
		socket_No_pane6.setEditable(true);
		p1sub1sub3.add(socket_No_pane6);	

		//  Socket No7
		tmpStr = "site7:";
		TitledBorder tb0s7 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0s7.setTitleColor(new Color(248,252,88));
		socket_No_pane7.setBorder(tb0s7);
		socket_No_pane7.setText(SocketCount2[0]);
		socket_No_pane7.setFont(new Font("Courier New", Font.BOLD, 12));
		socket_No_pane7.setForeground(Color.WHITE);
		socket_No_pane7.setBackground(new Color(48,172,238));// as blue
		socket_No_pane7.setEditable(true);
		p1sub1sub3.add(socket_No_pane7);	

		//  Socket No8
		tmpStr = "site8:";
		TitledBorder tb0s8 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb0s8.setTitleColor(new Color(248,252,88));
		socket_No_pane8.setBorder(tb0s8);
		socket_No_pane8.setText(SocketCount2[0]);
		socket_No_pane8.setFont(new Font("Courier New", Font.BOLD, 12));
		socket_No_pane8.setForeground(Color.WHITE);
		socket_No_pane8.setBackground(new Color(48,172,238));// as blue
		socket_No_pane8.setEditable(true);
		p1sub1sub3.add(socket_No_pane8);

		bt_GPIB.setText("ENG");//20110712  20170323
		bt_GPIB.setToolTipText("launcher optool");
		bt_GPIB.setFont(new Font("Courier New", Font.BOLD, 18));
		bt_GPIB.setForeground(Color.WHITE);
		bt_GPIB.setBackground(Color.BLUE);
		bt_GPIB.setEnabled(false);	//20170323
		bt_GPIB.addActionListener(this);

		p1sub1sub4sub2.add(p1sub1sub4sub2sub);

		//p1sub1sub4sub2sub.add(bt_Clean);
		p1sub1sub4sub2sub.add(bt_GPIB);


		tmpStr = "Summary_Report_No:";
		TitledBorder tb4_3 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb4_3.setTitleColor(new Color(100,100,255));//as red
		tx4_1.setBorder(tb4_3);
		sumRepNum = 1;
		if(sumRepNum<10){
			tx4_1.setText("0" + Integer.toString(sumRepNum));
			sumRepNumStr = "0" + Integer.toString(sumRepNum);
		}
		else {
			tx4_1.setText(Integer.toString(sumRepNum));
			sumRepNumStr = Integer.toString(sumRepNum);
		}
		tx4_1.setText("");                //20110209
		tx4_1.setFont(new Font("Courier New", Font.BOLD, 16));
		tx4_1.setForeground(Color.BLACK);
		tx4_1.setBackground(new Color(251,249,209)); // Color(int r, int g, int b)
		tx4_1.setEnabled(false);//20090601
		//tx4_1.addKeyListener(this);
		//tex4_1.getDocument().addDocumentListener(this);
		//p1sub1sub4sub2sub1.add(tx4_1);
		//p1sub1sub4sub2.add(tx4_1,BorderLayout.CENTER);
		p1sub1sub4sub2.add(tx4_1);
		p1sub1sub4.add(p1sub1sub4sub2);
		////////////////////////////////////        
		p1sub1.add(p1sub1sub1);
		p1sub1.add(p1sub1sub2);
		p1sub1.add(p1sub1sub3);
		p1sub1.add(p1sub1sub4);

		// set button - "OI START" //// get Tester Barcode file  
		bt1.setText("OI START");
		TitledBorder tb5_0 = new TitledBorder(new EtchedBorder(), "");
		bt1.setBorder(tb5_0);
		bt1.setToolTipText("Get production information");
		bt1.setFont(new Font("Courier New", Font.BOLD, 14));
		bt1.setForeground(Color.BLACK);
		bt1.setBackground(Color.CYAN);
		bt1.addActionListener(this);
		p1sub2.add(bt1);

		bt2_1.setText("Change New Lot");
		TitledBorder tb5_1 = new TitledBorder(new EtchedBorder(), "");
		bt2_1.setBorder(tb5_1);
		bt2_1.setToolTipText("production start NewLot");
		bt2_1.setFont(new Font("Courier New", Font.BOLD, 14));
		bt2_1.setForeground(Color.BLUE);
		bt2_1.setBackground(Color.CYAN);
		bt2_1.setEnabled(false);
		bt2_1.addActionListener(this);
		p1sub2.add(bt2_1);

		bt2_2.setText("Start test");
		TitledBorder tb5_2 = new TitledBorder(new EtchedBorder(), "");
		bt2_2.setBorder(tb5_2);
		bt2_2.setToolTipText("Start test");
		bt2_2.setFont(new Font("Courier New", Font.BOLD, 14));
		bt2_2.setForeground(Color.BLUE);
		bt2_2.setBackground(Color.CYAN);
		bt2_2.setEnabled(false);
		bt2_2.addActionListener(this);
		p1sub2.add(bt2_2);

		//20080811 bt2_3.setText("autoEndLot");
		//20080811 TitledBorder tb5_3 = new TitledBorder(new EtchedBorder(), "");
		//20080811 bt2_3.setBorder(tb5_3);
		//20080811 bt2_3.setToolTipText("production EndLot");
		//20080811 bt2_3.setFont(new Font("Courier New", Font.BOLD, 14));
		//20080811 bt2_3.setForeground(Color.BLUE);
		//20080811 bt2_3.setBackground(Color.CYAN);
		//20080811 bt2_3.addActionListener(this);
		//20080811 p1sub2.add(bt2_3);

		bt2_4.setText("End test");
		TitledBorder tb5_4 = new TitledBorder(new EtchedBorder(), "");
		bt2_4.setBorder(tb5_4);
		bt2_4.setToolTipText("Stop dmd_exe");
		bt2_4.setFont(new Font("Courier New", Font.BOLD, 14));
		bt2_4.setForeground(Color.BLUE);
		bt2_4.setBackground(Color.CYAN);
		bt2_4.setEnabled(false);
		bt2_4.addActionListener(this);
		p1sub2.add(bt2_4);

		// Build Panel 2
		// Display Production Tester BarCode File
		//p2.setLayout(new BorderLayout());
		p2.setLayout(new GridLayout(1,2));

		// Production Tester BarCode File Information
		tmpStr = ">>> Production Tester BarCode File Information <<<";
		TitledBorder tb2_1 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb2_1.setTitleColor(new Color(248,252,88));
		textArea1.setBorder(tb2_1);
		textArea1.setFont(new Font("Courier New", Font.CENTER_BASELINE, 12));
		textArea1.setForeground(new Color(168,0,168));//purple
		textArea1.setBackground(new Color(250,198,228));//ligh pink
		//textArea1.setBackground(Color.PINK); // Color(int r, int g, int b)
		textArea1.setToolTipText("Production Tester BarCode File Information");
		textArea1.setText("Tester BarCode File Information");
		textArea1.setAutoscrolls(true);
		textArea1.setEditable(false);

		JScrollPane sp1 = new JScrollPane(textArea1);
		sp1.setHorizontalScrollBarPolicy(sp1.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp1.setVerticalScrollBarPolicy(sp1.VERTICAL_SCROLLBAR_ALWAYS);
		//p2.add(sp1, BorderLayout.CENTER);
		p2.add(sp1);


		// Recipe & Load File Information for MTK
		tmpStr = ">>> Production Recipe & Load File Information <<<";
		TitledBorder tb2_2 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb2_2.setTitleColor(new Color(248,252,88));
		textArea1_2.setBorder(tb2_2);
		textArea1_2.setFont(new Font("Courier New", Font.CENTER_BASELINE, 12));
		textArea1_2.setForeground(new Color(168,0,168));//purple
		textArea1_2.setBackground(new Color(250,198,228));//ligh pink
		textArea1_2.setToolTipText("Production Recipe & Load File Information");
		textArea1_2.setText("Recipe & Load File Information");
		textArea1_2.setAutoscrolls(true);
		textArea1_2.setEditable(false);

		JScrollPane sp1_2 = new JScrollPane(textArea1_2);
		sp1_2.setHorizontalScrollBarPolicy(sp1_2.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp1_2.setVerticalScrollBarPolicy(sp1_2.VERTICAL_SCROLLBAR_ALWAYS);
		p2.add(sp1_2);


		// Build Panel 3
		p3.setLayout(new BorderLayout());

		// Message Information
		tmpStr = ">>> Message Log <<<";
		////lb5.setText(tmpStr);
		////lb5.setFont(new Font("Arial Black", Font.CENTER_BASELINE, 12));
		////lb5.setForeground(new Color(248,252,88));
		////lb5.setBackground(Color.cyan);
		////p3.add(lb5, BorderLayout.NORTH);

		TitledBorder tb3 = new TitledBorder(new EtchedBorder(), tmpStr);
		tb3.setTitleColor(new Color(248,252,88));
		textArea2.setBorder(tb3);
		//textArea2.setFont(new Font("Courier New", Font.BOLD, 12));
		textArea2.setFont(new Font("Courier New", Font.CENTER_BASELINE, 12));
		textArea2.setForeground(Color.WHITE);
		textArea2.setBackground(new Color(100,100,255));// as blue
		textArea2.setToolTipText("Message Log");
		textArea2.setText("");
		textArea2.setAutoscrolls(true);
		textArea2.getDocument().addDocumentListener(this);
		textArea2.setEditable(false);

		JScrollPane sp2 = new JScrollPane(textArea2);
		sp2.setHorizontalScrollBarPolicy(sp2.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp2.setVerticalScrollBarPolicy(sp2.VERTICAL_SCROLLBAR_ALWAYS);
		p3.add(sp2, BorderLayout.CENTER);

		p3sub.setLayout(new GridLayout(1,4));
		p3.add(p3sub, BorderLayout.SOUTH);

		// set button - "ShowDataInfo"   //20091229
		btd.setText("Show HBsum");
		TitledBorder tbd = new TitledBorder(new EtchedBorder(), "");
		btd.setBorder(tbd);
		btd.setToolTipText("Show HBSum Infomation");
		btd.setFont(new Font("Courier New", Font.BOLD, 12));
		btd.setForeground(Color.BLACK);
		btd.setBackground(Color.CYAN);
		btd.addActionListener(this);//
		p3sub.add(btd);

		// set button - "SaveMessage"
		bt3.setText("SaveMessage");
		TitledBorder tb6 = new TitledBorder(new EtchedBorder(), "");
		bt3.setBorder(tb6);
		bt3.setToolTipText("Save Message Log");
		bt3.setFont(new Font("Courier New", Font.BOLD, 12));
		//bt3.setForeground(new Color(111,46,248));
		bt3.setForeground(Color.BLACK);
		//bt3.setBackground(new Color(205,205,205));
		bt3.setBackground(Color.CYAN);
		bt3.addActionListener(this);//
		p3sub.add(bt3);

		// set button - "OIC"
		btOIC.setText("ENG-OIC");
		TitledBorder tb7 = new TitledBorder(new EtchedBorder(), "");
		btOIC.setBorder(tb7);
		btOIC.setToolTipText("Launch OIC tool");
		btOIC.setFont(new Font("Courier New", Font.BOLD, 14));
		btOIC.setForeground(new Color(111,46,248));
		//btOIC.setBackground(new Color(205,205,205));
		btOIC.setBackground(Color.CYAN);
		btOIC.addActionListener(this);//
		//p3sub.add(btOIC);


		// set button - "OI Version"
		btVer.setText("OI Version");
		TitledBorder tb8 = new TitledBorder(new EtchedBorder(), "");
		btVer.setBorder(tb8);
		btVer.setToolTipText("Show OI Revision");
		btVer.setFont(new Font("Courier New", Font.BOLD, 14));
		btVer.setForeground(Color.BLACK);
		btVer.setBackground(Color.CYAN);
		btVer.addActionListener(this);//
		p3sub.add(btVer);


		// set button - "Exit"
		btExit.setText("Exit");
		TitledBorder tb9 = new TitledBorder(new EtchedBorder(), "");
		btExit.setBorder(tb9);
		btExit.setToolTipText("Exit Automation");
		btExit.setFont(new Font("Courier New", Font.BOLD, 14));
		btExit.setForeground(Color.RED);
		//btExit.setBackground(new Color(205,205,205));
		btExit.setBackground(Color.CYAN);
		btExit.addActionListener(this);//
		p3sub.add(btExit);


		addMouseMotionListener(this);
		//addWindowListener(this); // invalid

		//frame.setDefaultCloseOperation(EXIT_ON_CLOSE);// be replaced
		frame.setSize(950, 900);
		frame.setVisible(true);
		frame.setResizable(true);
		frame.addWindowListener(this);//MUST
		frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	//Unused method
	public boolean judgeParameter(String testStr, String fixStr) {

		if ( testStr.endsWith(fixStr)
				|| testStr.matches("\\s+"+fixStr)
				|| testStr.matches(fixStr+"\\s+")
				|| testStr.matches("\\s+"+fixStr+"\\s+")
				) {

			return true;
		}

		return false;
	}


	public static String checkifHeadEndWithSlash(String inStr) {

		String tmpStr = "";

		if (inStr.endsWith("/")==false) {
			tmpStr = inStr + "/";
		} else {
			tmpStr = inStr;
		}

		if (tmpStr.startsWith("./")==true) {
			tmpStr = tmpStr.substring(1);
		}

		//System.out.println("InStr: " + inStr + "\tOutStr = " + tmpStr);

		return tmpStr;

	}

	public static String stringRemoveSpaceHeadTail(String inStr) {

		// This method to get rid of the space at head and/or tail of the String
		String newStr = "";

		int i, length, headIdx, tailIdx;

		length = inStr.length();
		headIdx = 0;
		tailIdx = 0;

		// remove head space only
		for (i=0; i<length; i++) {
			if (inStr.charAt(i)!=' ') {
				headIdx = i;
				break;
			}
		}

		// remove tail space only
		for (i=0; i<length; i++) {
			if (inStr.charAt(length-i-1)!=' ') {
				tailIdx = length-i-1;
				break;
			}
		}

		newStr = inStr.substring(headIdx, tailIdx+1);
		return newStr;
	}


	public static String stringRemoveArrowHeadTail(String inStr) {

		// This method to get rid of the Arrow ( '<' and '>' ) at head and tail of the String
		String newStr = "";

		int i, length, headIdx, tailIdx;

		length = inStr.length();
		headIdx = 0;
		tailIdx = 0;

		// remove head '<' only
		for (i=0; i<length; i++) {
			if (inStr.charAt(i)!='<') {
				headIdx = i;
				break;
			}
		}

		// remove tail '>' only
		for (i=0; i<length; i++) {
			if (inStr.charAt(length-i-1)!='>') {
				tailIdx = length-i-1;
				break;
			}
		}

		newStr = inStr.substring(headIdx, tailIdx+1);
		return newStr;
	}


	public static String stringRemoveChar(String inStr, char specChar) {

		// This method to get rid of the specified Char specChar
		String newStr = "";

		int i, length;
		int m;

		length = inStr.length();

		char[] inchar_a = new char[length];
		char[] newchar_a = new char[length];

		inchar_a = inStr.toCharArray();

		m = 0;
		for (i=0; i<length; i++) {
			if (inStr.charAt(i)!=specChar) {
				newchar_a[m] = inStr.charAt(i);
				//System.out.println("newchar_a["+m+"] = " + newchar_a[m]);
				m++;
			}
		}

		if(m<length)   // -- hh , if no replacement, problem will happen !!
		{ newchar_a[m] = '\0'; }  // MUST be here

		newStr = newStr.copyValueOf(newchar_a, 0, m);

		//System.out.println("newStr = " + newStr + "...END...");

		return newStr;
	}


	public static String getXMLTagString(String inStr) {

		// This method to get the Tag string per XML line
		// Ex: <myTag>

		String newStr = "";

		int i, length, tagheadIdx, tagtailIdx;

		length = inStr.length();
		tagheadIdx = 0;
		tagtailIdx = 0;

		// search first '<'
		for (i=0; i<length; i++) {
			if (inStr.charAt(i)=='<' && inStr.charAt(i+1)=='/') {
				tagheadIdx = i+2;
				break;
			} else if (inStr.charAt(i)=='<') {
				tagheadIdx = i+1;
			}
		}

		// search first '>'
		for (i=tagheadIdx; i<length; i++) {
			if (inStr.charAt(i)=='>') {
				tagtailIdx = i-1;
				break;
			}
		}

		newStr = inStr.substring(tagheadIdx, tagtailIdx+1);
		newStr = stringRemoveSpaceHeadTail(newStr);

		//System.out.println(" XML_Tag = " + newStr + " [ inStr = " + inStr + " ]");
		return newStr;
	}


	public static String getXMLTagValueString(String inStr) {

		// This method to get the Tag Value string per XML line
		// Ex: <myTag>TagValue</myTag>

		String newStr = "";

		int i, length, tagVheadIdx, tagVtailIdx;

		length = inStr.length();
		tagVheadIdx = 0;
		tagVtailIdx = 0;

		// search first '>'
		for (i=0; i<length; i++) {
			if (inStr.charAt(i)=='>') {
				tagVheadIdx = i+1;
				//System.out.println("tagVheadIdx = " + tagVheadIdx);
				break;
			}
		}

		// search first '<' after '>'
		for (i=tagVheadIdx; i<length; i++) {
			if (inStr.charAt(i)=='<' && inStr.charAt(i+1)=='/') {
				tagVtailIdx = i-1;
				//System.out.println("tagVtailIdx = " + tagVtailIdx);
				break;
			}
		}

		//MUST be here (either way as the below)
		if (tagVheadIdx > tagVtailIdx) {
			newStr = "NA";
		} else {
			newStr = inStr.substring(tagVheadIdx, tagVtailIdx+1);
			newStr = stringRemoveSpaceHeadTail(newStr);
		}
		return newStr;
	}

	// xmlFile content:
	// --------------------------------------
	// <BARCODE_SYSTEM>
	//     <SITE_NAME>SIGURD</SITE_NAME>
	//     <TesterID>TSA12</TesterID>
	//     <RECIPEFILE_PATH></RECIPEFILE_PATH>
	//     <SUMMARY_PATH></SUMMARY_PATH>
	//     <Customer>L022</Customer>
	//     <PROGRAM>specMJ_CM_SS_L_X22_1.load</PROGRAM>
	//     <FT1_FTn>F/T/1</FT1_FTn>
	//     <ProberID>GPIB</ProberID>
	//     <LOT_NUMBER>TP41349</LOT_NUMBER>
	//     <OPERATOR_ID>943594</OPERATOR_ID>
	//     <TEMPERATURE>25</TEMPERATURE>
	//     <FLOW_NAME>BEGIN</FLOW_NAME>
	//     <DEVICE_TYPE>specMJ/CM-SS-L</DEVICE_TYPE>
	//     <SG_NUMBER>PL0220801240-02</SG_NUMBER>
	//     <LOADBOARDID>SL058</LOADBOARDID>
	// </BARCODE_SYSTEM>


	public static void processBarCodeXMLInfoContent(String inStr) {

		//initial
		int i = 0;
		String testStr = "";

		String tmpStr = "";

		inStr = stringRemoveSpaceHeadTail(inStr);
		tmpStr = getXMLTagString(inStr);

		if (tmpStr.equals("SITE_NAME")) {
			barcode_sitenameStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("TesterID")) {
			barcode_testeridStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("RECIPEFILE_PATH")) {
			barcode_recipepathStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("SUMMARY_PATH")) {
			barcode_summarypathStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("Customer")) {
			
			barcode_customerStr = getXMLTagValueString(inStr);
			userSummaryfinalPath = autoLocalResultPath;//20100107
			userSTDFfinalPath = autoLocalResultPath;//20100107
			userTXTfinalPath = autoLocalResultPath;//20100107
			
			File dir_local = new File(autoLocalResultPath);                              //20100107
			if ( !dir_local.exists() )                                                        //20100107
			{                                                                            //20100107
				dir_local.mkdirs();                                                         //20100107
			}
			//20171020
			if(barcode_customerStr.equals("L022")||barcode_customerStr.equals("L129")||barcode_customerStr.equals("L449")||barcode_customerStr.equals("F167")||barcode_customerStr.equals("L343")||barcode_customerStr.equals("L320")||barcode_customerStr.equals("F054")||barcode_customerStr.equals("L389"))
				MTK_series = true;
			else
				MTK_series = false;
		} else if (tmpStr.equals("PROGRAM")) {
			barcode_programStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("FT1_FTn")) {
			testStr = getXMLTagValueString(inStr);
			int size = testStr.length();
			barcode_stationStr = "";

			for (i=0; i<size; i++) {
				if (testStr.charAt(i)!='/' && testStr.charAt(i)!=' ') {
					barcode_stationStr += testStr.charAt(i); 
					//System.out.println("barcode_stationStr = " + barcode_stationStr);
				}
			}
		} else if (tmpStr.equals("ProberID")) {
			barcode_proberidStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("HandlerID")) {  //20150115 into STDF
			barcode_handleridStr = getXMLTagValueString(inStr); 
		} else if (tmpStr.equals("LOT_NUMBER")) {
			barcode_lotidStr = getXMLTagValueString(inStr);
			if(barcode_customerStr.equals("L010")) {
				barcode_lotidStr_sub = barcode_lotidStr.substring(0,8);//jj
			}
		} else if (tmpStr.equals("OPERATOR_ID")) {
			barcode_opidStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("TEMPERATURE")) {
			barcode_temperatureStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("FLOW_NAME")) {
			barcode_flowStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("DEVICE_TYPE")) {
			barcode_devicetypeStr = getXMLTagValueString(inStr);
			barcode_devicetypeStr__ = barcode_devicetypeStr.replace('/', '_');
			barcode_devicetypeStr__ = barcode_devicetypeStr__.replace('-', '_');
			userSummaryfinalPath_bak = "/dx_summary/FT/" + barcode_customerStr +"_summary/" + barcode_devicetypeStr__+ "/";	//20170619
			userSTDFserverPath = "/dx_summary/FT/" + barcode_customerStr + "_STDF/" + barcode_devicetypeStr__+ "/";	//20170619
			userTXTserverPath = "/dx_summary/FT/" + barcode_customerStr +"/" + barcode_devicetypeStr__+ "/";	//20170619
			if(barcode_customerStr.equals("L010")) {
				barcode_devicetypeStr_sub = barcode_devicetypeStr.substring(0,6);
			} else if(barcode_customerStr.equals("L121")) { //Add by Cola. 20160817
				barcode_devicetypeStr_sub = barcode_devicetypeStr.substring(0,7);
			}
		} else if (tmpStr.equals("SG_NUMBER")) {
			barcode_sgidStr = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("LBC1_DATA")) {
			barcode_LBC1Str = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("LBC2_DATA")) {
			barcode_LBC2Str = getXMLTagValueString(inStr);
		} else if (tmpStr.equals("LOADBOARDID")) {
			barcode_lbidStr = getXMLTagValueString(inStr);
			LB_PCflag = false; //20081104
		} else if (tmpStr.equals("PROBECARDID")) {
			barcode_lbidStr = getXMLTagValueString(inStr);  //20081104
			LB_PCflag = true;   //20081104
		} else if (tmpStr.equals("STAGE")) { //20110824
			barcode_stage = getXMLTagValueString(inStr);
			if((barcode_stage.length()>0)&&(!barcode_stage.equals("NA"))) {
				stageFlag = true;
			}
		} else if (tmpStr.equals("SoakTime")) {//20110614
			barcode_SoaktimeStr = getXMLTagValueString(inStr); 
		} else if (tmpStr.equals("PGMDC")) {//20130314 xml datecode
			barcode_datecode = getXMLTagValueString(inStr); 
		} else if (tmpStr.equals("Duts")) {//20130416 xml duts
			barcode_duts = getXMLTagValueString(inStr); 
			barcode_duts_int = Integer.parseInt(barcode_duts);
		} else if (tmpStr.equals("locationCode")) {//20130416 xml locationCode
			barcode_locationCode = getXMLTagValueString(inStr); 
		} else if (tmpStr.equals("ProbeDeviceName")) {//20130416 xml ProbeDeviceName
			barcode_ProbeDeviceName = getXMLTagValueString(inStr); 
		}else if (tmpStr.equals("Tar_File")) {//20150303 save L100 tar file name by ChiaHui
			barcode_L100_Tar_File = getXMLTagValueString(inStr); 
		}
		//20150817 Add Alarm Information for Handler Setup by Cola.-----Start
		else if (tmpStr.equals("RT_Bin")) {
			barcode_RTbin_Str = getXMLTagValueString(inStr); 
		}else if (tmpStr.equals("Site_Diff_Yield")) {
			barcode_SiteDiffYield_Str = getXMLTagValueString(inStr); 
		}else if (tmpStr.equals("Alarm_Yield")) {
			barcode_AlarmYield_Str = getXMLTagValueString(inStr); 
		}else if (tmpStr.equals("Alarm_Bin_Yield")) {
			barcode_AlarmBinYield_Str = getXMLTagValueString(inStr); 
		}else if (tmpStr.equals("Bin_Def")) {  //20151209
			barcode_BinDefine_Str = getXMLTagValueString(inStr); 
		}//20150817 Add Alarm Information for Handler Setup by Cola.-----End
		else if (tmpStr.equals("FlowNumber")) { //Add by Cola. 20160504
        	barcode_TestFlowNumber_Str = getXMLTagValueString(inStr);
        	if (barcode_TestFlowNumber_Str.substring(0, 6).equals(barcode_customerStr + "-S"))
        		STR_TestFlow_flag = true;
        	else
        		STR_TestFlow_flag = false;
        } else if (tmpStr.equals("ReferenceDoc")) { //Add by Cola. 20160624
        	barcode_ReferenceDoc_Str = getXMLTagValueString(inStr);
        	if (!barcode_ReferenceDoc_Str.equals("")){
        		if (barcode_ReferenceDoc_Str.substring(0, 1).equals("S"))
        			STR_TestFlow_flag = true;
        		else
        			STR_TestFlow_flag = false;
        	}
        } else if (tmpStr.equals("Former_LOTID")) { //Add by Cola. 20160630
        	barcode_FormerLOTID_Str = getXMLTagValueString(inStr); 
        } else if (tmpStr.equals("DESIGN_TYPE")) { //Add by Cola. 20160630
        	barcode_DESIGN_TYPE_Str = getXMLTagValueString(inStr); 
        } else if (tmpStr.equals("Customer_TO")) { //Add by Cola. 20160630
        	barcode_customerTO_Str = getXMLTagValueString(inStr); 
        } else if (tmpStr.equals("ORDERNO")) {
			barcode_orderNO_Str = getXMLTagValueString(inStr);  //20161103	
		} else if (tmpStr.equals("TangoSBC_Setting")) { //Add by Cola. 20161209
        	barcode_TangoSBCset_Str = getXMLTagValueString(inStr); 
        } else if (tmpStr.indexOf("TangoSBC_Rule") != -1) { //Add by Cola. 20161209	
        	barcode_TangoSBCrule_Str[TangoSBC_Rule_index] = getXMLTagValueString(inStr); 
        	TangoSBC_Rule_index ++;
        } else if (tmpStr.equals("OCR_ID")) { //Add by Cola. 20170220
        	barcode_OCR_ID_Str = getXMLTagValueString(inStr);
        } else if (tmpStr.equals("SPIL_LOTNO")) {	//20170710
        	barcode_SPIL_LOTNO_Str = getXMLTagValueString(inStr);
        } else if (tmpStr.equals("SPIL_C_Item")) {	//20170710
        	barcode_SPIL_C_Item_Str = getXMLTagValueString(inStr);
        } else if (tmpStr.equals("WAFER_ID")) {	//20170808
        	barcode_Wafer_Number_Str = getXMLTagValueString(inStr);
        	barcode_Wafer_Number = barcode_Wafer_Number_Str.split(",");
        }
		
	}

	public static void ProcessEQUsocketFile_CP() {//20130527
		if(EQU_CP_Barcode_AutoKeyin_flag)
		{
		//initial
		int i = 0;
		String tmpStr = "";
		String paramStr[] = new String[2];

		//initial
		paramStr[0] = "";
		paramStr[1] = "";

		BufferedReader EQUsocketData;
		try {

			javaExecSystemCmd("touch /tmp/socket.txt"); 
			javaExecSystemCmd("chmod 777 /tmp/socket.txt");            

			EQUsocketData = new BufferedReader(new FileReader("/tmp/" + "socket.txt")); //open file

			while ((tmpStr = EQUsocketData.readLine()) != null){ 

				tmpStr = stringRemoveSpaceHeadTail(tmpStr);

				paramStr = tmpStr.split("=");

				if (tmpStr.endsWith("=")==false) {
					paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
					paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
				} else {
					paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
				}
				if (paramStr[0].equals("SG_Barcode_KIT")) {
					barcode_KIT2 = paramStr[1];
				}else if (paramStr[0].equals("SG_Barcode_PC")) {
					barcode_LB2 = paramStr[1];
				} else if (paramStr[0].equals("site0")) {
					SocketCount2[0] = paramStr[1];
				} else if (paramStr[0].equals("site1")) {
					SocketCount2[1] = paramStr[1];
				} else if (paramStr[0].equals("site2")) {
					SocketCount2[2] = paramStr[1];
				} else if (paramStr[0].equals("site3")) {
					SocketCount2[3] = paramStr[1];
				} else if (paramStr[0].equals("site4")) {
					SocketCount2[4] = paramStr[1];
				} else if (paramStr[0].equals("site5")) {
					SocketCount2[5] = paramStr[1];
				} else if (paramStr[0].equals("site6")) {
					SocketCount2[6] = paramStr[1];
				} else if (paramStr[0].equals("site7")) {
					SocketCount2[7] = paramStr[1];
				}}
			kit_No_panel.setText(barcode_KIT2);	
			tx0b.setText(barcode_LB2);

				if (barcode_customerStr.equals("L010")) //L010 by ChiaHui 20140325
					tx0b.setText("");

			socket_No_panel.setText(SocketCount2[0]);
			socket_No_pane2.setText(SocketCount2[1]);
			socket_No_pane3.setText(SocketCount2[2]);
			socket_No_pane4.setText(SocketCount2[3]);
			socket_No_pane5.setText(SocketCount2[4]);
			socket_No_pane6.setText(SocketCount2[5]);
			socket_No_pane7.setText(SocketCount2[6]);
			socket_No_pane8.setText(SocketCount2[7]);

		} catch (FileNotFoundException event) {	            
			tmpStr  = "<Exception> getSocketInfo:/tmp/socket.txt" + " is NOT Found !\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);

			tmpStr  = "<Exception> getSocketInfo:/tmp/socket.txt" + " is NOT Found !\n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+--------------------+ \n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:CP socket.txt is not found");
			killproc(); System.exit(1); // to quit Java app for Linux

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> getSocketInfo: " + err + "\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);

			tmpStr = "<Exception> getSocketInfo: " + err + "\n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+--------------------+ \n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:CP socket.txt is not found");
			killproc(); System.exit(1); // to quit Java app for Linux
		}
		}
	}

	public static void ProcessEQUsocketFile(boolean keyinbarcode) {//20130527  Add keyinbarcode flag for end test recheck LB. 20160510
		if(EQU_FT_Barcode_AutoKeyin_flag && keyinbarcode)
		{
			//initial
			int i = 0;
			String tmpStr = "";
			String paramStr[] = new String[2];

			//initial
			paramStr[0] = "";
			paramStr[1] = "";

			BufferedReader EQUsocketData;
			try {

				javaExecSystemCmd("touch /tmp/socket.txt"); 
				javaExecSystemCmd("chmod 777 /tmp/socket.txt");            

				EQUsocketData = new BufferedReader(new FileReader("/tmp/" + "socket.txt")); //open file

				while ((tmpStr = EQUsocketData.readLine()) != null){ 

					tmpStr = stringRemoveSpaceHeadTail(tmpStr);

					paramStr = tmpStr.split("=");

					if (tmpStr.endsWith("=")==false) {
						paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
						paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
					} else {
						paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
					}
					if (paramStr[0].equals("SG_Barcode_KIT")) {
						barcode_KIT2 = paramStr[1];
					}else if (paramStr[0].equals("SG_Barcode_LB")) {
						barcode_LB2 = paramStr[1];
					} else if (paramStr[0].equals("site0")) {
						SocketCount2[0] = paramStr[1];
					} else if (paramStr[0].equals("site1")) {
						SocketCount2[1] = paramStr[1];
					} else if (paramStr[0].equals("site2")) {
						SocketCount2[2] = paramStr[1];
					} else if (paramStr[0].equals("site3")) {
						SocketCount2[3] = paramStr[1];
					} else if (paramStr[0].equals("site4")) {
						SocketCount2[4] = paramStr[1];
					} else if (paramStr[0].equals("site5")) {
						SocketCount2[5] = paramStr[1];
					} else if (paramStr[0].equals("site6")) {
						SocketCount2[6] = paramStr[1];
					} else if (paramStr[0].equals("site7")) {
						SocketCount2[7] = paramStr[1];
					}}
				kit_No_panel.setText(barcode_KIT2);
				tx0b.setText(barcode_LB2);
				socket_No_panel.setText(SocketCount2[0]);
				socket_No_pane2.setText(SocketCount2[1]);
				socket_No_pane3.setText(SocketCount2[2]);
				socket_No_pane4.setText(SocketCount2[3]);
				socket_No_pane5.setText(SocketCount2[4]);
				socket_No_pane6.setText(SocketCount2[5]);
				socket_No_pane7.setText(SocketCount2[6]);
				socket_No_pane8.setText(SocketCount2[7]);

			} catch (FileNotFoundException event) {	            
				tmpStr  = "<Exception> getSocketInfo:/tmp/socket.txt" + " is NOT Found !\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "<Exception> getSocketInfo:/tmp/socket.txt" + " is NOT Found !\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:/tmp/socket.txt file is not found");
				killproc(); System.exit(1); // to quit Java app for Linux

			} catch (java.io.IOException err) {
				tmpStr = "<Exception> getSocketInfo: " + err + "\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr = "<Exception> getSocketInfo: " + err + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:/tmp/socket.txt file is not found");
				killproc(); System.exit(1); // to quit Java app for Linux
			}
		}
		//=========================================================================20140320 Due to duts Number to exchanging socket column.
		if (Integer.parseInt(barcode_duts) < 8){
			socket_No_pane8.setText("");
			socket_No_pane8.setEditable(false);
			socket_No_pane8.setBackground(new Color(248,62,62));// as red
		}

		if (Integer.parseInt(barcode_duts) < 7){
			socket_No_pane7.setText("");
			socket_No_pane7.setEditable(false);
			socket_No_pane7.setBackground(new Color(248,62,62));// as red
		}

		if (Integer.parseInt(barcode_duts) < 6){
			socket_No_pane6.setText("");
			socket_No_pane6.setEditable(false);
			socket_No_pane6.setBackground(new Color(248,62,62));// as red
		}

		if (Integer.parseInt(barcode_duts) < 5){
			socket_No_pane5.setText("");
			socket_No_pane5.setEditable(false);
			socket_No_pane5.setBackground(new Color(248,62,62));// as red
		}

		if (Integer.parseInt(barcode_duts) < 4){
			socket_No_pane4.setText("");
			socket_No_pane4.setEditable(false);
			socket_No_pane4.setBackground(new Color(248,62,62));// as red
		}

		if (Integer.parseInt(barcode_duts) < 3){
			socket_No_pane3.setText("");
			socket_No_pane3.setEditable(false);
			socket_No_pane3.setBackground(new Color(248,62,62));// as red
		}

		if (Integer.parseInt(barcode_duts) < 2){
			socket_No_pane2.setText("");
			socket_No_pane2.setEditable(false);
			socket_No_pane2.setBackground(new Color(248,62,62));// as red
		}
		//========================================================================= 			
	} 

	public static void processgpibContent(String inStr) {//20110614
		//initial
		int i = 0;
		String tmpStr = "";
		String paramStr[] = new String[2];

		//initial
		paramStr[0] = "";
		paramStr[1] = "";

		inStr = stringRemoveSpaceHeadTail(inStr);

		paramStr = inStr.split("=");

		if (inStr.endsWith("=")==false) {
			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
			paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
		} else {

			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
		}
		if (paramStr[0].equals("FTType")) {
			gpibFTType = paramStr[1];
			//Add Handler Software Information by Cola. 20151020-----Start
			String[] gpibFTType_split;

			gpibFTType_split = gpibFTType.split("-"); 		

			if(gpibFTType_split[0].equals("NEWOI")){//Return Example: NEWOI-3.0.4-HT_       		
				HandlerSW_sendGPIB = gpibFTType_split[0];
				HandlerSW_Version = gpibFTType_split[1];  
				//    			gpibFTType = gpibFTType_split[2];  'Remark by Cola. 20160629
		} 

			//Add Handler Software Information by Cola. 20151020-----End
		} else if(paramStr[0].equals("FTTemp"))
		{
			gpibFTTemp = paramStr[1];
			gpibFTTemp = gpibFTTemp.substring(1,gpibFTTemp.length());

		} else if(paramStr[0].equals("FTAllTemp")) // Capture each Temperature add by Cola. 2015/05/28 
		{
			gpibFTAllTemp = paramStr[1].split("_");

		} else if(paramStr[0].equals("FTSoakTime"))
		{
			if(paramStr[1].equals("NONE"))
				gpibFTSoakTime = "NA";
			else
			{
				gpibFTSoakTime = paramStr[1];
				//gpibFTSoakTime = gpibFTSoakTime.substring(0,gpibFTTemp.length()-1);
			}
		} else if(paramStr[0].equals("FTSiteMap"))
		{
			gpibFTSiteMap = paramStr[1];
		} else if(paramStr[0].equals("FTAlarmSetup")) //Add by Cola for GPIB Bin Alarm Check. 2015/09/01
		{
			gpibFTAlarmSetup = paramStr[1];
		}
	}

	public static void processgpibProberContent(String inStr) {//20110614
		//initial
		int i = 0;
		String tmpStr = "";
		String paramStr[] = new String[2];

		//initial
		paramStr[0] = "";
		paramStr[1] = "";

		inStr = stringRemoveSpaceHeadTail(inStr);

		paramStr = inStr.split("=");

		if (inStr.endsWith("=")==false) {
			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
			paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
		} else {

			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
		}
		if (paramStr[0].equals("waferNumber")) { 
			gpibCPWaferNo = paramStr[1].substring(2,paramStr[1].length());					

			if(gpibCPWaferNo.equals("01"))	
				gpibCPWaferNo = "1"; 
			if(gpibCPWaferNo.equals("02"))
				gpibCPWaferNo = "2";
			if(gpibCPWaferNo.equals("03"))
				gpibCPWaferNo = "3";							
			if(gpibCPWaferNo.equals("04"))
				gpibCPWaferNo = "4";			
			if(gpibCPWaferNo.equals("05"))
				gpibCPWaferNo = "5";			
			if(gpibCPWaferNo.equals("06"))
				gpibCPWaferNo = "6";
			if(gpibCPWaferNo.equals("07"))
				gpibCPWaferNo = "7";			
			if(gpibCPWaferNo.equals("08"))
				gpibCPWaferNo = "8";			
			if(gpibCPWaferNo.equals("09"))
				gpibCPWaferNo = "9";							
		}       

		//        if (paramStr[0].equals("CPTemp")) {
		//            gpibCPTemp = paramStr[1];
		//        } 
		//        else if (paramStr[0].equals("CPLotNum")) {
		//            paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
		//            gpibCPLotNum = paramStr[1].substring(1,paramStr[1].length());

		//						if(RobotStr_set.equals("TEL_soft")){			//20130604 remove first, add later
		//								gpibCPLotNum = gpibCPLotNum.substring(2);
		//							}

		/*} else if (paramStr[0].equals("FTSoakTime")) {
            if(paramStr[1].equals("NONE"))
                gpibFTSoakTime = "NA";
            else{
                gpibFTSoakTime = paramStr[1];
                //gpibFTSoakTime = gpibFTSoakTime.substring(0,gpibFTTemp.length()-1);
            }
        } else if (paramStr[0].equals("FTSiteMap")) {
            gpibFTSiteMap = paramStr[1];
        }*/
	}
	public static boolean getBarCodeXMLInfo() {

		boolean fileFlag = true;
		boolean status = false;
		String tmpStr = "";
		String infileStr = "";
		barcode_TangoSBCrule_Str = new String[50];

		//testerBarcodePath = checkifHeadEndWithSlash(testerBarcodePath);
		infileStr = testerBarcodePath + barcodeFile;

		try {
			br = new BufferedReader(new FileReader(infileStr));//open file

			while ((tmpStr = br.readLine()) != null) {

				System.out.print(tmpStr + "\n"); 
				System.out.print(tmpStr + "\n"); 
				System.out.print(tmpStr + "\n"); 

				if (tmpStr.length()!=0) {
					processBarCodeXMLInfoContent(tmpStr);
				}
			}

			XMLCnt ++;
			br.close(); // close file
		} catch (FileNotFoundException event) {

			fileFlag = false;
			status = false;

			tmpStr  = "<Exception> getBarCodeXMLInfo: " + barcodeFile + " is NOT Found !\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);

			tmpStr  = "<Exception> getBarCodeXMLInfo: " + barcodeFile + " is NOT Found !\n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+--------------------+ \n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:Barcode file .Xml is not found");
			killproc(); System.exit(1); // to quit Java app for Linux

		} catch (java.io.IOException err) {
			fileFlag = false;
			status = false;
			tmpStr = "<Exception> getBarCodeXMLInfo: " + err + "\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);

			tmpStr = "<Exception> getBarCodeXMLInfo: " + err + "\n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+--------------------+ \n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:Barcode file .Xml is not found");
			killproc(); System.exit(1); // to quit Java app for Linux
		} catch (Exception  err) {	//20170505
			tmpStr = "Run Time Error: \n" + err + "\n";
			err.printStackTrace();
			JOptionPane.showMessageDialog(null, tmpStr);
		}
		
		if (fileFlag) {

			status = true;

			LotStr = barcode_lotidStr;
			LBStr  = barcode_lbidStr;
			SGStr  = barcode_sgidStr;

			tx1.setText(SGStr);
			tx2.setText(barcode_customerStr);
			tx3.setText(LotStr);
			tx5.setText(barcode_stationStr);

			// for System.out.println
			tmpStr  = "=============================================== \n";
			tmpStr += "Tester BarCode Information: " + barcodeFile +  "\n";
			tmpStr += "=============================================== \n";
			tmpStr += "      SITE_NAME = " + barcode_sitenameStr    + "\n";
			tmpStr += "       TesterID = " + barcode_testeridStr    + "\n";
			tmpStr += "RECIPEFILE_PATH = " + barcode_recipepathStr  + "\n";
			tmpStr += "   SUMMARY_PATH = " + barcode_summarypathStr + "\n";
			tmpStr += "       Customer = " + barcode_customerStr    + "\n";
			tmpStr += "        PROGRAM = " + barcode_programStr     + "\n";
			tmpStr += "        FT1_FTn = " + barcode_stationStr     + "\n";
			tmpStr += "       ProberID = " + barcode_proberidStr    + "\n";
			tmpStr += "      HandlerID = " + barcode_handleridStr   + "\n";
			tmpStr += "     LOT_NUMBER = " + barcode_lotidStr       + "\n";
			tmpStr += "    OPERATOR_ID = " + barcode_opidStr        + "\n";
			tmpStr += "    TEMPERATURE = " + barcode_temperatureStr + "\n";
			tmpStr += "    SoakingTime = " + barcode_SoaktimeStr + "\n";            //20110614
			tmpStr += "      FLOW_NAME = " + barcode_flowStr        + "\n";
			tmpStr += "    DEVICE_TYPE = " + barcode_devicetypeStr  + "\n";
			tmpStr += "  DEVICE_TYPE__ = " + barcode_devicetypeStr__+ "\n";
			tmpStr += "      SG_NUMBER = " + barcode_sgidStr        + "\n";
			tmpStr += "           Duts = " + barcode_duts           + "\n";
			tmpStr += "PGMDC(DateCode) = " + barcode_datecode       + "\n";
//			tmpStr += "       Tar_File = " + barcode_L100_Tar_File  + "\n";
			tmpStr += "         RT_Bin = " + barcode_RTbin_Str      + "\n";
			tmpStr += "Site_Diff_Yield = " + barcode_SiteDiffYield_Str + "\n";
			tmpStr += "    Alarm_Yield = " + barcode_AlarmYield_Str + "\n";
			tmpStr += "Alarm_Bin_Yield = " + barcode_AlarmBinYield_Str + "\n";
			tmpStr += "TestFlow_Number = " + barcode_TestFlowNumber_Str + "\n";
			tmpStr += "  Reference_Doc = " + barcode_ReferenceDoc_Str + "\n";
			if (!barcode_SPIL_C_Item_Str.equals(""))
				tmpStr += "    SPIL_C_Item = " + barcode_SPIL_C_Item_Str + "\n";
			//tmpStr += "      LBC1_DATA = " + barcode_LBC1Str        + "\n";
			//tmpStr += "      LBC2_DATA = " + barcode_LBC2Str        + "\n";

			if(LB_PCflag ==false){ //20081104
				tmpStr += "    LOADBOARDID = " + barcode_lbidStr        + "\n";
			}
			else{

				tmpStr += "    PROBECARDID = " + barcode_lbidStr        + "\n";
			}
			tmpStr += "=============================================== \n";
			System.out.println(tmpStr);


			// for Frame display
			tmpStr  = "======================================= \n";
			tmpStr += "Tester BarCode Information: " + barcodeFile +  "\n";
			tmpStr += "======================================= \n";
			tmpStr += "          SITE_NAME = " + barcode_sitenameStr    + "\n";
			tmpStr += "              TesterID = " + barcode_testeridStr    + "\n";
			tmpStr += "RECIPEFILE_PATH = " + barcode_recipepathStr  + "\n";
			tmpStr += "  SUMMARY_PATH = " + barcode_summarypathStr + "\n";
			tmpStr += "             Customer = " + barcode_customerStr    + "\n";
			tmpStr += "            PROGRAM = " + barcode_programStr     + "\n";
			tmpStr += "              FT1_FTn = " + barcode_stationStr     + "\n";
			tmpStr += "             ProberID = " + barcode_proberidStr    + "\n";
			tmpStr += "             HandlerID = " + barcode_handleridStr    + "\n";
			tmpStr += "      LOT_NUMBER = " + barcode_lotidStr       + "\n";
			tmpStr += "     OPERATOR_ID = " + barcode_opidStr        + "\n";
			tmpStr += "    TEMPERATURE = " + barcode_temperatureStr + "\n";
			tmpStr += "    SoakingTime = " + barcode_SoaktimeStr + "\n";            //20110614
			tmpStr += "        FLOW_NAME = " + barcode_flowStr        + "\n";
			tmpStr += "      DEVICE_TYPE = " + barcode_devicetypeStr  + "\n";
			tmpStr += "        SG_NUMBER = " + barcode_sgidStr        + "\n";
			tmpStr += "PGMDC(DateCode) = " + barcode_datecode       + "\n";            
			tmpStr += "           Duts = " + barcode_duts           + "\n";            
//			tmpStr += "       Tar_File = " + barcode_L100_Tar_File  + "\n"; 
			tmpStr += "         RT_Bin = " + barcode_RTbin_Str      + "\n";
			tmpStr += "Site_Diff_Yield = " + barcode_SiteDiffYield_Str + "\n";
			tmpStr += "    Alarm_Yield = " + barcode_AlarmYield_Str + "\n";
			tmpStr += "Alarm_Bin_Yield = " + barcode_AlarmBinYield_Str + "\n";
			tmpStr += "TestFlow_Number = " + barcode_TestFlowNumber_Str + "\n";
			tmpStr += "  Reference_Doc = " + barcode_ReferenceDoc_Str + "\n";
			if (!barcode_SPIL_C_Item_Str.equals(""))
				tmpStr += "    SPIL_C_Item = " + barcode_SPIL_C_Item_Str + "\n";
			//tmpStr += "        LBC1_DATA = " + barcode_LBC1Str        + "\n";
			//tmpStr += "        LBC2_DATA = " + barcode_LBC2Str        + "\n";

			if(LB_PCflag==false){   //20081104
				tmpStr += "    LOADBOARDID = " + barcode_lbidStr        + "\n";
			}
			else {
				tmpStr += "    PROBECARDID = " + barcode_lbidStr        + "\n";
			}
			tmpStr += "======================================= \n";
			textArea1.setText(tmpStr);
			textArea2.append(tmpStr);


			int exitflag = 0;
			// yes to return 0, no to return 1
			exitflag = JOptionPane.showConfirmDialog(null, tmpStr, "Confirmation ? ", JOptionPane.YES_NO_OPTION);
			if (exitflag==0) {
				for(int i = 0 ; i < TryRunTester.length ; i++){	//20170605
					if(barcode_testeridStr.equals(TryRunTester[i])){
						TryRunOI = true;
						textArea2.append("\n!!Try Run OI on this Tester !!\n\n");
					}
				}
			} else {

				tmpStr  = "  BarCode is NOT right ! \n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.print(tmpStr); 

				tmpStr  = "  BarCode is NOT right ! \n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				textArea2.append(tmpStr);

				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:BarCode is Not right");
				killproc(); System.exit(1); // to quit Java app for Linux
			}
		}

		return status;
	}


	//Example Recipe File:
	//---------------------------------
	//Job_Path = /usr/local/home/chingkhh/production/spec/prog
	//Summary_Path = /usr/local/home/chingkhh/autoResult/summary/
	//RobotType = Epson Series
	//DmdLoadFile_Path = /usr/local/home/chingkhh/production/loadfile/ 
	//DmdLoadFile_Name = specMJ_CM_SS_L_v153bld11.load
	//Test_Type = Final
	//

	//Corr
	public void CallCorrFrame() { //20081219
		CORR_flag = true;
		Container CORR_c = CORR_frame.getContentPane();
		CORR_c.setLayout(new GridLayout(3,1));
		CORR_c.setBackground(new Color(112,32,255));
		CORR_p1.setBackground(new Color(112,32,255));
		CORR_p2.setBackground(new Color(112,32,255));
		CORR_p3.setBackground(new Color(112,32,255));
		CORR_c.add(CORR_p1);
		CORR_c.add(CORR_p2);
		CORR_c.add(CORR_p3);
		CORR_frame_bt.setBackground(Color.yellow);
		CORR_frame_bt.addActionListener(this);
		CORR_p3.add(CORR_frame_bt);
		CORR_lab.setBackground(Color.white);
		CORR_lab.setForeground(Color.RED);
		CORR_lab.setFont(new Font("Courier New", Font.BOLD, 16));
		CORR_p1.add(CORR_lab);

//		if (barcode_customerStr.equals("L176") || barcode_customerStr.equals("L400")){		//20140429 L176_cus
//
//			CORR_L176_flag = true;
//			for (int i=0; i<L176_corr_str.length; i++) {
//
//				if (i==0) {
//					L176_corr_JRadioButtonList[i] = new JRadioButton(L176_corr_str[i], true);
//					L176_FT_corr_reason = L176_corr_str[i];//default
//				}else {
//					L176_corr_JRadioButtonList[i] = new JRadioButton(L176_corr_str[i], false);
//				}
//				L176_Corr_grpbt.add(L176_corr_JRadioButtonList[i]);
//				L176_corr_JRadioButtonList[i].addItemListener(this);
//				CORR_p3.add(L176_corr_JRadioButtonList[i]);
//			}
//		}

		CORR_frame.getContentPane().add(CORR_p3);
		CORR_frame.setVisible(true);    		       			

		for (int i=0; i<CORR_btname.length; i++) {
			if (i==0) {
				CORR_bt[i] = new JRadioButton(CORR_btname[i], true);
				CORRStr_set = CORR_btname[i];//default
			}else {
				CORR_bt[i] = new JRadioButton(CORR_btname[i], false);
			}
			CORR_bt[i].setBackground(new Color(255,242,89));
			CORR_bt[i].setForeground(Color.RED);
			CORR_grpbt.add(CORR_bt[i]);
			CORR_bt[i].addItemListener(this);
			CORR_p2.add(CORR_bt[i]);
		}

		if (barcode_customerStr.equals("L176") || barcode_customerStr.equals("L400")){	//20140429 L176_cus
			CORR_frame.setSize(500,700);
		}else{
			CORR_frame.setSize(400,150);
		}
		//CORR_frame.setLocation(300,250);
		CORR_frame.setLocationRelativeTo(c);
		CORR_frame.setVisible(true);
		CORR_frame.addWindowListener(this);
		CORR_frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		return;

	}
	//A Frame for Select Corr Bin by Cola. 20160407
	public void CallSelectCorrBinFrame() {

		Container CORRbin_c = CORRbin_frame.getContentPane();
		CORRbin_c.setLayout(new GridLayout(3,1));
		CORRbin_c.setBackground(new Color(112,32,255));
		CORRbin_p1.setBackground(new Color(112,32,255));
		CORRbin_p2.setBackground(new Color(112,32,255));
		CORRbin_p3.setBackground(new Color(112,32,255));
		CORRbin_c.add(CORRbin_p1);
		CORRbin_c.add(CORRbin_p2);
		CORRbin_c.add(CORRbin_p3);
		CORRbin_frame_bt.setBackground(Color.yellow);
		CORRbin_frame_bt.addActionListener(this);
		CORRbin_p3.add(CORRbin_frame_bt);
		CORRbin_lab.setBackground(Color.white);
		CORRbin_lab.setForeground(Color.RED);
		CORRbin_lab.setFont(new Font("Courier New", Font.BOLD, 16));
		CORRbin_p1.add(CORRbin_lab);



		CORRbin_frame.getContentPane().add(CORRbin_p3);
		CORRbin_frame.setVisible(true);    		       			

		for (int i=0; i<CORRbin_btname.length; i++) {

			CORRbin_bt[i] = new JRadioButton(CORRbin_btname[i], false);
			CORRbin_bt[i].setBackground(new Color(255,242,89));
			CORRbin_bt[i].setForeground(Color.RED);
			CORRbin_grpbt.add(CORRbin_bt[i]);
			CORRbin_bt[i].addItemListener(this);
			CORRbin_p2.add(CORRbin_bt[i]);
		}


		CORRbin_frame.setSize(500,150);
		CORRbin_frame.setLocationRelativeTo(c);
		CORRbin_frame.setVisible(true);
		CORRbin_frame.addWindowListener(this);
		CORRbin_frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		return;

	}
	public void DisablePassFailBin(){
		//    	barcode_BinDefine_Str = "1|P|0_2|F|0_3|F|0_4|F|0_5|F|0_6|F|0";
		if (!barcode_BinDefine_Str.equals("") || !barcode_BinDefine_Str.equals("NA")){
			String[] BinDefine_split = barcode_BinDefine_Str.split("_");
			String PassFail_Define = CORRStr_set.substring(0, 1); //for get corr selection is pass or fail
			for(int i=0 ; i<10; i++)
				CORRbin_bt[i].setEnabled(false);

			for(int j=0 ; j<BinDefine_split.length ; j++){ //Ex. 1|P|0
				String BinNo = BinDefine_split[j].substring(0, 1);
				String BinType = BinDefine_split[j].substring(2, 3);
				int index = Integer.parseInt(BinNo) - 1;

				if(BinType.equals(PassFail_Define)) 
					CORRbin_bt[index].setEnabled(true);
			}
		}
	}

	public void CallCheckCorrFrame() { //20150303 by ChiaHui

		checkCORR_flag = true;
		Container checkCorr_c = checkCorr_frame.getContentPane();
		checkCorr_c.setLayout(new GridLayout(4,1));
		checkCorr_c.setBackground(new Color(112,32,255));
		checkCorr_p1.setBackground(new Color(112,32,255));
		checkCorr_p2.setBackground(new Color(112,32,255));
		checkCorr_p3.setBackground(new Color(112,32,255));
		checkCorr_c.add(checkCorr_p1);
		checkCorr_c.add(checkCorr_p2);
		checkCorr_c.add(checkCorr_p3);
		//        checkCorr_frame_bt.setBackground(Color.yellow);
		//        checkCorr_frame_bt.addActionListener(this);
		//        checkCorr_p3.add(checkCorr_frame_bt);
		checkCorr_lab.setBackground(Color.white);
		checkCorr_lab.setForeground(Color.RED);
		checkCorr_lab.setFont(new Font("Courier New", Font.BOLD, 12));
		checkCorr_p1.add(checkCorr_lab);

		checkCorr_frame.getContentPane().add(checkCorr_p3);
		checkCorr_frame.setVisible(true);    		       			

		for (int i=0; i<checkCorr_btname.length; i++) {
			if (i==0) {
				checkCorr_bt[i] = new JRadioButton(checkCorr_btname[i], true);
				checkCorrStr_set = checkCorr_btname[i];//default 
			}else {
				checkCorr_bt[i] = new JRadioButton(checkCorr_btname[i], false);
			}
			checkCorr_bt[i].setBackground(new Color(255,242,89));
			checkCorr_bt[i].setForeground(Color.RED);
			checkCorr_grpbt.add(checkCorr_bt[i]);
			checkCorr_bt[i].addItemListener(this);
			checkCorr_p2.add(checkCorr_bt[i]);
		}

		checkCorr_frame.setSize(400,150);
		checkCorr_frame.setLocationRelativeTo(c);
		checkCorr_frame.setVisible(true);
		checkCorr_frame.addWindowListener(this);
		checkCorr_frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		return;

	}    
	
	//Eqc
	public void CallEqcFrame() { //20081219//20100120
		EQC_flag = true;
		Container EQC_c = EQC_frame.getContentPane();
		EQC_c.setLayout(new GridLayout(8,1));
		EQC_c.setBackground(new Color(112,32,255));
		EQC_p1.setBackground(new Color(112,32,255));
		EQC_p2.setBackground(new Color(112,32,255));
		EQC_p3.setBackground(new Color(112,32,255));
		EQC_p4.setBackground(new Color(112,32,255));
		EQC_p5.setBackground(new Color(112,32,255));
		EQC_p6.setBackground(new Color(112,32,255));
		EQC_p7.setBackground(new Color(112,32,255));
		EQC_p8.setBackground(new Color(112,32,255));
		EQC_c.add(EQC_p1);
		EQC_c.add(EQC_p2);
		EQC_c.add(EQC_p3);
		EQC_c.add(EQC_p4);
		EQC_c.add(EQC_p5);
		EQC_c.add(EQC_p6);
		EQC_c.add(EQC_p7);
		EQC_c.add(EQC_p8);
		EQC_frame_bt.setBackground(Color.yellow);
		EQC_frame_bt.addActionListener(this);
		EQC_frame_bt2.setBackground(Color.PINK);
		EQC_frame_bt2.addActionListener(this);
		EQC_p8.add(EQC_frame_bt);
		EQC_lab.setBackground(Color.white);
		EQC_lab.setForeground(Color.BLUE);
		EQC_lab.setFont(new Font("Courier New", Font.BOLD, 20));
		EQC_p1.add(EQC_lab);
		Qnum_lab.setBackground(Color.white);
		Qnum_lab.setForeground(Color.BLUE);
		Qnum_lab.setFont(new Font("Courier New", Font.BOLD, 20));
		EQC_p4.add(Qnum_lab);
		QRT_lab.setBackground(Color.white);
		QRT_lab.setForeground(Color.BLUE);
		QRT_lab.setFont(new Font("Courier New", Font.BOLD, 20));
		EQC_p6.add(QRT_lab);

		for (int i=0; i<EQC_sub_btname.length; i++) {
			if (i==0) {
				EQC_sub_bt[i] = new JRadioButton(EQC_sub_btname[i], true);
				EQCStr_bin_set = EQC_sub_btname[i];//default
				//EQC_qbin_Str = "B1";
			}else {
				EQC_sub_bt[i] = new JRadioButton(EQC_sub_btname[i], false);
			}
			EQC_sub_bt[i].setBackground(new Color(233,213,152));
			EQC_sub_bt[i].setForeground(Color.RED);
			EQC_sub_grpbt.add(EQC_sub_bt[i]);
			EQC_sub_bt[i].addItemListener(this);
			EQC_p2.add(EQC_sub_bt[i]);
		}


		for (int i=0; i<EQC_btname.length; i++) {
			if (i==0) {
				EQC_bt[i] = new JRadioButton(EQC_btname[i], true);
				EQCStr_qbin_set = EQC_btname[i];//default
				EQC_qbin_Str = "B1";
			}else {
				EQC_bt[i] = new JRadioButton(EQC_btname[i], false);
			}
			EQC_bt[i].setBackground(new Color(248,252,0));
			EQC_bt[i].setForeground(Color.RED);
			EQC_grpbt.add(EQC_bt[i]);
			EQC_bt[i].addItemListener(this);
			EQC_p3.add(EQC_bt[i]);
		}
		for (int i=0; i<QNum_btname.length; i++) {
			if (i==0) {
				QNum_bt[i] = new JRadioButton(QNum_btname[i], true);
				EQC_QNum_Str = QNum_btname[i];//naming rule
			}else {
				QNum_bt[i] = new JRadioButton(QNum_btname[i], false);
			}
			QNum_bt[i].setBackground(new Color(248,252,0));
			QNum_bt[i].setForeground(Color.RED);
			QNum_grpbt.add(QNum_bt[i]);
			QNum_bt[i].addItemListener(this);
			EQC_p5.add(QNum_bt[i]);
		}
		for (int i=0;i<QRTBinname.length;i++) { 

			QRTBinbox[i] = new JCheckBox(QRTBinname[i]);
			QRTBinbox[i].setBackground(new Color(248,252,88));// as yellow
			QRTBinbox[i].setForeground(Color.RED);        
			QRTBinbox[i].addItemListener(this);
			EQC_p7.add(QRTBinbox[i]);
		}
		EQC_p7.add(EQC_frame_bt2);
		EQC_frame.setSize(800,300);
		EQC_frame.setLocation(250,250);
		EQC_frame.setLocationRelativeTo(c);
		//EQCframe.setLocationRelativeTo(c);
		EQC_frame.setVisible(true);
		EQC_frame.addWindowListener(this);
		EQC_frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		return;

	}
	//20170619-----Start
	public void Enable_XML_RTbin(){  //Enable using RT Bin by Cola.20151215
		for(int i=0;i<rtBinname.length;i++){ //Initial
			rtBinbox[i].setEnabled(false);
			rtBinbox[i].setSelected(false);
		}
//		barcode_RTbin_Str = "2,3,5";
		if(!barcode_RTbin_Str.equals("") && !barcode_RTbin_Str.equals("NA")){
			String Barcode_rtBin[] = barcode_RTbin_Str.split(","); 
			for(int i=0;i<rtBinname.length;i++)
				for(int j=0; j<Barcode_rtBin.length; j++)
					if(rtBinname[i].equals(Barcode_rtBin[j]))
						rtBinbox[i].setEnabled(true); 
		}
		else{ //Run Card don't set RT Bin    	
			for (int i=0;i<rtBinname.length;i++)
				rtBinbox[i].setEnabled(true);  
		}
		rtBinbox[0].setVisible(false);
	}
	public void Enable_XML_ANFRTbin(){  //Enable using ANF RT Bin by Cola. 20161026
		for(int i=0;i<rtBinname.length;i++){ //Initial
			ANFrtBinbox[i].setEnabled(false);
			ANFrtBinbox[i].setSelected(false);
		}
		if(!barcode_RTbin_Str.equals("") && !barcode_RTbin_Str.equals("NA")){
			String Barcode_rtBin[] = barcode_RTbin_Str.split(","); 
			for(int i=0;i<rtBinname.length;i++)
				for(int j=0; j<Barcode_rtBin.length; j++)
					if(rtBinname[i].equals(Barcode_rtBin[j]))
						ANFrtBinbox[i].setEnabled(true); 
		}
		else{ //Run Card don't set RT Bin    	
			for (int i=0;i<rtBinname.length;i++)
				ANFrtBinbox[i].setEnabled(true);  
		}
		if(RTStr == "EQC") ANFrtBinbox[0].setVisible(true);
		else ANFrtBinbox[0].setVisible(false);
	}
	//RT frame //20100428	//20170619----End
	public void CallRTFrame() { 
		RT_flag = true;
		Container RT_c = RT_frame.getContentPane();
		RT_c.setLayout(new GridLayout(4,1));
		RT_c.setBackground(new Color(112,32,255));
		RT_p1.setBackground(new Color(112,32,255));
		RT_p2.setBackground(new Color(112,32,255));
		RT_p3.setBackground(new Color(112,32,255));
		RT_p4.setBackground(new Color(112,32,255));
		RT_c.add(RT_p1);
		RT_c.add(RT_p2);
		RT_c.add(RT_p3);
		RT_c.add(RT_p4);
		RT_frame_bt.setBackground(Color.yellow);
		RT_frame_bt.addActionListener(this);
		RT_frame_bt2.setBackground(Color.PINK);
		RT_frame_bt2.addActionListener(this);
		RT_p4.add(RT_frame_bt);
		RT_lab.setBackground(Color.white);
		RT_lab.setForeground(Color.RED);
		RT_lab.setFont(new Font("Courier New", Font.BOLD, 16));
		RT_p1.add(RT_lab);

		for (int i=0; i<RT_btname.length; i++) {
			if (i==0) {
				RT_bt[i] = new JRadioButton(RT_btname[i], true);
				RTNumStr = RT_btname[i];//default
				//RT_qbin_Str = "B1";
			}else {
				RT_bt[i] = new JRadioButton(RT_btname[i], false);
			}
			RT_bt[i].setBackground(new Color(233,213,152));
			RT_bt[i].setForeground(Color.RED);
			RT_grpbt.add(RT_bt[i]);
			RT_bt[i].addItemListener(this);
			RT_p2.add(RT_bt[i]);
		}

//		if (testTypeStr.equalsIgnoreCase("Final")) {
			for (int i=0;i<rtBinname.length;i++) { 

				rtBinbox[i] = new JCheckBox(rtBinname[i]);
				rtBinbox[i].setBackground(new Color(248,252,88));// as yellow
				rtBinbox[i].setForeground(Color.RED);        
				rtBinbox[i].addItemListener(this);
				RT_p3.add(rtBinbox[i]);
			}
//		}
		RT_p3.add(RT_frame_bt2);
		RT_frame.setSize(550,325);
		RT_frame.setLocation(250,250);
		RT_frame.setLocationRelativeTo(c);
		//RTframe.setLocationRelativeTo(c);
		RT_frame.setVisible(true);
		RT_frame.addWindowListener(this);
		RT_frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		return;

	}
	//ANF Input RT frame //20161026	20170619-----Start
	public void CallANFRTFrame() { 
		ANFRT_flag = true;
		Container ANFRT_c = ANFRT_frame.getContentPane();
		ANFRT_c.setLayout(new GridLayout(4,1));
		ANFRT_c.setBackground(new Color(112,32,255));
		ANFRT_p1.setBackground(new Color(112,32,255));
		ANFRT_p2.setBackground(new Color(112,32,255));
		ANFRT_p3.setBackground(new Color(112,32,255));
		ANFRT_p4.setBackground(new Color(112,32,255));
		ANFRT_c.add(ANFRT_p1);	
		ANFRT_c.add(ANFRT_p3);
		ANFRT_c.add(ANFRT_p2);
		ANFRT_c.add(ANFRT_p4);
		ANFRT_frame_bt.setBackground(Color.yellow);
		ANFRT_frame_bt.addActionListener(this);
		ANFRT_p4.add(ANFRT_frame_bt);
		ANFRT_lab.setBackground(Color.white);
		ANFRT_lab.setForeground(Color.RED);
		ANFRT_lab.setFont(new Font("Courier New", Font.BOLD, 16));
		ANFRT_p1.add(ANFRT_lab);

		for (int i=0;i<rtBinname.length;i++) {    		  
			ANFrtBinbox[i] = new JCheckBox(rtBinname[i]);
			ANFrtBinbox[i].setBackground(new Color(248,252,88));// as yellow
			ANFrtBinbox[i].setForeground(Color.RED);
			ANFrtBinbox[i].addItemListener(this);
			ANFRT_p3.add(ANFrtBinbox[i]);
		}      

		ANFRT_frame.setSize(500,200);
		ANFRT_frame.setLocation(250,250);
		ANFRT_frame.setLocationRelativeTo(c);
		//RTframe.setLocationRelativeTo(c);
		ANFRT_frame.setVisible(true);
		ANFRT_frame.addWindowListener(this);
		ANFRT_frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		return;
	} //20170619-----End
	//Robot
	public void CallRobotFrame()//20081021
	{   
		mutiRobot_flag = true;
		Container c1 = robotframe.getContentPane();
		c1.setLayout(new GridLayout(3,1));
		c1.setBackground(new Color(112,32,255));
		robot_p1.setBackground(new Color(112,32,255));
		robot_p2.setBackground(new Color(112,32,255));
		robot_p3.setBackground(new Color(112,32,255));
		c1.add(robot_p1);
		c1.add(robot_p2);
		c1.add(robot_p3);
		robot_frame_bt.setBackground(Color.yellow);
		robot_frame_bt.addActionListener(this);
		robot_p3.add(robot_frame_bt);
		robot_lab.setBackground(Color.white);
		robot_lab.setForeground(Color.RED);
		robot_lab.setFont(new Font("Courier New", Font.BOLD, 16));
		robot_p1.add(robot_lab);

		for (int i=0; i<robot_btname.length; i++) {
			if (i==0) {
				robot_bt[i] = new JRadioButton(robot_btname[i], true);
				RobotStr_set = robot_btname[i];
			}else {
				robot_bt[i] = new JRadioButton(robot_btname[i], false);
			}
			robot_bt[i].setBackground(new Color(255,242,89));
			robot_bt[i].setForeground(Color.RED);
			robot_grpbt.add(robot_bt[i]);
			robot_bt[i].addItemListener(this);
			robot_p2.add(robot_bt[i]);
		}

		robotframe.setSize(400,150);
		robotframe.setLocation(300,250);
		//robotframe.setLocationRelativeTo(c);
		robotframe.setVisible(true);
		robotframe.addWindowListener(this);
		robotframe.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


		return;
	}

	public void Call_FT_GPIB_warnning(){	//20170619

		Container FT_GPIB_warnning_container = FT_GPIB_warnning_frame.getContentPane();//20140120
		FT_GPIB_warnning_container.setLayout(new GridLayout(1,1));
		FT_GPIB_warnning_container.setBackground(Color.WHITE);

		FT_GPIB_warnning_p1.setBackground(Color.WHITE);

		FT_GPIB_warnning_container.add(FT_GPIB_warnning_p1);

		ImageIcon img = null;
		img = new ImageIcon("/dx_profile/prod/csicDXAuto_Sigurd_CUS_try/bin/FT_GPIB_check_warnning_pic.jpg");
		FT_GPIB_warnning_lab1 = new JLabel(img);
		FT_GPIB_warnning_lab1.setBackground(Color.BLUE);
		FT_GPIB_warnning_lab1.setForeground(Color.YELLOW);
		FT_GPIB_warnning_lab1.setFont(new Font("Courier New", Font.BOLD, 10));

		FT_GPIB_warnning_p1.add(FT_GPIB_warnning_lab1); 

		FT_GPIB_warnning_frame.setSize(500,420);
		FT_GPIB_warnning_frame.setLocation(0,0);
		FT_GPIB_warnning_frame.setResizable(false);
		//        FT_GPIB_warnning_frame.setAlwaysOnTop(true);
		FT_GPIB_warnning_frame.setVisible(true);
		FT_GPIB_warnning_frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		FT_GPIB_warnning_frame.addWindowListener(this); 		

		return;	
	}
	
	//EFuse for MTK
	public void CallFailEfuseFrame()//20100722
	{   
		Container EfuseCheck_error_c  = EfuseCheck_error_frame.getContentPane();
		EfuseCheck_error_c.setLayout(new GridLayout(2,1));

		EfuseCheck_error_c.setBackground(Color.RED);
		EfuseCheck_error_p1.setBackground(Color.RED);
		EfuseCheck_error_p2.setBackground(Color.RED);
		EfuseCheck_error_c.add(EfuseCheck_error_p1);
		EfuseCheck_error_c.add(EfuseCheck_error_p2);
		EfuseCheck_error1_lab.setBackground(Color.RED);
		EfuseCheck_error1_lab.setForeground(Color.YELLOW);
		EfuseCheck_error1_lab.setFont(new Font("Courier New", Font.BOLD, 20));
		EfuseCheck_error2_JText.setBackground(Color.RED);
		EfuseCheck_error2_JText.setForeground(Color.YELLOW);
		EfuseCheck_error2_JText.setFont(new Font("Courier New", Font.BOLD, 16));
		EfuseCheck_error_p1.add(EfuseCheck_error1_lab);
		EfuseCheck_error_p2.add(EfuseCheck_error2_JText);        

		EfuseCheck_error_frame.setSize(600,200);
		EfuseCheck_error_frame.setLocation(300,250);
		EfuseCheck_error_frame.setVisible(true);
		EfuseCheck_error_frame.addWindowListener(this);
		//EfuseCheck_error_frame.setDefaultCloseOperation();

		return;
	}

	public void CallPassEfuseFrame()//20100722
	{   
		Container EfuseCheck_pass_c  = EfuseCheck_pass_frame.getContentPane();
		EfuseCheck_pass_c.setLayout(new GridLayout(2,1));

		EfuseCheck_pass_c.setBackground(Color.WHITE);
		EfuseCheck_pass_p1.setBackground(Color.WHITE);
		EfuseCheck_pass_p2.setBackground(Color.WHITE);
		EfuseCheck_pass_c.add(EfuseCheck_pass_p1);
		EfuseCheck_pass_c.add(EfuseCheck_pass_p2);
		EfuseCheck_pass1_lab.setBackground(Color.WHITE);
		EfuseCheck_pass1_lab.setForeground(Color.GREEN);
		EfuseCheck_pass1_lab.setFont(new Font("Courier New", Font.BOLD, 20));
		EfuseCheck_pass2_lab.setBackground(Color.WHITE);
		EfuseCheck_pass2_lab.setForeground(Color.GREEN);
		EfuseCheck_pass2_lab.setFont(new Font("Courier New", Font.BOLD, 16));
		EfuseCheck_pass_p1.add(EfuseCheck_pass1_lab);
		EfuseCheck_pass_p2.add(EfuseCheck_pass2_lab);        
		EfuseCheck_pass_p3.add(EfuseCheck_pass_frame_bt);        

		EfuseCheck_pass_frame.setSize(400,150);
		EfuseCheck_pass_frame.setLocation(300,250);
		EfuseCheck_pass_frame.setVisible(true);
		EfuseCheck_pass_frame.addWindowListener(this);
		//EfuseCheck_pass_frame.setDefaultCloseOperation();

		return;
	}

	public void CallDataFrame()//20091229
	{   

		String tmpStr="";
		Container d1 = dataframe.getContentPane();

		d1.setLayout(new BorderLayout());
		d1.setBackground(new Color(112,32,255));
		d1.add(p_data1,BorderLayout.NORTH);
		d1.add(p_data2,BorderLayout.CENTER);
		d1.add(p_data3,BorderLayout.SOUTH);
		p_data2.setLayout(new BorderLayout(1,1));
		p_data1.setLayout(new GridLayout(1,2));
		d_show.setText("Update");
		TitledBorder d_tb1 = new TitledBorder(new EtchedBorder(), "");
		d_show.setBorder(d_tb1);
		d_show.setToolTipText("show data information");
		d_show.setFont(new Font("Courier New", Font.BOLD, 14));
		d_show.setForeground(new Color(243,237,71));
		d_show.setBackground(new Color(203,28,97));
		d_show.addActionListener(this);
		p_data1.add(d_show);
		d_print.setText("Print");
		TitledBorder d_tb2 = new TitledBorder(new EtchedBorder(), "");
		d_print.setBorder(d_tb2);
		d_print.setToolTipText("print data information");
		d_print.setFont(new Font("Courier New", Font.BOLD, 14));
		d_print.setForeground(new Color(243,237,71));
		d_print.setBackground(new Color(203,28,97));
		d_print.addActionListener(this);
		p_data1.add(d_print);

		tmpStr = ">>> Data Information <<<";
		TitledBorder tb_d = new TitledBorder(new EtchedBorder(), tmpStr);
		tb_d.setTitleColor(new Color(248,252,88));
		textAread.setBorder(tb_d);
		textAread.setFont(new Font("Courier", Font.CENTER_BASELINE, 12));//Rex
		textAread.setForeground(new Color(232,231,153));//purple
		textAread.setBackground(new Color(10,103,126));//ligh pink
		textAread.setToolTipText("Data Information");
		textAread.setText("Data Information");
		textAread.setAutoscrolls(true);
		textAread.setEditable(false);

		sp_d = new JScrollPane(textAread);
		sp_d.setHorizontalScrollBarPolicy(sp_d.HORIZONTAL_SCROLLBAR_ALWAYS);
		sp_d.setVerticalScrollBarPolicy(sp_d.VERTICAL_SCROLLBAR_ALWAYS);
		p_data2.add(sp_d);

		data_frame_bt.setBackground(Color.yellow);
		data_frame_bt.addActionListener(this);
		p_data3.add(data_frame_bt);

		dataframe.setSize(500,500);
		dataframe.setLocation(300,150);
		dataframe.setVisible(true);
		dataframe.addWindowListener(this);
		dataframe.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		return;
	}


	public static void processRecipeFileInfoContent(String inStr) {

		String tmpStr = "";
		String paramStr[] = new String[2];
		boolean twostrFlag = true;
		String fileprefix[] = new String[1];
		fileprefix[0] = "";

		//initial
		paramStr[0] = "";
		paramStr[1] = "";

		inStr = stringRemoveSpaceHeadTail(inStr);

		//System.out.println("case0: inStr = " + inStr);

		paramStr = inStr.split("=");

		if (inStr.endsWith("=")==false) {

			System.out.println("debug--->" + paramStr[0]);
			System.out.println("debug--->" + paramStr[1]);
			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
			paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
			twostrFlag = true;

		} else {

			//System.out.println("processSystemInfoContent: paramStr[0] = " + paramStr[0]);
			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
			//System.out.println("processSystemInfoContent: paramStr[0] = " + paramStr[0] + " (***NEW***)");
			twostrFlag = false;
		}


		if (twostrFlag) {
			if (paramStr[0].equals("Job_Path")) {
				JobPathStr = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("Summary_Path")) {
				userSummaryPath = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("RobotType")) {   //20081021
				RobotStr  = paramStr[1];                    //20081021
				RobotStr_set = RobotStr;                    //20081021
				robot_type_num++;                           //20081021
			} else if (paramStr[0].equals("RobotType2")) {  //20081021  
				RobotStr2 = paramStr[1];                    //20081021
				robot_type_num++;                           //20081021
			} else if (paramStr[0].equals("RobotType3")) {  //20081021
				RobotStr3 = paramStr[1];                    //20081021
				robot_type_num++;                           //20081021
				// for MTK     
			} else if (paramStr[0].equals("DmdLoadFile_Path")) {
				loadfilePath = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("DmdLoadFile_Name")) {
				loadfileStr = paramStr[1];
				// for MTK end
			} else if (paramStr[0].equals("Test_Type")) {
				testTypeStr = paramStr[1];
				if(testTypeStr.equalsIgnoreCase("Final"))	//20171226
					javaExecSystemCmd("/usr/bin/expect /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/FT_userDataLog_update.expect");
//					javaExecSystemCmd2("xterm -rightbar -bg green -fg black -geometry 158x20 -e FT_userDataLog_update.csh", 10000);
			} else if (paramStr[0].equals("STDFlog")) { // 20090506
				STDFlogStr = paramStr[1];
				if(STDFlogStr.equalsIgnoreCase("ON")) {
					STDFlogFlag =true;
				}
				else if (STDFlogStr.equalsIgnoreCase("OFF")) {
					STDFlogFlag =false;
				}
			} else if (paramStr[0].equals("Job_Name")) { 
				JobStrRcp = paramStr[1];
				JobStr = paramStr[1];   

				/////////////////////////
				//////////////////////// 

				fileprefix = JobStr.split(".job");
				JobSoStr = fileprefix[0] + ".so";
				System.out.println("  JobStr = " + JobStr);
				System.out.println("JobSoStr = " + JobSoStr);
				//20080505 
				TPStr = JobStr;
				tx4.setText(TPStr);

			} else if (paramStr[0].equals("Dmd_SW_Rev")) {
				TPSWVerStr = paramStr[1];
				// System.out.println("@@@ TPSWVerStr = " + TPSWVerStr + " @@@");  // -- hh
				TPSWVer_prefix = stringRemoveChar(TPSWVerStr, '.');
				TPSWVer_prefix = stringRemoveChar(TPSWVer_prefix, '_');
				TPSWVer_prefix = TPSWVer_prefix.toUpperCase();
				//    System.out.println("    TPSWVerStr = " + TPSWVerStr);
				//    System.out.println("TPSWVer_prefix = " + TPSWVer_prefix);
				//} else if (paramStr[0].equals("Family_ID")) {
				//    FamilyIDStr = paramStr[1];

			} else if (paramStr[0].equals("Bonding")) {
				BondingStr = paramStr[1];
				FamilyIDStr = BondingStr;// 20080513 BondingStr==FamilyIDStr 

			} else if (paramStr[0].equals("Notch")) { //jj
				NotchStr = paramStr[1]; 

				//for MTK                 
			} else if (paramStr[0].equals("EfuseFlag")) {
				EfuseStr = paramStr[1];
				if(EfuseStr.equalsIgnoreCase("TRUE")){                    
					EfuseFlag = true;            		
				}
				else{
					EfuseFlag = false;            		
				}
			} else if (paramStr[0].equals("EngFlag")) {
				EngStr =  paramStr[1];
				if(EngStr.equalsIgnoreCase("TRUE")){                    
					EngFlag = true;            		
				}
				else{
					EngFlag = false;            		
				}
				//for MTK end  	

				/////////////////////////
				////////////////////////            	
			}  else if (paramStr[0].equals("TesterSiteMap")) {//20110614
				TesterSiteMapStr = paramStr[1];
			}else if (paramStr[0].equalsIgnoreCase("dateCode")) {//20130131
				dateCodeStr = paramStr[1];
			}else if (paramStr[0].equalsIgnoreCase("probeDeviceRcp")) {//20130131
				probeDeviceRcp = paramStr[1];
			}else if (paramStr[0].equalsIgnoreCase("JOB_REV")) {//201604.
				JOB_REV_Str_Rcp = paramStr[1];
			}else if (paramStr[0].equalsIgnoreCase("SBC_Rule")) {//20170912
				SBC_Rule_RCP = paramStr[1];
				if(TryRunOI && !RTStr.equalsIgnoreCase("RT"))
				SBC_Enable_RCP = true;
			}else if (paramStr[0].equals("WaferID_Detector")) { //20170912
				if(paramStr[1].equalsIgnoreCase("FALSE"))
					WaferID_detector_RCP = false; 
			}
		}

	}

	public static boolean getRecipeFileInfo() {
		
		boolean fileFlag = true;
		boolean status = false;
		String tmpStr = "";
		String tmpStr1 ="";//20080702;
		String infileStr = "";
		robot_type_num = 0; //20081021

		int strLength=barcode_programStr.length();//20080702

		if(MTK_series){
           //Add AZA10333B by Cola. 20160830  //Add BM10213B. 20170913  //Add BM10797 20171204
			if(barcode_devicetypeStr.indexOf("AHH10333E")!=-1 || barcode_devicetypeStr.indexOf("AHH10333D")!=-1 || barcode_devicetypeStr.indexOf("BHH10326CW")!=-1 || barcode_devicetypeStr.indexOf("BHH10326DW")!=-1 || barcode_devicetypeStr.indexOf("AZA10333B")!=-1 || barcode_devicetypeStr.indexOf("BM10213B")!=-1 || barcode_devicetypeStr.indexOf("BM10797") != -1){
                Load_ProbeCard_RCP = true;
				recipeFile = barcode_programStr.substring(0,strLength-5) + "_" + barcode_lbidStr + ".rcp";  //--hh	
			    
				if(STR_TestFlow_flag && (barcode_programStr.substring(strLength-9,strLength-5).equals("_STR"))) //for STR rule. 20160914  //for barcode_programStr have STR naming. 20170109
					
					recipeFile = barcode_programStr.substring(0,strLength-9) + "_" + barcode_lbidStr + ".rcp";  //--hh	
			}else{
				Load_ProbeCard_RCP = false;
				recipeFile = barcode_programStr.substring(0,strLength-5) + ".rcp";  //--hh
			}
			System.out.println("1.recipeFile =>> " + recipeFile); //--hh
		}else{
			if(barcode_programStr.endsWith("una")){  //by ChiaHui 20140513
				recipeFile = barcode_programStr.substring(0,strLength-3) + "rcp" ;//20080904
				jobFlag = true;
			}
			else {
				recipeFile = barcode_programStr + ".rcp" ;//20081021
				jobFlag = false;
			}
		}

		if(STR_TestFlow_flag){  //Add by Cola for STR test. 20160523-----Start
			int length = recipeFile.length();
			String recipeFile_noExtend = recipeFile.substring(0, length-4);
			String recipeFile_Extend = recipeFile.substring(length-4, length); // .rcp
			if(!recipeFile.substring(length-8, length-4).equals("_STR"))
				recipeFile = recipeFile_noExtend + "_STR" + recipeFile_Extend;     		
		}  //Add by Cola for STR test. 20160523-----End

		infileStr = recipefilePath + barcode_customerStr + "/" + recipeFile; //20080915
		System.out.println("2.infileStr =>> " + infileStr); //--hh

		recipeFileforComp = infileStr;//20120828 for csicDatacollection
		try {
			br = new BufferedReader(new FileReader(infileStr));//open file

			while ((tmpStr = br.readLine()) != null) {
				//System.out.println("br_read: " + tmpStr + ",  length = " + tmpStr.length());
				if (tmpStr.length()!=0) {
					processRecipeFileInfoContent(tmpStr);
				}
			}
			userPathforCP = autoResultPath + "CP/" + barcode_customerStr + "/" +
					barcode_devicetypeStr + "/";   //for CP file
			System.out.println("userPathforCP =>> " + userPathforCP);  //--hh

			System.out.println("testTypeStr: " + testTypeStr);                                                 
			System.out.println("testTypeStr: " + testTypeStr);                                                 
			System.out.println("testTypeStr: " + testTypeStr);                                                 
			System.out.println("testTypeStr: " + testTypeStr);                                                 
			
			//--- Create Datalog File Directory ------------ //--hh, ex: /var/home/service/autoResult/CPfile/L343/TT1S430.00-1/
			if(testTypeStr.equalsIgnoreCase("Wafer")){
				File dir_cp = new File(userPathforCP);
				if ( !dir_cp.exists() )                                                        
				{	
					dir_cp.mkdirs();  
					System.out.println("Make Datalog Directory = " + userPathforCP + "\n");
					textArea2.append("Make Datalog Directory = " + userPathforCP + "\n");
				}
			}


			br.close(); // close file
		} catch (FileNotFoundException event) {

			fileFlag = false;
			status = false;

			if (STR_TestFlow_flag){ //Add by Cola. 20160523-----Start
				tmpStr  = "<Exception> This is STR test flow , Recipe file naming error !\n";//20120412
				tmpStr += "+--------------------+ \n";
				tmpStr += "| RCP file :" + infileStr + " is NOT Found !\n";							
				tmpStr += "| Please call PE to check RCP file | \n";
				tmpStr += "+--------------------+ \n";
			}else{  //Add by Cola. 20160523-----End
				tmpStr  = "<Exception> getRecipeFileInfo: \n" + infileStr + " is NOT Found !\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "<Exception> getRecipeFileInfo: \n" + infileStr + " is NOT Found !\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
			}
			textArea2.append(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			if (STR_TestFlow_flag)
				saveErrorMessageRealtime("Msg:STR Recipe file is not found");
			else
				saveErrorMessageRealtime("Msg:Recipe file is not found");
			killproc(); System.exit(1); // to quit Java app for Linux

		} catch (java.io.IOException err) {
			fileFlag = false;
			status = false;

			tmpStr = "<Exception> getRecipeFileInfo: \n" + err + "\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);

			tmpStr = "<Exception> getRecipeFileInfo: \n" + err + "\n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+--------------------+ \n";
			textArea2.append(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:Recipe file is not found");
			killproc(); System.exit(1); // to quit Java app for Linux
		}

		//20081021
		if(robot_type_num>1)
		{

			robot_btname = new String[robot_type_num];
			robot_btname[0] = RobotStr;
			robot_btname[1] = RobotStr2;
			if (robot_type_num>2) {
				robot_btname[2] = RobotStr3;
			}
			//System.out.println("robot_type_num:"+robot_type_num);
			//System.out.println("=====Robot 1:"+ RobotStr);
			//System.out.println("=====Robot 2:"+ RobotStr2);
			//if (robot_type_num>2) {
			//System.out.println("=====Robot 3:"+ RobotStr3);
			//}
			//System.out.println("=====Robot 1:"+ robot_btname[0]);
			//System.out.println("=====Robot 2:"+ robot_btname[1]);
			//if (robot_type_num>2) {
			//System.out.println("=====Robot 3:"+ robot_btname[2]);
			//}
			robot_bt = new JRadioButton[robot_btname.length];

			//myPause();
		}

		if (STR_TestFlow_flag == false){  //Check Production program path and load file by Cola. 20160516-----Start
			boolean Unload_OI = false;
			if (JobPathStr.indexOf("/STR/") != -1 || JobPathStr.indexOf("STR") != -1){  
				Unload_OI = true;
				tmpStr  = "<Exception> This is SPRO test flow , program path or naming error !\n";//20120412
				tmpStr += "+--------------------+ \n";
				tmpStr += "| program path :" + JobPathStr + " can not using STR folder and naming ! \n";						
				tmpStr += "| Please call PE to check program path | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg: program path : " + JobPathStr + " is not SPRO path.");
			} 
			if (loadfileStr.indexOf("STR") != -1){
				Unload_OI = true;
				tmpStr  = "<Exception> This is SPRO test flow , load file name error !\n";//20120412
				tmpStr += "+--------------------+ \n";
				tmpStr += "| load file :" + loadfileStr + " can not using STR naming ! \n";						
				tmpStr += "| Please call PE to check load file | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg: load file : " + loadfileStr + " is not SPRO rule.");		
			}
			if(Unload_OI)
			{  killproc(); System.exit(1); }// to quit Java app for Linux
		}
		//Check Production program path and load file by Cola. 20161229-----Start
		boolean Unload_OI = false;
		if (JobPathStr.indexOf("eng") != -1 || JobPathStr.indexOf("Eng") != -1 || JobPathStr.indexOf("ENG") != -1){  
			Unload_OI = true;
			tmpStr  = "<Exception> This is SPRO test flow , program path or naming error !\n";//20120412
			tmpStr += "+--------------------+ \n";
			tmpStr += "| program path :" + JobPathStr + " can not using ENG folder and naming ! \n";						
			tmpStr += "| Please call PE to check program path | \n";
			tmpStr += "+--------------------+ \n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg: program path : " + JobPathStr + " is not SPRO path.");
		} 
		if (loadfileStr.indexOf("eng") != -1 || loadfileStr.indexOf("Eng") != -1 || loadfileStr.indexOf("ENG") != -1){
			Unload_OI = true;
			tmpStr  = "<Exception> This is SPRO test flow , load file name error !\n";//20120412
			tmpStr += "+--------------------+ \n";
			tmpStr += "| load file :" + loadfileStr + " can not using ENG naming ! \n";						
			tmpStr += "| Please call PE to check load file | \n";
			tmpStr += "+--------------------+ \n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg: load file : " + loadfileStr + " is not SPRO rule.");		
		}
		if(Unload_OI)
		{  killproc(); System.exit(1); }// to quit Java app for Linux
		//Check Production program path and load file by Cola. 20161229-----End
		
		if (fileFlag) {

			status = true;

			//for System.out.println
			tmpStr  = "==================================================================== \n";
			tmpStr += "Recipe File Information: " + recipeFile + "\n";
			tmpStr += "==================================================================== \n";
			tmpStr += "        Job_Path = " + JobPathStr       + "\n";
			tmpStr += "    Summary_Path = " + userSummaryPath  + "\n";
			tmpStr += "       RobotType = " + RobotStr         + "\n";
			if(robot_type_num>1){
				tmpStr += "       RobotType2 = " + RobotStr2        + "\n";//20081021
			}
			if(robot_type_num>2){
				tmpStr += "       RobotType3 = " + RobotStr3        + "\n";//20081021
			}
			//for MTK
			tmpStr += "DmdLoadFile_Path    = " + loadfilePath     + "\n";
			tmpStr += "DmdLoadFile_Name    = " + loadfileStr      + "\n";
			tmpStr += "      EfuseFlag     = " + EfuseFlag       + "\n";
			//for MTK end
			tmpStr += "        Job_Name = " + JobStr           + "\n";
			tmpStr += "      Dmd_SW_Rev = " + TPSWVerStr          + "\n";
			//tmpStr += "         Bonding = " + BondingStr     + "\n";
			tmpStr += "       Test_Type = " + testTypeStr      + "\n";
			tmpStr += "      STDFlogStr = " + STDFlogStr       + "\n";
			tmpStr += "   TesterSiteMap = " + TesterSiteMapStr + "\n";            //20110614
			if(!SBC_Rule_RCP.equals(""))
			tmpStr += "        SBC_Rule = " + SBC_Rule_RCP + "\n";
			tmpStr += "==================================================================== \n";
			System.out.println(tmpStr);


			// for Frame display
			tmpStr  = "================================================ \n";
			tmpStr += "Recipe File Information: " + recipeFile + "\n";
			tmpStr += "================================================ \n";
			tmpStr += "                  Job_Path = " + JobPathStr   + "\n";
			tmpStr += "         Summary_Path = " + userSummaryPath   + "\n";
			tmpStr += "               RobotType = " + RobotStr       + "\n";
			if(robot_type_num>1){
				tmpStr += "               RobotType2 = " + RobotStr2      + "\n";//20081021
			}
			if(robot_type_num>2){
				tmpStr += "               RobotType2 = " + RobotStr3      + "\n";//20081021
			}
			tmpStr += "  DmdLoadFile_Path = " + loadfilePath         + "\n";
			tmpStr += "DmdLoadFile_Name = " + loadfileStr            + "\n";
			tmpStr += "            EfuseFlag    = " + EfuseFlag       + "\n";
			tmpStr += "                Job_Name = " + JobStr           + "\n";
			tmpStr += "          Dmd_SW_Rev = " + TPSWVerStr          + "\n";
			//tmpStr += "                   Bonding = " + BondingStr     + "\n";
			tmpStr += "               Test_Type = " + testTypeStr    + "\n";
			tmpStr += "            STDFlogStr   = " + STDFlogStr       + "\n";
			tmpStr += "           TesterSiteMap = " + TesterSiteMapStr + "\n";            //20110614
			tmpStr += "          dateCode    = " + dateCodeStr + "\n";            				//20130131            
			if(!SBC_Rule_RCP.equals(""))
				tmpStr += "          SBC_Rule    = " + SBC_Rule_RCP + "\n";  
			tmpStr += "================================================ \n";
			textArea1_2.setText(tmpStr);

			textArea2.append(tmpStr);
		}
		System.out.print(" @@@ getRecipeFileInfo() = " + status);
		return status;
	}

	//Example Load File:
	//---------------------------------
	//Job_Name = spec.job
	//Dmd_SW_Rev = v1.5.3_BLD11
	//Family_ID = 1 //unused 20080513
	//Bonding = MJ
	//

	public static void processLoadFileInfoContent(String inStr) {

		String tmpStr = "";
		String paramStr[] = new String[2];

		String fileprefix[] = new String[1];
		fileprefix[0] = "";

		boolean twostrFlag = true;

		//initial
		paramStr[0] = "";
		paramStr[1] = "";

		inStr = stringRemoveSpaceHeadTail(inStr);

		//System.out.println("case0: inStr = " + inStr);

		paramStr = inStr.split("=");

		if (inStr.endsWith("=")==false) {

			//System.out.print("processSystemInfoContent: paramStr[0] = " + paramStr[0]);
			//System.out.println("\tparamStr[1] = " + paramStr[1]);

			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
			paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
			//System.out.print("processSystemInfoContent: paramStr[0] = " + paramStr[0]);
			//System.out.println("\tparamStr[1] = " + paramStr[1] + " (***NEW***)");
			twostrFlag = true;

		} else {

			//System.out.println("processSystemInfoContent: paramStr[0] = " + paramStr[0]);
			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
			//System.out.println("processSystemInfoContent: paramStr[0] = " + paramStr[0] + " (***NEW***)");
			twostrFlag = false;
		}


		if (twostrFlag) {

			if (paramStr[0].equals("Job_Name")) {
				JobStr = paramStr[1];
				//     fileprefix = JobStr.split(".job");  // --hh D10
				fileprefix = JobStr.split(".una");  // --hh
				JobSoStr = fileprefix[0] + ".so";
				JobCmpStr = fileprefix[0];  //20110311 
				System.out.println("  JobStr = " + JobStr);
				System.out.println("JobSoStr = " + JobSoStr);
				//20080505 
				TPStr = JobStr;
				tx4.setText(TPStr);
			} else if (paramStr[0].equals("Dmd_SW_Rev")) {
				TPSWVerStr = paramStr[1];
				TPSWVer_prefix = stringRemoveChar(TPSWVerStr, '.');
				TPSWVer_prefix = stringRemoveChar(TPSWVer_prefix, '_');
				TPSWVer_prefix = TPSWVer_prefix.toUpperCase();
				System.out.println("    TPSWVerStr = " + TPSWVerStr);
				System.out.println("TPSWVer_prefix = " + TPSWVer_prefix);
				//} else if (paramStr[0].equals("Family_ID")) {
				//    FamilyIDStr = paramStr[1];
			} else if (paramStr[0].equals("Bonding")) {
				BondingStr = paramStr[1];
				FamilyIDStr = BondingStr;// 20080513 BondingStr==FamilyIDStr 
			}
		}

	}


	public static boolean getLoadFileInfo() {

		boolean fileFlag = true;
		boolean status = false;
		String tmpStr = "";
		String infileStr = "";

		int length = loadfileStr.length();
		String loadfile_noExtend = loadfileStr.substring(0, length-5);
		String loadfile_Extend = loadfileStr.substring(length-5, length); // .load

		//String correctloadfileStr = ""; // move to global variable


		if (barcode_stationStr.equals("FT") || barcode_stationStr.equals("FT1") || barcode_stationStr.equals("PreFT")) {//20080522
			correctloadfileStr = barcode_programStr;//20080505
		} else {
			correctloadfileStr = barcode_programStr;//20080505
		}

		// System.out.println("@@@ correctloadfileStr = " + correctloadfileStr +" @@@"); //--hh
		// System.out.println("@@@ loadfileStr        = " + loadfileStr +" @@@"); //--hh
		// -- @@@ correctloadfileStr = Bin311_U3.1.1_V01.load @@@
		// -- @@@ loadfileStr        = Bin311_U3.1.1_V01.load @@@

		if (correctloadfileStr.equals(loadfileStr)==false) {  // --hh  if equals will return true
			if (STR_TestFlow_flag && correctloadfileStr.equals(loadfileStr.substring(0, length-9)+loadfile_Extend)==true)
			{ //Add by Cola. 20160520       		
			}else{
				status = false;

				tmpStr = "<Exception> getLoadFileInfo: \n";
				tmpStr += "LoadFile Name in Recipe file: " + loadfileStr + "\n";
				tmpStr += "Correct LoadFile Name in Barcode: " + correctloadfileStr + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				System.out.println(tmpStr);
				textArea2.append(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:compare Load file Name is fail");
				killproc(); System.exit(1); // to quit Java app for Linux
			}
		}

		loadfilePath = checkifHeadEndWithSlash(loadfilePath);
		if(STR_TestFlow_flag){ //Add by Cola for STR test. 20160506-----Start
			if(!loadfileStr.substring(length-9, length-5).equals("_STR"))
				infileStr = loadfilePath + loadfile_noExtend + "_STR" + loadfile_Extend;
			else
				infileStr = loadfilePath + loadfileStr;
		}else //Add by Cola for STR test. 20160506-----End
			infileStr = loadfilePath + loadfileStr;

		loadFileforComp = infileStr;//20120828 for csicDatacollection

		System.out.println("Loadfile = " + infileStr + " \n"); //--hh
		// -- @@@ infileStr = /usr/local/home/autoload/Loadfile/L022/Bin311_U3.1.1_V01.load @@@ //--hh


		try {
			br = new BufferedReader(new FileReader(infileStr));//open Load file

			while ((tmpStr = br.readLine()) != null) {
				//System.out.println("br_read: " + tmpStr + ",  length = " + tmpStr.length());
				if (tmpStr.length()!=0) {
					processLoadFileInfoContent(tmpStr);
				}
			}

			br.close(); // close file
		} catch (FileNotFoundException event) {

			fileFlag = false;
			status = false;
			if(STR_TestFlow_flag){
				tmpStr  = "<Exception> This is STR Test Flow , No STR load file\n"; 
				tmpStr += "Please call PE to Check STR load file !!\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| load file :" + infileStr + " is NOT Found !\n";		
				tmpStr += "+--------------------+ \n";
			} else{
				tmpStr  = "<Exception> getLoadFileInfo: \n" + infileStr + " is NOT Found !\n";
				tmpStr += "Correct LoadFile: " + correctloadfileStr + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
			}
			System.out.println(tmpStr);
			textArea2.append(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			if(STR_TestFlow_flag)
				saveErrorMessageRealtime("Msg:STR Load file is not found");
			else
				saveErrorMessageRealtime("Msg:Load file is not found");
			killproc(); System.exit(1); // to quit Java app for Linux

		} catch (java.io.IOException err) {
			fileFlag = false;
			status = false;
			if(STR_TestFlow_flag){
				tmpStr  = "<Exception> This is STR Test Flow , No STR load file\n"; 
				tmpStr += "Please call PE to Check STR load file !!\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| load file :" + infileStr + " is NOT Found !\n";	
				tmpStr += "+--------------------+ \n";
			} else{
				tmpStr = "<Exception> getLoadFileInfo: \n" + err + "\n";
				tmpStr += "Correct LoadFile: " + correctloadfileStr + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
			}
			System.out.println(tmpStr);
			textArea2.append(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			if(STR_TestFlow_flag)
				saveErrorMessageRealtime("Msg:STR Load file is not found");
			else
				saveErrorMessageRealtime("Msg:Load file is not found");
			killproc(); System.exit(1); // to quit Java app for Linux
		}

		if (fileFlag) {

			status = true;
			// for System.out.println
			// for Frame display
			tmpStr  = "================================================ \n";
			tmpStr += "LoadFile Information: "+ loadfileStr + "\n";
			tmpStr += "================================================ \n";
			tmpStr += "     Job_Name = " + JobStr           + "\n";
			tmpStr += "Dmd_SW_Rev = " + TPSWVerStr          + "\n";
			tmpStr += "        Bonding = " + BondingStr     + "\n";
			tmpStr += "================================================ \n";
			System.out.println(tmpStr);
			textArea1_2.append(tmpStr);
			textArea2.append(tmpStr);
		}

		//20110311 //        
		String barcode_programStr_tmp="";

		if (STR_TestFlow_flag)  //STR flow dont compare "_STR" by Cola. 20160519
			barcode_programStr_tmp=loadfileStr.substring(0,length-9);
		else
			barcode_programStr_tmp=loadfileStr.substring(0,length-5);
		if (testTypeStr.equalsIgnoreCase("Wafer")) {
			if (!JobCmpStr.equals(barcode_programStr_tmp)) {
				status = false;

				tmpStr = "<Exception> Job Name Align: \n";
				tmpStr += "Job Name in the Load file: " + JobCmpStr + "\n";
				tmpStr += "Correct Name in Barcode: " + barcode_programStr_tmp + "\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);
				textArea2.append(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:compare Load file Name is fail.");
				killproc(); System.exit(1); // to quit Java app for Linux
			}
		}

		if(!JobStr.equals(JobStrRcp)){
			status = false;

			tmpStr = "<Exception> Job Name Align: \n";
			tmpStr += "Job Name in the RCP file: " + JobStrRcp + "\n";
			tmpStr += "Job Name in the LOAD file: " + JobStr + "\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);
			textArea2.append(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:compare Job Name is fail.");
			killproc(); System.exit(1); // to quit Java app for Linux

		}
		// System.out.println(" @@@ getLoadFileInfo() = " + status + " @@@");  // --hh
		// status = true;   //--hhs
		return status;
	}

	public static boolean checkDmdSWVersion() {

		String tmpStr = "";
		boolean status = false;

		// System.out.println(" @@@ TPSWVerStr = " + TPSWVerStr + " @@@"); // --hh
		// System.out.println(" @@@ DMDSWVerStr = " + DMDSWVerStr + " @@@"); // --hh

		if (TPSWVerStr.equals(DMDSWVerStr)) {
			status = true;
		} else {

			status = false;

			tmpStr  = "     +-----------------------------+ \n";
			tmpStr += "     + DX SW Version is NOT match + \n";
			tmpStr += "     +-----------------------------+ \n";
			tmpStr += "     System DX SW Version = " + DMDSWVerStr + "\n";
			tmpStr += "TestProgram DX SW Version = " + TPSWVerStr  + "\n\n";
			tmpStr += "     +----------------------------+ \n";
			tmpStr += "     + Please call the Supervisor + \n";
			tmpStr += "     +----------------------------+ \n";
			System.out.println(tmpStr);

			tmpStr  = "     +-------------------------+ \n";
			tmpStr += "     + DX SW Version is NOT match + \n";
			tmpStr += "     +-------------------------+ \n";
			tmpStr += "        System DX SW Version = " + DMDSWVerStr + "\n";
			tmpStr += "TestProgram DX SW Version = " + TPSWVerStr  + "\n\n";
			tmpStr += "        +---------------------+ \n";
			tmpStr += "        + Please call the Supervisor + \n";
			tmpStr += "        +---------------------+ \n";

			textArea2.append(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:compare DMD Version is fail");
			killproc(); System.exit(1); // to quit Java app for Linux
		}
		System.out.println(" @@@ checkDmdSWVersion() = " + status);
		return status;
	}

	//public boolean checkifFileExistence() {
	//
	//    String tmpStr = "";
	//    boolean fileFlag = true;
	//
	//    //ftpfile  = "spec.tar.gz";
	//    ftpfile  = TPStr + ".tar.gz";
	//    tmpStr = localdownloadPath + ftpfile;
	//
	//    try {
	//        br = new BufferedReader(new FileReader(tmpStr));//open file
	//        br.close(); // close file
	//        tmpStr = "File: " + tmpStr + " is already Existent.  fileFlag = "+ fileFlag + "\n";
	//        System.out.print("checkifFileExistence: " + tmpStr);
	//        textArea2.append(tmpStr);
	//
	//    } catch (FileNotFoundException event) {
	//        fileFlag = false;
	//        tmpStr = "checkifFileExistence: \nLocal File = " + ftpfile + " is NOT Existed.  fileFlag = " + fileFlag + "\n";
	//        //tmpStr = tmpStr + event + "\n";
	//        System.out.print(tmpStr);
	//        textArea2.append(tmpStr);
	//
	//    } catch (java.io.IOException err) {
	//        fileFlag = false;
	//        tmpStr = "checkifFileExistence: " + err + ", fileFlag = " + fileFlag + "\n";
	//        System.out.print(tmpStr);
	//        textArea2.append(tmpStr);
	//    }
	//
	//    return fileFlag;
	//}

	//****************************************Start copy here  -Cloud 0713******************************
	//20090507  S
	int getSiteNum(){
		int SitesNum=0; 
		String tmpStr;       
		try{
			SitesNum = 0;
			BufferedReader br_STDinfo = new BufferedReader(new FileReader("/tmp/Sites_info.txt"));
			while ((tmpStr = br_STDinfo.readLine()) != null) {
				tmpStr = tmpStr.replace('\t',' ');
				tmpStr = stringRemoveSpaceHeadTail(tmpStr);

				if (tmpStr.startsWith("0 ")) {
					SitesNum = 1;
				} else if (tmpStr.startsWith("1 ")) {
					SitesNum = 2;
				} else if (tmpStr.startsWith("3 ")) {
					SitesNum = 4;
				} else if (tmpStr.startsWith("7 ")) {
					SitesNum = 8;
				} else if (tmpStr.startsWith("15 ")) {
					SitesNum = 16;
				} else if (tmpStr.startsWith("31 ")) {
					SitesNum = 32;
				}
				System.out.println("SitesNum :" +SitesNum);
			}

			br_STDinfo.close(); // close file

		} catch (FileNotFoundException event) {
			tmpStr  = "<Exception> SitesFileInfo: \n" + "/tmp/Sites_info.txt" + " is NOT Found !\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);

			tmpStr  = "<Exception> SitesFileInfo: \n" + "/tmp/Sites_info.txt" + " is NOT Found !\n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+--------------------+ \n";
			textArea2.append(tmpStr);
		} catch (java.io.IOException err) {
			tmpStr = "<Exception> SitesFileInfo: " + err + "\n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr);

			tmpStr = "<Exception> SitesFileInfo: " + err + "\n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";

		}
		return SitesNum;
	}
	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_CENTER = 2;
	public String fill = " ";

	/**
	 * @param name The String to be formatted.
	 * @param width The desired width.
	 * @param position The alignment position, can be ALIGN_LEFT,ALIGN_RIGHT, or ALIGN_CENTER)
	 * @param offset Only valid for ALIGN_LEFT
	 * @return The string which is formatted.
	 */
	public String format(String name, int width, int position, int offset)
	{
		StringBuffer temp = new StringBuffer();
		if (name.length() > width) {
			width = name.length();
		}
		temp.append(space(width));
		temp.setLength(width);

		if (position == ALIGN_LEFT) {
			temp.replace((0 + offset), name.length(), name);
		}
		else if (position == ALIGN_RIGHT) {	//Right
			temp.replace((width - name.length()), width,name);
		}
		else {	// position = ALIGN_CENTER  or else.
			temp.replace(((width - name.length()) / 2), ((width - name.length()) / 2 + name.length()), name);
		}
		return temp.toString();
	}

	/**
	 *
	 * @param name The String to be formatted.
	 * @param width The desired width.
	 * @param position The alignment position, can be ALIGN_LEFT,ALIGN_RIGHT, or ALIGN_CENTER)
	 * @return The string which is formatted.
	 */
	public String format(String name, int width, int position)
	{
		return format(name, width, position, 0);
	}

	/**
	 *
	 * @param name The int number to be formatted.
	 * @param width The desired width.
	 * @param position The alignment position, can be ALIGN_LEFT,ALIGN_RIGHT, or ALIGN_CENTER)
	 * @return The string which is formatted.
	 */
	public String format(int i, int width, int position)
	{
		return format(String.valueOf(i), width, position);
	}


	/**
	 *
	 * @param name The double number to be formatted.
	 * @param width The desired width.
	 * @param position The alignment position, can be ALIGN_LEFT,ALIGN_RIGHT, or ALIGN_CENTER)
	 * @return The string which is formatted.
	 */
	public String format(double i, int width, int position)
	{

		return format(String.valueOf(i), width, position);
	}

	/**
	 * To create the space string.
	 * @param s The desired width of the space.
	 * @return The space String.
	 */
	public String space(int s)
	{
		StringBuffer sp = new StringBuffer();
		for (int i = 0; i < s; i++) {
			sp.append(fill);
		}
		return sp.toString();
	}
	public void resetExtraCheck(int siteCount){
		String tmp = UserFlag_getString("csicSummaryFlagNames");  // turn csicDebug
		String[] flags=tmp.split(",");
		for (int i=0;i<flags.length;i++){
			for(int j=0;j<siteCount;j++) {
				String cmd = "dmd_cmd UserFlagSetInt SummaryFlag_"+flags[i]+"_"+j+" 0";
				javaExecSystemCmd(cmd);
			}
		}
	}
	void generateExtraCheck(String fileName,int siteCount){//20100722
		EFuseErrorFlag = false;
		int Cnt888[]=new int[2000];
		int Cnt899[]=new int[2000];
		int Cnt999[]=new int[2000];
		File log_file = new File(fileName);
		//myPause();
		//try {
		//	FileWriter fw = new FileWriter(log_file, log_file.exists());
		String tmp = UserFlag_getString("csicSummaryFlagNames");  // turn csicDebug
		if(!tmp.equals("")){
			//		fw.write("\n\n\n\n");
			//		fw.write(format("Extra Flag Checking\n",60,  ALIGN_RIGHT));
			//		fill="-";
			//		fw.write(format("\n",8*siteCount+50,ALIGN_RIGHT));
			//		fill=" ";
			//		fw.write("FlagName                              ") ;
			//		for(int site_num =0; site_num < siteCount; site_num++)
			//			fw.write(format("Site"+site_num ,(7-site_num/10),ALIGN_RIGHT));
			//		fw.write("       Total\n");;
			//		fill="-";
			//		fw.write(format("\n",8*siteCount+50,ALIGN_RIGHT));
			//		fill=" ";                        
			//                
			String[] flags=tmp.split(",");
			for (int i=0;i<flags.length;i++){
				//fw.write(format(flags[i],36,ALIGN_RIGHT));
				int total=0;
				for(int j=0;j<siteCount;j++) {
					String go = UserFlag_getInt("SummaryFlag_"+flags[i]+"_"+j);;                                            
					if(go.equals("999999999")){
						go = "0";
					}
					total += Integer.parseInt(go);
					//fw.write(format(go,8,ALIGN_RIGHT));

					if(Integer.parseInt(flags[i])==888) {
						Cnt888[j]=Integer.parseInt(go);
					}
					if(Integer.parseInt(flags[i])==899) {
						Cnt899[j]=Integer.parseInt(go);
					}
					if(Integer.parseInt(flags[i])==999) {
						Cnt999[j]=Integer.parseInt(go);
					}
				}
				//fw.write(format(total+"\n",12,ALIGN_RIGHT));
			}
			//fw.write(format("\n",8*siteCount+50,ALIGN_RIGHT));
			for(int i=0;i<siteCount;i++) {
				int totalCnt = Cnt888[i]-Cnt899[i]-Cnt999[i];


				System.out.println("Cnt888 :" + Cnt888[i]);
				System.out.println("Cnt899 :" + Cnt899[i]);
				System.out.println("Cnt999 :" + Cnt999[i]);
				if(totalCnt !=0) {
					EFuseErrorFlag = true;
				}
			}
			System.out.println("EFuseError :" + EFuseErrorFlag);
			//                
		}
		//	fw.flush();
		//	fw.close();
		//} catch (IOException e) {
		//	e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		//}
		//myPause();
	}

	public String UserFlag_getString(String cmdStr){
		String cmd = "dmd_cmd UserFlagGetString "+cmdStr;
		String result = getJavaExecSystemCmd(cmd);
		System.out.println("======"+result);
		int idx = result.indexOf(":");
		if(idx!=-1){
			result = result.substring(idx+1).trim();
		}else{	
			result = "";
		}
		int idx1=0;
		if(DMDSWVerStr.startsWith("v1.5.3")){
			for(int i=0;i<result.length();i++){
				if((int)result.charAt(i)==10){
					idx1=i;     
					break;
				}

			}

			result=result.substring(0,idx1);
		}
		System.out.println("result:" + result);
		return result;

	}

	public String UserFlag_getInt(String cmdStr){
		String cmd = "dmd_cmd UserFlagGetInt "+cmdStr;
		String result = getJavaExecSystemCmd(cmd);
		System.out.println("======"+result);
		int idx = result.indexOf(":");
		if(idx!=-1){
			result = result.substring(idx+1).trim();
		}else{	
			result = "";
		}	
		int idx1=0;
		if(DMDSWVerStr.startsWith("v1.5.3")){        
			for(int i=0;i<result.length();i++){
				if((int)result.charAt(i)==10){
					idx1=i;
					break;
				}
			}

			result=result.substring(0,idx1);
		}
		System.out.println("result:" + result);
		return result;
	}

	public String getJavaExecSystemCmd(String cmdStr) {

		String tmpStr = null;
		String result = "";

		try {



			Process proc = Runtime.getRuntime().exec(cmdStr);

			InputStream in = proc.getInputStream();


			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);

			String str;
			while ((str = br.readLine()) != null) {
				System.out.println(">>>>>"+str);
				result=result+"\n"+str;
			}
			in.close();

			Thread.sleep(500);// unit: ms

			tmpStr = "Complete cmd: " + cmdStr + "\n\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception1> javaExecSystemCmd: " + err + "\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		} catch (java.lang.InterruptedException Ierr) {
			tmpStr = "<Exception2> javaExecSystemCmd: " + Ierr + "\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		}

		return result;
	}

	//****************************************End copy here  -Cloud 0713******************************

	public static void javaExecSystemCmd(String cmdStr) {

		String tmpStr = null;

		try {

			Process proc = Runtime.getRuntime().exec(cmdStr);

			InputStream in = proc.getInputStream();

			int c;

			StringBuffer s = new StringBuffer();

			while ((c = in.read()) != -1) {

			}
			in.close();

			Thread.sleep(500);// unit: ms

			tmpStr = "Complete cmd: " + cmdStr + "\n\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception1> javaExecSystemCmd: " + err + "\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		} catch (java.lang.InterruptedException Ierr) {
			tmpStr = "<Exception2> javaExecSystemCmd: " + Ierr + "\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		}

	}

	public static void javaExecSystemCmd2(String cmdStr, int waittime) {

		String tmpStr = null;

		try {

			Process proc = Runtime.getRuntime().exec(cmdStr);

			//Thread.sleep(500);// unit: ms
			Thread.sleep(waittime);// unit: ms

			tmpStr = "Complete cmd: " + cmdStr + "\n\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception1> javaExecSystemCmd2: " + err + "\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		} catch (java.lang.InterruptedException Ierr) {
			tmpStr = "<Exception2> javaExecSystemCmd2: " + Ierr + "\n";
			System.err.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		}

	}

	//public void decompressFile() {
	//
	//    String cmd = null;
	//    String tmpStr = null;
	//    String fileprefix[] = new String[1];
	//
	//    fileprefix[0] = "";
	//
	//    if (ftpfile.endsWith(".tar.gz")) {
	//
	//        fileprefix = ftpfile.split(".tar.gz");
	//        //System.out.println("decompressFile: fileprefix = " + fileprefix[0]);
	//
	//        cmd = "tar xvfz " + localdownloadPath + ftpfile;
	//        tmpStr = "decompressFile: " + cmd + "\n";
	//        System.out.print(tmpStr); 
	//        textArea2.append(tmpStr);
	//        javaExecSystemCmd(cmd);
	//
	//        cmd = "rm -rf " + productionPath + fileprefix[0];
	//        System.out.print(cmd + "\n"); 
	//        textArea2.append(cmd + "\n");
	//        javaExecSystemCmd(cmd);
	//
	//        cmd = "mv " + fileprefix[0] + " " + productionPath;
	//        System.out.print(cmd + "\n"); 
	//        textArea2.append(cmd + "\n");
	//        javaExecSystemCmd(cmd);
	//
	//    } else {
	//
	//        tmpStr = "decompressFile: No decompress any File.\n\n";
	//        System.out.print(tmpStr); 
	//        textArea2.append(tmpStr);
	//    }
	//}


	public void processCmdFile() {
		String cmd = "";

		//cmd = "/bin/mv " + autoResultPath + "cmd_script.dmd" + " " + JobPathStr;
		//System.out.print(cmd + "\n"); 
		//textArea2.append(cmd + "\n");
		//javaExecSystemCmd(cmd);

		//cmd = "chmod 755 " + autoResultPath + "execDmd";
		cmd = "chmod 755 " + LocalPath + "execDmd";//20091103
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);

		cmd = "chmod 755 " + LocalPath + "cmd_script.dmd";//20091103
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);	

	}
	public void gpibSendCommand(String GPIB_Command)  //Send GPIB Command. Add by Cola. 20150817
	{	//20170619-----Start
		String tmpStr="";
		String cmd;
		String infileStr;
		try{
			//			JOptionPane.showMessageDialog(null, "GPIB_Command: " + GPIB_Command);
			cmd = "/bin/rm /tmp/gpib_info.txt";
			tmpStr = cmd + "\n";
			System.out.println(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			cmd = csicAutoPath + "/bin/newGpibClean 1 " + GPIB_Command;
			//			JOptionPane.showMessageDialog(null, "cmd: " + cmd);
			tmpStr = cmd + "\n\n";
			System.out.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			if(GPIB_Command.equals("CHKMATCH?"))
			{
				BufferedReader gpibBr;
				infileStr = "/tmp/gpib_info.txt";

				gpibBr = new BufferedReader(new FileReader(infileStr));//open file

				while ((tmpStr = gpibBr.readLine()) != null) {
					if (tmpStr.length()!=0) {
						//                        JOptionPane.showMessageDialog(null, "gpibsend CHKMATCH? : tmpStr = " + tmpStr);
						processgpibContent(tmpStr);
						//						JOptionPane.showMessageDialog(null, "gpibFTType = " + gpibFTType);						
					}
				}       
				gpibBr.close(); // close file
			}
		}catch(Exception err){
			JOptionPane.showMessageDialog(null, "gpibSendCommand() Error Message: " + err);  		
		}
	}
	public void GPIB_AlarmSetup()
	{
		//Send GPIB Command for Handler Setting. by Cola 2015/09/01-----Start
		String GPIBCommand;
		try{
			gpibSendCommand("CHKMATCH?");
			if(gpibFTType.equals("0") || gpibFTType.equals("1")){  //Add by Cola. 20160913
				NewHandler_GPIBnoSupport = true;
			}

		} catch (Exception e1){
			e1.printStackTrace();  JOptionPane.showMessageDialog(null, "Send CHKMATCH Interrupted Exception: " + e1);
		}
		//Smart Bin Tray sending. 20171012
//		if(barcode_devicetypeStr.indexOf("MT6328") != -1 || barcode_devicetypeStr.indexOf("MT6323L") != -1 ){
			if(gpibFTType.indexOf("NS") == -1 && !RTStr.equalsIgnoreCase("HW")){ //20171109
				if(RTStr.equalsIgnoreCase("A1") /*|| RTNumStr2.equalsIgnoreCase("RT1")*/){

					Smartbintray_mode = "FT";
					GPIBCommand = "SetHandlerStartMode FT ";
					gpibSendCommand(GPIBCommand);
				}
				else 
				{
					Smartbintray_mode = "RT";
					GPIBCommand = "SetHandlerStartMode RT ";
					gpibSendCommand(GPIBCommand);
				}
			}
//		}
		
		if(testTypeStr.equalsIgnoreCase("Final") && !RTStr.equalsIgnoreCase("HW") && Enable_AlarmSetup){ //Move to here. 20160913
			if(HandlerSW_sendGPIB.equals("NEWOI")) //Add by Cola for Different Handler Software. 20150925 
			{
				       			
				if(!barcode_temperatureStr.equals("") && !barcode_temperatureStr.equals("NA")) //Send Temperature Setting 
				{   //SETTEMP +25						
					if(barcode_temperatureStr.substring(1, 1).equals("-") || barcode_temperatureStr.substring(1, 1).equals("+"))
						GPIBCommand = "SETTEMP " + barcode_temperatureStr;
					else
						GPIBCommand = "SETTEMP +" + barcode_temperatureStr;
//					gpibSendCommand(GPIBCommand);  Remark on 20161007.

				}      	
				if(!barcode_SoaktimeStr.equals("") && !barcode_SoaktimeStr.equals("NA") && Float.parseFloat(barcode_temperatureStr) > 30) //Send Soak Time Setting 
				{   //SETSOAK 90
					GPIBCommand = "SETSOAK " + barcode_SoaktimeStr;
//					gpibSendCommand(GPIBCommand);  Remark on 20161007.

				}
				if(!barcode_locationCode.equals("") && !barcode_locationCode.equals("NA") && !barcode_duts.equals("1")) //Sigal Site Don't Send Command
				{
					GPIBCommand = "SETSITEMAP " + barcode_locationCode + "_"; //Send Site Map Setting 
//					gpibSendCommand(GPIBCommand);  Remark on 20161007.

				}
				//Send Site Difference and Bin Alarm Setting-----Start
				if((!barcode_SiteDiffYield_Str.equals("") && !barcode_SiteDiffYield_Str.equals("NA")) || (!barcode_AlarmBinYield_Str.equals("") && !barcode_AlarmBinYield_Str.equals("NA")))
				{
					if(barcode_SiteDiffYield_Str.equals("") || barcode_SiteDiffYield_Str.equals("NA"))
						barcode_SiteDiffYield_Str = "NULL";
					HandlerAlarmSetup_Command = "SETUP_" + barcode_SiteDiffYield_Str + "_" + barcode_AlarmBinYield_Str; 
				}

				if(!HandlerAlarmSetup_Command.equals("") && Enable_AlarmSetup){
					if(RTStr.equalsIgnoreCase("A1") || RTStr.equalsIgnoreCase("RT")){	

						GPIBCommand = "SETUP_NULL";   //Clean Bin Alarm Setting First. 
						gpibSendCommand(GPIBCommand);

						GPIBCommand = "SETUP_NULL_" + barcode_BinDefine_Str; //Set Bin Define for All used Bin.
						gpibSendCommand(GPIBCommand);

						if(rtrbt[1].isSelected()){ //Temp Remove EQC
							//    				if(rtrbt[1].isSelected() || rtrbt[3].isSelected()){ //Setting for A1 and EQC station only. by Cola. 20151202
							gpibSendCommand(HandlerAlarmSetup_Command);    	    			
						}
					}			
				}
				//Send Site Difference and Bin Alarm Setting-----End
			}
		}
		//Send GPIB Command for Handler Setting. by Cola 2015/09/01-----End
	} //20170619-----End
	public boolean gpibCheck() {//20110614

		boolean status=true;
		boolean GpibFlag=true;
		boolean GpibFlagTemp=true;
		boolean GpibFlagAllTemp[]=new boolean[9]; //Add for All Equipment Temperature by Cola. 2015/06/05
		boolean GpibFlagSoakTime=true;
		boolean GpibFlagSiteMap=true;
		boolean GpibFlagAlarmSetup = true; //Add by Cola. 2015/09/01 
		boolean FTSiteMapCounter = false;

		if(!barcode_temperatureStr.equals("")){
			String tmpStr="";
			String cmd;
			String infileStr;
			cmd = "/bin/rm /tmp/gpib_info.txt";     
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);
			//Add by Cola for Different Handler Software. 20150925 -----Start   	
			if(HandlerSW_sendGPIB.equals("NEWOI"))       //Go to New GPIB Command.
				cmd = csicAutoPath + "/bin/newGpibClean NewOI";
			else if(HandlerSW_sendGPIB.equals("TEMPE"))  //Go to All Temperature GPIB Command.
				cmd = csicAutoPath + "/bin/newGpibClean AllTemp";
			else if(HandlerSW_sendGPIB.equals("ALARM"))  //Go to Alarm Setup GPIB Command.
				cmd = csicAutoPath + "/bin/newGpibClean AlarmSet";
			else{                                       //Go to Old GPIB Command.
				HandlerSW_sendGPIB = "TEMPE";
				cmd = csicAutoPath + "/bin/newGpibClean AllTemp";
			}
			//Add by Cola for Different Handler Software. 20150925 -----End	

			tmpStr = cmd + "\n\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			//init
			gpibFTType = "";
			gpibFTTemp = "";
			gpibFTSoakTime = "";
			gpibFTSiteMap = "";
			for(int t = 0 ; t < gpibFTAllTemp.length ; t++)
				gpibFTAllTemp[t] = "";

			BufferedReader gpibBr;
			infileStr = "/tmp/gpib_info.txt";
			try {
				gpibBr = new BufferedReader(new FileReader(infileStr));//open file

				while ((tmpStr = gpibBr.readLine()) != null) {

					System.out.print(tmpStr + "\n"); 
					System.out.print(tmpStr + "\n"); 
					System.out.print(tmpStr + "\n"); 


					if (tmpStr.length()!=0) {
						//                    	JOptionPane.showMessageDialog(null, "gpibCheck(): tmpStr = " + tmpStr);
						processgpibContent(tmpStr);
					}
				}       

				gpibBr.close(); // close file
			} catch (FileNotFoundException event) {

				status = false;

				tmpStr  = "<Exception> getGpibInfo: " + infileStr + " is NOT Found !\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "<Exception> getGpibInfo: " + infileStr + " is NOT Found !\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:FT gpib file is not found");
				killproc(); System.exit(1); // to quit Java app for Linux

			} catch (java.io.IOException err) {
				status = false;
				tmpStr = "<Exception> getGpibLInfo: " + err + "\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr = "<Exception> getGpibLInfo: " + err + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:FT gpib file is not found");
				killproc(); System.exit(1); // to quit Java app for Linux
			}

			if(gpibFTType.equals("CHKMATCH?") || NewHandler_GPIBnoSupport) //20140120 //add NewHandler_GPIBnoSupport by Cola. 20160823
				Call_FT_GPIB_warnning();	
			if((!gpibFTType.equals("CHKMATCH?"))&&gpibFlag){ // only for HT do error check						          
				//            if((gpibFTType.equals("HT")||gpibFTType.equals("HT-9045W"))&&gpibFlag){ // only for HT do error check

				tmpStr  = "Handler Type          = " + gpibFTType + "\n";
				tmpStr  += "Handler Temperature   = " + gpibFTTemp + "\n";
				// Add by Cola. 2015/05/28--------Start
				tmpStr  += "Plate 1 Temperature   = " + gpibFTAllTemp[1] + "\n";
				tmpStr  += "Plate 2 Temperature   = " + gpibFTAllTemp[2] + "\n";
				tmpStr  += "Shuttle 1 Temperature = " + gpibFTAllTemp[3] + "\n";
				tmpStr  += "Shuttle 2 Temperature = " + gpibFTAllTemp[4] + "\n";
				tmpStr  += "Head 1 Temperature    = " + gpibFTAllTemp[5] + "\n";
				tmpStr  += "Head 2 Temperature    = " + gpibFTAllTemp[6] + "\n";
				tmpStr  += "Head 3 Temperature    = " + gpibFTAllTemp[7] + "\n";
				tmpStr  += "Head 4 Temperature    = " + gpibFTAllTemp[8] + "\n";
				// Add by Cola. 2015/05/28--------End
				tmpStr  += "Handler Soaking Time  = " + gpibFTSoakTime + "\n";
				tmpStr  += "Handler Sites Map     = " + gpibFTSiteMap + "\n";
				tmpStr  += "Handler Alarm Setting = " + gpibFTAlarmSetup + "\n";  //Add by Cola. 2015/09/02
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				System.out.println(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);
				if(!barcode_SoaktimeStr.equals("") && !barcode_SoaktimeStr.equals("NA") && Float.parseFloat(barcode_temperatureStr) > 30) {
					//Soak Time can by pass three digits. 20150824
					if(gpibFTSoakTime.equals(barcode_SoaktimeStr) || gpibFTSoakTime.equals("0" + barcode_SoaktimeStr) || gpibFTSoakTime.equals("00" + barcode_SoaktimeStr) || (barcode_SoaktimeStr.equals("0") && gpibFTSoakTime.equals("NA")))
					{
						;
					}
					else {
						GpibFlagSoakTime = false;
						GpibFlag = false;
					}
				}
				if(!barcode_temperatureStr.equals("") && !barcode_temperatureStr.equals("NA")/*&&Float.parseFloat(barcode_temperatureStr)>30*/) {
					if(barcode_temperatureStr.equals(gpibFTTemp))
					{	
						;
					}
					else
					{   					
						GpibFlagTemp = false;
						GpibFlag = false;
					}
				}
				// For Restrict FT All Equipment Temperature by Cola. 2015/05/28--------Start
				try{
					double RunCardTemp;
					double ActualTemp;
					double TemperatureGap = 3;
					boolean NULLcheck[] = new boolean[9];
					RunCardTemp = Double.parseDouble(barcode_temperatureStr); //String to double format
					if(!barcode_temperatureStr.equals("") && !barcode_temperatureStr.equals("NA")/*&&Float.parseFloat(barcode_temperatureStr)>30*/) {
						if(RunCardTemp == 25) //If Temperature is Normal, Gap is 10 Add by Cola. 2015/07/20
							TemperatureGap = 10;
						for(int HW = 1 ; HW <= 8 ; HW++)
						{
							GpibFlagAllTemp[HW] = true;
							if(gpibFTAllTemp[HW].equals("NULL"))
							{
								NULLcheck[HW] = true;
							}
							else
							{
								NULLcheck[HW] = false;
								//    							JOptionPane.showMessageDialog(null, "2. gpibFTAllTemp " +HW+" = " + gpibFTAllTemp[HW]);
								gpibFTAllTemp[HW] = gpibFTAllTemp[HW].substring(1, gpibFTAllTemp[HW].length());
								ActualTemp = Double.parseDouble(gpibFTAllTemp[HW]);
								//    							JOptionPane.showMessageDialog(null, "ActualTemp = " + ActualTemp);
								if(ActualTemp > (RunCardTemp + TemperatureGap) || ActualTemp < (RunCardTemp - TemperatureGap))
								{
									GpibFlagAllTemp[HW] = false;
									GpibFlag = false;
								}
							}
						}
						if(NULLcheck[1] == true && NULLcheck[2] == true && RunCardTemp != 25 ) //Normal Temperature don't restrict Plate. by Cola 20160112
						{  
							JOptionPane.showMessageDialog(null, "Plate Temprature Error, Please Check Equipment Temperature!!");
							GpibFlagAllTemp[1] = false; GpibFlagAllTemp[2] = false;
							GpibFlag = false;
						}
						else if(NULLcheck[5] == true && NULLcheck[6] == true && NULLcheck[7] == true && NULLcheck[8] == true)
						{
							JOptionPane.showMessageDialog(null,"Head Temprature Error, Please Check Equipment Temperature!!");
							GpibFlagAllTemp[5] = false; GpibFlagAllTemp[6] = false; GpibFlagAllTemp[7] = false; GpibFlagAllTemp[8] = false;
							GpibFlag = false;
						}
					}
				}catch(Exception err){
					JOptionPane.showMessageDialog(null, "Restrict All Temperature Error Message: " + err);
				}
				// For Restrict FT All Equipment Temperature by Cola. 2015/05/28--------End

				//////////////////////////////////////    GpibFlagSiteMap
				String[] barcode_ProberLocation_array = barcode_locationCode.split(",");

				for (int i=0 ; i<barcode_ProberLocation_array.length ; i++){

					tmpStr  += "Barcode_ProberLocation" + i + ":" + barcode_ProberLocation_array[i] + "\n";    							
					if(barcode_ProberLocation_array[i].equals(gpibFTSiteMap) || barcode_duts.equals("1")/* || !barcode_ProberLocation_array[i].equals("")*/){ //20140627 gpib site map check

						barcode_locationCode = barcode_ProberLocation_array[i];
						FTSiteMapCounter = true;
					}/*else if(barcode_duts.equals("1") && barcode_ProberLocation_array[i].equals(gpibFTSiteMap)){
    					barcode_ProberLocation = barcode_ProberLocation_array[i];
    					FTSiteMapCounter = true;
    				}*/  //Single Site have to be Same with XML or not?
					else {   					
						GpibFlagSiteMap = false;
						GpibFlag = false;
					}					         								
				}    

				if (FTSiteMapCounter){//xml site map one pass is pass
					GpibFlagSiteMap = true;
					//			GpibFlag = true;									
				}
				///////////////////////////                


				//                if(!TesterSiteMapStr.equals("")) {	//20140114 FT GPIB check site map
				//                    if(TesterSiteMapStr.equals(gpibFTSiteMap) || barcode_duts.equals("1"))
				//                        ;
				//                    else{GpibFlagSiteMap
				//                        GpibFlagSiteMap=false;
				//                        GpibFlag = false;
				//                    }
				//                }

				// For Compare FT Site Difference and Bin Yield Alarm Setup by Cola. 2015/09/01
//				if(!barcode_testeridStr.equals("D10-31")){  //Temp by pass. It's ok now.
				if(!HandlerAlarmSetup_Command.equals("") && HandlerSW_sendGPIB.equals("NEWOI") && rtrbt[1].isSelected()){ //Temp Remove EQC
					//    			if(Enable_AlarmSetup == true && HandlerSW_sendGPIB.equals("NEWOI") && (rtrbt[1].isSelected() || rtrbt[3].isSelected())){ //Setting for A1 and EQC station only. by Cola. 20151210
					//    				if(!HandlerAlarmSetup_Command.equals("") && (!gpibFTAlarmSetup.equals("") || !gpibFTAlarmSetup.equals("CHKSETUP?"))) { // GPIB no return haven't to restrict
					if(gpibFTAlarmSetup.equals(HandlerAlarmSetup_Command)){	
						JOptionPane.showMessageDialog(null, "Checking AlarmSetup OK !!");
					}
					else{   					
						GpibFlagAlarmSetup = false;
						GpibFlag = false;
					}
					//    				}
				}
//				}
				//Call New Frame Add by Cola. 2015/06/05--------Start
				if(!GpibFlag){
					JFrame GpibCheck_frame = new JFrame("Gpib Check");
					if(GpibFlag)
						GpibCheck_lab = new JLabel("Gpib Check Result : PASS");
					else
					{
						GpibCheck_lab = new JLabel("Gpib Check Result : FAIL");
						GpibCheck_lab2[1][0] = new JLabel("PARAMETER \\ SOURCE");
						GpibCheck_lab2[1][1] = new JLabel("TESTER");
						GpibCheck_lab2[1][2] = new JLabel("GPIB");
						GpibCheck_lab2[2][0] = new JLabel("Temperature");
						GpibCheck_lab2[2][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[2][2] = new JLabel(gpibFTTemp);
						GpibCheck_lab2[3][0] = new JLabel("SoakingTime");
						GpibCheck_lab2[3][1] = new JLabel(barcode_SoaktimeStr);
						GpibCheck_lab2[3][2] = new JLabel(gpibFTSoakTime);
						GpibCheck_lab2[4][0] = new JLabel("SitesMap");
						GpibCheck_lab2[4][1] = new JLabel(barcode_locationCode); //Change TesterSiteMapStr to barcode_ProberLocation by Cola. 2015/05/28
						GpibCheck_lab2[4][2] = new JLabel(gpibFTSiteMap);
						GpibCheck_lab2[5][0] = new JLabel("Plate 1");
						GpibCheck_lab2[5][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[5][2] = new JLabel(gpibFTAllTemp[1]);
						GpibCheck_lab2[6][0] = new JLabel("Plate 2");
						GpibCheck_lab2[6][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[6][2] = new JLabel(gpibFTAllTemp[2]);
						GpibCheck_lab2[7][0] = new JLabel("Shuttle 1");
						GpibCheck_lab2[7][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[7][2] = new JLabel(gpibFTAllTemp[3]);
						GpibCheck_lab2[8][0] = new JLabel("Shuttle 2");
						GpibCheck_lab2[8][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[8][2] = new JLabel(gpibFTAllTemp[4]);
						GpibCheck_lab2[9][0] = new JLabel("Head 1");
						GpibCheck_lab2[9][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[9][2] = new JLabel(gpibFTAllTemp[5]);
						GpibCheck_lab2[10][0] = new JLabel("Head 2");
						GpibCheck_lab2[10][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[10][2] = new JLabel(gpibFTAllTemp[6]);
						GpibCheck_lab2[11][0] = new JLabel("Head 5");
						GpibCheck_lab2[11][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[11][2] = new JLabel(gpibFTAllTemp[7]);
						GpibCheck_lab2[12][0] = new JLabel("Head 6");
						GpibCheck_lab2[12][1] = new JLabel(barcode_temperatureStr);
						GpibCheck_lab2[12][2] = new JLabel(gpibFTAllTemp[8]);
						GpibCheck_lab2[13][0] = new JLabel("Alarm Setting");
						GpibCheck_lab2[13][1] = new JLabel(HandlerAlarmSetup_Command);
						GpibCheck_lab2[13][2] = new JLabel(gpibFTAlarmSetup);
						Container GpibCheck_c  = GpibCheck_frame.getContentPane();
						GpibCheck_c.setLayout(new GridLayout(13,1));
						for(int i = 1 ; i <= 13 ; i ++)
						{
							GpibCheck_p[i]= new JPanel();
							GpibCheck_p[i].setBackground(Color.WHITE);
							GpibCheck_p[i].setLayout(new GridLayout(1,3));
							GpibCheck_c.add(GpibCheck_p[i]);
							for(int j = 0 ; j <= 2 ; j++)
							{
								GpibCheck_lab2[i][j].setOpaque(true);
								GpibCheck_lab2[i][j].setHorizontalAlignment(JLabel.CENTER);

								GpibCheck_lab2[i][j].setFont(new Font("Courier New", Font.BOLD, 20));
								GpibCheck_lab2[i][j].setForeground(Color.BLACK);

								GpibCheck_p[i].add(GpibCheck_lab2[i][j]);

								if(i==1) 
									GpibCheck_lab2[i][j].setBackground(Color.WHITE);
								else if(j==0)
									GpibCheck_lab2[i][j].setBackground(Color.WHITE);

							}

						}
						GpibCheck_lab.setFont(new Font("Courier New", Font.BOLD, 20));
						GpibCheck_lab.setForeground(Color.BLACK);


						if(GpibFlag) GpibCheck_lab.setBackground(Color.GREEN);
						else GpibCheck_lab.setBackground(Color.RED);

						if(GpibFlagTemp) {
							GpibCheck_lab2[2][1].setBackground(Color.GREEN);
							GpibCheck_lab2[2][2].setBackground(Color.GREEN);
						}
						else{
							GpibCheck_lab2[2][1].setBackground(Color.RED);
							GpibCheck_lab2[2][2].setBackground(Color.RED);
						}

						GpibCheck_lab2[3][0].setBackground(Color.WHITE);
						if(GpibFlagSoakTime) {
							GpibCheck_lab2[3][1].setBackground(Color.GREEN);
							GpibCheck_lab2[3][2].setBackground(Color.GREEN);
						}
						else{
							GpibCheck_lab2[3][1].setBackground(Color.RED);
							GpibCheck_lab2[3][2].setBackground(Color.RED);
						}

						if(GpibFlagSiteMap) {
							GpibCheck_lab2[4][1].setBackground(Color.GREEN);
							GpibCheck_lab2[4][2].setBackground(Color.GREEN);
						}
						else{
							GpibCheck_lab2[4][1].setBackground(Color.RED);
							GpibCheck_lab2[4][2].setBackground(Color.RED);
						}
						//For All Equipment Temperature by Cola. 2015/06/05
						for(int HW = 1 ; HW<=8 ; HW++)
						{
							int index = HW + 4;
							if(GpibFlagAllTemp[HW]) {
								GpibCheck_lab2[index][1].setBackground(Color.GREEN);
								GpibCheck_lab2[index][2].setBackground(Color.GREEN);
							}
							else{
								GpibCheck_lab2[index][1].setBackground(Color.RED);
								GpibCheck_lab2[index][2].setBackground(Color.RED);
							}
						}    
						//For Alarm Setting by Cola. 2015/09/03
						if(GpibFlagAlarmSetup) {
							GpibCheck_lab2[13][1].setBackground(Color.GREEN);
							GpibCheck_lab2[13][2].setBackground(Color.GREEN);
						}
						else{
							GpibCheck_lab2[13][1].setBackground(Color.RED);
							GpibCheck_lab2[13][2].setBackground(Color.RED);
						}
						GpibCheck_frame.setSize(800,600);
						GpibCheck_frame.setLocation(300,100);
						GpibCheck_frame.setVisible(true);
						GpibCheck_frame.addWindowListener(this);					
					}
				}
				//Call New Frame Add by Cola. 2015/06/05--------End					

			}
			cmd = "/bin/rm /tmp/gpib_info.txt";     
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);
			
			if(status&&GpibFlag){
				//Smart Bin Tray Waring Message 20171012-----Start
				if(gpibFTType.indexOf("NS") == -1){
					if(Smartbintray_mode == "FT"){
						tmpStr = "+======================================================================================+      \n";
						tmpStr += "|\n";
						tmpStr += "|\n";
						tmpStr += "|                                                 <Warning> Now Operation Station: FT Or RT1 \n";
						tmpStr += "|                                                       All Bin Mode only Pass & Fail \n";
						tmpStr += "|                        Please Check Handler Screen Setting and Handler actually Setting	 \n";
						tmpStr += "|                                                               Are They Correct?	 \n";
						tmpStr += "|\n";
						tmpStr += "|\n";
						tmpStr += "+======================================================================================+      \n";
						if(JOptionPane.showConfirmDialog(null, tmpStr, "confirmation?", JOptionPane.YES_NO_OPTION)== JOptionPane.NO_OPTION){

							tmpStr = "+===================================================================================+      \n";
							tmpStr += "|\n";
							tmpStr += "|\n";
							tmpStr += "|                                             <Warning> Now Operation Station: FT Or RT1 \n";             
							tmpStr += "|                   Please call EQU Check Handler Screen setting and Handler setting\n";
							tmpStr += "|                                                                  Unload OI now!\n";
							tmpStr += "|\n";
							tmpStr += "|\n";
							tmpStr += "+==================================================================================+      \n";
							JOptionPane.showMessageDialog(null, tmpStr);
							killproc(); System.exit(1);
						}
					}
					else if(Smartbintray_mode == "RT"){
						tmpStr = "+======================================================================================+      \n";
						tmpStr += "|\n";
						tmpStr += "|\n";
						tmpStr += "|                                                 <Warning> Now Operation Station: RT \n";
						tmpStr += "|                                                       All Bin mode Split all Binning \n";
						tmpStr += "|                        Please Check Handler Screen Setting and Handler actually Setting 	 \n";
						tmpStr += "|                                                               Are They Correct?	 \n";
						tmpStr += "|\n";
						tmpStr += "|\n";
						tmpStr += "+======================================================================================+      \n";
						if(JOptionPane.showConfirmDialog(null, tmpStr, "confirmation?", JOptionPane.YES_NO_OPTION)== JOptionPane.NO_OPTION){

							tmpStr = "+===================================================================================+      \n";
							tmpStr += "|\n";
							tmpStr += "|\n";
							tmpStr += "|                                             <Warning> Now Operation Station: RT \n";             
							tmpStr += "|                  Please call EQU Check Handler Screen setting and Handler setting\n";
							tmpStr += "|                                                                  Unload OI now!\n";
							tmpStr += "|\n";
							tmpStr += "|\n";
							tmpStr += "+==================================================================================+      \n";
							JOptionPane.showMessageDialog(null, tmpStr);
							killproc(); System.exit(1);
						}
					}
				}
				//Smart Bin Tray Waring Message 20171012-----End

				return true;
			}else {
				tmpStr = " | Gpib Check fail, please call the Supervisor | \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				saveErrorMessageRealtime("Msg:FT gpib check is fail");
				killproc(); System.exit(1);
				return false;
			} 
		}
		
		else 
			return true;
	}

	public boolean gpibCPCheck() {//20110614

		boolean status=true;
		boolean GpibFlag=false;
		boolean GpibProberTemp=true;
		boolean GpibProberLotNum=true;

		if (testTypeStr.equalsIgnoreCase("Wafer")){
			String tmpStr="";
			String cmd;
			String infileStr;
			cmd = "/bin/rm /tmp/gpib_CP_info.txt";     
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			cmd = csicAutoPath + "/bin/DumpTSKSetting.exe";
			tmpStr = cmd + "\n\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			//init
			gpibCPTemp = "";
			gpibCPLotNum = "";

			BufferedReader gpibBr;
			infileStr = "/tmp/gpib_CP_info.txt";
			try {
				gpibBr = new BufferedReader(new FileReader(infileStr));//open file

				while ((tmpStr = gpibBr.readLine()) != null) {

					System.out.print(tmpStr + "\n"); 
					System.out.print(tmpStr + "\n"); 
					System.out.print(tmpStr + "\n"); 


					if (tmpStr.length()!=0) {
						processgpibProberContent(tmpStr);
					}
				}       

				gpibBr.close(); // close file
			} catch (FileNotFoundException event) {

				status = false;

				tmpStr  = "<Exception> getGpibInfo: " + infileStr + " is NOT Found !\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "<Exception> getGpibInfo: " + infileStr + " is NOT Found !\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:CP gpib file is not found");
				killproc(); System.exit(1); // to quit Java app for Linux

			} catch (java.io.IOException err) {
				status = false;
				tmpStr = "<Exception> getGpibLInfo: " + err + "\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr = "<Exception> getGpibLInfo: " + err + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:CP gpib file is not found");
				killproc(); System.exit(1); // to quit Java app for Linux
			}
			if(false){
				//if(gpibFlag){ // 

				//boolean dotFlag = false;
				//if(barcode_temperatureStr.indexOf(".")!=-1) {
				//    dotFlag = true;
				//}
				String tempStr = "";
				String barcodetempStr = Double.toString(Float.parseFloat(barcode_temperatureStr)) ;
				//if(dotFlag){
				if(Float.parseFloat(barcode_temperatureStr)>30){
					tmpStr  = "Prober Temperature         = " + Float.parseFloat(gpibCPTemp.substring(5,9))*0.1 + "\n";                    
					tempStr = Double.toString(Float.parseFloat(gpibCPTemp.substring(5,9))*0.1);
				}
				else{
					tmpStr  = "Prober Temperature         = " + "Under 30.0" + "\n";                    
					tempStr = "Under 30.0";
				}
				//}
				//else{
				//    if(Integer.parseInt(barcode_temperatureStr)>30) {
				//        tmpStr  = "Prober Temperature         = " + Float.parseFloat(gpibCPTemp.substring(5,9))*0.1 + "\n";                    
				//        tempStr = Double.toString(Float.parseFloat(gpibCPTemp.substring(5,9))*0.1);
				//    }
				//    else{
				//        tmpStr  = "Prober Temperature         = " + "Under 30" + "\n";                    
				//        tempStr = "Under 30.0";
				//    }
				//}
				//tmpStr  = "Prober Temperature         = " + gpibCPTemp + "\n";
				tmpStr  += "Prober Lot Number  = " + gpibCPLotNum + "\n";
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				System.out.println(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);                

				if(Float.parseFloat(barcode_temperatureStr)>30) {
					double gpibCPTempValue = Float.parseFloat(gpibCPTemp.substring(5,9))*0.1;
					double barcodeTempValue = Float.parseFloat(barcode_temperatureStr);
					if(gpibCPTempValue==barcodeTempValue)
						;
					else {
						GpibProberTemp = false;
						GpibFlag = false;
					}
				}

				if(barcode_lotidStr.equals(gpibCPLotNum)||barcode_CorrlotidStr.equals(gpibCPLotNum))
					;
				else {
					GpibProberLotNum = false;
					GpibFlag = false;
				}

				//Call Frame
				if(!GpibFlag){
					JFrame GpibCheck_CP_frame = new JFrame("Gpib Check");
					if(GpibFlag)
						GpibCheck_CP_lab = new JLabel("Gpib Check Result : PASS");
					else
						GpibCheck_CP_lab = new JLabel("Gpib Check Result : FAIL");
					GpibCheck_CP_lab20 = new JLabel("PARAMETER \\ SOURCE");
					GpibCheck_CP_lab21 = new JLabel("TESTER");
					GpibCheck_CP_lab22 = new JLabel("GPIB");
					GpibCheck_CP_lab30 = new JLabel("Temperature");
					GpibCheck_CP_lab31 = new JLabel(barcodetempStr);
					GpibCheck_CP_lab32 = new JLabel(tempStr);
					GpibCheck_CP_lab40 = new JLabel("Lot Number");
					GpibCheck_CP_lab41 = new JLabel(barcode_lotidStr);
					GpibCheck_CP_lab42 = new JLabel(gpibCPLotNum);

					GpibCheck_CP_lab20.setOpaque(true);
					GpibCheck_CP_lab21.setOpaque(true);
					GpibCheck_CP_lab22.setOpaque(true);
					GpibCheck_CP_lab30.setOpaque(true);
					GpibCheck_CP_lab31.setOpaque(true);
					GpibCheck_CP_lab32.setOpaque(true);
					GpibCheck_CP_lab40.setOpaque(true);
					GpibCheck_CP_lab41.setOpaque(true);
					GpibCheck_CP_lab42.setOpaque(true);

					GpibCheck_CP_lab20.setHorizontalAlignment(JLabel.CENTER);
					GpibCheck_CP_lab21.setHorizontalAlignment(JLabel.CENTER);
					GpibCheck_CP_lab22.setHorizontalAlignment(JLabel.CENTER);
					GpibCheck_CP_lab30.setHorizontalAlignment(JLabel.CENTER);
					GpibCheck_CP_lab31.setHorizontalAlignment(JLabel.CENTER);
					GpibCheck_CP_lab32.setHorizontalAlignment(JLabel.CENTER);
					GpibCheck_CP_lab40.setHorizontalAlignment(JLabel.CENTER);
					GpibCheck_CP_lab41.setHorizontalAlignment(JLabel.CENTER);
					GpibCheck_CP_lab42.setHorizontalAlignment(JLabel.CENTER);
					Button GpibCheck_frame_CP_bt = new Button("OK");
					Container GpibCheck_error_CP_c = new Container();
					JPanel GpibCheck_CP_p1= new JPanel();
					JPanel GpibCheck_CP_p2= new JPanel();
					JPanel GpibCheck_CP_p3= new JPanel();
					JPanel GpibCheck_CP_p4= new JPanel();

					Container GpibCheck_CP_c  = GpibCheck_CP_frame.getContentPane();
					GpibCheck_CP_c.setLayout(new GridLayout(4,1));

					GpibCheck_CP_c.setBackground(Color.WHITE);
					GpibCheck_CP_p1.setBackground(Color.WHITE);
					GpibCheck_CP_p2.setBackground(Color.WHITE);
					GpibCheck_CP_p3.setBackground(Color.WHITE);
					GpibCheck_CP_p4.setBackground(Color.WHITE);
					//GpibCheck_p6.setBackground(Color.WHITE);

					GpibCheck_CP_p1.setLayout(new GridLayout(1,3));
					GpibCheck_CP_p2.setLayout(new GridLayout(1,3));
					GpibCheck_CP_p3.setLayout(new GridLayout(1,3));
					GpibCheck_CP_p4.setLayout(new GridLayout(1,3));

					GpibCheck_CP_c.add(GpibCheck_CP_p1);
					GpibCheck_CP_c.add(GpibCheck_CP_p2);
					GpibCheck_CP_c.add(GpibCheck_CP_p3);
					GpibCheck_CP_c.add(GpibCheck_CP_p4);
					//GpibCheck_c.add(GpibCheck_p5);

					GpibCheck_CP_lab.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab20.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab21.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab22.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab30.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab31.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab32.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab40.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab41.setFont(new Font("Courier New", Font.BOLD, 20));
					GpibCheck_CP_lab42.setFont(new Font("Courier New", Font.BOLD, 20));


					GpibCheck_CP_lab.setForeground(Color.BLACK);
					GpibCheck_CP_lab20.setForeground(Color.BLACK);
					GpibCheck_CP_lab21.setForeground(Color.BLACK);
					GpibCheck_CP_lab22.setForeground(Color.BLACK);
					GpibCheck_CP_lab30.setForeground(Color.BLACK);
					GpibCheck_CP_lab31.setForeground(Color.BLACK);
					GpibCheck_CP_lab32.setForeground(Color.BLACK);
					GpibCheck_CP_lab40.setForeground(Color.BLACK);
					GpibCheck_CP_lab41.setForeground(Color.BLACK);
					GpibCheck_CP_lab42.setForeground(Color.BLACK);
					//

					if(GpibFlag) GpibCheck_CP_lab.setBackground(Color.GREEN);
					else GpibCheck_CP_lab.setBackground(Color.RED);

					GpibCheck_CP_lab20.setBackground(Color.WHITE);
					GpibCheck_CP_lab21.setBackground(Color.WHITE);
					GpibCheck_CP_lab22.setBackground(Color.WHITE);

					GpibCheck_CP_lab30.setBackground(Color.WHITE);
					if(GpibProberTemp) {
						GpibCheck_CP_lab31.setBackground(Color.GREEN);
						GpibCheck_CP_lab32.setBackground(Color.GREEN);
					}
					else{
						GpibCheck_CP_lab31.setBackground(Color.RED);
						GpibCheck_CP_lab32.setBackground(Color.RED);
					}

					GpibCheck_CP_lab40.setBackground(Color.WHITE);
					if(GpibProberLotNum) {
						GpibCheck_CP_lab41.setBackground(Color.GREEN);
						GpibCheck_CP_lab42.setBackground(Color.GREEN);
					}
					else{
						GpibCheck_CP_lab41.setBackground(Color.RED);
						GpibCheck_CP_lab42.setBackground(Color.RED);
					}

					GpibCheck_CP_p1.add(GpibCheck_CP_lab20);
					GpibCheck_CP_p1.add(GpibCheck_CP_lab21);
					GpibCheck_CP_p1.add(GpibCheck_CP_lab22);
					GpibCheck_CP_p2.add(GpibCheck_CP_lab30);
					GpibCheck_CP_p2.add(GpibCheck_CP_lab31);
					GpibCheck_CP_p2.add(GpibCheck_CP_lab32);
					GpibCheck_CP_p3.add(GpibCheck_CP_lab40);
					GpibCheck_CP_p3.add(GpibCheck_CP_lab41);
					GpibCheck_CP_p3.add(GpibCheck_CP_lab42);

					GpibCheck_CP_frame.setSize(1000,200);
					GpibCheck_CP_frame.setLocation(300,250);
					GpibCheck_CP_frame.setVisible(true);
					GpibCheck_CP_frame.addWindowListener(this);

				}
			}
			cmd = "/bin/rm /tmp/gpib_CP_info.txt";     
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);
			//            if(status&&GpibFlag) ChiaHui 20140625
			if(true)
				return true;
			else {
				tmpStr = " | Gpib Check fail, please call the Supervisor | \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				saveErrorMessageRealtime("Msg:CP gpib check is fail");
				killproc(); System.exit(1);
				return false;
			}
		}
		else 
			return true;
	}
	/*    
    public boolean gpibCPCheck_TEL() {//20110614

        boolean status=true;
        boolean GpibFlag=true;
        boolean GpibProberTemp=true;
        boolean GpibProberLotNum=true;

        if (testTypeStr.equalsIgnoreCase("Wafer")){
            String tmpStr="";
            String cmd;
            String infileStr;
            cmd = "/bin/rm /tmp/gpib_CP_info.txt";     
            tmpStr = cmd + "\n";
            System.out.println(tmpStr); 
            textArea2.append(tmpStr);
            saveMessageRealtime(tmpStr);
            javaExecSystemCmd(cmd);

            cmd = csicAutoPath + "/bin/gpib_CP_TEL";
            tmpStr = cmd + "\n\n";
            System.out.print(tmpStr); 
            textArea2.append(tmpStr);
            saveMessageRealtime(tmpStr);
            javaExecSystemCmd(cmd);

            //init
            gpibCPTemp = "";
            gpibCPLotNum = "";

            BufferedReader gpibBr;
            infileStr = "/tmp/gpib_CP_info.txt";
            try {
                gpibBr = new BufferedReader(new FileReader(infileStr));//open file

                while ((tmpStr = gpibBr.readLine()) != null) {

                    System.out.print(tmpStr + "\n"); 
                    System.out.print(tmpStr + "\n"); 
                    System.out.print(tmpStr + "\n"); 


                    if (tmpStr.length()!=0) {
                        processgpibProberContent(tmpStr);
                    }
                }       

                gpibBr.close(); // close file
            } catch (FileNotFoundException event) {

                status = false;

                tmpStr  = "<Exception> getGpibInfo: " + infileStr + " is NOT Found !\n";
                tmpStr += "+----------------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+----------------------------+ \n";
                System.out.println(tmpStr);

                tmpStr  = "<Exception> getGpibInfo: " + infileStr + " is NOT Found !\n";
                tmpStr += "+--------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+--------------------+ \n";
                JOptionPane.showMessageDialog(null, tmpStr);
                autoSaveMessageBeforeExit();
                saveErrorMessageRealtime("Msg:CP gpib file is not found");
                killproc(); System.exit(1); // to quit Java app for Linux

            } catch (java.io.IOException err) {
                status = false;
                tmpStr = "<Exception> getGpibLInfo: " + err + "\n";
                tmpStr += "+----------------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+----------------------------+ \n";
                System.out.println(tmpStr);

                tmpStr = "<Exception> getGpibLInfo: " + err + "\n";
                tmpStr += "+--------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+--------------------+ \n";
                JOptionPane.showMessageDialog(null, tmpStr);
                autoSaveMessageBeforeExit();
                saveErrorMessageRealtime("Msg:CP gpib file is not found");
                killproc(); System.exit(1); // to quit Java app for Linux
            }

            if(gpibFlag){ // 

                //boolean dotFlag = false;
                //if(barcode_temperatureStr.indexOf(".")!=-1) {
                //    dotFlag = true;
                //}
                String tempStr = "";
                String barcodetempStr = Double.toString(Float.parseFloat(barcode_temperatureStr)) ;
                //if(dotFlag){
                    if(Float.parseFloat(barcode_temperatureStr)>30){
                        tmpStr  = "Prober Temperature         = " + Float.parseFloat(gpibCPTemp.substring(5,9))*0.1 + "\n";                    
                        tempStr = Double.toString(Float.parseFloat(gpibCPTemp.substring(5,9))*0.1);
                    }
                    else{
                        tmpStr  = "Prober Temperature         = " + "Under 30.0" + "\n";                    
                        tempStr = "Under 30.0";
                    }
                //}
                //else{
                //    if(Integer.parseInt(barcode_temperatureStr)>30) {
                //        tmpStr  = "Prober Temperature         = " + Float.parseFloat(gpibCPTemp.substring(5,9))*0.1 + "\n";                    
                //        tempStr = Double.toString(Float.parseFloat(gpibCPTemp.substring(5,9))*0.1);
                //    }
                //    else{
                //        tmpStr  = "Prober Temperature         = " + "Under 30" + "\n";                    
                //        tempStr = "Under 30.0";
                //    }
                //}
                //tmpStr  = "Prober Temperature         = " + gpibCPTemp + "\n";
                tmpStr  += "Prober Lot Number  = " + gpibCPLotNum + "\n";
                textArea2.append(tmpStr);
                saveMessageRealtime(tmpStr);
                System.out.println(tmpStr);
                JOptionPane.showMessageDialog(null, tmpStr);                

                if(Float.parseFloat(barcode_temperatureStr)>30) {
                    double gpibCPTempValue = Float.parseFloat(gpibCPTemp.substring(5,9))*0.1;
                    double barcodeTempValue = Float.parseFloat(barcode_temperatureStr);
                    if(gpibCPTempValue==barcodeTempValue)
                        ;
                    else {
                        GpibProberTemp = false;
                        GpibFlag = false;
                    }
                }

                if(barcode_lotidStr.equals(gpibCPLotNum)||barcode_CorrlotidStr.equals(gpibCPLotNum))
                    ;
                else {
                    GpibProberLotNum = false;
                    GpibFlag = false;
                }

                //Call Frame
                if(!GpibFlag){
                    JFrame GpibCheck_CP_frame = new JFrame("Gpib Check");
                    if(GpibFlag)
                        GpibCheck_CP_lab = new JLabel("Gpib Check Result : PASS");
                    else
                        GpibCheck_CP_lab = new JLabel("Gpib Check Result : FAIL");
                    GpibCheck_CP_lab20 = new JLabel("PARAMETER \\ SOURCE");
                    GpibCheck_CP_lab21 = new JLabel("TESTER");
                    GpibCheck_CP_lab22 = new JLabel("GPIB");
                    GpibCheck_CP_lab30 = new JLabel("Temperature");
                    GpibCheck_CP_lab31 = new JLabel(barcodetempStr);
                    GpibCheck_CP_lab32 = new JLabel(tempStr);
                    GpibCheck_CP_lab40 = new JLabel("Lot Number");
                    GpibCheck_CP_lab41 = new JLabel(barcode_lotidStr);
                    GpibCheck_CP_lab42 = new JLabel(gpibCPLotNum);

                    GpibCheck_CP_lab20.setOpaque(true);
                    GpibCheck_CP_lab21.setOpaque(true);
                    GpibCheck_CP_lab22.setOpaque(true);
                    GpibCheck_CP_lab30.setOpaque(true);
                    GpibCheck_CP_lab31.setOpaque(true);
                    GpibCheck_CP_lab32.setOpaque(true);
                    GpibCheck_CP_lab40.setOpaque(true);
                    GpibCheck_CP_lab41.setOpaque(true);
                    GpibCheck_CP_lab42.setOpaque(true);

                    GpibCheck_CP_lab20.setHorizontalAlignment(JLabel.CENTER);
                    GpibCheck_CP_lab21.setHorizontalAlignment(JLabel.CENTER);
                    GpibCheck_CP_lab22.setHorizontalAlignment(JLabel.CENTER);
                    GpibCheck_CP_lab30.setHorizontalAlignment(JLabel.CENTER);
                    GpibCheck_CP_lab31.setHorizontalAlignment(JLabel.CENTER);
                    GpibCheck_CP_lab32.setHorizontalAlignment(JLabel.CENTER);
                    GpibCheck_CP_lab40.setHorizontalAlignment(JLabel.CENTER);
                    GpibCheck_CP_lab41.setHorizontalAlignment(JLabel.CENTER);
                    GpibCheck_CP_lab42.setHorizontalAlignment(JLabel.CENTER);
                    Button GpibCheck_frame_CP_bt = new Button("OK");
                    Container GpibCheck_error_CP_c = new Container();
                    JPanel GpibCheck_CP_p1= new JPanel();
                    JPanel GpibCheck_CP_p2= new JPanel();
                    JPanel GpibCheck_CP_p3= new JPanel();
                    JPanel GpibCheck_CP_p4= new JPanel();

                    Container GpibCheck_CP_c  = GpibCheck_CP_frame.getContentPane();
                    GpibCheck_CP_c.setLayout(new GridLayout(4,1));

                    GpibCheck_CP_c.setBackground(Color.WHITE);
                    GpibCheck_CP_p1.setBackground(Color.WHITE);
                    GpibCheck_CP_p2.setBackground(Color.WHITE);
                    GpibCheck_CP_p3.setBackground(Color.WHITE);
                    GpibCheck_CP_p4.setBackground(Color.WHITE);
                    //GpibCheck_p6.setBackground(Color.WHITE);

                    GpibCheck_CP_p1.setLayout(new GridLayout(1,3));
                    GpibCheck_CP_p2.setLayout(new GridLayout(1,3));
                    GpibCheck_CP_p3.setLayout(new GridLayout(1,3));
                    GpibCheck_CP_p4.setLayout(new GridLayout(1,3));

                    GpibCheck_CP_c.add(GpibCheck_CP_p1);
                    GpibCheck_CP_c.add(GpibCheck_CP_p2);
                    GpibCheck_CP_c.add(GpibCheck_CP_p3);
                    GpibCheck_CP_c.add(GpibCheck_CP_p4);
                    //GpibCheck_c.add(GpibCheck_p5);

                    GpibCheck_CP_lab.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab20.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab21.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab22.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab30.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab31.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab32.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab40.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab41.setFont(new Font("Courier New", Font.BOLD, 20));
                    GpibCheck_CP_lab42.setFont(new Font("Courier New", Font.BOLD, 20));


                    GpibCheck_CP_lab.setForeground(Color.BLACK);
                    GpibCheck_CP_lab20.setForeground(Color.BLACK);
                    GpibCheck_CP_lab21.setForeground(Color.BLACK);
                    GpibCheck_CP_lab22.setForeground(Color.BLACK);
                    GpibCheck_CP_lab30.setForeground(Color.BLACK);
                    GpibCheck_CP_lab31.setForeground(Color.BLACK);
                    GpibCheck_CP_lab32.setForeground(Color.BLACK);
                    GpibCheck_CP_lab40.setForeground(Color.BLACK);
                    GpibCheck_CP_lab41.setForeground(Color.BLACK);
                    GpibCheck_CP_lab42.setForeground(Color.BLACK);
                          //

                    if(GpibFlag) GpibCheck_CP_lab.setBackground(Color.GREEN);
                    else GpibCheck_CP_lab.setBackground(Color.RED);

                    GpibCheck_CP_lab20.setBackground(Color.WHITE);
                    GpibCheck_CP_lab21.setBackground(Color.WHITE);
                    GpibCheck_CP_lab22.setBackground(Color.WHITE);

                    GpibCheck_CP_lab30.setBackground(Color.WHITE);
                    if(GpibProberTemp) {
                        GpibCheck_CP_lab31.setBackground(Color.GREEN);
                        GpibCheck_CP_lab32.setBackground(Color.GREEN);
                    }
                    else{
                        GpibCheck_CP_lab31.setBackground(Color.RED);
                        GpibCheck_CP_lab32.setBackground(Color.RED);
                    }

                    GpibCheck_CP_lab40.setBackground(Color.WHITE);
                    if(GpibProberLotNum) {
                        GpibCheck_CP_lab41.setBackground(Color.GREEN);
                        GpibCheck_CP_lab42.setBackground(Color.GREEN);
                    }
                    else{
                        GpibCheck_CP_lab41.setBackground(Color.RED);
                        GpibCheck_CP_lab42.setBackground(Color.RED);
                    }

                    GpibCheck_CP_p1.add(GpibCheck_CP_lab20);
                    GpibCheck_CP_p1.add(GpibCheck_CP_lab21);
                    GpibCheck_CP_p1.add(GpibCheck_CP_lab22);
                    GpibCheck_CP_p2.add(GpibCheck_CP_lab30);
                    GpibCheck_CP_p2.add(GpibCheck_CP_lab31);
                    GpibCheck_CP_p2.add(GpibCheck_CP_lab32);
                    GpibCheck_CP_p3.add(GpibCheck_CP_lab40);
                    GpibCheck_CP_p3.add(GpibCheck_CP_lab41);
                    GpibCheck_CP_p3.add(GpibCheck_CP_lab42);

                    GpibCheck_CP_frame.setSize(1000,200);
                    GpibCheck_CP_frame.setLocation(300,250);
                    GpibCheck_CP_frame.setVisible(true);
                    GpibCheck_CP_frame.addWindowListener(this);

                }
            }
            cmd = "/bin/rm /tmp/gpib_CP_info.txt";     
            tmpStr = cmd + "\n";
            System.out.println(tmpStr); 
            textArea2.append(tmpStr);
            saveMessageRealtime(tmpStr);
            javaExecSystemCmd(cmd);
            if(status&&GpibFlag) 
                return true;
            else {
                tmpStr = " | Gpib Check fail, please call the Supervisor | \n";
                JOptionPane.showMessageDialog(null, tmpStr);
                saveErrorMessageRealtime("Msg:CP gpib check is fail");
                killproc(); System.exit(1);
                return false;
            }
        }
        else 
            return true;
    }    
	 */    
	public void rmCmdFile() {
		String cmd = "";

		//cmd = "rm -f " + JobPathStr + "cmd_script.dmd";
		//System.out.print(cmd + "\n"); 
		//textArea2.append(cmd + "\n");
		//javaExecSystemCmd(cmd);

		//cmd = "rm -f " + autoResultPath + "cmd_script.dmd";
		cmd = "rm -f " + LocalPath + "cmd_script.dmd";//20091103
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);

		//cmd = "rm -f " + autoResultPath + "execDmd";
		cmd = "rm -f " + LocalPath + "execDmd"; //20091103

		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);
	}



	public void saveMessage(String inStr) {

		int yesORno = 0;
		String tmpStr = null;

		//tmpStr = "Do you want to save the Message block ?";
		//yesORno = JOptionPane.showConfirmDialog(null, "save Message ? ", tmpStr, JOptionPane.YES_NO_OPTION);
		//
		//if (yesORno==0) {
		if (true) {
			try {

				Date myDateTime = new Date();
				//startTime = thisDate.getTime();
				//or startTime = thisDate.toString();
				//startTime = Tue May 16 16:11:33 CST 2006
				myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
				mydatetime = myformatter.format(myDateTime);

				outfStr = messagePath + "dmdauto_msg_" + mydatetime + ".txt";
				outf = new File(outfStr);
				FileWriter myFW;

				myFW = new FileWriter(outf, false);
				myFW.write(inStr);
				myFW.close();

				tmpStr = "save Message to " + outfStr + "\n";
				textArea2.append(tmpStr);
				//JOptionPane.showMessageDialog(null, tmpStr);

			} catch (java.io.IOException err) {
				tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
				System.err.println(tmpStr);
				textArea2.append(tmpStr);
			}

		} else {
			tmpStr = "saveMessage: Without save Message !\n";
			System.out.println(tmpStr);
			textArea2.append(tmpStr);
			//JOptionPane.showMessageDialog(null, tmpStr);
		}
	}


	public static void saveMessageOK(String inStr) {

		String tmpStr = "";

		try {

			Date myDateTime = new Date();
			//startTime = thisDate.getTime();
			//or startTime = thisDate.toString();
			//startTime = Tue May 16 16:11:33 CST 2006
			myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
			mydatetime = myformatter.format(myDateTime);

			outfStr = messagePath + "dmdauto_msg_" + mydatetime + ".txt";
			outf = new File(outfStr);
			FileWriter myFW;

			myFW = new FileWriter(outf, false);
			myFW.write(inStr);
			myFW.close();

			tmpStr = "save Message to " + outfStr + "\n";
			textArea2.append(tmpStr);
			//JOptionPane.showMessageDialog(null, tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
		}
	}


	public static String getDateTime() {

		String tmpStr = "";
		String datetimeStr = "";

		Date myDateTime = new Date();

		myformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		datetimeStr = myformatter.format(myDateTime);

		return datetimeStr;
	}

	public static String getDateTime2() {

		String tmpStr = "";
		String datetimeStr = "";
		
		Date myDateTime = new Date();

		myformatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
		datetimeStr = myformatter.format(myDateTime);

		return datetimeStr;
	}

	public static String getDateTime3() {   //2009413

		String tmpStr = "";
		String datetimeStr = "";

		Date myDateTime = new Date();
		//startTime = thisDate.getTime();
		//or startTime = thisDate.toString();
		//startTime = Tue May 16 16:11:33 CST 2006
		myformatter = new SimpleDateFormat("yyyyMMddHHmmss");
		datetimeStr = myformatter.format(myDateTime);
//		JOptionPane.showMessageDialog(null, "Date1:" + datetimeStr);
		
		//20171226
		myformatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
		Summary_LOT_START_TIME = myformatter.format(myDateTime);
//		JOptionPane.showMessageDialog(null, "Summary_LOT_START_TIME:" + Summary_LOT_START_TIME);
		
//		//20171011
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(myDateTime);
//	    cal.add(Calendar.HOUR_OF_DAY,12);
//	    datetimeStr = myformatter.format(cal.getTime());
//	    JOptionPane.showMessageDialog(null, "Date2:" + datetimeStr);
	    
		return datetimeStr;
	}
	// ScrollBar Event
	public void adjustmentValueChanged(AdjustmentEvent ae) {
		//if (ae.getSource()==sb1) {
		//} else if (ae.getSource()==sb2) {
		//}
	}

	// Document Event
	public void insertUpdate(DocumentEvent de) {

		textArea2.setCaretPosition(de.getDocument().getLength());

	}
	public void removeUpdate(DocumentEvent de) {}
	public void changedUpdate(DocumentEvent de) {}

	// Window Event
	public void windowClosing(WindowEvent e) {
		//runExit();
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
		if(init_flag==false) { //20081021
			primaryHeader();
			showVersion_init();
			init_flag= true;
		}
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	// Mouse Event
	public void mouseDragged(MouseEvent me) {}

	public void mouseMoved(MouseEvent me) {}

	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see     java.lang.Thread#run()
	 */
	public void run() {}

	// Keyboard event
	public void keyPressed(KeyEvent ke) {}


	public void keyTyped(KeyEvent ke) {}

	public void keyReleased(KeyEvent ke) {

		String tmpStr = "";
		String tmpfileStr = "";//20081107
		int key = ke.getKeyCode();
		if (key==KeyEvent.VK_ENTER) {

		}


	}


	//20080529 update
	public void itemStateChanged(ItemEvent ie) {

		String tmpStr = "";
		int state_flag = 0;//if = 1 represent action
		btCnt++;

		myRBT = ie.getSource();

		if (true) { //20081231

			if(myRBT==null) {
				;
			} else if (myRBT==rtrbt[1]){ //20080724//20080826 //20081021
				tmpStr = "\n___" + btCnt + "___ RT Station is -A1- ...\n";
//				RTFlag = false;
				RTStr = "A1";
				state_flag = 1;
				NO_Corr_Reason = "";

				if (rtrbt[1].isSelected()) {
					if(checkCORR_num==0) {
						CallCheckCorrFrame(); //20150303 by ChiaHui
						checkCORR_num = 1 ;
					}
					else{
						checkCorr_bt[0].setSelected(true); //default. by Cola. 20151209
						checkCorr_frame.setVisible(true);
					}
					tmpStr += "\n___" + btCnt + "___ check corr status is -" + checkCorrStr_set+"- ...\n";                  
				}		
			} else if (myRBT==rtrbt[2]) {  //20100428
				tmpStr = "\n___" + btCnt + "___ RT Station is -RT- ...\n";
//				RTFlag = true;
				RTStr = "RT";
				state_flag = 1;
				if (rtrbt[2].isSelected()){
					if(RT_num==0) {
						CallRTFrame();
						Enable_XML_RTbin(); //Add by Cola. 20151215
						RTNumStr = RT_btname[0];
						RTNumStr2 = "RT1";
						RT_num = 1 ;
					}
					else {
						RT_frame.setVisible(true);
						Enable_XML_RTbin(); //Add by Cola. 20151215
					}
					//btCnt ++;  
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+ RTNumStr +"- ...\n";

				}
			} else if (myRBT==rtrbt[3]){ //20081104 20100428
				tmpStr = "\n___" + btCnt + "___ RT Station is -EQC- ...\n";
//				RTFlag = true;
				RTStr = "EQC";
				state_flag = 1;
				//20081219 call EQC bin frame
				if (rtrbt[3].isSelected()){
					if(EQC_num==0) {
						CallEqcFrame();
						EQC_num = 1 ;
					}
					else
						EQC_frame.setVisible(true);
					//btCnt ++;  
					tmpStr += "\n___" + btCnt + "___ Selected EQC bin is -"+EQCStr_bin_set+"- ...\n";
					tmpStr += "___" + btCnt + "___ Selected EQC Qbin is -"+EQCStr_qbin_set+"- ...\n";
					//EQCStr_qbin_set = "B1";
					//tmpStr += "___" + btCnt + "___ NOW barcode_stationStr = " + barcode_stationStr + "\n"; 
					//System.out.print(tmpStr); 
					//textArea2.append(tmpStr);

				}
			} else if (myRBT==rtrbt[4]) {//20081219
				tmpStr = "\n___" + btCnt + "___ RT Station is -CORR- ...\n";
//				RTFlag = true;
				RTStr = "CORR";
				state_flag = 1;
				if (rtrbt[4].isSelected()) {
					if(CORR_num==0) {
						CallCorrFrame();
						CORR_num = 1 ;
					}
					else
						CORR_frame.setVisible(true);
					//btCnt ++;
					tmpStr += "\n___" + btCnt + "___ Selected CORR type is -"+CORRStr_set+"- ...\n";
					//CORRStr_set = CORR_btname[0];
					//tmpStr += "___" + btCnt + "___ NOW barcode_stationStr = " + barcode_stationStr + "\n"; 
					//System.out.print(tmpStr); 
					//textArea2.append(tmpStr);

				}
			} else if (myRBT==rtrbt[5]) {//20081219
				tmpStr = "\n___" + btCnt + "___ RT Station is -HW- ...\n";
//				RTFlag = true;
				RTStr = "HW";
				state_flag = 1;

			} else if (myRBT==rtBinbox[0]) {//20081107
				if(rtBinbox[0].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[0]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[0]+"- ...\n";
					rtBinnum--;
				}
			} else if (myRBT==rtBinbox[1]) {//20081107
				if(rtBinbox[1].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[1]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[1]+"- ...\n";
					rtBinnum--;
				}
			} else if (myRBT==rtBinbox[2]) {//20081107
				if(rtBinbox[2].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[2]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[2]+"- ...\n";
					rtBinnum--;
				}
			} else if (myRBT==rtBinbox[3]) {//20081107
				if(rtBinbox[3].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[3]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[3]+"- ...\n";
					rtBinnum--;
				}
			} else if (myRBT==rtBinbox[4]) {//20081107
				if(rtBinbox[4].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[4]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[4]+"- ...\n";
					rtBinnum--;
				}
			} else if (myRBT==rtBinbox[5]) {//20100428
				if(rtBinbox[5].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[5]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[5]+"- ...\n";
					rtBinnum--;
				}
			} else if (myRBT==rtBinbox[6]) {//20100428
				if(rtBinbox[6].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[6]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[6]+"- ...\n";
					rtBinnum--;
				}
			} else if (myRBT==rtBinbox[7]) { //20160824	Dustin add RT bin 9
				if(rtBinbox[7].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[7]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[7]+"- ...\n";
					rtBinnum--;
				}
			} else if (myRBT==rtBinbox[8]) { //20160824	Dustin add RT bin 10
				if(rtBinbox[8].isSelected()){
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Selected Retest Bin number is -" +rtBinname[8]+"- ...\n";
					rtBinnum++;
				} else {
					state_flag = 1;
					tmpStr = "\n___" + btCnt + "___ Deleted Retest Bin number is -" +rtBinname[8]+"- ...\n";
					rtBinnum--;
				}
			}
			if(mutiRobot_flag == true) {//20081230
				if(myRBT==robot_bt[0]){//20081021                              
					tmpStr = "\n___" + btCnt + "___ Selected Robot is -"+robot_btname[0]+"- ...\n";
					RobotStr_set = robot_btname[0];
					state_flag = 1;
				} else if(myRBT==robot_bt[1]){
					tmpStr = "\n___" + btCnt + "___ Selected Robot is -"+robot_btname[1]+"- ...\n";
					RobotStr_set = robot_btname[1];
					state_flag = 1;
				} else if(myRBT==robot_bt[2]) {
					tmpStr = "\n___" + btCnt + "___ Selected Robot is -"+robot_btname[2]+"- ...\n";
					RobotStr_set = robot_btname[2];
					state_flag = 1;
				} //else if (myRBT==robot_bt[3]) {
				//  tmpStr = "\n___" + btCnt + "___ itemStateChanged: <WARNING> Wrong Operation (***)\n";
				//}
			}

			if(RT_flag == true)//20100428
			{ 
				if (myRBT==RT_bt[0]) {         
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[0]+"- ...\n";
					RTNumStr = RT_btname[0];
					RTNumStr2 = "RT1";
					state_flag = 1;
				} else if (myRBT==RT_bt[1]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[1]+"- ...\n";
					RTNumStr = RT_btname[1];
					RTNumStr2 = "RT2";
					state_flag = 1;
				} else if (myRBT==RT_bt[2]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[2]+"- ...\n";
					RTNumStr = RT_btname[2];
					RTNumStr2 = "RT3";
					state_flag = 1;
				} else if (myRBT==RT_bt[3]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[3]+"- ...\n";
					RTNumStr = RT_btname[3];
					RTNumStr2 = "RT4";
					state_flag = 1;
				} else if (myRBT==RT_bt[4]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[4]+"- ...\n";
					RTNumStr = RT_btname[4];
					RTNumStr2 = "RT5";
					state_flag = 1;
				} else if (myRBT==RT_bt[5]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[5]+"- ...\n";
					RTNumStr = RT_btname[5];
					RTNumStr2 = "RT6";
					state_flag = 1;
				} else if (myRBT==RT_bt[6]) {          //20110209
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[6]+"- ...\n";
					RTNumStr = RT_btname[6];
					RTNumStr2 = "RT7";
					state_flag = 1;
				} else if (myRBT==RT_bt[7]) {          //20110209
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[7]+"- ...\n";
					RTNumStr = RT_btname[7];
					RTNumStr2 = "RT8";
					state_flag = 1;
				} else if (myRBT==RT_bt[8]) {          //2016/08/24 Dustin add R9-R20
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[8]+"- ...\n";
					RTNumStr = RT_btname[8];
					RTNumStr2 = "RT9";
					state_flag = 1;
				} else if (myRBT==RT_bt[9]) {          
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[9]+"- ...\n";
					RTNumStr = RT_btname[9];
					RTNumStr2 = "RT10";
					state_flag = 1;
				} else if (myRBT==RT_bt[10]) {         
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[10]+"- ...\n";
					RTNumStr = RT_btname[10];
					RTNumStr2 = "RT11";
					state_flag = 1;
				} else if (myRBT==RT_bt[11]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[11]+"- ...\n";
					RTNumStr = RT_btname[11];
					RTNumStr2 = "RT12";
					state_flag = 1;
				} else if (myRBT==RT_bt[12]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[12]+"- ...\n";
					RTNumStr = RT_btname[12];
					RTNumStr2 = "RT13";
					state_flag = 1;
				} else if (myRBT==RT_bt[13]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[13]+"- ...\n";
					RTNumStr = RT_btname[13];
					RTNumStr2 = "RT14";
					state_flag = 1;
				} else if (myRBT==RT_bt[14]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[14]+"- ...\n";
					RTNumStr = RT_btname[14];
					RTNumStr2 = "RT15";
					state_flag = 1;
				} else if (myRBT==RT_bt[15]) {
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[15]+"- ...\n";
					RTNumStr = RT_btname[15];
					RTNumStr2 = "RT16";
					state_flag = 1;
				} else if (myRBT==RT_bt[16]) {          
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[16]+"- ...\n";
					RTNumStr = RT_btname[16];
					RTNumStr2 = "RT17";
					state_flag = 1;
				} else if (myRBT==RT_bt[17]) {          
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[17]+"- ...\n";
					RTNumStr = RT_btname[17];
					RTNumStr2 = "RT18";
					state_flag = 1;
				} else if (myRBT==RT_bt[18]) {          
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[18]+"- ...\n";
					RTNumStr = RT_btname[18];
					RTNumStr2 = "RT19";
					state_flag = 1;
				} else if (myRBT==RT_bt[19]) {          //20110209
					tmpStr += "\n___" + btCnt + "___ Selected RT Number is -"+RT_btname[19]+"- ...\n";
					RTNumStr = RT_btname[19];
					RTNumStr2 = "RT20";
					state_flag = 1;
				}
				if(rtrbt[2].isSelected()){ //20161026
					if (myRBT==ANFrtBinbox[0]) {//20161026 for ANF RT Bin -----Start
						if(ANFrtBinbox[0].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[0]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[0]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[1]) {//20161026
						if(ANFrtBinbox[1].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[1]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[1]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[2]) {//20161026
						if(ANFrtBinbox[2].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[2]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[2]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[3]) {//20161026
						if(ANFrtBinbox[3].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[3]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[3]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[4]) {//20161026
						if(ANFrtBinbox[4].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[4]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[4]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[5]) {//20161026
						if(ANFrtBinbox[5].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[5]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[5]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[6]) {//20161026
						if(ANFrtBinbox[6].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[6]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[6]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[7]) {//20161026
						if(ANFrtBinbox[7].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[7]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[7]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[8]) {//20161026
						if(ANFrtBinbox[8].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[8]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[8]+"- ...\n";
						}
					}  //for ANF RT Bin -----End

				}
			}
			if(EQC_flag == true)
			{ //System.out.println("GAGAGAGAGGAGAGAGAGAGAGA");
				if (myRBT==EQC_bt[0]) {         //qbin set
					tmpStr += "\n___" + btCnt + "___ Selected EQC Qbin is -"+EQC_btname[0]+"- ...\n";
					EQCStr_qbin_set = EQC_btname[0];

					state_flag = 1;
				} else if (myRBT==EQC_bt[1]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Qbin is -"+EQC_btname[1]+"- ...\n";
					EQCStr_qbin_set = EQC_btname[1];
					state_flag = 1;
				} else if (myRBT==EQC_bt[2]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Qbin is -"+EQC_btname[2]+"- ...\n";
					EQCStr_qbin_set = EQC_btname[2];
					state_flag = 1;
				} else if (myRBT==EQC_bt[3]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Qbin is -"+EQC_btname[3]+"- ...\n";
					EQCStr_qbin_set = EQC_btname[3];
					state_flag = 1;
				} else if (myRBT==EQC_bt[4]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Qbin is -"+EQC_btname[4]+"- ...\n";
					EQCStr_qbin_set = EQC_btname[4];
					state_flag = 1;
				} else if (myRBT==EQC_bt[5]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Qbin is -"+EQC_btname[5]+"- ...\n";
					EQCStr_qbin_set = EQC_btname[5];
					state_flag = 1;
				} else if (myRBT==EQC_bt[6]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Qbin is -"+EQC_btname[6]+"- ...\n";
					EQCStr_qbin_set = EQC_btname[6];
					state_flag = 1;
				} else if (myRBT==EQC_bt[7]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Qbin is -"+EQC_btname[7]+"- ...\n";
					EQCStr_qbin_set = EQC_btname[7];
					state_flag = 1;
				}
				if (myRBT==EQC_sub_bt[0]) {  //bin set  20100120   20110209
					tmpStr += "\n___" + btCnt + "___ Selected EQC Bin is -"+EQC_sub_btname[0]+"- ...\n";
					EQCStr_bin_set = EQC_sub_btname[0];
					state_flag = 1;
				} else if (myRBT==EQC_sub_bt[1]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Bin is -"+EQC_sub_btname[1]+"- ...\n";
					EQCStr_bin_set = EQC_sub_btname[1];
					state_flag = 1;
				} else if (myRBT==EQC_sub_bt[2]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Bin is -"+EQC_sub_btname[2]+"- ...\n";
					EQCStr_bin_set = EQC_sub_btname[2];
					state_flag = 1;
				} else if (myRBT==EQC_sub_bt[3]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Bin is -"+EQC_sub_btname[3]+"- ...\n";
					EQCStr_bin_set = EQC_sub_btname[3];
					state_flag = 1;
				} else if (myRBT==EQC_sub_bt[4]) {
					tmpStr += "\n___" + btCnt + "___ Selected EQC Bin is -"+EQC_sub_btname[4]+"- ...\n";
					EQCStr_bin_set = EQC_sub_btname[4];
					state_flag = 1;
				} 

				if (myRBT==QNum_bt[0]) {  //20100618
					tmpStr += "\n___" + btCnt + "___ Selected QNum is -"+QNum_btname[0]+"- ...\n";
					EQC_QNum_Str = QNum_btname[0];
					state_flag = 1;
				} else if (myRBT==QNum_bt[1]) {
					tmpStr += "\n___" + btCnt + "___ Selected QNum is -"+QNum_btname[1]+"- ...\n";
					EQC_QNum_Str = QNum_btname[1];
					EQC_QNum_Str2 = "RT1";
					state_flag = 1;
				} else if (myRBT==QNum_bt[2]) {
					tmpStr += "\n___" + btCnt + "___ Selected QNum is -"+QNum_btname[2]+"- ...\n";
					EQC_QNum_Str = QNum_btname[2];
					EQC_QNum_Str2 = "RT2";
					state_flag = 1;
				} else if (myRBT==QNum_bt[3]) {
					tmpStr += "\n___" + btCnt + "___ Selected QNum is -"+QNum_btname[3]+"- ...\n";
					EQC_QNum_Str = QNum_btname[3];
					EQC_QNum_Str2 = "RT3";
					state_flag = 1;
				} 

				if(rtrbt[3].isSelected()){ //20161026
				if (myRBT==QRTBinbox[0]) {//20100618
						if(QRTBinbox[0].isSelected()){
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[0]+"- ...\n";
						QrtBinnum++;
					} else {
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[0]+"- ...\n";
						QrtBinnum--;
					}
				} else if (myRBT==QRTBinbox[1]) {
						if(QRTBinbox[1].isSelected()){
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[1]+"- ...\n";
						QrtBinnum++;
					} else {
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[1]+"- ...\n";
						QrtBinnum--;
					}
				} else if (myRBT==QRTBinbox[2]) {
						if(QRTBinbox[2].isSelected()){
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[2]+"- ...\n";
						QrtBinnum++;
					} else {
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[2]+"- ...\n";
						QrtBinnum--;
					}
				} else if (myRBT==QRTBinbox[3]) {
						if(QRTBinbox[3].isSelected()){
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[3]+"- ...\n";
						QrtBinnum++;
					} else {
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[3]+"- ...\n";
						QrtBinnum--;
					}
				} else if (myRBT==QRTBinbox[4]) {
						if(QRTBinbox[4].isSelected()){
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[4]+"- ...\n";
						QrtBinnum++;
					} else {
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[4]+"- ...\n";
						QrtBinnum--;
					}
				}else if (myRBT==QRTBinbox[5]) {
						if(QRTBinbox[5].isSelected()){
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[5]+"- ...\n";
						QrtBinnum++;
					} else {
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[5]+"- ...\n";
						QrtBinnum--;
					}
				} else if (myRBT==QRTBinbox[6]) {
						if(QRTBinbox[6].isSelected()){
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[6]+"- ...\n";
						QrtBinnum++;
					} else {
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[6]+"- ...\n";
						QrtBinnum--;
					}
				} else if (myRBT==QRTBinbox[7]) {
						if(QRTBinbox[7].isSelected()){
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[7]+"- ...\n";
						QrtBinnum++;
					} else {
						state_flag = 1;
						tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[7]+"- ...\n";
						QrtBinnum--;
					}
					} else if (myRBT==QRTBinbox[8]) {
						if(QRTBinbox[8].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[8]+"- ...\n";
							QrtBinnum++;
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[8]+"- ...\n";
							QrtBinnum--;
						}
					} else if (myRBT==QRTBinbox[9]) {
						if(QRTBinbox[9].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected QRT bin is -" +QRTBinname[9]+"- ...\n";
							QrtBinnum++;
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted QRT bin is -" +QRTBinname[9]+"- ...\n";
							QrtBinnum--;
						}
					} else if (myRBT==ANFrtBinbox[0]) {//20161026 for ANF QRT Bin -----Start
						if(ANFrtBinbox[0].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[0]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[0]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[1]) {//20161026
						if(ANFrtBinbox[1].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[1]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[1]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[2]) {//20161026
						if(ANFrtBinbox[2].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[2]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[2]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[3]) {//20161026
						if(ANFrtBinbox[3].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[3]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[3]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[4]) {//20161026
						if(ANFrtBinbox[4].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[4]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[4]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[5]) {//20161026
						if(ANFrtBinbox[5].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[5]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[5]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[6]) {//20161026
						if(ANFrtBinbox[6].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[6]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[6]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[7]) {//20161026
						if(ANFrtBinbox[7].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[7]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[7]+"- ...\n";
						}
					} else if (myRBT==ANFrtBinbox[8]) {//20161026
						if(ANFrtBinbox[8].isSelected()){
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Selected ANF RT Bin number is -" +rtBinname[8]+"- ...\n";
						} else {
							state_flag = 1;
							tmpStr = "\n___" + btCnt + "___ Deleted ANF RT Bin number is -" +rtBinname[8]+"- ...\n";
						}
					}  //for ANF QRT Bin -----End

				}
			}

			if(CORR_flag==true) {
				if (myRBT==CORR_bt[0]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR type is -"+CORR_btname[0]+"- ...\n";
					CORRStr_set = CORR_btname[0];
					state_flag = 1;
				} else if (myRBT==CORR_bt[1]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR type is -"+CORR_btname[1]+"- ...\n";
					CORRStr_set = CORR_btname[1];
					state_flag = 1;
				} else if (myRBT==CORRbin_bt[0]) { //Add by Cola. 20160408-----Start
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[0]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[0];
					state_flag = 1;
				} else if (myRBT==CORRbin_bt[1]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[1]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[1];
					state_flag = 1;
				}
				else if (myRBT==CORRbin_bt[2]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[2]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[2];
					state_flag = 1;
				}
				else if (myRBT==CORRbin_bt[3]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[3]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[3];
					state_flag = 1;
				}
				else if (myRBT==CORRbin_bt[4]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[4]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[4];
					state_flag = 1;
				}
				else if (myRBT==CORRbin_bt[5]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[5]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[5];
					state_flag = 1;
				}
				else if (myRBT==CORRbin_bt[6]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[6]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[6];
					state_flag = 1;
				}
				else if (myRBT==CORRbin_bt[7]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[7]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[7];
					state_flag = 1;
				}
				else if (myRBT==CORRbin_bt[8]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[8]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[8];
					state_flag = 1;
				}
				else if (myRBT==CORRbin_bt[9]) {
					tmpStr += "\n___" + btCnt + "___ Selected CORR Bin is -"+CORRbin_btname[9]+"- ...\n";
					CORRbinStr_set = CORRbin_btname[9];
					state_flag = 1;
				}  //Add by Cola. 20160408
			}

			if(checkCORR_flag==true) {
				if (myRBT==checkCorr_bt[0]) {
//					checkCorr_frame.setVisible(false);
					tmpStr += "\n___" + btCnt + "___ Selected check CORR type is -"+checkCorr_btname[0]+"- ...\n";
					checkCorrStr_set = checkCorr_btname[0];
					state_flag = 1;
				} if (myRBT==checkCorr_bt[1]) {
					checkCorr_frame.setVisible(false);
					tmpStr += "\n___" + btCnt + "___ Selected check CORR type is -"+checkCorr_btname[1]+"- ...\n";
					checkCorrStr_set = checkCorr_btname[1];
					state_flag = 1;
				}else if (myRBT==checkCorr_bt[2]) {
					checkCorr_frame.setVisible(false);
					tmpStr += "\n___" + btCnt + "___ Selected check CORR type is -"+checkCorr_btname[2]+"- ...\n";
					checkCorrStr_set = checkCorr_btname[2];
					state_flag = 1;

					tmpStr  = "<Exception> \n";//20120412
					tmpStr += "+--------------------+ \n";			
					tmpStr += "| please do Corr before A1 | \n";
					tmpStr += "+--------------------+ \n";
					JOptionPane.showMessageDialog(null, tmpStr);
					autoSaveMessageBeforeExit();
					saveErrorMessageRealtime("Msg:dont do Corr before A1");
					killproc(); System.exit(1); // to quit Java app for Linux		    

				} else if (myRBT==checkCorr_bt[3]) {
					tmpStr += "\n___" + btCnt + "___ Selected check CORR type is -"+checkCorr_btname[3]+"- ...\n";
					checkCorrStr_set = checkCorr_btname[3];
					state_flag = 1;



					while (NO_Corr_Reason.equals("")){

						NO_Corr_Reason = JOptionPane.showInputDialog("Please input 'don't need do corr' reason:");
						try {
							Thread.sleep(20);// unit: ms
						} catch (java.lang.InterruptedException Ierr) {
						}
						if(NO_Corr_Reason.equals("")) {
							JOptionPane.showMessageDialog(null, "Please input 'don't need do corr' reason:");
						}		    
					}
					checkCorr_frame.setVisible(false);
				}
			}	    
			if(CORR_L176_flag == true){

				if (myRBT==L176_corr_JRadioButtonList[0]) {

					L176_FT_corr_reason = L176_corr_str[0];
					state_flag = 1;
				}

				if (myRBT==L176_corr_JRadioButtonList[1]) {

					L176_FT_corr_reason = L176_corr_str[1];
					state_flag = 1;
				}					

				if (myRBT==L176_corr_JRadioButtonList[2]) {

					L176_FT_corr_reason = L176_corr_str[2];
					state_flag = 1;
				}

				if (myRBT==L176_corr_JRadioButtonList[3]) {

					L176_FT_corr_reason = L176_corr_str[3];
					state_flag = 1;
				}

				if (myRBT==L176_corr_JRadioButtonList[4]) {

					L176_FT_corr_reason = L176_corr_str[4];
					state_flag = 1;
			}

				if (myRBT==L176_corr_JRadioButtonList[5]) {

					L176_FT_corr_reason = L176_corr_str[5];
					state_flag = 1;
				}
			}				
		}

		if (state_flag ==0) {
			tmpStr = "\n___" + btCnt + "___ itemStateChanged: <WARNING> Wrong Operation (***)\n";
		}

		tmpStr += "___" + btCnt + "___ NOW barcode_stationStr = " + barcode_stationStr + "\n"; 
		System.out.print(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		if(EQCStr_qbin_set == "QBIN1") {  //20081231//20100618
			EQC_qbin_Str = "B1";
		} else if (EQCStr_qbin_set == "QBIN2") {
			EQC_qbin_Str = "B2";
		} else if (EQCStr_qbin_set == "QBIN3") {
			EQC_qbin_Str = "B3";
		} else if (EQCStr_qbin_set == "QBIN4") {
			EQC_qbin_Str = "B4";
		} else if (EQCStr_qbin_set == "QBIN5") {
			EQC_qbin_Str = "B5";
		} else if (EQCStr_qbin_set == "QBIN6") {
			EQC_qbin_Str = "B6";
		} else if (EQCStr_qbin_set == "QBIN7") {
			EQC_qbin_Str = "B7";
		} else if (EQCStr_qbin_set == "QBIN8") {
			EQC_qbin_Str = "B8";
		} 

		if(EQCStr_bin_set == "EQC1") {  //20100120
			EQC_bin_Str = "EQC1";
			EQC_bin_F128_Str = "QA1";
		} else if (EQCStr_bin_set == "EQC2") {
			EQC_bin_Str = "EQC2";
			EQC_bin_F128_Str = "QA2";
		} else if (EQCStr_bin_set == "EQC3") {
			EQC_bin_Str = "EQC3";
			EQC_bin_F128_Str = "QA3";
		} else if (EQCStr_bin_set == "EQC4") { //20110209
			EQC_bin_Str = "EQC4";
			EQC_bin_F128_Str = "QA4";
		} else if (EQCStr_bin_set == "EQC5") { //20110209
			EQC_bin_Str = "EQC5";
			EQC_bin_F128_Str = "QA5";
		} 

		QrtBinCount=0;//2010618
		QrtBinStr = "";//2010618
		if(QrtBinnum>0 && rtrbt[3].isSelected()) { //2010618  
			QrtBinStr = "";
			for(int i=0;i<QRTBinname.length;i++)
			{
				if(QRTBinbox[i].isSelected()){
					if(QRTBinname[i].equals("B1")) {
						QrtBinStr += "1";
					} else if (QRTBinname[i].equals("B2")) {
						QrtBinStr += "2";
					} else if (QRTBinname[i].equals("B3")) {
						QrtBinStr += "3";
					} else if (QRTBinname[i].equals("B4")) {
						QrtBinStr += "4";
					} else if (QRTBinname[i].equals("B5")) {
						QrtBinStr += "5";
					} else if (QRTBinname[i].equals("B6")) {
						QrtBinStr += "6";
					} else if (QRTBinname[i].equals("B7")) {
						QrtBinStr += "7";
					} else if (QRTBinname[i].equals("B8")) {
						QrtBinStr += "8";
					} else if (QRTBinname[i].equals("B9")) {
						QrtBinStr += "9";
					} else if (QRTBinname[i].equals("B10")) {
						QrtBinStr += "10";
					}
					QrtBinCount++;
				}
			}
			QrtBinStr = "B" + QrtBinStr;//20100428
			tmpStr = "___ NOW QRT Bin String = " + QrtBinStr + "\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		}
		rtBinCount=0;//20100428
		rtBinSumStr = "";
		rtBinStr = "";//20100428
		if(rtBinnum>0 && rtrbt[2].isSelected()) {   //20081107
			rtBinSumStr = "B";
			rtBinStr = "";//20100428
			
			for(int i=0;i<rtBinname.length;i++)
			{
				if(rtBinbox[i].isSelected()){//20104028
					rtBinCount++;
					if(!barcode_customerStr.equals("F128")) {//20110712
						if(rtBinCount==1) {;
						}else{
							rtBinStr+="+";
						}
					}
					rtBinStr += rtBinname[i];
					rtBinSumStr += rtBinname[i];
				}
			}
			if(!barcode_customerStr.equals("F128")) {//20110712
				rtBinStr += "_";//20100428
			}
			tmpStr = "___ NOW Retest Bin String = " + rtBinStr + "\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			tmpStr = "___ NOW Retest Bin String for Sum= " + rtBinSumStr + "\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		}

	}

	public static void killproc(){
		String tmpStr="";
		String cmd="";
		cmd = csicAutoPath + "/bin/opkill all";  //--hh
		tmpStr = cmd + "\n\n";
		System.out.print(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		//--hh
		//        cmd = csicAutoPath + "/bin/dmdkill";
		//        tmpStr = cmd + "\n\n";
		//        System.out.print(tmpStr); 
		//        textArea2.append(tmpStr);
		//        saveMessageRealtime(tmpStr);
		//        javaExecSystemCmd(cmd);

	}

	// --hh  , do not use
	public boolean exitOIC(boolean query) {
		String cmd = "";
		String tmpStr = "";
		boolean status = false;

		if (query==true) {

			cmd = "dmd_cmd -s";
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			cmd = "dmd_cmd cont stop";
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			// exit OIC tool
			killproc();

			cmd = "/bin/rm -f pid_oic";
			tmpStr = cmd + "\n\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			cmd = csicAutoPath + "/bin/dmdexekill";
			tmpStr = cmd + "\n\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			cmd = "/bin/rm -f pid_dmd_exe";
			tmpStr = cmd + "\n\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			oicFlag = false;
			status = true;
		}
		else {
			//tmpStr  = "Do you want to unload OIC ?\n";
			//int confirmflag = 1;
			//// yes to return 0, no to return 1
			//confirmflag = JOptionPane.showConfirmDialog(null, tmpStr, "Exit OIC ?", JOptionPane.YES_NO_OPTION);
			//if (confirmflag==0) {
			if (true) {

				cmd = "dmd_cmd -s";
				tmpStr = cmd + "\n";
				System.out.println(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				javaExecSystemCmd(cmd);

				cmd = "dmd_cmd cont stop";
				tmpStr = cmd + "\n";
				System.out.println(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				javaExecSystemCmd(cmd);

				// exit OIC tool
				killproc();

				cmd = "/bin/rm -f pid_oic";
				tmpStr = cmd + "\n\n";
				System.out.print(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				javaExecSystemCmd(cmd);

				cmd = csicAutoPath + "/bin/dmdexekill";
				tmpStr = cmd + "\n\n";
				System.out.print(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				javaExecSystemCmd(cmd);

				cmd = "/bin/rm -f pid_dmd_exe";
				tmpStr = cmd + "\n\n";
				System.out.print(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				javaExecSystemCmd(cmd);

				oicFlag = false;
				status = true;
			}
			else {
				// NO exit OIC tool
				status = false;
				oicFlag = true;
			}
		}
		return status;
	}


	// --hh , copy this
	public void exitOicu() { // execute  stop_session, and  opkill all,  
		String tmpStr = "";     
		javaExecSystemCmd("cex -t " + hostnameStr + " -c stop_session\n"); // hostnameStr  is  hostname
		tmpStr = "unload program and exit the system\n";  
		System.out.println(tmpStr);
		textArea2.append(tmpStr);  //  >>> Message Log <<< 

		javaExecSystemCmd("opkill all\n");  // opkill all, pokill oicu, opkill all tpwn, opkill oicu tpwn
		tmpStr = "kill all process\n";
		System.out.println(tmpStr);	
		textArea2.append(tmpStr); 
	}

	// --hh,  do not use !!
	public void runOIC() {
		String cmd = "";
		String tmpStr = "";

		boolean exitflag = true;

		if (testTypeStr.equalsIgnoreCase("Wafer")){
			if(JobFlag&&oicFlag){
				;
			}
			else {
				if (oicFlag==true) {
					exitflag = exitOIC(false);
				}
				else {
					exitflag = exitOIC(true);
				}

				if (exitflag) {
					tmpStr = "\nLaunch OIC tool ......\n";
					System.out.print(tmpStr); 
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);

					if(DMDSWVerStr.startsWith("v2.1.0")){ //20100308
						;
					} 
					else 
					{
						cmd  = "oic";
						javaExecSystemCmd2(cmd, 5000); // can not use javaExecSystemCmd()
						oicFlag = true;
					}
				}
			}
		} else{
			if (oicFlag==true) {
				exitflag = exitOIC(false);
			}
			else {
				exitflag = exitOIC(true);
			}

			if (exitflag) {
				tmpStr = "\nLaunch OIC tool ......\n";
				System.out.print(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);

				if(DMDSWVerStr.startsWith("v2.1.0")){ //20100308
					;
				} 
				else 
				{
					cmd  = "oic";
					javaExecSystemCmd2(cmd, 5000); // can not use javaExecSystemCmd()
					oicFlag = true;
				}
			}
		}

	}


	//--hh
	public static String processUserInfoContent_new(String inStr, String splitStr) { 

		String paramStr[] = new String[2];

		//initial
		paramStr[0] = "";
		paramStr[1] = "";

		//inStr = stringRemoveSpaceHeadTail(inStr);

		if ( inStr != null ){

			paramStr = inStr.split(splitStr);
			if (paramStr.length >= 2 ) {
				paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
				paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
				return paramStr[1];
			}
		}
		return "";
	}    


	//--hh
	public void wait_until_load_done(){
		// execute:   cex  -t  DX-2  -c  status,  judge Status: Tester Ready?  if yes, print Done!
		String cmd2 = "";          String tmpStr = "";        String checkStr = "";
		Boolean checkflag = true;  String tmpStr2 = "";

		// 1. Tester Idle        // 2. Tester Loading        // 3. Tester Ready

		//--- For offline Simulator --------------------
		if(offline){
			// System.out.println( " @@@ hostnameStr = " + hostnameStr);
			hostnameStr = "harrisonx_Hi6561_V311_DMDx_sim";                       //-- offline
		}	

		cmd2 = "cex -t " + hostnameStr + " -c status\n";   // Page 3
		System.out.println(cmd2);
		try{     
			do{  
				Process proc = Runtime.getRuntime().exec(cmd2);
				// if JAVA will execute  system command, command  must be put to process !!
				Thread.sleep(2000);
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while ((tmpStr = in.readLine()) != null) {
					checkStr = processUserInfoContent_new(tmpStr, ":"); // Page 46, return the string after divider
					if (checkStr.equals("Tester Loading")){
						if (checkflag) {
							tmpStr2 = checkStr;
							checkflag = false;
						}else{
							tmpStr2 = " .";
						}
						System.out.print(tmpStr2);   textArea2.append(tmpStr2);
						// wait(1000);
					}else if (checkStr.equals("Tester Ready")){
						System.out.print("Done.\n");    textArea2.append("Done.\n");
						break;
					}else{   }
				} // while ((tmpStr = in.readLine()) != null)
				in.close(); // close file
			} while (checkStr.equals("Tester Ready")==false); 
			// --- do 
		} // --- try
		catch (Exception t) {  System.out.print(t);   }

	}







	//--hh
	/*    class thread_2 extends Thread {  // --- Load Program ---
        public thread_2() {}
        public void run() {

             String tmpStr = "";
	     String cmd    = "";

//	javaExecSystemCmd("xterm -rightbar -bg green -fg black -geometry 158x20  -e  pwd.csh" ); //20140522
//	System.out.println("====load prog===========");	     

           //--- For offline Simulator --------------------
             if(offline){
                // System.out.println( " @@@ hostnameStr = " + hostnameStr);
	        hostnameStr = "Hi6561_V311_DMDx";                                         //-- offline
             }

           //--- Load Prog --------------------------------
	    if(offline){
	     cmd    = "launcher -prod -sim " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";  
	     tmpStr = "launcher -prod -sim " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";
	     }
	    if(online){	     
	     cmd    = "launcher -prod " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";  
	     tmpStr = "launcher -prod " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";
	     }
	     textArea2.append(tmpStr);
	     System.out.println(tmpStr);
             javaExecSystemCmd2(cmd,5000);	

        }
    }*/  


	//--hh
	public void SetExtintf(){
		String cmd = "";
		String tmpStr = "";	    

		tmpStr = "\nExternal interface Setting Process...\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		cmd = "cex -t " + hostnameStr + " -c set_extintf_object Epson\n";	//--hh , TBD
		System.out.println(cmd); 	    
		textArea2.append("Set External interface ----> \"" + "Epson" + "\"\n");	    
		javaExecSystemCmd(cmd);

	}    

	//--hh
	/*    class thread_1 extends Thread { // --- Set LotInfo and Datalog ---
        public thread_1() {}
        public void run() {          
            String cmd = "";

            wait_until_load_done();	    

            // SetLotInfo();   SetDatalog();	    
	    if (genDmdCmdFile()) {  // --- create LotInfo and Datalog in, cmd_script.dmd !!
                // genExecShellFile(); // --- create "execDmd" file
                // processCmdFile();   // --- 755 cmd_script.dmd,   755  execDmd
            }

  	    if(online){				  
	        SetExtintf(); 
	    }
	    JOptionPane.showMessageDialog(null, "7777777 ");
//            cmd = "xterm -rightbar -bg green -fg black -geometry 158x20  -e " + "cd /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/NoStart_OICU/start_oicu";
            cmd = "xterm -rightbar -bg green -fg black -geometry 158x20  -e " + "xterm_nostart.csh";
	    javaExecSystemCmd(cmd);	    	    
//	    JOptionPane.showMessageDialog(null, "Ready to Run! ");
		JOptionPane.showMessageDialog(null, "1111111111111 ");
        }
    } */   



	//--hh
	public void runLoadJobtoOICu() {
		String cmd = "";
		String tmpStr = "";

		tmpStr = "runLoadJobtoOICu ......\n";
		System.out.print(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		System.out.println("@@@ LocalPath = " + LocalPath + "\n"); //--hh       @@@ LocalPath = /tmp/LinuxT5_barcode_file/
		textArea2.append("@@@ LocalPath = " + LocalPath + "\n");   //--hh

		//				if(false){
		//		        thread_2 LoadProgram = new thread_2();         // --- Load Program ---

		//		        LoadProgram.start();

		//		        thread_1 Wait_and_SetLotInfo = new thread_1(); // --- Set LotInfo and Datalog ---
		//		        Wait_and_SetLotInfo.start();
		//				}else{

		//  String tmpStr = "";
		// String cmd    = "";

		//     javaExecSystemCmd("xterm -rightbar -bg green -fg black -geometry 158x20  -e  pwd.csh" ); //20140522
		//     System.out.println("====load prog===========");            

		//--- For offline Simulator --------------------
		if(offline){
			// System.out.println( " @@@ hostnameStr = " + hostnameStr);
			hostnameStr = "Hi6561_V311_DMDx";                                         //-- offline
		}

		//--- Load Prog --------------------------------
		if(offline){
			cmd    = "launcher -prod -sim " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";  
			tmpStr = "launcher -prod -sim " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";
		}
		if(online){             
			cmd    = "launcher -prod " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";  
			tmpStr = "launcher -prod " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";
		}
		if(openOICu_flag == false) //Add by Cola. 20150714    
		{
			textArea2.append(tmpStr);
			System.out.println(tmpStr);
			javaExecSystemCmd2(cmd, 5000);
			openOICu_flag = true;  
		}
		//-----------------------
		wait_until_load_done();     
		
		// SetLotInfo();   SetDatalog();         
		if (genDmdCmdFile()) {  // --- create LotInfo and Datalog in, cmd_script.dmd !!
			// genExecShellFile(); // --- create "execDmd" file
			// processCmdFile();   // --- 755 cmd_script.dmd,   755  execDmd
		}

		if(online){                                  
			SetExtintf(); 
		}
		if(barcode_devicetypeStr.equalsIgnoreCase("BHH10326CW")){ //Clear Test Program Datalog to Window Print by Cola. 20160629
			javaExecSystemCmd("cex -t " + hostnameStr + " -c evx_dlog_clear_methods 0" + "\n"); 
			javaExecSystemCmd("cex -t " + hostnameStr + " -c evx_dlog_failcount 1" + "\n"); 
		}
		//This is for Production --> Set Manual_Test = FALSE. 20170907
		javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp Manual_Test FALSE\n");	// "Manual_Test" is the parameter in the .una
		
		if(testTypeStr.equalsIgnoreCase("Final")){ //20170719
			textArea2.append("!!! Start of Lot !!!\n");
			javaExecSystemCmd("cex -t " + hostnameStr + " -c set_start_of_lot" + "\n");
			
			//--- Lot ID ---
			if ( LotStr != null ){	//After set_start_of_lot execution, Lot ID will be clear. 20171002
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.LotId " + LotStr + "\n";

				textArea2.append("Set Lot ID ----> \"" + LotStr + "\"\n");
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.LotId " + LotStr + "\n");
			}  
		}
		
		JOptionPane.showMessageDialog(null, "Ready to Run! ");

	//No create execDmd file Remark. 20170619 	
//		cmd  = "xterm -rightbar -bg green -fg black -geometry 158x20  -e " + LocalPath + "execDmd &";            
//		tmpStr = cmd + " \n";
//		System.out.println(tmpStr); 
//		textArea2.append(tmpStr);
//		saveMessageRealtime(tmpStr);     

	}
	/*
//--hh
    public void runLoadJobtoOICu() {
        String cmd = "";
        String tmpStr = "";

        tmpStr = "runLoadJobtoOICu ......\n";
        System.out.print(tmpStr); 
        textArea2.append(tmpStr);
        saveMessageRealtime(tmpStr);

        System.out.println("@@@ LocalPath = " + LocalPath); //--hh	@@@ LocalPath = /tmp/LinuxT5_barcode_file/
        textArea2.append("@@@ LocalPath = " + LocalPath);   //--hh

        thread_2 LoadProgram = new thread_2();         // --- Load Program ---

        LoadProgram.start();

        thread_1 Wait_and_SetLotInfo = new thread_1(); // --- Set LotInfo and Datalog ---
        Wait_and_SetLotInfo.start();


//	javaExecSystemCmd("xterm -rightbar -bg green -fg black -geometry 158x20  -e  pwd.csh" ); //20140522
//	System.out.println("=======runLoadJobtoOICu========");

        cmd  = "xterm -rightbar -bg green -fg black -geometry 158x20  -e " + LocalPath + "execDmd &";
        // cmd += LocalPath + "execDmd &";  //20091103
	// cmd +=  " execDmd &";		
	tmpStr = cmd + " \n";
	System.out.println(tmpStr); 
	textArea2.append(tmpStr);
        // javaExecSystemCmd2(cmd, 1000);
        saveMessageRealtime(tmpStr);	

/*        cmd  = "xterm -rightbar -bg green -fg black -geometry 158x20  -e ";
        //cmd += autoResultPath + "execDmd &";
        cmd += LocalPath + "execDmd &";  //20091103
        tmpStr = cmd + "\n";
        System.out.println(tmpStr); 
        textArea2.append(tmpStr);
        saveMessageRealtime(tmpStr);
        javaExecSystemCmd2(cmd, 1000);

    }
	 */


	//--hh
	public boolean confirmNewJob(String inStr) {

		String tmpStr = "";

		boolean status = false;

		tmpStr  = "Do you want to load the below Job ?" + "\n";
		tmpStr += "Job Program = " + inStr + "\n";
		System.out.println(tmpStr);
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		int confirmflag = 0;
		// yes to return 0, no to return 1
		confirmflag = JOptionPane.showConfirmDialog(null, tmpStr, "Confirm New Job ?", JOptionPane.YES_NO_OPTION);
		if (confirmflag==0) {
			status = true;
		} else {
			status = false;
			tmpStr = "Please check the Job Program name \n\n";
			System.out.print(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			//autoSaveMessageBeforeExit();
			//killproc(); System.exit(1); // to quit Java app for Linux
		}

		return status;
	}



	//--hh
	public boolean genDmdCmdFile() {

		String tmpStr0 = "";
		String DlogFileStr ="";
		String tmpStr  = "";
		String ProductCheckSummary = "";
		String checkSocketData = "";
		String cmd = "";
		String cmd1 = "";
		String outfStr = "";
		boolean status = false;

		// rmCmdFile();  //--hh


		// cmd = "cex -t " + hostnameStr + " -c load " + JobPathStr  + TPStr + "\n";  //--hh , load OPTool
		// javaExecSystemCmd2(cmd,5000); 


		try {
			//--- Create Datalog File Directory to /tmp/sumNsumpPATH.txt ------ //--hh,
			//--- This is for  uniDataCollection_Sigurd_20130918.cpp to generate *.sum & *.sump
			//cmd = "@@@ echo " + "sumNsumpPATH = " + userPathforCP + " > /tmp/sumNsumpPATH.txt " + "\n";
			//System.out.println(cmd);
			//textArea2.append(cmd);

			//cmd = "touch /tmp/sumNsumpPATH.txt";	     
			//javaExecSystemCmd(cmd);
			//cmd = "chmod 777 /tmp/sumNsumpPATH.txt";	     
			//javaExecSystemCmd(cmd);	     

			//cmd1 = "sumNsumpPATH=" + userPathforCP;
			//cmd = "echo " + cmd1 + " > /tmp/sumNsumpPATH.txt";	     
			//javaExecSystemCmd(cmd);
			outfStr = "/tmp/sumNsumpPATH.txt";
			outf = new File(outfStr);
			FileWriter UserFlagFW;

			UserFlagFW = new FileWriter(outf, false);
			tmpStr = "sumNsumpPATH=" + userPathforCP;
			UserFlagFW.write(tmpStr);
			UserFlagFW.close();




			//--- Open File:cmd_script.dmd  ----------------
			outfStr = LocalPath + "cmd_script.dmd"; //--- save command to cmd_script.dmd  // 20091103
			outf = new File(outfStr);
			FileWriter dmdFW;
			dmdFW = new FileWriter(outf, false);
			textArea2.append("Open file = " + outfStr + "\n");
			System.out.println("Open file = " + outfStr + "\n");


			//--- For offline Simulator --------------------
			if(offline){
				// System.out.println( " @@@ hostnameStr = " + hostnameStr);
				hostnameStr = "harrisonx_Hi6561_V311_DMDx_sim";                                         //-- offline
			}


			cmd = "#!/bin/csh \n";      //--hh
			//   cmd += "cd " + LocalPath + " \n";  //--hh
			//   cmd += "pwd \n";  //--hh
			//   cmd += "ls \n";  //--hh

			//--- SetLotInfo() -----------------------------
			// set system : Lot ID  and    operator ID

			tmpStr = "\nLot Info Setting Process... ...\n";
			System.out.println(tmpStr);     	
			textArea2.append(tmpStr);

			//--- Lot ID ---
			if ( LotStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.LotId " + LotStr + "\n";

				textArea2.append("Set Lot ID ----> \"" + LotStr + "\"\n");
				System.out.println("Set Lot ID ----> \"" + LotStr + "\"\n");
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.LotId " + LotStr + "\n");

			}    	    	   	
			//--- operator ID ---
			if ( barcode_opidStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.Operator " + barcode_opidStr + "\n";
				textArea2.append("Set Operator ID ----> \"" + barcode_opidStr + "\"\n");
				System.out.println("Set Operator ID ----> \"" + barcode_opidStr + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.Operator " + barcode_opidStr + "\n");

			} 
			//--- probe card ID ---
			if ( tx0b.getText() != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.CardId " + tx0b.getText() + "\n";
				textArea2.append("Set probe card ----> \"" + tx0b.getText() + "\"\n");
				System.out.println("Set probe card ----> \"" + tx0b.getText() + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.CardId " + tx0b.getText() + "\n");

			} 
			//--- probe Hand Type ---
			if ( barcode_proberidStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.HandlerType " + barcode_proberidStr + "\n";
				textArea2.append("Set Hand Type ----> \"" + barcode_proberidStr + "\"\n");
				System.out.println("Set Hand Type ----> \"" + barcode_proberidStr + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.HandlerType " + barcode_proberidStr + "\n");

			} 
			//--- probe hand_id ---
			if ( barcode_handleridStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.PHID " + barcode_handleridStr + "\n";
				textArea2.append("Set hand_id ----> \"" + barcode_handleridStr + "\"\n");
				System.out.println("Set hand_id ----> \"" + barcode_handleridStr + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.PHID " + barcode_handleridStr + "\n");

			} 				
			//--- device name ---
			if ( barcode_devicetypeStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.Device " + barcode_devicetypeStr + "\n";
				textArea2.append("Set device ----> \"" + barcode_devicetypeStr + "\"\n");
				System.out.println("Set device ----> \"" + barcode_devicetypeStr + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.Device " + barcode_devicetypeStr + "\n");

			} 
			//--- job name input to job_rev ---
			if ( barcode_programStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.FileNameRev " + barcode_programStr + "\n";
				textArea2.append("Set job version ----> \"" + barcode_programStr + "\"\n");
				System.out.println("Set job version ----> \"" + barcode_programStr + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.FileNameRev " + barcode_programStr + "\n");

			} 
			//--- job name input to TestMode ---
			if ( barcode_programStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TestMode " + "P" + "\n";
				textArea2.append("Set TestMode ----> \"" + "P" + "\"\n");
				System.out.println("Set TestMode ----> \"" + "P" + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TestMode " + "P" + "\n");

			} 		
			//--- job name input to TestCode ---
			if ( barcode_stationStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TestPhase " + barcode_stationStr + "\n";
				textArea2.append("Set TestCode ----> \"" + barcode_stationStr + "\"\n");
				System.out.println("Set TestCode ----> \"" + barcode_stationStr + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TestPhase " + barcode_stationStr + "\n");

			} 		
			//--- job name input to TestTemp ---
			if ( barcode_temperatureStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TestTemp " + barcode_temperatureStr + "\n";
				textArea2.append("Set TestTemp ----> \"" + barcode_temperatureStr + "\"\n");
				System.out.println("Set TestTemp ----> \"" + barcode_temperatureStr + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TestTemp " + barcode_temperatureStr + "\n");

			} 		
			//--- job name input to facil_id---
			if ( barcode_programStr != null ){
				cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TestFacility " + "Sigurd" + "\n";
				textArea2.append("Set facil_id ----> \"" + "Sigurd" + "\"\n");
				System.out.println("Set facil_id ----> \"" + "Sigurd" + "\"\n");	
				javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TestFacility " + "Sigurd" + "\n");

			} 		
			//--- job name input to proc_id ---
			if ( barcode_stationStr != null ){
				if(testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("L227")){
					//L227 Summary clean FAB ID item. 20170710
				} else{
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.FabId " + barcode_stationStr + "\n";
					textArea2.append("Set proc_id ----> \"" + barcode_stationStr + "\"\n");
					System.out.println("Set proc_id ----> \"" + barcode_stationStr + "\"\n");	
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.FabId " + barcode_stationStr + "\n");
				}
			}

			if(testTypeStr.equalsIgnoreCase("Wafer")){	     
				if(MTK_series){
					//--- job name input to User_Text ---
					if ( barcode_programStr != null ){
						cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.UserText " + "MTK" + "\n";
						textArea2.append("Set User_Text ----> \"" + "MTK" + "\"\n");
						System.out.println("Set User_Text ----> \"" + "MTK" + "\"\n");	
						javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.UserText " + "MTK" + "\n");

					}
					//20171027
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TesterType Fusion_EX" + "\n";
					textArea2.append("Set Tester Type ----> \"" + "Fusion_EX" + "\"\n");
					System.out.println("Set Tester Type ----> \"" + "Fusion_EX" + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TesterType Fusion_EX" + "\n");
				}

				//F137 Customize for New Lot. Add by Cola 2015/07/14
				if(barcode_customerStr.equals("F137"))
				{
					String[] token = barcode_devicetypeStr.split("-");
					String F137_lotNO = barcode_lotidStr.substring(0,6);
					//Shift
					cmd += "cex -t " + hostnameStr + " -c set_lot_info Shift " + "DA" + "\n";
					textArea2.append("Set Shift ----> \"" + "DA" + "\"\n");
					System.out.println("Set Shift ----> \"" + "DA" + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_lot_info Shift " + "DA" + "\n");
					//lot
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.LotId  " + LotStr + "\n";
					textArea2.append("Set Lot ID ----> \"" + LotStr + "\"\n");
					System.out.println("Set Lot ID ----> \"" + LotStr + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.LotId " + LotStr + "_P1" + "\n");	    	 
					//ProductID
					cmd += "cex -t " + hostnameStr + " -c set_lot_info ProductID " + F137_lotNO + "\n";
					textArea2.append("Set ProductID ----> \"" + F137_lotNO + "\"\n");
					System.out.println("Set ProductID ----> \"" + F137_lotNO + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_lot_info ProductID " + F137_lotNO + "\n");
					//FabricationID
					cmd += "cex -t " + hostnameStr + " -c set_lot_info FabricationID " + barcode_testeridStr + "\n";
					textArea2.append("Set FabricationID ----> \"" + barcode_testeridStr + "\"\n");
					System.out.println("Set FabricationID ----> \"" + barcode_testeridStr + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_lot_info FabricationID " + barcode_testeridStr + "\n");
					//LotState     set_exp  TestProgData.LotState 
					cmd += "cex -t " + hostnameStr + " -c set_lot_info LotState " + "NewTest" + "\n";
					textArea2.append("Set LotState ----> \"" + "NewTest" + "\"\n");
					System.out.println("Set LotState ----> \"" + "NewTest" + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_lot_info LotState " + "NewTest" + "\n");
					//LotStatus    set_exp  TestProgData.LotStates 
					cmd += "cex -t " + hostnameStr + " -c set_lot_info LotStatus " + "PreBurn" + "\n";
					textArea2.append("Set LotStatus ----> \"" + "PreBurn" + "\"\n");
					System.out.println("Set LotStatus ----> \"" + "PreBurn" + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_lot_info LotStatus " + "PreBurn" + "\n");
					//LotDescription
					cmd += "cex -t " + hostnameStr + " -c set_lot_info Description " + "BP" + "\n";
					textArea2.append("Set LotDescription ----> \"" + "BP" + "\"\n");
					System.out.println("Set LotDescription ----> \"" + "BP" + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_lot_info Description " + "BP" + "\n");
				}

				if(barcode_customerStr.equals("L121"))
				{
					String[] token = barcode_devicetypeStr.split("-");
					String L121_lotNO = barcode_lotidStr.substring(0,6);

					//device name only show 7 bit information by Cola. 20160817 -----Start
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.Device " + barcode_devicetypeStr_sub + "\n";
					textArea2.append("Set device ----> \"" + barcode_devicetypeStr_sub + "\"\n");
					System.out.println("Set device ----> \"" + barcode_devicetypeStr_sub + "\"\n");	
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.Device " + barcode_devicetypeStr_sub + "\n");
					//device name only show 7 bit information by Cola. 20160817 -----End  	  	    
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.LotId  " + L121_lotNO + "\n";
					textArea2.append("Set Lot ID ----> \"" + L121_lotNO + "\"\n");
					System.out.println("Set Lot ID ----> \"" + L121_lotNO + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.LotId " + L121_lotNO + "\n");						

					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TesterType DX" + "\n";
					textArea2.append("Set Tester Type ----> \"" + "DX" + "\"\n");
					System.out.println("Set Tester Type ----> \"" + "DX" + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TesterType DX" + "\n");

					if ( JobPathStr != null ){
						cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.FileNameRev " + JobPathStr + "\n";
						textArea2.append("Set job name ----> \"" + JobPathStr + "\"\n");
						System.out.println("Set job name ----> \"" + JobPathStr + "\"\n");	
						javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.FileNameRev " + JobPathStr + "\n");
					} 

					if ( RTNumStr2 != null ){

						if (RTNumStr2 == "")
							RTNumStr2 = "P1";
						cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.ActiveFlowName " + RTNumStr2 + "\n";
						textArea2.append("Set flow id ----> \"" + RTNumStr2 + "\"\n");
						System.out.println("Set flow id ----> \"" + RTNumStr2 + "\"\n");	
						javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.ActiveFlowName " + RTNumStr2 + "\n");
					} 

				}

				if(barcode_customerStr.equals("F186") || barcode_customerStr.equals("F191"))
				{		 
					//--- barcode_stationStr input to flowID
					if ( barcode_stationStr != null ){

						cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.ActiveFlowName " + barcode_stationStr + "\n";
						textArea2.append("Set flow id ----> \"" + barcode_stationStr + "\"\n");
						System.out.println("Set flow id ----> \"" + barcode_stationStr + "\"\n");	
						javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.ActiveFlowName " + barcode_stationStr + "\n");
					} 
					//--- job name input to job_rev ---
					if ( JOB_REV_Str_Rcp != null ){

						cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.FileNameRev " + JOB_REV_Str_Rcp + "\n";
						textArea2.append("Set job version ----> \"" + JOB_REV_Str_Rcp + "\"\n");
						System.out.println("Set job version ----> \"" + JOB_REV_Str_Rcp + "\"\n");	
						javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.FileNameRev " + JOB_REV_Str_Rcp + "\n");
					}
					/*	    	//--- sales ID input to proc_id ---
	    		 if ( F191_SALESID != null ){

	    			 cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.FabId " + F191_SALESID + "\n";
	    			 textArea2.append("Set proc_id ----> \"" + F191_SALESID + "\"\n");
	    			 System.out.println("Set proc_id ----> \"" + F191_SALESID + "\"\n");	
	    			 javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.FabId " + F191_SALESID + "\n");
	    		 }          
	    		 //--- "wafer" input to Package ---	    	 	
	    		 cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.Package " + "wafer" + "\n";
	    		 textArea2.append("Set Package ----> \"" + "wafer" + "\"\n");
	    		 System.out.println("Set Package ----> \"" + "wafer" + "\"\n");	
	    		 javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.Package " + "wafer" + "\n");
					 */
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TesterType DX" + "\n";
					textArea2.append("Set Tester Type ----> \"" + "DX" + "\"\n");
					System.out.println("Set Tester Type ----> \"" + "DX" + "\"\n");
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TesterType DX" + "\n");

					//--- device input to familyID ---
					if ( barcode_devicetypeStr != null ){

						cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.ProdId " + barcode_devicetypeStr + "\n";
						textArea2.append("Set family_id ----> \"" + barcode_devicetypeStr + "\"\n");
						System.out.println("Set family_id ----> \"" + barcode_devicetypeStr + "\"\n");	
						javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.ProdId " + barcode_devicetypeStr + "\n");
					}

					//--- "SIGURD HK" input to facil_id---  		
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TestFacility " + "SIGURD HK" + "\n";
					textArea2.append("Set facil_id ----> \"" + "SIGURD HK" + "\"\n");
					System.out.println("Set facil_id ----> \"" + "SIGURD HK" + "\"\n");	
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TestFacility " + "SIGURD HK" + "\n");  	              

					//--- "SIGURD HK" input to floor_id---  		
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.TestFloor " + "SIGURD HK" + "\n";
					textArea2.append("Set floor_id ----> \"" + "SIGURD HK" + "\"\n");
					System.out.println("Set floor_id ----> \"" + "SIGURD HK" + "\"\n");	
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.TestFloor " + "SIGURD HK" + "\n");	     
				}
			} else if (testTypeStr.equalsIgnoreCase("Final")){		
				
				if(barcode_customerStr.equals("L227")){ //20170710
					//--- Summary SUBLOT ID ---
					if (!barcode_SPIL_LOTNO_Str.equals("")){
						cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.SublotId " + barcode_SPIL_LOTNO_Str + "\n";
						textArea2.append("Set SubLot ID ----> \"" + barcode_SPIL_LOTNO_Str + "\"\n");
						System.out.println("Set Sublot ID ----> \"" + barcode_SPIL_LOTNO_Str + "\"\n");	
						javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.SublotId " + barcode_SPIL_LOTNO_Str + "\n");
					}
					
					//--- Summary ACTIVE FLOW ---
					cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.ActiveFlowName " + "FT" + "\n";
					textArea2.append("Set flow id ----> \"" + "FT" + "\"\n");
					System.out.println("Set flow id ----> \"" + "FT" + "\"\n");	
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.ActiveFlowName " + "FT" + "\n");
				
					//--- Summary PROBER/HANDLER ---
					if (!barcode_handleridStr.equals("")){
						cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.HandlerType " + barcode_handleridStr + "\n";
						textArea2.append("Set Hand Type ----> \"" + barcode_handleridStr + "\"\n");
						System.out.println("Set Hand Type ----> \"" + barcode_handleridStr + "\"\n");	
						javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.HandlerType " + barcode_handleridStr + "\n");
					} 
				}
			}
			
			//--- wafer number ---
			/*	     if ( barcode_programStr != null ){
		  cmd += "cex -t " + hostnameStr + " -c set_exp  TestProgData.SublotId " + "1" + "\n";
	          textArea2.append("Set wafer number ----> \"" + "1" + "\"\n");
		  System.out.println("Set wafer number ----> \"" + "1" + "\"\n");	
    	          javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp  TestProgData.SublotId " + "1" + "\n");

	        }*/	   
			
//			FTsummarySTDFsetup();
			
			//--- SetDatalog() -----------------------------
			if(testTypeStr.equalsIgnoreCase("Wafer")){
				// DlogFileStr = TLotSerStr  + "_${DlogSetupTime}";
				//tmpStr0 = barcode_devicetypeStr + "_" + getDateTime2();
				tmpStr0 = STDF_start_time + "_" + barcode_lotidStr + "_" + barcode_stationStr + "D__" + barcode_sgidStr;

				// DlogFileStr = "${WaferId}_${DlogSetupTime}"; // ${WaferId}  and ${DlogSetupTime}, these are  unison  default system variable
				// =============== SetSTDFFile =========================================

				//     if(barcode_testeridStr.equals("DX-12") || barcode_testeridStr.equals("DX-13") || barcode_testeridStr.equals("DX-14") || barcode_testeridStr.equals("DX-15") || barcode_testeridStr.equals("DX-16")){

				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:STDFV4 "+ "/tmp/" + tmpStr0 + ".std" + "\n";
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:STDFV4 "+ "/tmp/" + tmpStr0 + ".std" + "\n";
				//     }else{
				//	    		cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:STDFV4 "+ userPathforCP + tmpStr0 + ".std" + "\n";
				//          tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:STDFV4 "+ userPathforCP + tmpStr0 + ".std" + "\n";    
				//     }
			} else if(testTypeStr.equalsIgnoreCase("Final")){
//				JOptionPane.showMessageDialog(null, "1.STDfileStr = " + STDfileStr);
				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:STDFV4 " + STDfileStr + "\n";
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:STDFV4 " + STDfileStr + "\n";
			}

			javaExecSystemCmd(tmpStr);    	    
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);

			System.out.println("@@@ STDF path: barcode_devicetypeStr = " + barcode_devicetypeStr + "\n");	    
			System.out.println("@@@ STDF path: userPathforCP         = " + userPathforCP + "\n");
			System.out.println("@@@ STDF path: tmpStr0               = " + tmpStr0 + "\n");


			// System.out.println("@@@ testTypeStr ="+testTypeStr+"=\n");

			//testTypeStr  //--hh
			if(testTypeStr.equalsIgnoreCase("Wafer")){
				System.out.println("@@@ Now is at "+testTypeStr+" setting STDF file"+" \n");
				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_freq -m LTXC_Datalog:STDFV4 " + "Wafer" + "\n";
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_freq -m LTXC_Datalog:STDFV4 " + "Wafer" + "\n";
			} else if(testTypeStr.equalsIgnoreCase("Final")){
				System.out.println("@@@ Now is at "+testTypeStr+" setting STDF file"+" \n");
				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_freq -m LTXC_Datalog:STDFV4 " + "Final" + "\n";
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_freq -m LTXC_Datalog:STDFV4 " + "Final" + "\n";
			}
			    
			javaExecSystemCmd(tmpStr);	    
			System.out.println(tmpStr); 	
			textArea2.append(tmpStr);

		// =============== SetAsciiFile ==========================================Start
			if(testTypeStr.equalsIgnoreCase("Wafer")){
				DlogFileStr = STDF_start_time + "_" + barcode_lotidStr + "_" + barcode_stationStr + "D_" + "_" + barcode_sgidStr;	
				//cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:ASCII " + userPathforCP + DlogFileStr + ".txt" + "\n"; old path
				//tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:ASCII " + userPathforCP + DlogFileStr + ".txt" + "\n"; old path

				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:ASCII " + "/tmp/" + DlogFileStr + ".txt" + "\n"; //change RealTimelog path to local by Cola. 20170109
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:ASCII " + "/tmp/" + DlogFileStr + ".txt" + "\n";
			} else if(testTypeStr.equalsIgnoreCase("Final")){
//				JOptionPane.showMessageDialog(null, "2.STDfileStr = " + STDfileStr);
				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:ASCII " + TXTfileStr + "\n";
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_destination LTXC_Datalog:ASCII " + TXTfileStr + "\n";
			}
			javaExecSystemCmd(tmpStr);   	    
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);	

			System.out.println("@@@ TXT path: userPathforCP      = " + userPathforCP + "\n");
			System.out.println("@@@ TXT path: DlogFileStr        = " + DlogFileStr + "\n");
			System.out.println("@@@ TXT path: barcode_lotidStr   = " + barcode_lotidStr + "\n");


			//testTypeStr  //--hh
			if(testTypeStr.equalsIgnoreCase("Wafer")){            
				System.out.println("@@@ Now is at "+testTypeStr+" setting ASCII file"+" \n");
				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_freq -m LTXC_Datalog:ASCII " + "Wafer" + "\n";
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_freq -m LTXC_Datalog:ASCII " + "Wafer" + "\n";
			} else if(testTypeStr.equalsIgnoreCase("Final")){            
				System.out.println("@@@ Now is at "+testTypeStr+" setting ASCII file"+" \n");
				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_freq -m LTXC_Datalog:ASCII " + "Final" + "\n";
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_freq -m LTXC_Datalog:ASCII " + "Final" + "\n";
			}
			  
			javaExecSystemCmd(tmpStr);	    
			System.out.println(tmpStr); 	
			textArea2.append(tmpStr);
		// =============== SetAsciiFile ==========================================End
			
			if(testTypeStr.equalsIgnoreCase("Final")){	//for FT summary. 20170619
//				JOptionPane.showMessageDialog(null, "3.summaryfileStr = " + summaryfileStr);
				cmd   += "cex -t " + hostnameStr + " -c evx_dlog_file_destination Sigurd_SBC:ASCII "+ summaryfileStr + "\n";
				tmpStr = "cex -t " + hostnameStr + " -c evx_dlog_file_destination Sigurd_SBC:ASCII "+ summaryfileStr + "\n";
				
				javaExecSystemCmd(tmpStr);	    
				System.out.println(tmpStr); 	
				textArea2.append(tmpStr);
			}
			//  cmd   += "pwd \n";        //--hh
			//  cmd   += "sleep 20 \n";   //--hh

			dmdFW.write(cmd);  //--hh
			dmdFW.close();     //--hh


			tmpStr = "Save DX script to File: \n" + outfStr + "\n";  //--hh
			System.out.println(tmpStr);
			textArea2.append(tmpStr);


			//        try {    		

			//    		javaExecSystemCmd2("touch /tmp/barcode_file/userFlag.txt",500); 
			//    		javaExecSystemCmd2("chmod 777 /tmp/barcode_file/userFlag.txt",500);

			//        barcode_OI_file = new File(checkSocketData);
			//        FileWriter myFW;

			//				barcode_OI_content = "lotID_Str=" + barcode_lotidStr + "\n";
			//				barcode_OI_content += "TestStation_Str=" + barcode_stationStr + "\n";
			//				barcode_OI_content += "runCardStr=" + barcode_sgidStr + "\n";
			//				barcode_OI_content += "OI_stdf_file_path=" + "/tmp/" + tmpStr0 + ".std" + "\n";				
			//				barcode_OI_content += "final_stdf_file_path=" + "/dx_summary/CP/" + barcode_customerStr +"/" + barcode_devicetypeStr__ + "\n";	
			//				barcode_OI_content += "final_summary_file_path=" + "/dx_summary/CP/" + barcode_customerStr +"_summary/" + barcode_devicetypeStr__ + "\n";
			//        myFW = new FileWriter(barcode_OI_file,true);
			//        myFW.write(barcode_OI_content);
			//        myFW.close();

			//        }catch (java.io.IOException err) {

			//            tmpStr = "<Exception> saveMessage: FileWriter: " + err + "\n";
			//            System.err.println(tmpStr);
			//            textArea2.append(tmpStr);
			//        }	    

		} catch (java.io.IOException err) {
			status = false;
			tmpStr = "<Exception> genDmdCmdFile: FileWriter: \n" + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
		}

		status = true;    //--hh   // sim
		return status;

	}

	public void FTsummarySTDFsetup(){
		
/*		if(EfuseFlag&&testTypeStr.equalsIgnoreCase("Wafer")&&barcode_customerStr.equals("L022")){
			tmpStr += "UserFlagSetInt  EfuseFlagBarcode 1 \n";
		}
		else{
			tmpStr += "UserFlagSetInt  EfuseFlagBarcode 0 \n";
		}          
		//for MTK end
		if(EfuseFlag&&RTStr.equals("A1")){
			tmpStr += "UserFlagSetInt  EfuseFlagBarcode 1 \n";
		}
		else{
			tmpStr += "UserFlagSetInt  EfuseFlagBarcode 0 \n";
		}
		tmpStr += "UserFlagSetInt  DataCollection_buffer_flag 1 \n";		//20131028 dataCollection buffer check flag
		//20120828 start

		if(barcode_customerStr.equals("L176")){

			barcode_lotidStr	= barcode_lotidStr.replace(';', '#');					
			LotStr = LotStr.replace(';', '#');           	
		}            

		tmpStr += "UserFlagSetString barcode_stationStr " + "\"" + barcode_stationStr + "\"" +"\n";	//20130724 CP .std file naming rule changed by ChiaHui  
		tmpStr += "UserFlagSetString LBC1_DATA " + "\"" + barcode_LBC1Str + "\"" +"\n";
		tmpStr += "UserFlagSetString LBC2_DATA " + "\"" + barcode_LBC2Str + "\"" +"\n";
		tmpStr += "UserFlagSetString SG_NUMBER " + "\"" + barcode_sgidStr + "\"" +"\n";
		tmpStr += "UserFlagSetInt MTKFlag " + MTKFlag + "\n";
		tmpStr += "UserFlagSetString recipeFileComp " + "\"" + recipeFileforComp + "\"" +"\n";
		tmpStr += "UserFlagSetString loadFileComp " + "\"" + loadFileforComp + "\"" +"\n";
		tmpStr += "UserFlagSetString buf_lotNum " + "\"" + barcode_lotidStr + "\"" +"\n";
		barcode_CorrlotidStr=barcode_lotidStr+ "_CORR";
		tmpStr += "UserFlagSetString buf_CorrlotNum " + "\"" + barcode_CorrlotidStr + "\"" +"\n";

		tmpStr += "UserFlagSetString autoSendPathCP " + "\"" + autoSendPathCP_MAP + "\"" +"\n";
		tmpStr += "UserFlagSetInt autoSend_CP " + autoSend_CP + "\n";
		tmpStr += "UserFlagSetInt mailFlag " + mailFlag + "\n";
		tmpStr += "UserFlagSetString mailStr " + "\"" + mailStr + "\"" +"\n"; //20120828 end
		//Add F186 InfoFile data to CP Datacollection by Cola. 20160525-----Start
		if(testTypeStr.equalsIgnoreCase("Wafer") && barcode_customerStr.equals("F186")){          	
			tmpStr += "UserFlagSetString PROBER " + "\"" + barcode_proberidStr + "\"" +"\n";
			tmpStr += "UserFlagSetString PROBERID " + "\"" + barcode_handleridStr + "\"" +"\n";
			tmpStr += "UserFlagSetString F186_SIFOLOTID " + "\"" + barcode_FormerLOTID_Str + "\"" +"\n";
			tmpStr += "UserFlagSetString F186_MANUFACTURINGID " + "\"" + barcode_DESIGN_TYPE_Str + "\"" +"\n";
			tmpStr += "UserFlagSetString F186_TESTMODE " + "\"" + getF186InfoFileData_TestMode() + "\"" +"\n";
			tmpStr += "UserFlagSetString F186_TESTTYPE " + "\"" + "N" + "\"" +"\n";
			tmpStr += "UserFlagSetString F186_PRODUCTIONSITE " + "\"" + "SDK" + "\"" +"\n";
			tmpStr += "UserFlagSetString F186_REVISION " + "\"" + JOB_REV_Str_Rcp + "\"" +"\n";
			if(!barcode_customerTO_Str.equals(""))
				tmpStr += "UserFlagSetString F186_customerTO " + "\"" + barcode_customerTO_Str + "\"" +"\n";
			if(barcode_programStr.endsWith(".job"))
				tmpStr += "UserFlagSetString F186_TESTPROGRAM " + "\"" + barcode_programStr.substring(0,barcode_programStr.length()-4) + "\"" +"\n";
			else
				tmpStr += "UserFlagSetString F186_TESTPROGRAM " + "\"" + barcode_programStr + "\"" +"\n";
		} //Add F186 InfoFile data to CP Datacollection by Cola. 20160525-----End

		if(barcode_customerStr.equals("L010")||((barcode_customerStr.equals("L022")||barcode_customerStr.equals("L129")||barcode_customerStr.equals("L320")||barcode_customerStr.equals("F054")||barcode_customerStr.equals("L389")||barcode_customerStr.equals("F154")||barcode_customerStr.equals("F167")) && testTypeStr.equalsIgnoreCase("Final"))){	//20151118 MTK serios job name remove file name extension

			String jobNameWithoutFileNameExtension[] = TPStr.split(".job");
			tmpStr += "ProductionSetJobName " + jobNameWithoutFileNameExtension[0] + "\n";       //20090827            	
		}else{	
			tmpStr += "ProductionSetJobName " + TPStr + "\n";       //20090827			
		}

		if(testTypeStr.equalsIgnoreCase("Wafer")){ //Insert HandlerID to STDF by Cola. 20160503
			tmpStr += "ProductionSetHandlerType " + "\"" + barcode_proberidStr + "\"" +"\n";
			tmpStr += "ProductionSetHandlerID " + "\"" + barcode_handleridStr + "\"" +"\n";
			//				tmpStr += "UserFlagSetString SG_OCR_ID " + "\"" + barcode_OCR_ID_Str + "\"" +"\n";	//20170328
			tmpStr += "UserFlagSetInt SG_DataCollection_map_start_link " + DC_MapStartLink_Flag + "\n"; //20170328
			tmpStr += "UserFlagSetInt SG_DataCollection_map_end_link " + DC_MapEndLink_Flag + "\n"; //20170328
			tmpStr += "UserFlagSetInt SG_DataCollection_device_eot_link " + DC_DeviceEotLink_Flag + "\n"; //20170328
			tmpStr += "UserFlagSetInt SG_DataCollection_device_eot_runAll " + DC_DeviceEotRunAll_Flag + "\n"; //20170328
		} else if(testTypeStr.equalsIgnoreCase("Final")) {
			String barcode_handleridStr_split[] = barcode_handleridStr.split("-");
			tmpStr += "ProductionSetHandlerType " + "\"" + barcode_handleridStr_split[0] + "\"" +"\n";
			//            	if(barcode_customerStr.equals("F191"))
			//            		tmpStr += "ProductionSetHandlerID " + "\"" + barcode_handleridStr_split[1] + "\"" +"\n";
			//            	else
			tmpStr += "ProductionSetHandlerID " + "\"" + barcode_handleridStr + "\"" +"\n";
		}
		if(barcode_customerStr.equals("L022")||barcode_customerStr.equals("L129")||barcode_customerStr.equals("L320")||barcode_customerStr.equals("F054")||barcode_customerStr.equals("L389")||barcode_customerStr.equals("F154")||barcode_customerStr.equals("F167")){
			//OTProxy FlowID get. 20170412-----Start
			String FlowID = "", OTProxyA1Count = "";
			if(testTypeStr.equalsIgnoreCase("Final")){
				if(sumRepNumStr.substring(0, 1).equals("0"))
					OTProxyA1Count = sumRepNumStr.substring(1, 2);
				else
					OTProxyA1Count = sumRepNumStr;
				if(RTStr.equals("A1")){	
					FlowID = barcode_stationStr + "-" + OTProxyA1Count;
				} else if(RTStr.equals("EQC")){
					if(EQC_QNum_Str.startsWith("QA1")) {
						FlowID = EQC_bin_Str + "-" + OTProxyA1Count;;
					} else{
						FlowID = EQC_bin_Str + "_" + EQC_QNum_Str2 + QrtBinStr;
					}
				} else if(RTStr.equals("HW"))FlowID = "HW";
				else if(RTStr.equals("CORR"))FlowID = "CORR";
				else FlowID = barcode_stationStr + "_" + RTNumStr2 + rtBinSumStr; //RT

			} else if(testTypeStr.equalsIgnoreCase("Wafer")){

			}
			if(!FlowID.equals(""))	//20170412
				tmpStr += "ProductionSetTestFlowID " + "\"" + FlowID + "\"" + "\n";
			//OTProxy FlowID get. 20170412-----End
			//20151118 MTK STDF new format
			tmpStr += "ProductionSetPartType " + "\"" + barcode_devicetypeStr + "\"" +"\n";
			tmpStr += "ProductionSetTestCode " + "\"" + barcode_stationStr + "\"" +"\n";          	
			tmpStr += "ProductionSetSublotID " + "\"" + barcode_lotidStr + "\"" +"\n"; //Add by Chia for MTK STDF. 20160309

			tmpStr += "ProductionSetTestFacilityID " + "\"" + "Sigurd" + "\"" +"\n";  //Add FT STDF Rule. by Cola 20160412
			tmpStr += "ProductionSetUserText " + "\"" + "MTK" + "\"" +"\n";
			tmpStr += "ProductionSetJobRevision " + "\"" + barcode_programStr + "\"" +"\n";

		}

		if(barcode_customerStr.equals("F186") || barcode_customerStr.equals("F191")){ //Add data to F186 STDF by Cola. 20160531
			//            	tmpStr += "ProductionSetRetestCode " + "\"" +  + "\"" +"\n";
			if(!JOB_REV_Str_Rcp.equals(""))
				tmpStr += "ProductionSetJobRevision " + "\"" + JOB_REV_Str_Rcp + "\"" +"\n";
			//            	tmpStr += "ProductionSetProcessID " + "\"" +  + "\"" +"\n";      //SALESID
			//            	tmpStr += "ProductionSetPartType " + "\"" +  + "\"" +"\n";
			//            	tmpStr += "ProductionSetPackageType " + "\"" +  + "\"" +"\n";
			if(!F186_RTbin.equals(""))
				tmpStr += "ProductionSetRetestCode " + "\"" + F186_RTbin + "\"" +"\n";
			tmpStr += "ProductionSetFamilyID " + "\"" + barcode_devicetypeStr + "\"" +"\n";
			//            	tmpStr += "ProductionSetDesignRevision " + "\"" +  + "\"" +"\n";
			tmpStr += "ProductionSetTestFacilityID " + "\"" + "SIGURD HK" + "\"" +"\n";
			tmpStr += "ProductionSetTestFloorID " + "\"" + "SIGURD HK" + "\"" +"\n";
			tmpStr += "ProductionSetTestFlowID " + "\"" + barcode_stationStr + "\"" +"\n";
			//            	tmpStr += "ProductionSetDateCode " + "\"" +  + "\"" +"\n";
		}
		tmpStr += "pause off \n";
		//tmpStr += "override off \n";
		tmpStr += "log -all eot \n";
		tmpStr += "DatalogSetQueue enable \n";
		tmpStr += "ProductionSetLotID " + "\"" + LotStr + "\"" + "\n";

		tmpStr += "ProductionSetOperatorName " + "\"" + barcode_opidStr + "\"" + "\n";
		tmpStr += "ProductionSetTestMode " + "\"" + barcode_stationStr + "\"" + "\n";

		//	    tmpStr += "ProductionSetTestCode " + "\"" + barcode_stationStr + "\"" +"\n"; 
		//	    tmpStr += "ProductionSetTestMode " + "\"" + "P" + "\"" +"\n";
		//	    tmpStr += "ProductionSetTestFacilityID " + "\"" + "SIGURD" + "\"" +"\n"; 

		//			if (testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("L050") && barcode_devicetypeStr.equals("M3821-ALAAA-A1BG0X")){
		//							tmpStr += "UserFlagSetString part " + "\"" + "M3821_ALAAA_A1BG0X" + "\"" +"\n";

		//			}else if (testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("L050") && barcode_devicetypeStr.equals("M3821-ALCAA-A1BG0X")){
		//							tmpStr += "UserFlagSetString part " + "\"" + "M3821_ALCAA_A1BG0X" + "\"" +"\n"; 	

		//			}else if (testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("L050")){
		//							tmpStr += "UserFlagSetString part " + "\"" + barcode_devicetypeStr + "\"" +"\n"; 								
		//			}

		if (testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("L050")){
			tmpStr += "UserFlagSetString part " + "\"" + barcode_devicetypeStr + "\"" +"\n";
			tmpStr += "UserFlagSetString po " + "\"" + barcode_orderNO_Str + "\"" +"\n"; //add by Cola. 20161103
		}

		if (testTypeStr.equalsIgnoreCase("Wafer") && barcode_customerStr.equals("F137")){

			String[] token = barcode_devicetypeStr.split("-");
			String F137_lotNO = barcode_lotidStr.substring(0,6);
			if (barcode_devicetypeStr.equals("NSWSKN802#D") || barcode_devicetypeStr.equals("NSCSHN802#A")) //This Device lot No. is 8 digital add by Cola. 20161101
				F137_lotNO = barcode_lotidStr.substring(0,8);
			tmpStr += "ProductionSetTestCode " + "\"" + F137_lotNO + "\"" +"\n";  								
			tmpStr += "ProductionSetPartType " + token[0] + "\n";	
			tmpStr += "ProductionSetRetestCode " + "p1" + "\n";	
		}

		if (testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("F128")){	//20130912 F128 req
			tmpStr += "ProductionSetPartType " + "\"" + barcode_devicetypeStr + "\"" +"\n"; 	
			//							tmpStr += "ProductionSetDIBBoardID " + "\"" + LBStr + "\"" +"\n";							
		}
		if (testTypeStr.equalsIgnoreCase("Final"))						
			tmpStr += "ProductionSetDIBBoardID " + "\"" + LBStr + "\"" +"\n";		//20140814 STDF LB & probe crd nme by Chi Hui

		if (testTypeStr.equalsIgnoreCase("Wafer"))
			tmpStr += "ProductionSetProbeCardID " + "\"" + LBStr + "\"" +"\n";		//20140814 STDF LB & probe crd nme by Chi Hui

		if (barcode_temperatureStr.equals("")==false) {
			tmpStr += "ProductionSetTemperature " + barcode_temperatureStr +"\n";
		}

		tmpStr += "ProductionSetUserValue " + "\"SG_RecipeFile            \" " + recipeFile + "\n";
		ProductCheckSummary += "\"SG_RecipeFile            \" " + recipeFile + "\n";	//check summary issue add by ChiaHui 20130502
		// for MTK
		if(barcode_customerStr.equals("L022")||barcode_customerStr.equals("L129")||barcode_customerStr.equals("L320")||barcode_customerStr.equals("F054")||barcode_customerStr.equals("L389")||barcode_customerStr.equals("F154")||barcode_customerStr.equals("F167")){
			tmpStr += "ProductionSetUserValue " + "\"SG_LoadFile              \" " + correctloadfileStr + "\n";
			ProductCheckSummary += "\"SG_LoadFile              \" " + correctloadfileStr + "\n";	//check summary issue add by ChiaHui 20130502
		}
		// for MTK end 
		tmpStr += "UserFlagSetInt csicStdfBarFlag 0\n";//default 0
*/
		STDFlag = false; //20090413
		STDfiletmpStr = "";
		STDfileStrTime = STDF_start_time;
		String TestStationStr = "";//2011/07/12 
		if(RTStr.equals("A1")){    //20100428
			STDFlag = true;

			//20140115 STDF

			if(barcode_customerStr.equals("F128")) {//20110712	//20130108 F087 ->F128
				Date myDateTime = new Date();
				myformatter = new SimpleDateFormat("yyyyMMddHHmmss");
				mydatetime = myformatter.format(myDateTime); 
				String dateTime = mydatetime;
				if(barcode_programStr.endsWith("job"))  //20110831
					STDfiletmpStr = autoLocalResultPath + LotStr + "_" +barcode_stationStr + "-" +sumRepNumStr + "_" + barcode_programStr.substring(0,barcode_programStr.length()-4) +  "_" + barcode_devicetypeStr + "_" + dateTime;//20110712
				else
					STDfiletmpStr = autoLocalResultPath + LotStr + "_" +barcode_stationStr + "-" +sumRepNumStr + "_" + barcode_programStr +  "_" + barcode_devicetypeStr + "_" + dateTime;//20110712
				TestStationStr = barcode_stationStr + "-" +sumRepNumStr;
			}
			else{

				//               	  if(barcode_customerStr.equals("L022")||barcode_customerStr.equals("L129")||barcode_customerStr.equals("L320")||barcode_customerStr.equals("F054")||barcode_customerStr.equals("L389")){
				STDfiletmpStr = autoLocalResultPath + STDfileStrTime + "_" + LotStr + "_" + barcode_stationStr + "D_" + "R0_ALL_" + SGStr; //20100428
				//                	  }
				//                	   else{
				//                    STDfiletmpStr = autoLocalResultPath + LotStr + "_" +barcode_stationStr + "D_" + "R0_ALL_" + SGStr + "_";//20100428
				//                    }
			}
		}
		else if(RTStr.equals("HW")){
			STDFlag = false;

			if(barcode_customerStr.equals("F128"))
				TestStationStr = "HW_";
		}
		else if(RTStr.equals("EQC")){  //20100128
			STDFlag = true;
			STDfiletmpStr = userSTDFfinalPath + STDfileStrTime+ "_" + LotStr + "_" + barcode_stationStr + "D";   

			if(EQC_QNum_Str.startsWith("QA1")){ //Chang EQC STDF Naming to MTK format by Cola. 20160608
				STDfiletmpStr += "_R0_ALL_";
			}
			else{//QRT
				//STDF sample:20160729092821_ASSRCK_EQC1D_R1_3+4+5_HF1921607035.std.gz
				//3+4+5 is the result for Srting STDF_QrtBinStr
				String STDF_QrtBinStr = "", STDF_QrtBinStr_tmp = "", tempStr[]; //add by Cola for QRT MTK STDF naming rule. 20161104-----Start
				STDF_QrtBinStr_tmp = QrtBinStr.replaceAll("B", "");			
				if(STDF_QrtBinStr_tmp.indexOf("-")!=-1){ //String include "-" when ANF RT
					tempStr = STDF_QrtBinStr_tmp.split("-");
					STDF_QrtBinStr_tmp = tempStr[tempStr.length-1];
				}		
				for(int i=0 ; i<STDF_QrtBinStr_tmp.length() ; i++)
					STDF_QrtBinStr += STDF_QrtBinStr_tmp.substring(i, i+1) + "+";
				STDF_QrtBinStr = STDF_QrtBinStr.substring(0, STDF_QrtBinStr.length()-1); //last Bin don't add "+"
				STDfiletmpStr += "_" + EQC_QNum_Str2.replaceAll("T", "") + "_" + STDF_QrtBinStr + "_"; //add by Cola for QRT MTK STDF naming rule. 20161104-----End
				//					STDfiletmpStr += "_" + EQC_QNum_Str2.replaceAll("T", "") + "_" + QrtBinStr.replaceAll("B", "") + "_";  This is old code.
			}

			//                if(EQC_bin_Str.equals("EQC1")) {
			//                    STDfiletmpStr += "EQC1_";
			//                } else if(EQC_bin_Str.equals("EQC2")) {
			//                    STDfiletmpStr += "EQC2_";
			//                } else if(EQC_bin_Str.equals("EQC3")) {
			//                    STDfiletmpStr += "EQC3_";
			//                }       
			STDfiletmpStr += SGStr;

			//20140115 STDF

			if(barcode_customerStr.equals("F128")) {//20110712
				Date myDateTime = new Date();
				myformatter = new SimpleDateFormat("yyyyMMddHHmmss");
				mydatetime = myformatter.format(myDateTime); 
				String dateTime = mydatetime;                	
				STDfiletmpStr = userSTDFfinalPath + LotStr + "_" ;//20110712
				if(EQC_bin_Str.equals("EQC1")) {
					STDfiletmpStr += "QA1-";
					TestStationStr = "QA1-";
				} else if(EQC_bin_Str.equals("EQC2")) {
					STDfiletmpStr += "QA2-";
					TestStationStr = "QA2-";
				} else if(EQC_bin_Str.equals("EQC3")) {
					STDfiletmpStr += "QA3-";
					TestStationStr = "QA3-";
				}

				if(barcode_programStr.endsWith("job"))  //20110831
					STDfiletmpStr += sumRepNumStr + "_" + barcode_programStr.substring(0,barcode_programStr.length()-4) + "_" + barcode_devicetypeStr + "_" + dateTime;   
				else
					STDfiletmpStr += sumRepNumStr + "_" + barcode_programStr + "_" + barcode_devicetypeStr + "_" + dateTime;   
				TestStationStr += sumRepNumStr;


			}

		}
		else if (RTStr.equals("CORR")) {  //20100128
			STDFlag = true;
			//20171101	
//			STDfiletmpStr = autoLocalResultPath + STDfileStrTime+ "_" + LotStr + "_" + barcode_stationStr + "DCORR_" + SGStr ;
			STDfiletmpStr = autoLocalResultPath + STDfileStrTime+ "_" + LotStr + "_" + barcode_stationStr + "-CORR-" + CORRStr_set +CORRbinStr_set+ "-" + sumRepNumStr + "_" + SGStr ;
			TestStationStr = barcode_stationStr;
		}
		else {//RT// 20100428
			STDFlag = true;
			STDfiletmpStr = userSTDFfinalPath + STDfileStrTime+ "_" + LotStr + "_" + barcode_stationStr + "D_";   //20090427 

			if(RTNumStr.equals("R1")) {
				STDfiletmpStr += "R1_";
				L010_RT_string += "_RT1";
			} else if(RTNumStr.equals("R2")) {
				STDfiletmpStr += "R2_";
				L010_RT_string += "_RT2";
			} else if(RTNumStr.equals("R3")) {
				STDfiletmpStr += "R3_";
				L010_RT_string += "_RT3";
			} else if(RTNumStr.equals("R4")) {
				STDfiletmpStr += "R4_";
				L010_RT_string += "_RT4";
			} else if(RTNumStr.equals("R5")) {
				STDfiletmpStr += "R5_";
				L010_RT_string += "_RT5";
			} else if(RTNumStr.equals("R6")) {
				STDfiletmpStr += "R6_";
				L010_RT_string += "_RT6";
			} else if(RTNumStr.equals("R7")) {  //20110209
				STDfiletmpStr += "R7_";
				L010_RT_string += "_RT7";
			} else if(RTNumStr.equals("R8")) {  //20110209
				STDfiletmpStr += "R8_";
				L010_RT_string += "_RT8";
			} else if(RTNumStr.equals("R9")) {  //20170314-----Start
				STDfiletmpStr += "R9_";
				L010_RT_string += "_RT9";
			} else if(RTNumStr.equals("R10")) {
				STDfiletmpStr += "R10_";
				L010_RT_string += "_RT10";
			} else if(RTNumStr.equals("R11")) {
				STDfiletmpStr += "R11_";
				L010_RT_string += "_RT11";
			} else if(RTNumStr.equals("R12")) {
				STDfiletmpStr += "R12_";
				L010_RT_string += "_RT12";
			} else if(RTNumStr.equals("R13")) {
				STDfiletmpStr += "R13_";
				L010_RT_string += "_RT13";
			} else if(RTNumStr.equals("R14")) {
				STDfiletmpStr += "R14_";
				L010_RT_string += "_RT14";
			} else if(RTNumStr.equals("R15")) {
				STDfiletmpStr += "R15_";
				L010_RT_string += "_RT15";
			} else if(RTNumStr.equals("R16")) {
				STDfiletmpStr += "R16_";
				L010_RT_string += "_RT16";
			} else if(RTNumStr.equals("R17")) {
				STDfiletmpStr += "R17_";
				L010_RT_string += "_RT17";
			} else if(RTNumStr.equals("R18")) {
				STDfiletmpStr += "R18_";
				L010_RT_string += "_RT18";
			} else if(RTNumStr.equals("R19")) {
				STDfiletmpStr += "R19_";
				L010_RT_string += "_RT19";
			} else if(RTNumStr.equals("R20")) {	
				STDfiletmpStr += "R20_";
				L010_RT_string += "_RT20";
			}	//20170314-----End

			System.out.println("rtBinStr:"+rtBinStr+"\n");
			System.out.println("rtBinStr:"+rtBinStr+"\n");
			System.out.println("rtBinStr:"+rtBinStr+"\n");
			//for MTK
			STDfiletmpStr += rtBinStr + SGStr;
			//for MTK end
			//STDfiletmpStr += rtBinStr + SGStr + "_";

			//20140115 STDF

			if(barcode_customerStr.equals("F128")) {//20110712
				STDfiletmpStr = userSTDFfinalPath + LotStr + "_" +barcode_stationStr + "_";
				TestStationStr = barcode_stationStr + "_";	//20130912 
				if(RTNumStr.equals("R1")) {
					STDfiletmpStr += "RT1";
					TestStationStr += "RT1";
				} else if(RTNumStr.equals("R2")) {
					STDfiletmpStr += "RT2";
					TestStationStr += "RT2";
				} else if(RTNumStr.equals("R3")) {
					STDfiletmpStr += "RT3";
					TestStationStr += "RT3";
				} else if(RTNumStr.equals("R4")) {
					STDfiletmpStr += "RT4";
					TestStationStr += "RT4";
				} else if(RTNumStr.equals("R5")) {
					STDfiletmpStr += "RT5";
					TestStationStr += "RT5";
				} else if(RTNumStr.equals("R6")) {
					STDfiletmpStr += "RT6";
					TestStationStr += "RT6";
				} else if(RTNumStr.equals("R7")) {  
					STDfiletmpStr += "RT7";
					TestStationStr += "RT7";
				} else if(RTNumStr.equals("R8")) {  
					STDfiletmpStr += "RT8";
					TestStationStr += "RT8";
				} else if(RTNumStr.equals("R9")) {  //20170314-----Start
					STDfiletmpStr += "RT9";
					TestStationStr += "RT9";
				} else if(RTNumStr.equals("R10")) {
					STDfiletmpStr += "RT10";
					TestStationStr += "RT10";
				} else if(RTNumStr.equals("R11")) {
					STDfiletmpStr += "RT11";
					TestStationStr += "RT11";
				} else if(RTNumStr.equals("R12")) {
					STDfiletmpStr += "RT12";
					TestStationStr += "RT12";
				} else if(RTNumStr.equals("R13")) {
					STDfiletmpStr += "RT13";
					TestStationStr += "RT13";
				} else if(RTNumStr.equals("R14")) {
					STDfiletmpStr += "RT14";
					TestStationStr += "RT14";
				} else if(RTNumStr.equals("R15")) {
					STDfiletmpStr += "RT15";
					TestStationStr += "RT15";
				} else if(RTNumStr.equals("R16")) {
					STDfiletmpStr += "RT16";
					TestStationStr += "RT16";
				} else if(RTNumStr.equals("R17")) {
					STDfiletmpStr += "RT17";
					TestStationStr += "RT17";
				} else if(RTNumStr.equals("R18")) {
					STDfiletmpStr += "RT18";
					TestStationStr += "RT18";
				} else if(RTNumStr.equals("R19")) {
					STDfiletmpStr += "RT19";
					TestStationStr += "RT19";
				} else if(RTNumStr.equals("R20")) {
					STDfiletmpStr += "RT20";
					TestStationStr += "RT20";
				}	//20170314-----End

				TestStationStr += "B" + rtBinStr + "-" + sumRepNumStr;
				STDfiletmpStr += "B" + rtBinStr + "-";

				Date myDateTime = new Date();
				myformatter = new SimpleDateFormat("yyyyMMddHHmmss");
				mydatetime = myformatter.format(myDateTime); 
				String dateTime = mydatetime;
				if(barcode_programStr.endsWith("job"))  //20110831
					STDfiletmpStr += sumRepNumStr + "_" + barcode_programStr.substring(0,barcode_programStr.length()-4) +  "_" + barcode_devicetypeStr + "_" + dateTime;//20110712
				else
					STDfiletmpStr += sumRepNumStr + "_" + barcode_programStr +  "_" + barcode_devicetypeStr + "_" + dateTime;//20110712


			}

		}

//		tmpStr += "DatalogSTDFSetMode off \n";
		if (STDFlag == true && STDFlogFlag ==true && testTypeStr.equalsIgnoreCase("Final")) {    // 20090506 //20101026

			if (barcode_customerStr.equals("F186")){ //Add by Cola for F186 STDF naming. 20160711				
				if (barcode_customerTO_Str.equals("INTEL-LANTIQ"))
					STDfiletmpStr += "_" + getF186InfoFileData_TestMode();
				else
					STDfiletmpStr += "_" + getF186InfoFileData_TestMode() + "_" + getF186InfoFileData_TestType();
			}
			if(barcode_customerStr.equals("L124")){ //20170222	
				String L124_orderNo = "";
				if(RTStr.equals("A1"))	//20170314----Start
					L124_orderNo = "RF" + barcode_orderNO_Str;
				else if(RTStr.equals("RT")){
					if(RTNumStr.equals("R1"))
						L124_orderNo = "RR" + barcode_orderNO_Str;
					else
						L124_orderNo = "RD" + barcode_orderNO_Str;					
				}	//20170314-----End
				STDfiletmpStr = STDfiletmpStr.substring(0,9) + L124_orderNo + "_" + STDfiletmpStr.substring(9);
//				tmpStr += "UserFlagSetString FinalSTDFname " + "\"" + STDfiletmpStr + "\"" +"\n";
			}
			TXTfileStr = STDfiletmpStr + ".txt"; //20170619 for FT
			STDfileStr = STDfiletmpStr + ".std";//20100128
//			tmpStr += "DatalogSTDFSetFileName " + STDfileStr + "\n";
//			tmpStr += "DatalogSTDFSetMode all \n";
//			tmpStr += "UserFlagSetInt csicStdfBarFlag 1\n";
		}
		//20101026
		//STDFlag for which RT station need to log .std , STDFlogFlag is used to control if logs .std file in barcode.
//		if (STDFlag == true && STDFlogFlag ==true && testTypeStr.equalsIgnoreCase("Wafer")) {    // 20090506 //20101026
//			tmpStr += "UserFlagSetString  tmpCPfilePath \"" + tmpCPFilePath +"\"\n";
//			tmpStr += "UserFlagSetString  CPfilePath \"" + userPathforCP +"\"\n";
//			tmpStr += "UserFlagSetInt csicStdfBarFlag 999\n";
//
//			File CPdir = new File(tmpCPFilePath);                                   //20090413
//			if ( !CPdir.exists() )                                                         //20090413
//			{                                                                            //20090413
//				CPdir.mkdirs();                                                          //20090413
//			}
//		}

//		if(!barcode_customerStr.equals("L010")){//jj
//			if (testTypeStr.equalsIgnoreCase("Final")) { //20090413//20100128
//				tmpStr += "ProductionSetUserValue " + "\"SG_Loadboard_ID          \" " + LBStr + "\n";
//				ProductCheckSummary += "\"SG_Loadboard_ID          \" " + LBStr + "\n";	//check summary issue add by ChiaHui 20130502
//			}
//
//			else if (testTypeStr.equalsIgnoreCase("Wafer")) {
//				tmpStr += "ProductionSetUserValue " + "\"SG_ProbeCard_ID          \" " + LBStr + "\n";
//				ProductCheckSummary += "\"SG_ProbeCard_ID          \" " + LBStr + "\n";	//check summary issue add by ChiaHui 20130502
//			}
//		}
//		else {
//			tmpStr += "UserFlagSetString  LBStr \"" + LBStr +"\"\n";
//		}

		//2012 2 for dataCollection to log real test data
		RTCheckStr = "";
		if(RTStr.equals("A1")){    //20081107
			summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + "R0_ALL" + "-" + sumRepNumStr;//20081107
			RTCheckStr = barcode_stationStr;
		}
		else if(RTStr.equals("HW")){
			summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + "HW" + "-" + sumRepNumStr;//20081107
			RTCheckStr = "HW";
		}
		else if(RTStr.equals("EQC")){//20081110//20100618
			if(EQC_QNum_Str.startsWith("QA1")) {
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "-" + sumRepNumStr;//20100120
				RTCheckStr =  EQC_bin_Str + EQC_qbin_Str;
			}
			else{//QRT
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "_" + ANFQrtBinSumStr + EQC_QNum_Str2 + QrtBinStr + "-" + sumRepNumStr;//20100120 //add ANF RTbin 20161026
				RTCheckStr =  EQC_bin_Str + EQC_qbin_Str + "_" + ANFQrtBinSumStr + EQC_QNum_Str2 + QrtBinStr;
			}
		}
		else if (RTStr.equals("CORR")) {//20081230
			summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + barcode_stationStr + "-CORR-" + CORRStr_set +CORRbinStr_set+ "-" + sumRepNumStr;//20081230  //Add Corr Bin by Cola. 20160408
			RTCheckStr =  "CORR" + CORRStr_set + CORRbinStr_set;
		}
		else {//RT

			summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + ANFrtBinSumStr + RTNumStr2 + rtBinSumStr + "-" + sumRepNumStr;//20100505 //Add ANF RTbin 20161026
			RTCheckStr =  barcode_stationStr + "_" + ANFrtBinSumStr + RTNumStr2 + rtBinSumStr;
		}
		
		summaryfileStr_MES = summaryfileStr + "_" + STDfileStrTime + ".sum"; //for MES update.
		summaryfileStr = summaryfileStr + ".sum";
		
//		tmpStr += "UserFlagSetString  summaryfileStr \"" + summaryfileStr_DataCollection +"\"\n";
//		//2012 2 end
//		tmpStr += "UserFlagSetString  CustStr \"" + barcode_customerStr +"\"\n";
//
//		tmpStr += "ProductionSetUserValue " + "\"Device Name              \" " +"\"" + barcode_devicetypeStr + "\""+ "\n"; //20081031
//		ProductCheckSummary += "\"Device Name              \" " +"\"" + barcode_devicetypeStr + "\""+ "\n";	//check summary issue add by ChiaHui 20130502
//		tmpStr += "ProductionSetUserValue " + "\"SG_Customer              \" " + barcode_customerStr + "\n";
//		ProductCheckSummary += "\"SG_Customer              \" " + barcode_customerStr + "\n";	//check summary issue add by ChiaHui 20130502
//		tmpStr += "ProductionSetUserValue " + "\"SG_Number                \" " + SGStr               + "\n";
//		ProductCheckSummary += "\"SG_Number                \" " + SGStr               + "\n";	//check summary issue add by ChiaHui 20130502
//		tmpStr += "ProductionSetUserValue " + "\"SG_LotNumber             \" " + LotStr              + "\n";
//		ProductCheckSummary += "\"SG_LotNumber             \" " + LotStr              + "\n";	//check summary issue add by ChiaHui 20130502
//		tmpStr += "ProductionSetUserValue " + "\"SG_TestProgram           \" " + TPStr               + "\n";
//		ProductCheckSummary += "\"SG_TestProgram           \" " + TPStr               + "\n";	//check summary issue add by ChiaHui 20130502
//		tmpStr += "ProductionSetUserValue " + "\"SG_TesterHostname        \" " + hostnameStr         + "\n";
		//20130604 productionSetUserValue EQU issue
		if (!barcode_KIT.equals("")){
//			tmpStr += "ProductionSetUserValue " + "SG_Barcode_KIT           " + " " + barcode_KIT + "\n";
			checkSocketData += "SG_Barcode_KIT = " + barcode_KIT         + "\n";            
		}
		if (!barcode_LB.equals("")){
			if(testTypeStr.equalsIgnoreCase("Wafer")){														
//				tmpStr += "ProductionSetUserValue " + "SG_Barcode_PC            " + " " + barcode_LB + "\n";
				checkSocketData += "SG_Barcode_PC = " + barcode_LB          + "\n";
				if(!LBNo_with_OldLBName.equals("")){ //Add by Cola. 20160311
//					tmpStr += "ProductionSetUserValue " + "SG_PC_DeviceNo            " + " " + "\"" + LBNo_with_OldLBName + "\"" + "\n"; 
//					if(barcode_customerStr.equals("F192")) //Add by Cola. 20160308										
//						tmpStr += "ProductionSetProbeCardID " + "\"" + LBNo_with_OldLBName + "\"" + "\n";
				}

			}
			if(testTypeStr.equalsIgnoreCase("Final")){														

//				tmpStr += "ProductionSetUserValue " + "SG_Barcode_LB            " + " " + barcode_LB + "\n";
				if(!LBNo_with_OldLBName.equals("")){ //Add by Cola. 20160311
//					tmpStr += "ProductionSetUserValue " + "SG_LB_DeviceNo            " + " " + "\"" + LBNo_with_OldLBName + "\"" + "\n"; //Add by Cola. 20160308
//					tmpStr += "ProductionSetLoadBoardID " + "\"" + LBNo_with_OldLBName + "\"" + "\n"; //Add by Cola. 20160308
				}
				if(gpibFTType.equalsIgnoreCase("CHKMATCH?") || NewHandler_GPIBnoSupport){ //Add L178 NewHandler_GPIBnoSupport check by Cola. 20160823
					gpibFTType = "NA";
					gpibFTTemp = "NA";
					gpibFTSoakTime = "NA";
					gpibFTSiteMap = "NA";
				}
//				tmpStr += "ProductionSetUserValue " + "\"FT_Handler_Name_GPIB    \" " + gpibFTType + "\n";
//				tmpStr += "ProductionSetUserValue " + "\"FT_Handler_Temp_GPIB    \" " + " " + gpibFTTemp + "\n";										
//				tmpStr += "ProductionSetUserValue " + "\"FT_Handler_SockTime_GPIB\" " + " " + gpibFTSoakTime + "\n";										
//				tmpStr += "ProductionSetUserValue " + "\"FT_Handler_SiteMap_GPIB \" " + " " + gpibFTSiteMap + "\n";										
				checkSocketData += "SG_Barcode_LB = " + barcode_LB          + "\n";  

				if(barcode_customerStr.equals("L176") || barcode_customerStr.equals("L400")){	//20140429 L176_cus
					JOptionPane.showMessageDialog(null, L176_FT_corr_reason);
//					tmpStr += "ProductionSetUserValue " + "\"FT_L176_corr_reason \" " + "\"" + L176_FT_corr_reason + "\"" + "\n";
				}
			}
		}        

		if(barcode_customerStr.equals("F128")) {//20110712
//			tmpStr += "ProductionSetUserValue " + "\"SG_TestStation           \" " + TestStationStr  + "\n";
//			ProductCheckSummary += "\"SG_TestStation           \" " + TestStationStr  + "\n";	//check summary issue add by ChiaHui 20130502
		}
		else {
//			tmpStr += "ProductionSetUserValue " + "\"SG_TestStation           \" " + barcode_stationStr  + "\n";
//			ProductCheckSummary += "\"SG_TestStation           \" " + barcode_stationStr  + "\n";	//check summary issue add by ChiaHui 20130502
		}

		if(barcode_customerStr.equals("L010")){//jj
			//tmpStr += "ProductionSetLotID " + barcode_lotidStr_sub +"\n";
			if(testTypeStr.equalsIgnoreCase("Wafer")){
//				tmpStr += "UserFlagSetString LotID \"" + barcode_lotidStr_sub + "\"\n";
//				tmpStr += "UserFlagSetString DeviceType \"" + barcode_devicetypeStr_sub  + "\"\n";
//				tmpStr += "UserFlagSetString Notch \"" + NotchStr  + "\"\n";
//				tmpStr += "UserFlagSetString Station \"" + barcode_stationStr  + "\"\n";
//				tmpStr += "ProductionSetUserValue " + "\"Notch Direction            \" \"" + "Down"           + "\"\n";//20131216
//				tmpStr += "ProductionSetUserValue " + "\"Probecard No.            \" \"" + barcode_LB           + "\"\n";//20131216
//				tmpStr += "ProductionSetUserValue " + "\"Product code            \" \"" + barcode_devicetypeStr_sub           + "\"\n";//20131216
//				tmpStr += "ProductionSetUserValue " + "\"Production Station            \" \"" + L010_RT_string           + "\"\n";//20131216
			}

			if(testTypeStr.equalsIgnoreCase("Final")){
//				tmpStr += "UserFlagSetString LotID \"" + barcode_lotidStr_sub  + "\"\n";
//				tmpStr += "UserFlagSetString DeviceType \"" + barcode_devicetypeStr_sub  + "\"\n";
//				tmpStr += "UserFlagSetString Notch \"" + NotchStr  + "\"\n";
//				tmpStr += "UserFlagSetString Station \"" + barcode_stationStr  + "\"\n";
//				tmpStr += "ProductionSetUserValue " + "\"Product code            \" \"" + barcode_devicetypeStr_sub           + "\"\n";//20131216
//				tmpStr += "ProductionSetUserValue " + "\"Temperature            \" \"" + barcode_temperatureStr           + "\"\n";

				String L010_stationStr = "";
				L010_stationStr = barcode_stationStr;
				if(RTStr.equalsIgnoreCase("RT")){
					if(RTNumStr2.equalsIgnoreCase("RT1")){
						L010_stationStr = barcode_stationStr + "_" + RTStr;	//FT2_RT
					}else{
						L010_stationStr = barcode_stationStr + "_" + RTNumStr2;	//FT2_RT2 FT2_RT3 FT2_RT4...
					}		
				}	
				if(RTStr.equalsIgnoreCase("EQC")){
					if(RTNumStr2.equalsIgnoreCase("EQC1")){
						L010_stationStr = barcode_stationStr + "_" + RTStr;	//FT2_EQC
					}else{
						L010_stationStr = barcode_stationStr + "_" + EQC_bin_Str;	//FT2_EQC2 FT2_EQC3 FT2_EQC4...
					}		
				}		                		  
//				tmpStr += "ProductionSetUserValue " + "\"TestMode            \" \"" + L010_stationStr           + "\"\n";//20131216
			}               
		}


//		if (testTypeStr.equalsIgnoreCase("Wafer") && barcode_customerStr.equals("F137") && rtrbt[4].isSelected()){	//F137 corr dont want upload by CHiaHui 20140613
//
//			F137_corr_flag = 1;
//			tmpStr += "UserFlagSetInt F137_corr_flag " + "\"" + F137_corr_flag + "\"" +"\n";
//		}else{
//			F137_corr_flag = 0;
//			tmpStr += "UserFlagSetInt F137_corr_flag " + "\"" + F137_corr_flag + "\"" +"\n";
//		}	    

		//tmpStr += "ProductionSetFamilyID " + FamilyIDStr + "\n";

//		if (!BondingStr.equals("")) {
//			tmpStr += "ProductionSetFamilyID " + BondingStr + "\n"; //20080513
//			tmpStr += "ProductionSetUserValue " + "\"SG_Bonding               \" \"" + BondingStr + "\"\n";
//			ProductCheckSummary += "\"SG_Bonding               \" \"" + BondingStr + "\"\n";	//check summary issue add by ChiaHui 20130502
//		}

		// MUST be known the rule.
		//tmpStr += "RobotSetType '\"Epson Series\"'\n";//only work on command mode
		//tmpStr += "RobotSetType \"Epson Series\"\n";// work on script mode

		//tmpStr += "RobotSetType Simulator\n"; // offline continuous running
//		tmpStr += "RobotSetType \"" + RobotStr_set + "\"\n";//20081021
//		tmpStr += "ProductionSetUserValue " + "\"SG_Robot_Type            \" \"" + RobotStr_set           + "\"\n";//20081021
//		ProductCheckSummary += "\"SG_Robot_Type            \" \"" + RobotStr_set           + "\"\n";	//check summary issue add by ChiaHui 20130502

		autoEndLotDateTime  = "";
		autoSummaryDateTime = "";
		autoStopDateTime    = "";
		autoNewLotDateTime = getDateTime();
		if(!barcode_customerStr.equals("L010")){//jj
//			tmpStr += "ProductionSetUserValue " + "SG_1_autoNewLot__DateTime \"" + autoNewLotDateTime  + "\"\n";
//			tmpStr += "ProductionSetUserValue " + "SG_2_autoEndLot__DateTime \"" + nullStr  + "\"\n";
//			tmpStr += "ProductionSetUserValue " + "SG_3_autoSummary_DateTime \"" + autoStopDateTime + "\"\n";
//			tmpStr += "ProductionSetUserValue " + "SG_4_autoStop____DateTime \"" + nullStr    + "\"\n";
//			ProductCheckSummary += "SG_1_autoNewLot__DateTime \"" + autoNewLotDateTime  + "\"\n";	//check summary issue add by ChiaHui 20130502
//			ProductCheckSummary += "SG_2_autoEndLot__DateTime \"" + nullStr  + "\"\n";	//check summary issue add by ChiaHui 20130502
//			ProductCheckSummary += "SG_3_autoSummary_DateTime \"" + autoStopDateTime + "\"\n";	//check summary issue add by ChiaHui 20130502
//			ProductCheckSummary += "SG_4_autoStop____DateTime \"" + nullStr    + "\"\n";	//check summary issue add by ChiaHui 20130502
		}
		if(testTypeStr.equalsIgnoreCase("Final")){
//			tmpStr += "ProductionStartLot\n";
		}
	
	//            if(testTypeStr.equalsIgnoreCase("Final")){       20140429     

	//						    String bin_path = csicAutoPath + "bin/";
	//						    cmd  = "xterm -rightbar -bg green -fg black -geometry 158x20  -e ";
	//						    cmd += bin_path+"PowerUp.csh "+bin_path+"\n";  
	//						    javaExecSystemCmd2(cmd,5000); 
	//						}           

	//tmpStr += "run\n";		//power check remove 20150731 by ChiaHui
	//tmpStr += "run cont 10\n";

//	dmdFW.write(tmpStr);
//	dmdFW.close();
//	textArea2.append(tmpStr);
//	saveMessageRealtime(tmpStr);
//	tmpStr = "Save dmd_cmd script to File: \n" + outfStr + "\n";
//	System.out.println(tmpStr);

//	EQUsocketCheckdata(checkSocketData); Remark 20171229

	if (!NO_Corr_Reason.equals("")){
		SaveNoCorrReason("Msg:" + NO_Corr_Reason);
		NO_Corr_Reason = "";
	}

//	            ProductCheckSummary(ProductCheckSummary);
//	textArea2.append(tmpStr);
//	saveMessageRealtime(tmpStr);
		
	}

	public void genExecShellFile() { //--hh

		String tmpStr = "";
		try {

			//outfStr = autoResultPath + "execDmd";
			outfStr = LocalPath + "execDmd";
			outf = new File(outfStr);
			FileWriter execFW;

			execFW = new FileWriter(outf, false);

			tmpStr  = "#!/bin/csh\n";
			//tmpStr += "oic& \n";
			//tmpStr += "usleep 6000000\n";
			//tmpStr += "xterm -bg green -fg black -geometry 158x20 -e dmd_cmd -f " + JobPathStr + "cmd_script.dmd &\n";

			//tmpStr += "xterm -rightbar -bg green -fg black -geometry 158x20 -e dmd_cmd -f " + autoResultPath + "cmd_script.dmd & \n";
			tmpStr += "xterm -rightbar -bg green -fg black -geometry 158x20 -e " + LocalPath + "cmd_script.dmd & \n";

			execFW.write(tmpStr);
			execFW.close();

			tmpStr = "Save exec shell cmd to File: " + outfStr + "\n";
			System.out.println(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

		} catch (java.io.IOException err) {
			tmpStr = "<Exception> genExecShellFile: FileWriter: \n" + err + "\n";
			System.err.println(tmpStr);
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
		}
	}


	// if using csic D10 Summary template
	public void moveSummaryREPFile() {//20080915
		String tmpStr = "";
		String cmd = "";

		cmd  = csicAutoPath + "classes/moveCmd ";
		cmd += JobPathStr + " sum " + userSummaryfinalPath;
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);

		cmd  = csicAutoPath + "classes/moveCmd ";
		cmd += JobPathStr + " sump " + userSummaryfinalPath;
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);

		cmd  = csicAutoPath + "classes/moveCmd ";
		cmd += JobPathStr + " rep " + userSummaryfinalPath;
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);

		cmd  = csicAutoPath + "classes/moveCmd ";
		cmd += JobPathStr + " map " + userSummaryfinalPath;
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);

		cmd  = csicAutoPath + "classes/moveCmd ";
		cmd += JobPathStr + " dat " + userSummaryfinalPath;
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);
	}

	public void runautoSummary() {
		String cmd = "";
		String tmpStr = "";
		String summaryfileStr = "";

		//lotSummaryFlag = true; 20081110

		if(!bt2_4Flag){
			autoSummaryDateTime = getDateTime();
			autoSummaryDateTime2= getDateTime2();
			STDfileEndTime = getDateTime3();
			tmpStr = "autoSummary DateTime: " + autoSummaryDateTime + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			tmpStr = "Run runautoSummary ......\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		}

		cmd = "dmd_cmd --pause ";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		cmd = "dmd_cmd ProductionSetUserValue " + "SG_3_autoSummary_DateTime \"" + autoSummaryDateTime + "\"";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		if(bt2_2Flag) {//20100428
			sumTmpNum ++;
			if(RTStr.equals("A1")){    //20081107
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + barcode_stationStr + "-" + sumRepNumStr + "_" +"tmp" + sumTmpNum + ".sum";//20081107
				summaryfileName_tmp = SGStr + "_" + LotStr + "_" + barcode_stationStr + "-" + sumRepNumStr + "_" +"tmp" + sumTmpNum + ".sum";//20081107
			}
			else if(RTStr.equals("HW")){  
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + "HW" + "-" + sumRepNumStr + "_" +"tmp" + sumTmpNum + ".sum";//20081107
				summaryfileName_tmp = SGStr + "_" + LotStr + "_" + "HW" + "-" + sumRepNumStr + "_" +"tmp" + sumTmpNum + ".sum";//20081107
			}
			else if(RTStr.equals("EQC")){//20081110//20100618
				if(EQC_QNum_Str.startsWith("QA1")) {
					summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "-" + sumRepNumStr  + "_" +"tmp" + sumTmpNum + ".sum";//20100120
					summaryfileName_tmp = SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "-" + sumRepNumStr  + "_" +"tmp" + sumTmpNum + ".sum";//20100120
				}
				else{//QRT
					summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "_" + EQC_QNum_Str2 + QrtBinStr + "-" + sumRepNumStr  + "_" +"tmp" + sumTmpNum + ".sum";//20100120
					summaryfileName_tmp = SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "_" + EQC_QNum_Str2 + QrtBinStr + "-" + sumRepNumStr  + "_" +"tmp" + sumTmpNum + ".sum";//20100120
				}
			} 
			else if (RTStr.equals("CORR")) {//20081230
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + "CORR" + CORRStr_set + "-" + sumRepNumStr  + "_" +"tmp" + sumTmpNum + ".sum";//20081230
				summaryfileName_tmp = SGStr + "_" + LotStr + "_" + barcode_stationStr + "-CORR-" +CORRStr_set + "-" + sumRepNumStr  + "_" +"tmp" + sumTmpNum + ".sum";//Chia-Hui 20130401
			}
			else {//RT
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + RTNumStr2 + rtBinSumStr + "-" + sumRepNumStr + "_" +"tmp" + sumTmpNum + ".sum";//20100505
				summaryfileName_tmp = SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + RTNumStr2 + rtBinSumStr + "-" + sumRepNumStr + "_" +"tmp" + sumTmpNum + ".sum";//20100505


			}      
			bt2_2Flag =false;

		}
		else//20100428
		{
			if(RTStr.equals("A1")){    //20081107
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + "R0_ALL" + "-" + sumRepNumStr + ".sum";//20081107
				summaryfileName_tmp = SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + "R0_ALL" + "-" + sumRepNumStr + ".sum";//20081107
			} else if(RTStr.equals("HW")){
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + "HW" + "-" + sumRepNumStr + ".sum";//20081107
				summaryfileName_tmp = SGStr + "_" + LotStr + "_" + "HW" + "-" + sumRepNumStr + ".sum";//20081107
			} else if(RTStr.equals("EQC")){//20081110//20100618
				if(EQC_QNum_Str.startsWith("QA1")) {
					summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "-" + sumRepNumStr + ".sum";//20100120
					summaryfileName_tmp = SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "-" + sumRepNumStr + ".sum";//20100120
				}
				else{//QRT
					summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "_" + EQC_QNum_Str2 + QrtBinStr + "-" + sumRepNumStr + ".sum";//20100120
					summaryfileName_tmp = SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "_" + EQC_QNum_Str2 + QrtBinStr + "-" + sumRepNumStr + ".sum";//20100120
				}
			}
			else if (RTStr.equals("CORR")) {//20081230
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + barcode_stationStr + "-CORR-" + CORRStr_set + "-" + sumRepNumStr + ".sum";//20081230
				summaryfileName_tmp = SGStr + "_" + LotStr + "_" + barcode_stationStr + "-CORR-" +CORRStr_set + "-" + sumRepNumStr + ".sum";//Chia-Hui 20130401
			}
			else {//RT
				summaryfileStr = userSummaryfinalPath + SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + RTNumStr2 + rtBinSumStr + "-" + sumRepNumStr + ".sum";//20100505
				summaryfileName_tmp = SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + RTNumStr2 + rtBinSumStr + "-" + sumRepNumStr + ".sum";//20100505

			}
		}

		summaryfileStr = summaryfileStr;     //20090225
		summaryfileStr = summaryfileStr; // 20110831 

		cmd = "/bin/rm -f /tmp/Sites_info.txt";  //20090504   
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		cmd = "save_Sites.csh ";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd2(cmd, 1000);
		if(!bt2_4Flag){
			cmd = "/bin/rm -f /tmp/Sites_info.txt";  //20090504   
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);

			cmd = "save_Sites.csh ";
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd2(cmd, 1000);

			if(testTypeStr.equalsIgnoreCase("Final")){ //20101026 By Map for CP has not

				for(int n=0;n<=-1;n++){
					cmd = "dmd_cmd SummaryOpenFile " + summaryfileStr;
					tmpStr = cmd + "\n";
					System.out.println(tmpStr); 
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);
					javaExecSystemCmd(cmd);

					cmd = "save_Sites.csh ";
					tmpStr = cmd + "\n";
					System.out.println(tmpStr); 
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);
					javaExecSystemCmd2(cmd, 100);

					try{              
						Thread.sleep(1000);

					} catch (java.lang.InterruptedException Ierr) {
						System.out.println(" --> InterruptedException error");
						tmpStr = " --> InterruptedException error" + "\n";
						System.out.println(tmpStr); 
						textArea2.append(tmpStr);
						saveMessageRealtime(tmpStr);

					}
					myPause();

					//20090504  S
					try{

						SitesNum = 0;

						Thread.sleep(1000);// unit: ms
						br_STDinfo = new BufferedReader(new FileReader("/tmp/Sites_info.txt"));

						while ((tmpStr = br_STDinfo.readLine()) != null) {
							tmpStr = tmpStr.replace('\t',' ');
							tmpStr = stringRemoveSpaceHeadTail(tmpStr);
							if (tmpStr.startsWith("0 ")) {
								SitesNum = 1;
								System.out.println("SitesNum :" +SitesNum);
							} else if (tmpStr.startsWith("1 ")) {
								SitesNum = 2;
							} else if (tmpStr.startsWith("3 ")) {
								SitesNum = 4;
							} else if (tmpStr.startsWith("7 ")) {
								SitesNum = 8;
							} else if (tmpStr.startsWith("15 ")) {
								SitesNum = 16;
							} else if (tmpStr.startsWith("31 ")) {
								SitesNum = 32;
							}
						}

						Thread.sleep(4000);// unit: ms
						br_STDinfo.close(); // close file
						//                        Thread.sleep(1000);// unit: ms

					} catch (FileNotFoundException event) {
						tmpStr  = "<Exception> SitesFileInfo: \n" + "/tmp/Sites_info.txt" + " is NOT Found !\n";
						tmpStr += "+----------------------------+ \n";
						tmpStr += "| Please call the Supervisor | \n";
						tmpStr += "+----------------------------+ \n";
						System.out.println(tmpStr);

						tmpStr  = "<Exception> SitesFileInfo: \n" + "/tmp/Sites_info.txt" + " is NOT Found !\n";
						tmpStr += "+--------------------+ \n";
						tmpStr += "| Please call the Supervisor | \n";
						tmpStr += "+--------------------+ \n";
						textArea2.append(tmpStr);
						saveMessageRealtime(tmpStr);
					} catch (java.io.IOException err) {
						tmpStr = "<Exception> SitesFileInfo: " + err + "\n";
						tmpStr += "+----------------------------+ \n";
						tmpStr += "| Please call the Supervisor | \n";
						tmpStr += "+----------------------------+ \n";
						System.out.println(tmpStr);

						tmpStr = "<Exception> SitesFileInfo: " + err + "\n";
						tmpStr += "+--------------------+ \n";
						tmpStr += "| Please call the Supervisor | \n";

					} catch (java.lang.InterruptedException Ierr) {
						tmpStr = "SitesFileInfo:: " + Ierr;
						System.err.println(tmpStr);
					}

					cmd ="xterm -g 1000x1024 -e dmd_cmd GenSummary";
					if(SitesNum == 1) {
						cmd += " 0";//20100428
					} else if (SitesNum == 2) {
						cmd += " 0 1";//20100428
					} else if (SitesNum == 4) {
						cmd += " 0 1 2 3";//20100428
					} else if (SitesNum == 8) {
						cmd += " 0 1 2 3 4 5 6 7";//20100428
					} else if (SitesNum == 16) {
						cmd += " 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15";//20100428
					} else if (SitesNum == 32) {
						cmd += " 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35";//20100428
					}
					for(int i=0;i<10000000;i++);
					//20090504 E
					//cmd = "dmd_cmd GenSummary ";
					tmpStr = cmd + "\n";
					System.out.println(tmpStr); 
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);
					javaExecSystemCmd(cmd);

					cmd = "dmd_cmd SummaryCloseFile ";
					tmpStr = cmd + "\n";
					System.out.println(tmpStr); 
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);
					javaExecSystemCmd(cmd);
				}
			}
		}
		if(EfuseFlag&&RTStr.equals("A1")){   //20110831                     
			System.out.println(getSiteNum()); 
			System.out.println(getSiteNum()); 
			System.out.println(getSiteNum()); 
			System.out.println(getSiteNum()); 
			generateExtraCheck(summaryfileStr,getSiteNum());	//Cloud 0713
		}



	}

	public void runautoEndLot() {
		String cmd = "";
		String tmpStr = "";

		autoEndLotDateTime = getDateTime();
		autoEndLotDateTime2= getDateTime2();
		tmpStr = "autoEndLot DateTime: " + autoEndLotDateTime + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		cmd = "dmd_cmd ProductionSetUserValue " + "SG_2_autoEndLot__DateTime \"" + autoEndLotDateTime + "\"";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		cmd = "dmd_cmd ProductionEndLot ";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		moveSummaryREPFile();
	}

	public void queryGenLotSummary() {
		String cmd = "";
		String tmpStr = "";

		if (lotSummaryFlag==false) {
			runautoSummary();
		}
		else {
			cmd = "dmd_cmd --pause ";
			tmpStr = cmd + "\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			javaExecSystemCmd(cmd);
		}
	}

	public void runautoStopWoSum(){
		String cmd = "";
		String tmpStr = "";
		tmpStr = "\n___" + btCnt + "___ autoStop Process without generating summary...\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		cmd = "rm -f /tmp/tmp.sum";   //20090225
		tmpStr = cmd + "\n";          //20090225
		System.out.println(tmpStr);   //20090225
		textArea2.append(tmpStr);     //20090225
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);       //20090225

		cmd = "dmd_cmd --pause ";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		cmd = "dmd_cmd cont stop "; // as the OIC "Stop" button action
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		cmd  = "rm " + STDfileStr;
		tmpStr = cmd + "\n";
		System.out.println(tmpStr);   
		textArea2.append(tmpStr);     
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);  


	}

	public void runautoStop() {
		String cmd = "";
		String tmpStr = "";
		String tmpSumStr = "";
		int err_Cnt=0;         //20090225
		int confirm_flag = 0;  //20090225
		int rFlag = 0;
		int tmpFlag = 0;

		queryGenLotSummary();

		cmd = "dmd_cmd --pause ";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);

		autoStopDateTime = getDateTime();
		tmpStr = "autoStop DateTime: " + autoStopDateTime + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		//cmd = "dmd_cmd ProductionSetUserValue " + "SG_4_autoStop____DateTime \"" + autoStopDateTime + "\"";
		cmd = "dmd_cmd ProductionSetUserValue " + "SG_3_autoSummary_DateTime \"" + autoStopDateTime + "\"";
		//tmpStr += "ProductionSetUserValue " + "SG_3_autoSummary_DateTime \"" + autoStopDateTime + "\"\n";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd2(cmd,1000);

		cmd = "dmd_cmd UserFlagSetString summaryfileStr \"" + summaryfileStr + "\"";
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd2(cmd,1000);        

		//cmd = "dmd_cmd --stop ";  // no working while in "pause" mode
		cmd = "dmd_cmd cont stop "; // as the OIC "Stop" button action
		tmpStr = cmd + "\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		javaExecSystemCmd(cmd);
		/*
        try{  //check .std file size by ChiaHui 20130415 
        		String STDfiletmpStr_try = STDfiletmpStr + ".std";  
  					File std_File = new File(STDfiletmpStr_try);      				
						boolean xxx = true;
						long preSTD = 0;
						int timeCounter=0;

					while (xxx){

//						JOptionPane.showMessageDialog(null, ".std file size check ..."+STDfiletmpStr_try+" "+ preSTD +" "+std_File.length());//20130415						
						if (preSTD != std_File.length()){
								Thread.sleep(60000);
//								timeCounter+=5;
//								if (timeCounter>600)	//over 600sec then auto break
//									break;
						}else{
							xxx = false;						
						}

						preSTD = std_File.length();	
					}

        } catch (java.lang.InterruptedException Ierr) {
                System.out.println(" --> InterruptedException error");
                tmpStr = " --> InterruptedException error" + "\n";
                System.out.println(tmpStr); 
                textArea2.append(tmpStr);
                saveMessageRealtime(tmpStr);

        }
		 */

		//moveSummaryREPFile();
		//        20090225

		//err_Cnt=0;
		//if(testTypeStr.equalsIgnoreCase("Final")){      
		//    
		//    try{              
		//        Thread.sleep(1000);
		//    
		//        fw_tmp = new FileWriter(summaryfileStr_tmp,true);
		//        tmpStr = "open "+ summaryfileStr_tmp + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//             
		//    } catch (FileNotFoundException event) {
		//        System.out.println("catch " + summaryfileStr_tmp + " fail");
		//        System.out.println("catch " + summaryfileStr_tmp + " fail");
		//        System.out.println("catch " + summaryfileStr_tmp + " fail");
		//        tmpStr = "catch " + summaryfileStr_tmp + " fail" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    } catch (java.io.IOException err) {
		//    
		//        System.out.println(summaryfileStr_tmp +" --> IO error");
		//        System.out.println(summaryfileStr_tmp +" --> IO error");
		//        System.out.println(summaryfileStr_tmp +" --> IO error");
		//        tmpStr = summaryfileStr_tmp +" --> IO error" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    } catch (java.lang.InterruptedException Ierr) {
		//        System.out.println(summaryfileStr_tmp + " --> InterruptedException error");
		//        System.out.println(summaryfileStr_tmp + " --> InterruptedException error");
		//        System.out.println(summaryfileStr_tmp + " --> InterruptedException error");
		//        tmpStr = summaryfileStr_tmp + " --> InterruptedException error" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    }
		//    
		//    int reOpenTmpSumCnt;   //20090727
		//    reOpenTmpSumCnt = 3;   //20090727
		//    
		//    while(reOpenTmpSumCnt > 0){ //20090727
		//    
		//        try {
		//                        
		//            Thread.sleep(10000);
		//            br_tmp = new BufferedReader(new FileReader("/tmp/tmp.sum"));
		//            tmpStr = "open /tmp/tmp.sum" + "\n";
		//            System.out.println(tmpStr); 
		//            textArea2.append(tmpStr);
		//        
		//            while((tmpStr = br_tmp.readLine())!=null) {
		//                System.out.println(tmpStr);
		//                fw_tmp.write(tmpStr + "\n");
		//            }
		//        
		//            reOpenTmpSumCnt = 0;//20090727
		//            fw_tmp.close();
		//            br_tmp.close();
		//            
		//        } catch (FileNotFoundException event) {
		//            System.out.println("catch /tmp/tmp.sum fail");
		//            System.out.println("catch /tmp/tmp.sum fail");
		//            System.out.println("catch /tmp/tmp.sum fail");
		//            tmpStr = "catch /tmp/tmp.sum fail" + "\n";
		//            System.out.println(tmpStr); 
		//            textArea2.append(tmpStr);
		//            reOpenTmpSumCnt--;//20090727
		//        
		//        } catch (java.io.IOException err) {
		//            System.out.println("/tmp/tmp.sum --> IO error");
		//            System.out.println("/tmp/tmp.sum --> IO error");
		//            System.out.println("/tmp/tmp.sum --> IO error");
		//            tmpStr = "/tmp/tmp.sum --> IO error" + "\n";
		//            System.out.println(tmpStr); 
		//            textArea2.append(tmpStr);
		//            reOpenTmpSumCnt--;//20090727
		//        
		//        } catch (java.lang.InterruptedException Ierr) {
		//            System.out.println("/tmp/tmp.sum --> InterruptedException error");
		//            System.out.println("/tmp/tmp.sum --> InterruptedException error");
		//            System.out.println("/tmp/tmp.sum --> InterruptedException error");
		//            tmpStr = "/tmp/tmp.sum --> InterruptedException error" + "\n";
		//            System.out.println(tmpStr); 
		//            textArea2.append(tmpStr);
		//            reOpenTmpSumCnt--;//20090727
		//        
		//        }
		//    }
		//    
		//    
		//    cmd = "rm -f /tmp/tmp.sum";
		//    tmpStr = cmd + "\n";
		//    System.out.println(tmpStr); 
		//    textArea2.append(tmpStr);
		//    javaExecSystemCmd(cmd);
		//    
		//    
		//    //20090225
		//    
		//    //20090311
		//    tmpSumStr = userSummaryfinalPath + summaryfileName_tmp + ".bak";
		//    cmd = "/bin/mv "+ summaryfileStr_tmp + " "+ tmpSumStr;     
		//    tmpStr = cmd + "\n";
		//    System.out.println(tmpStr); 
		//    textArea2.append(tmpStr);
		//    javaExecSystemCmd(cmd);
		//    
		//    try{              
		//        Thread.sleep(500);
		//    
		//        fw_tmp = new FileWriter(summaryfileStr_tmp);
		//        tmpStr = "open "+ summaryfileStr_tmp + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    
		//    } catch (FileNotFoundException event) {
		//        System.out.println("catch " + summaryfileStr_tmp + " fail");
		//        System.out.println("catch " + summaryfileStr_tmp + " fail");
		//        System.out.println("catch " + summaryfileStr_tmp + " fail");
		//        tmpStr = "catch " + summaryfileStr_tmp + " fail" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    } catch (java.io.IOException err) {
		//    
		//        System.out.println(summaryfileStr_tmp +" --> IO error");
		//        System.out.println(summaryfileStr_tmp +" --> IO error");
		//        System.out.println(summaryfileStr_tmp +" --> IO error");
		//        tmpStr = summaryfileStr_tmp +" --> IO error" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    } catch (java.lang.InterruptedException Ierr) {
		//        System.out.println(summaryfileStr_tmp + " --> InterruptedException error");
		//        System.out.println(summaryfileStr_tmp + " --> InterruptedException error");
		//        System.out.println(summaryfileStr_tmp + " --> InterruptedException error");
		//        tmpStr = summaryfileStr_tmp + " --> InterruptedException error" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    }
		//    
		//    try {
		//    
		//        Thread.sleep(500);
		//        br_tmp = new BufferedReader(new FileReader(tmpSumStr));
		//        tmpStr = "open " + tmpSumStr + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//        while((tmpStr = br_tmp.readLine())!=null) {
		//            if(tmpStr.equals("(3) Bin Information")) {
		//                rFlag = 1;
		//            }
		//            //if(tmpStr.endsWith("SOFT BIN")) {
		//            //    rFlag = 0;
		//            //    tmpFlag = 1;
		//            //}
		//            if (tmpStr.startsWith("Hard Bin")) {
		//                rFlag = 0;
		//                tmpFlag = 1;
		//            }
		//            System.out.println(tmpStr);
		//            if(rFlag!=1) {
		//                if(tmpFlag == 1) {
		//                    fw_tmp.write("(3) Bin Information\n");
		//                    fw_tmp.write("--------------------\n");
		//                    tmpFlag = 0;
		//                    fw_tmp.write(tmpStr + "\n");
		//                }
		//                else {
		//                    fw_tmp.write(tmpStr + "\n");
		//                }
		//          
		//            }
		//        }
		//    
		//        fw_tmp.close();
		//        br_tmp.close();
		//    
		//    } catch (FileNotFoundException event) {
		//        System.out.println("catch " + tmpSumStr + " fail");
		//        System.out.println("catch " + tmpSumStr + " fail");
		//        System.out.println("catch " + tmpSumStr + " fail");
		//        tmpStr = "catch "+ tmpSumStr + " fail" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    } catch (java.io.IOException err) {
		//        System.out.println(tmpSumStr + " --> IO error");
		//        System.out.println(tmpSumStr + " --> IO error");
		//        System.out.println(tmpSumStr + " --> IO error");
		//        tmpStr = tmpSumStr + " --> IO error" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    } catch (java.lang.InterruptedException Ierr) {
		//        System.out.println(tmpSumStr +" --> InterruptedException error");
		//        System.out.println(tmpSumStr +" --> InterruptedException error");
		//        System.out.println(tmpSumStr +" --> InterruptedException error");
		//        tmpStr = tmpSumStr + " --> InterruptedException error" + "\n";
		//        System.out.println(tmpStr); 
		//        textArea2.append(tmpStr);
		//    
		//    }
		//    
		//    //////to copy the summary to another path  //Alex //20090609
		//    //String autoSendPath = "";
		//    //autoSendPath = "/usr/local/home/prod/autoResult/summary/autosend/"; 
		//    //autoSendPath += barcode_customerStr + "/";
		//    //File dirAS = new File(autoSendPath); 
		//    //if ( !dirAS.exists() )               
		//    //{                                    
		//    //      dirAS.mkdirs();                
		//    //}
		//    //
		//    //cmd = "/bin/cp "+ summaryfileStr_tmp + " "+ autoSendPath ;     
		//    //tmpStr = cmd + "\n";
		//    //System.out.println(tmpStr); 
		//    //textArea2.append(tmpStr);
		//    //javaExecSystemCmd(cmd);
		//    //////Alex
		//    
		//    cmd = "/bin/rm -f "+ tmpSumStr;     
		//    tmpStr = cmd + "\n";
		//    System.out.println(tmpStr); 
		//    textArea2.append(tmpStr);
		//    javaExecSystemCmd(cmd);
		//    
		//    
		//    //20090311
		//    //20090413   remove 20090504
		//    //STDfilefinalStr = STDfiletmpStr + STDfileEndTime + ".std\n";
		//    //cmd = "/bin/mv " + STDfileStr + " " + STDfilefinalStr;
		//    //tmpStr = cmd + "\n";
		//    //System.out.println(tmpStr); 
		//    //textArea2.append(tmpStr);
		//    //javaExecSystemCmd(cmd);
		//    
		//    //cmd = "/usr/bin/gzip " + STDfilefinalStr;
		//    //tmpStr = cmd + "\n";
		//    //System.out.println(tmpStr); 
		//    //textArea2.append(tmpStr);
		//    //javaExecSystemCmd(cmd);
		//}
		// rmCmdFile();  //--hh
	}

	public void killBarCodeXml() { //20101029

		String infileStr="";
		String cmd="";
		infileStr = testerBarcodePath + barcodeFile;

		cmd = "rm " + infileStr;
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);

		return;
	}


	public void cleanBarCodeXML() { //20101029

		//initial
		userSummaryfinalPath_bak = "";
		userSummaryfinalPath = "";
		userSTDFfinalPath = "";
		userTXTfinalPath = ""; //20170619
		userSTDFserverPath = "";//20170619
		userTXTserverPath = "";//20170619
		LB_PCflag = false; //20081104
		barcode_sitenameStr = "";
		barcode_testeridStr = "";
		barcode_recipepathStr = "";
		barcode_summarypathStr = "";
		barcode_customerStr = "";
		barcode_programStr = "";
		barcode_stationStr = "";
		barcode_proberidStr = "";
		barcode_handleridStr = ""; //Add by Cola. 20160503
		barcode_lotidStr = "";
		barcode_opidStr = "";
		barcode_temperatureStr = "";
		barcode_flowStr = "";
		barcode_devicetypeStr = "";
		barcode_devicetypeStr__ = "";
		barcode_sgidStr = "";
		barcode_LBC1Str = "";
		barcode_LBC2Str = "";
		barcode_lbidStr = "";
		barcode_stage = "";
		barcode_SoaktimeStr = "";
		barcode_datecode = "";//20130314 
		barcode_duts = "";//20130416
		barcode_RTbin_Str = ""; 
		barcode_SiteDiffYield_Str = "";
		barcode_AlarmYield_Str = "";
		barcode_AlarmBinYield_Str = "";
		barcode_BinDefine_Str = ""; 
		barcode_TestFlowNumber_Str = "";  //20160624
		barcode_ReferenceDoc_Str = "";  //20160624
		barcode_FormerLOTID_Str = "";  //20160726
		barcode_DESIGN_TYPE_Str = "";  //20160726
		barcode_customerTO_Str = "";  //20160726
		barcode_orderNO_Str = ""; //20161103
		barcode_TangoSBCset_Str = "";  //20161209
		TangoSBC_Rule_index = 0;  //20161209
		barcode_TangoSBCrule_Str = null;  //20161209
		barcode_OCR_ID_Str = "";
		barcode_SPIL_C_Item_Str = "";	//20170710

		//load file
		JobStr = "";
		JobSoStr = "";
		JobCmpStr = "";
		TPStr = "";
		TPSWVerStr = "";
		TPSWVer_prefix = "";
		BondingStr = "";
		FamilyIDStr = "";


		//recipe file
		JobPathStr = "";
		userSummaryPath = "";
		RobotStr  = "";                    
		RobotStr_set = "";           
		RobotStr2 = "";                    
		RobotStr3 = "";                    
		loadfilePath = "";
		loadfileStr = "";
		testTypeStr = "";
		STDFlogStr = "";
		STDFlogFlag = true;	//20170619
		JobStrRcp = "";                
		EfuseStr = "";
		EfuseFlag = false;            		
		EngStr =  "";
		EngFlag = false;            		
		TesterSiteMapStr = "";
		dateCodeStr = "";                
		SBC_Enable_RCP = false; //20170912
		SBC_Rule_RCP = ""; //20170912
		//other 20120828
		recipeFileforComp="";
		loadFileforComp=""; 
		barcode_CorrlotidStr="";
	}

	public void runConfirmation_MTK() {

		String tmpStr0 = "";
		String tmpStr  = "";
		int confirmflag = 0;

		LBStr   = tx0b.getText();

		// for MTK  
		if(MTK_series){
			if (!loadfileStr.equals(barcode_programStr)) {

				tmpStr  = "Barcode program name = " + barcode_programStr + "\n";
				tmpStr += "DMD Loadfile name = " + loadfileStr + "\n";
				tmpStr += "===> No matching ! \n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr); 

				tmpStr  = "Barcode program name = " + barcode_programStr + "\n";
				tmpStr += "DMD Loadfile name = " + loadfileStr + "\n";
				tmpStr += "===> No matching ! \n";
				tmpStr += "+---------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+---------------------+ \n";
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);

				tmpStr  = "Barcode program name = " + barcode_programStr + "\n";
				tmpStr += "DMD Loadfile name = " + loadfileStr + "\n";
				tmpStr += "===> No matching ! \n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:compare program name is fail");
				killproc(); System.exit(1); // to quit Java app for Linux

			}}
		// for MTK end
		//
		//// unuse: check if composite's job name == Job Test program name (from screen input)
		//
		// check if composite's loadfile name == barcode XML program name
		String combarcode_programStr="";//20080702
		String comJobStr = "";
		String comJobStr2 = "";
		String comJobStr3 = "";
		String comJobStr4 = ""; //only for FT
		String comJobStr5 = ""; //only for FT1
		int CompFlow = 0;
		int strLength=barcode_programStr.length();//20080702

		String barcode_programStr_tmp = "";                                   //20091028
		barcode_programStr_tmp = barcode_programStr.substring(0,strLength-5); //20091028

		//20110926
		int V_point_eng=0;       
		if(EngFlag){
			for(int i=0;i<strLength;i++)                     
			{                                                
				if(barcode_programStr.charAt(i)=='_')        
					V_point_eng=i;                             
			}
			if(V_point_eng != 0)
			{     
				strLength= V_point_eng;
			}
		}
		//for eng
		int V_point=0;                               //20091028
		for(int i=0;i<strLength;i++)                 //20091028
		{                                            //20091028
			if(barcode_programStr.charAt(i)=='_')    //20091028
				V_point=i;                           //20091028
		}                                            //20091028

		int UpLow_flag = 0; //0 represent lower-case, 1 represent upper-case

		if(barcode_programStr.endsWith("LOAD")){ //20090202
			UpLow_flag = 1;
		} else {
			UpLow_flag = 0;
		}


		if(UpLow_flag ==1) {  //20090202

			combarcode_programStr=barcode_programStr.substring(0,V_point)+".LOAD"; //20091028
			comJobStr  = barcode_stationStr + "_" + barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".LOAD";
			comJobStr2 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + "_" + barcode_stationStr + ".LOAD";

			if (barcode_stationStr.equals("FT") || barcode_stationStr.equals("FT1") || barcode_stationStr.equals("CP1") || barcode_stationStr.equals("PreFT") ){
				comJobStr3 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".LOAD";
				if(barcode_stationStr.endsWith("1")){
					comJobStr4  = barcode_stationStr.substring(0,2) + "_" + barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".LOAD";
					comJobStr5 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + "_" + barcode_stationStr.substring(0,2) + ".LOAD";                  
				}

			}
			if (barcode_stationStr.equals("EQC1") || barcode_stationStr.equals("EQC")) {
				if(!EfuseFlag){
					comJobStr3 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".LOAD";
				}
				if(barcode_stationStr.endsWith("1")){
					comJobStr4  = barcode_stationStr.substring(0,3) + "_" + barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".LOAD";
					comJobStr5 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + "_" + barcode_stationStr.substring(0,3) + ".LOAD";                  
				}

			}
		}
		else {

			combarcode_programStr=barcode_programStr.substring(0,V_point)+".load"; //20091028
			comJobStr  = barcode_stationStr + "_" + barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".load";
			comJobStr2 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + "_" + barcode_stationStr + ".load";

			if (barcode_stationStr.equals("FT") || barcode_stationStr.equals("FT1") || barcode_stationStr.equals("CP1") || barcode_stationStr.equals("PreFT") ){
				comJobStr3 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".load";
				if(barcode_stationStr.endsWith("1")){
					comJobStr4  = barcode_stationStr.substring(0,2) + "_" + barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".load";
					comJobStr5 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + "_" + barcode_stationStr.substring(0,2) + ".load";                  
				}

			}
			if (barcode_stationStr.equals("EQC1") || barcode_stationStr.equals("EQC")) {
				if(!EfuseFlag){
					comJobStr3 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".load";
				}
				if(barcode_stationStr.endsWith("1")){
					comJobStr4  = barcode_stationStr.substring(0,3) + "_" + barcode_devicetypeStr__ + "_" + TPSWVer_prefix + ".load";
					comJobStr5 = barcode_devicetypeStr__ + "_" + TPSWVer_prefix + "_" + barcode_stationStr.substring(0,3) + ".load";                  
				}

			}

		}

		tmpStr  = "Barcode program name = " + combarcode_programStr + "\n";
		tmpStr += "Composite's loadfile name = " + comJobStr + "\n";
		tmpStr += "Composite's loadfile name2 = " + comJobStr2 + "\n";
		tmpStr += "Composite's loadfile name3 = " + comJobStr3 + "\n";
		tmpStr += "Composite's loadfile name4 = " + comJobStr4 + "\n";
		tmpStr += "Composite's loadfile name5 = " + comJobStr5 + "\n";
		System.out.println(tmpStr);
		// if (!comJobStr.equals(combarcode_programStr)&&!comJobStr2.equals(combarcode_programStr)&&!comJobStr3.equals(combarcode_programStr)&&!comJobStr4.equals(combarcode_programStr)&&!comJobStr5.equals(combarcode_programStr)) {//20110926
		if(false){ //Rex
			tmpStr  = "Barcode program name = " + combarcode_programStr + "\n";
			tmpStr += "Composite's loadfile name = " + comJobStr + "\n";
			tmpStr += "Composite's loadfile name2 = " + comJobStr2 + "\n";
			tmpStr += "Composite's loadfile name3 = " + comJobStr3 + "\n";
			tmpStr += "Composite's loadfile name4 = " + comJobStr4 + "\n";
			tmpStr += "Composite's loadfile name5 = " + comJobStr5 + "\n";
			tmpStr += "===> All No matching ! \n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr); 


			tmpStr  = "Barcode program name = " + combarcode_programStr + "\n";
			tmpStr += "Composite's name = " + comJobStr + "\n";
			tmpStr += "Composite's loadfile name = " + comJobStr + "\n";
			tmpStr += "Composite's loadfile name2 = " + comJobStr2 + "\n";
			tmpStr += "Composite's loadfile name3 = " + comJobStr3 + "\n";
			tmpStr += "Composite's loadfile name4 = " + comJobStr4 + "\n";
			tmpStr += "Composite's loadfile name5 = " + comJobStr5 + "\n";
			tmpStr += "===> All No matching ! \n";
			tmpStr += "+---------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+---------------------+ \n";
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			tmpStr  = "Barcode program name = " + combarcode_programStr + "\n";
			tmpStr += "Composite's loadfile name = " + comJobStr + "\n";
			tmpStr += "Composite's loadfile name2 = " + comJobStr2 + "\n";
			tmpStr += "Composite's loadfile name3 = " + comJobStr3 + "\n";
			tmpStr += "Composite's loadfile name4 = " + comJobStr4 + "\n";
			tmpStr += "Composite's loadfile name5 = " + comJobStr5 + "\n";
			tmpStr += "===> All No matching ! \n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+--------------------+ \n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:compare composites's load file name is fail");
			killproc(); System.exit(1); // to quit Java app for Linux                
		}

		tmpStr  = "   LoadFile Name = " + barcode_programStr  + "\n";
		tmpStr += "       SG Number = " + SGStr  + "\n";
		tmpStr += "      Lot Number = " + LotStr + "\n";
		tmpStr += "    LB/PC Number = " + LBStr  + "\n";
		tmpStr += "Test Program Job = " + TPStr  + "\n";
		tmpStr += "\n==> Are they correct ?\n";
		System.out.println(tmpStr); 

		tmpStr  = "    LoadFile Name = " + barcode_programStr  + "\n";
		tmpStr += "          SG Number = " + SGStr  + "\n";
		tmpStr += "         Lot Number = " + LotStr + "\n";
		tmpStr += "     LB/PC Number = " + LBStr  + "\n";
		tmpStr += "Test Program Job = " + TPStr  + "\n";
		tmpStr += "\n==> Are they correct ?\n";
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		tmpStr  = "    LoadFile Name = " + barcode_programStr  + "\n";
		tmpStr += "          SG Number = " + SGStr  + "\n";
		tmpStr += "         Lot Number = " + LotStr + "\n";
		tmpStr += "    LB/PC Number = " + LBStr  + "\n";
		tmpStr += "Test Program Job = " + TPStr  + "\n";
		tmpStr += "\n===> Are they correct ?\n";

		confirmflag = 0;
		// yes to return 0, no to return 1
		confirmflag = JOptionPane.showConfirmDialog(null, tmpStr, "Confirm ?", JOptionPane.YES_NO_OPTION);
		if (confirmflag==0) {

			autoNewLotDateTime = getDateTime();
			tmpStr = "autoNewLot DateTime: " + autoNewLotDateTime + "\n";
			//System.out.println("autoNewLot DateTime: " + getDateTime()); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			lotSummaryFlag = false; //init

			//if(testTypeStr.equalsIgnoreCase("Final")){
			//    if(gpibCheck()){//20110614
			//        //System.out.println("Finl---------------------"); 
			//        //System.out.println("Fianl---------------------"); 
			//        
			//        runLoadJobtoOICu();
			//        
			//    }
			//}
			//else{
			//    if(gpibCPCheck()){//20110614
			//System.out.println("Fianl---------------------NONONO"); 
			//System.out.println("Fianl---------------------NONONO"); 
			//System.out.println("Fianl---------------------NONONO"); 
				runLoadJobtoOICu();
			//    }
			//}
		} else {

			//20081110
			sumRepFlag = false; //repeat-->true,no repeat -->false,deault -->false 
			sumRepNum = 1;
			if(sumRepNum<10){
				tx4_1.setText("0" + Integer.toString(sumRepNum));
				sumRepNumStr = "0" + Integer.toString(sumRepNum);
			}
			else {
				tx4_1.setText(Integer.toString(sumRepNum));
				sumRepNumStr = Integer.toString(sumRepNum);
			}
			tx4_1.setBackground(new Color(251,249,209));

			textArea1.setText("");
			textArea1_2.setText("");

			tmpStr  = "The Data is not correct, the system will exit.\n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:Confirmation is fail");
			killproc(); System.exit(1); // to quit Java app for Linux

		}

		JobCheckStr = JobStr;

		autonewlotCnt++; //for genExecShellFile

	}



	public void runConfirmation() {

		String tmpStr0 = "";
		String tmpStr  = "";
		int confirmflag = 0;

		LBStr   = tx0b.getText();

		//
		//// unuse: check if composite's job name == Job Test program name (from screen input)
		//
		// check if composite's loadfile name == barcode XML program name


		String combarcode_programStr="";//20080702
		String comJobStr = "";

		int CompFlow = 0;
		int strLength=barcode_programStr.length();//20080702

		combarcode_programStr=barcode_programStr;//20080808//20080904
		if (jobFlag) {//20081021
			comJobStr = JobStr;
		}
		else {
			int JobStrLength = JobStr.length();
			comJobStr = JobStr.substring(0,JobStrLength-4);
		}

		if(MTK_series){		

			comJobStr = comJobStr + ".load";
		}        

		System.out.println("combarcode_programStr " + combarcode_programStr);
		System.out.println("comJobStr = " + comJobStr);

		if (!comJobStr.equalsIgnoreCase(combarcode_programStr) && !comJobStr.equalsIgnoreCase(combarcode_programStr.substring(0, combarcode_programStr.length()-9) + combarcode_programStr.substring(combarcode_programStr.length()-5, combarcode_programStr.length()))) {//20080702
			tmpStr  = "Barcode program name = " + combarcode_programStr + "\n";

			tmpStr += "Composite's name = " + comJobStr + "\n";
			tmpStr += "===> No matching ! \n";
			tmpStr += "+----------------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+----------------------------+ \n";
			System.out.println(tmpStr); 


			tmpStr  = "Barcode program name = " + combarcode_programStr + "\n";
			tmpStr += "Composite's name = " + comJobStr + "\n";
			tmpStr += "Composite's loadfile name = " + comJobStr + "\n";
			tmpStr += "===> No matching ! \n";
			tmpStr += "+---------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+---------------------+ \n";
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			tmpStr  = "Barcode program name = " + combarcode_programStr + "\n";
			tmpStr += "Composite's name = " + comJobStr + "\n";
			tmpStr += "===> No matching ! \n";
			tmpStr += "+--------------------+ \n";
			tmpStr += "| Please call the Supervisor | \n";
			tmpStr += "+--------------------+ \n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:compare composite's name in fail");
			killproc(); System.exit(1); // to quit Java app for Linux                
		}


		if(MTK_series){
			tmpStr  = "    LoadFile Name = " + barcode_programStr  + "\n";
		}
		tmpStr += "          SG Number = " + SGStr  + "\n";
		tmpStr += "         Lot Number = " + LotStr + "\n";
		tmpStr += "     LB/PC Number = " + LBStr  + "\n";
		tmpStr += "Test Program Job = " + TPStr  + "\n";
		tmpStr += "\n==> Are they correct ?\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);


		confirmflag = 0;
		// yes to return 0, no to return 1
		confirmflag = JOptionPane.showConfirmDialog(null, tmpStr, "Confirm ?", JOptionPane.YES_NO_OPTION);
		if (confirmflag==0) {

			//		String cmd = "";
			//	    JOptionPane.showMessageDialog(null, "11111111111 ");
			//            cmd = "xterm -rightbar -bg green -fg black -geometry 158x20  -e " + "cd /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/NoStart_OICU/start_oicu";
			//            cmd = "xterm -rightbar -bg green -fg black -geometry 158x20  -e " + "xterm_nostart.csh";
			//	    javaExecSystemCmd(cmd);


			autoNewLotDateTime = getDateTime();
			tmpStr = "autoNewLot DateTime: " + autoNewLotDateTime + "\n";
			//System.out.println("autoNewLot DateTime: " + getDateTime()); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			lotSummaryFlag = false; //init

			//if(testTypeStr.equalsIgnoreCase("Final")){
			//    if(gpibCheck()){//20110614
			//System.out.println("Finl---------------------"); 
			//System.out.println("Fianl---------------------"); 

			//        runLoadJobtoOICu();

			//    }
			//}
			//else{
			//    if(gpibCPCheck()){//20110614
			//System.out.println("Fianl---------------------NONONO"); 
			//System.out.println("Fianl---------------------NONONO"); 
			//System.out.println("Fianl---------------------NONONO"); 

				runLoadJobtoOICu();
			//    }
			//}
		} else {

			//20081110
			sumRepFlag = false; //repeat-->true,no repeat -->false,deault -->false 
			sumRepNum = 1;
			if(sumRepNum<10){
				tx4_1.setText("0" + Integer.toString(sumRepNum));
				sumRepNumStr = "0" + Integer.toString(sumRepNum);
			}
			else {
				tx4_1.setText(Integer.toString(sumRepNum));
				sumRepNumStr = Integer.toString(sumRepNum);
			}
			tx4_1.setBackground(new Color(251,249,209));

			textArea1.setText("");
			textArea1_2.setText("");

			tmpStr  = "The Data is not correct, the system will exit.\n";
			JOptionPane.showMessageDialog(null, tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:Confirmation is fail");
			killproc(); System.exit(1); // to quit Java app for Linux

		}

		JobCheckStr = JobStr;

		autonewlotCnt++; //for genExecShellFile

	}

	public void runExit() {   //-- hh


		String cmd = ""; 
		String tmpStr = "";

		tmpStr = "Are you sure you want to Exit ?\n";
		System.out.println(tmpStr); 
		textArea2.append(tmpStr); 
		saveMessageRealtime(tmpStr);

		int exitflag = 0; int savaSTDF_flag = 0;
		// yes to return 0, no to return 1
		exitflag = JOptionPane.showConfirmDialog(null, tmpStr, "Confirm Exit ?", JOptionPane.YES_NO_OPTION);
		if (exitflag==0) {        
			//For STDF upload. 20170524-----Start
//			savaSTDF_flag = JOptionPane.showConfirmDialog(null, "Upload STDF or not ?", "Confirm Upload STDF ?", JOptionPane.YES_NO_OPTION);
//			if(savaSTDF_flag == 0){
//			if(TryRunOI)
			if (testTypeStr.equalsIgnoreCase("Wafer"))
				javaExecSystemCmd2("cex -t " + hostnameStr + " -c set_end_of_wafer\n", 5000);
//			if (testTypeStr.equalsIgnoreCase("Final"))
//				javaExecSystemCmd2("cex -t " + hostnameStr + " -c set_end_of_lot" + "\n", 5000);
				
//			} //For STDF upload. 20170524-----End
			
			if (testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("L227"))
				javaExecSystemCmd("rm -rf /home/swcpd/STPWIN");	//20170710
				
			javaExecSystemCmd2("rm -f /tmp/barcode_file/userFlag.txt",200);
			exitOicu(); 		    
			tmpStr = "\nEXIT DateTime = " + getDateTime() + "\n\n";
			System.out.print(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			tmpStr = textArea2.getText();
			saveMessage(tmpStr);
			System.exit(1); // to quit Java app for Linux 
		}


		// --hh D10
		/*        String tmpStr = "";

        tmpStr = "Are you sure you want to Exit ?\n";
        System.out.println(tmpStr); 
        textArea2.append(tmpStr);
        saveMessageRealtime(tmpStr);


        int exitflag = 0;
        // yes to return 0, no to return 1
        exitflag = JOptionPane.showConfirmDialog(null, tmpStr, "Confirm Exit ?", JOptionPane.YES_NO_OPTION);
        if (exitflag==0) {

            rmCmdFile();

            if (oicFlag) {
                //exitOIC(true);
                // exitOIC(false);   // --hh
		exitOicu();          // --hh

            }

            tmpStr = "\nEXIT DateTime = " + getDateTime() + "\n\n";
            System.out.print(tmpStr); 
            textArea2.append(tmpStr);
            saveMessageRealtime(tmpStr);

            tmpStr = textArea2.getText();
            saveMessage(tmpStr);
            killproc(); System.exit(1); // to quit Java app for Linux 
        }
		 */	
	}


	public static void autoSaveMessageBeforeExit() {

		String tmpStr = "";

		tmpStr = "\nEXIT DateTime = " + getDateTime() + "\n\n";
		System.out.print(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		tmpStr = textArea2.getText();
		saveMessageOK(tmpStr);
	}

	public static void barcode_KIT_process(){	//kit barcode 20130416


		barcode_KIT = kit_No_panel.getText();
		/*
				int exitflag =1;

				while(exitflag ==1){

						String tmpBarcodeKIT="";	

						barcode_KIT = JOptionPane.showInputDialog("Please input the KIT Barcode");
						tmpBarcodeKIT += "The KIT Barcode is: " + barcode_KIT + "\n";          
		            // yes to return 0, no to return 1
		        exitflag = JOptionPane.showConfirmDialog(null, tmpBarcodeKIT, "Confirmation ? ", JOptionPane.YES_NO_OPTION);
				}
		 */						
	}

	public static void barcode_LB_process(){	//kit barcode 20130416

		barcode_LB = tx0b.getText();
		/*
				int exitflag =1;

				while(exitflag ==1){

						String tmpBarcodeLB="";	

						barcode_LB = JOptionPane.showInputDialog("Please input the LB Barcode");
						tmpBarcodeLB += "The LB Barcode is: " + barcode_LB + "\n";          
		            // yes to return 0, no to return 1
		        exitflag = JOptionPane.showConfirmDialog(null, tmpBarcodeLB, "Confirmation ? ", JOptionPane.YES_NO_OPTION);
				}	 
		 */						
	}

	public static void CountSocketQuantity() {	//count socket contact 20130411

		barcode_duts_int = new Integer(barcode_duts).intValue();
		SocketCount = new String[barcode_duts_int];

		for (int site=1 ; site<=barcode_duts_int ; site++){

			switch(site) { 
			case 1:
				SocketCount[0] = socket_No_panel.getText();
				break;
			case 2:
				SocketCount[1] = socket_No_pane2.getText();
				break; 
			case 3:
				SocketCount[2] = socket_No_pane3.getText();
				break;           		 	
			case 4:
				SocketCount[3] = socket_No_pane4.getText();
				break;            		 	
			case 5:
				SocketCount[4] = socket_No_pane5.getText();
				break;            		 	
			case 6:
				SocketCount[5] = socket_No_pane6.getText();
				break;            		 	
			case 7:
				SocketCount[6] = socket_No_pane7.getText();
				break;            		 	
			case 8:
				SocketCount[7] = socket_No_pane8.getText();
				break;            		 	
			}
		}		

		/*
        barcode_duts_int = new Integer(barcode_duts).intValue();
        int exitflag = 1;
        int siteNO = 0;
        int x=1;
        SocketCount = new String[barcode_duts_int];				

				while(exitflag ==1){

						String tmpSocketCount="";	

						for (x=1;x<=barcode_duts_int;x++){
							siteNO = x-1;
							SocketCount[siteNO] = JOptionPane.showInputDialog("Please input the Socket Barcode , site" + x + ": ");
							tmpSocketCount += "site" + x + ":"+ SocketCount[siteNO] + "\n";
						}            
		            // yes to return 0, no to return 1
		        exitflag = JOptionPane.showConfirmDialog(null, tmpSocketCount, "Confirmation ? ", JOptionPane.YES_NO_OPTION);				

				}
		 */				
	}

	public void actionPerformed(ActionEvent e) {

		String tmpStr = "";
		btCnt++;
		String tmpfileStr = "";//20081110
		String cmd;//20090225

		if (e.getSource()==bt1) {
			
			//	 	javaExecSystemCmd("xterm -rightbar -bg green -fg black -geometry 158x20  -e  pwd.csh" ); //20140522
			//		System.out.println("====bt1===========");	
			
			//Add change_NewLotUnison.csh command by Cola. 20170117 - Test
//			xterm -rightbar -bg green -fg black -geometry 100x150 -e
//			javaExecSystemCmd("xterm change_NewLotUnison_test.csh");
//			JOptionPane.showMessageDialog(null, "send cmd:change_NewLotUnison.csh");
//			javaExecSystemCmd("xterm");
//			javaExecSystemCmd2("xterm", 50);
//			JOptionPane.showMessageDialog(null, "send cmd:xterm");
			
			saveStartEndRealtime("OI_START");
			cleanBarCodeXML();
			bt1.setEnabled(false);
			tmpStr = "\n___" + btCnt + "___ OI START Process ...\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			sumRepNum = 1;
			if(sumRepNum<10){
				tx4_1.setText("0" + Integer.toString(sumRepNum));
				sumRepNumStr = "0" + Integer.toString(sumRepNum);
			}
			else {
				tx4_1.setText(Integer.toString(sumRepNum));
				sumRepNumStr = Integer.toString(sumRepNum);
			}
			sumRepFlag = false;//20081110
			tx4_1.setBackground(new Color(251,249,209));//20081110
			autonewlotCnt = 0;
			EfuseStr = "";
			stageFlag = false; //20110724
			if (getBarCodeXMLInfo()) { 
				MTKFlag = 1;//20120828
				if(MTK_series){
					if (getRecipeFileInfo()) {
						if (checkDmdSWVersion()){  
							if (getLoadFileInfo()) {  //--hh
								if (true) {               //--hh
									if (checkDmdSWVersion()) {
										// System.out.println(" @@@ I am here !! @@@"); // --hh
										// launch OIC tool
										//	runOIC();  // --hh

										if(robot_type_num>1){     
											JOptionPane jp1 = new JOptionPane();                                 //20081021
											tmpStr = "<Warning>:There are multiple \"Robot Type\" in the file."; //20081021
											tmpStr +="\nPlease Select one of them...\n\n";                            //20081021
											System.out.print(tmpStr); 
											textArea2.append(tmpStr);
											saveMessageRealtime(tmpStr);
											jp1.showMessageDialog(null, tmpStr);                                 //20081021
											CallRobotFrame();                                                    //20081021
											btCnt ++;
											tmpStr = "\n___" + btCnt + "___ Selected Robot is -"+robot_btname[0]+"- ...\n";
											RobotStr_set = robot_btname[0];
											tmpStr += "___" + btCnt + "___ NOW barcode_stationStr = " + barcode_stationStr + "\n"; 
											System.out.print(tmpStr); 
											textArea2.append(tmpStr);
											saveMessageRealtime(tmpStr);
										}
									}
								}
							}
						}
					}
				}
				else {

					MTKFlag = 0;//20120828
					if (getRecipeFileInfo()) {
						if (checkDmdSWVersion()) {

							// launch OIC tool
							// runOIC();  // --hh , do not use

							if(robot_type_num>1){     
								JOptionPane jp1 = new JOptionPane();                                 //20081021
								tmpStr = "<Warning>:There are multiple \"Robot Type\" in the file."; //20081021
								tmpStr +="\nPlease Select one of them...\n\n";                            //20081021
								System.out.print(tmpStr); 
								textArea2.append(tmpStr);
								saveMessageRealtime(tmpStr);
								jp1.showMessageDialog(null, tmpStr);                                 //20081021
								CallRobotFrame();                                                    //20081021
								btCnt ++;
								tmpStr = "\n___" + btCnt + "___ Selected Robot is -"+robot_btname[0]+"- ...\n";
								RobotStr_set = robot_btname[0];
								tmpStr += "___" + btCnt + "___ NOW barcode_stationStr = " + barcode_stationStr + "\n"; 
								System.out.print(tmpStr); 
								textArea2.append(tmpStr);
								saveMessageRealtime(tmpStr);
							}
						}
					}
				}
			}
			else {
				tmpStr  = "getBarCodeXMLInfo: status=false >>> UNKNOWN ERROR <<< \n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "getBarCodeXMLInfo: status=false >>> UNKNOWN ERROR <<< \n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:get Barcode xml info is fail");
				killproc(); System.exit(1); // to quit Java app for Linux
			}

			if(testTypeStr.equalsIgnoreCase("Final")) 
				ProcessEQUsocketFile(true);

			if(testTypeStr.equalsIgnoreCase("Wafer") && EQU_CP_Barcode_AutoKeyin_flag) //Add EQU_CP_Barcode_AutoKeyin_flag by Cola. 20160218
				ProcessEQUsocketFile_CP();

			int exitflag = 0;  //Change to 0 by Cola. 20160215
			if(testTypeStr.equalsIgnoreCase("Final")){
				if(EQU_FT_Barcode_AutoKeyin_flag){ //Add by Cola. 20160215
					// yes to return 0, no to return 1
					exitflag = JOptionPane.showConfirmDialog(null,"change kit,LB,socket barcode?", "Confirmation ? ", JOptionPane.YES_NO_OPTION);             
				}else{
					JOptionPane.showMessageDialog(null,"Please key in kit, LB, socket barcode !!");         	   
					bt2_2.setEnabled(false); 
				}
			}  

			if(testTypeStr.equalsIgnoreCase("Wafer")){
				if(EQU_CP_Barcode_AutoKeyin_flag){ //Add by Cola. 20151209
					//yes to return 0, no to return 1
					exitflag = JOptionPane.showConfirmDialog(null,"change probe card barcode?", "Confirmation ? ", JOptionPane.YES_NO_OPTION);  
				}else{
					JOptionPane.showMessageDialog(null,"Please key in probe card barcode !!");  
					bt2_2.setEnabled(false);
				}
				kit_No_panel.setEditable(false);		        		
				socket_No_panel.setEditable(false);						
				socket_No_pane2.setEditable(false);
				socket_No_pane3.setEditable(false);
				socket_No_pane4.setEditable(false);
				socket_No_pane5.setEditable(false);
				socket_No_pane6.setEditable(false);
				socket_No_pane7.setEditable(false);
				socket_No_pane8.setEditable(false);		        		
			}

			if(exitflag == 1){         

				boolean EQU_check_probeCard = false;
				boolean EQU_FT_flag = false;                  

				if(EQU_FT_check_ProbeCard_flag == true && testTypeStr.equalsIgnoreCase("Final")){

					EQU_FT_flag = true;
					boolean EQU_FT_KIT_check_flag = true;        
					boolean EQU_FT_LB_check_flag = true;         
					boolean EQU_FT_socket1_check_flag = true;    
					boolean EQU_FT_socket2_check_flag = true;    
					boolean EQU_FT_socket3_check_flag = true;    
					boolean EQU_FT_socket4_check_flag = true;    
					boolean EQU_FT_socket5_check_flag = true;    
					boolean EQU_FT_socket6_check_flag = true;    
					boolean EQU_FT_socket7_check_flag = true;    
					boolean EQU_FT_socket8_check_flag = true;    

					File EQU_FT_KIT_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + kit_No_panel.getText() + ".txt");
					File EQU_FT_LB_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + tx0b.getText() + ".txt");
					File EQU_FT_socket1_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_panel.getText() + ".txt");
					File EQU_FT_socket2_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane2.getText() + ".txt");				
					File EQU_FT_socket3_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane3.getText() + ".txt");
					File EQU_FT_socket4_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check//" + barcode_testeridStr + "/" + socket_No_pane4.getText() + ".txt");
					File EQU_FT_socket5_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane5.getText() + ".txt");
					File EQU_FT_socket6_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane6.getText() + ".txt");
					File EQU_FT_socket7_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane7.getText() + ".txt");
					File EQU_FT_socket8_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane8.getText() + ".txt");

					String EQUcmd;
					String EQU_check_error_message;
					String EQU_error_list = "";

					//20140304 check the Kit,LB,sockets		

//					EQUcmd = "rm -f /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr;
//					javaExecSystemCmd2(EQUcmd,1000);
//					System.out.println(EQUcmd);
//					saveMessageRealtime(EQUcmd);

					EQUcmd = "ftp_sigurd_check_EQU_probeCard.csh " + barcode_testeridStr+ " /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/";
					javaExecSystemCmd2(EQUcmd,7000);
					System.out.println(EQUcmd);
					saveMessageRealtime(EQUcmd);	

					File EQU_check_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr);
					if (EQU_check_dir.isDirectory()){

						if(!EQU_FT_KIT_dir.isFile() || !kit_No_panel.getText().substring(0, 2).equals("CK")){
							EQU_FT_KIT_check_flag = false;
						}

						if (!EQU_FT_LB_dir.isFile() || (!tx0b.getText().substring(0, 2).equals("LB") && !tx0b.getText().substring(0, 2).equals("MB"))){
							EQU_FT_LB_check_flag = false;
						}

						if (!socket_No_panel.getText().equalsIgnoreCase("") && (!EQU_FT_socket1_dir.isFile() || !socket_No_panel.getText().substring(0, 2).equals("SK"))){
							EQU_FT_socket1_check_flag = false;
						}

						if (!socket_No_pane2.getText().equalsIgnoreCase("") && (!EQU_FT_socket2_dir.isFile() || !socket_No_pane2.getText().substring(0, 2).equals("SK"))){
							EQU_FT_socket2_check_flag = false;
						}

						if (!socket_No_pane3.getText().equalsIgnoreCase("") && (!EQU_FT_socket3_dir.isFile() || !socket_No_pane3.getText().substring(0, 2).equals("SK"))){
							EQU_FT_socket3_check_flag = false;
						}

						if (!socket_No_pane4.getText().equalsIgnoreCase("") && (!EQU_FT_socket4_dir.isFile() || !socket_No_pane4.getText().substring(0, 2).equals("SK"))){
							EQU_FT_socket4_check_flag = false;
						}

						if (!socket_No_pane5.getText().equalsIgnoreCase("") && (!EQU_FT_socket5_dir.isFile() || !socket_No_pane5.getText().substring(0, 2).equals("SK"))){
							EQU_FT_socket5_check_flag = false;
						}

						if (!socket_No_pane6.getText().equalsIgnoreCase("") && (!EQU_FT_socket6_dir.isFile() || !socket_No_pane6.getText().substring(0, 2).equals("SK"))){
							EQU_FT_socket6_check_flag = false;
						}

						if (!socket_No_pane7.getText().equalsIgnoreCase("") && (!EQU_FT_socket7_dir.isFile() || !socket_No_pane7.getText().substring(0, 2).equals("SK"))){
							EQU_FT_socket7_check_flag = false;
						}

						if (!socket_No_pane8.getText().equalsIgnoreCase("") && (!EQU_FT_socket8_dir.isFile() || !socket_No_pane8.getText().substring(0, 2).equals("SK"))){
							EQU_FT_socket8_check_flag = false;
						}

						if (EQU_FT_KIT_check_flag == false || EQU_FT_LB_check_flag == false || EQU_FT_socket1_check_flag == false || EQU_FT_socket2_check_flag == false || EQU_FT_socket3_check_flag == false || EQU_FT_socket4_check_flag == false || EQU_FT_socket5_check_flag == false || EQU_FT_socket6_check_flag == false || EQU_FT_socket7_check_flag == false || EQU_FT_socket8_check_flag == false){

							if(EQU_FT_KIT_check_flag == false)		
								EQU_error_list += kit_No_panel.getText() + ",";															

							if(EQU_FT_LB_check_flag == false)
								EQU_error_list += tx0b.getText() + ",";

							if(EQU_FT_socket1_check_flag == false)
								EQU_error_list += socket_No_panel.getText() + ",";																	

							if(EQU_FT_socket2_check_flag == false)
								EQU_error_list += socket_No_pane2.getText() + ",";																	

							if(EQU_FT_socket3_check_flag == false)
								EQU_error_list += socket_No_pane3.getText() + ",";

							if(EQU_FT_socket4_check_flag == false)
								EQU_error_list += socket_No_pane4.getText() + ",";

							if(EQU_FT_socket5_check_flag == false)
								EQU_error_list += socket_No_pane5.getText() + ",";

							if(EQU_FT_socket6_check_flag == false)
								EQU_error_list += socket_No_pane6.getText() + ",";

							if(EQU_FT_socket7_check_flag == false)
								EQU_error_list += socket_No_pane7.getText() + ",";

							if(EQU_FT_socket8_check_flag == false)
								EQU_error_list += socket_No_pane8.getText() + ",";																	

							EQU_FT_flag = false;
						}
						if(socket_No_panel.getText().equalsIgnoreCase("") && socket_No_pane2.getText().equalsIgnoreCase("") && socket_No_pane3.getText().equalsIgnoreCase("") && socket_No_pane4.getText().equalsIgnoreCase("") && socket_No_pane5.getText().equalsIgnoreCase("") && socket_No_pane6.getText().equalsIgnoreCase("") && socket_No_pane7.getText().equalsIgnoreCase("") && socket_No_pane8.getText().equalsIgnoreCase(""))
							EQU_FT_flag = false;
					}
					else{
						System.out.println("Error directory");
					}
					if (EQU_FT_flag == false){
						EQU_check_error_message  = "EQU probe card do not match the Kit,LB or sockets server data!\n";
						EQU_check_error_message += "+------------------------------------------------------------------+ \n";
						EQU_check_error_message += "+Kit,LB or sockets << " + EQU_error_list + " >> do not fixture loan from system at " + barcode_testeridStr + "\n";   
						EQU_check_error_message += "| Please call the EQU check Kit,LB or sockets number | \n";
						EQU_check_error_message += "+------------------------------------------------------------------+ \n";
						JOptionPane.showMessageDialog(null, EQU_check_error_message);
						autoSaveMessageBeforeExit();
						saveErrorMessageRealtime("Msg:EQU check probe card do not match");
						//					                killproc(); System.exit(1); // to quit Java app for Linux																							
					}							
				}

				if(EQU_CP_check_ProbeCard_flag == true && testTypeStr.equalsIgnoreCase("Wafer")){

					String EQUcmd;
					String[] probeCard_array;
					String EQU_check_error_message;


					//20131219 check the LB		

//					EQUcmd = "rm -f /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr;
//					javaExecSystemCmd2(EQUcmd,1000);
//					System.out.println(EQUcmd);
//					saveMessageRealtime(EQUcmd);

					EQUcmd = "ftp_sigurd_check_EQU_probeCard.csh " + barcode_testeridStr+ " /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/";
					javaExecSystemCmd2(EQUcmd,7000);
					System.out.println(EQUcmd);
					saveMessageRealtime(EQUcmd);	

					File EQU_check_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr);
					if (EQU_check_dir.isDirectory()){
						probeCard_array = EQU_check_dir.list();
						for (int i = 0; i < probeCard_array.length; i++){
							probeCard_string = probeCard_array[i];
							probeCard_array_string = probeCard_string.split(".txt");
							if(probeCard_array_string[0].equals(tx0b.getText()))
								EQU_check_probeCard = true;
						}
					}
					else{
						System.out.println("Error directory");
					}
					if (EQU_check_probeCard == false){
						EQU_check_error_message  = "EQU probe card do not match the LB server data!\n";
						EQU_check_error_message += "+------------------------------------------------------------------+ \n";
						//			                EQU_check_error_message += "+the System probe card:" + probeCard_array_string[0] + "\n";
						EQU_check_error_message += "+Probe card << " + tx0b.getText() + " >> do not fixture loan from system at " + barcode_testeridStr + "\n";   
						EQU_check_error_message += "| Please call the EQU check probeCard number | \n";
						EQU_check_error_message += "+------------------------------------------------------------------+ \n";
						JOptionPane.showMessageDialog(null, EQU_check_error_message);
						autoSaveMessageBeforeExit();
						saveErrorMessageRealtime("Msg:EQU check probe card do not match");
						//					                killproc(); System.exit(1); // to quit Java app for Linux																							
					}

                    if(Load_ProbeCard_RCP){
						if (!barcode_lbidStr.equals(tx0b.getText()))
						{
							EQU_check_error_message  = "EQU probe card do not match the check in probe card data!\n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							EQU_check_error_message += "+Probe card << " + tx0b.getText() + " >> do not match check in data " + barcode_lbidStr + "\n";   
							EQU_check_error_message += "| Please call the EQU check probeCard number and re-cehckIn RunCard | \n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							JOptionPane.showMessageDialog(null, EQU_check_error_message);
							autoSaveMessageBeforeExit();
							saveErrorMessageRealtime("Msg:EQU check probe card do not match");
							killproc(); System.exit(1); // to quit Java app for Linux											
						}
					}									
				}    

				if (((EQU_FT_flag == true || EQU_FT_check_ProbeCard_flag == false) && testTypeStr.equalsIgnoreCase("Final"))
						|| ((EQU_check_probeCard == true || EQU_CP_check_ProbeCard_flag == false) &&
								testTypeStr.equalsIgnoreCase("Wafer"))){

					startTestOk= true;
					bt1.setEnabled(false);
					bt2_2.setEnabled(true);
					bt_EQU.setEnabled(false);
					kit_No_panel.setEditable(false);
					tx0b.setEditable(false);
					socket_No_panel.setEditable(false);						
					socket_No_pane2.setEditable(false);
					socket_No_pane3.setEditable(false);
					socket_No_pane4.setEditable(false);
					socket_No_pane5.setEditable(false);
					socket_No_pane6.setEditable(false);
					socket_No_pane7.setEditable(false);
					socket_No_pane8.setEditable(false);
					tmpStr = "Please use \"Start test\" to Start test !\n\n";
					System.out.println(tmpStr); 
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);
					JOptionPane.showMessageDialog(null, tmpStr);
				}
			}
			
//			L227_infoFIle_Copy();

			//st 20120828
//			if(autoSend_CP){
//				autoSendPathCP_MAP = "/usr/local/home/vol5/MAP/"; //20100107 Sigurd only
//				autoSendPathCP_Summary = "/usr/local/home/vol5/Summary/";	//20130121 summary upload to server
//			} else{
//				autoSendPathCP_MAP = "/mnt/automount/server_x/harrison_x/autoResult/autosend"; //Sigurd only  //--hh
//			}
//			autoSendPathCP_MAP += barcode_customerStr + "/";
//			File dirAS_CP = new File(autoSendPathCP_MAP); 
//			if ( !dirAS_CP.exists() )               
//			{                                    
//				dirAS_CP.mkdirs();                
//			}

			//sp 20120828


			//  check .so date code
			BufferedReader SoBr;
			BufferedReader dateStr1Br;
			BufferedReader dateStr2Br;
			String infileStr = "";
			String SoStr = "";

			/*   //--hh 	    
            infileStr = JobPathStr+TPStr;
	    // System.out.print( "@@@ infileStr = " + infileStr + " @@@ \n");  // --hh

            try {
                SoBr = new BufferedReader(new FileReader(infileStr));//-- hh   open .../Program/*.una    file

                while ((tmpStr = SoBr.readLine()) != null) {                   

                    if (tmpStr.length()!=0) {
                        tmpStr=stringRemoveSpaceHeadTail(tmpStr);
                        //System.out.print(tmpStr + "\n"); 
                        if(tmpStr.indexOf("TestProg")!=-1) {
                            //System.out.print(tmpStr + "\n"); 
                            int idx1 =0,idx2=0,cnt=1;
                            idx1=tmpStr.indexOf("\"");
                            idx2=tmpStr.lastIndexOf("\"");
                            //System.out.print(idx1 + "\n"); 
                            //System.out.print(idx2 + "\n"); 
                            SoStr = tmpStr.substring(idx1+1,idx2);
                           // System.out.print(" @@@ SoStr = " + SoStr + "\n");  //--hh
                        }
                    }
                }       
                SoBr.close(); // close file
            } catch (FileNotFoundException event) {

                tmpStr  = "<Exception> getSoFileInfo: " + infileStr + " is NOT Found !\n";
                tmpStr += "+----------------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+----------------------------+ \n";
                System.out.println(tmpStr);

                tmpStr  = "<Exception> getSoFileInfo: " + infileStr + " is NOT Found !\n";
                tmpStr += "+--------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+--------------------+ \n";
                JOptionPane.showMessageDialog(null, tmpStr);
                autoSaveMessageBeforeExit();
                saveErrorMessageRealtime("Msg:So file info is not found");
                killproc(); System.exit(1); // to quit Java app for Linux

            } catch (java.io.IOException err) {
                tmpStr = "<Exception> getSoFileInfo: " + err + "\n";
                tmpStr += "+----------------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+----------------------------+ \n";
                System.out.println(tmpStr);

                tmpStr = "<Exception> getSoFileInfo: " + err + "\n";
                tmpStr += "+--------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+--------------------+ \n";
                JOptionPane.showMessageDialog(null, tmpStr);
                autoSaveMessageBeforeExit();
                saveErrorMessageRealtime("Msg:So file info is not found");
                killproc(); System.exit(1); // to quit Java app for Linux
            }
            String cmdStr = "";
            cmdStr = csicAutoPath + "classes/statCmd " +JobPathStr + " " + SoStr;

            javaExecSystemCmd(cmdStr);
			 */

			String cmdStr = "";  //--hh

			String dateStrLocal="";
			String dateStrServer="";
			String targetJotStr="";
			int xml_or_rcp_CheckKey = 0; //0:check xml&so ; 1:check rcp&so
			if((barcode_datecode.equals("NA"))){
				xml_or_rcp_CheckKey = 1;
			}


			//--hh
			/*            infileStr = "/tmp/automation_stat.txt";
            try {
                dateStr1Br = new BufferedReader(new FileReader(infileStr));//open file

                while ((tmpStr = dateStr1Br.readLine()) != null) {                   

                    if (tmpStr.length()!=0) {
                        tmpStr=stringRemoveSpaceHeadTail(tmpStr);
                        //System.out.print(tmpStr + "\n"); 
                        if(tmpStr.indexOf("Modify")!=-1) {
                            System.out.print(tmpStr + "\n"); 
                            int idx1 =0,idx2=0,cnt=1;
                            idx1=tmpStr.indexOf(" ");
                            idx2=tmpStr.indexOf(".");
                            dateStrLocal = tmpStr.substring(idx1+1,idx2-2);
                            dateStrLocal= stringRemoveChar(dateStrLocal,' ');
                            dateStrLocal= stringRemoveChar(dateStrLocal,'-');
                            dateStrLocal= stringRemoveChar(dateStrLocal,':');
                        }
                    }
                }       
                dateStr1Br.close(); // close file
                cmdStr = "rm -f "+infileStr;
                System.out.print(cmdStr + "\n"); 
                textArea2.append(cmdStr + "\n");
               // javaExecSystemCmd(cmdStr); //--hh
            } catch (FileNotFoundException event) {

                tmpStr  = "<Exception> getDateStrLocalInfo: " + infileStr + " is NOT Found !\n";
                tmpStr += "+----------------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+----------------------------+ \n";
                System.out.println(tmpStr);

                tmpStr  = "<Exception> getDateStrLocalInfo: " + infileStr + " is NOT Found !\n";
                tmpStr += "+--------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+--------------------+ \n";
                JOptionPane.showMessageDialog(null, tmpStr);
                autoSaveMessageBeforeExit();
                saveErrorMessageRealtime("Msg:Date file(local) info is not found");
                killproc(); System.exit(1); // to quit Java app for Linux

            } catch (java.io.IOException err) {
                tmpStr = "<Exception> getDateStrLocalInfo: " + err + "\n";
                tmpStr += "+----------------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+----------------------------+ \n";
                System.out.println(tmpStr);

                tmpStr = "<Exception> getDateStrLocalInfo: " + err + "\n";
                tmpStr += "+--------------------+ \n";
                tmpStr += "| Please call the Supervisor | \n";
                tmpStr += "+--------------------+ \n";
                JOptionPane.showMessageDialog(null, tmpStr);
                autoSaveMessageBeforeExit();
                saveErrorMessageRealtime("Msg:Date file(local) info is not found");
                killproc(); System.exit(1); // to quit Java app for Linux
            }

			 */	    

			/*   //--hh	    
            dateStrServer = dateCodeStr;
            System.out.println("dateStrLocal  :" + dateStrLocal  +"\n"); 
            System.out.println("dateStrServer :" + dateStrServer +"\n"); 
            textArea2.append("dateStrLocal  :" + dateStrLocal  +"\n");
            textArea2.append("dateStrServer :" + dateStrServer +"\n");
            saveMessageRealtime("dateStrLocal  :" + dateStrLocal  +"\n");
            saveMessageRealtime("dateStrServer :" + dateStrServer +"\n");

            if (xml_or_rcp_CheckKey==0){
		            if(barcode_customerStr.equals("L022")||barcode_customerStr.equals("L129")||barcode_customerStr.equals("L320")||barcode_customerStr.equals("F054")||barcode_customerStr.equals("L389")){
		            	 barcode_datecode = barcode_datecode.substring(0,12);
				           if(!dateStrLocal.equals("") &&!dateStrLocal.equals(barcode_datecode)) {
				              tmpStr = "<Exception> XML & SO DateCode check Error \n";
				              tmpStr += "+--------------------+ \n";
				              tmpStr += "+.so datecode:";
				              tmpStr += dateStrLocal;
				              tmpStr += "+ \n";
				              tmpStr += "+.xml datecode:";
				              tmpStr += barcode_datecode;
				              tmpStr += "+ \n";				              
				              tmpStr += "| Please call the Supervisor | \n";
				              tmpStr += "+--------------------+ \n";
				              JOptionPane.showMessageDialog(null, tmpStr);
				              autoSaveMessageBeforeExit();
				              saveErrorMessageRealtime("Msg:XML & SO DateCode check Error");
				              killproc(); System.exit(1); // to quit Java app for Linux
				           }
				       }             
          	}

            if (xml_or_rcp_CheckKey==1){    
            		if(barcode_customerStr.equals("L022")||barcode_customerStr.equals("L129")||barcode_customerStr.equals("L320")||barcode_customerStr.equals("F054")||barcode_customerStr.equals("L389")){
		            			if(!dateStrLocal.equals("") &&!dateStrLocal.equals(dateStrServer)) {
		                		tmpStr = "<Exception> RCP & SO DateCode check Error \n";
		                		tmpStr += "+--------------------+ \n";
				              	tmpStr += "+.so datecode:";
				              	tmpStr += dateStrLocal;
				              	tmpStr += "+ \n";
				              	tmpStr += "+.rcp datecode:";
				              	tmpStr += dateStrServer;
				              	tmpStr += "+ \n";
		               			tmpStr += "| Please call the Supervisor | \n";
		                		tmpStr += "+--------------------+ \n";
		                		JOptionPane.showMessageDialog(null, tmpStr);
		                		autoSaveMessageBeforeExit();
		                		saveErrorMessageRealtime("Msg:RCP & SO DateCode check Error");
		                		killproc(); System.exit(1); // to quit Java app for Linux
		            			}
		        		}else {
		            			if(!dateStrServer.equals("") &&!dateStrLocal.equals(dateStrServer)) {		//whthout MTK custmer rcp datecode check by ChiaHui 20130503
		                		tmpStr = "<Exception> RCP & SO DateCode check Error \n";
		                		tmpStr += "+--------------------+ \n";
				              	tmpStr += "+.so datecode:";
				              	tmpStr += dateStrLocal;
				              	tmpStr += "+ \n";
				              	tmpStr += "+.rcp datecode:";
				              	tmpStr += dateStrServer;
				              	tmpStr += "+ \n";
		               			tmpStr += "| Please call the Supervisor | \n";
		                		tmpStr += "+--------------------+ \n";
		                		JOptionPane.showMessageDialog(null, tmpStr);
		                		autoSaveMessageBeforeExit();
		                		saveErrorMessageRealtime("Msg:RCP & SO DateCode check Error");
		                		killproc(); System.exit(1); // to quit Java app for Linux
		            			}
		        		}                                          
          	}

			 */

		} else if (e.getSource()==bt2_1) { //20090601
			ChangeNewLotFlag = true;
			beforeJobStr = TPStr;

			//          autonewlotCnt = 0;//20120828
			tmpStr = "\n___" + btCnt + "___ Change New Lot ...\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			cleanBarCodeXML();//20120828
			if (getBarCodeXMLInfo()) {   
				if(MTK_series){
					MTKFlag = 1;//20120828
					if (getRecipeFileInfo()) {
						if (checkDmdSWVersion()) {
							if (getLoadFileInfo()) { //--hh
								if (true) {              //--hhs
									if (checkDmdSWVersion()) {
										if(robot_type_num>1){
											JOptionPane jp1 = new JOptionPane();                                 //20081021
											tmpStr = "<Warning>:There are multiple \"Robot Type\" in the file."; //20081021
											tmpStr +="\nPlease Select one of them...\n\n";                            //20081021
											System.out.print(tmpStr); 
											textArea2.append(tmpStr);
											saveMessageRealtime(tmpStr);
											jp1.showMessageDialog(null, tmpStr);                                 //20081021
											CallRobotFrame();                                                    //20081021
											btCnt ++;
											tmpStr = "\n___" + btCnt + "___ Selected Robot is -"+robot_btname[0]+"- ...\n";
											RobotStr_set = robot_btname[0];
											tmpStr += "___" + btCnt + "___ NOW barcode_stationStr = " + barcode_stationStr + "\n"; 
											System.out.print(tmpStr); 
											textArea2.append(tmpStr);
											saveMessageRealtime(tmpStr);

										}
										tmpStr = "";
										tmpStr = "Please use \"Start test\" to Start test !\n\n";
										System.out.println(tmpStr); 
										textArea2.append(tmpStr);
										saveMessageRealtime(tmpStr);
										JOptionPane.showMessageDialog(null, tmpStr);
									}                                          
								}
							}
						}
					}
				}
				else{

					MTKFlag = 0;//20120828
					if (getRecipeFileInfo()) {
						if (checkDmdSWVersion()) {
							if(robot_type_num>1){
								JOptionPane jp1 = new JOptionPane();                                 //20081021
								tmpStr = "<Warning>:There are multiple \"Robot Type\" in the file."; //20081021
								tmpStr +="\nPlease Select one of them...\n\n";                            //20081021
								System.out.print(tmpStr); 
								textArea2.append(tmpStr);
								saveMessageRealtime(tmpStr);
								jp1.showMessageDialog(null, tmpStr);                                 //20081021
								CallRobotFrame();                                                    //20081021
								btCnt ++;
								tmpStr = "\n___" + btCnt + "___ Selected Robot is -"+robot_btname[0]+"- ...\n";
								RobotStr_set = robot_btname[0];
								tmpStr += "___" + btCnt + "___ NOW barcode_stationStr = " + barcode_stationStr + "\n"; 
								System.out.print(tmpStr); 
								textArea2.append(tmpStr);
								saveMessageRealtime(tmpStr);

							}
							tmpStr = "";
							tmpStr = "Please use \"Start test\" to Start test !\n\n";
							System.out.println(tmpStr); 
							textArea2.append(tmpStr);
							saveMessageRealtime(tmpStr);
							JOptionPane.showMessageDialog(null, tmpStr);
						}                                          
					}
				}
			}
			else {
				tmpStr  = "getBarCodeXMLInfo: status=false >>> UNKNOWN ERROR <<< \n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "getBarCodeXMLInfo: status=false >>> UNKNOWN ERROR <<< \n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:get Barcode xml info is fail");
				killproc(); System.exit(1); // to quit Java app for Linux
			}

			tmpStr = "Compare former and current job string\n";
			tmpStr+= "former Job String: " + beforeJobStr + "\n";
			tmpStr+= "current Job String: " + TPStr + "\n";
			if(beforeJobStr.equals(TPStr)) {            
				tmpStr+="former and current Job String is matched\n";
				System.out.println(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
			}
			else{
				tmpStr+="former and current Job String is Not matched\n";
				tmpStr+="The system will exit.\n";
				System.out.println(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:former and current Job String is Not matched");
				killproc(); System.exit(1); // to quit Java app for Linux
			}

			startTestOk= true;

			//  check .so date code
			BufferedReader SoBr;
			BufferedReader dateStr1Br;
			BufferedReader dateStr2Br;
			String infileStr = "";
			String SoStr = "";
			infileStr = JobPathStr+TPStr;
			try {
				SoBr = new BufferedReader(new FileReader(infileStr));//open file

				while ((tmpStr = SoBr.readLine()) != null) {                   

					if (tmpStr.length()!=0) {
						tmpStr=stringRemoveSpaceHeadTail(tmpStr);
						//System.out.print(tmpStr + "\n"); 
						if(tmpStr.indexOf("TestProg")!=-1) {
							//System.out.print(tmpStr + "\n"); 
							int idx1 =0,idx2=0,cnt=1;
							idx1=tmpStr.indexOf("\"");
							idx2=tmpStr.lastIndexOf("\"");
							//System.out.print(idx1 + "\n"); 
							//System.out.print(idx2 + "\n"); 
							SoStr = tmpStr.substring(idx1+1,idx2);

							//System.out.print(SoStr + "\n"); 
						}
					}
				}       
				SoBr.close(); // close file
			} catch (FileNotFoundException event) {

				tmpStr  = "<Exception> getSoFileInfo: " + infileStr + " is NOT Found !\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "<Exception> getSoFileInfo: " + infileStr + " is NOT Found !\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:So file info is not found");
				killproc(); System.exit(1); // to quit Java app for Linux

			} catch (java.io.IOException err) {
				tmpStr = "<Exception> getSoFileInfo: " + err + "\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr = "<Exception> getSoFileInfo: " + err + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:So file info is not found");
				killproc(); System.exit(1); // to quit Java app for Linux
			}
			String cmdStr = "";
			cmdStr = csicAutoPath + "classes/statCmd " +JobPathStr + " " + SoStr;

			javaExecSystemCmd(cmdStr);

			String dateStrLocal="";
			String dateStrServer="";
			String targetJotStr="";
			int xml_or_rcp_CheckKey = 0; //0:check xml&so ; 1:check rcp&so
			if((barcode_datecode.equals("NA"))){
				xml_or_rcp_CheckKey = 1;
			}

			infileStr = "/tmp/automation_stat.txt";
			try {
				dateStr1Br = new BufferedReader(new FileReader(infileStr));//open file

				while ((tmpStr = dateStr1Br.readLine()) != null) {                   

					if (tmpStr.length()!=0) {
						tmpStr=stringRemoveSpaceHeadTail(tmpStr);
						//System.out.print(tmpStr + "\n"); 
						if(tmpStr.indexOf("Modify")!=-1) {
							System.out.print(tmpStr + "\n"); 
							int idx1 =0,idx2=0,cnt=1;
							idx1=tmpStr.indexOf(" ");
							idx2=tmpStr.indexOf(".");
							dateStrLocal = tmpStr.substring(idx1+1,idx2-2);
							dateStrLocal= stringRemoveChar(dateStrLocal,' ');
							dateStrLocal= stringRemoveChar(dateStrLocal,'-');
							dateStrLocal= stringRemoveChar(dateStrLocal,':');
						}
					}
				}       
				dateStr1Br.close(); // close file
				cmdStr = "rm -f "+infileStr;
				System.out.print(cmdStr + "\n"); 
				textArea2.append(cmdStr + "\n");
				javaExecSystemCmd(cmdStr);
			} catch (FileNotFoundException event) {

				tmpStr  = "<Exception> getDateStrLocalInfo: " + infileStr + " is NOT Found !\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "<Exception> getDateStrLocalInfo: " + infileStr + " is NOT Found !\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:Date file(local) info is not found");
				killproc(); System.exit(1); // to quit Java app for Linux

			} catch (java.io.IOException err) {
				tmpStr = "<Exception> getDateStrLocalInfo: " + err + "\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr = "<Exception> getDateStrLocalInfo: " + err + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				autoSaveMessageBeforeExit();
				saveErrorMessageRealtime("Msg:Date file(local) info is not found");
				killproc(); System.exit(1); // to quit Java app for Linux
			}
			dateStrServer = dateCodeStr;
			System.out.println("dateStrLocal  :" + dateStrLocal  +"\n"); 
			System.out.println("dateStrServer :" + dateStrServer +"\n"); 
			textArea2.append("dateStrLocal  :" + dateStrLocal  +"\n");
			textArea2.append("dateStrServer :" + dateStrServer +"\n");
			saveMessageRealtime("dateStrLocal  :" + dateStrLocal  +"\n");
			saveMessageRealtime("dateStrServer :" + dateStrServer +"\n");

			if (xml_or_rcp_CheckKey==0){
				if(MTK_series){
					barcode_datecode = barcode_datecode.substring(0,12);
					if(!dateStrLocal.equals("") &&!dateStrLocal.equals(barcode_datecode)) {
						tmpStr = "<Exception> XML & SO DateCode check Error \n";
						tmpStr += "+--------------------+ \n";
						tmpStr += "+.so datecode:";
						tmpStr += dateStrLocal;
						tmpStr += "+ \n";
						tmpStr += "+.xml datecode:";
						tmpStr += barcode_datecode;
						tmpStr += "+ \n";				              
						tmpStr += "| Please call the Supervisor | \n";
						tmpStr += "+--------------------+ \n";
						JOptionPane.showMessageDialog(null, tmpStr);
						autoSaveMessageBeforeExit();
						saveErrorMessageRealtime("Msg:XML & SO DateCode check Error");
						killproc(); System.exit(1); // to quit Java app for Linux
					}
				}             
			}

			if (xml_or_rcp_CheckKey==1){    
				if(MTK_series){
					if(!dateStrLocal.equals("") &&!dateStrLocal.equals(dateStrServer)) {
						tmpStr = "<Exception> RCP & SO DateCode check Error \n";
						tmpStr += "+--------------------+ \n";
						tmpStr += "+.so datecode:";
						tmpStr += dateStrLocal;
						tmpStr += "+ \n";
						tmpStr += "+.rcp datecode:";
						tmpStr += dateStrServer;
						tmpStr += "+ \n";			                		
						tmpStr += "| Please call the Supervisor | \n";
						tmpStr += "+--------------------+ \n";
						JOptionPane.showMessageDialog(null, tmpStr);
						autoSaveMessageBeforeExit();
						saveErrorMessageRealtime("Msg:RCP & SO DateCode check Error");
						killproc(); System.exit(1); // to quit Java app for Linux
					}
				}else {
					if(!dateStrServer.equals("") &&!dateStrLocal.equals(dateStrServer)) {		//whthout MTK custmer rcp datecode check by ChiaHui 20130503
						tmpStr = "<Exception> RCP & SO DateCode check Error \n";
						tmpStr += "+--------------------+ \n";
						tmpStr += "+.so datecode:";
						tmpStr += dateStrLocal;
						tmpStr += "+ \n";
						tmpStr += "+.rcp datecode:";
						tmpStr += dateStrServer;
						tmpStr += "+ \n";
						tmpStr += "| Please call the Supervisor | \n";
						tmpStr += "+--------------------+ \n";
						JOptionPane.showMessageDialog(null, tmpStr);
						autoSaveMessageBeforeExit();
						saveErrorMessageRealtime("Msg:RCP & SO DateCode check Error");
						killproc(); System.exit(1); // to quit Java app for Linux
					}		        			
				}                                          
			}

		} else if (e.getSource()==bt2_2) {
			//	javaExecSystemCmd("xterm -rightbar -bg green -fg black -geometry 158x20  -e  pwd.csh" ); //20140522
			//	System.out.println("====bt2_2===========");
			tmpStr = "\n___" + btCnt + "___ Start test ...\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
			
			if(testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("L227")){
				if(RTStr.equals("HW")){	//20170821-----Start
					HWstationUsage_UnloadOI = true;
				}else{
					if(HWstationUsage_UnloadOI){
						JOptionPane.showMessageDialog(null, "L227 can't be Production after HW station used !! Please reload Program !!");
//						killproc();System.exit(1);	// to quit Java app for Linux
						return;
					}
				}	//20170821-----End
				
				L227_infoFIle_Copy();	//20170710
			}
			
			if(testTypeStr.equalsIgnoreCase("Wafer") && MTK_series)
				if(barcode_devicetypeStr.indexOf("BM10551")!=-1 || barcode_devicetypeStr.indexOf("AM10690")!=-1 || barcode_devicetypeStr.indexOf("AZA10699")!=-1 || barcode_devicetypeStr.indexOf("AXD10699")!=-1) //20170920
					//if(!barcode_programStr.equals("AZA10699BW_U511_V05.load")) //Temp by Program
					if(!MTK_HSMCode_Copy())	//20170808	
						return;
			
			//Send GPIB Command for Handler Setting. by Cola 20170619      		
			try
			{
				if(testTypeStr.equalsIgnoreCase("Final")){
					GPIB_AlarmSetup();
					Thread.sleep(5000);  //Delay 5 sec for waiting Handler Temperature add by Cola. 20160414
				}
			} catch (InterruptedException e1)
			{
				e1.printStackTrace(); JOptionPane.showMessageDialog(null, "GPIB_AlarmSetup() Interrupted Exception: " + e1);
			}// unit: ms
			
			
			sumTmpNum = 0;//20081110
			sumRepNum = 1;
			if(sumRepNum<10){
				sumRepNumStr = "0" + Integer.toString(sumRepNum);
			} else {
				sumRepNumStr = Integer.toString(sumRepNum);
			}
			sumRepFlag = false;//default 20081110
			boolean sumRepTmpFlag = true; //20090601

			while(sumRepFlag||sumRepTmpFlag) {
				//System.out.println("loop");
				sumRepTmpFlag = false;//20090601
				tmpfileStr = userSummaryfinalPath_bak;//20100128
				try {//20081107 fool proof
					if(RTStr.equals("A1")){    //20081107
						tmpfileStr += SGStr + "_" + LotStr + "_" + barcode_stationStr + "_R0_ALL" + "-" + sumRepNumStr + ".sum";//20081107
					} else if(RTStr.equals("HW")){ 
						tmpfileStr += SGStr + "_" + LotStr + "_" + "HW" + "-" + sumRepNumStr + ".sum";//20081107
					} else if(RTStr.equals("EQC")){//20081110//20100618
						if(EQC_QNum_Str.startsWith("QA1")) {
							tmpfileStr += SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str +"-" + sumRepNumStr + ".sum";//20100120
						} else{ 
								tmpfileStr += SGStr + "_" + LotStr + "_" + EQC_bin_Str + EQC_qbin_Str + "_" + ANFQrtBinSumStr + EQC_QNum_Str2 + QrtBinStr +"-" + sumRepNumStr + ".sum";//20100120 //Add for ANF RTbin 20161026
						}

						} else if (RTStr.equals("CORR")) {  //20081230  //Add Corr Bin by Cola. 20160408
							tmpfileStr += SGStr + "_" + LotStr + "_" + barcode_stationStr + "-CORR-" + CORRStr_set +CORRbinStr_set+ "-" + sumRepNumStr + ".sum";//20130509 by ChiaHui
					} else {//RT
							tmpfileStr += SGStr + "_" + LotStr + "_" + barcode_stationStr + "_" + ANFrtBinSumStr + RTNumStr2 + rtBinSumStr + "-" + sumRepNumStr + ".sum";//20100505 //Add for ANF RTbin 20161026 
					}

					sumRepFlag = false; //repeat-->true,no repeat -->false,deault -->false 
					br = new BufferedReader(new FileReader(tmpfileStr));//open file

					//System.out.println(tmpfileStr);

					sumRepFlag = true; //repeat-->true,no repeat -->false,deault -->false 

					tx4_1.setText(Integer.toString(sumRepNum));

					if(sumRepNum<10){
						tx4_1.setText("0" + Integer.toString(sumRepNum));
					} else {
						tx4_1.setText(Integer.toString(sumRepNum));
					}

					tx4_1.setBackground(new Color(251,249,209));
					br.close(); // close file
					sumRepNum = sumRepNum +1;

					if(sumRepNum<10){
						tx4_1.setText("0" + Integer.toString(sumRepNum));
						sumRepNumStr = "0" + Integer.toString(sumRepNum);
					} else {
						tx4_1.setText(Integer.toString(sumRepNum));
						sumRepNumStr = Integer.toString(sumRepNum);
					}
				} catch (FileNotFoundException event) {
					sumRepFlag = false;
				} catch (java.io.IOException err) {
					sumRepFlag = false;
				}

			}
			
			//20170720
			if(testTypeStr.equalsIgnoreCase("Final")){
				if(NewHandler_GPIBnoSupport)
				{ //L178 this two Device are low temperature testing, by pass GPIB check. by Cola. 20160823
					Call_FT_GPIB_warnning(); 
				}
				else{
					try{

						if(gpibCheck()){//20110614 
							;
						}
					}catch (Exception e1){
						JOptionPane.showMessageDialog(null, "gpibCheck Fail, Please Call EQU Check Handler GPIB connect ! \n" + "gpibCheck() Fail Message: " + e1);
						killproc(); System.exit(1); // to quit Java app for Linux
					}
				}
			}else{
				try{
			/*		if(RobotStr_set.equals("TEL_soft") || RobotStr_set.equals("TEL Series")) {
						if(gpibCPCheck_TEL()){//20130508
							;
						}
					}
					else{
						if(gpibCPCheck()){//20110614
							;
						}
					}
			*/	}catch (Exception e1){
					JOptionPane.showMessageDialog(null, "CP gpibCheck Fail, Please Call EQU Check Prober GPIB connect ! \n" + "CP gpibCheck() Fail Message: " + e1);
					killproc(); System.exit(1); // to quit Java app for Linux
				}
			}
			
			if(testTypeStr.equalsIgnoreCase("Final")){ //barcode kit LB socket add by Chia-Hui 20130416            	
				barcode_KIT_process();
				barcode_LB_process();
				CountSocketQuantity();	
			}

			if(testTypeStr.equalsIgnoreCase("wafer")){ //barcode kit LB socket add by Chia-Hui 20131218
				barcode_LB_process();
			}

			STDF_start_time = getDateTime3();	//Move to Here. 20171121
			if(testTypeStr.equalsIgnoreCase("Final"))
				FTsummarySTDFsetup(); //for summary STDF naming. 20170710  //run before generate_userFlag_file(). 20171121
			generate_userFlag_file();
			EQUsocketCheckdata(checkSocketData);	//move to here. 20171229
//			JOptionPane.showMessageDialog(null, "1.STDfileStr = " + STDfileStr);
//			JOptionPane.showMessageDialog(null, "2.summaryfileStr = " + summaryfileStr);


			/*     if (genDmdCmdFile()) {    //--hh
                genExecShellFile();
                processCmdFile();
            } */

			/*         
            barcode_LBC1Str = "";
            barcode_LBC2Str = "";
            private static String LBC1InputStr = ""; //20120412   
            private static String LBC2InputStr = ""; //20120412  
			 */
			//20120412
			SGInputFlag = true;
			boolean SGCompFlag = false;
			boolean LBC1CompFlag = false;
			boolean LBC2CompFlag = false;
			
			int RTSelectionFlag = 0;
			tmpStr = "RT Selection :" + RTCheckStr ;

			// System.out.println("@@@ the R/C Barcode = " + barcode_LBC1Str + " @@@ \n");   //--hh
			// System.out.println("@@@ LOT Barcode = " + barcode_LBC2Str + " @@@ \n");       //--hh

			RTSelectionFlag = JOptionPane.showConfirmDialog(null, tmpStr, "Confirmation ? ", JOptionPane.YES_NO_OPTION);

			
			if((rtrbt[0].isSelected()) && testTypeStr.equalsIgnoreCase("Final")){	//20170619

				String testStationString = "";
				testStationString = "Please select RT STATION to Start test !\n\n";
				System.out.println(testStationString); 
				saveMessageRealtime(testStationString);
				JOptionPane.showMessageDialog(null, testStationString);             	
				RTSelectionFlag = 1;
			} 
/* 20170619
			if (!barcode_L100_Tar_File.equals("") && (barcode_customerStr.equals("L100") || barcode_customerStr.equals("L438"))){ //Add L438 by Cola. 20160726
				if (barcode_L100_Tar_File.length() >= 7){
					if(JobPathStr.indexOf(barcode_L100_Tar_File.substring(0,barcode_L100_Tar_File.length()-7))==-1){

						tmpStr  = "<Exception> L100 XML tar file name VS rcp file job name is not match !\n";//20120412
						tmpStr += "+--------------------+ \n";
						tmpStr += "| rcp :" + JobPathStr + " | \n";			
						tmpStr += "| xml :" + barcode_L100_Tar_File.substring(0,barcode_L100_Tar_File.length()-7)+ " | \n";			
						tmpStr += "| Please call PE check program path folder | \n";
						tmpStr += "+--------------------+ \n";
						JOptionPane.showMessageDialog(null, tmpStr);
						autoSaveMessageBeforeExit();
						saveErrorMessageRealtime("Msg:L100 .tar.gz xml VS rcp is not match");
						killproc(); System.exit(1); // to quit Java app for Linux	    
					}
				}
			}
*/			
			if(RTSelectionFlag==0){//20120412
				for (int i=0; i<rtrbtname.length; i++) //20170619  //Moved to this by Cola. 2015/05/27.
					rtrbt[i].setEnabled(false);	      //When "Start Test" Click, Disable RT Station choice.  by Cola 2015/03/12
				LBC1InputStr = JOptionPane.showInputDialog("Please input the R/C Barcode");
				try {
					Thread.sleep(20);// unit: ms
				} catch (java.lang.InterruptedException Ierr) {
				}
				if(barcode_LBC1Str.equals(LBC1InputStr)) {
					LBC1CompFlag = true;
				}
				if(LBC1CompFlag) {

					LBC2InputStr = JOptionPane.showInputDialog("Please input the LOT Barcode");
					try {
						Thread.sleep(20);// unit: ms
					} catch (java.lang.InterruptedException Ierr) {
					}
					if(barcode_LBC2Str.equals(LBC2InputStr)) {
						LBC2CompFlag = true;
					}
					if(LBC2CompFlag) {

						if(!sumRepFlag){   
							if(MTK_series){  
								// runConfirmation_MTK(); //--hh
								runConfirmation();  //--hh
							}
							else{
								runConfirmation();
							}
							ChangeNewLotFlag = true;
						}
					}
					else{
						tmpStr  = "<Exception> Input LOT Barcode string is not correct!\n";//20120412
						tmpStr += "+--------------------+ \n";
						tmpStr += "| Please check input Barcode | \n";
						tmpStr += "+--------------------+ \n";
						JOptionPane.showMessageDialog(null, tmpStr);
						autoSaveMessageBeforeExit();
						saveErrorMessageRealtime("Msg:Input LOT Barcode string is not correct");
						killproc(); System.exit(1); // to quit Java app for Linux

					}

				}
				else{
					tmpStr  = "<Exception> Input R/C Barcode string is not correct!\n";//20120412
					tmpStr += "+--------------------+ \n";
					tmpStr += "| Please check input Barcode | \n";
					tmpStr += "+--------------------+ \n";
					JOptionPane.showMessageDialog(null, tmpStr);
					autoSaveMessageBeforeExit();
					saveErrorMessageRealtime("Msg:Input R/C Barcode string is not correct");
					killproc(); System.exit(1); // to quit Java app for Linux

				}
			}
			else{
				tmpStr  = "Need to re-select RT station and start test again\n";
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);
			}
			//if(startTestOk){
			bt2_1.setEnabled(false);
			bt2_2.setEnabled(false);
			bt2_4.setEnabled(true);


			System.out.println("RTSelectionFlag :" + RTSelectionFlag + "\n"); 
			System.out.println("ChangeNewLotFlag :" + ChangeNewLotFlag + "\n"); 
			if(RTSelectionFlag==1)
			{
				//bt2_1.setEnabled(true);
				bt2_2.setEnabled(true);
				bt2_4.setEnabled(false);
				if(ChangeNewLotFlag) {
					bt2_1.setEnabled(true);
				}
			}
/* 20190619	//Add Enable Auto endlot here by Cola. 20150610                  
			AutoEndLot_flag = false;
			AutoEndLot_Timeout = false;
			//Enable Timer when FT Testing and Select HW or Corr Station. Modify by Cola. 20150626
			if(testTypeStr.equalsIgnoreCase("Final") && (rtrbt[4].isSelected() || rtrbt[5].isSelected()))
			{
				//                	JOptionPane.showMessageDialog(null, "Start Timer!! Hardware or Correlation is Selected");
				AutoEndLot_flag = true;
				EndLotTimer = new Timer();
				long TimeLimit = 60 * 120 * 1000;   //60 Second X 120 Minutes   (two hours)                 
				EndLotTimer.schedule(new RunTask(), TimeLimit);
			}
			if(testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("F186")){
				if(RTStr.equalsIgnoreCase("A1") || RTStr.equalsIgnoreCase("RT"))
					OpenF186infoFile(); //Add by Cola. 20160525
			}
*/
			//startTestOk= false;
		} else if ((e.getSource()==bt_Clean)) {//20100709

			tmpStr = "Waring : This function is used to stop production without generating summary file.";
			JOptionPane.showMessageDialog(null, tmpStr);
			System.out.println("User press Clean_Data button\n"); 
			textArea2.append("User press Clean_Data button\n");

			userCleanCode = JOptionPane.showInputDialog(null, "Please input the Code");
			System.out.println("The code which you input is :" + userCleanCode + "\n"); 
			textArea2.append("The code which you input is :" + userCleanCode + "\n");

			if(userCleanCode.equals(systemCleanCode)) {
				tmpStr = "The Code is RIGHT. It will stop production without generating the summary file.";
				JOptionPane.showMessageDialog(null, tmpStr);
				System.out.println(tmpStr +"\n"); 
				textArea2.append(tmpStr + "\n");
				runautoStopWoSum();
				JOptionPane.showMessageDialog(null, "End of file processing of stop without generating summary file...");

			}else
			{                
				tmpStr = "The Code is WRONG.";
				JOptionPane.showMessageDialog(null, tmpStr);
				System.out.println(tmpStr +"\n"); 
				textArea2.append(tmpStr + "\n");
			}


		} else if ((e.getSource()==bt_GPIB)) {//20100709  20170323_start
			
			int ENGflag = 0;
			tmpStr = "Waring : This function is used to launcher the optool.";
			// yes to return 0, no to return 1
			ENGflag = JOptionPane.showConfirmDialog(null,tmpStr, "Confirmation ? ", JOptionPane.YES_NO_OPTION); 
			
			if(ENGflag == 0){
//			userCleanCode = JOptionPane.showInputDialog(null, "Please input the Code");
//			if(userCleanCode.equals(systemCleanCode)) {
//				tmpStr = "It will check if Oicu was existed.";                 				
//				JOptionPane.showMessageDialog(null, tmpStr);
//				System.out.println(tmpStr +"\n"); 
//				textArea2.append(tmpStr + "\n");

				String ProductioninfileStr = "";
				System.out.println("User press optool_launcher ENG button\n"); 
				textArea2.append("User press optool_launcher ENG button\n");
				
				//testerBarcodePath = checkifHeadEndWithSlash(testerBarcodePath);

				javaExecSystemCmd("checkProduction\n");  // check oicu if exist
				ProductioninfileStr = "/tmp/ProductionFlag.log";
				int logidx = 1;
				boolean oicuflag = false;
				try {
					br = new BufferedReader(new FileReader(ProductioninfileStr));//open file
					while ((tmpStr = br.readLine()) != null) {
						if (tmpStr.length()!=0) {		
							System.out.println("string : " +tmpStr +"\n"); 
							textArea2.append("string : " +tmpStr +"\n"); 
							if(logidx ==1){					
								if(tmpStr.equals("true")) { // oicu exist
									tmpStr = "Oicu exist. Please Exit Oicu firstly.\n";
									oicuflag = true;				
								}
								else{
									oicuflag = false;
									tmpStr = "Oicu does not exist. It will launcher optool.\n";
								}			
								JOptionPane.showMessageDialog(null, tmpStr);
								System.out.println(tmpStr +"\n"); 
								textArea2.append(tmpStr + "\n");
							}
						}
					}

					br.close(); // close file
				} catch (FileNotFoundException event) {

					tmpStr  = "<Exception> getOicuInfo: " + ProductioninfileStr + " is NOT Found !\n";
					tmpStr += "+----------------------------+ \n";
					tmpStr += "| Please call the Supervisor | \n";
					tmpStr += "+----------------------------+ \n";
					System.out.println(tmpStr);

					tmpStr  = "<Exception> getOicuInfo: " + ProductioninfileStr + " is NOT Found !\n";
					tmpStr += "+--------------------+ \n";
					tmpStr += "| Please call the Supervisor | \n";
					tmpStr += "+--------------------+ \n";
					JOptionPane.showMessageDialog(null, tmpStr);
				//	saveErrorMessageRealtime("Msg:Barcode file .Xml is not found");
				//	killproc(); System.exit(1); // to quit Java app for Linux

				} catch (java.io.IOException err) {
					tmpStr = "<Exception> getOicuInfo: " + err + "\n";
					tmpStr += "+----------------------------+ \n";
					tmpStr += "| Please call the Supervisor | \n";
					tmpStr += "+----------------------------+ \n";
					System.out.println(tmpStr);

					tmpStr = "<Exception> getOicuInfo: " + err + "\n";
					tmpStr += "+--------------------+ \n";
					tmpStr += "| Please call the Supervisor | \n";
					tmpStr += "+--------------------+ \n";
					JOptionPane.showMessageDialog(null, tmpStr);
				//	autoSaveMessageBeforeExit();
				//	saveErrorMessageRealtime("Msg:Barcode file .Xml is not found");
				//	killproc(); System.exit(1); // to quit Java app for Linux
				}
				javaExecSystemCmd("rm -f /tmp/Production.log \n");
				javaExecSystemCmd("rm -f "+ ProductioninfileStr +"\n");  
				
				if(oicuflag == false){
					bt2_2.setEnabled(false);	//20170323
					String launcher_cmd = "";
					//offline = true;				
					if(offline){
						//hostnameStr = "prod_demo_DMDx_sim";
						//launcher_cmd    = "launcher -eng -sim " + hostnameStr + " -nodisplay\n";  
						//tmpStr = "launcher -eng -sim " + hostnameStr + " -nodisplay\n";
						launcher_cmd    = "launcher -prod -sim " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";  
						tmpStr = "launcher -prod -sim " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";
						//hostnameStr = "";				
					}
					else{	     
						launcher_cmd    = "launcher -eng " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";  
						tmpStr = "launcher -eng " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";
					}
					//offline = false;                                        
					textArea2.append(tmpStr);
					System.out.println(tmpStr);
					javaExecSystemCmd2(launcher_cmd,5000);	
					
					wait_until_load_done();     
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp Write_Efuse FALSE\n");	// "Write_Efuse" is the parameter in the .una
					javaExecSystemCmd("cex -t " + hostnameStr + " -c set_exp Manual_Test TRUE\n");	// "Manual_Test" is the parameter in the .una
					JOptionPane.showMessageDialog(null, "ENG Ready to Run! ");    
					
				}		

//			}
//			else
//			{                
//				tmpStr = "The Code is WRONG.";
//				JOptionPane.showMessageDialog(null, tmpStr);
//				System.out.println(tmpStr +"\n"); 
//				textArea2.append(tmpStr + "\n");
//
//			}	
			}
			//20170323_end
			
			/*
			tmpStr = "Waring : This function is used to Control GPIB Compare function.";
			JOptionPane.showMessageDialog(null, tmpStr);
			System.out.println("User press GPIB_SW button\n"); 
			textArea2.append("User press GPIB_SW button\n");

			userCleanCode = JOptionPane.showInputDialog(null, "Please input the Code");
			//System.out.println("The code which you input is :" + userCleanCode + "\n"); 
			//textArea2.append("The code which you input is :" + userCleanCode + "\n");

			if(userCleanCode.equals(systemCleanCode)) {//20110712
				if(gpib_cnt==0){
					gpib_cnt=1;
					gpibFlag =false;
					tmpStr = "The Code is RIGHT. It will disable GPIB Compare function.";
					bt_GPIB.setText("Off");//20110712
					bt_GPIB.setForeground(Color.WHITE);
					bt_GPIB.setBackground(Color.BLUE);

				}
				else {
					tmpStr = "The Code is RIGHT. It will enable GPIB Compare function.";
					gpib_cnt=0;
					gpibFlag =true;  
					bt_GPIB.setText("On");//20110712
					bt_GPIB.setForeground(Color.WHITE);
					bt_GPIB.setBackground(Color.BLUE);

				}
				JOptionPane.showMessageDialog(null, tmpStr);
				System.out.println(tmpStr +"\n"); 
				textArea2.append(tmpStr + "\n");
				//JOptionPane.showMessageDialog(null, "End of file processing of stop without generating summary file...");
				if(gpib_cnt==1){
					String launcher_cmd = "";
					offline = true;

					if(offline){
						hostnameStr = "prod_demo_DMDx_sim";
						cmd    = "launcher -prod -sim " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";  
						tmpStr = "launcher -prod -sim " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";
						hostnameStr = "";		
						System.out.println("jackie");
						System.out.println("jackie");				
					}
					else{	     
						cmd    = "launcher -prod " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";  
						tmpStr = "launcher -prod " + hostnameStr + " -load " + JobPathStr  + TPStr + " -nodisplay\n";
					}
					offline = false;                                        
				        textArea2.append(tmpStr);
					System.out.println(tmpStr);
					javaExecSystemCmd2(launcher_cmd,5000);	
				}

			}else
			{                
				tmpStr = "The Code is WRONG.";
				JOptionPane.showMessageDialog(null, tmpStr);
				System.out.println(tmpStr +"\n"); 
				textArea2.append(tmpStr + "\n");

			}
*/

		} else if ((e.getSource()==bt_EQU)) {	//20130516 EQU Kit,LB,socket check button by ChiaHui

			String EQUcmd;
			String[] probeCard_array;
			String EQU_check_error_message;
			boolean EQU_check_probeCard = false;
			boolean EQU_FT_flag = false;                  
			int exitflag = 0;

			tmpStr = "check EQU,KIT,socket LB/probeCard barcode are OK???";
			// yes to return 0, no to return 1
			exitflag = JOptionPane.showConfirmDialog(null,tmpStr, "Confirmation ? ", JOptionPane.YES_NO_OPTION); 
			System.out.println("check EQU,KIT,socket LB/probeCard barcode are OK???\n"); 
			textArea2.append("check EQU,KIT,socket LB/probeCard barcode are OK???\n");

			if(exitflag == 0){
//				Get_PC_LB_Info_for_SUM_STDF(); //Add by Cola. 20160307
				if(EQU_FT_check_ProbeCard_flag == true && testTypeStr.equalsIgnoreCase("Final")){

					//Add Socket Repeat Check by Cola. 20170414-----Start
					String socketID[] = new String[10];
					String message = "";
					boolean socketIDrepeat = false;
					socketID[1] = socket_No_panel.getText();socketID[2] = socket_No_pane2.getText();socketID[3] = socket_No_pane3.getText();socketID[4] = socket_No_pane4.getText();socketID[5] = socket_No_pane5.getText();socketID[6] = socket_No_pane6.getText();socketID[7] = socket_No_pane7.getText();socketID[8] = socket_No_pane8.getText();				

					for(int s1 = 1 ; s1 <= barcode_duts_int ; s1 ++ ){
						for(int s2 = 1 ; s2 <= barcode_duts_int ; s2 ++ ){
//							JOptionPane.showMessageDialog(null, "3. socketID["+s1+"] = " + socketID[s1] + " , " + "socketID["+s2+"] = " + socketID[s2]);
							if(s1 != s2 && socketID[s1].equals(socketID[s2]) && !socketID[s1].equals("")){
								socketIDrepeat = true;
								message += "site" + s1 + " , site" + s2 + " is repeat : " + socketID[s1] + "\n";
//								JOptionPane.showMessageDialog(null, "socketID["+s1+"] = " + socketID[s1] + " , " + "socketID["+s2+"] = " + socketID[s2]);
							}
						}
					}
					if(socketIDrepeat){
						JOptionPane.showMessageDialog(null, "========== Don't repeat key in Socket ID ========== \n\n" + message);
					}else{	//Add Socket Repeat Check by Cola. 20170414-----End
						EQU_FT_flag = true;
						boolean EQU_FT_KIT_check_flag = true;        
						boolean EQU_FT_LB_check_flag = true;         
						boolean EQU_FT_socket1_check_flag = true;    
						boolean EQU_FT_socket2_check_flag = true;    
						boolean EQU_FT_socket3_check_flag = true;    
						boolean EQU_FT_socket4_check_flag = true;    
						boolean EQU_FT_socket5_check_flag = true;    
						boolean EQU_FT_socket6_check_flag = true;    
						boolean EQU_FT_socket7_check_flag = true;    
						boolean EQU_FT_socket8_check_flag = true;    

						File EQU_FT_KIT_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + kit_No_panel.getText() + ".txt");
						File EQU_FT_LB_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + tx0b.getText() + ".txt");
						File EQU_FT_socket1_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_panel.getText() + ".txt");
						File EQU_FT_socket2_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane2.getText() + ".txt");				
						File EQU_FT_socket3_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane3.getText() + ".txt");
						File EQU_FT_socket4_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane4.getText() + ".txt");
						File EQU_FT_socket5_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane5.getText() + ".txt");
						File EQU_FT_socket6_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane6.getText() + ".txt");
						File EQU_FT_socket7_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane7.getText() + ".txt");
						File EQU_FT_socket8_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane8.getText() + ".txt");

						String EQU_error_list = "";

						//20140304 check the Kit,LB,sockets		

						//					EQUcmd = "rm -f /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr;
						//					javaExecSystemCmd2(EQUcmd,1000);
						//					System.out.println(EQUcmd);
						//					saveMessageRealtime(EQUcmd);

						EQUcmd = "ftp_sigurd_check_EQU_probeCard.csh " + barcode_testeridStr+ " /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/";
						javaExecSystemCmd2(EQUcmd,7000);
						System.out.println(EQUcmd);
						saveMessageRealtime(EQUcmd);	

						File EQU_check_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr);
						if (EQU_check_dir.isDirectory()){

							if(!EQU_FT_KIT_dir.isFile() || !kit_No_panel.getText().substring(0, 2).equals("CK")){
								EQU_FT_KIT_check_flag = false;
							}

							if (!EQU_FT_LB_dir.isFile() || (!tx0b.getText().substring(0, 2).equals("LB") && !tx0b.getText().substring(0, 2).equals("MB"))){
								EQU_FT_LB_check_flag = false;
							}

							if (!socket_No_panel.getText().equalsIgnoreCase("") && (!EQU_FT_socket1_dir.isFile() || !socket_No_panel.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket1_check_flag = false;
							}

							if (!socket_No_pane2.getText().equalsIgnoreCase("") && (!EQU_FT_socket2_dir.isFile() || !socket_No_pane2.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket2_check_flag = false;
							}

							if (!socket_No_pane3.getText().equalsIgnoreCase("") && (!EQU_FT_socket3_dir.isFile() || !socket_No_pane3.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket3_check_flag = false;
							}

							if (!socket_No_pane4.getText().equalsIgnoreCase("") && (!EQU_FT_socket4_dir.isFile() || !socket_No_pane4.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket4_check_flag = false;
							}

							if (!socket_No_pane5.getText().equalsIgnoreCase("") && (!EQU_FT_socket5_dir.isFile() || !socket_No_pane5.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket5_check_flag = false;
							}

							if (!socket_No_pane6.getText().equalsIgnoreCase("") && (!EQU_FT_socket6_dir.isFile() || !socket_No_pane6.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket6_check_flag = false;
							}

							if (!socket_No_pane7.getText().equalsIgnoreCase("") && (!EQU_FT_socket7_dir.isFile() || !socket_No_pane7.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket7_check_flag = false;
							}

							if (!socket_No_pane8.getText().equalsIgnoreCase("") && (!EQU_FT_socket8_dir.isFile() || !socket_No_pane8.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket8_check_flag = false;
							}

							if (EQU_FT_KIT_check_flag == false || EQU_FT_LB_check_flag == false || EQU_FT_socket1_check_flag == false || EQU_FT_socket2_check_flag == false || EQU_FT_socket3_check_flag == false || EQU_FT_socket4_check_flag == false || EQU_FT_socket5_check_flag == false || EQU_FT_socket6_check_flag == false || EQU_FT_socket7_check_flag == false || EQU_FT_socket8_check_flag == false){

								if(EQU_FT_KIT_check_flag == false)		
									EQU_error_list += kit_No_panel.getText() + ",";															

								if(EQU_FT_LB_check_flag == false)
									EQU_error_list += tx0b.getText() + ",";

								if(EQU_FT_socket1_check_flag == false)
									EQU_error_list += socket_No_panel.getText() + ",";																	

								if(EQU_FT_socket2_check_flag == false)
									EQU_error_list += socket_No_pane2.getText() + ",";																	

								if(EQU_FT_socket3_check_flag == false)
									EQU_error_list += socket_No_pane3.getText() + ",";

								if(EQU_FT_socket4_check_flag == false)
									EQU_error_list += socket_No_pane4.getText() + ",";

								if(EQU_FT_socket5_check_flag == false)
									EQU_error_list += socket_No_pane5.getText() + ",";

								if(EQU_FT_socket6_check_flag == false)
									EQU_error_list += socket_No_pane6.getText() + ",";

								if(EQU_FT_socket7_check_flag == false)
									EQU_error_list += socket_No_pane7.getText() + ",";

								if(EQU_FT_socket8_check_flag == false)
									EQU_error_list += socket_No_pane8.getText() + ",";																	

								EQU_FT_flag = false;
							}

							if(socket_No_panel.getText().equalsIgnoreCase("") && socket_No_pane2.getText().equalsIgnoreCase("") && socket_No_pane3.getText().equalsIgnoreCase("") && socket_No_pane4.getText().equalsIgnoreCase("") && socket_No_pane5.getText().equalsIgnoreCase("") && socket_No_pane6.getText().equalsIgnoreCase("") && socket_No_pane7.getText().equalsIgnoreCase("") && socket_No_pane8.getText().equalsIgnoreCase(""))
								EQU_FT_flag = false;
						}
						else{
							System.out.println("Error directory");
						}
						if (EQU_FT_flag == false){
							EQU_check_error_message  = "EQU probe card do not match the Kit,LB or sockets server data!\n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							EQU_check_error_message += "+Kit,LB or sockets << " + EQU_error_list + " >> do not fixture loan from system at " + barcode_testeridStr + "\n";   
							EQU_check_error_message += "| Please call the EQU check Kit,LB or sockets number | \n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							JOptionPane.showMessageDialog(null, EQU_check_error_message);
							autoSaveMessageBeforeExit();
							saveErrorMessageRealtime("Msg:EQU check probe card do not match");
							bt2_2.setEnabled(false);  //Add by Cola. 20160215
							bt2_1.setEnabled(false);  //Add by Cola. 20160215
							//					                killproc(); System.exit(1); // to quit Java app for Linux																							
						}							
					}
				}							

				if (EQU_CP_check_ProbeCard_flag == true && testTypeStr.equalsIgnoreCase("Wafer")){

					//20131219 check the LB		

//					EQUcmd = "rm -f /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr;
//					javaExecSystemCmd2(EQUcmd,1000);
//					System.out.println(EQUcmd);
//					saveMessageRealtime(EQUcmd);

					EQUcmd = "ftp_sigurd_check_EQU_probeCard.csh " + barcode_testeridStr+ " /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/";
					javaExecSystemCmd2(EQUcmd,7000);
					System.out.println(EQUcmd);
					saveMessageRealtime(EQUcmd);	

					File EQU_check_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr);
					if (EQU_check_dir.isDirectory()){
						probeCard_array = EQU_check_dir.list();
						for (int i = 0; i < probeCard_array.length; i++){
							probeCard_string = probeCard_array[i];
							probeCard_array_string = probeCard_string.split(".txt");
							if(probeCard_array_string[0].equals(tx0b.getText()))
								EQU_check_probeCard = true;
						}
					}
					else{
						System.out.println("Error directory");
					}
					if (EQU_check_probeCard == false){
						EQU_check_error_message  = "EQU probe card do not match the LB server data!\n";
						EQU_check_error_message += "+------------------------------------------------------------------+ \n";
						//			                EQU_check_error_message += "+the System probe card:" + probeCard_array_string[0] + "\n";
						EQU_check_error_message += "+Probe card << " + tx0b.getText() + " >> do not fixture loan from system at " + barcode_testeridStr + "\n";   
						EQU_check_error_message += "| Please call the EQU check probeCard number | \n";
						EQU_check_error_message += "+------------------------------------------------------------------+ \n";
						JOptionPane.showMessageDialog(null, EQU_check_error_message);
						autoSaveMessageBeforeExit();
						saveErrorMessageRealtime("Msg:EQU check probe card do not match");
						bt2_2.setEnabled(false);  //Add by Cola. 20160215
						bt2_1.setEnabled(false);  //Add by Cola. 20160215
						//			                killproc(); System.exit(1); // to quit Java app for Linux																							
					}

					if(Load_ProbeCard_RCP){
						if (!barcode_lbidStr.equals(tx0b.getText()))
						{
							EQU_check_error_message  = "EQU probe card do not match the check in probe card data!\n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							EQU_check_error_message += "+Probe card << " + tx0b.getText() + " >> do not match check in data " + barcode_lbidStr + "\n"; EQU_check_error_message += "| Please call the EQU check probeCard number and re-cehckIn RunCard | \n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							JOptionPane.showMessageDialog(null, EQU_check_error_message);
							autoSaveMessageBeforeExit();
							saveErrorMessageRealtime("Msg:EQU check probe card do not match");
							killproc(); System.exit(1); // to quit Java app for Linux											
						}
					}								

				}

				if (((EQU_FT_flag == true || EQU_FT_check_ProbeCard_flag == false) && testTypeStr.equalsIgnoreCase("Final"))
						|| ((EQU_check_probeCard == true || EQU_CP_check_ProbeCard_flag == false) && testTypeStr.equalsIgnoreCase("Wafer"))){

					if (FT_EQUendlot == 1)
						bt2_1.setEnabled(true);	//after end lot enable bt2_1

					tmpStr = "Please use \"Start test\" to Start test !\n\n";
					System.out.println(tmpStr); 
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);
					JOptionPane.showMessageDialog(null, tmpStr);		
					bt2_2.setEnabled(true);
					bt_EQU.setEnabled(false);
					kit_No_panel.setEditable(false);
					tx0b.setEditable(false);
					socket_No_panel.setEditable(false);						
					socket_No_pane2.setEditable(false);
					socket_No_pane3.setEditable(false);
					socket_No_pane4.setEditable(false);
					socket_No_pane5.setEditable(false);
					socket_No_pane6.setEditable(false);
					socket_No_pane7.setEditable(false);
					socket_No_pane8.setEditable(false);		
					socket_No_pane8.setEditable(false);	
					
//					if(barcode_customerStr.equals("F186"))
						bt_GPIB.setEnabled(true);	//20170323
					//Add clean /tmp/pulse-*** floder command by Cola. 20170505  No effect, add to autoload_Sigurd_CUS.csh
//					cmd = "/bin/find /tmp -type d -name pulse\\* -mtime +1 -print0 | xargs -0 rm -rf";
//					javaExecSystemCmd(cmd);
				}				
			}


		} else if (e.getSource()==bt2_4) {    //--hh, button  "Exit test"
			try{
				EQU_FT_Barcode_AutoKeyin_flag = true; //Add by Cola. 20160215

				btCnt++;

				bt2_4Flag=true;
				tmpStr = "\n___" + btCnt + "___ End test ...\n";
				System.out.println(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				//For 
				if(testTypeStr.equalsIgnoreCase("Final")){
					//For FT. Stop OICu 20170619-----Start
					cmd = "cex -t " + hostnameStr + " -c set_end_of_lot" + "\n";
					tmpStr = cmd + "\n";
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);
					javaExecSystemCmd2(cmd, 5000);	//20170925
					//For FT. Stop OICu 20170619-----End

//					cmd = "rm -f /tmp/tmp.sum";   //20090225
//					tmpStr = cmd + "\n";          //20090225
//					System.out.println(tmpStr);   //20090225
//					textArea2.append(tmpStr);     //20090225
//					saveMessageRealtime(tmpStr);
//					javaExecSystemCmd(cmd);       //20090225 

					//		    		String bin_path = csicAutoPath + "bin/";		//20150602 by ChiaHui .disable power after press end lot button
					//		    		cmd  = "xterm -rightbar -bg green -fg black -geometry 158x20  -e ";
					//		    		cmd += bin_path+"PowerDown.csh "+bin_path+"\n"; 
					//		    		javaExecSystemCmd2(cmd,5000);            
					/*
         if(testTypeStr.equalsIgnoreCase("Wafer")){
		         bt2_1.setEnabled(true);
		         bt2_2.setEnabled(true);
		     } 
					 */
					//            runautoStop();    //--hh


					//--hh

					//20170619-----Start
					bt2_4Flag=false;


					//20081110
					sumRepFlag = false; //repeat-->true,no repeat -->false,deault -->false 
					sumRepNum = 1;
					if(sumRepNum<10){
						tx4_1.setText("0" + Integer.toString(sumRepNum));
						sumRepNumStr = "0" + Integer.toString(sumRepNum);
					}
					else {
						tx4_1.setText(Integer.toString(sumRepNum));
						sumRepNumStr = Integer.toString(sumRepNum);
					}
					tx4_1.setBackground(new Color(251,249,209));
					//process std sum file //with std and sum flag//20100107file size and file existence
					if(testTypeStr.equalsIgnoreCase("Final")){ //only for FT 20101026
						File stdFile = new File(STDfileStr);
						File sumFile = new File(summaryfileStr);	//20170619
						long stdFileSize;
						long sumFileSize;

						stdFileSize = stdFile.length();
						sumFileSize = sumFile.length();
						tmpStr = "stdFileSize :" + stdFileSize +"\n";
						System.out.println(tmpStr); 
						textArea2.append(tmpStr);
						saveMessageRealtime(tmpStr);
						tmpStr = "sumFileSize :" + sumFileSize +"\n\n";
						System.out.println(tmpStr); 
						textArea2.append(tmpStr);
						saveMessageRealtime(tmpStr);

						if(sumFileSize < 100){
							sumSizeFlag = false;

							//       cmd = "rm -f "+ summaryfileStr_tmp;   
							//       tmpStr = cmd + "\n";          
							//       System.out.println(tmpStr);   
							//       textArea2.append(tmpStr);     
							//       javaExecSystemCmd(cmd);     

						}
						else {
							sumSizeFlag = true;
						}                
						try{              
							Thread.sleep(5000);

						} catch (java.lang.InterruptedException Ierr) {

						}                
						if(stdFile.exists()) {
							stdExistFlag = true;
						}
						else {
							stdExistFlag = false;
						}

						try{              
							Thread.sleep(5000);

						} catch (java.lang.InterruptedException Ierr) {

						}

						if(stdExistFlag == true) {
							////                                      
							cmd  = "gzip " + STDfileStr;
							tmpStr = cmd + "\n";
							System.out.println(tmpStr);   
							textArea2.append(tmpStr);     
							saveMessageRealtime(tmpStr);
							javaExecSystemCmd(cmd);       
						}

						////to copy the summary to another path  //Alex //20090609  //20100107 //20100128
						//					if(SigurdFlag){
						//						autoSendPath = "/usr/local/home/prod/autoResult/summary/autosend/" //20100107 Sigurd only
						//					} else{
						//						autoSendPath = "/mnt/automount/server_dmd/jackie_dmd/autoResult/autosend/"; //Sigurd only
						//					}
						//					autoSendPath += barcode_customerStr + "/";
						//					File dirAS = new File(autoSendPath); 
						//					if ( !dirAS.exists() )               
						//					{                                    
						//						dirAS.mkdirs();                
						//					}

						if(SigurdFlag){
							unCompressPath = "/dx_summary/FT/unCompress/"; //20170619
						} else{
							unCompressPath = "/mnt/automount/server_dmd/jackie_dmd/autoResult/unCompress/";//Sigurd only 
						}
						File dirUnCompress = new File(unCompressPath); 
						if ( !dirUnCompress.exists() )               
						{                                    
							dirUnCompress.mkdirs();                
						}


						System.out.println("sumSizeFlag  :" + sumSizeFlag);
						textArea2.append("sumSizeFlag  :" + sumSizeFlag + "\n");
						System.out.println("stdExistFlag :" + stdExistFlag);
						textArea2.append("stdExistFlag :" + stdExistFlag + "\n\n");

						tmpStr = cmd + "\n";
						System.out.println(tmpStr); 
						textArea2.append(tmpStr);
						saveMessageRealtime(tmpStr);

						if(RTStr.equals("HW")){
							cmd = "/bin/rm "+ summaryfileStr;	//20170619
							tmpStr = cmd + "\n";
							System.out.println(tmpStr); 
							textArea2.append(tmpStr);
							saveMessageRealtime(tmpStr);
							javaExecSystemCmd(cmd);

						}
						//20170619			//New solution of F186 Sum insert STDF Time for IT to check STDF & InfoFile by Cola. 20160919-----Start
//						if(barcode_customerStr.equals("F186")){
//							F186_NoTimeStr_SumName = summaryfileStr;
//							summaryfileStr = summaryfileStr.substring(0, summaryfileStr.length()-4) + "_" + STDfileStrTime + ".sum";
//							cmd = "/bin/cp "+ F186_NoTimeStr_SumName + " "+ summaryfileStr ;     
//							tmpStr = cmd + "\n";
//							System.out.println(tmpStr); 
//							textArea2.append(tmpStr);
//							saveMessageRealtime(tmpStr);
//							javaExecSystemCmd2(cmd,3000);                                   
//						}//New solution of F186 Sum insert STDF Time for IT to check STDF & InfoFile by Cola. 20160919-----End
						
						cmd = "/bin/cp "+ summaryfileStr + " "+ summaryfileStr_MES;     
						tmpStr = cmd + "\n";
						System.out.println(tmpStr); 
						textArea2.append(tmpStr);
						saveMessageRealtime(tmpStr);
						javaExecSystemCmd2(cmd,3000);    
						
						String summaryfilePath_ftp = summaryfileStr_MES.substring(0,summaryfileStr_MES.lastIndexOf("/")+1);
						String summaryfileStr_ftp = summaryfileStr_MES.substring(summaryfileStr_MES.lastIndexOf("/")+1,summaryfileStr_MES.length());
						if(sumSizeFlag==true&&stdExistFlag==true&&!RTStr.equals("HW")) { //20100128  //20100308

							cmd = "ftp_sigurd_DX.csh "+ summaryfilePath_ftp + " "+ summaryfileStr_ftp + " "+barcode_customerStr ;     
							tmpStr = cmd + "\n";
							System.out.println(tmpStr); 
							textArea2.append(tmpStr);
							saveMessageRealtime(tmpStr);
							javaExecSystemCmd2(cmd,1000);

							cmd = "/bin/cp "+ summaryfileStr + " "+ userSummaryfinalPath_bak ;
//							if(barcode_customerStr.equals("F186")) //Add by Cola. 20160919
//								cmd = "/bin/cp "+ F186_NoTimeStr_SumName + " "+ userSummaryfinalPath_bak;
							tmpStr = cmd + "\n";
							System.out.println(tmpStr); 
							textArea2.append(tmpStr);
							saveMessageRealtime(tmpStr);
							javaExecSystemCmd(cmd);
							//Backup time naming summary. 20170925-----Start
							cmd = "/bin/cp "+ summaryfileStr_MES + " "+ userSummaryfinalPath_bak ;
							tmpStr = cmd + "\n";
							System.out.println(tmpStr); 
							textArea2.append(tmpStr);
							saveMessageRealtime(tmpStr);
							javaExecSystemCmd(cmd);
							//Backup time naming summary. 20170925-----End
							
							if (RTStr.equals("CORR")) {
								if(barcode_customerStr.equals("L227")) {	//20171101
									cmd = "/bin/cp "+ STDfileStr + ".gz "+ userSTDFserverPath ; //20170619
									tmpStr = cmd + "\n";                                      
									System.out.println(tmpStr);                               
									textArea2.append(tmpStr);                                 
									saveMessageRealtime(tmpStr);
									javaExecSystemCmd2(cmd,3000);
									javaExecSystemCmd2(cmd,3000);
								}
								else{
								cmd = "/bin/rm "+ STDfileStr + ".gz ";                
								tmpStr = cmd + "\n";                                  
								System.out.println(tmpStr);                           
								textArea2.append(tmpStr);                             
								saveMessageRealtime(tmpStr);
								javaExecSystemCmd(cmd);  
								}
								//							cmd = "/bin/cp "+ summaryfileStr + " "+ autoSendPath ;     
								//							tmpStr = cmd + "\n";
								//							System.out.println(tmpStr); 
								//							textArea2.append(tmpStr);
								//							saveMessageRealtime(tmpStr);
								//							javaExecSystemCmd2(cmd,3000);                                   
								//							javaExecSystemCmd2(cmd,3000);

							} else if (RTStr.equals("EQC")) {//20100325
								if(barcode_customerStr.equals("L227") || barcode_customerStr.equals("F128") || barcode_customerStr.equals("F192")) {//20110712
									cmd = "/bin/cp "+ STDfileStr + ".gz "+ userSTDFserverPath ; //20170619
									tmpStr = cmd + "\n";                                      
									System.out.println(tmpStr);                               
									textArea2.append(tmpStr);                                 
									saveMessageRealtime(tmpStr);
									javaExecSystemCmd2(cmd,3000);
									javaExecSystemCmd2(cmd,3000);
								}
								else{
									cmd = "/bin/rm "+ STDfileStr + ".gz ";                
									tmpStr = cmd + "\n";                                  
									System.out.println(tmpStr);                           
									textArea2.append(tmpStr);                             
									saveMessageRealtime(tmpStr);
									javaExecSystemCmd(cmd);
								}

								//							cmd = "/bin/cp "+ summaryfileStr + " "+ autoSendPath ;     
								//							tmpStr = cmd + "\n";
								//							System.out.println(tmpStr); 
								//							textArea2.append(tmpStr);
								//							saveMessageRealtime(tmpStr);
								//							javaExecSystemCmd2(cmd,3000);
								//							javaExecSystemCmd2(cmd,3000);

							} else {

								//							cmd = "/bin/cp "+ summaryfileStr + " "+ autoSendPath ;     
								//							tmpStr = cmd + "\n";
								//							System.out.println(tmpStr); 
								//							textArea2.append(tmpStr);
								//							saveMessageRealtime(tmpStr);
								//							javaExecSystemCmd2(cmd,3000);
								//							javaExecSystemCmd2(cmd,3000);

								cmd = "/bin/cp "+ STDfileStr + ".gz "+ userSTDFserverPath ;     
								tmpStr = cmd + "\n";                                      
								System.out.println(tmpStr);                               
								textArea2.append(tmpStr);                                 
								saveMessageRealtime(tmpStr);
								javaExecSystemCmd2(cmd,3000);                                   
								javaExecSystemCmd2(cmd,3000);                                   
							}
						}

						if(sumSizeFlag==true&&stdExistFlag==false&&!RTStr.equals("HW")) {

							cmd = "ftp_sigurd_DX.csh "+ summaryfilePath_ftp + " "+ summaryfileStr_ftp + " "+barcode_customerStr ;     
							tmpStr = cmd + "\n";
							System.out.println(tmpStr); 
							textArea2.append(tmpStr);
							saveMessageRealtime(tmpStr);
							javaExecSystemCmd2(cmd,1000);

							cmd = "/bin/cp "+ summaryfileStr + " "+ userSummaryfinalPath_bak ;     
							tmpStr = cmd + "\n";
							System.out.println(tmpStr); 
							textArea2.append(tmpStr);
							saveMessageRealtime(tmpStr);
							javaExecSystemCmd(cmd);

							//						cmd = "/bin/cp "+ summaryfileStr + " "+ autoSendPath ;     
							//						tmpStr = cmd + "\n";
							//						System.out.println(tmpStr); 
							//						textArea2.append(tmpStr);
							//						saveMessageRealtime(tmpStr);
							//						javaExecSystemCmd2(cmd,3000);
							//						javaExecSystemCmd2(cmd,3000);
						}

						if(sumSizeFlag==false&&stdExistFlag==true&&!RTStr.equals("HW")) { //move .std to uncompress
							if (!barcode_customerStr.equals("L227") && RTStr.equals("CORR")) {	//20171101
							}
							else {

								cmd = "/bin/cp "+ STDfileStr + ".gz "+ unCompressPath ;       
								tmpStr = cmd + "\n";                                          
								System.out.println(tmpStr);                                   
								textArea2.append(tmpStr);                                     
								saveMessageRealtime(tmpStr);
								javaExecSystemCmd(cmd); 
							}
						}

					}

					//Process TXT DataLog file. 20170619 for FT
					File txtFile = new File(TXTfileStr);	//20170824
					if(txtFile.exists()){
						cmd  = "gzip " + TXTfileStr;
						tmpStr = cmd + "\n";
						System.out.println(tmpStr);   
						textArea2.append(tmpStr);     
						saveMessageRealtime(tmpStr);
						javaExecSystemCmd(cmd);  
						
						cmd = "/bin/cp "+ TXTfileStr + ".gz "+ userTXTserverPath ;       
						tmpStr = cmd + "\n";                                          
						System.out.println(tmpStr);                                   
						textArea2.append(tmpStr);                                     
						saveMessageRealtime(tmpStr);
						javaExecSystemCmd(cmd); 
					}
				}
				// for MTK
//				if(testTypeStr.equalsIgnoreCase("Final")){
//					if(EfuseFlag&&RTStr.equals("A1")){
//						if(EFuseErrorFlag) {//error
//							CallFailEfuseFrame();
//						}
//						else {
//							CallPassEfuseFrame();
//						}
//					}
//				}
				// for MTK end

				for (int i=0; i<rtrbtname.length; i++) { //20100507
					if(i==0)
						rtrbt[i].setSelected(true);    
					else 
						rtrbt[i].setSelected(false);    
					RTStr="A1";
				}

//				if(AutoEndLot_Timeout == false)   //Add by Cola. 2015/06/18
//					JOptionPane.showMessageDialog(null, "End of file processing...");//20100308
//				else  //Add by Cola. 20160107
//					JOptionPane.showMessageDialog(null, "Time Out!! This is HW or CORR Station, Auto End Lot Done.. Please Check RT Station !!");

				int exitflag = 0;

				if((testTypeStr.equalsIgnoreCase("Final") && EQU_FT_Barcode_AutoKeyin_flag) || (testTypeStr.equalsIgnoreCase("Wafer") && EQU_CP_Barcode_AutoKeyin_flag)){ //Add by Cola. 20151209
					// yes to return 0, no to return 1  	
					exitflag = JOptionPane.showConfirmDialog(null,"Please recheck KIT,socket and LB / ProbeCard barcode", "Confirmation ? ", JOptionPane.YES_NO_OPTION);
				}else{
					JOptionPane.showMessageDialog(null,"Please key in KIT,socket and LB/probeCard barcode !!");
				}
				if(exitflag == 0 && testTypeStr.equalsIgnoreCase("Final")){

					bt_EQU.setEnabled(true);
					kit_No_panel.setEditable(true);
					tx0b.setEditable(true);
					socket_No_panel.setEditable(true);						
					socket_No_pane2.setEditable(true);
					socket_No_pane3.setEditable(true);
					socket_No_pane4.setEditable(true);
					socket_No_pane5.setEditable(true);
					socket_No_pane6.setEditable(true);
					socket_No_pane7.setEditable(true);
					socket_No_pane8.setEditable(true);
					if(!EQU_FT_Barcode_AutoKeyin_flag){
						kit_No_panel.setText("");
						tx0b.setText("");
						socket_No_panel.setText("");					
						socket_No_pane2.setText("");
						socket_No_pane3.setText("");
						socket_No_pane4.setText("");
						socket_No_pane5.setText("");
						socket_No_pane6.setText("");
						socket_No_pane7.setText("");
						socket_No_pane8.setText("");
					}
				}
				if(exitflag == 0 && testTypeStr.equalsIgnoreCase("Wafer")){

					bt_EQU.setEnabled(true);
					tx0b.setEditable(true);  
					if(!EQU_CP_Barcode_AutoKeyin_flag)
						tx0b.setText("");
				}	
				boolean EQU_FT_flag = true;
				boolean EQU_check_probeCard = false;
				if(exitflag == 1){

				if(EQU_FT_check_ProbeCard_flag == true && testTypeStr.equalsIgnoreCase("Final")){

						boolean EQU_FT_KIT_check_flag = true;        
						boolean EQU_FT_LB_check_flag = true;         
						boolean EQU_FT_socket1_check_flag = true;    
						boolean EQU_FT_socket2_check_flag = true;    
						boolean EQU_FT_socket3_check_flag = true;    
						boolean EQU_FT_socket4_check_flag = true;    
						boolean EQU_FT_socket5_check_flag = true;    
						boolean EQU_FT_socket6_check_flag = true;    
						boolean EQU_FT_socket7_check_flag = true;    
						boolean EQU_FT_socket8_check_flag = true;                 

						File EQU_FT_KIT_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + kit_No_panel.getText() + ".txt");
						File EQU_FT_LB_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + tx0b.getText() + ".txt");
						File EQU_FT_socket1_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_panel.getText() + ".txt");
						File EQU_FT_socket2_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane2.getText() + ".txt");				
						File EQU_FT_socket3_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane3.getText() + ".txt");
						File EQU_FT_socket4_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane4.getText() + ".txt");
						File EQU_FT_socket5_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane5.getText() + ".txt");
						File EQU_FT_socket6_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane6.getText() + ".txt");
						File EQU_FT_socket7_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane7.getText() + ".txt");
						File EQU_FT_socket8_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr + "/" + socket_No_pane8.getText() + ".txt");

						String EQUcmd;
						String EQU_check_error_message;
						String EQU_error_list = "";

						//20140304 check the Kit,LB,sockets		

						//					EQUcmd = "rm -f /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr;
						//					javaExecSystemCmd2(EQUcmd,1000);
						//					System.out.println(EQUcmd);
						//					saveMessageRealtime(EQUcmd);

						EQUcmd = "ftp_sigurd_check_EQU_probeCard.csh " + barcode_testeridStr+ " /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/";
						javaExecSystemCmd2(EQUcmd,7000);
						System.out.println(EQUcmd);
						saveMessageRealtime(EQUcmd);	

						File EQU_check_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr);
						if (EQU_check_dir.isDirectory()){

							if(!EQU_FT_KIT_dir.isFile() || !kit_No_panel.getText().substring(0, 2).equals("CK")){
								EQU_FT_KIT_check_flag = false;
							}

							if (!EQU_FT_LB_dir.isFile() || (!tx0b.getText().substring(0, 2).equals("LB") && !tx0b.getText().substring(0, 2).equals("MB"))){
								EQU_FT_LB_check_flag = false;
							}

							if (!socket_No_panel.getText().equalsIgnoreCase("") && (!EQU_FT_socket1_dir.isFile() || !socket_No_panel.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket1_check_flag = false;
							}

							if (!socket_No_pane2.getText().equalsIgnoreCase("") && (!EQU_FT_socket2_dir.isFile() || !socket_No_pane2.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket2_check_flag = false;
							}

							if (!socket_No_pane3.getText().equalsIgnoreCase("") && (!EQU_FT_socket3_dir.isFile() || !socket_No_pane3.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket3_check_flag = false;
							}

							if (!socket_No_pane4.getText().equalsIgnoreCase("") && (!EQU_FT_socket4_dir.isFile() || !socket_No_pane4.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket4_check_flag = false;
							}

							if (!socket_No_pane5.getText().equalsIgnoreCase("") && (!EQU_FT_socket5_dir.isFile() || !socket_No_pane5.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket5_check_flag = false;
							}

							if (!socket_No_pane6.getText().equalsIgnoreCase("") && (!EQU_FT_socket6_dir.isFile() || !socket_No_pane6.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket6_check_flag = false;
							}

							if (!socket_No_pane7.getText().equalsIgnoreCase("") && (!EQU_FT_socket7_dir.isFile() || !socket_No_pane7.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket7_check_flag = false;
							}

							if (!socket_No_pane8.getText().equalsIgnoreCase("") && (!EQU_FT_socket8_dir.isFile() || !socket_No_pane8.getText().substring(0, 2).equals("SK"))){
								EQU_FT_socket8_check_flag = false;
							}

							if (EQU_FT_KIT_check_flag == false || EQU_FT_LB_check_flag == false || EQU_FT_socket1_check_flag == false || EQU_FT_socket2_check_flag == false || EQU_FT_socket3_check_flag == false || EQU_FT_socket4_check_flag == false || EQU_FT_socket5_check_flag == false || EQU_FT_socket6_check_flag == false || EQU_FT_socket7_check_flag == false || EQU_FT_socket8_check_flag == false){

								if(EQU_FT_KIT_check_flag == false)		
									EQU_error_list += kit_No_panel.getText() + ",";															

								if(EQU_FT_LB_check_flag == false)
									EQU_error_list += tx0b.getText() + ",";

								if(EQU_FT_socket1_check_flag == false)
									EQU_error_list += socket_No_panel.getText() + ",";																	

								if(EQU_FT_socket2_check_flag == false)
									EQU_error_list += socket_No_pane2.getText() + ",";																	

								if(EQU_FT_socket3_check_flag == false)
									EQU_error_list += socket_No_pane3.getText() + ",";

								if(EQU_FT_socket4_check_flag == false)
									EQU_error_list += socket_No_pane4.getText() + ",";

								if(EQU_FT_socket5_check_flag == false)
									EQU_error_list += socket_No_pane5.getText() + ",";

								if(EQU_FT_socket6_check_flag == false)
									EQU_error_list += socket_No_pane6.getText() + ",";

								if(EQU_FT_socket7_check_flag == false)
									EQU_error_list += socket_No_pane7.getText() + ",";

								if(EQU_FT_socket8_check_flag == false)
									EQU_error_list += socket_No_pane8.getText() + ",";																	

								EQU_FT_flag = false;
							}

							if(socket_No_panel.getText().equalsIgnoreCase("") && socket_No_pane2.getText().equalsIgnoreCase("") && socket_No_pane3.getText().equalsIgnoreCase("") && socket_No_pane4.getText().equalsIgnoreCase("") && socket_No_pane5.getText().equalsIgnoreCase("") && socket_No_pane6.getText().equalsIgnoreCase("") && socket_No_pane7.getText().equalsIgnoreCase("") && socket_No_pane8.getText().equalsIgnoreCase(""))
								EQU_FT_flag = false;
						}
						else{
							System.out.println("Error directory");
						}
						if (EQU_FT_flag == false){
							EQU_check_error_message  = "EQU probe card do not match the Kit,LB or sockets server data!\n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							EQU_check_error_message += "+Kit,LB or sockets << " + EQU_error_list + " >> do not fixture loan from system at " + barcode_testeridStr + "\n";   
							EQU_check_error_message += "| Please call the EQU check Kit,LB or sockets number | \n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							JOptionPane.showMessageDialog(null, EQU_check_error_message);
							autoSaveMessageBeforeExit();
							saveErrorMessageRealtime("Msg:EQU check probe card do not match");
							//						killproc(); System.exit(1); // to quit Java app for Linux																							
						}							
					}		     	

				if(EQU_CP_check_ProbeCard_flag == true && testTypeStr.equalsIgnoreCase("Wafer")){

						String EQUcmd;
						String[] probeCard_array;
						String EQU_check_error_message;


						//20131219 check the LB		

						//					EQUcmd = "rm -f /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr;
						//					javaExecSystemCmd2(EQUcmd,1000);
						//					System.out.println(EQUcmd);
						//					saveMessageRealtime(EQUcmd);

						EQUcmd = "ftp_sigurd_check_EQU_probeCard.csh " + barcode_testeridStr+ " /dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/";
						javaExecSystemCmd2(EQUcmd,7000);
						System.out.println(EQUcmd);
						saveMessageRealtime(EQUcmd);	

						File EQU_check_dir = new File("/dx_profile/prod/csicDXAuto_Sigurd_CUS/bin/EQU_check/" + barcode_testeridStr);
						if (EQU_check_dir.isDirectory()){
							probeCard_array = EQU_check_dir.list();
							for (int i = 0; i < probeCard_array.length; i++){
								probeCard_string = probeCard_array[i];
								probeCard_array_string = probeCard_string.split(".txt");
								if(probeCard_array_string[0].equals(tx0b.getText()))
									EQU_check_probeCard = true;
							}
						}
						else{
							System.out.println("Error directory");
						}
						if (EQU_check_probeCard == false){
							EQU_check_error_message  = "EQU probe card do not match the LB server data!\n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							//			                EQU_check_error_message += "+the System probe card:" + probeCard_array_string[0] + "\n";
							EQU_check_error_message += "+Probe card << " + tx0b.getText() + " >> do not fixture loan from system at " + barcode_testeridStr + "\n";   
							EQU_check_error_message += "| Please call the EQU check probeCard number | \n";
							EQU_check_error_message += "+------------------------------------------------------------------+ \n";
							JOptionPane.showMessageDialog(null, EQU_check_error_message);
							autoSaveMessageBeforeExit();
							saveErrorMessageRealtime("Msg:EQU check probe card do not match");
							//						killproc(); System.exit(1); // to quit Java app for Linux																							
						}

						if(Load_ProbeCard_RCP){
							if (!barcode_lbidStr.equals(tx0b.getText()))
							{
								EQU_check_error_message  = "EQU probe card do not match the check in probe card data!\n";
								EQU_check_error_message += "+------------------------------------------------------------------+ \n";
								EQU_check_error_message += "+Probe card << " + tx0b.getText() + " >> do not match check in data " + barcode_lbidStr + "\n";   
								EQU_check_error_message += "| Please call the EQU check probeCard number and re-cehckIn RunCard | \n";
								EQU_check_error_message += "+------------------------------------------------------------------+ \n";
								JOptionPane.showMessageDialog(null, EQU_check_error_message);
								autoSaveMessageBeforeExit();
								saveErrorMessageRealtime("Msg:EQU check probe card do not match");
								killproc(); System.exit(1); // to quit Java app for Linux											
							}
						}																		
					} 		
					if((testTypeStr.equalsIgnoreCase("Final") && EQU_FT_flag) || (testTypeStr.equalsIgnoreCase("Wafer") && EQU_check_probeCard)){ //Add by Cola. 20151209
						bt2_1.setEnabled(true);
						bt2_2.setEnabled(true);	
						bt_EQU.setEnabled(false);  //Add by Cola. 20160510
					}
					else{  //When End test EQU_check is not correct. Add by Cola. 20160412
						tx0b.setEditable(true);
						bt_EQU.setEnabled(true);
						if(testTypeStr.equalsIgnoreCase("Final")){
							kit_No_panel.setEditable(true);
							socket_No_panel.setEditable(true);						
							socket_No_pane2.setEditable(true);
							socket_No_pane3.setEditable(true);
							socket_No_pane4.setEditable(true);
							socket_No_pane5.setEditable(true);
							socket_No_pane6.setEditable(true);
							socket_No_pane7.setEditable(true);
							socket_No_pane8.setEditable(true);
							ProcessEQUsocketFile(false);  //Add by Cola. 20160510
						}
					}
				}
				FT_EQUendlot = 1; 
				bt2_4.setEnabled(false);       
				for(int i=0; i<rtrbtname.length; i++){
					rtrbt[i].setEnabled(true);	      //When "End Test" Click, Enable RT Station choice.  by Cola 2015/03/12
				}
				
				if (testTypeStr.equalsIgnoreCase("Final") && barcode_customerStr.equals("L227"))
					javaExecSystemCmd("rm -rf /home/swcpd/STPWIN/");	//20170710
				
			}catch(Exception err){
				JOptionPane.showMessageDialog(null, "End Lot Function Error Message: " + err);
			}

		} else if (e.getSource()==btd) {//20091229
			tmpStr = "\n___" + btCnt + "___ Show HBSum Infomation ...\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			dataFlag ++;
			if(dataFlag == 1) {
				CallDataFrame();
			} else {
				dataframe.setVisible(true);
				d1.repaint();
			}
		} else if (e.getSource()==bt3) {

			tmpStr = "\n___" + btCnt + "___ SaveMessage Process ...\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			tmpStr = textArea2.getText();
			saveMessage(tmpStr);

		} else if (e.getSource()==btOIC) {

			tmpStr = "\n___" + btCnt + "___ btOIC Process ...\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			// runOIC();  //-- hh , do not use

		} else if (e.getSource()==btExit) {  //--hh
			saveStartEndRealtime("Exit");
			tmpStr = "\n___" + btCnt + "___ check Exit Process ...\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			runExit();

		} else if (e.getSource()==btVer) {

			tmpStr = "\n___" + btCnt + "___ btVer OI Version Display ...\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);

			showVersion();

		} else if (e.getSource()==robot_frame_bt) {    //20081021
			robotframe.setVisible(false);
			robot_p3.removeAll();
			for(int i=0;i<robot_type_num;i++){
				robot_grpbt.remove(robot_bt[i]);
			}
			robot_p2.removeAll();
			robot_p1.removeAll();
			c1.validate();
			c1.repaint();
			mutiRobot_flag = false;        //20081230

		} else if (e.getSource()==RT_frame_bt) {//20100507
			btCnt--;
			if((RTStr.equals("RT") && rtBinnum>0) || testTypeStr.equalsIgnoreCase("Wafer")) {
				RT_frame.setVisible(false);
			} else {
				tmpStr  = "<Exception> Please Select ReTest Bin Number!!!!\n";
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
				JOptionPane.showMessageDialog(null, tmpStr);
			}
		} else if (e.getSource()==EQC_frame_bt) {
			btCnt--;
			if(!EQC_QNum_Str.startsWith("QRT")) {
				EQC_frame.setVisible(false);
			}
			else {
				if(QrtBinnum>0) {
					EQC_frame.setVisible(false);
				}else {
					tmpStr  = "<Exception> Please Select QRT Bin Number!!!!\n";
					textArea2.append(tmpStr);
					saveMessageRealtime(tmpStr);
					JOptionPane.showMessageDialog(null, tmpStr);
				}
			}

		} else if (e.getSource()==RT_frame_bt2) {//20161026
			btCnt--;
			if(!ANFRT_flag){
				CallANFRTFrame();
				Enable_XML_ANFRTbin();
			}else{ 
				ANFRT_frame.setVisible(true);
				Enable_XML_ANFRTbin();
			}
		} else if (e.getSource()==EQC_frame_bt2) {//20161026
			btCnt--;
			if(!ANFRT_flag){
				CallANFRTFrame();
				Enable_XML_ANFRTbin();
			}else{ 
				ANFRT_frame.setVisible(true);
				Enable_XML_ANFRTbin();
			}
		} else if (e.getSource()==ANFRT_frame_bt) {//20161026
			btCnt--;
			//Add Summary ANF Input RT name-----Start
			ANFrtBinSumStr = ""; ANFQrtBinSumStr ="";
			for(int i=0;i<rtBinname.length;i++)
				if(ANFrtBinbox[i].isSelected())
					if(rtrbt[2].isSelected()) //RT Station
						ANFrtBinSumStr += rtBinname[i];
					else if(rtrbt[3].isSelected()) //EQC Station
						ANFQrtBinSumStr += rtBinname[i]; 
			if(rtrbt[2].isSelected() && !ANFrtBinSumStr.equals("")){
				ANFrtBinSumStr = "B" + ANFrtBinSumStr + "-";
				tmpStr = "___ NOW ANF Retest Bin String for Sum = " + ANFrtBinSumStr + "\n";
//				System.out.print(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
			}
            if(rtrbt[3].isSelected() && !ANFQrtBinSumStr.equals("")){
            	ANFQrtBinSumStr = "B" + ANFQrtBinSumStr + "-";
            	tmpStr = "___ NOW ANF QRT Bin String for Sum = " + ANFQrtBinSumStr + "\n";
//				System.out.print(tmpStr); 
				textArea2.append(tmpStr);
				saveMessageRealtime(tmpStr);
			}
			//Add Summary ANF Input RT name-----End
			ANFRT_frame.setVisible(false);
			
		} else if (e.getSource()==CORR_frame_bt) {	//20170619
			btCnt--;
			CORR_frame.setVisible(false);
			if(CORRbin_num == 0) { //Add by Cola. 20160407-----Start
				CORRbin_num = 1;
				CallSelectCorrBinFrame();
				DisablePassFailBin();
			}
			else{
				DisablePassFailBin();
				CORRbin_frame.setVisible(true);
			}					//Add by Cola. 20160407-----End
		} else if (e.getSource()==CORRbin_frame_bt) {
			btCnt--;
			CORRbin_frame.setVisible(false);

		} else if (e.getSource()==data_frame_bt) {   //20091229
			textAread.setText("");
			dataframe.setVisible(false);


		} else if (e.getSource()==d_show) {   //20091229
			String dataTmpStr = "";
			String show_data_file_path = show_data_path + "HBinReport_" + barcode_testeridStr + ".txt";	    	    

			try {
				data_info = new BufferedReader(new FileReader(show_data_file_path));//open file

				while ((tmpStr = data_info.readLine()) != null) {
					//if (tmpStr.length()!=0) {
					dataTmpStr += tmpStr + "\n";
					//}
				}
				br.close(); // close file
				textAread.setText(dataTmpStr);

			} catch (FileNotFoundException event) {

				tmpStr  = "<Exception> getDataInfo: " + show_data_path + " is NOT Found !\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr  = "<Exception> getDataInfo: " + show_data_path + " is NOT Found !\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				//killproc(); System.exit(1); // to quit Java app for Linux

			} catch (java.io.IOException err) {
				tmpStr = "<Exception> getDataInfo: " + err + "\n";
				tmpStr += "+----------------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+----------------------------+ \n";
				System.out.println(tmpStr);

				tmpStr = "<Exception> getDataInfo: " + err + "\n";
				tmpStr += "+--------------------+ \n";
				tmpStr += "| Please call the Supervisor | \n";
				tmpStr += "+--------------------+ \n";
				JOptionPane.showMessageDialog(null, tmpStr);
				//killproc(); System.exit(1); // to quit Java app for Linux
			}

		} else if (e.getSource()==d_print) {   //20091229

			cmd = "lpr -l "+ show_data_path + "HBinReport_" + barcode_testeridStr + ".txt";   //Rex
			tmpStr = cmd + "\n";          
			javaExecSystemCmd(cmd);       


		} else {

			tmpStr = "\n___" + btCnt + "___ actionPerformed: <WARNING> Wrong Operation Fail (***)\n";
			System.out.println(tmpStr); 
			textArea2.append(tmpStr);
			saveMessageRealtime(tmpStr);
		}
	}

	public static long hexStrtoLong(String inStr) {

		String tmpStr = null;
		int i, size = 0;
		long value = 0L;
		size = inStr.length();

		if (size > 0) {
			value = Long.parseLong(inStr,16);
			//System.out.println("hexStrtoLong: inStr = " + inStr + " to value = " + value);
		}

		return value;
	}

	//public static String checkifHeadEndWithSlash(String inStr) {
	//
	//    String tmpStr = "";
	//
	//    if (inStr.endsWith("/")==false) {
	//        tmpStr = inStr + "/";
	//    } else {
	//        tmpStr = inStr;
	//    }
	//
	//    if (tmpStr.startsWith("./")==true) {
	//        tmpStr = tmpStr.substring(1);
	//    }
	//
	//    //System.out.println("InStr: " + inStr + "\tOutStr = " + tmpStr);
	//
	//    return tmpStr;
	//
	//}

	// tmp.1 content:
	// --------------------------------------
	// SITENAME =
	// csicAutoPath =
	// autoResultPath =                    
	// //// userSummaryPath =
	// messagePath =
	// testerBarcodePath =
	// recipefilePath =
	// User = 
	// homePath = 
	// HOSTNAME = 
	// HOSTID = 
	// DMDSWVersion = v1.5.2_BLD7

	public static void processSystemInfoContent(String inStr) {

		String paramStr[] = new String[2];
		boolean twostrFlag = true;

		//initial
		paramStr[0] = "";
		paramStr[1] = "";

		inStr = stringRemoveSpaceHeadTail(inStr);

		//System.out.println("case0: inStr = " + inStr);

		paramStr = inStr.split("=");

		if (inStr.endsWith("=")==false) {

			//System.out.print("processSystemInfoContent: paramStr[0] = " + paramStr[0]);
			//System.out.println("\tparamStr[1] = " + paramStr[1]);

			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
			paramStr[1] = stringRemoveSpaceHeadTail(paramStr[1]);
			//System.out.print("processSystemInfoContent: paramStr[0] = " + paramStr[0]);
			//System.out.println("\tparamStr[1] = " + paramStr[1] + " (***NEW***)");
			twostrFlag = true;

		} else {

			//System.out.println("processSystemInfoContent: paramStr[0] = " + paramStr[0]);
			paramStr[0] = stringRemoveSpaceHeadTail(paramStr[0]);
			//System.out.println("processSystemInfoContent: paramStr[0] = " + paramStr[0] + " (***NEW***)");
			twostrFlag = false;
		}

		// Variable           tmp.1's content
		// ---------------    --------------------------
		// SITENAME           SITENAME =                          
		// csicAutoPath       csicAutoPath =                      
		// autoResultPath     autoResultPath =                    
		// ////userSummaryPath    userSummaryPath =                   
		// messagePath        messagePath =                       
		// testerBarcodePath  testerBarcodePath =                       
		// recipefilePath     recipefilePath =                       
		// whoamiStr          User =                      
		// homeStr            homePath = 
		// hostnameStr        HOSTNAME = 
		// hostidStr          HOSTID = 
		// DMDSWVerStr        DMDSWVersion = v1.5.3_BLD11        
		//


		if (twostrFlag) {
			if (paramStr[0].equals("SITENAME")) {
				SITENAME = paramStr[1];
			} else if (paramStr[0].equals("csicAutoPath")) {
				csicAutoPath = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("autoResultPath")) {
				autoResultPath = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("messagePath")) {
				messagePath = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("testerBarcodePath")) {
				testerBarcodePath = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("recipefilePath")) {
				recipefilePath = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("User")) {
				whoamiStr = paramStr[1];
			} else if (paramStr[0].equals("homePath")) {
				homeStr = checkifHeadEndWithSlash(paramStr[1]);
			} else if (paramStr[0].equals("HOSTNAME")) {
				hostnameStr = paramStr[1];
			} else if (paramStr[0].equals("HOSTID")) {
				hostidStr = paramStr[1];
			} else if (paramStr[0].equals("DMDSWVersion")) {
				DMDSWVerStr = paramStr[1];
			}
		}

	}

	public static void getSystemInfo() {

		String tmpStr = "";
		boolean fileFlag = true;
		infStr = "tmp.1";

		try {
			br = new BufferedReader(new FileReader(infStr));//open file

			while ((tmpStr = br.readLine()) != null) {
				//System.out.println("br_read: " + tmpStr + ",  length = " + tmpStr.length());
				if (tmpStr.length()!=0) {
					processSystemInfoContent(tmpStr);
				}
			}

			br.close(); // close file
		} catch (FileNotFoundException event) {

			fileFlag = false;
			tmpStr = "<Exception> getSystemInfo: " + infStr + " is NOT Found !\n";
			JOptionPane.showMessageDialog(null, tmpStr);
			System.out.print(tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:get system info is fail");
			killproc(); System.exit(1); // to quit Java app for Linux

		} catch (java.io.IOException err) {
			fileFlag = false;
			tmpStr = "<Exception> getSystemInfo: " + err + "\n";
			JOptionPane.showMessageDialog(null, tmpStr);
			System.out.print(tmpStr);
			autoSaveMessageBeforeExit();
			saveErrorMessageRealtime("Msg:get system info is fail");
			killproc(); System.exit(1); // to quit Java app for Linux
		}


		System.out.println("===========================================================");
		System.out.println("Local System Information:");
		System.out.println("===========================================================");
		System.out.println("             User = " + whoamiStr);
		System.out.println("             HOME = " + homeStr);
		System.out.println("           Hostid = " + hostidStr);
		System.out.println("         Hostname = " + hostnameStr);
		System.out.println("===========================================================");
		System.out.println("         SITENAME = " + SITENAME);
		System.out.println("     csicAutoPath = " + csicAutoPath);
		System.out.println("   autoResultPath = " + autoResultPath);
		System.out.println("      messagePath = " + messagePath);
		System.out.println("testerBarcodePath = " + testerBarcodePath);
		System.out.println("   recipefilePath = " + recipefilePath);
		System.out.println("     DMDSWVersion = " + DMDSWVerStr);
		System.out.println("===========================================================");
		System.out.println("");

	}


	public static void getLicenseInfo() {

		String tmpStr = "";
		String tmpStr1 = "";
		String tmpStr2 = "";
		boolean fileFlag = true;
		int stackcnt = 0;
		int i=0;
		int idxk1=0;
		int idxk2=0;
		int idxk3=0;

		Stack stack = new Stack();
		//infStr = "./license/csicAuto_license.dat";
		infStr = csicAutoPath + "/license/csicAuto_license.dat";

		try {
			br = new BufferedReader(new FileReader(infStr));//open file

			// reading file in progress 
			while ((tmpStr1 = br.readLine()) != null) {
				//System.out.println("tmpStr1 length = " + tmpStr1.length());
				// tmpStr1.length()==0 means that no any characters except space characters
				if (tmpStr1.length()!=0) {

					//System.out.println("...1... getLicenseInfo:  tmpStr1 =..." + tmpStr1 + "...");

					if (tmpStr1.equals("END")) {
						//System.out.println("...E... getLicenseInfo:  tmpStr1 =..." + tmpStr1 + "...");
						System.out.println("License " + tmpStr1);
						break;
					}

					//else if (tmpStr1.compareTo("hostname")>=0) {
					else if (tmpStr1.matches("hostname\\s+=\\s+(.*)")) {
						//System.out.println("...2... getLicenseInfo:  tmpStr1 =..." + tmpStr1 + "...");

						for (i=0; i<tmpStr1.length(); i++) {

							if (tmpStr1.charAt(i)=='=') {
								lic_hostnameStr = tmpStr1.substring(i+2);// because extra-space after '='
								//System.out.println("lic_hostname =..." + lic_hostnameStr + "...");
								System.out.println("License hostname = " + lic_hostnameStr);
								break;
							}
						}
					}

					//else if (tmpStr1.compareTo("hostid")>=0) {
					else if (tmpStr1.matches("hostid\\s+=\\s+(.*)")) {
						//System.out.println("...3... getLicenseInfo:  tmpStr =..." + tmpStr1 + "...");

						for (i=0; i<tmpStr1.length(); i++) {

							if (tmpStr1.charAt(i)=='=') {
								lic_hostidStr = tmpStr1.substring(i+2);// because extra-space after '=' 
								//System.out.println("lic_hostid =..." + lic_hostidStr + "...");
								System.out.println("License hostid = " + lic_hostidStr);
								lic_hostid = hexStrtoLong(lic_hostidStr);
								break;
							}
						}
					}

					//else if (tmpStr1.compareTo("Key")>=0) {
					else if (tmpStr1.matches("Key\\s+=\\s+(.*)")) {
						//System.out.println("...4... getLicenseInfo:  tmpStr =..." + tmpStr + "...");

						for (i=0; i<tmpStr1.length(); i++) {
							if (tmpStr1.charAt(i)=='=') {
								lic_keyStr = tmpStr1.substring(i+2);// because extra-space after '=' 
								idxk1 = i+2;
								//System.out.println("lic_keyStr =..." + lic_keyStr + "...");
								System.out.println("License Key = " + lic_keyStr);
								break;
							}
						}

						// to get the 1st key
						for (i=0; i<tmpStr1.length(); i++) {
							if (tmpStr1.charAt(i)=='-') {
								tmpStr2 = tmpStr1.substring(idxk1, i);
								idxk2 = i+1;
								//System.out.println("lic_keyStr1 =..." + tmpStr2 + "...");
								lic_keyvalue1 = hexStrtoLong(tmpStr2);
								break;
							}
						}

						// to get the 2nd key
						for (i=idxk2; i<tmpStr1.length(); i++) {
							if (tmpStr1.charAt(i)=='-') {
								tmpStr2 = tmpStr1.substring(idxk2, i);
								idxk3 = i+1;
								//System.out.println("lic_keyStr2 =..." + tmpStr2 + "...");
								lic_keyvalue2 = hexStrtoLong(tmpStr2);
								break;
							}
						}

						// to get the 3rd key
						tmpStr2 = tmpStr1.substring(idxk3);
						//System.out.println("lic_keyStr3 =..." + tmpStr2 + "...");
						lic_keyvalue3 = hexStrtoLong(tmpStr2);

					}
				}
			} // end of while()

			br.close(); // close file
		} catch (FileNotFoundException event) {
			fileFlag = false;
			tmpStr = "<Exception> getLicenseInfo: " + infStr + " is NOT Found !\n";
			JOptionPane.showMessageDialog(null, tmpStr);
			System.out.print(tmpStr);
			saveErrorMessageRealtime("Msg:get license info is fail");
			killproc(); System.exit(1); // to quit Java app for Linux

		} catch (java.io.IOException err) {
			fileFlag = false;
			tmpStr = "<Exception> getLicenseInfo: " + err + "\n";
			JOptionPane.showMessageDialog(null, tmpStr);
			System.out.print(tmpStr);
			saveErrorMessageRealtime("Msg:get license info is fail");
			killproc(); System.exit(1); // to quit Java app for Linux
		}

	}


	public static long orgKey1(long value) {

		//gen: value = value + (0x9ab98fce - 0x137658 + 0x5678452);

		long v1 = 0x9ab98fceL; // MUST have 'L' or 'l' for long type data
		long v2 = 0x137658L; 
		long v3 = 0x5678452L; 
		long sum;

		sum = v1 - v2 + v3;
		//System.out.print("...orgKey1... pre_value = " + value + ", hostid = " + hostid);
		//System.out.print(", sum1 = " + sum);

		value = value - sum;
		//System.out.println(" ... value = " + value);
		return value;
	}

	public static long orgKey2(long value) {

		//gen: value = 0xffeeaabb - value;

		long v1 = 0xffeeaabbL;
		//System.out.print("...orgKey2... pre_value = " + value + ", v1 = " + v1);

		value = v1 - value;
		//System.out.println(" ... value = " + value);
		return value;
	}

	public static long orgKey3(long value) {

		//gen: value = value + 0x789036;

		long v1 = 0x789036L;
		//System.out.print("...orgKey3... pre_value = " + value + ", v1 = " + v1);

		value = value - v1;
		//System.out.println(" ... value = " + value);
		return value;
	}

	public static boolean verifyKey() {

		long lic_key12 = 0L;

		String tmpStr = "";

		System.out.println("....... License verifyKey() .......");

		lic_key12 = lic_keyvalue1 + lic_keyvalue2;
		// C++'s unsigned long's MAX = 0xFFFFFFFF
		if (lic_key12 > 0x100000000L) {
			lic_key12 = lic_key12 - 0x100000000L;
		}
		//System.out.println("...verifyKey... lic_key12 = " + lic_key12);

		if (hostnameStr.equals(lic_hostnameStr)) {
			System.out.println("...1... hostname ok");

			if (hostidStr.equals(lic_hostidStr)) {
				System.out.println("...2... hostid ok");

				if (hostid==orgKey1(lic_keyvalue1)) {
					System.out.println("...3... key1 ok");

					if (hostid==orgKey2(lic_keyvalue2)) {
						System.out.println("...4... key2 ok");

						if (lic_key12==orgKey3(lic_keyvalue3)) {
							System.out.println("...5... key3 ok");
							System.out.println("\n==> Launch csicAutomation System !");
							//JOptionPane.showMessageDialog(null, "Launch csicAutomation System !");
							return true;
						}
					}
				}
			}
		}

		tmpStr = "Wrong License Key, please call Supervisor !";
		System.out.println(tmpStr);
		JOptionPane.showMessageDialog(null, tmpStr);
		return false;
	}

	public static void processLicense() {

		String cmd = null;
		String tmpStr = null;

		try {
			tmpStr = "...... processLicense() ......";
			System.out.println(tmpStr); 

			//cmd = csicAutoPath + "classes/getSystem_Sigurd";
			cmd = "getSystem_Sigurd";
			Runtime.getRuntime().exec(cmd);

			//MUST have a wait time here
			Thread.sleep(5000);// unit: ms

			getSystemInfo();
			hostid = hexStrtoLong(hostidStr);

			cmd = "rm -f tmp.1";
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(500);// unit: ms

			getLicenseInfo();

			if (verifyKey()==false) {
				saveErrorMessageRealtime("Msg:verify key is fail");
				killproc(); System.exit(1); // to quit Java app for Linux
			}

		} catch (java.io.IOException err) {
			tmpStr = "processLicense: " + err;
			System.err.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			killproc(); System.exit(1); // to quit Java app for Linux
		} catch (java.lang.NullPointerException err2) {
			tmpStr = "processLicense: " + err2;
			System.err.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			killproc(); System.exit(1); // to quit Java app for Linux
		} catch (java.lang.InterruptedException Ierr) {
			tmpStr = "processLicense: " + Ierr;
			System.err.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			killproc(); System.exit(1); // to quit Java app for Linux
		}

	}

	public static void processSystemInfo() {

		String cmd = "";
		String tmpStr = "";

		csicAutoPath = checkifHeadEndWithSlash(csicAutoPath);

		try {
			tmpStr = "...... processSystemInfo() ......";
			System.out.println(tmpStr); 

			//cmd = csicAutoPath + "classes/getSystem_Sigurd";
			cmd = "getSystem_Sigurd";
			Runtime.getRuntime().exec(cmd);

			//MUST have a wait time here
			Thread.sleep(5000);// unit: ms

			//myPause();

			getSystemInfo();
			hostid = hexStrtoLong(hostidStr);

			//myPause();

			cmd = "/bin/rm -f tmp.1";
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(1000);// unit: ms

		} catch (java.io.IOException err) {
			tmpStr = "processSystemInfo: " + err;
			System.err.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			killproc(); System.exit(1); // to quit Java app for Linux
		} catch (java.lang.NullPointerException err2) {
			tmpStr = "processSystemInfo: " + err2;
			System.err.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			killproc(); System.exit(1); // to quit Java app for Linux
		} catch (java.lang.InterruptedException Ierr) {
			tmpStr = "processSystemInfo: " + Ierr;
			System.err.println(tmpStr);
			JOptionPane.showMessageDialog(null, tmpStr);
			killproc(); System.exit(1); // to quit Java app for Linux
		}
	}

	public static void showVersion_init() {
		String tmpStr = "";

		tmpStr = "START DateTime = " + getDateTime() + "\n\n";
		System.out.print(tmpStr); 
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		tmpStr  = "csicDMDAuto System CUS @ " + SITENAME + "\n";
		tmpStr += "OI Version: " + oiversion + "\n";
		tmpStr += "Date: " + oidate + "\n";
		tmpStr += "+-------------------------+\n";
		tmpStr += "| Please press [OI START] |\n";
		tmpStr += "+-------------------------+\n";
		System.err.println(tmpStr);

		tmpStr  = "csicDMDAuto System CUS @ " + SITENAME + "\n";
		tmpStr += "OI Version: " + oiversion + "\n";
		tmpStr += "Date: " + oidate + "\n";
		tmpStr += "+---------------------+\n";
		tmpStr += "+ Please press \"OI START\" +\n";
		tmpStr += "+---------------------+\n";
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);

		tmpStr  = "csicDMDAuto System CUS @ " + SITENAME + "\n";
		tmpStr += "OI Version: " + oiversion + "\n";
		tmpStr += "Date: " + oidate + "\n";
		tmpStr += "+--------------------+\n";
		tmpStr += "+ Please press \"OI START\"  +\n";
		tmpStr += "+--------------------+\n";

		JOptionPane JP = new JOptionPane();

		JP.showMessageDialog(null, tmpStr);
	}

	public static void myPause() {
		JOptionPane.showMessageDialog(null, "pause");
	}

	public static void showVersion() {
		String tmpStr = "";
		tmpStr  = "csicDMDAuto System CUS @ " + SITENAME + "\n";
		tmpStr += "OI Version: " + oiversion + "\n";
		tmpStr += "Date: " + oidate + "\n";
		System.err.println(tmpStr);
		textArea2.append(tmpStr);
		saveMessageRealtime(tmpStr);
		JOptionPane.showMessageDialog(null, tmpStr);
	}

	public static void main(String args[]) {
		
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));	//20171011
		processSystemInfo();
		LocalPath = "/tmp/"+hostnameStr+"_barcode_file/"; //20091103  
		File dir_local = new File(LocalPath); //20091103                                     //20090427
		if ( !dir_local.exists() )                                                         //20090427
		{                                                                            //20090427
			dir_local.mkdirs();                                                          //20090427
		}  
		String cmd;
		cmd = "rm -f " + LocalPath + "cmd_script.dmd";//20120828
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);
		cmd = "rm -f " + LocalPath + "execDmd";//20120828
		System.out.print(cmd + "\n"); 
		textArea2.append(cmd + "\n");
		javaExecSystemCmd(cmd);
		csicDXAuto_Sigurd_CUS autoSigurd = new csicDXAuto_Sigurd_CUS();

	}

}

