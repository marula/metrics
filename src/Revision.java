public class Revision {
    private String contents;
    private String author;

   public Revision(String contents, String author) {
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
