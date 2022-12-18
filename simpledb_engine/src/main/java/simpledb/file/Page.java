package simpledb.file;

import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.util.Date;

public class Page {
   private ByteBuffer bb;
   public static Charset CHARSET = StandardCharsets.US_ASCII;

   // For creating data buffers
   public Page(int blocksize) {
      bb = ByteBuffer.allocateDirect(blocksize);
   }
   
   // For creating log pages
   public Page(byte[] b) {
      bb = ByteBuffer.wrap(b);
   }

   public int getInt(int offset) {
      return bb.getInt(offset);
   }

   public void setInt(int offset, int n) {
      int contentSize = 4;
      if(bb.remaining()-contentSize>0){
         bb.putInt(offset, n);
      }
   }

   public byte[] getBytes(int offset) {
      bb.position(offset);
      int length = bb.getInt();
      byte[] b = new byte[length];
      bb.get(b);
      return b;
   }

   public void setBytes(int offset, byte[] b) {
      //size of the content plus 4 bytes for the length of the content
      int contentSize = b.length+4;
      if(bb.remaining()-contentSize>0) {
         bb.position(offset);
         bb.putInt(b.length);
         bb.put(b);
      }
      System.out.println("remaining: "+bb.remaining());
   }
   
   public String getString(int offset) {
      byte[] b = getBytes(offset);
      return new String(b, CHARSET);
   }

   public void setString(int offset, String s) {
      byte[] b = s.getBytes(CHARSET);
      setBytes(offset, b);
   }

   public static int maxLength(int strlen) {
      float bytesPerChar = CHARSET.newEncoder().maxBytesPerChar();
      return Integer.BYTES + (strlen * (int)bytesPerChar);
   }

   // a package private method, needed by FileMgr
   ByteBuffer contents() {
      bb.position(0);
      return bb;
   }

   public void setShort(int offset,short value){
      bb.putShort(offset,value);
   }

   public short getShort(int offset){
      return bb.getShort(offset);
   }

   public void setBoolean(int offset,boolean value){
      bb.putInt(offset,value?1:0);
   }

   public boolean getBoolean(int offset){
      int value = bb.getInt(offset);
      return value == 1;
   }

   public void setDate(int offset,Date value){
      System.out.println(value.getTime());
      bb.putLong(offset,value.getTime());
   }

   public Date getDate(int offset){
      long value = bb.getLong(offset);
      System.out.println(offset);
      return new Date(value);
   }
}
