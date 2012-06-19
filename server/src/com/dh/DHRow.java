package com.dh;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * **/
public class DHRow extends DHElement {
	
	public DHRow()
	{
		super(DHDocConst.TR);
	}

	public DHRow( HashMap<String, String> dhAttrs )
	{
		super(DHDocConst.TD, dhAttrs );
	}
	
	public void addCol(DHColumn dhCol) {
		addChild(dhCol);
	}

	/**
	 * getTableColumnCount()
	 * 
	 *		retrieve the children to calculate the ColumnCount
	 *		Due to the "colspan" attribute, the size of children is not equal the Table Column Count in some case.
	 * @return 
	 * 	the Column Count for this table.
	 */
	public int getTableColumnCount() {
		int span = 0, count = 0;
		for(int i = 0; i< mChildren.size(); i++)
		{
			DHColumn column = (DHColumn)mChildren.get(i);
			String colspan = column.getAttributeText("colspan");
			if( colspan.equals("") )
				span = 0;
			else
				span = Integer.parseInt(colspan);
			count += 1;
			count += span;
		}
		
		return count;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getColumnCount() {
		return mChildren.size();
	}

	public DHColumn getChild(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
