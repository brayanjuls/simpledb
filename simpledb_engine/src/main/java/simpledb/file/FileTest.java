package simpledb.file;

import java.io.*;
import java.util.Date;

import simpledb.server.SimpleDB;

public class FileTest {
   public static void main(String[] args) throws IOException {
      SimpleDB db = new SimpleDB("filetest", 400, 8);
      FileMgr fm = db.fileMgr();
      BlockId blk = new BlockId("testfile", 2);
      int pos1 = 88;

      Page p1 = new Page(fm.blockSize());
      p1.setString(pos1, "abcdefghijklm");
      int size = Page.maxLength("abcdefghijklm".length());
      int pos2 = pos1 + size;
      p1.setInt(pos2, 345);
      int pos3 = pos2+4;
      String content = "AHHHHASDASSADASDASDA ASDASDASDAS ASDASDASD ASDASDASDASD ASDASDASDASD ASDASDASD ASDASDASDASD ASDASDASDASD ASDASDASDA ASDASDASDADS ASDASDASD";
      StringBuilder contentBuilder = new StringBuilder(content);
      p1.setString(pos3,contentBuilder.toString());

      int pos4 = pos3+Page.maxLength(content.length());
      p1.setDate(pos4,new Date(728389697000L));

      int pos5 = pos4+8;
      p1.setBoolean(pos5,true);
      fm.write(blk, p1);

      Page p2 = new Page(fm.blockSize());
      fm.read(blk, p2);
      System.out.println("offset " + pos1 + " contains " + p2.getString(pos1));
      System.out.println("offset " + pos2 + " contains " + p2.getInt(pos2));
      System.out.println("offset " + pos3 + " contains " + p2.getString(pos3));
      System.out.println("offset " + pos4 + " contains " + p2.getDate(pos4));
      System.out.println("offset " + pos5 + " contains " + p2.getBoolean(pos5));
   }
}