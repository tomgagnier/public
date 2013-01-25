package mangotiger.util;

import java.util.Iterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author tom_gagnier@yahoo.com
 */
class FileIterator implements Iterator {

   final BufferedReader in;
   String line = null;

   public FileIterator(File file) throws FileNotFoundException {
      in = new BufferedReader(new FileReader(file));
   }

   public FileIterator(String path) throws FileNotFoundException {
      in = new BufferedReader(new FileReader(path));
   }

   public boolean hasNext() {
      if (line == null) {
         try {
            line = in.readLine();
         } catch (IOException e) {
            line = null;
         }
      }
      return line != null;
   }

   public Object next() {
      try {
         return line == null ? in.readLine() : line;
      } catch (IOException e) {
         return null;
      } finally {
         line = null;
      }
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
