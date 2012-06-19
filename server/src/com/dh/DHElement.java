package com.dh;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * 
 * **/
public class DHElement implements Cloneable  {
	
	protected int MAX_CHILDREN = 20;
	protected ArrayList<DHElement> mChildren = new ArrayList<DHElement>( MAX_CHILDREN );
	protected String mTagName;
	protected HashMap<String, String> mAttributes;
	
	public DHElement( String tagName ) {
		mTagName = new String(tagName);
	}
	
	public DHElement( String tagName, HashMap<String, String> attrs ) {
		mTagName = new String(tagName);
		mAttributes = attrs;
	}
	
	@SuppressWarnings("unchecked")
	public DHElement( DHElement input ) {
		mTagName = new String(input.mTagName);
		mAttributes = (HashMap<String, String>) input.mAttributes.clone();
		
		//For children 
		//TODO 
	}
	
	@Override
	/**
	 * return deep copy for the element
	 * **/
	public Object clone() {
		return new DHElement(this);
	}

	public ArrayList<DHElement> getChilds() {
		return mChildren;
	}

	public String getTagName() {
		return mTagName;
	}


	public HashMap<String, String> getAttributes() {
		return mAttributes;
	}

	/**
	 * 
	 * @param template
	 * @return 
	 * 	return empty string if there is no attribute.
	 */
	public String getAttributeText(String template) {
		
		if(mAttributes == null )
			return new String("");
		
		String value = mAttributes.get(template);
		return value == null ? new String("") : value ;
	}

	public void addChild(DHElement child )
	{
		mChildren.add(child);
	}
	
	public void setAttributes( HashMap<String, String> attrs )
	{
		mAttributes = attrs;
	}
	
}
