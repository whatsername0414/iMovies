package com.example.imovies.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imovies.R
import com.example.imovies.common.Extensions.convertToCurrency
import com.example.imovies.common.Extensions.loadImage
import com.example.imovies.common.setSingleClickListener
import com.example.imovies.data.model.movie.Movie
import com.example.imovies.databinding.AdapterMovieBinding

class MovieDiffUtil: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }

}

class MovieAdapter: ListAdapter<Movie, MovieViewHolder>(MovieDiffUtil()) {

    var onMovieClicked: ((Movie) -> Unit)? = null
    var onFavoriteClicked: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            AdapterMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.binding.apply {
            favoriteButton.isChecked = movie.isFavorite
            artworkImageView.loadImage(movie.artwork)
            titleTextView.text = movie.title
            releaseAndGenreTextView.text = holder.itemView.context.getString(
                R.string.movie_adapter_release_year_and_genre, movie.releaseYear, movie.genre)
            priceTextView.text = movie.currency.convertToCurrency(movie.price)

            favoriteButton.setSingleClickListener {
                onFavoriteClicked?.invoke(movie)
            }
            holder.itemView.setSingleClickListener {
                onMovieClicked?.invoke(movie)
            }
        }
    }
}

class MovieViewHolder(val binding: AdapterMovieBinding): RecyclerView.ViewHolder(binding.root)