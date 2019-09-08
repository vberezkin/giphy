package berezkin.giphy

import berezkin.giphy.model.GiphyApi
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun trending() {
    val resp = GiphyApi.instance.trending(100000,20).execute()
    assertEquals(4, 2 + 2)
  }

  @Test
  fun search() {
    val resp = GiphyApi.instance.search("cat", 1000,20).execute()
    assertEquals(4, 2 + 2)
  }
}
