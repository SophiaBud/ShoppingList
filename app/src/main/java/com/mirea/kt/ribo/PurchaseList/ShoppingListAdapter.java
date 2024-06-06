package com.mirea.kt.ribo.PurchaseList;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mirea.kt.ribo.PurchaseList.database.AppDatabase;
import com.mirea.kt.ribo.PurchaseList.database.Product;
import com.mirea.kt.ribo.PurchaseList.database.ShoppingList;
import com.mirea.kt.ribo.PurchaseList.database.ShoppingListDao;

import java.util.List;
import java.util.Locale;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private final List<ShoppingList> shoppingLists;
    private final ShoppingListDao shoppingListDao;
    private final Context context;

    public ShoppingListAdapter(List<ShoppingList> shoppingLists, ShoppingListDao shoppingListDao, Context context) {
        this.shoppingLists = shoppingLists;
        this.shoppingListDao = shoppingListDao;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        holder.textViewShoppingListName.setText(shoppingList.name);

        holder.buttonShareList.setOnClickListener(v -> shareShoppingList(shoppingList));
        holder.buttonRenameList.setOnClickListener(v -> editShoppingList(shoppingList, position));

        holder.buttonDeleteShoppingList.setOnClickListener(v -> {
            shoppingListDao.delete(shoppingList);
            shoppingLists.remove(position);
            notifyItemRemoved(position);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra("SHOPPING_LIST_ID", shoppingList.id);
            context.startActivity(intent);
        });
    }

    private void shareShoppingList(ShoppingList shoppingList) {
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "shopping-list-db").allowMainThreadQueries().build();
        List<Product> products = db.productDao().getProductsByListId(shoppingList.id);

        StringBuilder shareContent = new StringBuilder();
        shareContent.append("Список покупок: ").append(shoppingList.name).append("\n\n");
        for (Product product : products) {
            Locale Locale = new Locale("ru");
            shareContent.append(product.name).append(": ")
                    .append(product.quantity).append(" ").append(product.unit)
                    .append(", ").append(String.format(Locale,"%.2f ₽", product.price)).append("\n");
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent.toString());
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Поделиться списком"));
    }
    private void editShoppingList(ShoppingList shoppingList, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Редактировать название списка");

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(shoppingList.name);
        builder.setView(input);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String newName = input.getText().toString();
            if (!newName.isEmpty()) {
                AppDatabase db = Room.databaseBuilder(context,
                        AppDatabase.class, "shopping-list-db").allowMainThreadQueries().build();
                shoppingList.name = newName;
                db.shoppingListDao().update(shoppingList);
                notifyItemChanged(position);
                Toast.makeText(context, "Название списка изменено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Название не может быть пустым", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewShoppingListName;
        public ImageButton buttonDeleteShoppingList;
        public ImageButton buttonShareList;
        public ImageButton buttonRenameList;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewShoppingListName = itemView.findViewById(R.id.textViewShoppingListName);
            buttonDeleteShoppingList = itemView.findViewById(R.id.buttonDeleteShoppingList);
            buttonShareList = itemView.findViewById(R.id.buttonShareList);
            buttonRenameList = itemView.findViewById(R.id.buttonRenameShoppingList);
        }
    }
}
