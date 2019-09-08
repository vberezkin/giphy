package berezkin.giphy.ui.main

import androidx.lifecycle.ViewModel
import berezkin.giphy.repo.MainRepository

class MainViewModel : ViewModel() {
  val repo = MainRepository()
  val items = repo.items(20)
}
