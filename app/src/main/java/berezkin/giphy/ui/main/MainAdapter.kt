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

class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)

class MainAdapter : PagedListAdapter<MainItem, RecyclerView.ViewHolder>(comparator) {
  private var progress = false

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    return when (viewType) {
      R.layout.main_item -> MainViewHolder(view)
      R.layout.progress_item -> ProgressViewHolder(view)
      else -> throw IllegalArgumentException("unknown view type $viewType")
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
    is MainViewHolder -> holder.bind(getItem(position))
    else -> {
    }
  }

  override fun getItemCount(): Int = super.getItemCount() + if (progress) 1 else 0

  override fun getItemViewType(position: Int): Int =
    if (progress && position == itemCount - 1) R.layout.progress_item else R.layout.main_item

  fun setProgress(value: Boolean) {
    val old = progress
    progress = value
    if (!old && value) notifyItemInserted(super.getItemCount())
    else if (old && !value) notifyItemRemoved(super.getItemCount())
  }

  companion object {
    val comparator = object : DiffUtil.ItemCallback<MainItem>() {
      override fun areItemsTheSame(oldItem: MainItem, newItem: MainItem): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: MainItem, newItem: MainItem): Boolean =
        oldItem == newItem
    }
  }
}
