package com.example.sensorapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sensorapp3.R;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    private static final String TAG = "SENSOR_ACTIVITY";
    public static final String KEY_EXTRA_SENSOR_TYPE = "SENSOR_TYPE";
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;

    private SensorAdapter sensorAdapter;

    private boolean subtitleVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        sensorList.forEach((sensor) -> Log.i(TAG,
                "Nazwa: " + sensor.getName() +
                        ", Producent: " + sensor.getVendor() +
                        ", Maksymalna wartość: " + sensor.getMaximumRange()));

        if (sensorAdapter == null) {
            sensorAdapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(sensorAdapter);
        } else {
            sensorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.sensor_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                this.invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle() {
        String subtitle = getString(R.string.sensors_count, this.sensorList.size());
        if (!subtitleVisible) {
            subtitle = null;
        }

        this.getSupportActionBar().setSubtitle(subtitle);
    }

    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private ImageView imageView;

        private Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnClickListener(this);
            this.nameTextView = itemView.findViewById(R.id.sensor_list_item_name);
            this.imageView = itemView.findViewById(R.id.sensor_list_item_image);
        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            this.nameTextView.setText(sensor.getName());
            this.imageView.setImageResource(R.drawable.ic_sensor);
        }



        @Override
        public void onClick(View view) {
            switch (sensor.getType()) {
                case Sensor.TYPE_LIGHT:
                case Sensor.TYPE_RELATIVE_HUMIDITY: {
                    Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    intent.putExtra(KEY_EXTRA_SENSOR_TYPE, sensor.getType());
                    startActivity(intent);
                    break;
                }
                case Sensor.TYPE_MAGNETIC_FIELD: {
                    Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                    startActivity(intent);
                    break;
                }
                default: {
                    break;
                }
            }
        }

    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private List<Sensor> sensors;

        public SensorAdapter(List<Sensor> sensors) {
            this.sensors = sensors;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SensorActivity.this);
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }

}