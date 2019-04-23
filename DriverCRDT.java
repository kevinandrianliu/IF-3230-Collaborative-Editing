public class DriverCRDT {
	public DriverCDRT(){}
	public static void print(CRDT crdt) {
		System.out.println(crdt.getIndex());
		System.out.println(crdt.getString());
	}
	public static void main(String []args) {
		CRDT crdt = new CRDT(1, "Peter Parker");
		print(crdt);
		crdt.setIndex(2);
		crdt.Insert1('c', crdt.getIndex());
		print(crdt);
		crdt.setIndex(8);
		crdt.Insert2('d', crdt.getIndex());
		print(crdt);
		crdt.setIndex(0);
		crdt.Delete2(crdt.getIndex());
		print(crdt);
	}
}