package com.dh;

import java.util.ArrayList;

public class DHTable extends DHElement {
	
	public DHTable( )
	{
		super(DHDocConst.TABLE );
	}
	
	public void addRow( DHRow dhRow )
	{
		addChild(dhRow);
	}

	public int getRowCount() {
		return mChildren.size();
	}
	
	public int getColumnCount() {
		//Retrieve Column to get ColumnCount
		if( mChildren.size() == 0 )
			return 0;
		
		DHElement element = mChildren.get(0);
		if(element.getClass() != DHRow.class )
		{
			//exception there.
			//Log there
			return 0;
		}
		DHRow row = (DHRow) element;
		return row.getTableColumnCount();
	}

	public ArrayList<DHElement> getRows() {
		return mChildren;
	}
	
	/**
	 * getRow
	 * 	get one Row
	 * @param index
	 * @return
	 */
	public DHRow getRow(int index )
	{
		if( index >= mChildren.size() )
			return null;
		
		return (DHRow)mChildren.get(index);
	}
}
