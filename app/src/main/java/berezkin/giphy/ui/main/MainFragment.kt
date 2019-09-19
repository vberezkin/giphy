package berezkin.giphy.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import berezkin.giphy.R
import berezkin.giphy.repo.MainItem
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

  companion object {
    fun newInstance() = MainFragment()
  }

  private val adapter = MainAdapter()
  private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.main_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    list.layoutManager = LinearLayoutManager(context)
    list.adapter = adapter

    search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean = false

      override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("MainFragment", "onQueryTextChange $newText")
        viewModel.query.value = newText ?: ""
        return true
      }
    })

    viewModel.items.observe(this, Observer<PagedList<MainItem>> { adapter.submitList(it) })
    viewModel.progress.observe(this, Observer<Boolean> { adapter.setProgress(it) })
  }

  override fun onResume() {
    super.onResume()
    viewModel.retry() //  TODO Use ConnectivityManager
  }
}
