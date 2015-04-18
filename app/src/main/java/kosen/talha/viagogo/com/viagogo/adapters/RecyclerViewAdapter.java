/*
 * Copyright (C) ${YEAR} Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kosen.talha.viagogo.com.viagogo.adapters;

import android.content.Context;
        import android.os.Handler;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import org.json.JSONArray;
        import org.json.JSONException;

        import java.util.List;

        import kosen.talha.viagogo.com.viagogo.R;
        import kosen.talha.viagogo.com.viagogo.core.Constants;
        import kosen.talha.viagogo.com.viagogo.pojos.CountryModal;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private List<CountryModal> items;
    private OnItemClickListener onItemClickListener;
    private Context context;
    public RecyclerViewAdapter(Context contex,List<CountryModal> items) {
        this.items = items;
        this.context = contex;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {

        holder.text.setText(items.get(position).getLocalizedName());
        holder.population.setText(items.get(position).getPopulation().toString()+"");
        holder.region.setText(items.get(position).getRegion().toString()+"");

        holder.image.setImageBitmap(null);
        Picasso.with(holder.image.getContext()).load(String.format(Constants.FLAG_URL, items.get(position).getAlpha2Code().toLowerCase())).into(holder.image);
        holder.itemView.setTag(items.get(position));
    }

    @Override public int getItemCount() {
        return items.size();
    }

    @Override public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    onItemClickListener.onItemClick(v, (CountryModal) v.getTag());
                }
            }, 200);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;
        public TextView region;
        public TextView population;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            region = (TextView) itemView.findViewById(R.id.text_region);
            population = (TextView) itemView.findViewById(R.id.text_population);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, CountryModal viewModel);

    }
}
