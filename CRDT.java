public class CRDT{
	private int index;
	private String text;
	public getIndex() {
		return index;
	}
	public getString() {
		return text;
	}
	public setIndex(int mIndex) {
		index = mIndex;
	}
	public setString(String mText) {
		text = mText;
	}
	public Insert1(char additional_char, int mIndex) {
		String new_text = getString().substring(0, mIndex) + additional_char + getString().substring(mIndex);
		setString(new_text);
	}
	public Insert2(char additional_char, int mIndex) {
		StringBuilder sb = new StringBuilder(getString());
		sb.insert(mIndex, additional_char);
		String new_text = sb.toString();
		setString(new_text);
	}
	public Delete1(int mIndex) {
		String new_text = getString().substring(0, mIndex) + getString().substring(mIndex + 1);
		setString(new_text);
	}
	public Delete2(int mIndex) {
		StringBuilder sb = new StringBuilder(getString());
		sb.deleteCharAt(mIndex);
		String new_text = sb.toString();
		setString(new_text);
	}
}