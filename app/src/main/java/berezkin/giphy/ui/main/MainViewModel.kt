package berezkin.giphy.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import berezkin.giphy.repo.MainRepository
import berezkin.giphy.rx.debounce

class MainViewModel : ViewModel() {
  private val repo = MainRepository()
  val query = MutableLiveData<String>().apply { value = "" }
  private val repoResult = Transformations.map(query.debounce(500)) { repo.items(it, 20) }
  val items = Transformations.switchMap(repoResult) { it.pagedList }
  val progress = Transformations.switchMap(repoResult) { it.progress }
}
