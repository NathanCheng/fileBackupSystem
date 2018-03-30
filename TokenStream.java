package edu.csulb.cecs534;
/**
 * Description: Interface TokenStream coping a folder to the destination folder.
 * @author Wanli Cheng chengwl2008@gmail.com,
 */
public interface TokenStream {
   /**
    Returns the next token from the stream, or null if there is no token
    available.
    */
   String nextToken();

   /**
    Returns true if the stream has tokens remaining.
    */
   boolean hasNextToken();
}
