package berezkin.giphy.ui.main

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import berezkin.giphy.R
import berezkin.giphy.repo.MainItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.main_item.view.*

class MainViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
  fun bind(item: MainItem?) {
    view.progress.hide()
    view.title.text = item?.title
    val thumbnail = Glide.with(view).load(item?.stillUrl)
    thumbnail.into(view.image)
    view.image.setOnClickListener {
      view.progress.show()
      Glide.with(view).load(item?.url).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
          e: GlideException?,
          model: Any?,
          target: Target<Drawable>?,
          isFirstResource: Boolean
        ): Boolean {
          view.progress.hide()
          return false
        }

        override fun onResourceReady(
          resource: Drawable?,
          model: Any?,
          target: Target<Drawable>?,
          dataSource: DataSource?,
          isFirstResource: Boolean
        ): Boolean {
          view.progress.hide()
          return false
        }
      }).thumbnail(thumbnail).into(view.image)
    }
  }
}

class MainAdapter() : PagedListAdapter<MainItem, MainViewHolder>(comparator) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
    MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false))

  override fun onBindViewHolder(holder: MainViewHolder, position: Int) =
    holder.bind(getItem(position))

  companion object {
    val comparator = object : DiffUtil.ItemCallback<MainItem>() {
      override fun areItemsTheSame(oldItem: MainItem, newItem: MainItem): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: MainItem, newItem: MainItem): Boolean =
        oldItem == newItem
    }
  }
}
