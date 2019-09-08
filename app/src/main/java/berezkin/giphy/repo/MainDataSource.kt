package berezkin.giphy.repo

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import berezkin.giphy.model.GiphyApi
import berezkin.giphy.model.GiphyResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainDataSource : PositionalDataSource<MainItem>() {
  override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<MainItem>) {
    GiphyApi.instance.trending(params.requestedStartPosition, params.requestedLoadSize)
      .enqueue(object : Callback<GiphyResult> {
        override fun onFailure(call: Call<GiphyResult>, t: Throwable) {
          callback.onResult(emptyList(), 0) //  TODO Error handling
        }

        override fun onResponse(call: Call<GiphyResult>, response: Response<GiphyResult>) {
          val items = response.body()?.data.orEmpty().map {
            MainItem(
              it.id,
              it.title,
              stillUrl = it.images.fixedSmallStill.url,
              url = it.images.fixedSmall.url
            )
          }
          val offset = response.body()?.pagination?.offset ?: 0
          val total = response.body()?.pagination?.totalCount ?: 0
          callback.onResult(items, offset, total)
        }
      })
  }

  override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MainItem>) {
    GiphyApi.instance.trending(params.startPosition, params.loadSize)
      .enqueue(object : Callback<GiphyResult> {
        override fun onFailure(call: Call<GiphyResult>, t: Throwable) {
          callback.onResult(emptyList()) //  TODO Error handling
        }

        override fun onResponse(call: Call<GiphyResult>, response: Response<GiphyResult>) {
          val items = response.body()?.data.orEmpty().map {
            MainItem(
              it.id,
              it.title,
              stillUrl = it.images.fixedSmallStill.url,
              url = it.images.fixedSmall.url
            )
          }
          callback.onResult(items)
        }
      })
  }
}

class MainDataSourceFactory : DataSource.Factory<Int, MainItem>() {
  override fun create(): DataSource<Int, MainItem> = MainDataSource()
}
