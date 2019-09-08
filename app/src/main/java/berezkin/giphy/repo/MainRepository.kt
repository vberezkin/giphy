package berezkin.giphy.repo

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.Config

class MainRepository {
  fun items(pageSize: Int): LiveData<PagedList<MainItem>> {
    val factory = MainDataSourceFactory()
    return LivePagedListBuilder(factory, Config(pageSize, enablePlaceholders = false)).build()
  }
}
