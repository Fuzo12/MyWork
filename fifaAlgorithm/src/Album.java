/**
 * Class information
 * 
 * @author Cláudio Coelho
 * @version 1.00
 * @see Sticker
 *
 */
public class Album {
  private int friendCount;
  private int stickerCount;
  private int stickerIndex = 0;
  // private Sticker sticker;
  private Sticker[] stickers;

  Album() {
  }

  /**
   * Constructor for Friends Album
   * 
   * @param stickers is the array of Stickers {@link #stickers}
   */
  Album(Sticker[] stickers) {
    this.stickers = stickers;
  }

  /**
   * Setter for the Sticker Count
   * 
   * @param stickerCount the {@link #stickerCount} to set
   */
  public void setStickerCount(int stickerCount) {
    this.stickerCount = stickerCount;
  }

  /**
   * Getter for the Sticker Count {@link #stickerCount}
   * 
   * @return {@link #stickerCount}
   */
  public int getStickerCount() {
    return this.stickerCount;
  }

  /**
   * Setter for the Stickers Array
   * 
   * @param stickerCount the {@link #stickerCount} to set
   *                     stickers the {@link #stickers} to set
   */
  public void setStickers(int stickerCount) {
    this.stickerCount = stickerCount;
    this.stickers = new Sticker[this.stickerCount];
  }

  /**
   * Getter for the Sticker Array {@link #stickers}
   * 
   * @return {@link #stickers}
   */
  public Sticker[] Stickers() {
    return this.stickers;
  }

  /**
   * Method to add Sticker to Array of Stickers
   * 
   * @param stickerIndex the {@link #stickerIndex} to set
   * @param id           ot the Sticker to set
   * @param quantity     of the Sticker to set
   * @see Sticker
   */
  public void addStickers(int stickerIndex, String id, int quantity) {
    this.stickerIndex = stickerIndex;
    this.stickers[this.stickerIndex] = new Sticker(id, quantity);
  }

  /**
   * Setter for the Stickers Array
   * 
   * @param friendCount the {@link #friendCount} to set
   */
  public void setFriends(int friendCount) {
    this.friendCount = friendCount;
  }

  /**
   * Getter for the Friends Count {@link #friendCount}
   * 
   * @return {@link #friendCount}
   */
  public int getFriends() {
    return this.friendCount;
  }

  /**
   * Method to check possible trades and creates an array with the size of trade
   * possibilities
   * 
   * @param friendsAlbum is the Friends Album to check
   *                     tempArray is a temporary Array to receive the number of
   *                     possible trades. The array size is calculated by the
   *                     minimum trades.
   *                     counter1 is a counter to receive the amount of possible
   *                     trades for the User (user = 0 && friend > 1)
   *                     counter2 is a counter to receive the amount of possible
   *                     trades for the Friend (friend = 0 && user > 1)
   * @return tempArray
   */
  public int[] checkAmountOfPossibleTrades(Album[] friendsAlbum) {
    int[] tempArray = new int[friendsAlbum.length];
    for (int i = 0; i < friendsAlbum.length; i++) {
      int counter1 = checkMyStickersToTrade(this, friendsAlbum[i]);
      int counter2 = checkMyStickersToTrade(friendsAlbum[i], this);
      // algorithm that assigns the size to the temporary array to the minimum number
      // of possible trades
      if (counter1 > counter2)
        tempArray[i] = counter2;
      else
        tempArray[i] = counter1;
    }
    return tempArray;
  }

  /**
   * Method to check on each Album the possible trades for the User && Friends
   * 
   * @param secondAlbum is the Album B for friend album
   * @param firstAlbum  is the Album A for friend album
   *                    counter1 is a counter to receive the amount of possible
   *                    trades for the User (user = 0 && friend > 1)
   * @return counter1 with the amount of trades
   */
  public int checkMyStickersToTrade(Album firstAlbum, Album secondAlbum) {
    int tradeCount = 0;
    for (int i = 0; i < firstAlbum.stickers.length; i++) {
      if (firstAlbum.stickers[i].getQuantity() == 0 && secondAlbum.stickers[i].getQuantity() > 1) {
        tradeCount++;
      }
    }
    return tradeCount;
  }

  /**
   * Method to check Friend Album and gives the number os available trades and
   * make the trade
   */
  public void checkFriend(Album friendsAlbum[]) {
    int bigger = 0;
    int[] tempArray = checkAmountOfPossibleTrades(friendsAlbum);
    for (int i = 0; i < tempArray.length; i++) {
      if (tempArray[i] > tempArray[bigger])
        bigger = i;
    }
    // makes the trade
    makeTrade(friendsAlbum[bigger], this, tempArray[bigger]);
    System.out.print("\nObtive: ");
    makeTrade(this, friendsAlbum[bigger], tempArray[bigger]);
  }

  /**
   * Method that makes the trade, adding and removing the Stickers quantity
   * 
   * @param tradeCounter is the number of trades available
   * @param firstAlbum   is the first Album to check
   * @param secondAlbum  is the second Album to check
   */
  public void makeTrade(Album firstAlbum, Album secondAlbum, int tradeCounter) {
    for (int k = 0; k < firstAlbum.stickers.length; k++) {
      if (firstAlbum.stickers[k].getQuantity() == 0 && secondAlbum.stickers[k].getQuantity() > 1
          && tradeCounter > 0) {
        firstAlbum.stickers[k].quantity++;
        secondAlbum.stickers[k].quantity--;
        tradeCounter--;
        System.out.print(secondAlbum.stickers[k].getId() + " ");
      }

    }
  }

  /**
   * Method to Print User Album
   * 
   * 
   */
  public void printAlbum() {
    // print User stickers
    for (int i = 0; i < this.stickers.length; i++) {
      System.out.println(this.stickers[i].toString());
    }
  }

  /**
   * Method to Print Friends Album
   * 
   * @param friendsAlbum is the Friends Album
   */
  public void printFriendsAlbum(Album[] friendsAlbum) {
    System.out.println("\nFriends Stickers:\n");
    for (int i = 0; i < friendsAlbum.length; i++) {
      System.out.println("Friend: " + (i + 1));
      for (int j = 0; j < friendsAlbum[i].stickers.length; j++)
        System.out.println(friendsAlbum[i].stickers[j]);
      System.out.println();
    }
  }

  /**
   * Method to Print the Stickers that the User needs
   * 
   * @param myAlbum is the User Album
   */
  public void stickersNeeded() {
    System.out.print("\n\nStickers em falta: ");
    for (int i = 0; i < this.stickers.length; i++) {
      if (this.stickers[i].getQuantity() == 0)
        System.out.print(this.stickers[i].getId() + " ");
    }
  }

  /**
   * Method to Print the User Stickers to trade
   * 
   * @param myAlbum is the User Album
   */
  public void myStickersToGive(Album myAlbum) {
    System.out.print("\nStickers to trade: ");
    for (int i = 0; i < myAlbum.stickers.length; i++) {
      if (myAlbum.stickers[i].getQuantity() > 1)
        System.out.print(myAlbum.stickers[i].getId() + " ");
    }
    System.out.println();
  }

  public String toString() {
    return "Sticker:" + stickers + "";
  }

}
