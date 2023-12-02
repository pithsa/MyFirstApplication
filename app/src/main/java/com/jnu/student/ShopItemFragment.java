package com.jnu.student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.student.data.DataBank;
import com.jnu.student.data.ShopItem;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopItemFragment extends Fragment {
    private ArrayList<ShopItem> shopItems = new ArrayList<ShopItem>();
    CustomAdapter customAdapter;
    DataBank dataBank = new DataBank();

    ActivityResultLauncher<Intent> addItemLauncher = registerForActivityResult(//添加页面启动器
            new ActivityResultContracts.StartActivityForResult(),
            result-> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;

                    String name = data.getStringExtra("name");
                    String price = data.getStringExtra("price");

                    assert price != null;
                    double price_double = Double.parseDouble(price);

                    shopItems.add(new ShopItem(name, R.drawable.book_no_name, price_double));
                    customAdapter.notifyItemInserted(shopItems.size());

                    dataBank.SaveShopItems(this.requireContext(), shopItems);
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                }
            });

    ActivityResultLauncher<Intent> updateItemLauncher = registerForActivityResult(//修改页面启动器
            new ActivityResultContracts.StartActivityForResult(),
            result->{
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    assert data != null;

                    String name = data.getStringExtra("name");
                    String price = data.getStringExtra("price");
                    assert price != null;
                    double price_double = Double.parseDouble(price);
                    int pos = data.getIntExtra("position",0);

                    ShopItem shopItem = shopItems.get(pos);
                    shopItem.setPrice(price_double);
                    shopItem.setName(name);
                    customAdapter.notifyItemChanged(pos);

                    dataBank.SaveShopItems(this.requireContext(),shopItems);
                }
                else if(result.getResultCode()==Activity.RESULT_CANCELED){

                }
            });



    public ShopItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory meth    od to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ShopItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopItemFragment newInstance() {
        ShopItemFragment fragment = new ShopItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shop_item, container, false);

        RecyclerView mainRecyclerView = rootView.findViewById(R.id.recyclerview_main);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        shopItems = dataBank.LoadShopItems(this.requireContext());//加载数据

        if(shopItems.size()==0){//如果一开始没有数据就塞给他三个
            shopItems.add(new ShopItem("软件项目管理案例教程(第四版)",R.drawable.book_1,30));
            shopItems.add(new ShopItem("信息安全数学基础(第二版)",R.drawable.book_2,20));
            shopItems.add(new ShopItem("创新工程实践",R.drawable.book_no_name,40));
        }

        customAdapter = new CustomAdapter(shopItems);
        mainRecyclerView.setAdapter(customAdapter);

        registerForContextMenu(mainRecyclerView);//注册
        return rootView;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){//重载一个相应菜单事件的函数
        Toast.makeText(this.getContext(),"clicked"+item.getOrder(),Toast.LENGTH_SHORT).show();
        switch(item.getItemId()){
            case 0://添加
                Intent intent = new Intent(ShopItemFragment.this.getContext(), ShopItemDetailsActivity.class);
                addItemLauncher.launch(intent);
                break;
            case 1://删除
                AlertDialog.Builder builder = new AlertDialog.Builder(this.requireContext());
                builder.setTitle("删除");
                builder.setMessage("确定删除当前数据");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shopItems.remove(item.getOrder());
                        customAdapter.notifyItemRemoved(item.getOrder());

                        new DataBank().SaveShopItems(ShopItemFragment.this.requireContext(),shopItems);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
            case 2://修改
                Intent intentUpdate = new Intent(ShopItemFragment.this.getContext(), ShopItemDetailsActivity.class);
                ShopItem shopItem = shopItems.get(item.getOrder());
                intentUpdate.putExtra("name",shopItem.getName());
                intentUpdate.putExtra("price",shopItem.getPrice());
                intentUpdate.putExtra("position",item.getOrder());
                updateItemLauncher.launch(intentUpdate);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
}

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final  ArrayList<ShopItem> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final TextView itemName;
        private final TextView itemPrice;
        private final ImageView itemImage;


        public ViewHolder(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View
            itemName =  itemView.findViewById(R.id.itemname);
            itemPrice = itemView.findViewById(R.id.itemprice);
            itemImage =  itemView.findViewById(R.id.imageView);
            itemView.setOnCreateContextMenuListener(this);//然后调用
        }

        public TextView getItemName() {
            return itemName;
        }

        public TextView getItemPrice() {
            return itemPrice;
        }

        public ImageView getItemImage() {
            return itemImage;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("具体操作");
            menu.add(0,0,this.getAdapterPosition(),"添加"+this.getAdapterPosition());
            menu.add(0,1,this.getAdapterPosition(),"删除"+this.getAdapterPosition());
            menu.add(0,2,this.getAdapterPosition(),"修改"+this.getAdapterPosition());
        }
    }

    public CustomAdapter(ArrayList<ShopItem> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_display, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getItemName().setText(localDataSet.get(position).getName());
        viewHolder.getItemPrice().setText(localDataSet.get(position).getPrice()+"元");
        viewHolder.getItemImage().setImageResource(localDataSet.get(position).getImageId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}