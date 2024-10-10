package com.asb;

import java.util.Enumeration;
import com.ibm.edms.od.ODConfig;
import com.ibm.edms.od.ODConstant;
import com.ibm.edms.od.ODException;
import com.ibm.edms.od.ODFolder;
import com.ibm.edms.od.ODServer;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
/**
 * This class demonstrates connecting to an OnDemand
 * server using Version 10.5 of the ODWEK Java API
 * by providing a simple ping console utility.
 * 
 * */
public class ODPing
{
public static void main (String[] args) {
String serverName = "DESKTOP-B3JB93R"; // Name or IP address
//String serverName = "ECMUKDEMO22"; // Name or IP address
String userId = "ADMIN";   // User ID
String pwd = "filenet123"; // User password
int port = 1445; // Default port for OD. Configurable.
//
// Configure a new OnDemand server connection with
// the default configuration for this
// platform
//
ODServer odServer = new ODServer (new ODConfig ());
try {
//
// Set server and log-on credentials
//
odServer.setConnectType (ODConstant.CONNECT_TYPE_TCPIP);
odServer.setServerName (serverName); // Name or IP address
odServer.setPort (port);
odServer.setUserId (userId);
odServer.setPassword (pwd);
//
// Initialize the ODServer connection;
// once initialized, connection MUST be
// terminated when we're finished.
// Note use of this class' name, ODPing,
// as parameter to initialize().
//
odServer.initialize ("ODPing");
//
// Log on
//
odServer.logon ();
//
// Report ping status
//
System.out.println ("OnDemand server " +
odServer.getServerName () + " is alive");
//
//Perform some useful function
//
listFolders (odServer);
}
catch (ODException e) {
//
//If server returns a "bad credentials"
//code 2107, then it must be alive
//
if (e.getErrorId () == 2107)
{
System.out.print ("OnDemand server " +
odServer.getServerName () + " is alive but: ");
System.out.println (e.getErrorMsg ());
}
else
{
System.err.println ("Encountered error: " +
e.getErrorMsg ());
System.err.println (" Error code: " +
e.getErrorId ());
}
}
catch (Exception e) {
//Unknown problem
e.printStackTrace ();
}
finally {
//
//Ensure user is logged off
//
try { odServer.logoff (); }
catch (Exception e) { /* ignore any problem */ }
//
//ALWAYS terminate connections that
//are no longer needed
//
odServer.terminate ();
 }
}
public static void listFolders (ODServer odServer)
		throws Exception
		{
		Enumeration en = odServer.getFolders ();
		while (en.hasMoreElements ()) {
		System.out.println ("Folder: " +
		((ODFolder) en.nextElement ()).getName ());
		}
	}
}

