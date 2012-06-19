package com.dh;

import java.util.ArrayList;
import java.util.HashMap;

import com.dh.content.Data;
import com.dh.content.DataList;
import com.dh.utility.DHLogger;

/**
 *  Poster is the file which is present the layout and data build-in. There is different format for different platform. 
 *  Now Poster support Android, Window Phone in XML format.
 *  
 *  
 * **/
public abstract class Poster {
	
	protected HashMap<String, String > mHeader = null;
	protected DHElement mContent = null;
	
	
	public void setHeader( HashMap<String, String> header) {
		mHeader = header;
	}

	public void setContent( DHElement dhElement ) {
		
		String template = dhElement.getAttributeText(DHDocConst.TEMPLATE);
		if( template.equals( DHDocConst.RICH_TABLE ) == false )
		{
			DHLogger.log(DHLogger.WARNNING , DHLogger.PKG_POSTER , "the content is not the rich_table! only support rich_table by now");
		}
		
		mContent = dhElement;
	}
	
	/**
	 * return the full content of Poster in String Format.
	 * **/
	public String getContent()
	{
		return null;
		//TODO
	}

	/**
	 * build()
	 * 	build the XML for target platform 
	 * **/
	abstract void build(DataList dList, String outputPath );

	/**
	 * save()
	 * 	save and output
	 *	
	 * 	@param	path
	 * 	the path to save
	 * **/
	public void save(String path) {
		// TODO Auto-generated method stub
		
	}
}
