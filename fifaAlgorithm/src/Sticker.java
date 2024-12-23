/**
 * Class Album
 * 
 * @author Cláudio Coelho
 * @version 1.00
 * @see Album
 *
 */
public class Sticker {
  String id;
  int quantity;

  Sticker() {
  }

  /**
   * Constructor Sticker
   * 
   * @param id       is the Sticker identification {@link #id}
   * @param quantity is the amount of Stickers {@link #quantity}
   */
  Sticker(String id, int quantity) {
    this.id = id;
    this.quantity = quantity;
  }

  /**
   * Setter for the Sticker id
   * 
   * @param id the {@link #id} to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Getter for Sticker {@link #id}
   * 
   * @return {@link #id}
   */
  public String getId() {
    return id;
  }

  /**
   * Setter for the Sticker quantity
   * 
   * @param quantity the {@link #quantity} to set
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * Getter for Sticker {@link #quantity}
   * 
   * @return {@link #quantity}
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Setter to update the Sticker quantity after trade is made
   * 
   * @param quantity the {@link #quantity} to set
   */
  public void setQuantityMinus1(int quantity) {
    this.quantity -= quantity;
  }

  /**
   * Print Sticker
   * 
   * @return {@link #id} and {@link #quantity}
   */
  public String toString() {
    return this.id + " -> " + this.quantity;
  }

}
