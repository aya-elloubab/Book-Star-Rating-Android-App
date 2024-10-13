package com.example.starratingsapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.starratingsapp.R;
import com.example.starratingsapp.beans.BookStar;
import com.example.starratingsapp.services.BookStarService;

import java.util.ArrayList;
import java.util.List;

public class BookStarAdapter  extends RecyclerView.Adapter<BookStarAdapter.StarViewHolder>  implements Filterable {
    private static final String TAG = "StarAdapter";
    private List<BookStar> bookStars;
    private List<BookStar> starsFilter;
    private Context context;
    private NewFilter mfilter;
    public BookStarAdapter(Context context, List<BookStar> bookStars) {
        this.bookStars = bookStars;
        this.starsFilter = new ArrayList<>(bookStars);
        this.context = context;
        this.mfilter = new NewFilter(this);
    }
    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.star_item, viewGroup, false);
        final StarViewHolder holder = new StarViewHolder(v);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null,
                        false);
                final ImageView img = popup.findViewById(R.id.img);
                final RatingBar bar = popup.findViewById(R.id.ratingBar);
                final TextView idss = popup.findViewById(R.id.idss);
                Bitmap bitmap =
                        ((BitmapDrawable)((ImageView)v.findViewById(R.id.img)).getDrawable()).getBitmap();
                img.setImageBitmap(bitmap);
                bar.setRating(((RatingBar)v.findViewById(R.id.stars)).getRating());
                idss.setText(((TextView)v.findViewById(R.id.ids)).getText().toString());
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Notez : ")
                        .setMessage("Donner une note entre 1 et 5 :")
                        .setView(popup)
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                float s = bar.getRating();
                                int ids = Integer.parseInt(idss.getText().toString());
                                BookStar star = BookStarService.getInstance().findById(ids);
                                star.setStar(s);
                                BookStarService.getInstance().update(star);
                                notifyItemChanged(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .setNeutralButton("Supprimer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int ids = Integer.parseInt(idss.getText().toString());

                                // Find the BookStar object by its ID
                                BookStar star = BookStarService.getInstance().findById(ids);

                                // Pass the BookStar object to the delete method
                                if (star != null) {
                                    BookStarService.getInstance().delete(star);
                                }

                                // Remove the item from the RecyclerView
                                notifyItemRemoved(holder.getBindingAdapterPosition());
                            }

                        })
                        .create();
                dialog.show();

            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull StarViewHolder starViewHolder, int i) {
        if (starsFilter != null && !starsFilter.isEmpty() && starsFilter.get(i) != null) {
            Log.d(TAG, "onBindView call ! " + i);

            // Load image using Glide, ensure the URL is valid
            String imageUrl = starsFilter.get(i).getImg();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(context)
                        .asBitmap()
                        .load(imageUrl)
                        .apply(new RequestOptions().override(100, 100))
                        .into(starViewHolder.img);
            }

            starViewHolder.name.setText(starsFilter.get(i).getName().toUpperCase());
            starViewHolder.stars.setRating(starsFilter.get(i).getStar());
            starViewHolder.idss.setText(String.valueOf(starsFilter.get(i).getId()));
        }
    }

    @Override
    public int getItemCount() {
        return starsFilter.size();
    }
    @Override
    public Filter getFilter() {
        if (mfilter == null) {
            mfilter = new NewFilter(this);  // Initialize mfilter if it's null
        }
        return mfilter;
    }

    public class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        TextView name;
        RatingBar stars;
        RelativeLayout parent;
        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
            parent = itemView.findViewById(R.id.parent);
        }
    }
    public class NewFilter extends Filter {
        public RecyclerView.Adapter mAdapter;
        public NewFilter(RecyclerView.Adapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            final FilterResults results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                starsFilter = new ArrayList<>(bookStars);  // Reset to original list
            } else {
                List<BookStar> filteredList = new ArrayList<>();
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (BookStar bookStar : bookStars) {
                    if (bookStar.getName().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(bookStar);
                    }
                }
                starsFilter = filteredList;  // Update starsFilter with filtered list
            }

            results.values = starsFilter;
            results.count = starsFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter = (List<BookStar>) filterResults.values;
            notifyDataSetChanged();  // This will refresh the RecyclerView
        }

    }

}
