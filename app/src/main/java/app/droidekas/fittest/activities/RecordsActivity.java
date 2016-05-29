package app.droidekas.fittest.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import app.droidekas.fittest.R;
import app.droidekas.fittest.adapters.RecordsAdapter;
import app.droidekas.fittest.models.InactivityHistory;
import app.droidekas.fittest.tasks.Crunch;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RecordsActivity extends AppCompatActivity {

    @Bind(R.id.rv_records)
    RecyclerView rvRecords;

    RecordsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        ButterKnife.bind(this);
        rvRecords.setLayoutManager(new LinearLayoutManager(this));
        rvRecords.setAdapter(mAdapter = new RecordsAdapter());
        new Crunch(() ->
                mAdapter.addAll(InactivityHistory.listAll(InactivityHistory.class)))
                .execute(this);
    }


}
