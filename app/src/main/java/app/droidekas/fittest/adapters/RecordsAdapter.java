package app.droidekas.fittest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.droidekas.fittest.R;
import app.droidekas.fittest.models.InactivityHistory;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Satyarth on 29/05/16.
 */
public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordVH> {
    List<InactivityHistory> mList = new ArrayList<>();


    public RecordsAdapter() {
    }

    public void addAll(List<InactivityHistory> data) {
        int start = mList.size();
        mList.addAll(data);
        notifyItemRangeInserted(start, mList.size() - start);
    }

    @Override
    public RecordVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_records_vh, parent, false));
    }

    @Override
    public void onBindViewHolder(RecordVH holder, int position) {
        holder.setUp(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class RecordVH extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_duration)
        TextView duration;
        @Bind(R.id.tv_location_lat)
        TextView locationLat;
        @Bind(R.id.tv_location_long)
        TextView locationLong;

        public RecordVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setUp(InactivityHistory history) {
            duration.setText(String.format(Locale.ENGLISH, "%d hours",
                    (history.getDuration() / (60 * 60))));
            locationLat.setText(String.format(Locale.ENGLISH, "Latitude: %.6f", history.getLocationLat()));
            locationLong.setText(String.format(Locale.ENGLISH, "Longitude: %.6f", history.getLocationLong()));

        }
    }
}
