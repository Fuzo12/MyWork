
/**
 * Class information
 * @author Cláudio Coelho
 * @version 1.00
 * @see Album
 * @see Sticker
 */
import java.util.*;
import java.io.*;

public class Client {

  /**
   * If user insert a file not found
   * 
   * @throws FileNotFoundException
   */

  public static void main(String[] args) {
    Album myAlbum = new Album();
    Album[] friendsAlbum;

    try {
      Scanner scanner = new Scanner(new File("input3.txt"));

      myAlbum.setStickerCount(scanner.nextInt());// Method to check User amount of Stickers for the input
      myAlbum.setStickers(myAlbum.getStickerCount()); // Create the Album
      for (int i = 0; i < myAlbum.Stickers().length; i++) {
        myAlbum.addStickers(i, scanner.next(), scanner.nextInt()); // Read the Sticker id and quantity
      }

      myAlbum.setFriends(scanner.nextInt()); // Read the number of Friends
      friendsAlbum = new Album[myAlbum.getFriends()]; // create the Array with the size of friends
      for (int i = 0; i < friendsAlbum.length; i++) { // For each friend create an Album
        friendsAlbum[i] = new Album();
      }

      for (int i = 0; i < friendsAlbum.length; i++) { // For each friend
        friendsAlbum[i].setStickerCount(scanner.nextInt()); // read the number of Stickers
        friendsAlbum[i].setStickers(friendsAlbum[i].getStickerCount()); // Create the Album with the size os the
                                                                        // Stickers number
        for (int j = 0; j < friendsAlbum[i].Stickers().length; j++) { // For each Sticker read the id and
                                                                      // quantity
          friendsAlbum[i].addStickers(j, scanner.next(), scanner.nextInt());
        }
      }

      scanner.close();

      // ? ============================ FIM DOS INPUTS ===============================

      System.out.println("\n===================== OUTPUT =====================\n");

      int totalFriends = friendsAlbum.length;
      System.out.println("Total number of Friends: " + totalFriends);

      for (int i = 0; i < friendsAlbum.length; i++) {
        System.out.print("\n\nFriend:" + (i + 1) + "\nDei: ");
        myAlbum.checkFriend(friendsAlbum);
      }
      myAlbum.stickersNeeded();
      System.out.println("\n==================================================\n");

    } catch (Exception e) {
      System.out.println(e.toString());
    }

  }
}
