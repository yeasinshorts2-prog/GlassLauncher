package com.glass.launcher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.appsGrid);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        List<AppInfo> apps = loadInstalledApps();
        adapter = new AppAdapter(this, apps);
        recyclerView.setAdapter(adapter);

        EditText searchBox = findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private List<AppInfo> loadInstalledApps() {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo ri : allApps) {
            String pkg = ri.activityInfo.packageName;
            if (!pkg.equals(getPackageName())) {
                String label = ri.loadLabel(pm).toString();
                list.add(new AppInfo(label, pkg, ri.loadIcon(pm)));
            }
        }

        Collections.sort(list, (a, b) -> a.label.compareToIgnoreCase(b.label));
        return list;
    }

    @Override
    public void onBackPressed() {
        // হোম স্ক্রিনে ব্যাক বাটন চাপলে যেন লঞ্চার বন্ধ না হয়
    }
}
