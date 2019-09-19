package berezkin.giphy.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

data class Listing(
  val pagedList: LiveData<PagedList<MainItem>>,
  val progress: LiveData<Boolean>,
  val retry: () -> Unit
)

class MainRepository {
  fun items(query: String, pageSize: Int): Listing {
    val factory = MainDataSourceFactory(query)
    return Listing(
      LivePagedListBuilder(factory, Config(pageSize, enablePlaceholders = false)).build(),
      Transformations.switchMap(factory.dataSource) { it.progress }) { factory.dataSource.value?.retryFailed() }
  }
}
