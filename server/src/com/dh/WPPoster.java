package com.dh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.dh.content.Data;
import com.dh.content.DataList;
import com.dh.utility.DHLogger;
import com.dh.utility.Utility;

public class WPPoster extends Poster {

	
	public final String mBaseXML = "wpbase.xml";
	/**
	 * build()
	 * 	build the XML for target platform 
	 * @param dataList
	 * 	the data which will be filled into template
	 * @param outputPath
	 * 	the path for output
	 * **/
	@Override
	public void build(DataList dataList, String outputPath ) {
		
		//TODO
		//By now we only support the rich_table
		if( mContent.getClass() !=  DHTable.class )
		{
			DHLogger.log(DHLogger.WARNNING, DHLogger.PKG_POSTER, "build() Only Support the RichTable by now.");
			return;
		}
		
		//open new template for output
		SAXReader saxReader = new SAXReader();
		Document document = null;
        try {
			 document = saxReader.read( Utility.getFullPath(mBaseXML) );	
		} catch (DocumentException e) {
			DHLogger.log(DHLogger.ERROR, DHLogger.PKG_POSTER, "saxReader failed.");
			e.printStackTrace();
			return;
		}
		
        //instert the header
        List<Node> list = (List<Node>) document.selectNodes("/UserControl");
        Iterator<Node> iter = list.iterator();
        while (iter.hasNext()) {
        	//Screen Height and Width
        	Attribute attribute = (Attribute) iter.next();
            if (attribute.getName().equals("d:DesighHeight"))
                attribute.setValue(mHeader.get(DHDocConst.HEIGHT));
            else if (attribute.getName().equals("d:DesighWidth"))
                attribute.setValue(mHeader.get(DHDocConst.WIDTH));
        }
        
		//title
        list = (List<Node>) document.selectNodes("/UserControl/Grid/StackPanel/TextBlock");
        iter = list.iterator();
        boolean bFind = false;
        while( iter.hasNext() && bFind == false )
        {
        	Element element = (Element) iter.next();
        	Iterator<Attribute> iter1 = element.attributeIterator();
        	while( iter1.hasNext() )
        	{
        		Attribute attr = (Attribute )iter1.next();
        		if(attr.getName().equals("x::Name") == true && attr.getText().equals("ApplicationTitle") == true)
        		{
        			//Find the title and update it
        			bFind = true;
        		}
        		
        		attr = element.attribute("Text");
        		attr.setText(mHeader.get(DHDocConst.TITLE));
        		bFind = true;
        	}
        }
        
		/* 
		 * Begin::insert the content with dataList
        */
        //Fill data as resource at first.
        //TODO
        
        //Fill the content 
        DHTable table = (DHTable) mContent;
		setTableInDoc( document , table, dataList );
		
        //
		/* 
		 * End::insert the content with dataList
        */
	}
	
	private void setTableInDoc(Document document, DHTable table,
			DataList dataList) {
				
		setRowDefInDoc( document , table );
		setColumnDefInDoc( document, table );
		
		ArrayList<DHElement> dhRows = table.getRows();
		int rowCount = table.getRowCount();
		
		int colNum = table.getRowCount();
		List<Node> list = (List<Node>) document.selectNodes("/UserControl/Grid/Grid/ScrollViewer/Grid");
		Iterator<Node> iter = list.iterator();
		if( iter.hasNext() != true )
		{
			//Fatal Error There
			return;
		}
		
		Element element = (Element)iter.next();
		
		
		for( int i = 0 ; i < rowCount ; i++ )
		{
			DHRow dhRow = (DHRow) dhRows.get(i);
			ArrayList<DHElement> columenSet = dhRow.getChilds();
			for( int j = 0; j < dhRow.getChilds().size(); j++ )
			{
				//Fill Column
				String dataID = columenSet.get(j).getAttributeText("data");
				Data data = dataList.getData(dataID);
				addOneColumn(element, dhRow.getChilds().get(j) , data );
			}
			
			
		}
		
	}
	
	/**
	 * add one XML column as a children element of parent
	 * 
	 * There is a sample below.
	 * <Button Grid.ColumnSpan="3" Template="{StaticResource ButtonControlEmptyTemplate}"><tbcontrols:TBImage Source="http://t1.gstatic.com/images?q=tbn:ANd9GcSpJxvhm7qlewsik2_c4maetAzwcDXgHaMjiuRjpzSZ3SKoqJIntA" Background="{StaticResource imageBackgroundColor}">
     * </tbcontrols:TBImage><i:Interaction.Triggers><i:EventTrigger EventName="Click"><tbAction:TBNavigateToPageAction ObjParameter="{StaticResource id1}" TargetPage="/TBWP7Project;component/Pages/AuctionDetailPage.xaml"/></i:EventTrigger></i:Interaction.Triggers></Button>
	 * 
	 * @param parent
	 * 	the parent element of the xml tree
	 * @param dhElement
	 * 	the dhElement for HTML tree
	 * @param data
	 * 	the data and action image to each column
	 */
	private void addOneColumn(Element parent, DHElement dhElement, Data data) {
		
		//Temporary code There.
		Element column = parent.addElement("Button");
		column.addAttribute("Template", "{StaticResource ButtonControlEmptyTemplate}");
		column.addAttribute("Grid.ColumnSpan", dhElement.getAttributeText("colspan"));
		Element control = column.addElement("tbcontrols:TBImage");
		control.addAttribute("Source", "http://t1.gstatic.com/images?q=tbn:ANd9GcSpJxvhm7qlewsik2_c4maetAzwcDXgHaMjiuRjpzSZ3SKoqJIntA");
		control.addAttribute("Background", "{StaticResource imageBackgroundColor}" );
		
		//TODO
	}

	private void setRowDefInDoc(Document document, DHTable table) {
		int colNum = table.getRowCount();
		List<Node> list = (List<Node>) document.selectNodes("/UserControl/Grid/Grid/ScrollViewer/Grid");
		Iterator<Node> iter = list.iterator();
		if(iter.hasNext())
		{
			Element element = (Element)iter.next();
			element = element.addElement("Grid.RowDefinition");
			
			ArrayList<DHElement> dhElements = table.getRows();
			
			for( int i = 0; i < colNum ; i++ )
			{
				Element item = element.addElement("RowDefinition");
				DHElement dhItem = dhElements.get(i);
				//NOTE: Poster handle the translate between Semantic of DHHTML to platform XML
				HashMap<String, String> itemAttr = translateAttr( "RowDefinition", dhItem.getAttributes());
				addAttributes(item, itemAttr );	
				
				//Default Attribute add in the end
				addDefaultAttribute(item, "Height", "auto");
			}
		}	
		
	}
	
	private boolean setColumnDefInDoc(Document document, DHTable table) {
		int colNum = table.getColumnCount();
		
		if(table.getRowCount() < 1)
		{
			DHLogger.log(DHLogger.ERROR, DHLogger.PKG_POSTER, "setColumnDefInDoc() failed, there is no rows");
			return false;
		}
		
		//Find one row that its columns have smallest "colspan" count.
		//NOTE[assert]: I assert template should have at least one row that its columns have no colspan attribute. We say it "no_colspan_row"
		List<Node> list = (List<Node>) document.selectNodes("/UserControl/Grid/Grid/ScrollViewer/Grid");
		Iterator<Node> iter = list.iterator();
		if(iter.hasNext() == false)
		{
			//root is missing
			//Log here
			return false;
		}
		
		//find no_colspan_row
		Element element = (Element)iter.next();
		DHRow dhRow = null;
		int i = 0;
		for( i = 0; i < table.getRowCount(); i++)
		{
			dhRow = table.getRow(i);
			
			int j = 0;
			for(  j = 0; j < dhRow.getColumnCount(); j++)
			{
				DHColumn dhColumn = dhRow.getChild(j);
				String span = dhColumn.getAttributeText("colspan");
				if( span.isEmpty() == false )
					break;
			}
			
			if( j == dhRow.getColumnCount() )
			{
				DHLogger.log(DHLogger.ERROR, DHLogger.PKG_POSTER, String.format("setColumnDefInDoc(), find no_colspan_row row.no=%d", i));
				//find it
				break;
			}			
		}
		
		if(i == table.getRowCount() )
		{
			//exception there
			DHLogger.log(DHLogger.ERROR, DHLogger.PKG_POSTER, "setColumnDefInDoc() failed, no found no_colspan_row");
			return false;
		}
			
			
		//add column parameters into xml
		element = element.addElement("Grid.ColumnDefinitions");
		for(  i = 0; i < colNum ; i++ )
		{
			Element item = element.addElement("ColumnDefinition");
			DHElement dhItem = dhRow.getChild(i);
			//NOTE: Poster handle the translate between Semantic of DHHTML to platform XML
			HashMap<String, String> itemAttr = translateAttr( "ColumnDefinition", dhItem.getAttributes());
			addAttributes(item, itemAttr );	
		}
		
		return true;
	}

	/**
	 * addDefaultAttribute
	 * 	Default attribute only is added when this attribute is not specified manually 
	 *  
	 * @param item
	 * @param key
	 * @param value
	 */
	 
	private void addDefaultAttribute(Element item, String key, String value) {
		Attribute attr = item.attribute(key);
		if( attr != null )
		{
			DHLogger.log(DHLogger.INFO , DHLogger.PKG_POSTER , "addDefaultAttribute() already there, ignore adding");
			return;
		}
		
		DHLogger.log(DHLogger.INFO , DHLogger.PKG_POSTER , 
				String.format( "addDefaultAttribute(), the default %s=%s will be added", key, value));
		
		item.addAttribute(key, value );
		
	}
	
	private void addAttributes(Element item, HashMap<String, String> itemAttr) {
		Set<String> keyList = itemAttr.keySet();
		Iterator<String> iter = keyList.iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			item.addAttribute(key, itemAttr.get(key));
		}
		
		
	}

	/** translateAttr
	 * 		Poster handle the translate between Semantic of DHHTML to platform XML
	 * 
	 * @param attributes
	 * 	
	 * @return
	 **/
	private HashMap<String, String> translateAttr(String xmlTag , HashMap<String, String> attributes) {
		//xmlTag just for logger
		DHLogger.log(DHLogger.INFO, DHLogger.PKG_POSTER, String.format("translateAttr() key=%s", xmlTag ));
		
		Set<String> attrKeys = attributes.keySet();
		Iterator<String> iter = attrKeys.iterator();
		
		//output hashmap
		HashMap<String, String> output = new HashMap<String, String>();
		while( iter.hasNext() )
		{
			String key = (String) iter.next();
			String newValue = translateAttributeText(key, attributes.get(key) );
			output.put(key, newValue);
		}
		
		return output;
	}



	/*
	 * BEGIN: translate functions
	 * */
	private String translateAttributeText(String key, String value ) {
		if( key.equals("Height") || key.equals("Width") )
		{
			//Check Value
			//Regular formal
			//TODO
			if(value.contains("%") )
			{
				//OK, it means it is percent format
				return value.substring(0, value.length() -1 );
			}
		}
		
		//by default return value directly
		return value;
	}
	/*
	 * END: translate functions
	 * */


	
}
