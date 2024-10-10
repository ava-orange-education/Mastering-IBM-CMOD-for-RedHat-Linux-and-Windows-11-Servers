package com.asb;

	import java.io.File;
	import java.util.*;
	        
	import com.ibm.edms.od.*;
	        
	public class TcLoadDocs
	{
	    public static void main ( String argv[] )
	    {
	        ODServer    odServer;
	        ODConfig    odConfig;
	        Hashtable<String,String> idxs;
	        File        loadFile;
	        String serverName = "DESKTOP-B3JB93R"; // Name or IP address
	         //String serverName = "ECMUKDEMO22"; // Name or IP address
	         String userId = "ADMIN";   // User ID
	         String pwd = "filenet123"; // User password
	         int port = 1445; // Default port for OD. Configurable.
	         //argv[0]= serverName;
	         //argv[1]= userId;
	         //argv[2]= pwd;
	        //if ( argv.length < 3 )
	        //{
	        //    System.out.println( "usage: java TcLoadDocs <server> <userid> <password> " );
	        //    return;
	        //}
	        try
	        {
	            //----------
	            // Set the stage
	            //----------
	            System.out.println( "This testcase should" );
	            System.out.println( "  Demonstrate the ODWEK Load Process" );
	            System.out.println( "" );
	            System.out.println( "---------------------------------------------------" );
	            System.out.println( "" );
	            
	            odConfig = new ODConfig
	                (ODConstant.PLUGIN,
	                      ODConstant.APPLET,
	                      null,
	                      200,
	                      "/applets",
	                      "ENU",
	                      "c:/temp",
	                      "c:/trace",
	                      0);
	            
	            odServer = new ODServer(odConfig );
	            odServer.initialize(  "TcLoadDocs.java" );
	            //System.out.println( "Logging on to " + argv[0] + "..." );
	            //odServer.logon( argv[0], argv[1], argv[2] );
	            System.out.println( "Logging on to " + serverName + "..." );
	            odServer.logon( serverName, userId, pwd );
	            
	            //----------
	            // Step 1 in the ODWEK Load process
	            //----------
	            odServer.loadInit();
	            
	            //----------
	            // Step 2 in the ODWEK Load process
	            // Repeated for each document to be loaded
	            //----------
	            idxs = new Hashtable<String, String>();
	            /*              // ASB We have the following Baxter Bay Credit fields
	             * 	name
					account
					ord_date
					balance
	             */
	                
	            idxs.put("name",    "Geoffrey R. Stephens"); // ASB Changed to name from acct_name
	            idxs.put("account",     "32414432551");      // ASB Changed to account from acct_num
	            idxs.put("crd_date","2012-03-25");           // ASB Changed to crd_date from purchase_date
	            idxs.put("balance", "1172.18");              // ASB Changed to balance from purchase_amt
	            //loadFile = new File("c:\\invoices\\acme\\bluePaintingInvoice.afp");
	            loadFile = new File("insure.afp");                      //ASB in /root/eclipse-workspace/ODAPI_Tests/
	            odServer.loadAddDoc(4, idxs, loadFile);
/*	  // ASB Try just one document load at first              
	            idxs.clear();
	            loadFile = null;
	                
	            idxs.put("acct_name",    "Acme Art Company");
	            idxs.put("acct_num",     "00112233445");
	            idxs.put("purchase_date","2012-05-30");
	            idxs.put("purchase_amt", "879.39");
	            loadFile = new File("c:\\invoices\\acme\\redPaintingInvoice.afp");
	            odServer.loadAddDoc(3, idxs, loadFile);
	                
	            idxs.clear();
	            loadFile = null;
	               
	            idxs.put("acct_name",    "Light and Dark Inc.");
	            idxs.put("acct_num",     "99887766554");
	            idxs.put("purchase_date","2012-05-15");
	            idxs.put("purchase_amt", "8711.10");
	            loadFile = new File("c:\\invoices\\light_dark\\blackWhiteCharcoalInvoice.afp");
	            odServer.loadAddDoc(7, idxs, loadFile);            
*/ // ASB Try just one document load at first	                
	            //----------
	            // Step 3 in the ODWEK Load process
	            // Done adding documents, now call the server to load
	            //----------
	            //odServer.loadCommit("Invoices", "InvoicesWestCoast"); //ASB Updated to our CMOD configuration
	            odServer.loadCommit("Baxter Bay Credit", "Baxter Bay Credit"); //Application Group,Application
	            //----------
	            // Cleanup
	            //----------
	            odServer.logoff( );
	            odServer.terminate( );
	            System.out.println( "" );
	            System.out.println( "---------------------------------------------------" );
	            System.out.println( "" );
	            System.out.println( "Testcase completed" );
	            System.out.println( "" );
	        }
	        catch ( ODException e )
	        {
	            System.out.println( "ODException: " + e );
	            System.out.println( "  id = " + e.getErrorId( ) );
	            System.out.println( "  msg = " + e.getErrorMsg( ) );
	            e.printStackTrace( );
	        }
	        catch ( Exception e2 )
	        {
	            System.out.println( "exception: " + e2 );
	            e2.printStackTrace( );
	        }
	    }
	}
