package berezkin.giphy.model

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class GiphyResult(
  @SerializedName("data") val data: Array<DataItem>,
  @SerializedName("pagination") val pagination: Pagination
)

class Pagination(
  @SerializedName("offset") val offset: Int,
  @SerializedName("count") val count: Int,
  @SerializedName("total_count") val totalCount: Int
)

class DataItem(
  @SerializedName("id") val id: String,
  @SerializedName("title") val title: String,
  @SerializedName("images") val images: Images
)

class Images(
  @SerializedName("fixed_height_small") val fixedSmall: ImageItem,
  @SerializedName("fixed_height_small_still") val fixedSmallStill: ImageItem
)

class ImageItem(@SerializedName("url") val url: String)

interface GiphyApi {
  @GET("/v1/gifs/trending")
  fun trending(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<GiphyResult>

  companion object {
    private val apiKey = "lIfCpkskRp42stFmyoTMmZITeZ99gWsw"
    private val client = OkHttpClient.Builder().addInterceptor {
      val req = it.request()
      val url = req.url.newBuilder().addQueryParameter("api_key", apiKey).build()
      val resp = it.proceed(req.newBuilder().url(url).build())
      resp
    }.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
      .build()
    val instance = Retrofit.Builder().baseUrl("https://api.giphy.com")
      .addConverterFactory(GsonConverterFactory.create()).client(client).build()
      .create(GiphyApi::class.java)
  }
}
