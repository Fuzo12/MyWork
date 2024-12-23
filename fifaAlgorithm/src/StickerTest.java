import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StickerTest {

  @Test
  @DisplayName("id deve ser uma string")
  public void testGetters() {
    Sticker sticker = new Sticker("QAT1", 0);
    assertEquals("QAT1", sticker.getId());
    assertEquals(0, sticker.getQuantity());
  }
}
