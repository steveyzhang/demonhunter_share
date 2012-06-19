package com.dh.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import com.dh.Poster;
import com.dh.PosterBuilder;
import com.dh.content.ContentParser;
import com.dh.content.DataList;
import com.dh.utility.DHLogger;
import com.dh.utility.GlobalContext;
import com.opensymphony.xwork2.ActionSupport;

public class DeployAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTemplateID() {
		return templateID;
	}
	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}
	String actionName = "DeployAction";
	
	private String data;
	private String templateID;
	public String execute() throws Exception { 
		
		try { 			
			if ( true ) 
			{ 
				int tID = 0;//Template ID for DHXML
				
				tID = Integer.parseInt( templateID);
				
				//Temporary Code There.
				DHLogger.log( DHLogger.INFO , actionName, String.format("Deploy id=%d, data=%s", tID , data ));
				
				//Parse and build the data
				ContentParser parser = new ContentParser();
				parser.parse( data );
				
				//Success and then get Content list
				DataList dList = parser.getDataList();
				
				//TODO save Content into separate data base.
				File output;
		        output = new File( GlobalContext.basePath + "database/content.xml");
		        PrintWriter outWriter = new PrintWriter(new BufferedWriter(new FileWriter(output)));
		        //Save data into database
		        outWriter.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?> <content><item><id value=\"1\" />" +
		        		"<imgurl path=\"http://www.google.com.hk\" /></item></content>");
		        outWriter.flush();
		        outWriter.close();
		        
		        
				DHLogger.log( DHLogger.INFO , actionName, String.format("Save to File Success" ));
				
				//Fill data	into Template
				PosterBuilder builder = new PosterBuilder();
				builder.setInput(tID);
				Poster wpPoster = builder.convertAndFill( PosterBuilder.POSTER_WP7,  dList, "~/Source/dh/output/outputsample.xml");
			
		
				
				//new Poster record is created and save it.
				//dao.store( "Poster", posterWP );
				//dao.store( "Poster", posterAndroid );
				
				//Log4j is necessary
				return "res_wp";//TODO 
			} 
		} 
		catch (Exception e) 
		{ 
			DHLogger.log( DHLogger.ERROR ,actionName, String.format("Exception:%s", e.getMessage()));
			e.printStackTrace();
		} 
	
		//this shouldn't happen in this example 
		return "res_wp";
	} 
}
