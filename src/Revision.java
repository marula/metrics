public class Revision {
    private String contents;
    private String author;

   public Revision(String author, String contents) {
       this.contents = contents;
       this.author = author;
   }

   public String getContents() {
       return contents;
   }

   public String getAuthor() {
       return author;
   }
}
