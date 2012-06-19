package com.dh;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dh.content.Data;
import com.dh.content.DataList;
import com.dh.utility.DHLogger;
import com.dh.utility.Utility;

public class PosterBuilder {	
	public static final int POSTER_WP7 = 1;
	public static final int POSTER_ANDROID = 2;
	
	//Output 
	private Poster mPoster = null;
	
	//Input 
	private Document mInputDoc = null;
	private int mTID = 0;
	
	/**
	 * create
	 * 
	 * @param htmlTmp
	 * 	the DHXML layout with blank item which is used to fill the data.(jSoup)
	 * @param platTag
	 * 	the platform tag for poster
	 * @param dList
	 * 		the Content Source for Poster
	 * **/
	public Poster convertAndFill( int posterid , DataList dList, String outputPath ) {
		
		mPoster = createPoster(posterid );
		
		mPoster.setHeader( getInputHeader() );
		mPoster.setContent( getContent() );
		
		mPoster.build(dList, outputPath);
		
		return mPoster;
	}

	/**
	 * Create Different Poster 
	 * @param posterID 
	 * 	the id of poster, for example "POSTER_WP7"
	 * **/
	private Poster createPoster(int posterID) {
		Poster poster = null;
		
		switch(posterID)
		{
		case POSTER_WP7:
			poster = new WPPoster();
			break;
		case POSTER_ANDROID:
			poster = null;
			break;
		}
		return poster;
	}

	/**
	 * getHeader
	 * 
	 * 	get All information for header from input;
	 * 		The Key-Value map will return 
	 *	
	 *		keys and value: 
	 *		title = String
	 *		width = numeric
	 *		height = numeric
	 *		templateType= preset String, table
	 *
	 * @return HashMap
	 * 
	 * **/
	private HashMap<String, String> getInputHeader() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		//title
		Elements elements = mInputDoc.getElementsByTag(DHDocConst.TITLE);
		Element node = null;
		if( elements.isEmpty() != true )
		{
			node = elements.get(0);
			fillValueInMap(node, map);
		}
		
		//Width, Height in <table>
		elements = mInputDoc.getElementsByTag(DHDocConst.TABLE);
		if( elements.isEmpty() != true )
		{
			node = elements.get(0);
		
			fillAttributeInMap(node , DHDocConst.HEIGHT, map);
			fillAttributeInMap(node , DHDocConst.WIDTH, map);
		}
		
		map.put(DHDocConst.TEMPLATE, DHDocConst.TABLE);
		
		return null;
	}

	/**
	 * fillAttributeInMap
	 * 	fill the Attribute from HTML element into Map given.
	 * **/
	private boolean fillAttributeInMap(Element parent, String attr, HashMap<String, String> map )
	{
		if( parent == null )
			return false;
		
		String Str = parent.attr(attr);
		if(Str.isEmpty() != false )
		{
			map.put(attr, Str );
			return true;
		}
		
		return false;
	}
	
	/**
	 * fillValueInMap
	 * 	fill the value from HTML element into Map given.
	 * 	for example, <title>Value is there</title>
	 * 	the parent's tag name will be the key of map.
	 * 
	 * **/
	private boolean fillValueInMap(Element parent, HashMap<String, String> map )
	{
		if( parent == null )
			return false;
		
		String Str = parent.val();
		if(Str.isEmpty() != false )
		{
			map.put(parent.tagName(), Str );
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * Convert HTML Content(input) to DHElement
	 * the content is marked as attribute that id="content"
	 * 	
	 * 	html:: <table id="content" template_type="rich_table" >
	 * **/
	protected DHElement getContent() {
		
		Elements eles = mInputDoc.getAllElements();
		Element content = null;
		//Find the Content tag
		for(Element ele : eles)
		{
			String attrValue = ele.attr(DHDocConst.ID );
			if( attrValue.equals(DHDocConst.CONTENT) )
			{
				content = ele;
				break;
			}
		}
		
		//By now we only support rich_table
		if( content == null )
		{
			DHLogger.log(DHLogger.WARNNING, DHLogger.PKG_POSTER , "content is not found");
			return null;
		}
		
		//Check the template type
		if( content.attr(DHDocConst.TEMPLATE).equals(DHDocConst.RICH_TABLE) == true )
		{
			//OK, this is a rich_table template.
			return (DHElement)getRichTable(content);
		}
		else
		{
			//TODO 
			//other type is supported in future.
		}
	
		return null;
	}

	/**
	 * getRichTable
	 * 	get table after parser the <table> content
	 * 	@param content
	 * 		the table in jsoup element.
	 * **/
	private DHTable getRichTable(Element content) {
		if( content == null )
			return null;
		
		if(content.tagName().equals(DHDocConst.TABLE) != true )
			return null;
		
		//New table
		DHTable dhTable = new DHTable();
			
		//Table's Attributes
		Attributes tabAttrs = content.attributes();
		
		HashMap<String, String> dhTableAttrs = new HashMap<String, String>();
		for( Attribute attr : tabAttrs )
		{
			dhTableAttrs.put( attr.getKey(), attr.getValue() );
		}
		dhTable.setAttributes(dhTableAttrs);
		
		//OK, there is a table
		//Retrieve the row one by one
		Elements rows = content.getElementsByTag(DHDocConst.TR);

		//Add Rows and Columns
		for(Element row : rows )
		{
			Elements columns = row.getElementsByTag(DHDocConst.TD);
			
			//TR's attributes
			Attributes attrs = row.attributes();
			
			HashMap<String, String> dhRowAttrs = new HashMap<String, String>();
			for( Attribute attr : attrs )
			{
				dhRowAttrs.put( attr.getKey(), attr.getValue() );
			}
			
			//New on DHRow
			DHRow dhRow = new DHRow(dhRowAttrs);
			
			//Retrieve TD(Column)
			for( Element col : columns )
			{
				Attributes colAttrs = col.attributes();
				
				HashMap<String, String> dhColAttrs = new HashMap<String, String>();
				for( Attribute attr : colAttrs )
				{
					dhColAttrs.put( attr.getKey(), attr.getValue() );
				}
				
				DHColumn dhCol = new DHColumn( dhColAttrs );
				dhRow.addCol( dhCol);
			}

			//Add into Table
			dhTable.addRow(dhRow);
		}
		
		
		return dhTable;
	}

	/**
	 * setInput
	 * 
	 * @param tID
	 * 	the input template ID for Poster Builder.
	 * **/
	public void setInput(int tID) {
		mTID = tID;
		try {
			mInputDoc = openTemplate(mTID);
		} catch (IOException e) {
			DHLogger.log(DHLogger.ERROR , DHLogger.PKG_POSTER, "exception in setInput()");
			e.printStackTrace();
		}
	}
	
	private Document openTemplate( int tID ) throws IOException {
		Document doc = null;
		switch(tID)
		{
		case 1:
		{
			 File file = new File( Utility.getFullPath("t_table.html") );
	         if (file.exists()) {
	        	 doc = Jsoup.parse(file, "UTF-8");
	            }
		}
			break;
		default:
			break;
		}
		
		return doc;
	}
}

