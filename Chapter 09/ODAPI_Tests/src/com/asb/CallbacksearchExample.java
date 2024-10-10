package com.asb;

import java.io.BufferedReader;		//ASB Added Import
import java.io.IOException;			//ASB Added Import
import java.io.InputStreamReader;	//ASB Added Import

import com.ibm.edms.od.ODCallback;
import com.ibm.edms.od.ODConfig;    //ASB Added Import
import com.ibm.edms.od.ODConstant;
import com.ibm.edms.od.ODCriteria;
import com.ibm.edms.od.ODFolder;
import com.ibm.edms.od.ODServer;
/**
 * Demonstrates folder search using a custom callback object.
 */
public class CallbacksearchExample
{
static ODServer odServer = null;
public static void main (String[] args) throws Exception
{
connect (
"CallbackSearchExample",   
"DESKTOP-B3JB93R", 1445,                // ASB Changed from mydocserver to DESKTOP-B3JB93R
"ADMIN", "filenet123"                   // ASB Changed to ADMIN and filenet123 from myusername", "mypassword
);
try
{
ODFolder folder = odServer.openFolder ("System Log"); //Recent Folders
MyCallback callback = getCallback (folder);
ODCriteria crit = folder.getCriteria ("Message");
crit.setOperator (ODConstant.OPEqual);                     //ASB Changed to setOperator from setOperand
crit.setSearchValue ("%pdf%");
//crit = folder.getCriteria ("TimeStamp");
//crit.setOperator (ODConstant.OPLike);					   //ASB Changed to setOperator from setOperand
//crit.setSearchValue ("%2024-04-02%");
System.out.print("Criteria Description   :" + crit.getDescription() + "\r\n");   //ASB Added for debug
System.out.print("Search Operator        :" + crit.getOperator() +    "\r\n");   //ASB Added for debug
System.out.print("Search Operator Values :" + crit.getFixedValues() + "\r\n");   //ASB Added for debug
folder.search (callback);
folder.close ();
}
catch (Exception e) { e.printStackTrace (); }
finally
{
disconnect ();
}
}
static void connect
(
String applicationName,
String server,
int port,
String usr,
String pwd
		) throws Exception
		{
		odServer = new ODServer (new ODConfig ());
		odServer.initialize (applicationName);
		odServer.setPort (port);
		odServer.setServerName (server);               //ASB Changed to setServerName from setServer
		odServer.setUserId (usr);
		odServer.setPassword (pwd);
		odServer.setConnectType (ODConstant.CONNECT_TYPE_TCPIP);
		odServer.logon ();
		}
		static void disconnect () throws Exception
		{
		if (odServer != null)
		{
		odServer.logoff ();
		odServer.terminate ();
		odServer = null;
		}
		}
		/**
		 * Factory method
		 * 
		 * @param folder An open ODFolder
		 * @return A callback that prints displayable hit fields
		 * in correct display order for the given
		 * folder
		 */
		public static MyCallback getCallback (ODFolder folder)
		{
		String[] displayOrder = folder.getDisplayOrder ();
		String[] queryOrder = folder.getQueryOrder ();
		int[] showIndex = new int[displayOrder.length];
		display: for (int i=0; i<displayOrder.length; i++)
		{
		ODCriteria criterion = folder.getCriteria (displayOrder[i]);
		showIndex[i] = -1; // -1 --> don't display
		if (criterion != null && criterion.isDisplayable ())
		{
		for (int j=0; j<queryOrder.length; j++)
		if (queryOrder[j].equals (displayOrder[i]))
		{
			showIndex[i] = j;
			continue display;
			}
			}
			}
			return new MyCallback (showIndex);
			}
			/**
			 * Custom callback class that prints search results
			 * to the console. Display values for hit fields 
			 * are printed in the order given to the constructor.
			 */
			public static class MyCallback extends ODCallback
			{
			int[] displayOrder = null;
			public MyCallback (int[] showIndex)
			{
			displayOrder = new int[showIndex.length];
			System.arraycopy (
			showIndex, 0, displayOrder, 0, showIndex.length);
			}
			public boolean HitCallback (
			java.lang.String docId, 
			char type, 
			java.lang.String[] values)
			throws java.lang.Exception
			{
			// print index field values in display order
			for (int i=0; i<displayOrder.length; i++)
			if (displayOrder[i] != -1)
			System.out.print (values[displayOrder[i]] + " ");
			System.out.println ();
			System.out.print ("Continue searching (y/n)? ");
			String response = null;
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			try
			{
			response = in.readLine ();
			}
			catch (IOException e)
			{
			e.printStackTrace ();
			System.err.println (
			"Error reading user input; search cancelled.");
			return false;
			}
			if (response != null && 
					response.toUpperCase ().startsWith ("Y"))
			{
				return true; // true --> continue searching
			}
				else return false; // false --> cancel search
			}
		}
	}
