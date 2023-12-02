package com.jnu.student;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.data.DataBank;
import com.jnu.student.data.ShopItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public class PagerViewFragmentAdapter extends FragmentStateAdapter {

        public PagerViewFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position){
                case 0:
                    return ShopItemFragment.newInstance();
                case 1:
                    return TencentMapFragment.newInstance();
                case 2:
                    return BrowserFragment.newInstance();
            }
            return ShopItemFragment.newInstance();
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager2_main);
        viewPager2.setAdapter(new PagerViewFragmentAdapter(getSupportFragmentManager(),getLifecycle()));

        TabLayout tabLayout = findViewById(R.id.tablayout_header);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Book Shopping");
                        break;
                    case 1:
                        tab.setText("TencentMap");
                        break;
                    case 2:
                        tab.setText("News Browser");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }

}