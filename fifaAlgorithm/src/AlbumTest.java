import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class AlbumTest {

  @Test
  public void testStickerCount() {
    Album album = new Album();
    album.setStickerCount(320);
    assertEquals(320, album.getStickerCount());
  }

  @Test
  public void testStickers() {
    Album album = new Album();
    album.setStickers(320);
    assertEquals(320, album.Stickers().length);
  }

  @Test
  public void testFriends() {
    Album album = new Album();
    album.setFriends(5);
    assertEquals(5, album.getFriends());
  }

}
