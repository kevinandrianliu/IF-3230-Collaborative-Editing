public class CRDT{
	private int index;
	private String text;
	
	public CRDT (int mIndex, String mText) {
		index = mIndex;
		text = mText;
	}
	public int getIndex() {
		return index;
	}
	public String getString() {
		return text;
	}
	public void setIndex(int mIndex) {
		index = mIndex;
	}
	public void setString(String mText) {
		text = mText;
	}
	public void Insert1(char additionalChar, int mIndex) {
		String newText = text.substring(0, mIndex) + additionalChar + text.substring(mIndex);
		setString(newText);
	}
	public void Insert2(char additionalChar, int mIndex) {
		StringBuilder sb = new StringBuilder(text);
		sb.insert(mIndex, additionalChar);
		String newText = sb.toString();
		setString(newText);
	}
	public void Delete1(int mIndex) {
		String new_text = text.substring(0, mIndex) + text.substring(mIndex + 1);
		setString(new_text);
	}
	public void Delete2(int mIndex) {
		StringBuilder sb = new StringBuilder(text);
		sb.deleteCharAt(mIndex);
		String new_text = sb.toString();
		setString(new_text);
	}
}