package com.dh.content;

public class DHAction {
	int actionType;//goto type. ... 
	boolean bValidation;
	String goToModel;//goto to model when clicked.
	
	public DHAction( String actionStr )
	{
		//construct DHAction with action string 
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public String getGoToModel() {
		return goToModel;
	}

	public void setGoToModel(String goToModel) {
		this.goToModel = goToModel;
	}
	
	
}
