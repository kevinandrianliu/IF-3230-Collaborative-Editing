# CDRT
## Petunjuk Penggunaan Program
Program di-compile dengan format sebagai berikut : './compile.sh' untuk Linux. Jika menggunakan Windows, harus meng-install Cygwin atau Git Bash, lalu compile dengan '.\compile.sh'
Program di-execute dengan format sebagai berikut : 'java Controller.java <ip> <port>' 

## Pembagian Tugas
13516058 : GUI dan laporan  
13516109 : GUI, Thread(Sender & Receiver), Controller, CRDT final model, insertionBuffer, deletionBuffer  
13516118 : CRDT initial model, Controller, Version Vector model, dan laporan  

## Laporan
### Cara Kerja Program
Kami membuat arsitektur peer-to-peer dengan n komputer(n menandakan jumlah node). Pertama kali, kami broadcast seluruh Id dari setiap komputer kepada komputer-komptuter lainnya. Lalu, setiap kali sebuah komputer melakukan operasi tersebut, akan dicek jenisnya. Jika ada komputer yang saat bersamaan melakukan insert=, akan dimasukkan ke dalam InsertionBuffer, sedangkan jika ada komputer yang melakukan delete, akan dimasukkan ke dalam DeletionBuffer. Ini untuk mencegah kemungkinan terjadinya insert di indeks yang sama, insert baru delete pada komputer yang berbeda dan pada indeks yang sama(baik ada delay maupun tidak), serta deleter baru insert pada komputer yang berbeda dan pada indeks yang sama(baik ada delay maupun tidak) Lalu, setiap jangka waktu tertentu, kedua buffer ini aka dicek apakah kosong atau tidak. Jika tidak kosong, insert/delete yang tercatat akan dikerjakan, dengan asumsi bahwa waktu delay memastikan pengklasifikasian antara operasi yang delay karena koneksi internet yang kurang baik(terlambat), dan yang tidak ada aksi yang mendahuluinya. Untuk kasus sisanya dipastikan memenuhi sifat konsistensi dan idempoten.

Untuk GUInya kami menggunakan Java Swing, dengan memiliki menubar berupa help. Help berisi cara penggunaan program yang kami buat. Setiap komputer harus terhubung dahulu, baru memencet enter di semua komputer yang terhubung tersebut. Untuk melakukan collaborative editing, pengguna dapat menambah/menghapus karakter pada textarea yang disediakan.

### Fungsi-fungsi Penting
CRDT merupakan model class yang bertujuan untuk mengatur memasukkan/menghapus karakter berdasarkan posisi tertentu pada komputer dengan Id tertentu. Untuk struktur datanya, kami menggunakan 2 kelas, yang pertama CharacterData dengan struktur data berikut(ini yang dimaksud CRDT pada spesifikasi):  
  'private String computerId;  
  private char value;  
  private int position;  
  private String positionId;  
  private String order;'  
dan CRDT yang berisi LinkedList dari CharacterData.  
Version Vector merupakan model class yang bertujuan untuk mengetahui versi dari sebuah komputer Id tertentu saat mendapatkan message dari komputerId lainnya bahwa ada update yang terjadi. Komputer yang menerima message dapat membandingkan version vector yang dikirimkan oleh komputer lain dengan version vector miliknya. Jika lebih kecil, akan diupdate(algoritma tidak perlu mengecek jika lebih besar karena tidak terjadi). Struktur datanya adalah sebagai berikut:  
 'private String computerId;  
 private int counter;'  
Deletion Buffer akan menjadi tempat penyimpanan sementara untuk operasi yang melakukan delete, agar ketika ada operasi yang melakukan insert di sebuah komputer dan dilanjutkan dengan delete pada indeks yang sama di komputer lainnya tetapi yang sampai pada sebuah komputer adalah kebalikan dari proses tersebut(karena delay akibat koneksi internet) dapat dikembalikan urutannya seperti sediakala, sehingga menjamin konsistensi hasil data dari ketiganya. Struktur datanya adalah LinkedList dari CharacterData.  

### Analisis
Menurut kami, sistem peer-to-peer membuat progam kami menjadi tidak intuitif. Kami harus mengakali kelemahan dalam InitialConnection dengan memaksa semua komputer harus terhubung terlebih dahulu, baru dapat melakukan collaborative editing. Lalu, kami tidak mengimplementasikan kemungkinan jika ada dua atau lebih operasi yang dilakukan pada komputer yan berbeda pada teks yang sama secara konkuren, sehingga ada kemungkinan gagal.

Seharusnya kami mengimplementasikannnya dengan sistem client-server agar server dapat mengatur operasi yang dilakukan oleh setiap client agar dapat konsisten.

### Kasus Uji

### Screenshoot



