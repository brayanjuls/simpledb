package simpledb.file;

import java.io.*;
import java.util.*;

public class FileMgr {
   private File dbDirectory;
   private int blocksize;
   private boolean isNew;
   private Map<String,RandomAccessFile> openFiles = new HashMap<>();

   private Map<String,Long> statistics = new HashMap<>();

   public FileMgr(File dbDirectory, int blocksize) {
      this.dbDirectory = dbDirectory;
      this.blocksize = blocksize;
      isNew = !dbDirectory.exists();

      // create the directory if the database is new
      if (isNew)
         dbDirectory.mkdirs();

      // remove any leftover temporary tables
      for (String filename : dbDirectory.list())
         if (filename.startsWith("temp"))
         		new File(dbDirectory, filename).delete();
   }

   public synchronized void read(BlockId blk, Page p) {
      try {
         RandomAccessFile f = getFile(blk.fileName());
         f.seek(blk.number() * blocksize);
         f.getChannel().read(p.contents());
         statistics.put(READ_COUNTS, statistics.getOrDefault(READ_COUNTS,0L)+1L);
      }
      catch (IOException e) {
         throw new RuntimeException("cannot read block " + blk);
      }
   }

   public synchronized void write(BlockId blk, Page p) {
      try {
         RandomAccessFile f = getFile(blk.fileName());
         f.seek(blk.number() * blocksize);
         f.getChannel().write(p.contents());
         statistics.put(WRITE_COUNTS, statistics.getOrDefault(WRITE_COUNTS,0L)+1L);
      }
      catch (IOException e) {
         throw new RuntimeException("cannot write block" + blk);
      }
   }

   public synchronized BlockId append(String filename) {
      int newblknum = length(filename);
      BlockId blk = new BlockId(filename, newblknum);
      byte[] b = new byte[blocksize];
      try {
         RandomAccessFile f = getFile(blk.fileName());
         f.seek(blk.number() * blocksize);
         f.write(b);
      }
      catch (IOException e) {
         throw new RuntimeException("cannot append block" + blk);
      }
      return blk;
   }

   public int length(String filename) {
      try {
         RandomAccessFile f = getFile(filename);
         return (int)(f.length() / blocksize);
      }
      catch (IOException e) {
         throw new RuntimeException("cannot access " + filename);
      }
   }

   public boolean isNew() {
      return isNew;
   }
   
   public int blockSize() {
      return blocksize;
   }

   private RandomAccessFile getFile(String filename) throws IOException {
      RandomAccessFile f = openFiles.get(filename);
      if (f == null) {
         File dbTable = new File(dbDirectory, filename);
         f = new RandomAccessFile(dbTable, "rws");
         openFiles.put(filename, f);
      }
      return f;
   }

   public Map<String, Long> getStatistics() {
      return statistics;
   }

   private final String READ_COUNTS="READ_COUNTS";
   private final String WRITE_COUNTS="WRITE_COUNTS";
}
