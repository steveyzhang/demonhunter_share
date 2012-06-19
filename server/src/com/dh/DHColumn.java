package com.dh;

import java.util.HashMap;

public class DHColumn extends DHElement {
	
	public DHColumn()
	{
		super(DHDocConst.TD);
	}
	
	public DHColumn( HashMap<String, String> dhAttrs )
	{
		super(DHDocConst.TD, dhAttrs );
	}
	
}
