package berezkin.giphy.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import berezkin.giphy.model.GiphyApi
import berezkin.giphy.model.GiphyResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainDataSource(private val query: String) : PositionalDataSource<MainItem>() {

  val progress = MutableLiveData<Boolean>()

  private fun call(start: Int, size: Int) =
    if (query.isEmpty()) GiphyApi.instance.trending(start, size)
    else GiphyApi.instance.search(query, start, size)

  override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<MainItem>) {
    progress.postValue(true)
    call(params.requestedStartPosition, params.requestedLoadSize)
      .enqueue(object : Callback<GiphyResult> {
        override fun onFailure(call: Call<GiphyResult>, t: Throwable) {
          progress.postValue(false)
          callback.onResult(emptyList(), 0) //  TODO Error handling
        }

        override fun onResponse(call: Call<GiphyResult>, response: Response<GiphyResult>) {
          progress.postValue(false)
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
    progress.postValue(true)
    call(params.startPosition, params.loadSize)
      .enqueue(object : Callback<GiphyResult> {
        override fun onFailure(call: Call<GiphyResult>, t: Throwable) {
          progress.postValue(false)
          callback.onResult(emptyList()) //  TODO Error handling
        }

        override fun onResponse(call: Call<GiphyResult>, response: Response<GiphyResult>) {
          progress.postValue(false)
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

class MainDataSourceFactory(private val query: String) : DataSource.Factory<Int, MainItem>() {
  val dataSource = MutableLiveData<MainDataSource>()
  override fun create(): DataSource<Int, MainItem> {
    val source = MainDataSource(query)
    dataSource.postValue(source)
    return source
  }
}
