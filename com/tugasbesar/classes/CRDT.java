package com.tugasbesar.classes;

import java.util.LinkedList;

public class CRDT{
	private LinkedList<CharacterData> characterDataCRDT;
	
	public CRDT () {
		characterDataCRDT = new LinkedList<CharacterData>();
	}
	public CharacterData getCharacterData(int index) {
		return characterDataCRDT.get(index);
	}
	public void setCharacterData(CharacterData characterData) {
		characterDataCRDT.set(characterData.getPosition(), characterData);
	}

	public void insert(CharacterData characterData) {
		if (characterData.getPosition() >= characterDataCRDT.size()){	// Appends behind the string
			characterDataCRDT.addLast(characterData);
		} else { // Insert inside the string
			characterDataCRDT.add(characterData.getPosition(), characterData);
		}
	}
	public void remove(String positionId){
		for (CharacterData characterData : characterDataCRDT){
			if (characterData.getPositionId().equals(positionId)){
				characterDataCRDT.remove(characterData);
				break;
			}
		}
	}

	public void printCrdt(){
		System.out.println("\nCRDT CONTENTS");
		System.out.println("=============");
		for (CharacterData characterData : characterDataCRDT) {
			System.out.println(characterData.getComputerId() + " | " + characterData.getPositionId() + " | " + characterData.getValue());
		}
	}

	/**
	 * @return the characterDataCRDT
	 */
	public LinkedList<CharacterData> getCharacterDataCRDT() {
		return characterDataCRDT;
	}

	/**
	 * @param characterDataCRDT the characterDataCRDT to set
	 */
	public void setCharacterDataCRDT(LinkedList<CharacterData> characterDataCRDT) {
		this.characterDataCRDT = characterDataCRDT;
	}
}