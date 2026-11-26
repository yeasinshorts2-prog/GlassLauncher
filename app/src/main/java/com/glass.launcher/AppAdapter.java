package com.glass.launcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private final Context context;
    private List<AppInfo> appList;
    private final List<AppInfo> fullList;

    public AppAdapter(Context context, List<AppInfo> appList) {
        this.context = context;
        this.appList = new ArrayList<>(appList);
        this.fullList = new ArrayList<>(appList);
    }

    public void filter(String query) {
        appList.clear();
        if (query.isEmpty()) {
            appList.addAll(fullList);
        } else {
            String lower = query.toLowerCase();
            for (AppInfo app : fullList) {
                if (app.label.toLowerCase().contains(lower)) {
                    appList.add(app);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo app = appList.get(position);
        holder.appName.setText(app.label);
        holder.appIcon.setImageDrawable(app.icon);

        holder.itemView.setOnClickListener(v -> {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (launchIntent != null) {
                context.startActivity(launchIntent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + app.packageName));
            context.startActivity(intent);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
        }
    }
}
